package com.multiblocked2additions.forge.mixin;

import com.multiblocked2additions.forge.client.MBD2EditorWindowView;

import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.editor.ui.menu.ViewMenu;
import com.lowdragmc.lowdraglib.gui.editor.ui.view.FloatViewWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ViewMenu.class, remap = false)
public abstract class ViewMenuMixin {
    @Shadow
    public abstract void openView(FloatViewWidget view);

    @Shadow
    public abstract void removeView(String viewName);

    @Shadow
    public abstract boolean isViewOpened(String viewName);

    @Overwrite
    protected TreeBuilder.Menu createMenu() {
        var viewMenu = TreeBuilder.Menu.start().branch("ldlib.gui.editor.menu.view.window_size", menu -> {
            Minecraft minecraft = Minecraft.getInstance();
            int maxScale = !minecraft.isRunning() ? 0x7FFFFFFE : minecraft.getWindow().calculateScale(0, minecraft.isEnforceUnicode());
            for (int i = 0; i <= maxScale; i++) {
                int finalI = i;
                menu.leaf(MBD2EditorWindowView.get() == i ? Icons.CHECK : IGuiTexture.EMPTY, i == 0 ? "options.guiScale.auto" : i + "", () -> {
                    if (MBD2EditorWindowView.get() != finalI) {
                        MBD2EditorWindowView.set(finalI);
                        Minecraft.getInstance().resizeDisplay();
                    }
                });
            }
        });
        for (AnnotationDetector.Wrapper<LDLRegister, FloatViewWidget> wrapper : AnnotationDetector.REGISTER_FLOAT_VIEWS) {
            if (Editor.INSTANCE.name().startsWith(wrapper.annotation().group())) {
                String translateKey = "ldlib.gui.editor.register.%s.%s".formatted(wrapper.annotation().group(), wrapper.annotation().name());
                String name = wrapper.annotation().name();
                if (isViewOpened(name)) {
                    viewMenu.leaf(Icons.CHECK, translateKey, () -> removeView(name));
                } else {
                    viewMenu.leaf(translateKey, () -> {
                        var view = wrapper.creator().get();
                        openView(view);
                    });
                }
            }
        }
        return viewMenu;
    }
}
