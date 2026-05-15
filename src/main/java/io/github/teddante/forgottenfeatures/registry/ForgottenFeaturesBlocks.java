package io.github.teddante.forgottenfeatures.registry;

import io.github.teddante.forgottenfeatures.ForgottenFeatures;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ForgottenFeaturesBlocks {
    public static final Block RUBY_BLOCK = register(
            "ruby_block",
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_BLOCK),
            true
    );
    public static final Block RUBY_ORE = register(
            "ruby_ore",
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE),
            true
    );
    public static final Block DEEPSLATE_RUBY_ORE = register(
            "deepslate_ruby_ore",
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_EMERALD_ORE),
            true
    );

    private ForgottenFeaturesBlocks() {
    }

    public static void initialize() {
    }

    private static Block register(
            String name,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties properties,
            boolean registerItem
    ) {
        ResourceKey<Block> blockKey = ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(ForgottenFeatures.MOD_ID, name)
        );
        Block block = blockFactory.apply(properties.setId(blockKey));

        if (registerItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(ForgottenFeatures.MOD_ID, name)
            );
            BlockItem blockItem = new BlockItem(block, new Item.Properties()
                    .setId(itemKey)
                    .useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }
}

