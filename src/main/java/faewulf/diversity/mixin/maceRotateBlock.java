package faewulf.diversity.mixin;

//? if >=1.21 {

/*import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.getNextBlockState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MaceItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MaceItem.class)
public abstract class maceRotateBlock extends Item {
    public maceRotateBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (!ModConfigs.mace_rotate_block)
            return ActionResult.PASS;

        if (context.getWorld().isClient)
            return ActionResult.PASS;

        if (context.getPlayer() instanceof ServerPlayerEntity serverPlayerEntity) {
            if (!serverPlayerEntity.isSneaking())
                return ActionResult.PASS;

            BlockState currentBlock = context.getWorld().getBlockState(context.getBlockPos());
            BlockState blockState = getNextBlockState.getNextState(currentBlock);
            BlockPos blockPos = context.getBlockPos();

            if (blockState != null && blockState.canPlaceAt(context.getWorld(), blockPos)) {

                Vec3d hitPos = context.getHitPos();

                context.getWorld().setBlockState(blockPos, blockState);
                context.getWorld().playSound(null, hitPos.x, hitPos.y, hitPos.z, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.PLAYERS, 0.3f, 0.6f);
                context.getWorld().playSound(null, hitPos.x, hitPos.y, hitPos.z, SoundEvents.ENTITY_ITEM_FRAME_ROTATE_ITEM, SoundCategory.PLAYERS, 0.7f, 0.6f);

                context.getStack().damage(1, serverPlayerEntity, LivingEntity.getSlotForHand(context.getHand()));

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}
 
*///?}
