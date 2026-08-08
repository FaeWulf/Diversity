package xyz.faewulf.diversity.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import xyz.faewulf.lib.util.Compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class shearDefusesTnt {
    public static InteractionResult run(Level level, Player player, InteractionHand hand, Entity entity, HitResult hitResult) {


        //if not enable in config file
        if (!ModConfigs.shear_defuses_tnt)
            return InteractionResult.PASS;

        Item item = player.getItemInHand(hand).getItem();

        //if not mainhand
        if ((entity.getType() == EntityType.TNT)
                && hand == InteractionHand.MAIN_HAND
                && hitResult == null
                && level instanceof ServerLevel serverLevel
                && Compare.isHasTag(item, "diversity:tnt_defuser")
        ) {

            if (entity.getRandom().nextFloat() < 0.15f)
                entity.remove(Entity.RemovalReason.KILLED);

//            ItemStack itemStack = new ItemStack(Items.TNT, 1);
//            ItemEntity item1 = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), itemStack);
//            item1.setDefaultPickUpDelay();
//            level.addFreshEntity(item1);

            player.swing(hand, true);
            player.getItemInHand(hand).hurtAndBreak(1, player, hand.asEquipmentSlot());
            entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0f, 1.0f);

            //game event
            level.gameEvent(player, GameEvent.ITEM_INTERACT_FINISH, entity.position());

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
