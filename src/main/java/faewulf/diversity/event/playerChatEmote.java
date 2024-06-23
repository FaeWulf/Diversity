package faewulf.diversity.event;

import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class playerChatEmote {
    public static void run() {


        ServerMessageEvents.CHAT_MESSAGE.register(((message, sender, params) -> {

            if (!ModConfigs.emote)
                return;

            World world = sender.getWorld();

            if (world.isClient)
                return;

            String content = String.valueOf(message.getContent()).toLowerCase();

            List<SoundEvent> soundShouldPLay = new ArrayList<>();

            //if contains valid condition
            if (content.contains("meow") || content.contains(":3"))
                soundShouldPLay.add(SoundEvents.ENTITY_CAT_AMBIENT);
            if (content.contains("purr"))
                soundShouldPLay.add(SoundEvents.ENTITY_CAT_PURR);
            if (content.contains("purroew"))
                soundShouldPLay.add(SoundEvents.ENTITY_CAT_PURREOW);
            if (content.contains("woof"))
                soundShouldPLay.add(SoundEvents.ENTITY_WOLF_AMBIENT);

            soundShouldPLay.forEach(soundEvent -> {
                world.playSound(null, sender.getBlockPos(), soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
            });
        }));
    }
}
