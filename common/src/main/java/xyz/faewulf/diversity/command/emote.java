package xyz.faewulf.diversity.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import xyz.faewulf.diversity.util.ModConfigs;

public class emote {
    static public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        if (!ModConfigs.emote)
            return;

        dispatcher.register(
                Commands.literal("emote")
                        .requires(CommandSourceStack::isPlayer)
                        .then(Commands.literal("meow")
                                .executes(context -> emote.play(context, SoundEvents.CAT_AMBIENT))
                        )
                        .then(Commands.literal("purr")
                                .executes(context -> emote.play(context, SoundEvents.CAT_PURR))
                        )
                        .then(Commands.literal("purreow")
                                .executes(context -> emote.play(context, SoundEvents.CAT_PURREOW))
                        )
                        .then(Commands.literal("woof")
                                .executes(context -> emote.play(context, SoundEvents.WOLF_AMBIENT))
                        )
        );

    }

    static private int play(CommandContext<CommandSourceStack> context, SoundEvent soundEvent) throws CommandSyntaxException {

//        if (ModConfigs.permission_enable) {
//            if (!Permissions.check(context.getSource(), permission.EMOTE)) {
//                context.getSource().sendSuccess(() -> Component.literal("You don't have permission to do this command"), false);
//                return 0;
//            }
//        }

        ServerPlayer player = context.getSource().getPlayerOrException();
        Level world = player.level();
        if (world.isClientSide)
            return 0;
        world.playSound(null, player.blockPosition(), soundEvent, SoundSource.PLAYERS, 1.0f, 1.0f);
        return 0;
    }
}
