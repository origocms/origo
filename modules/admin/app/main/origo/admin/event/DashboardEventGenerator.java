package main.origo.admin.event;

import com.google.common.collect.Lists;
import main.origo.admin.annotations.Admin;
import main.origo.core.InterceptorRepository;
import main.origo.core.Node;
import main.origo.core.annotations.Provides;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
import play.Logger;

import java.util.Collections;
import java.util.List;

public class DashboardEventGenerator {

    /*
    * Convenience methods for hooks with DASHBOARD_ITEM type
    */

    public static List<Element> triggerProvidesDashboardItemInterceptor(String withType, Node node) {
        List<CachedAnnotation> cachedAnnotations = findProvidersWithParent(Admin.DASHBOARD_ITEM, withType);
        List<Element> items = Lists.newArrayList();
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            try {
                //noinspection unchecked
                Element element = (Element) cachedAnnotation.method.invoke(null, new Provides.Context(node, Collections.<String, Object>emptyMap()));
                OnLoadEventGenerator.triggerAfterInterceptor(Admin.DASHBOARD_ITEM, withType, node, Collections.<String, Object>emptyMap(), element);
                items.add(element);
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
            }
        }
        return items;
    }

    private static List<CachedAnnotation> findProvidersWithParent(final String withType, final String parent) {
        List<CachedAnnotation> providers = InterceptorRepository.getInterceptor(Provides.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                Provides annotation = (Provides) cachedAnnotation.annotation;
                return annotation.type().equals(withType) &&
                        cachedAnnotation.relationship != null && cachedAnnotation.relationship.parent().equals(parent);
            }
        });
        if (providers.isEmpty()) {
            Logger.warn("Every type (specified by using attribute 'with') must have a class annotated with @Provides to instantiate an instance. Unable to find a provider for type \'" + withType + "\'");
        }
        return providers;
    }


}
