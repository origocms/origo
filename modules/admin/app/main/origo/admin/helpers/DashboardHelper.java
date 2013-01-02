package main.origo.admin.helpers;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.event.DashboardEventGenerator;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.Element;

import java.util.Collections;
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

    public static String getDashBoardURL(String dashboard) {
        return routes.Dashboard.dashboard(dashboard).url();
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

}