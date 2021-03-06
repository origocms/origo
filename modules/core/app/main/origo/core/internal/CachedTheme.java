package main.origo.core.internal;

import com.google.common.collect.Maps;
import main.origo.core.ui.Element;

import java.util.List;
import java.util.Map;

public class CachedTheme {

    /**
     * Common identifier for this theme.
     */
    private final String themeId;

    /**
     * The class where the theme is declared.
     */
    private final Class declaringClass;

    /**
     * Collection of cached theme variants, with their template method and the content areas they contain.
     *
     * @see CachedThemeVariant
     */
    public final Map<String, CachedThemeVariant> themeVariants = Maps.newHashMap();

    /**
     * Collection of decorators for each theme that can transform UIElements into elements in a RenderedNode.
     * Each type of Element can have a list of decorators, differing on input type for example.
     *
     * @see main.origo.core.ui.Element
     * @see main.origo.core.ui.RenderedNode
     */
    public final Map<Class<? extends Element>, List<CachedDecorator>> decorators = Maps.newHashMap();

    public CachedTheme(String themeId, Class declaringClass) {
        this.themeId = themeId;
        this.declaringClass = declaringClass;
    }

    public String getThemeId() {
        return themeId;
    }

    public Class getDeclaringClass() {
        return declaringClass;
    }

    public Map<String, CachedThemeVariant> getThemeVariants() {
        return themeVariants;
    }

    public Map<Class<? extends Element>, List<CachedDecorator>> getDecorators() {
        return decorators;
    }

}
