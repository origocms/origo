package main.origo.core.event.forms;

import main.origo.core.InterceptorRepository;
import main.origo.core.Node;
import main.origo.core.annotations.forms.ProvidesForm;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.internal.CachedAnnotation;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.util.*;

/**
 * Helper to trigger \@ProvidesForm origo interceptors. Should not be used directly except in core and admin, use NodeHelper
 * instead when creating a new module.
 *
 * @see main.origo.core.helpers.NodeHelper
 * @see ProvidesForm
 */
public class ProvidesFormEventGenerator {

    public static <T> T triggerInterceptor(String withType, Node node) {
        //noinspection unchecked
        return (T) triggerInterceptor(withType, node, Collections.<String, Object>emptyMap());
    }

    public static <T> T triggerInterceptor(String withType, Node node, Map<String, Object> args) {
        CachedAnnotation cachedAnnotation = findInterceptor(withType);
        try {
            //noinspection unchecked
            return (T) cachedAnnotation.method.invoke(null, new ProvidesForm.Context(node, args));
        } catch (Throwable e) {
            Logger.error("", e);
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
        }
    }

    /**
     * Collects all \@Provides.with for the specified providesType. To be used when choosing a type for a new item for
     * instance or to find all DASHBOARD_ITEM's for the admin module.
     *
     * @return a list of all "with" added to the system.
     */
    public static Set<String> getAllProvidesForm() {
        Set<String> providedTypes = new HashSet<String>();
        List<CachedAnnotation> cachedAnnotations = InterceptorRepository.getInterceptors(ProvidesForm.class);
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            providedTypes.add(((ProvidesForm) cachedAnnotation.annotation).with());
        }
        return providedTypes;
    }

    private static CachedAnnotation findInterceptor(String withType) {
        CachedAnnotation Interceptor = findProvidersForType(withType);
        if (Interceptor == null) {
            Interceptor = findProvidersForType(CoreSettingsHelper.getDefaultFormType());
            if (Interceptor == null) {
                throw new RuntimeException("Unable to find a form provider for type \'" + withType + "\' and the default form provider from settings is also not available");
            }
        }
        return Interceptor;
    }

    private static CachedAnnotation findProvidersForType(final String withType) {
        List<CachedAnnotation> providers = InterceptorRepository.getInterceptors(ProvidesForm.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation Interceptor) {
                ProvidesForm annotation = (ProvidesForm) Interceptor.annotation;
                return (annotation.with().equals(withType) || StringUtils.isBlank(withType));
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
