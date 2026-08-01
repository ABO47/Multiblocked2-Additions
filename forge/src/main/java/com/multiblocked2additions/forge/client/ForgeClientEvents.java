package com.multiblocked2additions.forge.client;

import com.multiblocked2additions.Multiblocked2Additions;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public final class ForgeClientEvents {

    private ForgeClientEvents() {
    }

    @Mod.EventBusSubscriber(modid = Multiblocked2Additions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBus {
        private ModBus() {
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(MBD2KeyMappings.OPEN_EDITOR);
        }
    }

    @Mod.EventBusSubscriber(modid = Multiblocked2Additions.MOD_ID, value = Dist.CLIENT)
    public static final class ForgeBus {
        private ForgeBus() {
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END && MBD2KeyMappings.OPEN_EDITOR.consumeClick()) {
                MBD2EditorOpener.openEditor();
            }
        }
    }
}
