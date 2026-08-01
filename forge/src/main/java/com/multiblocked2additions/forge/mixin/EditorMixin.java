package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.editor.data.IProject;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.multiblocked2additions.forge.client.MBD2ProjectAutosaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Editor.class, remap = false)
public abstract class EditorMixin {

    @Inject(method = "loadProject", at = @At("TAIL"))
    private void onProjectLoaded(IProject project, CallbackInfo ci) {
        if (MBD2ProjectAutosaver.consumeCreated()) {
            MBD2ProjectAutosaver.saveNewProject((Editor) (Object) this);
        }
    }
}
