package xyz.faewulf.diversity.event;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.ArrayList;
import java.util.List;

public class playerChatEmote {
    public static void run(PlayerChatMessage message, ServerPlayer sender, ChatType.Bound params) {

        if (!ModConfigs.emote)
            return;

        Level world = sender.level();

        if (world.isClientSide)
            return;

        String content = String.valueOf(message.decoratedContent()).toLowerCase();

        List<SoundEvent> soundShouldPLay = new ArrayList<>();

        //if contains valid condition
        if (content.contains("meow") || content.contains(":3"))
            soundShouldPLay.add(SoundEvents.CAT_AMBIENT);
        if (content.contains("purr"))
            soundShouldPLay.add(SoundEvents.CAT_PURR);
        if (content.contains("purroew"))
            soundShouldPLay.add(SoundEvents.CAT_PURREOW);
        if (content.contains("woof"))
            soundShouldPLay.add(SoundEvents.WOLF_AMBIENT);

        soundShouldPLay.forEach(soundEvent -> {
            world.playSound(null, sender.blockPosition(), soundEvent, SoundSource.PLAYERS, 1.0f, 1.0f);
        });
    }
}
