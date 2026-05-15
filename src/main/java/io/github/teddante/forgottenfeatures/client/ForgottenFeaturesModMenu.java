package io.github.teddante.forgottenfeatures.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class ForgottenFeaturesModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ForgottenFeaturesConfigScreen::new;
    }
}

