package main.origo.core;

import com.google.common.collect.Maps;
import main.origo.core.helpers.SettingsCoreHelper;
import main.origo.core.themes.DefaultTheme;
import main.origo.core.ui.Element;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Themes are used to render Nodes. A theme has multiple theme variants, for example 2-columns, 3-columns, top-middle-bottom, etc.
 * Each rootNode has a chosen theme variant to use for rendering.
 * Decorators are applied to each Element a RootNode has before rendering.
 */
public class Themes {

    /**
     * Collection of theme variants id's that a theme offers.
     */
    public static Map<String, CachedTheme> themes = Maps.newConcurrentMap();

    /**
     * Maps variant id's to it's parent theme id.
     */
    private static Map<String, String> themeVariantsToThemeMapping = Maps.newHashMap();

    public static void addTheme(String themeId, Class declaringClass) {
        if (themes.containsKey(themeId) && themes.get(themeId).getDeclaringClass().equals(declaringClass)) {
            throw new InitializationException("Theme [" + themeId + "] declared in both " +
                    declaringClass.getName() + " and " + themes.get(themeId).getDeclaringClass().getName());
        }
        themes.put(themeId, new CachedTheme(themeId, declaringClass));
    }

    public static void addThemeVariant(String themeId, String variantId, String[] regions, Method templateMethod) {
        // Themes are declared on the class level and should be parsed first so we don't need to check if the themeId exists before accessing
        Map<String, CachedThemeVariant> themeVariants = themes.get(themeId).getThemeVariants();

        if (themeVariants.containsKey(variantId)) {
            throw new InitializationException("Duplicate theme variant id [" + variantId + "]");
        }
        themeVariantsToThemeMapping.put(variantId, themeId);
        themeVariants.put(variantId, new CachedThemeVariant(themeId, variantId, templateMethod, new HashSet<>(Arrays.asList(regions))));
    }

    public static void addDecorator(String themeId, Class[] uiElementType, Method method) {
        // Themes are declared on the class level and should be parsed first so we don't need to check if the themeId exists before accessing
        Map<Class<? extends Element>, CachedDecorator> themeDecorators = themes.get(themeId).getDecorators();

        for (Class type : uiElementType) {
            themeDecorators.put(type, new CachedDecorator(type, method));
        }
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

    public static Map<Class<? extends Element>, CachedDecorator> getDecoratorsForTheme(String themeId) {
        if (themes.containsKey(themeId)) {
            Map<Class<? extends Element>, CachedDecorator> decorators = themes.get(themeId).getDecorators();
            if (decorators != null) {
                return decorators;
            }
        }
        return Collections.emptyMap();
    }

    public static CachedDecorator getDecoratorForTheme(String themeId, Class<? extends Element> elementType) {
        return getDecoratorsForTheme(themeId).get(elementType);
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

    public static Collection<CachedThemeVariant> getAvailableThemeVariants() {

        String theme = SettingsCoreHelper.getTheme();
        if (!StringUtils.isBlank(theme)) {
            return getThemeVariants(theme);
        }
        Logger.debug("No theme set, using default.");
        return getThemeVariants(DefaultTheme.ID);
    }
}
