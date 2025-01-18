package agusev.peepochat.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class PeepochatConfig {
    private static PeepochatConfig INSTANCE;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("peepochat.json");

    public boolean enableFilter = true;
    public boolean enableDebug = false;
    public boolean showRawJson = false;

    public static PeepochatConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PeepochatConfig();
            INSTANCE.load();
        }
        return INSTANCE;
    }

    public void load() {
        try {
            if (CONFIG_PATH.toFile().exists()) {
                try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                    PeepochatConfig loaded = GSON.fromJson(reader, PeepochatConfig.class);
                    this.enableFilter = loaded.enableFilter;
                    this.enableDebug = loaded.enableDebug;
                    this.showRawJson = loaded.showRawJson;
                }
            } else {
                save();
            }
        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage());
        }
    }

    public void save() {
        try {
            try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("Error saving config: " + e.getMessage());
        }
    }
}