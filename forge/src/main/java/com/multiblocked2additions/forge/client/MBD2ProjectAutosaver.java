package com.multiblocked2additions.forge.client;

import com.lowdragmc.lowdraglib.gui.editor.data.IProject;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.mbd2.common.gui.editor.MachineProject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;

@OnlyIn(Dist.CLIENT)
public final class MBD2ProjectAutosaver {
    private static int ticks;
    private static boolean created;

    private MBD2ProjectAutosaver() {
    }

    public static void tick() {
        ticks++;
        if (ticks < MBD2Config.getAutosaveIntervalMinutes() * 60 * 20) {
            return;
        }
        ticks = 0;
        saveLoadedProject();
    }

    public static void markCreated() {
        created = true;
    }

    public static boolean consumeCreated() {
        boolean wasCreated = created;
        created = false;
        return wasCreated;
    }

    public static void saveLoadedProject() {
        Editor editor = Editor.INSTANCE;
        if (editor == null) {
            return;
        }
        IProject project = editor.getCurrentProject();
        save(project, editor.getCurrentProjectFile());
        reloadMachines(project);
    }

    public static void reloadMachines(IProject project) {
        if (!(project instanceof MachineProject machineProject)) {
            return;
        }
        ResourceLocation id = machineProject.getDefinition().id();
        if (id == null) {
            return;
        }
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.sendCommand("mbd2additions reload_machine " + id);
        }
    }

    public static void save(IProject project, File file) {
        if (project == null || file == null) {
            return;
        }
        project.saveProject(file);
    }

    public static void saveNewProject(Editor editor) {
        IProject project = editor.getCurrentProject();
        if (project == null) {
            return;
        }
        ResourceLocation id = machineId(project);
        if (id == null) {
            return;
        }
        File file = new File(project.getProjectWorkSpace(editor), id.getPath() + "." + project.getSuffix());
        project.saveProject(file);
        editor.setCurrentProjectFile(file);
    }

    private static ResourceLocation machineId(IProject project) {
        if (project instanceof MachineProject machineProject) {
            return machineProject.getDefinition().id();
        }
        return null;
    }
}
