package com.multiblocked2additions.forge.client;

import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MBD2EditorTabs {

    private MBD2EditorTabs() {
    }

    public static void restore(Editor editor) {
        int index = MBD2Config.getLastEditorTab();
        if (index >= 0) {
            editor.getTabPages().switchTabIndex(index);
        }
    }

    public static void capture(Editor editor) {
        MBD2Config.setLastEditorTab(editor.getTabPages().getTabIndex());
    }
}
