package com.multiblocked2additions.fabric;

import com.multiblocked2additions.Multiblocked2Additions;
import com.multiblocked2additions.platform.Services;

import net.fabricmc.api.ModInitializer;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Services.setPlatform(new FabricPlatformService());
        FabricContent.register();
        Multiblocked2Additions.bootstrap();
    }
}
