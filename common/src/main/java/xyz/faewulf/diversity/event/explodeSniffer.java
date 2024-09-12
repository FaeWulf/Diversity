package xyz.faewulf.diversity.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import xyz.faewulf.diversity.inter.ICustomSniffer;
import xyz.faewulf.diversity.inter.typeSnort;
import xyz.faewulf.diversity.util.MissingMethod.ItemStackMethod;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.ArrayList;
import java.util.List;

public class explodeSniffer {

    private static final List<Item> acceptItems = new ArrayList<>() {{
        add(Items.SUGAR);
        add(Items.BLAZE_POWDER);
        add(Items.GUNPOWDER);
        add(Items.REDSTONE);
        add(Items.GLOWSTONE_DUST);
        add(Items.BONE_MEAL);
    }};

    public static InteractionResult run(Level world, Player player, InteractionHand hand, Entity entity, HitResult hitResult) {


        //if not enable in config file
        if (!ModConfigs.explosive_sniffer)
            return InteractionResult.PASS;

        //if not mainhand
        if ((entity.getType() == EntityType.SNIFFER)
                && hand == InteractionHand.MAIN_HAND
                && acceptItems.contains(player.getItemInHand(hand).getItem())
                && hitResult == null
        ) {
            if (!world.isClientSide) {


                //if already snort
                if (((ICustomSniffer) entity).getSnortType() != null)
                    return InteractionResult.PASS;

                Item item = player.getItemInHand(hand).getItem();

                if (item == Items.SUGAR)
                    ((ICustomSniffer) entity).setSnortType(typeSnort.SUGAR);
                else if (item == Items.GLOWSTONE_DUST)
                    ((ICustomSniffer) entity).setSnortType(typeSnort.GLOW_DUST);
                else if (item == Items.REDSTONE)
                    ((ICustomSniffer) entity).setSnortType(typeSnort.REDSTONE);
                else if (item == Items.BLAZE_POWDER)
                    ((ICustomSniffer) entity).setSnortType(typeSnort.BLAZE_POWDER);
                else if (item == Items.BONE_MEAL)
                    ((ICustomSniffer) entity).setSnortType(typeSnort.BONE_MEAL);
                else if (item == Items.GUNPOWDER)
                    ((ICustomSniffer) entity).setSnortType(typeSnort.GUN_POWDER);


                //player.getItemInHand(hand).consume(1, player);
                ItemStackMethod.consume(player.getItemInHand(hand), 1, player);

                entity.playSound(SoundEvents.BONE_MEAL_USE, 1.0f, 1.0f);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
