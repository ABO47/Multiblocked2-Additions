package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.multiblocked2additions.forge.client.MBD2EditorPanelState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ResourcePanel.class, remap = false)
public abstract class ResourcePanelMixin {

    @Shadow
    protected ButtonWidget buttonHide;

    @Shadow
    protected boolean isShow;

    @Inject(method = "hide()V", at = @At("HEAD"), cancellable = true)
    private void onHideHead(CallbackInfo cir) {
        if (MBD2EditorPanelState.isTabSwitching()) {
            if (!MBD2EditorPanelState.isResourcePanelOpen()) {
                instantHide();
            }
            cir.cancel();
        }
    }

    @Inject(method = "hide()V", at = @At("TAIL"))
    private void onHideTail(CallbackInfo cir) {
        if (!MBD2EditorPanelState.isTabSwitching()) {
            MBD2EditorPanelState.setResourcePanelOpen(false);
        }
    }

    @Inject(method = "show()V", at = @At("HEAD"), cancellable = true)
    private void onShowHead(CallbackInfo cir) {
        if (MBD2EditorPanelState.isTabSwitching()) {
            if (MBD2EditorPanelState.isResourcePanelOpen()) {
                instantShow();
            }
            cir.cancel();
        }
    }

    @Inject(method = "show()V", at = @At("TAIL"))
    private void onShowTail(CallbackInfo cir) {
        if (!MBD2EditorPanelState.isTabSwitching()) {
            MBD2EditorPanelState.setResourcePanelOpen(true);
        }
    }

    private void instantHide() {
        if (isShow) {
            isShow = false;
            ((ResourcePanel) (Object) this).addSelfPosition(0, ResourcePanel.HEIGHT);
            buttonHide.setButtonTexture(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.UP);
        }
    }

    private void instantShow() {
        if (!isShow) {
            isShow = true;
            ((ResourcePanel) (Object) this).addSelfPosition(0, -ResourcePanel.HEIGHT);
            buttonHide.setButtonTexture(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.DOWN);
        }
    }
}
