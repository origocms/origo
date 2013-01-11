package main.origo.core.internal;

import main.origo.core.InitializationException;
import main.origo.core.annotations.Module;

import java.lang.reflect.Method;

public class CachedModule implements Comparable<CachedModule> {

    public final Class clazz;

    public final Module annotation;
    public final Module.Version moduleVersion;

    public final Method initMethod;
    public final Method annotationsMethod;
    public final Method dependenciesMethod;

    public CachedModule(Class clazz, Module annotation, Module.Version moduleVersion, Method initMethod, Method annotationsMethod, Method dependencies) {
        if (annotation.order() <= 0 && !annotation.name().equals("core")) {
            throw new InitializationException("Order must be higher than 0");
        }
        this.annotation = annotation;
        this.clazz = clazz;
        this.moduleVersion = moduleVersion;
        this.initMethod = initMethod;
        this.annotationsMethod = annotationsMethod;
        this.dependenciesMethod = dependencies;
    }

    @Override
    public int compareTo(CachedModule that) {
        return new Integer(annotation.order()).compareTo(that.annotation.order());
    }

}
