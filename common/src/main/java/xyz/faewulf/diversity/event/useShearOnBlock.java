package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import xyz.faewulf.diversity.feature.entity.PseudoBlockEntity.PseudoBlockEntities;
import xyz.faewulf.diversity.feature.entity.PseudoBlockEntity.PseudoBlockEntity;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.pseudoBlockEntityUtil;

public class useShearOnBlock {
    public static InteractionResult run(Level world, Player player, InteractionHand hand, BlockHitResult hitResult) {


        if (!ModConfigs.check_villager_schedule)
            return InteractionResult.PASS;

        if (world.isClientSide)
            return InteractionResult.PASS;

        Item item = player.getItemInHand(hand).getItem();

        if (item == Items.SHEARS) {

            BlockState block = world.getBlockState(hitResult.getBlockPos());

            if (compare.isHasTag(block.getBlock(), "diversity:trimmable") || compare.isHasTag(block.getBlock(), "minecraft:saplings")) {

                if (pseudoBlockEntityUtil.getBlockEntity(world, hitResult.getBlockPos()) != null)
                    return InteractionResult.PASS;

                Display display = PseudoBlockEntities.STOP_GROW.build(world);

                BlockPos pos = hitResult.getBlockPos();
                display.setPos(pos.getCenter().x, pos.getCenter().y, pos.getCenter().z);

                if (display instanceof PseudoBlockEntity pseudoBlockEntity) {
                    if (!pseudoBlockEntity.isBlockEntityAlreadyExist()) {
                        world.addFreshEntity(display);
                        world.playSound(null, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.PLAYERS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
                        player.getItemInHand(hand).hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                    }
                }

                player.swing(hand, true);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;

    }
}
