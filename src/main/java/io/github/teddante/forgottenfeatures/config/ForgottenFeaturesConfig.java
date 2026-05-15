package io.github.teddante.forgottenfeatures.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class ForgottenFeaturesConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "forgotten-features.json";

    public Features features = new Features();

    public static ForgottenFeaturesConfig load() {
        return load(FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME));
    }

    public static ForgottenFeaturesConfig load(Path path) {
        if (Files.notExists(path)) {
            ForgottenFeaturesConfig defaults = new ForgottenFeaturesConfig();
            defaults.save(path);
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            ForgottenFeaturesConfig config = GSON.fromJson(reader, ForgottenFeaturesConfig.class);
            return config == null ? new ForgottenFeaturesConfig() : config.normalize();
        } catch (IOException | JsonParseException exception) {
            ForgottenFeatures.LOGGER.warn("Could not read config at {}, using defaults", path, exception);
            return new ForgottenFeaturesConfig();
        }
    }

    public void save(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException exception) {
            ForgottenFeatures.LOGGER.warn("Could not write default config at {}", path, exception);
        }
    }

    private ForgottenFeaturesConfig normalize() {
        if (features == null) {
            features = new Features();
        }
        if (features.ruby == null) {
            features.ruby = new RubyFeature();
        }
        return this;
    }

    public static final class Features {
        public RubyFeature ruby = new RubyFeature();
    }

    public static final class RubyFeature {
        public boolean enabled = true;
        public boolean showInCreativeTab = true;
    }
}

