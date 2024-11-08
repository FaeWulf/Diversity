package xyz.faewulf.diversity.platform;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.injection.Group;
import xyz.faewulf.diversity.platform.services.IPlatformHelper;
import xyz.faewulf.diversity.util.gameTests.TestGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public List<Class<?>> findClasses(String scannedPackage) {
        List<Class<?>> result = new ArrayList<>();

        ModList.get().getAllScanData().forEach(modFileScanData -> {
            modFileScanData.getClasses().forEach(classData -> {
                if (classData.clazz().getClassName().contains(scannedPackage)) {
                    Class<?> clazz = getClass(classData.clazz().getClassName());
                    if (clazz != null)
                        result.add(clazz);
                }
            });
        });

        return result;
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ignore) {
            return null;
        }
    }

}