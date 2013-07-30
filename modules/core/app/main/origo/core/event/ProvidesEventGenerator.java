package main.origo.core.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.*;
import main.origo.core.annotations.Provides;
import main.origo.core.internal.CachedAnnotation;
import play.Logger;

import java.lang.reflect.InvocationTargetException;
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

    public static <T> T triggerInterceptor(Node node, String providesType, String withType) throws NodeLoadException, ModuleException {
        return triggerInterceptor(node, providesType, withType, Maps.<String, Object>newHashMap());
    }

    public static <T> T triggerInterceptor(Node node, String providesType, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        return triggerInterceptor(node, providesType, withType, null, args);
    }

    public static <T> T triggerInterceptor(Node node, String providesType, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = findInterceptor(providesType, withType);
        if (!ModuleRepository.isEnabled(cachedAnnotation.module)) {
            Logger.debug("Module '" + cachedAnnotation.module.name + "' is disabled");
            throw new ModuleException(cachedAnnotation.module.name, ModuleException.Cause.NOT_ENABLED);
        }
        try {
            assert(NodeContext.current() != null);
            NodeContext.current().attributes.put(withType, cachedAnnotation.method.getDeclaringClass());
            //noinspection unchecked
            return (T) cachedAnnotation.method.invoke(null, new Provides.Context(node, navigation, args));
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ModuleException) {
                throw (ModuleException) e.getCause();
            } else if (e.getCause() instanceof NodeLoadException) {
                throw (NodeLoadException) e.getCause();
            } else {
                throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
        }
    }

    /**
     * Filters out the cached providers types.
     * @return a list of all the classes that provides a matching nodeType and a withType
     * @see main.origo.core.annotations.Provides
     * @see main.origo.core.annotations.Core
     */
    public static Set<CachedAnnotation> getAllProviders(final String type, final String with) {
        return InterceptorRepository.getInterceptors(Provides.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cacheAnnotation) {
                Provides annotation = (Provides) cacheAnnotation.annotation;
                return annotation.type().equals(type) && annotation.with().equals(with);
            }
        });
    }

    /**
     * Filters out the cached providers types.
     * @return a list of all the classes that provides a matching nodeType
     * @see main.origo.core.annotations.Provides
     * @see main.origo.core.annotations.Core
     */
    public static Set<CachedAnnotation> getAllProviders(final String type) {
        return InterceptorRepository.getInterceptors(Provides.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cacheAnnotation) {
                Provides annotation = (Provides) cacheAnnotation.annotation;
                return annotation.type().equals(type);
            }
        });
    }

    public static CachedAnnotation findInterceptor(final String nodeType, final String withType) {
        List<CachedAnnotation> providers = Lists.newArrayList(InterceptorRepository.getInterceptors(Provides.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                Provides annotation = (Provides) cachedAnnotation.annotation;
                return annotation.type().equals(nodeType) && annotation.with().equals(withType);
            }
        }));

        CachedAnnotation cacheAnnotation = EventGeneratorUtils.selectEventHandler(Provides.class, nodeType, withType, providers);
        if (cacheAnnotation == null) {
            throw new RuntimeException("Every type (specified by using attribute 'with') must have a class annotated with @Provides to instantiate an instance. Unable to find a provider for type '" + nodeType + "' with '"+withType+"'");
        }
        return cacheAnnotation;
    }

}
