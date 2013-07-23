package main.origo.admin.helpers;

import com.google.common.collect.Lists;
import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.event.DashboardEventGenerator;
import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Provides;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
import org.apache.commons.lang3.StringUtils;
import play.i18n.Messages;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DashboardHelper {

    private static final String PREFIX = "breadcrumb.origo.admin.dashboard.";

    public static Element createDashboard(Node node, String withType) throws NodeLoadException, ModuleException {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Admin.Type.DASHBOARD, withType);
        Element element = ProvidesEventGenerator.triggerInterceptor(node, Admin.Type.DASHBOARD, withType);
        if (element == null) {
            throw new NodeLoadException(node.nodeId(), "The provider for type [" + withType + "] did not return a Element");
        }

        OnLoadEventGenerator.triggerAfterInterceptor(node, Admin.Type.DASHBOARD, withType);
        return element;
    }

    public static List<Element> createDashboardItems(Node node, String withType) {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Admin.Type.DASHBOARD_ITEM, withType);
        List<Element> elements = DashboardEventGenerator.triggerProvidesDashboardItemInterceptor(node, withType);
        for (Element element : elements) {
            OnLoadEventGenerator.triggerAfterInterceptor(node, Admin.Type.DASHBOARD_ITEM, withType, element, Collections.<String, Object>emptyMap());
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
        return createBreadcrumb(withType, null);
    }

    public static Element.ListBulleted createBreadcrumb(String withType, String extraBullet) {
        List<String> dashboards = createDashboardTrail(withType);
        if (StringUtils.isNotBlank(extraBullet)) {
            dashboards.add(extraBullet);
        }
        return createBreadcrumb(dashboards);
    }

    private static Element.ListBulleted createBreadcrumb(List<String> dashboards) {
        List<Element.ListItem> items = createBreadcrumbItems(dashboards);

        return new Element.ListBulleted().
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
                        createBreadcrumbItem(new Element.Anchor().addAttribute("href", url).setBody(Messages.get(PREFIX +dashboard)), true)
                );
            } else {
                items.add(
                        createBreadcrumbItem(new Element.Text().setBody(Messages.get(PREFIX +dashboard)), false)
                );
            }
        }

        return items;
    }

    private static Element.ListItem createBreadcrumbItem(Element innerElement, boolean addDivider) {
        Element.ListItem listItemElement = new Element.ListItem().addChild(innerElement);
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
        Set<CachedAnnotation> providers = InterceptorRepository.getInterceptors(Provides.class, new CachedAnnotation.InterceptorSelector() {
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