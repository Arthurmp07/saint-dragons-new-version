package com.leon.saintsdragons.client.input;

import com.leon.saintsdragons.client.ui.DragonSpawnChanceScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

/**
 * Opens the personal Dragon Spawn Menu. Registered by each loader's own keybind event.
 */
public final class DragonSpawnMenuKeybind {
    private static final String KEY_CATEGORY = "key.categories.saintsdragons";

    public static final KeyMapping OPEN_SPAWN_MENU = new KeyMapping(
            "key.saintsdragons.open_spawn_menu",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_G,
            KEY_CATEGORY
    );

    private DragonSpawnMenuKeybind() {
    }

    public static void registerKeys(Consumer<KeyMapping> registrar) {
        registrar.accept(OPEN_SPAWN_MENU);
    }

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }
        if (OPEN_SPAWN_MENU.consumeClick()) {
            minecraft.setScreen(new DragonSpawnChanceScreen(null));
        }
    }
}
