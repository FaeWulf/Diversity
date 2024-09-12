package xyz.faewulf.diversity.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class slimechunk {

    static public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        if (!ModConfigs.slime_chunk_check)
            return;

        dispatcher.register(
                Commands.literal("slimechunkcheck")
                        .requires(CommandSourceStack::isPlayer)
                        .executes(slimechunk::run)
        );

    }

    static private int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        if (ModConfigs.permission_enable) {
            if (!context.getSource().hasPermission(1)) {
                context.getSource().sendSuccess(() -> Component.literal("You don't have permission to use this command"), false);
                return 0;
            }
        }

        ServerPlayer serverPlayerEntity = context.getSource().getPlayerOrException();

        Level world = serverPlayerEntity.level();

        if (world.isClientSide)
            return 0;

        ChunkPos chunkPos = new ChunkPos(serverPlayerEntity.blockPosition());

        long seed = ((WorldGenLevel) world).getSeed();

        //check slimechunk
        boolean isSlimeChunk = WorldgenRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, seed, 987234911L).nextInt(10) == 0;

        if (isSlimeChunk)
            serverPlayerEntity.displayClientMessage(Component.literal("You are standing in the slime chunk").withStyle(ChatFormatting.GREEN), true);
        else
            serverPlayerEntity.displayClientMessage(Component.literal("You are not standing in the slime chunk").withStyle(ChatFormatting.GRAY), true);

        return 1;
    }
}
