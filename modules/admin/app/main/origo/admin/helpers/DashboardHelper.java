package main.origo.admin.helpers;

import com.google.common.collect.Lists;
import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.core.CachedAnnotation;
import main.origo.core.InterceptorRepository;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.OnLoadHelper;
import main.origo.core.helpers.ProvidesHelper;
import main.origo.core.ui.Element;
import play.Logger;

import java.util.Collections;
import java.util.List;

public class DashboardHelper {

    /*
    * Convenience methods for hooks with DASHBOARD type
    */

    public static Element createDashboard(String withType, Node node) throws NodeLoadException {
        DashboardHelper.triggerBeforeDashboardLoaded(withType, node);
        Element element = DashboardHelper.triggerProvidesDashboardInterceptor(withType, node);
        if (element == null) {
            throw new NodeLoadException(node.getNodeId(), "The provider for type [" + withType + "] did not return a Element");
        }

        List<Element> dashboardItems = createDashboardItems(withType, node);
        for (Element dashboardItem : dashboardItems) {
            element.addChild(dashboardItem);
        }

        DashboardHelper.triggerAfterDashboardLoaded(withType, node);
        return element;
    }

    public static Element triggerProvidesDashboardInterceptor(String withType, Node node) {
        return ProvidesHelper.triggerInterceptor(Admin.DASHBOARD, withType, node);
    }

    public static void triggerBeforeDashboardLoaded(String withType, Node node) {
        OnLoadHelper.triggerBeforeInterceptor(Admin.DASHBOARD, withType, node);
    }

    public static void triggerAfterDashboardLoaded(String withType, Node node) {
        OnLoadHelper.triggerAfterInterceptor(Admin.DASHBOARD, withType, node);
    }

    /*
    * Convenience methods for hooks with DASHBOARD_ITEM type
    */

    public static List<Element> createDashboardItems(String withType, Node node) {
        DashboardHelper.triggerBeforeDashboardItemLoaded(withType, node);
        List<Element> element = DashboardHelper.triggerProvidesDashboardItemInterceptor(withType, node);
        DashboardHelper.triggerAfterDashboardItemLoaded(withType, node);
        return element;
    }

    public static List<Element> triggerProvidesDashboardItemInterceptor(String withType, Node node) {
        List<CachedAnnotation> cachedAnnotations = findProvidersWithParent(Admin.DASHBOARD_ITEM, withType);
        List<Element> items = Lists.newArrayList();
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            try {
                //noinspection unchecked
                items.add((Element) cachedAnnotation.method.invoke(null, new Provides.Context(node, Collections.<String, Object>emptyMap())));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
            }
        }
        return items;
    }

    public static void triggerBeforeDashboardItemLoaded(String withType, Node node) {
        OnLoadHelper.triggerBeforeInterceptor(Admin.DASHBOARD_ITEM, withType, node);
    }

    public static void triggerAfterDashboardItemLoaded(String withType, Node node) {
        OnLoadHelper.triggerAfterInterceptor(Admin.DASHBOARD_ITEM, withType, node);
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

    public static String getDashBoardURL(String dashboard) {
        return routes.Dashboard.dashboard(dashboard).url();
    }
}