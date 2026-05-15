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

@Mixin(value = FogRenderer.class, priority = 500)
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
        if (environmentalEnd >= fogData.environmentalEnd) {
            return;
        }

        fogData.environmentalStart = VoidFogClientFeature.fogStart(fogData.environmentalStart, environmentalEnd);
        fogData.environmentalEnd = environmentalEnd;
    }
}
