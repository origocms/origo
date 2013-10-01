package main.origo.core.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.*;
import main.origo.core.annotations.Provides;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.internal.ReflectionInvoker;
import models.origo.core.Segment;
import models.origo.core.Text;
import play.Logger;
import play.data.Form;

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
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(NodeContext.current() != null);
        NodeContext.current().attributes.put(withType, cachedAnnotation.method.getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, args);
    }

    public static <T> T triggerInterceptor(Node node, String providesType, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(NodeContext.current() != null);
        NodeContext.current().attributes.put(withType, cachedAnnotation.method.getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, navigation, args);
    }

    public static <T> T triggerInterceptor(Node node, String providesType, String withType, Form form, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(NodeContext.current() != null);
        NodeContext.current().attributes.put(withType, cachedAnnotation.method.getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, form, args);
    }

    public static <T> T triggerInterceptor(Node node, String providesType, String withType, Segment segment, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(NodeContext.current() != null);
        NodeContext.current().attributes.put(withType, cachedAnnotation.method.getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, segment, args);
    }

    public static <T> T triggerInterceptor(Node node, String providesType, String withType, Text text, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(NodeContext.current() != null);
        NodeContext.current().attributes.put(withType, cachedAnnotation.method.getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, text, args);
    }

    private static CachedAnnotation getCachedAnnotationIfModuleIsEnabled(String providesType, String withType) throws ModuleException {
        CachedAnnotation cachedAnnotation = findInterceptor(providesType, withType);
        if (!ModuleRepository.isEnabled(cachedAnnotation.module)) {
            Logger.debug("Module '" + cachedAnnotation.module.name + "' is disabled");
            throw new ModuleException(cachedAnnotation.module.name, ModuleException.Cause.NOT_ENABLED);
        }
        return cachedAnnotation;
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
