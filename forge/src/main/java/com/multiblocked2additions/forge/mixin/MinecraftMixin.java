package com.multiblocked2additions.forge.mixin;

import com.multiblocked2additions.forge.client.MBD2EditorWindowView;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Redirect(method = "resizeDisplay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"))
    private Object substituteGuiScale(OptionInstance instance) {
        return MBD2EditorWindowView.isActive() ? MBD2EditorWindowView.get() : instance.get();
    }
}
