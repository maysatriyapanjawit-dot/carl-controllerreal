package com.example.carl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.util.Locale;

/**
 * Single-player educational prop controller. Requires cheats / permission level 2.
 * It sends normal vanilla commands: no file access, networking, or native code.
 */
public final class CarlController implements ClientModInitializer {
    private boolean lastC, lastDelete;
    private boolean spawned = false;
    private int cooldown = 0;
    private double x, y, z;

    @Override public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void tick(Minecraft mc) {
        if (mc.player == null || mc.level == null || mc.screen != null) {
            lastC = false; lastDelete = false; return;
        }
        long window = mc.getWindow().getWindow();
        boolean c = key(window, GLFW.GLFW_KEY_C);
        boolean del = key(window, GLFW.GLFW_KEY_DELETE);
        if (c && !lastC) {
            // Put Carl ahead of the player at eye level, so filming is easy.
            x = mc.player.getX() + 2.0;
            y = mc.player.getY() + 1.0;
            z = mc.player.getZ();
            send(mc, "kill @e[type=minecraft:block_display,tag=carl,limit=1]");
            send(mc, String.format(Locale.ROOT,
                "summon minecraft:block_display %.2f %.2f %.2f {Tags:[\"carl\"],block_state:{Name:\"minecraft:coal_block\"}}", x,y,z));
            spawned = true;
            mc.player.sendSystemMessage(Component.literal("Carl spawned! Arrows: move | Page Up/Down: height | Delete: remove"));
        }
        if (del && !lastDelete) {
            send(mc, "kill @e[type=minecraft:block_display,tag=carl,limit=1]");
            spawned = false;
        }
        lastC = c; lastDelete = del;
        if (!spawned) return;
        if (cooldown-- > 0) return;
        double step = 0.25;
        boolean moved = false;
        if (key(window,GLFW.GLFW_KEY_LEFT)) { x -= step; moved = true; }
        if (key(window,GLFW.GLFW_KEY_RIGHT)) { x += step; moved = true; }
        if (key(window,GLFW.GLFW_KEY_UP)) { z -= step; moved = true; }
        if (key(window,GLFW.GLFW_KEY_DOWN)) { z += step; moved = true; }
        if (key(window,GLFW.GLFW_KEY_PAGE_UP)) { y += step; moved = true; }
        if (key(window,GLFW.GLFW_KEY_PAGE_DOWN)) { y -= step; moved = true; }
        if (moved) {
            send(mc, String.format(Locale.ROOT,
               "tp @e[type=minecraft:block_display,tag=carl,limit=1] %.2f %.2f %.2f",x,y,z));
            cooldown = 2; // limit packets/commands while held
        }
    }
    private static boolean key(long w, int code) { return GLFW.glfwGetKey(w,code) == GLFW.GLFW_PRESS; }
    private static void send(Minecraft mc,String command) { mc.player.connection.sendCommand(command); }
}
