package faewulf.diversity.mixin.increaseStackSize;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.UnaryOperator;

@Mixin(DataComponentTypes.class)
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
                    target = "Lnet/minecraft/component/DataComponentTypes;register(Ljava/lang/String;Ljava/util/function/UnaryOperator;)Lnet/minecraft/component/ComponentType;",
                    ordinal = 0
            )
    )
    private static UnaryOperator<ComponentType.Builder<Integer>> modifyMaxStackSize(UnaryOperator<ComponentType.Builder<Integer>> builderOperator) {
        return builder -> builderOperator.apply(builder).codec(Codecs.rangedInt(1, 1024)).packetCodec(PacketCodecs.VAR_INT);
    }
}
