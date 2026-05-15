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
        FogData fogData = cir.getReturnValue();
        float environmentalEnd = VoidFogClientFeature.distanceLimit(level, camera, fogData.environmentalEnd);
        float renderDistanceEnd = VoidFogClientFeature.distanceLimit(level, camera, fogData.renderDistanceEnd);
        if (environmentalEnd >= fogData.environmentalEnd
                && renderDistanceEnd >= fogData.renderDistanceEnd) {
            return;
        }

        fogData.environmentalStart = VoidFogClientFeature.fogStart(fogData.environmentalStart, environmentalEnd);
        fogData.environmentalEnd = environmentalEnd;
        fogData.renderDistanceStart = VoidFogClientFeature.fogStart(fogData.renderDistanceStart, renderDistanceEnd);
        fogData.renderDistanceEnd = renderDistanceEnd;
    }
}
