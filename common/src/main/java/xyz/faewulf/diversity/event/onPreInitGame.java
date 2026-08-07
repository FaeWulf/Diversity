package xyz.faewulf.diversity.event;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.platform.Services;
import xyz.faewulf.lib.util.gameTests.TestGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class onPreInitGame {
    public static void run() {

        if (!Services.PLATFORM.isDevelopmentEnvironment())
            return;

        List<String> paths = new ArrayList<>();

        paths.add("xyz.faewulf.diversity.util.gameTests.entry");

        try {
            for (String path : paths) {
                List<Class<?>> testClassList = xyz.faewulf.lib.platform.Services.PLATFORM.findClasses(path);

                testClassList.forEach(aClass -> {
                    if (aClass.isAnnotationPresent(TestGroup.class)) {
                        String[] pkgFullPath = aClass.getPackage().getName().split("\\.");
                        String pkgName = "";

                        if (pkgFullPath.length > 0)
                            pkgName = pkgFullPath[pkgFullPath.length - 1];

                        for (Method method : aClass.getDeclaredMethods()) {

                            if (method.isSynthetic()) {
                                continue;
                            }

                            String className = aClass.getSimpleName().toLowerCase();
                            String testName = pkgName + "_" + className + "_" + method.getName().toLowerCase();

                            Registry.register(
                                    BuiltInRegistries.TEST_FUNCTION,
                                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, testName),
                                    (GameTestHelper helper) -> {
                                        try {
                                            method.invoke(aClass.getDeclaredConstructor().newInstance(), helper);
                                        } catch (NullPointerException | IllegalArgumentException |
                                                 IllegalAccessException | InvocationTargetException |
                                                 NoSuchMethodException | InstantiationException e) {
                                            Constants.LOG.error(e.toString());
                                        }
                                    }
                            );
                        }
                    }
                });
            }
        } catch (IllegalArgumentException a) {
            Constants.LOG.error(a.getMessage());
        }
    }
}
