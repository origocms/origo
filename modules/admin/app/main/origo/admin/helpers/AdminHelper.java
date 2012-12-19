package main.origo.admin.helpers;

import controllers.origo.admin.routes;
import main.origo.core.Node;
import main.origo.core.helpers.SettingsHelper;
import main.origo.core.ui.UIElement;
import models.origo.core.Content;

import java.util.HashMap;
import java.util.Map;

public class AdminHelper {

    public static String getURLForAdminAction(String type) {

        return routes.Dashboard.pageWithType(type).url();

/*
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("type", type);
        Router.ActionDefinition actionDefinition = Router.reverse(Application.class.getName() + ".pageWithType", args);
        if (actionDefinition != null) {
            return actionDefinition.url;
        }
*/
    }

    public static String getURLForAdminAction(String type, String identifier) {

        return routes.Dashboard.pageWithTypeAndIdentifier(type, identifier).url();
/*
        Router.ActionDefinition actionDefinition = Router.reverse(Application.class.getName() + ".pageWithTypeAndIdentifier", args);
        if (actionDefinition != null) {
            return actionDefinition.url;
        }
*/
    }

    public static UIElement createRichTextEditor(Node node, Content content) {
        String editorType = SettingsHelper.Admin.getEditorType();
        EditorHelper.triggerBeforeRichTextEditorLoaded(editorType, node, content);
        UIElement richTextEditor = EditorHelper.triggerProvidesRichTextEditorListener(editorType, node, content);
        EditorHelper.triggerAfterRichTextEditorLoaded(editorType, node, richTextEditor, content);
        return richTextEditor;
    }

    public static UIElement createDashboardItem(String dashboardItemName, Node node) {
        DashboardHelper.triggerBeforeDashboardItemLoaded(dashboardItemName, node);
        UIElement uiElement = DashboardHelper.triggerProvidesDashboardItemListener(dashboardItemName, node);
        DashboardHelper.triggerAfterDashboardItemLoaded(dashboardItemName, node);
        return uiElement;
    }
}
