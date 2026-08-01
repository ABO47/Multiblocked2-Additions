package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.text.NumberFormat;

import com.multiblocked2additions.forge.client.ITextFieldWidgetMixin;

@Mixin(value = TextFieldWidget.class, remap = false)
@Implements(@Interface(iface = ITextFieldWidgetMixin.class, prefix = "mixin$"))
public abstract class TextFieldWidgetMixin implements ITextFieldWidgetMixin {

    @Shadow
    protected EditBox textField;

    @Shadow
    protected NumberFormat numberInstance;

    private boolean digitsFilterInstalled;

    @Inject(method = "charTyped", at = @At("HEAD"))
    private void onCharTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!digitsFilterInstalled && numberInstance != null && textField != null) {
            digitsFilterInstalled = true;
            textField.setFilter(s -> s.matches("-?[0-9.]*"));
        }
    }

    public boolean mixin$isEditBoxFocused() {
        return textField != null && textField.isFocused();
    }

    public void mixin$forceLoseFocus() {
        if (textField != null) {
            textField.setFocused(false);
        }
    }
}
