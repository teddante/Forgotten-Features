package io.github.teddante.forgottenfeatures.registry;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class ForgottenFeaturesParticles {
    public static final SimpleParticleType DEPTH_SUSPEND = register("depth_suspend");

    private ForgottenFeaturesParticles() {
    }

    public static void initialize() {
    }

    private static SimpleParticleType register(String name) {
        return Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(ForgottenFeatures.MOD_ID, name),
                FabricParticleTypes.simple()
        );
    }
}
