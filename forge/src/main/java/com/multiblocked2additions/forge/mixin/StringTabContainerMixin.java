package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.editor.ui.StringTabContainer;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.multiblocked2additions.forge.client.MBD2EditorPanelState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = StringTabContainer.class, remap = false)
public abstract class StringTabContainerMixin {

    @Inject(method = "onTabChanged", at = @At("HEAD"))
    private void onTabChangedHead(WidgetGroup oldGroup, WidgetGroup newGroup, CallbackInfo cir) {
        MBD2EditorPanelState.setTabSwitching(true);
    }

    @Inject(method = "onTabChanged", at = @At("TAIL"))
    private void onTabChangedTail(WidgetGroup oldGroup, WidgetGroup newGroup, CallbackInfo cir) {
        MBD2EditorPanelState.setTabSwitching(false);
    }

    @Inject(method = "addTab(Lcom/lowdragmc/lowdraglib/gui/texture/IGuiTexture;Ljava/lang/String;Lcom/lowdragmc/lowdraglib/gui/widget/WidgetGroup;Ljava/lang/Runnable;Ljava/lang/Runnable;Ljava/lang/Runnable;)V", at = @At("HEAD"))
    private void onAddTabHead(CallbackInfo cir) {
        MBD2EditorPanelState.setTabSwitching(true);
    }

    @Inject(method = "addTab(Lcom/lowdragmc/lowdraglib/gui/texture/IGuiTexture;Ljava/lang/String;Lcom/lowdragmc/lowdraglib/gui/widget/WidgetGroup;Ljava/lang/Runnable;Ljava/lang/Runnable;Ljava/lang/Runnable;)V", at = @At("TAIL"))
    private void onAddTabTail(CallbackInfo cir) {
        MBD2EditorPanelState.setTabSwitching(false);
    }
}
