package com.multiblocked2additions.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.multiblocked2additions.forge.TranslationKeys;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class MBD2KeyMappings {

    public static final KeyMapping OPEN_EDITOR = new KeyMapping(
            TranslationKeys.KEY_OPEN_EDITOR,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            TranslationKeys.KEY_CATEGORY);

    private MBD2KeyMappings() {
    }
}
