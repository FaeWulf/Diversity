package faewulf.diversity.mixin.edibleEndStone;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Items.class)
public abstract class ItemsMixin {

    @Shadow
    public static Item register(BlockItem item) {
        return null;
    }

    @Mutable
    @Shadow
    @Final
    public static Item END_STONE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onStaticInit(CallbackInfo ci) {
        //TODO: find a way to do server-side only later
//        if (ModConfigs.endstone_is_cheese)
//            END_STONE = register(
//                    new BlockItem(
//                            Blocks.END_STONE,
//                            new Item.Settings()
//                                    .food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.6F).build())
//                    )
//            );
    }
}
