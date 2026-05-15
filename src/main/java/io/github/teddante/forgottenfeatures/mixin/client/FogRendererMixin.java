package io.github.teddante.forgottenfeatures.mixin.client;

import io.github.teddante.forgottenfeatures.client.feature.VoidFogClientFeature;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Inject(method = "setupFog", at = @At("RETURN"))
    private void forgottenfeatures$applyVoidFog(
            Camera camera,
            int viewDistance,
            DeltaTracker deltaTracker,
            float renderDistance,
            ClientLevel level,
            CallbackInfoReturnable<FogData> cir
    ) {
        float strength = VoidFogClientFeature.strength(level, camera);
        if (strength <= 0.0F) {
            return;
        }

        FogData fogData = cir.getReturnValue();
        fogData.environmentalStart = VoidFogClientFeature.fogStart(fogData.environmentalStart, strength);
        fogData.environmentalEnd = VoidFogClientFeature.fogEnd(fogData.environmentalEnd, strength);
        fogData.renderDistanceStart = VoidFogClientFeature.fogStart(fogData.renderDistanceStart, strength);
        fogData.renderDistanceEnd = VoidFogClientFeature.fogEnd(fogData.renderDistanceEnd, strength);
        fogData.skyEnd = VoidFogClientFeature.fogEnd(fogData.skyEnd, strength);
        fogData.cloudEnd = VoidFogClientFeature.fogEnd(fogData.cloudEnd, strength);
    }
}

