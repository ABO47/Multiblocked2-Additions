package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.mbd2.common.gui.editor.MachineProject;
import com.lowdragmc.mbd2.common.machine.definition.MBDMachineDefinition;
import com.multiblocked2additions.forge.client.MBD2MachineIdUniquifier;
import com.multiblocked2additions.forge.client.MBD2ProjectAutosaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = MachineProject.class, remap = false)
public abstract class MachineProjectMixin {

    @ModifyArg(method = "newEmptyProject", at = @At(value = "INVOKE", target = "Lcom/lowdragmc/mbd2/common/gui/editor/MachineProject;<init>(Lcom/lowdragmc/lowdraglib/gui/editor/data/Resources;Lcom/lowdragmc/mbd2/common/machine/definition/MBDMachineDefinition;Lcom/lowdragmc/lowdraglib/gui/widget/WidgetGroup;)V"), index = 1)
    private MBDMachineDefinition uniqueId(MBDMachineDefinition definition) {
        MBD2MachineIdUniquifier.uniquify(definition);
        MBD2ProjectAutosaver.markCreated();
        return definition;
    }
}
