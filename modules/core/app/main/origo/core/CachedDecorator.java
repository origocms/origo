package main.origo.core;

import java.lang.invoke.MethodHandle;

public class CachedDecorator {

    public final MethodHandle method;
    public final Class declaringClass;

    public CachedDecorator(Class declaringClass, MethodHandle methodHandle) {
        this.declaringClass = declaringClass;
        this.method = methodHandle;
    }

}
