package com.multiblocked2additions.forge.client;

import com.lowdragmc.mbd2.common.gui.editor.multiblock.MultiblockAreaPanel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3i;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@OnlyIn(Dist.CLIENT)
public final class MBD2SelectionSync {

    private MBD2SelectionSync() {
    }

    public static void syncToPanel(MultiblockAreaPanel panel) {
        BlockPos from = MBD2MultiblockSelector.getFirstCorner();
        BlockPos to = MBD2MultiblockSelector.getSecondCorner();
        if (from == null || to == null) {
            return;
        }
        Object runtime = runtimeOf(panel);
        if (runtime == null) {
            return;
        }
        invoke(runtime, "setFrom", new Class<?>[]{BlockPos.class}, from);
        invoke(runtime, "setTo", new Class<?>[]{BlockPos.class}, to);
        BlockPos controller = MBD2MultiblockSelector.getController();
        if (controller != null) {
            invoke(runtime, "setControllerOffset", new Class<?>[]{Vector3i.class}, new Vector3i(
                    controller.getX() - Math.min(from.getX(), to.getX()),
                    controller.getY() - Math.min(from.getY(), to.getY()),
                    controller.getZ() - Math.min(from.getZ(), to.getZ())));
            setControllerFace(runtime, MBD2MultiblockSelector.getControllerFace());
        }
    }

    private static Object runtimeOf(MultiblockAreaPanel panel) {
        try {
            Field field = MultiblockAreaPanel.class.getDeclaredField("runtime");
            field.setAccessible(true);
            return field.get(panel);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    private static void invoke(Object runtime, String name, Class<?>[] types, Object value) {
        try {
            Method method = runtime.getClass().getMethod(name, types);
            method.invoke(runtime, value);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static void setControllerFace(Object runtime, Direction face) {
        try {
            Field field = runtime.getClass().getDeclaredField("controllerFace");
            field.setAccessible(true);
            field.set(runtime, face);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
