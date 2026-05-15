package io.github.teddante.forgottenfeatures.client;

import io.github.teddante.forgottenfeatures.client.feature.VoidFogClientFeature;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;

public final class ForgottenFeaturesClientCommands {
    private ForgottenFeaturesClientCommands() {
    }

    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommands.literal("ffvoidfog")
                        .executes(context -> {
                            context.getSource().sendFeedback(
                                    VoidFogClientFeature.diagnosticMessage(context.getSource().getClient())
                            );
                            return 1;
                        })
        ));
    }
}
