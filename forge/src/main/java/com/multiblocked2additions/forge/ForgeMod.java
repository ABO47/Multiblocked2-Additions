package com.multiblocked2additions.forge;

import com.multiblocked2additions.Multiblocked2Additions;
import com.multiblocked2additions.platform.Services;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Multiblocked2Additions.MOD_ID)
public final class ForgeMod {
    public ForgeMod() {
        Services.setPlatform(new ForgePlatformService());

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeContent.register(modBus);
        modBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Multiblocked2Additions::bootstrap);
    }
}
