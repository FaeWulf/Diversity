package xyz.faewulf.diversity.mixin.increaseStackSize;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.UnaryOperator;

@Mixin(DataComponents.class)
public class DataComponentTypesMixin {

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=max_stack_size"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/component/DataComponents;register(Ljava/lang/String;Ljava/util/function/UnaryOperator;)Lnet/minecraft/core/component/DataComponentType;",
                    ordinal = 0
            )
    )
    private static UnaryOperator<DataComponentType.Builder<Integer>> modifyMaxStackSize(UnaryOperator<DataComponentType.Builder<Integer>> builderOperator) {
        return builder -> builderOperator.apply(builder).persistent(ExtraCodecs.intRange(1, 1024)).networkSynchronized(ByteBufCodecs.VAR_INT);
    }
}
