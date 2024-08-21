package faewulf.diversity.mixin.edibleEndStone;

//? if >=1.20.6 {

/*import com.mojang.authlib.GameProfile;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity {

    public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void tickInject(CallbackInfo ci) {
        ItemStack itemStack = this.getMainHandStack();

        if (itemStack.getItem() == Items.END_STONE) {
            if (ModConfigs.endstone_is_cheese) {
                itemStack.set(DataComponentTypes.FOOD, new FoodComponent.Builder().nutrition(3).saturationModifier(0.6F).alwaysEdible().build());
            } else {
                itemStack.remove(DataComponentTypes.FOOD);
            }
        }
    }
}
 
*///?}