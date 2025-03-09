package xyz.faewulf.diversity_better_bundle.event_handler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import xyz.faewulf.diversity_better_bundle.Constants;
import xyz.faewulf.diversity_better_bundle.platform.RegisterEnchantment;
import xyz.faewulf.diversity_better_bundle.util.CustomEnchant;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class RegsitryDone {

    @SubscribeEvent
    public static void onDone(FMLCommonSetupEvent event) {
        if (RegisterEnchantment.CAPACITY != null)
            CustomEnchant.CAPACITY = RegisterEnchantment.CAPACITY.get();

        if (RegisterEnchantment.REFILL != null)
            CustomEnchant.REFILL = RegisterEnchantment.REFILL.get();

        if (RegisterEnchantment.VACUUM != null)
            CustomEnchant.VACUUM = RegisterEnchantment.VACUUM.get();

        if (RegisterEnchantment.SELECTIVE_VACUUM != null)
            CustomEnchant.SELECTIVE_VACUUM = RegisterEnchantment.SELECTIVE_VACUUM.get();
    }
}
