package xyz.faewulf.diversity;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.minecraft.world.item.BoneMealItem;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity(IEventBus eventBus) {
        Constants.LOG.info("Loading");

        CommonClass.init();

        Constants.LOG.info("Init done");
    }
}