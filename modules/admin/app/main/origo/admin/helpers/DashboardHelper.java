package main.origo.admin.helpers;

import com.google.common.collect.Lists;
import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.event.DashboardEventGenerator;
import main.origo.core.InterceptorRepository;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Provides;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
import play.i18n.Messages;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DashboardHelper {

    public static Element createDashboard(String withType, Node node) throws NodeLoadException {
        OnLoadEventGenerator.triggerBeforeInterceptor(Admin.Type.DASHBOARD, withType, node);
        Element element = ProvidesEventGenerator.triggerInterceptor(Admin.Type.DASHBOARD, withType, node);
        if (element == null) {
            throw new NodeLoadException(node.getNodeId(), "The provider for type [" + withType + "] did not return a Element");
        }

        OnLoadEventGenerator.triggerAfterInterceptor(Admin.Type.DASHBOARD, withType, node);
        return element;
    }

    public static List<Element> createDashboardItems(String withType, Node node) {
        OnLoadEventGenerator.triggerBeforeInterceptor(Admin.Type.DASHBOARD_ITEM, withType, node);
        List<Element> elements = DashboardEventGenerator.triggerProvidesDashboardItemInterceptor(withType, node);
        for (Element element : elements) {
            OnLoadEventGenerator.triggerAfterInterceptor(Admin.Type.DASHBOARD_ITEM, withType, node, Collections.<String, Object>emptyMap(), element);
        }
        return elements;
    }

    public static Element createBasicDashboard(int weight) {
        return new Admin.Dashboard().setWeight(weight).addAttribute("class", "dashboard");
    }

    public static Element createBasicDashboard() {
        return createBasicDashboard(10);
    }

    public static Element createBasicDashboardItem() {
        return new Admin.DashboardItem().addAttribute("class", "item");
    }

    public static Element.ListBulleted createBreadcrumb(String withType) {
        List<String> dashboards = createDashboardTrail(withType);
        List<Element.ListItem> items = createBreadcrumbItems(dashboards);

        return (Element.ListBulleted) new Element.ListBulleted().
                addAttribute("class", "breadcrumb").
                addChildren(items);
    }

    private static List<Element.ListItem> createBreadcrumbItems(List<String> dashboards) {

        List<Element.ListItem> items = Lists.newArrayList();
        for(Iterator<String> dashboardIterator = dashboards.iterator(); dashboardIterator.hasNext(); ) {
            String dashboard = dashboardIterator.next();
            String url;
            if (dashboard.equals(AdminSettingsHelper.getHomeDashboard())) {
                url = routes.Dashboard.index().url();
            } else {
                url = routes.Dashboard.dashboard(dashboard).url();
            }
            if (dashboardIterator.hasNext()) {
                items.add(
                        createBreadcrumbItem(new Element.Anchor().addAttribute("href", url).setBody(Messages.get("breadcrumb."+dashboard)), true)
                );
            } else {
                items.add(
                        createBreadcrumbItem(new Element.Text().setBody(Messages.get("breadcrumb."+dashboard)), false)
                );
            }
        }

        return items;
    }

    private static Element.ListItem createBreadcrumbItem(Element innerElement, boolean addDivider) {
        Element.ListItem listItemElement = (Element.ListItem) new Element.ListItem().addChild(innerElement);
        if (addDivider) {
            listItemElement.addChild(
                    new Element.Span().addAttribute("class", "divider").setBody("/")
            );
        } else {
            listItemElement.addAttribute("class", "active");
        }
        return listItemElement;
    }

    public static List<String> createDashboardTrail(String withType) {
        List<String> dashboards = Lists.newArrayList();
        String current = withType;

        dashboards.add(current);
        do {
            String parent = getParentDashboard(current);
            if (parent != null) {
                dashboards.add(parent);
            }
            current = parent;
        } while (current != null);

        Collections.reverse(dashboards);
        return dashboards;
    }

    public static String getParentDashboard(String withType) {
        return findParentForProvider(Admin.Type.DASHBOARD_ITEM, withType);
    }

    private static String findParentForProvider(final String type, final String withType) {
        List<CachedAnnotation> providers = InterceptorRepository.getInterceptors(Provides.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                Provides annotation = (Provides) cachedAnnotation.annotation;
                return annotation.type().equals(type) &&
                        annotation.with().equals(withType) &&
                        cachedAnnotation.relationship != null && cachedAnnotation.relationship.parent() != null;
            }
        });
        if (!providers.isEmpty()) {
            return providers.iterator().next().relationship.parent();
        } else {
            return null;
        }
    }

}