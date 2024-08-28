package xyz.faewulf.diversity;


import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.faewulf.diversity.event_handler.RegsitryDone;
import xyz.faewulf.diversity.platform.RegisterEnchantment;
import xyz.faewulf.diversity.platform.Services;
import xyz.faewulf.diversity.util.ModConfigs;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity() {
        Constants.LOG.info("Loading");

        if (!Services.PLATFORM.isDevelopmentEnvironment())
            MidnightConfig.init(Constants.MOD_ID, ModConfigs.class);

        loadCommand();

        //register enchant
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(RegsitryDone.class);

        RegisterEnchantment.ENCHANTMENTS.register(modEventBus);
        RegisterEnchantment.init();

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
    }
}