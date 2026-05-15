package io.github.teddante.forgottenfeatures.client;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import io.github.teddante.forgottenfeatures.client.feature.VoidFogClientFeature;
import io.github.teddante.forgottenfeatures.client.particle.DepthSuspendParticle;
import io.github.teddante.forgottenfeatures.registry.ForgottenFeaturesParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

public final class ForgottenFeaturesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(
                ForgottenFeaturesParticles.DEPTH_SUSPEND,
                DepthSuspendParticle.Provider::new
        );
        HudElementRegistry.addFirst(
                Identifier.fromNamespaceAndPath(ForgottenFeatures.MOD_ID, "void_fog_overlay"),
                VoidFogClientFeature::renderOverlay
        );
        VoidFogClientFeature.initialize();
        ForgottenFeaturesClientCommands.initialize();
    }
}
