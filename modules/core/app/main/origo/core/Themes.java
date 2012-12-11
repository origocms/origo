package main.origo.core;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Themes are used to render Nodes. A theme has multiple theme variants, for example 2-columns, 3-columns, top-middle-bottom, etc.
 * Each rootNode has a chosen theme variant to use for rendering.
 * Decorators are applied to each UIElement a RootNode has before rendering.
 */
public class Themes {

    /**
     * Collection of theme variants id's that a theme offers.
     */
    public static Map<String, CachedTheme> themes = new ConcurrentHashMap<String, CachedTheme>();

    /**
     * Maps variant id's to it's parent theme id.
     */
    private static Map<String, String> themeVariantsToThemeMapping = new HashMap<String, String>();

    public static void addTheme(String themeId, Class declaringClass) {
        if (themes.containsKey(themeId) && themes.get(themeId).getDeclaringClass().equals(declaringClass)) {
            throw new InitializationException("Theme [" + themeId + "] declared in both " +
                    declaringClass.getName() + " and " + themes.get(themeId).getDeclaringClass().getName());
        }
        themes.put(themeId, new CachedTheme(themeId, declaringClass));
    }

    public static void addThemeVariant(String themeId, String variantId, String[] regions, Class declaringClass, MethodHandle templateMethod) {
        // Themes are declared on the class level and should be parsed first so we don't need to check if the themeId exists before accessing
        Map<String, CachedThemeVariant> themeVariants = themes.get(themeId).getThemeVariants();

        if (themeVariants.containsKey(variantId)) {
            throw new InitializationException("Duplicate theme variant id [" + variantId + "]");
        }
        themeVariantsToThemeMapping.put(variantId, themeId);
        themeVariants.put(variantId, new CachedThemeVariant(themeId, variantId, declaringClass, templateMethod, new HashSet<String>(Arrays.asList(regions))));
    }

    public static void addDecorator(String themeId, String uiElementType, Class declaringClass, MethodHandle method) {
        // Themes are declared on the class level and should be parsed first so we don't need to check if the themeId exists before accessing
        Map<String, CachedDecorator> themeDecorators = themes.get(themeId).getDecorators();

        themeDecorators.put(uiElementType, new CachedDecorator(declaringClass, method));
    }

    public static CachedTheme getTheme(String themeId) {
        return themes.get(themeId);
    }

    public static CachedThemeVariant getThemeVariant(String themeId, String variantId) {
        return themes.get(themeId).getThemeVariants().get(variantId);
    }

    public static CachedThemeVariant getThemeVariant(String variantId) {
        if (themeVariantsToThemeMapping.containsKey(variantId)) {
            return getThemeVariant(themeVariantsToThemeMapping.get(variantId), variantId);
        } else {
            return null;
        }
    }

    public static Collection<CachedThemeVariant> getThemeVariants(String themeId) {
        if (themes.containsKey(themeId)) {
            return themes.get(themeId).getThemeVariants().values();
        }
        return Collections.emptyList();
    }

    public static Map<String, CachedDecorator> getDecoratorsForTheme(String themeId) {
        if (themes.containsKey(themeId)) {
            Map<String, CachedDecorator> decorators = themes.get(themeId).getDecorators();
            if (decorators != null) {
                return decorators;
            }
        }
        return Collections.emptyMap();
    }

    public static CachedDecorator getDecoratorForTheme(String themeId, String uiElementType) {
        return getDecoratorsForTheme(themeId).get(uiElementType);
    }

    /**
     * Invalidates all themes and all theme variants so that we can reload the themes and theme variants from the classes.
     */
    public static void invalidate() {
        themes.clear();
    }

    public static Map<String, CachedTheme> getThemesMap() {
        return Collections.unmodifiableMap(themes);
    }
}
