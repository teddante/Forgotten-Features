package io.github.teddante.forgottenfeatures.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

final class ForgottenFeaturesConfigTest {
    @Test
    void createsDefaultConfigWhenMissing(@TempDir Path tempDir) {
        Path configPath = tempDir.resolve("forgotten-features.json");

        ForgottenFeaturesConfig config = ForgottenFeaturesConfig.load(configPath);

        assertTrue(config.features.ruby.enabled);
        assertTrue(config.features.ruby.showInCreativeTab);
        assertTrue(Files.exists(configPath));
    }

    @Test
    void readsRubyToggle(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("forgotten-features.json");
        Files.writeString(configPath, """
                {
                  "features": {
                    "ruby": {
                      "enabled": false,
                      "showInCreativeTab": false
                    }
                  }
                }
                """);

        ForgottenFeaturesConfig config = ForgottenFeaturesConfig.load(configPath);

        assertFalse(config.features.ruby.enabled);
        assertFalse(config.features.ruby.showInCreativeTab);
    }

    @Test
    void normalizesPartialConfig(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("forgotten-features.json");
        Files.writeString(configPath, "{}");

        ForgottenFeaturesConfig config = ForgottenFeaturesConfig.load(configPath);

        assertNotNull(config.features);
        assertNotNull(config.features.ruby);
        assertTrue(config.features.ruby.enabled);
    }
}

