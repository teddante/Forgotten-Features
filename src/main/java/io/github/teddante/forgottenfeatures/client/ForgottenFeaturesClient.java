package io.github.teddante.forgottenfeatures.client;

import io.github.teddante.forgottenfeatures.client.feature.VoidFogClientFeature;
import net.fabricmc.api.ClientModInitializer;

public final class ForgottenFeaturesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VoidFogClientFeature.initialize();
    }
}

