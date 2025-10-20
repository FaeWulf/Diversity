package xyz.faewulf.diversity.event_handler;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.diversity.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class deepslateGenerator {
    @SubscribeEvent
    public static void cobbleGeneratorEvent(BlockEvent.FluidPlaceBlockEvent blockEvent) {
        if (blockEvent.getPos().getY() < 8) {
            if (blockEvent.getState().getBlock() == Blocks.COBBLESTONE) {
                blockEvent.setNewState(Blocks.COBBLED_DEEPSLATE.defaultBlockState());
            } else if (blockEvent.getState().getBlock() == Blocks.STONE) {
                blockEvent.setNewState(Blocks.DEEPSLATE.defaultBlockState());
            }
        }
    }
}
