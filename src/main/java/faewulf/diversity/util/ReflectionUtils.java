package faewulf.diversity.util;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Method;
import java.util.Set;

public class ReflectionUtils {

    public static void invokeMethodOnClasses(String packageName, String methodName, Class<?> parameterType, Object parameterValue) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.SubTypes.filterResultsBy(s -> true))
                .filterInputsBy(new FilterBuilder().includePackage(packageName)));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        for (Class<?> clazz : classes) {
            try {
                Method method = clazz.getMethod(methodName, parameterType);
                method.invoke(clazz.getDeclaredConstructor().newInstance(), parameterValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void invokeClasses(String packageName, String methodName) {

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.SubTypes.filterResultsBy(s -> true))
                .filterInputsBy(new FilterBuilder().includePackage(packageName)));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);


        for (Class<?> clazz : classes) {
            try {
                Method method = clazz.getMethod(methodName);
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
