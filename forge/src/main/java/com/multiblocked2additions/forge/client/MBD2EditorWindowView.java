package com.multiblocked2additions.forge.client;

import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MBD2EditorWindowView {
    private static boolean active;
    private static boolean resizePending;

    public static int get() {
        return MBD2Config.getWindowView();
    }

    public static void set(int view) {
        MBD2Config.setWindowView(view);
    }

    public static boolean isActive() {
        return active;
    }

    public static void applyPendingResize() {
        if (resizePending) {
            resizePending = false;
            Minecraft.getInstance().resizeDisplay();
        }
    }

    public static void onEditorOpened() {
        if (active || Editor.INSTANCE == null) {
            return;
        }
        active = true;
        resizePending = true;
    }

    public static void onEditorClosed() {
        if (!active) {
            return;
        }
        active = false;
        resizePending = true;
    }

    private MBD2EditorWindowView() {
    }
}
