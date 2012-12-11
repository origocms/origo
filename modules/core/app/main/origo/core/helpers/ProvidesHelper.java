package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.CachedAnnotation;
import main.origo.core.Listeners;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.NavigationElement;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Helper to trigger \@Provides origo listeners. Should not be used directly except in core and admin, use NodeHelper
 * instead when creating a new module.
 *
 * @see NodeHelper
 * @see Provides
 */
public class ProvidesHelper {

    public static <T> T triggerListener(String providesType, String withType, Node node) {
        return triggerListener(providesType, withType, node, Maps.<String, Object>newHashMap());
    }

    public static <T> T triggerListener(String providesType, String withType, Node node, Map<String, Object> args) {
        CachedAnnotation cachedAnnotation = findListener(providesType, withType);
        try {
            //noinspection unchecked
            return (T) cachedAnnotation.method.invokeExact(cachedAnnotation.declaringClass, new Provides.Context(node, args));
        } catch (Throwable e) {
            throw new RuntimeException("", e);
        }
    }

    public static <T> T triggerListener(String providesType, String withType, RootNode node, Navigation navigation, Map<String, Object> args) {
        CachedAnnotation cachedAnnotation = findListener(providesType, withType);
        try {
            //noinspection unchecked
            return (T) cachedAnnotation.method.invokeExact(cachedAnnotation.declaringClass, new Provides.Context(node, navigation, args));
        } catch (Throwable e) {
            throw new RuntimeException("", e);
        }
    }

    /**
     * Collects all \@Provides.with for the specified providesType. To be used when choosing a type for a new item for
     * instance or to find all DASHBOARD_ITEM's for the admin module.
     *
     * @param providesType a type to look for (NODE, NAVIGATION, NAVIGATION_ITEM, DASHBOARD_ITEM, etc).
     * @return a list of all "with" added to the system.
     */
    public static Set<String> getAllProvidesWithForType(String providesType) {
        Set<String> providedTypes = new HashSet<String>();
        List<CachedAnnotation> cachedAnnotations = getAllProvidersForType(providesType);
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            providedTypes.add(((Provides) cachedAnnotation.annotation).with());
        }
        return providedTypes;
    }

    private static List<CachedAnnotation> getAllProvidersForType(final String providesType) {
        return Listeners.getListenersForAnnotationType(Provides.class, new CachedAnnotation.ListenerSelector() {
            @Override
            public boolean isCorrectListener(CachedAnnotation listener) {
                return ((Provides) listener.annotation).type().equals(providesType);
            }
        });
    }

    private static CachedAnnotation findListener(String providesType, String withType) {
        CachedAnnotation listener = findProvidersForType(providesType, withType);
        if (listener == null) {
            throw new RuntimeException("Every type (specified by using attribute 'with') must have a class annotated with @Provides to instantiate an instance. Unable to find a provider for type \'" + withType + "\'");
        }
        return listener;
    }

    private static CachedAnnotation findProvidersForType(final String type, final String withType) {
        List<CachedAnnotation> providers = Listeners.getListenersForAnnotationType(Provides.class, new CachedAnnotation.ListenerSelector() {
            @Override
            public boolean isCorrectListener(CachedAnnotation listener) {
                Provides annotation = (Provides) listener.annotation;
                return annotation.type().equals(type) && (annotation.with().equals(withType) || StringUtils.isBlank(withType));
            }
        });
        if (!providers.isEmpty()) {
            if (providers.size() > 1) {
                throw new RuntimeException("Only one @Provides per type (attribute 'with') is allowed");
            }
            return providers.iterator().next();
        } else {
            return null;
        }
    }
}
