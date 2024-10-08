package xyz.faewulf.diversity.util.mixinPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Define the annotation to specify the class and field
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) // This targets classes (Mixins)
public @interface ConditionalMixin {
    Class<?> configClass(); // The class containing the static field

    String fieldName();     // The static field name
}