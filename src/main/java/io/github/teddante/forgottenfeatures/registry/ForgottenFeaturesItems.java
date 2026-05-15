package io.github.teddante.forgottenfeatures.registry;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import io.github.teddante.forgottenfeatures.config.ForgottenFeaturesConfig;
import java.util.function.Function;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ForgottenFeaturesItems {
    public static final Item RUBY = register("ruby", Item::new, new Item.Properties());

    private ForgottenFeaturesItems() {
    }

    public static void initialize(ForgottenFeaturesConfig config) {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            ForgottenFeaturesConfig currentConfig = ForgottenFeatures.config();
            if (currentConfig.features.ruby.enabled && currentConfig.features.ruby.showInCreativeTab) {
                entries.accept(RUBY);
                entries.accept(ForgottenFeaturesBlocks.RUBY_BLOCK);
                entries.accept(ForgottenFeaturesBlocks.RUBY_ORE);
                entries.accept(ForgottenFeaturesBlocks.DEEPSLATE_RUBY_ORE);
            }
        });
    }

    private static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(ForgottenFeatures.MOD_ID, name)
        );
        T item = factory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }
}
