package main.origo.core.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Module {

    String name();

    /**
     * Order in which modules get loaded, lowest number loaded first.
     * Must be bigger than 0.
     * @return sort order
     */
    int order();

    /**
     * The packages to scan for
     * @return
     */
    String[] packages();

    /**
     *  The version of this module.
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Version {
        int major();
        int minor();
        int patch();
    }

    /**
     * An [optional] init method
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Init {
    }

    /**
     * An [optional] method that MUST return a List of AnnotationProcessor.Prototype
     * @see main.origo.core.internal.AnnotationProcessor.Prototype
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Annotations {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Dependencies {
    }

}
