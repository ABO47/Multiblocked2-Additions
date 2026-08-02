package com.multiblocked2additions.forge;

import com.multiblocked2additions.Multiblocked2Additions;

import com.lowdragmc.mbd2.api.registry.MBDRegistries;
import com.lowdragmc.mbd2.common.machine.definition.MBDMachineDefinition;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Multiblocked2Additions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeCommonEvents {

    private ForgeCommonEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("mbd2additions")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("reload_machine")
                        .then(Commands.argument("id", ResourceLocationArgument.id())
                                .executes(context -> {
                                    ResourceLocation id = ResourceLocationArgument.getId(context, "id");
                                    MBDMachineDefinition definition = MBDRegistries.MACHINE_DEFINITIONS.get(id);
                                    if (definition == null || !definition.isCreatedFromProjectFile()) {
                                        return 0;
                                    }
                                    definition.reloadFromProjectFile();
                                    context.getSource().sendSuccess(() -> Component.translatable(TranslationKeys.COMMAND_RELOAD_SUCCESS, id), true);
                                    return 1;
                                }))));
    }
}
