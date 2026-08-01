package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.editor.data.IProject;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.multiblocked2additions.forge.client.MBD2EditorTabs;
import com.multiblocked2additions.forge.client.MBD2ProjectAutosaver;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Editor.class, remap = false)
public abstract class EditorMixin {

    @Inject(method = "loadProject", at = @At("HEAD"))
    private void onProjectSwitching(IProject project, CallbackInfo ci) {
        Editor editor = (Editor) (Object) this;
        IProject current = editor.getCurrentProject();
        if (current != null) {
            MBD2ProjectAutosaver.save(current, editor.getCurrentProjectFile());
            MBD2ProjectAutosaver.reloadMachines(current);
        }
    }

    @Inject(method = "loadProject", at = @At("TAIL"))
    private void onProjectLoaded(IProject project, CallbackInfo ci) {
        if (MBD2ProjectAutosaver.consumeCreated()) {
            MBD2ProjectAutosaver.saveNewProject((Editor) (Object) this);
        }
        MBD2EditorTabs.restore((Editor) (Object) this);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            Editor.INSTANCE.getGui().getModularUIGui().onClose();
            cir.setReturnValue(true);
        }
    }
}
