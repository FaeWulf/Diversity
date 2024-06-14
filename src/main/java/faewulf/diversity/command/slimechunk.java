package faewulf.diversity.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;

public class slimechunk {

    static public void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        if (!ModConfigs.slime_chunk_check)
            return;

        dispatcher.register(
                CommandManager.literal("slimechunkcheck")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(slimechunk::run)
        );

    }

    static private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        if (ModConfigs.permission_enable) {
            if (!Permissions.check(context.getSource(), permission.SLIME_CHUNK)) {
                context.getSource().sendFeedback(() -> Text.literal("You don't have permission to do this command"), false);
                return 0;
            }
        }

        ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayerOrThrow();

        World world = serverPlayerEntity.getWorld();

        if (world.isClient)
            return 0;

        ChunkPos chunkPos = new ChunkPos(serverPlayerEntity.getBlockPos());

        long seed = ((StructureWorldAccess) world).getSeed();

        //check slimechunk
        boolean isSlimeChunk = ChunkRandom.getSlimeRandom(chunkPos.x, chunkPos.z, seed, 987234911L).nextInt(10) == 0;

        if (isSlimeChunk)
            serverPlayerEntity.sendMessage(Text.literal("You are standing in the slime chunk").formatted(Formatting.GREEN), true);
        else
            serverPlayerEntity.sendMessage(Text.literal("You are not standing in the slime chunk").formatted(Formatting.GRAY), true);

        return 1;
    }
}
