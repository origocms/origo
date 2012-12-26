package main.origo.core.event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import main.origo.core.InterceptorRepository;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.Provides;
import main.origo.core.internal.CachedAnnotation;
import play.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper to trigger \@Provides origo interceptors. Should not be used directly except in core and admin, use NodeHelper
 * instead when creating a new module.
 *
 * @see main.origo.core.helpers.NodeHelper
 * @see Provides
 */
public class ProvidesEventGenerator {

    public static <T> T triggerInterceptor(String providesType, String withType, Node node) {
        return triggerInterceptor(providesType, withType, node, Maps.<String, Object>newHashMap());
    }

    public static <T> T triggerInterceptor(String providesType, String withType, Node node, Map<String, Object> args) {
        CachedAnnotation cachedAnnotation = findInterceptor(providesType, withType);
        try {
            //noinspection unchecked
            return (T) cachedAnnotation.method.invoke(null, new Provides.Context(node, args));
        } catch (Throwable e) {
            Logger.error("", e);
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
        }
    }

    public static <T> T triggerInterceptor(String providesType, String withType, Node node, Navigation navigation, Map<String, Object> args) {
        CachedAnnotation cachedAnnotation = findInterceptor(providesType, withType);
        try {
            //noinspection unchecked
            return (T) cachedAnnotation.method.invoke(null, new Provides.Context(node, navigation, args));
        } catch (Throwable e) {
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
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
        Set<String> providedTypes = Sets.newHashSet();
        List<CachedAnnotation> cachedAnnotations = getAllProvidersForType(providesType);
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            providedTypes.add(((Provides) cachedAnnotation.annotation).with());
        }
        return providedTypes;
    }

    private static List<CachedAnnotation> getAllProvidersForType(final String providesType) {
        return InterceptorRepository.getInterceptors(Provides.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cacheAnnotation) {
                return ((Provides) cacheAnnotation.annotation).type().equals(providesType);
            }
        });
    }

    private static CachedAnnotation findInterceptor(String providesType, String withType) {
        CachedAnnotation cacheAnnotation = findProvidersForType(providesType, withType);
        if (cacheAnnotation == null) {
            throw new RuntimeException("Every type (specified by using attribute 'with') must have a class annotated with @Provides to instantiate an instance. Unable to find a provider for type \'" + withType + "\'");
        }
        return cacheAnnotation;
    }

    private static CachedAnnotation findProvidersForType(final String type, final String withType) {
        List<CachedAnnotation> providers = InterceptorRepository.getInterceptors(Provides.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                Provides annotation = (Provides) cachedAnnotation.annotation;
                return annotation.type().equals(type) && annotation.with().equals(withType);
            }
        });
        return EventGeneratorUtils.selectEventHandler(withType, providers);
    }

}
