package main.origo.admin.internal;

import main.origo.core.annotations.Interceptor;
import org.reflections.Reflections;

import java.util.Set;

public class AdminAnnotationProcessor {

    public static void initialize() {
        scan();
    }

    public static void scan() {
        Reflections reflections = new Reflections("");

        Set<Class<?>> interceptors = reflections.getTypesAnnotatedWith(Interceptor.class);

    }
}
