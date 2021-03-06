package main.origo.core.internal;

import java.lang.reflect.Method;
import java.util.Set;

public class CachedThemeVariant {

    public final String themeId;
    public final String variantId;
    public final Method templateMethod;
    public final Set<String> regions;

    public CachedThemeVariant(String themeId, String variantId, Method templateMethod, Set<String> regions) {
        this.themeId = themeId;
        this.variantId = variantId;
        this.templateMethod = templateMethod;
        this.regions = regions;
    }

}
