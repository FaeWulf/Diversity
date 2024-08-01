package faewulf.diversity.mixin.edibleEndStone;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Items.class)
public abstract class ItemsMixin {

    @Shadow
    public static Item register(BlockItem item) {
        return null;
    }

    @Shadow
    public static Item register(Block block) {
        return null;
    }

    @Shadow
    public static final Item END_STONE = ModConfigs.endstone_is_cheese ? register(
            new BlockItem(
                    Blocks.END_STONE,
                    new Item.Settings()
                            .food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.6F).build())
            )
    )
            : register(Blocks.END_STONE);
}
