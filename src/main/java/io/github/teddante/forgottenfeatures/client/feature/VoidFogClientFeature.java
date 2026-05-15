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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class VoidFogClientFeature {
    private static final int START_ABOVE_BOTTOM = 26;
    private static final int FULL_ABOVE_BOTTOM = 4;
    private static final int PARTICLE_START_ABOVE_BOTTOM = 17;

    private VoidFogClientFeature() {
    }

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(VoidFogClientFeature::spawnParticles);
    }

    public static float strength(ClientLevel level, Camera camera) {
        ForgottenFeaturesConfig.VoidFogFeature config = ForgottenFeatures.config().features.voidFog;
        if (!config.enabled || level.dimension() != Level.OVERWORLD) {
            return 0.0F;
        }

        BlockPos pos = camera.blockPosition();
        if (level.canSeeSky(pos)) {
            return 0.0F;
        }

        return depthStrength(level, camera.position().y(), START_ABOVE_BOTTOM, FULL_ABOVE_BOTTOM);
    }

    public static int fogColor(ClientLevel level, Camera camera, int originalColor, float strength) {
        int target = 0x000000;
        int red = blend((originalColor >> 16) & 0xFF, (target >> 16) & 0xFF, strength);
        int green = blend((originalColor >> 8) & 0xFF, (target >> 8) & 0xFF, strength);
        int blue = blend(originalColor & 0xFF, target & 0xFF, strength);
        return (red << 16) | (green << 8) | blue;
    }

    public static float fogEnd(float originalEnd, float strength) {
        float targetEnd = Mth.lerp(strength, 18.0F, 4.0F);
        return Math.min(originalEnd, Mth.lerp(strength, originalEnd, targetEnd));
    }

    public static float fogStart(float originalStart, float strength) {
        float targetStart = Mth.lerp(strength, 4.0F, 0.0F);
        return Math.min(originalStart, Mth.lerp(strength, originalStart, targetStart));
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
        float strength = depthStrength(minecraft.level, playerPos.y(), PARTICLE_START_ABOVE_BOTTOM, FULL_ABOVE_BOTTOM);
        if (strength <= 0.0F || minecraft.level.canSeeSky(BlockPos.containing(playerPos))) {
            return;
        }

        int count = Math.max(1, Math.round(strength * 3.0F));
        for (int i = 0; i < count; i++) {
            double x = playerPos.x() + (minecraft.level.getRandom().nextDouble() - 0.5D) * 18.0D;
            double y = playerPos.y() + (minecraft.level.getRandom().nextDouble() - 0.5D) * 6.0D;
            double z = playerPos.z() + (minecraft.level.getRandom().nextDouble() - 0.5D) * 18.0D;
            minecraft.level.addParticle(ParticleTypes.ASH, x, y, z, 0.0D, -0.003D, 0.0D);
        }
    }

    private static float depthStrength(ClientLevel level, double y, int startAboveBottom, int fullAboveBottom) {
        double startY = level.getMinY() + startAboveBottom;
        double fullY = level.getMinY() + fullAboveBottom;
        return (float) Mth.clamp((startY - y) / (startY - fullY), 0.0D, 1.0D);
    }

    private static int blend(int original, int target, float strength) {
        return Mth.clamp(Math.round(Mth.lerp(strength, original, target)), 0, 255);
    }
}
