package xyz.faewulf.diversity.util.MissingMethod;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;

public class LivingEntityMethod {
    public static EquipmentSlot getSlotForHand(InteractionHand pHand) {
        return pHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }
}
