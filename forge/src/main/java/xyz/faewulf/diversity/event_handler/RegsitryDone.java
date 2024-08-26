package xyz.faewulf.diversity.event_handler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.platform.RegisterEnchantment;
import xyz.faewulf.diversity.util.CustomEnchant;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class RegsitryDone {

    @SubscribeEvent
    public static void onDone(FMLCommonSetupEvent event) {
        CustomEnchant.CAPACITY = RegisterEnchantment.CAPACITY.get();
        CustomEnchant.REFILL = RegisterEnchantment.REFILL.get();
        CustomEnchant.BACKUP_PROTECTION = RegisterEnchantment.BACKUP_PROTECTION.get();
        CustomEnchant.BACKUP_FIRE_PROTECTION = RegisterEnchantment.BACKUP_FIRE_PROTECTION.get();
        CustomEnchant.BACKUP_BLAST_PROTECTION = RegisterEnchantment.BACKUP_BLAST_PROTECTION.get();
        CustomEnchant.BACKUP_PROJECTILE_PROTECTION = RegisterEnchantment.BACKUP_PROJECTILE_PROTECTION.get();
    }
}
