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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class emote {
    static public void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        if (!ModConfigs.emote)
            return;

        dispatcher.register(
                CommandManager.literal("emote")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .then(CommandManager.literal("meow")
                                .executes(context -> emote.play(context, SoundEvents.ENTITY_CAT_AMBIENT))
                        )
                        .then(CommandManager.literal("purr")
                                .executes(context -> emote.play(context, SoundEvents.ENTITY_CAT_PURR))
                        )
                        .then(CommandManager.literal("purreow")
                                .executes(context -> emote.play(context, SoundEvents.ENTITY_CAT_PURREOW))
                        )
                        .then(CommandManager.literal("woof")
                                .executes(context -> emote.play(context, SoundEvents.ENTITY_WOLF_AMBIENT))
                        )
        );

    }

    static private int play(CommandContext<ServerCommandSource> context, SoundEvent soundEvent) throws CommandSyntaxException {

        if (ModConfigs.permission_enable) {
            if (!Permissions.check(context.getSource(), permission.EMOTE)) {
                context.getSource().sendFeedback(() -> Text.literal("You don't have permission to do this command"), false);
                return 0;
            }
        }

        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        World world = player.getWorld();
        if (world.isClient)
            return 0;
        world.playSound(null, player.getBlockPos(), soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
        return 0;
    }
}
