package io.github.teddante.forgottenfeatures.client.feature;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import io.github.teddante.forgottenfeatures.config.ForgottenFeaturesConfig;
import io.github.teddante.forgottenfeatures.registry.ForgottenFeaturesParticles;
import java.util.Locale;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;

public final class VoidFogClientFeature {
    private static final double COLOR_FACTOR = 0.03125D;
    private static final float MAX_OVERLAY_ALPHA = 0.72F;
    private static final int PARTICLE_RANGE = 16;
    private static final int PARTICLE_ATTEMPTS = 180;
    private static final int PARTICLE_RANDOM_HEIGHT = 8;
    private static final int PARTICLE_HEIGHT_OFFSET = 16;

    private VoidFogClientFeature() {
    }

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(VoidFogClientFeature::spawnParticles);
    }

    public static Component diagnosticMessage(Minecraft minecraft) {
        if (minecraft.level == null) {
            return Component.literal("Void Fog: no client level loaded.");
        }

        ForgottenFeaturesConfig.VoidFogFeature config = ForgottenFeatures.config().features.voidFog;
        Camera camera = minecraft.gameRenderer.getMainCamera();
        BlockPos pos = camera.blockPosition();
        double relativeY = camera.position().y() - minecraft.level.getMinY();
        int skyLight = minecraft.level.getBrightness(LightLayer.SKY, pos);
        float color = colorStrength(minecraft.level, camera);
        float overlayAlpha = overlayAlpha(minecraft.level, camera);

        return Component.literal(String.format(
                Locale.ROOT,
                "Void Fog: enabled=%s particles=%s fluid=%s y=%.2f relativeY=%.2f sky=%d color=%.0f%% overlay=%.0f%% distance=unchanged",
                config.enabled,
                config.particles,
                camera.getFluidInCamera(),
                camera.position().y(),
                relativeY,
                skyLight,
                color * 100.0F,
                overlayAlpha * 100.0F
        ));
    }

    public static float colorStrength(ClientLevel level, Camera camera) {
        ForgottenFeaturesConfig.VoidFogFeature config = ForgottenFeatures.config().features.voidFog;
        if (!canApply(config, level, camera)) {
            return 0.0F;
        }

        double relativeY = camera.position().y() - level.getMinY();
        double factor = Mth.clamp(relativeY * COLOR_FACTOR, 0.0D, 1.0D);
        if (factor >= 1.0D) {
            return 0.0F;
        }

        return smooth((float) (1.0D - factor * factor));
    }

    public static int fogColor(ClientLevel level, Camera camera, int originalColor, float strength) {
        int originalRed = (originalColor >> 16) & 0xFF;
        int originalGreen = (originalColor >> 8) & 0xFF;
        int originalBlue = originalColor & 0xFF;
        int neutral = Math.round((originalRed * 0.30F + originalGreen * 0.59F + originalBlue * 0.11F) * (1.0F - strength));
        return (neutral << 16) | (neutral << 8) | neutral;
    }

    private static float overlayAlpha(ClientLevel level, Camera camera) {
        return colorStrength(level, camera) * MAX_OVERLAY_ALPHA;
    }

    public static void renderOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null || minecraft.options.hideGui) {
            return;
        }

        float alpha = overlayAlpha(minecraft.level, minecraft.gameRenderer.getMainCamera());
        if (alpha <= 0.0F) {
            return;
        }

        int alphaByte = Mth.clamp(Math.round(alpha * 255.0F), 0, 255);
        graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), alphaByte << 24);
    }

    private static void spawnParticles(Minecraft minecraft) {
        ForgottenFeaturesConfig.VoidFogFeature config = ForgottenFeatures.config().features.voidFog;
        if (!config.enabled || !config.particles || minecraft.level == null || minecraft.player == null) {
            return;
        }
        if (minecraft.level.dimension() != Level.OVERWORLD) {
            return;
        }

        Vec3 playerPos = minecraft.player.position();
        BlockPos playerBlock = BlockPos.containing(playerPos);
        if (minecraft.level.getBrightness(LightLayer.SKY, playerBlock) > 0) {
            return;
        }

        for (int i = 0; i < PARTICLE_ATTEMPTS; i++) {
            int x = playerBlock.getX() + minecraft.level.getRandom().nextInt(PARTICLE_RANGE) - minecraft.level.getRandom().nextInt(PARTICLE_RANGE);
            int y = playerBlock.getY() + minecraft.level.getRandom().nextInt(PARTICLE_RANGE) - minecraft.level.getRandom().nextInt(PARTICLE_RANGE);
            int z = playerBlock.getZ() + minecraft.level.getRandom().nextInt(PARTICLE_RANGE) - minecraft.level.getRandom().nextInt(PARTICLE_RANGE);
            int particleY = y - minecraft.level.getMinY() - PARTICLE_HEIGHT_OFFSET;

            if (particleY < PARTICLE_RANDOM_HEIGHT
                    && minecraft.level.getRandom().nextInt(PARTICLE_RANDOM_HEIGHT) > particleY
                    && minecraft.level.isEmptyBlock(new BlockPos(x, y, z))) {
                minecraft.level.addParticle(
                        ForgottenFeaturesParticles.DEPTH_SUSPEND,
                        x + minecraft.level.getRandom().nextFloat(),
                        y + minecraft.level.getRandom().nextFloat(),
                        z + minecraft.level.getRandom().nextFloat(),
                        0.0D,
                        0.0D,
                        0.0D
                );
            }
        }
    }

    private static boolean canApply(ForgottenFeaturesConfig.VoidFogFeature config, ClientLevel level, Camera camera) {
        FogType fogType = camera.getFluidInCamera();
        return config.enabled
                && level.dimension() == Level.OVERWORLD
                && (fogType == FogType.NONE || fogType == FogType.ATMOSPHERIC);
    }

    private static float smooth(float value) {
        float clamped = Mth.clamp(value, 0.0F, 1.0F);
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }
}
