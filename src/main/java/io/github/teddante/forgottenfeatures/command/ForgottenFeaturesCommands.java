package io.github.teddante.forgottenfeatures.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.teddante.forgottenfeatures.registry.ForgottenFeaturesBlocks;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.level.block.Block;

public final class ForgottenFeaturesCommands {
    private static final int DEFAULT_RADIUS = 64;
    private static final int MAX_RADIUS = 160;

    private ForgottenFeaturesCommands() {
    }

    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("forgottenfeatures")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(Commands.literal("findrubyore")
                        .executes(context -> findRubyOre(context.getSource(), DEFAULT_RADIUS))
                        .then(Commands.argument("radius", IntegerArgumentType.integer(1, MAX_RADIUS))
                                .executes(context -> findRubyOre(
                                        context.getSource(),
                                        IntegerArgumentType.getInteger(context, "radius")
                                )))));
    }

    private static int findRubyOre(CommandSourceStack source, int radius) {
        ServerLevel level = source.getLevel();
        BlockPos origin = BlockPos.containing(source.getPosition());
        ScanResult result = scan(level, origin, radius);

        if (result.count == 0) {
            source.sendFailure(Component.literal("No Ruby Ore found within " + radius + " blocks of "
                    + format(origin) + ". Try a newly generated mountain biome or a larger radius."));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Found " + result.count + " Ruby Ore blocks within "
                + radius + " blocks. Nearest: " + format(result.nearest)
                + " (" + result.nearestDistanceSquared + " blocks squared away)."), false);
        return result.count;
    }

    private static ScanResult scan(ServerLevel level, BlockPos origin, int radius) {
        int minY = Math.max(level.getMinY(), origin.getY() - radius);
        int maxY = Math.min(level.getMaxY() - 1, origin.getY() + radius);
        int radiusSquared = radius * radius;

        int count = 0;
        BlockPos nearest = null;
        int nearestDistanceSquared = Integer.MAX_VALUE;

        for (int x = origin.getX() - radius; x <= origin.getX() + radius; x++) {
            for (int z = origin.getZ() - radius; z <= origin.getZ() + radius; z++) {
                int dx = x - origin.getX();
                int dz = z - origin.getZ();

                for (int y = minY; y <= maxY; y++) {
                    int dy = y - origin.getY();
                    int distanceSquared = dx * dx + dy * dy + dz * dz;
                    if (distanceSquared > radiusSquared) {
                        continue;
                    }

                    BlockPos pos = new BlockPos(x, y, z);
                    if (!level.isLoaded(pos)) {
                        continue;
                    }

                    if (isRubyOre(level.getBlockState(pos).getBlock())) {
                        count++;
                        if (distanceSquared < nearestDistanceSquared) {
                            nearest = pos;
                            nearestDistanceSquared = distanceSquared;
                        }
                    }
                }
            }
        }

        return new ScanResult(count, nearest, nearestDistanceSquared);
    }

    private static boolean isRubyOre(Block block) {
        return block == ForgottenFeaturesBlocks.RUBY_ORE || block == ForgottenFeaturesBlocks.DEEPSLATE_RUBY_ORE;
    }

    private static String format(BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }

    private record ScanResult(int count, BlockPos nearest, int nearestDistanceSquared) {
    }
}
