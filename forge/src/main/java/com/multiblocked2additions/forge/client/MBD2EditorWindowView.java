package com.multiblocked2additions.forge.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@OnlyIn(Dist.CLIENT)
public final class MBD2EditorWindowView {
    private static final int DEFAULT_VIEW = 2;
    private static final int MAX_VIEW = 10;
    private static int windowView = loadView();
    private static boolean active;
    private static boolean resizePending;

    public static int get() {
        return windowView;
    }

    public static void set(int view) {
        if (windowView != view) {
            windowView = view;
            saveView(view);
        }
    }

    public static boolean isActive() {
        return active;
    }

    public static void applyPendingResize() {
        if (resizePending) {
            resizePending = false;
            Minecraft.getInstance().resizeDisplay();
        }
    }

    public static void onEditorOpened() {
        if (active || Editor.INSTANCE == null) {
            return;
        }
        active = true;
        resizePending = true;
    }

    public static void onEditorClosed() {
        if (!active) {
            return;
        }
        active = false;
        resizePending = true;
    }

    private static Path configPath() {
        return FMLPaths.CONFIGDIR.get().resolve("multiblocked2additions.json");
    }

    private static int loadView() {
        Path path = configPath();
        if (!Files.isRegularFile(path)) {
            saveView(DEFAULT_VIEW);
            return DEFAULT_VIEW;
        }
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            int view = JsonParser.parseReader(reader).getAsJsonObject().get("window_view").getAsInt();
            return view >= 0 && view <= MAX_VIEW ? view : DEFAULT_VIEW;
        } catch (IOException | IllegalStateException | NullPointerException ignored) {
            return DEFAULT_VIEW;
        }
    }

    private static void saveView(int view) {
        Path path = configPath();
        JsonObject json = new JsonObject();
        json.addProperty("window_view", view);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private MBD2EditorWindowView() {
    }
}
