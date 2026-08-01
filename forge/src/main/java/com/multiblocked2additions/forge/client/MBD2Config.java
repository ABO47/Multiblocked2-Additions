package com.multiblocked2additions.forge.client;

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

    private static int windowView = loadInt("window_view", DEFAULT_WINDOW_VIEW, 0, MAX_WINDOW_VIEW);
    private static int autosaveIntervalMinutes = loadInt("autosave_interval_minutes", DEFAULT_AUTOSAVE_INTERVAL_MINUTES, 1, MAX_AUTOSAVE_INTERVAL_MINUTES);

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

    private static void save() {
        JsonObject json = new JsonObject();
        json.addProperty("window_view", windowView);
        json.addProperty("autosave_interval_minutes", autosaveIntervalMinutes);
        try {
            Path path = configPath();
            Files.createDirectories(path.getParent());
            Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private static Path configPath() {
        return FMLPaths.CONFIGDIR.get().resolve("multiblocked2additions.json");
    }
}
