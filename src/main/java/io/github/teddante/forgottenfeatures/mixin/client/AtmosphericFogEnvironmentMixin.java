package io.github.teddante.forgottenfeatures.mixin.client;

import io.github.teddante.forgottenfeatures.client.feature.VoidFogClientFeature;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtmosphericFogEnvironment.class)
public abstract class AtmosphericFogEnvironmentMixin {
    @Inject(method = "getBaseColor", at = @At("RETURN"), cancellable = true)
    private void forgottenfeatures$darkenVoidFogColor(
            ClientLevel level,
            Camera camera,
            int viewDistance,
            float renderDistance,
            CallbackInfoReturnable<Integer> cir
    ) {
        float strength = VoidFogClientFeature.colorStrength(level, camera);
        if (strength <= 0.0F) {
            return;
        }
        cir.setReturnValue(VoidFogClientFeature.fogColor(level, camera, cir.getReturnValue(), strength));
    }
}
