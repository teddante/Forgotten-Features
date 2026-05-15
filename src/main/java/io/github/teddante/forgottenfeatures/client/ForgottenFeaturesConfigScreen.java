package io.github.teddante.forgottenfeatures.client;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import io.github.teddante.forgottenfeatures.config.ForgottenFeaturesConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ForgottenFeaturesConfigScreen extends Screen {
    private final Screen parent;
    private final ForgottenFeaturesConfig config;

    public ForgottenFeaturesConfigScreen(Screen parent) {
        super(Component.translatable("screen.forgottenfeatures.config"));
        this.parent = parent;
        this.config = ForgottenFeatures.config();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int left = centerX - 155;
        int top = this.height / 6;

        addRenderableWidget(new StringWidget(
                centerX - 100,
                top,
                200,
                20,
                this.title,
                this.font
        ));

        addRenderableWidget(new StringWidget(
                left,
                top + 34,
                310,
                20,
                Component.translatable("screen.forgottenfeatures.category.blocks_items"),
                this.font
        ));

        addRenderableWidget(Checkbox.builder(
                        Component.translatable("option.forgottenfeatures.ruby.enabled"),
                        this.font
                )
                .pos(left, top + 64)
                .maxWidth(310)
                .selected(config.features.ruby.enabled)
                .onValueChange((checkbox, selected) -> {
                    config.features.ruby.enabled = selected;
                    save();
                })
                .build());

        addRenderableWidget(Checkbox.builder(
                        Component.translatable("option.forgottenfeatures.ruby.show_in_creative_tab"),
                        this.font
                )
                .pos(left, top + 94)
                .maxWidth(310)
                .selected(config.features.ruby.showInCreativeTab)
                .onValueChange((checkbox, selected) -> {
                    config.features.ruby.showInCreativeTab = selected;
                    save();
                })
                .build());

        addRenderableWidget(Button.builder(
                        Component.translatable("button.forgottenfeatures.done"),
                        button -> onClose()
                )
                .bounds(centerX - 100, this.height - 32, 200, 20)
                .build());
    }

    @Override
    public void onClose() {
        save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    private void save() {
        config.save();
    }
}
