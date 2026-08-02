package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.ui.ToolPanel;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.multiblocked2additions.forge.client.MBD2EditorPanelState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ToolPanel.class, remap = false)
public abstract class ToolPanelMixin {

    @Shadow
    protected boolean isShow;

    @Shadow
    protected ButtonWidget buttonHide;

    @Inject(method = "show()V", at = @At("HEAD"), cancellable = true)
    private void onShowHead(CallbackInfo cir) {
        if (MBD2EditorPanelState.isTabSwitching()) {
            if (MBD2EditorPanelState.isToolPanelOpen()) {
                forceShow();
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
            forceHide();
            cir.cancel();
        }
    }

    @Inject(method = "hide()V", at = @At("TAIL"))
    private void onHideTail(CallbackInfo cir) {
        if (!MBD2EditorPanelState.isTabSwitching()) {
            MBD2EditorPanelState.setToolPanelOpen(false);
        }
    }

    private void forceShow() {
        if (!isShow) {
            isShow = true;
            ((ToolPanel) (Object) this).addSelfPosition(((ToolPanel) (Object) this).getSizeWidth(), 0);
            buttonHide.setButtonTexture(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.LEFT);
        }
    }

    private void forceHide() {
        if (isShow) {
            isShow = false;
            ((ToolPanel) (Object) this).addSelfPosition(-((ToolPanel) (Object) this).getSizeWidth(), 0);
            buttonHide.setButtonTexture(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.RIGHT);
        }
    }
}
