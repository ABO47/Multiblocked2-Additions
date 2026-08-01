package com.multiblocked2additions.forge.client;

import com.lowdragmc.lowdraglib.gui.editor.data.IProject;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;

import com.lowdragmc.mbd2.common.gui.editor.MachineEditor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;

@OnlyIn(Dist.CLIENT)
public final class MBD2EditorOpener {
    private static IProject lastProject;
    private static File lastProjectFile;
    private static ModularUI capturedUi;

    public static void clearLastProject() {
        lastProject = null;
        lastProjectFile = null;
    }

    public static void restoreLastSession(Editor editor) {
        if (!(editor instanceof MachineEditor machineEditor)) {
            return;
        }
        ModularUI ui = editor.getGui();
        if (ui != capturedUi) {
            capturedUi = ui;
            ui.registerCloseListener(() -> {
                lastProject = machineEditor.getCurrentProject();
                lastProjectFile = machineEditor.getCurrentProjectFile();
                MBD2EditorWindowView.onEditorClosed();
            });
        }
        if (machineEditor.getCurrentProject() == null) {
            if (lastProject != null) {
                machineEditor.loadProject(lastProject);
                machineEditor.setCurrentProjectFile(lastProjectFile);
            }
        }
    }

    public static void openEditor() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }
        LocalPlayer player = minecraft.player;
        IUIHolder holder = new IUIHolder() {
            @Override
            public ModularUI createUI(Player entityPlayer) {
                return null;
            }

            @Override
            public boolean isInvalid() {
                return true;
            }

            @Override
            public boolean isRemote() {
                return true;
            }

            @Override
            public void markAsDirty() {
            }
        };
        ModularUI ui = new ModularUI(holder, player).widget(new MachineEditor());
        ui.initWidgets();
        ModularUIGuiContainer gui = new ModularUIGuiContainer(ui, player.containerMenu.containerId);
        minecraft.setScreen(gui);
        player.containerMenu = gui.getMenu();
    }

    private MBD2EditorOpener() {
    }
}
