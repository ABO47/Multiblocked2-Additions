package com.multiblocked2additions.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MBD2EditorPanelState {
    private static boolean tabSwitching;
    private static boolean toolPanelOpen = MBD2Config.isToolPanelOpen();
    private static boolean resourcePanelOpen = MBD2Config.isResourcePanelOpen();

    private MBD2EditorPanelState() {
    }

    public static boolean isTabSwitching() {
        return tabSwitching;
    }

    public static void setTabSwitching(boolean switching) {
        tabSwitching = switching;
    }

    public static boolean isToolPanelOpen() {
        return toolPanelOpen;
    }

    public static void setToolPanelOpen(boolean open) {
        toolPanelOpen = open;
        MBD2Config.setToolPanelOpen(open);
    }

    public static boolean isResourcePanelOpen() {
        return resourcePanelOpen;
    }

    public static void setResourcePanelOpen(boolean open) {
        resourcePanelOpen = open;
        MBD2Config.setResourcePanelOpen(open);
    }
}
