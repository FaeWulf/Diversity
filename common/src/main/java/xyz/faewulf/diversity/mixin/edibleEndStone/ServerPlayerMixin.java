package xyz.faewulf.diversity.mixin.edibleEndStone;

import com.mojang.authlib.GameProfile;
import xyz.faewulf.diversity.util.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void tickInject(CallbackInfo ci) {
        ItemStack itemStack = this.getMainHandItem();

        if (itemStack.getItem() == Items.END_STONE) {
            if (ModConfigs.endstone_is_cheese) {
                itemStack.set(DataComponents.FOOD, new FoodProperties.Builder().nutrition(3).saturationModifier(0.6F).alwaysEdible().build());
            } else {
                itemStack.remove(DataComponents.FOOD);
            }
        }
    }
}
