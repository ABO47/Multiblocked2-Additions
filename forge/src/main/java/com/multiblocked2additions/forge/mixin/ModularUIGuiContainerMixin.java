package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.multiblocked2additions.forge.client.MBD2TextFieldWalker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModularUIGuiContainer.class, remap = false)
public abstract class ModularUIGuiContainerMixin {

    private static boolean typingAllowed = true;

    @Inject(method = {"mouseClicked", "m_6375_"}, at = @At("HEAD"))
    private void onMouseClickedPre(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        blurOnClick(mouseX, mouseY);
    }

    @Inject(method = {"mouseClicked", "m_6375_"}, at = @At("TAIL"))
    private void onMouseClickedPost(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        blurOnClick(mouseX, mouseY);
    }

    @Inject(method = {"mouseReleased", "m_6348_"}, at = @At("TAIL"))
    private void onMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        blurOnClick(mouseX, mouseY);
    }

    @Inject(method = {"charTyped", "m_5534_"}, at = @At("HEAD"), cancellable = true)
    private void onCharTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Editor editor = Editor.INSTANCE;
        if (editor == null || editor.getGui() == null) {
            return;
        }
        ModularUIGuiContainer container = (ModularUIGuiContainer) (Object) this;
        if (editor.getGui().getModularUIGui() != container) {
            return;
        }
        if (!typingAllowed && MBD2TextFieldWalker.hasFocusedFieldInConfigPanel(editor.getConfigPanel())) {
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
        typingAllowed = !MBD2TextFieldWalker.anyFieldAt(container.modularUI.mainGroup, mouseX, mouseY);
        MBD2TextFieldWalker.blurTextFields(container.modularUI.mainGroup, mouseX, mouseY);
        MBD2TextFieldWalker.blurAllFields(configPanel);
        MBD2TextFieldWalker.blurLastFocus(container.lastFocus, mouseX, mouseY);
        container.focused = false;
        container.lastFocus = null;
    }
}
