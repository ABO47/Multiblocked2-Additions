package com.multiblocked2additions.forge.client;

import com.lowdragmc.mbd2.MBD2;
import com.lowdragmc.mbd2.common.machine.definition.MBDMachineDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public final class MBD2MachineIdUniquifier {

    private static final Field ID_FIELD = idField();

    private MBD2MachineIdUniquifier() {
    }

    public static void uniquify(MBDMachineDefinition definition) {
        ResourceLocation id = definition.id();
        if (id == null) {
            return;
        }
        ResourceLocation uniqueId = nextId(id, usedIds());
        if (!uniqueId.equals(id)) {
            setId(definition, uniqueId);
        }
    }

    private static Set<String> usedIds() {
        Set<String> used = new HashSet<>();
        collectUsedIds(used, new File(MBD2.getLocation(), "machine"), ".sm");
        collectUsedIds(used, new File(MBD2.getLocation(), "multiblock"), ".mb");
        collectUsedIds(used, new File(MBD2.getLocation(), "kinetic_machine"), ".km");
        return used;
    }

    private static void collectUsedIds(Set<String> used, File folder, String extension) {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(extension));
        if (files == null) {
            return;
        }
        for (File file : files) {
            try {
                CompoundTag tag = NbtIo.read(file);
                if (tag != null) {
                    used.add(tag.getCompound("definition").getString("id"));
                }
            } catch (IOException ignored) {
            }
        }
    }

    private static ResourceLocation nextId(ResourceLocation id, Set<String> used) {
        if (!used.contains(id.toString())) {
            return id;
        }
        int n = 2;
        ResourceLocation candidate;
        do {
            candidate = new ResourceLocation(id.getNamespace(), id.getPath() + "_" + n);
            n++;
        } while (used.contains(candidate.toString()));
        return candidate;
    }

    private static void setId(MBDMachineDefinition definition, ResourceLocation id) {
        try {
            ID_FIELD.set(definition, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field idField() {
        try {
            Field field = MBDMachineDefinition.class.getDeclaredField("id");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
