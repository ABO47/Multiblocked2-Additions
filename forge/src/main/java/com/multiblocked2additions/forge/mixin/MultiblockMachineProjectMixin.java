package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.mbd2.common.gui.editor.MultiblockMachineProject;
import com.lowdragmc.mbd2.common.machine.definition.MultiblockMachineDefinition;
import com.multiblocked2additions.forge.client.MBD2MachineIdUniquifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = MultiblockMachineProject.class, remap = false)
public abstract class MultiblockMachineProjectMixin {

    @ModifyArg(method = "newEmptyProject", at = @At(value = "INVOKE", target = "Lcom/lowdragmc/mbd2/common/gui/editor/MultiblockMachineProject;<init>(Lcom/lowdragmc/lowdraglib/gui/editor/data/Resources;Lcom/lowdragmc/mbd2/common/machine/definition/MultiblockMachineDefinition;Lcom/lowdragmc/lowdraglib/gui/widget/WidgetGroup;)V"), index = 1)
    private MultiblockMachineDefinition uniqueId(MultiblockMachineDefinition definition) {
        MBD2MachineIdUniquifier.uniquify(definition);
        return definition;
    }
}
