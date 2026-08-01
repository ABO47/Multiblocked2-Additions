package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.multiblocked2additions.forge.client.ITextFieldWidgetMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.text.NumberFormat;

@Mixin(value = TextFieldWidget.class, remap = false)
@Implements(@Interface(iface = ITextFieldWidgetMixin.class, prefix = "mixin$"))
public abstract class TextFieldWidgetMixin implements ITextFieldWidgetMixin {

    @Shadow
    protected EditBox textField;
    @Shadow
    protected int maxStringLength;
    @Shadow
    protected boolean isBordered;
    @Shadow
    protected int textColor;
    @Shadow
    protected NumberFormat numberInstance;
    @Shadow
    protected abstract void onTextChanged(String newTextString);

    private boolean digitsFilterInstalled;

    @Inject(method = "drawInBackground", at = @At("HEAD"))
    private void onDrawInBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ensureEditBox();
    }

    @Inject(method = "setCurrentString", at = @At("HEAD"))
    private void onSetCurrentString(Object currentString, CallbackInfoReturnable<TextFieldWidget> cir) {
        ensureEditBox();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        ensureEditBox();
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        ensureEditBox();
    }

    @Inject(method = "charTyped", at = @At("HEAD"))
    private void onCharTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        ensureEditBox();
        installDigitsFilter();
    }

    @Inject(method = "onFocusChanged", at = @At("HEAD"))
    private void onFocusChanged(Widget lastFocus, Widget focus, CallbackInfo ci) {
        ensureEditBox();
    }

    public boolean mixin$isEditBoxFocused() {
        return textField != null && textField.isFocused();
    }

    public void mixin$forceLoseFocus() {
        if (textField != null) {
            textField.setFocused(false);
        }
    }

    private void ensureEditBox() {
        if (textField == null && Minecraft.getInstance() != null) {
            Font font = Minecraft.getInstance().font;
            Position position = ((TextFieldWidget) (Object) this).getPosition();
            Size size = ((TextFieldWidget) (Object) this).getSize();
            textField = new EditBox(font, position.x, position.y, size.width, size.height, Component.literal("text field"));
            textField.setBordered(isBordered);
            textField.setMaxLength(maxStringLength);
            textField.setTextColor(textColor);
            textField.setResponder(this::onTextChanged);
            textField.setX(isBordered ? position.x : position.x + 2);
            textField.setY(isBordered ? position.y : position.y + (size.height - font.lineHeight) / 2 + 1);
            textField.setWidth(isBordered ? size.width : size.width - 2);
        }
    }

    private void installDigitsFilter() {
        if (numberInstance != null && textField != null && !digitsFilterInstalled) {
            textField.setFilter(s -> s.matches("-?[0-9.]*"));
            digitsFilterInstalled = true;
        }
    }
}
