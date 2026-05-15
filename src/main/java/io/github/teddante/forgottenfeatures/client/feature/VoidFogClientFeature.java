package io.github.teddante.forgottenfeatures.client.feature;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import io.github.teddante.forgottenfeatures.config.ForgottenFeaturesConfig;
import io.github.teddante.forgottenfeatures.registry.ForgottenFeaturesParticles;
import java.util.Locale;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
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
    private static final double DISTANCE_VERTICAL_OFFSET = 4.0D;
    private static final double DISTANCE_HEIGHT_FACTOR = 32.0D;
    private static final float DISTANCE_FADE_RANGE = 0.35F;
    private static final float FOG_DISTANCE_SCALE = 100.0F;
    private static final float MIN_FOG_DISTANCE = 5.0F;
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
        float endAtShortDistance = distanceLimit(minecraft.level, camera, 96.0F);
        float endAtLongDistance = distanceLimit(minecraft.level, camera, 512.0F);

        return Component.literal(String.format(
                Locale.ROOT,
                "Void Fog: enabled=%s particles=%s fluid=%s y=%.2f relativeY=%.2f sky=%d color=%.0f%% end@96=%.1f end@512=%.1f",
                config.enabled,
                config.particles,
                camera.getFluidInCamera(),
                camera.position().y(),
                relativeY,
                skyLight,
                color * 100.0F,
                endAtShortDistance,
                endAtLongDistance
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

    public static float distanceLimit(ClientLevel level, Camera camera, float originalEnd) {
        ForgottenFeaturesConfig.VoidFogFeature config = ForgottenFeatures.config().features.voidFog;
        if (!canApply(config, level, camera)) {
            return originalEnd;
        }

        BlockPos pos = camera.blockPosition();
        double skyLight = level.getBrightness(LightLayer.SKY, pos) / 16.0D;
        double relativeY = camera.position().y() - level.getMinY();
        double factor = skyLight + (relativeY + DISTANCE_VERTICAL_OFFSET) / DISTANCE_HEIGHT_FACTOR;
        if (factor >= 1.0D) {
            return originalEnd;
        }

        factor = Mth.clamp(factor, 0.0D, 1.0D);
        float targetEnd = Math.max(MIN_FOG_DISTANCE, FOG_DISTANCE_SCALE * (float) (factor * factor));
        float fade = smooth((float) ((1.0D - factor) / DISTANCE_FADE_RANGE));
        return Mth.lerp(fade, originalEnd, Math.min(originalEnd, targetEnd));
    }

    public static int fogColor(ClientLevel level, Camera camera, int originalColor, float strength) {
        int originalRed = (originalColor >> 16) & 0xFF;
        int originalGreen = (originalColor >> 8) & 0xFF;
        int originalBlue = originalColor & 0xFF;
        int neutral = Math.round((originalRed * 0.30F + originalGreen * 0.59F + originalBlue * 0.11F) * (1.0F - strength));
        return (neutral << 16) | (neutral << 8) | neutral;
    }

    public static float fogStart(float originalStart, float fogEnd) {
        return Math.min(originalStart, fogEnd * 0.25F);
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
