package com.multiblocked2additions.forge.client;

import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MBD2TextFieldWalker {

    private MBD2TextFieldWalker() {
    }

    public static boolean hasFocusedFieldInConfigPanel(ConfigPanel configPanel) {
        return hasFocusedField(configPanel);
    }

    public static boolean anyFieldAt(Widget widget, double mouseX, double mouseY) {
        if (widget instanceof ITextFieldWidgetMixin && widget.isMouseOverElement(mouseX, mouseY)) {
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

    public static void blurTextFields(Widget widget, double mouseX, double mouseY) {
        if (widget instanceof ITextFieldWidgetMixin mixin && !widget.isMouseOverElement(mouseX, mouseY)) {
            if (widget.isFocus() || mixin.isEditBoxFocused()) {
                mixin.forceLoseFocus();
                widget.setFocus(false);
            }
        }
        if (widget instanceof WidgetGroup group) {
            for (Widget child : group.widgets) {
                blurTextFields(child, mouseX, mouseY);
            }
        }
    }

    public static void blurAllFields(Widget widget) {
        if (widget instanceof ITextFieldWidgetMixin mixin) {
            mixin.forceLoseFocus();
            widget.setFocus(false);
        }
        if (widget instanceof WidgetGroup group) {
            for (Widget child : group.widgets) {
                blurAllFields(child);
            }
        }
    }

    public static void blurLastFocus(Widget lastFocus, double mouseX, double mouseY) {
        if (lastFocus instanceof ITextFieldWidgetMixin mixin && !lastFocus.isMouseOverElement(mouseX, mouseY)) {
            mixin.forceLoseFocus();
            lastFocus.setFocus(false);
        }
    }

    private static boolean hasFocusedField(Widget widget) {
        if (widget instanceof ITextFieldWidgetMixin mixin) {
            if (widget.isFocus() || mixin.isEditBoxFocused()) {
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
}
