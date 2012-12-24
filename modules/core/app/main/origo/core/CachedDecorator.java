package main.origo.core;

import java.lang.reflect.Method;

public class CachedDecorator {

    public final Class uiElementType;
    public final Class inputType;
    public final Method method;

    public CachedDecorator(Class uiElementType, Method method) {
        this.uiElementType = uiElementType;
        this.method = method;
        this.inputType = null;
    }

    public CachedDecorator(Class uiElementType, Method method, Class inputType) {
        this.uiElementType = uiElementType;
        this.method = method;
        this.inputType = inputType;
    }

}
