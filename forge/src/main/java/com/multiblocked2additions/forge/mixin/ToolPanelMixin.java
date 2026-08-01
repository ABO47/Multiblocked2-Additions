package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.editor.ui.ToolPanel;
import com.multiblocked2additions.forge.client.MBD2EditorPanelState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ToolPanel.class, remap = false)
public abstract class ToolPanelMixin {

    @Inject(method = "show()V", at = @At("HEAD"), cancellable = true)
    private void onShowHead(CallbackInfo cir) {
        if (MBD2EditorPanelState.isTabSwitching()) {
            if (MBD2EditorPanelState.isToolPanelOpen()) {
                ((ToolPanel) (Object) this).show(false);
            }
            cir.cancel();
        }
    }

    @Inject(method = "show()V", at = @At("TAIL"))
    private void onShowTail(CallbackInfo cir) {
        if (!MBD2EditorPanelState.isTabSwitching()) {
            MBD2EditorPanelState.setToolPanelOpen(true);
        }
    }

    @Inject(method = "hide()V", at = @At("HEAD"), cancellable = true)
    private void onHideHead(CallbackInfo cir) {
        if (MBD2EditorPanelState.isTabSwitching()) {
            ((ToolPanel) (Object) this).hide(false);
            cir.cancel();
        }
    }

    @Inject(method = "hide()V", at = @At("TAIL"))
    private void onHideTail(CallbackInfo cir) {
        if (!MBD2EditorPanelState.isTabSwitching()) {
            MBD2EditorPanelState.setToolPanelOpen(false);
        }
    }
}
