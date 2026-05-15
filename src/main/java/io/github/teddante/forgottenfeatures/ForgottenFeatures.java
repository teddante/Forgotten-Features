package io.github.teddante.forgottenfeatures;

import io.github.teddante.forgottenfeatures.config.ForgottenFeaturesConfig;
import io.github.teddante.forgottenfeatures.registry.ForgottenFeaturesBlocks;
import io.github.teddante.forgottenfeatures.registry.ForgottenFeaturesItems;
import io.github.teddante.forgottenfeatures.registry.ForgottenFeaturesWorldgen;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ForgottenFeatures implements ModInitializer {
    public static final String MOD_ID = "forgottenfeatures";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ForgottenFeaturesConfig config;

    @Override
    public void onInitialize() {
        config = ForgottenFeaturesConfig.load();
        ForgottenFeaturesBlocks.initialize();
        ForgottenFeaturesItems.initialize(config);
        ForgottenFeaturesWorldgen.initialize(config);
        LOGGER.info("Forgotten Features initialized");
    }

    public static ForgottenFeaturesConfig config() {
        return config;
    }
}
