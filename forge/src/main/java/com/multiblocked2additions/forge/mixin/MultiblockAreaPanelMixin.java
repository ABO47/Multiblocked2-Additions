package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.mbd2.common.gui.editor.multiblock.MultiblockAreaPanel;
import com.multiblocked2additions.forge.client.MBD2SelectionSync;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MultiblockAreaPanel.class, remap = false)
public abstract class MultiblockAreaPanelMixin {

    @Inject(method = "onPanelSelected", at = @At("HEAD"))
    private void onPanelSelected(CallbackInfo ci) {
        MBD2SelectionSync.syncToPanel((MultiblockAreaPanel) (Object) this);
    }
}
