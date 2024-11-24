package xyz.faewulf.diversity.util.gameTests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestGroup {
    String name() default "ungroup";     // Config name to use in the file
}
