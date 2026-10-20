CARL CONTROLLER — Minecraft Java 26.3 — GitHub Auto-Build

This archive contains SOURCE CODE (not a compiled JAR). It includes a GitHub Actions workflow to build the JAR in the cloud (Java 25 + Gradle 9.7.1), without installing local build tools.

HOW TO BUILD USING ONLY GITHUB.COM
1. Sign in to github.com and create a NEW PUBLIC repository (e.g. carl-controller). Do not enable "Add a README".
2. Choose "uploading an existing file" and upload the CONTENTS of this ZIP (not the ZIP itself). IMPORTANT: the hidden .github folder and its build.yml file must also upload. If GitHub browser won't let you upload the .github folder, create .github/workflows/build.yml in GitHub's file editor and paste the contents from this ZIP.
3. Commit the files.
4. Open the Actions tab, choose "Build Carl Mod JAR", and select "Run workflow" (or open the workflow run triggered automatically by your upload).
5. On success, open the completed run and download artifact "Carl-Controller-26.3-JAR". Unzip that artifact to retrieve carl-controller-1.0.1.jar.
6. Put JAR into %appdata%\.minecraft\mods on Windows, using the Minecraft 26.3 Fabric profile. You also need Fabric API 26.3.
7. Put Carl_Smiley_Coal_26.3.zip (separate download) into %appdata%\.minecraft\resourcepacks. Enable it in-game.
8. Open a single-player world with cheats enabled. C: spawn Carl. Arrow keys: move Carl along world axes. Page Up/Down: adjust height. Delete: remove Carl.

IMPORTANT: The build has NOT been run in this environment. If GitHub Actions shows a red X, open "Build mod" and copy the error message into ChatGPT so the source can be fixed. The code has not been tested inside Minecraft.

SECURITY: This project only sends ordinary in-game commands to move an educational block-display prop. Never run untrusted JARs.
