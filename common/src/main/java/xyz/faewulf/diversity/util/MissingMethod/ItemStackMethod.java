package xyz.faewulf.diversity.util.MissingMethod;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemStackMethod {

    public static void consume(ItemStack itemStack, int pAmount, @Nullable LivingEntity pEntity) {
        if (pEntity == null || (!(pEntity instanceof Player) || !((Player) pEntity).getAbilities().instabuild)) {
            itemStack.shrink(pAmount);
        }
    }

    public static <T extends LivingEntity> void hurtAndBreak(ItemStack itemStack, int amount, T user, EquipmentSlot equipmentSlot) {
        itemStack.hurtAndBreak(amount, user, livingEntity -> livingEntity.broadcastBreakEvent(equipmentSlot));
    }
}
