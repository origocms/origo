package main.origo.core;

import java.lang.reflect.Method;

public class CachedDecorator {

    public final String uiElementType;
    public final Method method;

    public CachedDecorator(String uiElementType, Method method) {
        this.uiElementType = uiElementType;
        this.method = method;
    }

}
