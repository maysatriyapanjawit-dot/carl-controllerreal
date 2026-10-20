package com.example.carl;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.Locale;

/** Client-only educational prop controller; requires command permission. */
public final class CarlController implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
        Identifier.fromNamespaceAndPath("carlcontroller", "controls"));
    private static KeyMapping spawn, remove, left, right, forward, backward, up, down;
    private boolean spawned;
    private int cooldown;
    private double x, y, z;

    private static KeyMapping bind(String action, int key) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.carlcontroller." + action, InputConstants.Type.KEYBOARD, key, CATEGORY));
    }

    @Override
    public void onInitializeClient() {
        spawn = bind("spawn", InputConstants.KEY_C);
        remove = bind("remove", InputConstants.KEY_DELETE);
        left = bind("left", InputConstants.KEY_LEFT);
        right = bind("right", InputConstants.KEY_RIGHT);
        forward = bind("forward", InputConstants.KEY_UP);
        backward = bind("backward", InputConstants.KEY_DOWN);
        up = bind("up", InputConstants.KEY_PAGEUP);
        down = bind("down", InputConstants.KEY_PAGEDOWN);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void tick(Minecraft mc) {
        if (mc.player == null || mc.level == null || mc.gui.screen() != null) return;

        if (spawn.consumeClick()) {
            x = mc.player.getX() + 2.0;
            y = mc.player.getY() + 1.0;
            z = mc.player.getZ();
            send(mc, "kill @e[type=minecraft:block_display,tag=carl,limit=1]");
            send(mc, String.format(Locale.ROOT,
                "summon minecraft:block_display %.2f %.2f %.2f {Tags:[\"carl\"],block_state:{id:\"minecraft:coal_block\"}}",
                x, y, z));
            spawned = true;
            mc.player.sendSystemMessage(Component.literal(
                "Carl spawned! Arrow keys: move; Page Up/Down: height; Delete: remove"));
        }
        if (remove.consumeClick()) {
            send(mc, "kill @e[type=minecraft:block_display,tag=carl,limit=1]");
            spawned = false;
        }
        if (!spawned || cooldown-- > 0) return;
        double step = 0.25;
        boolean moved = false;
        if (left.isDown()) { x -= step; moved = true; }
        if (right.isDown()) { x += step; moved = true; }
        if (forward.isDown()) { z -= step; moved = true; }
        if (backward.isDown()) { z += step; moved = true; }
        if (up.isDown()) { y += step; moved = true; }
        if (down.isDown()) { y -= step; moved = true; }
        if (moved) {
            send(mc, String.format(Locale.ROOT,
                "tp @e[type=minecraft:block_display,tag=carl,limit=1] %.2f %.2f %.2f", x, y, z));
            cooldown = 2;
        }
    }
    private static void send(Minecraft mc, String cmd) {
        mc.player.connection.sendCommand(cmd);
    }
}
