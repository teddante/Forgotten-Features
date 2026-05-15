package io.github.teddante.forgottenfeatures.client.feature;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import io.github.teddante.forgottenfeatures.config.ForgottenFeaturesConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class VoidFogClientFeature {
    private static final double COLOR_FACTOR = 0.03125D;
    private static final double DISTANCE_VERTICAL_OFFSET = 4.0D;
    private static final double DISTANCE_HEIGHT_FACTOR = 32.0D;
    private static final float FOG_DISTANCE_SCALE = 100.0F;
    private static final float MIN_FOG_DISTANCE = 5.0F;
    private static final int PARTICLE_RANGE = 16;
    private static final int PARTICLE_ATTEMPTS = 120;
    private static final int PARTICLE_RANDOM_HEIGHT = 8;

    private VoidFogClientFeature() {
    }

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(VoidFogClientFeature::spawnParticles);
    }

    public static float colorStrength(ClientLevel level, Camera camera) {
        ForgottenFeaturesConfig.VoidFogFeature config = ForgottenFeatures.config().features.voidFog;
        if (!canApply(config, level)) {
            return 0.0F;
        }

        double relativeY = camera.position().y() - level.getMinY();
        double factor = Mth.clamp(relativeY * COLOR_FACTOR, 0.0D, 1.0D);
        if (factor >= 1.0D) {
            return 0.0F;
        }

        return (float) (1.0D - factor * factor);
    }

    public static float distanceLimit(ClientLevel level, Camera camera, float originalEnd) {
        ForgottenFeaturesConfig.VoidFogFeature config = ForgottenFeatures.config().features.voidFog;
        if (!canApply(config, level)) {
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
        return Math.min(originalEnd, targetEnd);
    }

    public static int fogColor(ClientLevel level, Camera camera, int originalColor, float strength) {
        int target = 0x000000;
        int red = blend((originalColor >> 16) & 0xFF, (target >> 16) & 0xFF, strength);
        int green = blend((originalColor >> 8) & 0xFF, (target >> 8) & 0xFF, strength);
        int blue = blend(originalColor & 0xFF, target & 0xFF, strength);
        return (red << 16) | (green << 8) | blue;
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
            int relativeY = y - minecraft.level.getMinY();

            if (relativeY >= 0
                    && minecraft.level.getRandom().nextInt(PARTICLE_RANDOM_HEIGHT) > relativeY
                    && minecraft.level.isEmptyBlock(new BlockPos(x, y, z))) {
                minecraft.level.addParticle(
                        ParticleTypes.ASH,
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

    private static boolean canApply(ForgottenFeaturesConfig.VoidFogFeature config, ClientLevel level) {
        return config.enabled && level.dimension() == Level.OVERWORLD;
    }

    private static int blend(int original, int target, float strength) {
        return Mth.clamp(Math.round(Mth.lerp(strength, original, target)), 0, 255);
    }
}
