package xyz.faewulf.diversity.util.MissingMethod;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ItemStackMethod {

    public static void consume(ItemStack itemStack, int pAmount, @Nullable LivingEntity pEntity) {
        if (pEntity == null || (!(pEntity instanceof Player) || !((Player) pEntity).getAbilities().instabuild)) {
            itemStack.shrink(pAmount);
        }
    }
}
