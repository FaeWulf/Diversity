package xyz.faewulf.diversity;


import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.faewulf.diversity.platform.RegisterEnchantment;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity() {
        Constants.LOG.info("Loading");

        //MidnightConfig.init(Constants.MOD_ID, ModConfigs.class);

        loadCommand();

        //register enchant
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RegisterEnchantment.ENCHANTMENTS.register(modEventBus);

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
    }
}