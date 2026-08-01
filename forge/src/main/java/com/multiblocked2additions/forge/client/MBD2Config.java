package com.multiblocked2additions.forge.client;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@OnlyIn(Dist.CLIENT)
public final class MBD2Config {
    private static final int DEFAULT_WINDOW_VIEW = 2;
    private static final int MAX_WINDOW_VIEW = 10;
    private static final int DEFAULT_AUTOSAVE_INTERVAL_MINUTES = 5;
    private static final int MAX_AUTOSAVE_INTERVAL_MINUTES = 60;
    private static final int DEFAULT_LAST_EDITOR_TAB = -1;
    private static final int DEFAULT_SELECTION_REACH = 5;
    private static final int MAX_SELECTION_REACH = 64;
    private static final int DEFAULT_SELECTION_LINE_WIDTH = 4;
    private static final int MAX_SELECTION_LINE_WIDTH = 16;

    private static int windowView = loadInt("window_view", DEFAULT_WINDOW_VIEW, 0, MAX_WINDOW_VIEW);
    private static int autosaveIntervalMinutes = loadInt("autosave_interval_minutes", DEFAULT_AUTOSAVE_INTERVAL_MINUTES, 1, MAX_AUTOSAVE_INTERVAL_MINUTES);
    private static int lastEditorTab = loadInt("last_editor_tab", DEFAULT_LAST_EDITOR_TAB, -1, 100);
    private static int selectionReach = loadInt("selection_reach", DEFAULT_SELECTION_REACH, 1, MAX_SELECTION_REACH);
    private static boolean airPickEnabled = loadBoolean("air_pick_enabled", true);
    private static int selectionLineWidth = loadInt("selection_line_width", DEFAULT_SELECTION_LINE_WIDTH, 1, MAX_SELECTION_LINE_WIDTH);

    static {
        save();
    }

    private MBD2Config() {
    }

    public static int getWindowView() {
        return windowView;
    }

    public static void setWindowView(int view) {
        if (windowView == view) {
            return;
        }
        windowView = view;
        save();
    }

    public static int getAutosaveIntervalMinutes() {
        return autosaveIntervalMinutes;
    }

    public static int getLastEditorTab() {
        return lastEditorTab;
    }

    public static int getSelectionReach() {
        return selectionReach;
    }

    public static boolean isAirPickEnabled() {
        return airPickEnabled;
    }

    public static int getSelectionLineWidth() {
        return selectionLineWidth;
    }

    public static void setLastEditorTab(int index) {
        if (lastEditorTab == index) {
            return;
        }
        lastEditorTab = index;
        save();
    }

    private static int loadInt(String key, int defaultValue, int min, int max) {
        int value = defaultValue;
        Path path = configPath();
        if (Files.isRegularFile(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                value = JsonParser.parseReader(reader).getAsJsonObject().get(key).getAsInt();
            } catch (IOException | IllegalStateException | NullPointerException ignored) {
            }
        }
        return value >= min && value <= max ? value : defaultValue;
    }

    private static boolean loadBoolean(String key, boolean defaultValue) {
        boolean value = defaultValue;
        Path path = configPath();
        if (Files.isRegularFile(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                value = JsonParser.parseReader(reader).getAsJsonObject().get(key).getAsBoolean();
            } catch (IOException | IllegalStateException | NullPointerException ignored) {
            }
        }
        return value;
    }

    private static void save() {
        JsonObject json = new JsonObject();
        json.addProperty("window_view", windowView);
        json.addProperty("autosave_interval_minutes", autosaveIntervalMinutes);
        json.addProperty("last_editor_tab", lastEditorTab);
        json.addProperty("selection_reach", selectionReach);
        json.addProperty("air_pick_enabled", airPickEnabled);
        json.addProperty("selection_line_width", selectionLineWidth);
        try {
            Path path = configPath();
            Files.createDirectories(path.getParent());
            Files.writeString(path, new GsonBuilder().setPrettyPrinting().create().toJson(json), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private static Path configPath() {
        return FMLPaths.CONFIGDIR.get().resolve("multiblocked2additions.json");
    }
}
