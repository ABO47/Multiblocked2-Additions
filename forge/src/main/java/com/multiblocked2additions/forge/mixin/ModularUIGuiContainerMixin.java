package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.multiblocked2additions.forge.client.ITextFieldWidgetMixin;

@Mixin(value = ModularUIGuiContainer.class, remap = false)
public abstract class ModularUIGuiContainerMixin {

    private static boolean typingAllowed = true;

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClickedPre(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        blurOnClick(mouseX, mouseY);
    }

    @Inject(method = "mouseClicked", at = @At("TAIL"))
    private void onMouseClickedPost(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        blurOnClick(mouseX, mouseY);
    }

    @Inject(method = "mouseReleased", at = @At("TAIL"))
    private void onMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        blurOnClick(mouseX, mouseY);
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void onCharTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Editor editor = Editor.INSTANCE;
        if (editor == null || editor.getGui() == null) {
            return;
        }
        ModularUIGuiContainer container = (ModularUIGuiContainer) (Object) this;
        if (editor.getGui().getModularUIGui() != container) {
            return;
        }
        if (!typingAllowed && hasFocusedFieldInConfigPanel(editor.getConfigPanel())) {
            cir.setReturnValue(true);
        }
    }

    private void blurOnClick(double mouseX, double mouseY) {
        Editor editor = Editor.INSTANCE;
        if (editor == null || editor.getGui() == null) {
            return;
        }
        ModularUIGuiContainer container = (ModularUIGuiContainer) (Object) this;
        if (editor.getGui().getModularUIGui() != container) {
            return;
        }
        ConfigPanel configPanel = editor.getConfigPanel();
        if (configPanel == null) {
            return;
        }
        if (configPanel.isMouseOverElement(mouseX, mouseY)) {
            typingAllowed = true;
            return;
        }
        typingAllowed = anyFieldAt(container.modularUI.mainGroup, mouseX, mouseY);
        blurTextFields(container.modularUI.mainGroup, mouseX, mouseY);
        blurConfigFields(configPanel);
        Widget lastFocus = container.lastFocus;
        if (lastFocus instanceof TextFieldWidget textFieldWidget && !textFieldWidget.isMouseOverElement(mouseX, mouseY)) {
            ITextFieldWidgetMixin mixin = (ITextFieldWidgetMixin) (Object) textFieldWidget;
            mixin.forceLoseFocus();
            textFieldWidget.setFocus(false);
        }
        container.focused = false;
        container.lastFocus = null;
    }

    private static boolean hasFocusedFieldInConfigPanel(ConfigPanel configPanel) {
        if (configPanel == null) {
            return false;
        }
        return hasFocusedField(configPanel);
    }

    private static boolean hasFocusedField(Widget widget) {
        if (widget instanceof TextFieldWidget textFieldWidget) {
            ITextFieldWidgetMixin mixin = (ITextFieldWidgetMixin) (Object) textFieldWidget;
            if (textFieldWidget.isFocus() || mixin.isEditBoxFocused()) {
                return true;
            }
        }
        if (widget instanceof WidgetGroup group) {
            for (Widget child : group.widgets) {
                if (hasFocusedField(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean anyFieldAt(Widget widget, double mouseX, double mouseY) {
        if (widget instanceof TextFieldWidget textFieldWidget && textFieldWidget.isMouseOverElement(mouseX, mouseY)) {
            return true;
        }
        if (widget instanceof WidgetGroup group) {
            for (Widget child : group.widgets) {
                if (anyFieldAt(child, mouseX, mouseY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void blurTextFields(Widget widget, double mouseX, double mouseY) {
        if (widget instanceof TextFieldWidget textFieldWidget) {
            if (!textFieldWidget.isMouseOverElement(mouseX, mouseY)) {
                ITextFieldWidgetMixin mixin = (ITextFieldWidgetMixin) (Object) textFieldWidget;
                if (textFieldWidget.isFocus() || mixin.isEditBoxFocused()) {
                    mixin.forceLoseFocus();
                    textFieldWidget.setFocus(false);
                }
            }
        }
        if (widget instanceof WidgetGroup group) {
            for (Widget child : group.widgets) {
                blurTextFields(child, mouseX, mouseY);
            }
        }
    }

    private static void blurConfigFields(Widget widget) {
        if (widget instanceof TextFieldWidget textFieldWidget) {
            ITextFieldWidgetMixin mixin = (ITextFieldWidgetMixin) (Object) textFieldWidget;
            mixin.forceLoseFocus();
            textFieldWidget.setFocus(false);
        }
        if (widget instanceof WidgetGroup group) {
            for (Widget child : group.widgets) {
                blurConfigFields(child);
            }
        }
    }
}
