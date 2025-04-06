package xyz.faewulf.diversity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity(IEventBus eventBus) {
        Constants.LOG.info("Loading");

        //MidnightConfig.init(Constants.MOD_ID, ModConfigs.class);

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private static void registerGameTest() {
        DeferredRegister<Consumer<GameTestHelper>> GAME_TEST = DeferredRegister.create(
                BuiltInRegistries.TEST_FUNCTION,
                Constants.MOD_ID
        );
    }
}