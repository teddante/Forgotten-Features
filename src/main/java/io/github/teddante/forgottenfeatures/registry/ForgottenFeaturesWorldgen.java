package io.github.teddante.forgottenfeatures.registry;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import io.github.teddante.forgottenfeatures.config.ForgottenFeaturesConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class ForgottenFeaturesWorldgen {
    private static final ResourceKey<PlacedFeature> ORE_RUBY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath(ForgottenFeatures.MOD_ID, "ore_ruby")
    );

    private ForgottenFeaturesWorldgen() {
    }

    public static void initialize(ForgottenFeaturesConfig config) {
        if (config.features.ruby.enabled && config.features.ruby.generateOre) {
            BiomeModifications.addFeature(
                    BiomeSelectors.tag(BiomeTags.IS_MOUNTAIN),
                    GenerationStep.Decoration.UNDERGROUND_ORES,
                    ORE_RUBY
            );
        }
    }
}

