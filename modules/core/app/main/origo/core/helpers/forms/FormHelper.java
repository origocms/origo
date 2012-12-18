package main.origo.core.helpers.forms;

import main.origo.core.Node;
import main.origo.core.helpers.SettingsHelper;
import main.origo.core.ui.UIElement;

import java.util.Collections;

public class FormHelper {

    private static final String NODE_ID = "_core_node_id";
    private static final String NODE_VERSION = "_core_node_version";

    public static UIElement createFormElement(Node node, String withType) {
        return createFormElement(SettingsHelper.Core.getDefaultFormProviderType(), node, withType);
    }

    public static UIElement createFormElement(String formProviderType, Node node, String nodeType) {
        OnLoadFormHelper.triggerBeforeInterceptor(formProviderType, node, nodeType);
        UIElement formElement = ProvidesFormHelper.triggerInterceptor(formProviderType, node, nodeType);
        addNodeIdAndVersion(formElement, node);
        OnLoadFormHelper.triggerAfterInterceptor(formProviderType, node, nodeType);
        return formElement;
    }

    public static String getPostURL() {
        //return URLHelper.getReverseURL(SubmitController.class, "submit");
        return "";
    }

    public static String getNodeIdParamName() {
        return NODE_ID;
    }

    public static String getNodeVersionParamName() {
        return NODE_VERSION;
    }

    /*
    public static String getNodeId(Scope.Params params) {
        return params.get(NODE_ID);
    }

    public static Long getNodeVersion(Scope.Params params) {
        try {
            return Long.parseLong(params.get(NODE_VERSION));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Version is not a number: " + e.getLocalizedMessage(), e);
        }
    }
    */

    public static void addNodeIdAndVersion(UIElement form, Node node) {
        form.
                addChild(new UIElement(UIElement.INPUT_HIDDEN).addAttribute("name", FormHelper.getNodeIdParamName()).addAttribute("value", node.getNodeId())).
                addChild(new UIElement(UIElement.INPUT_HIDDEN).addAttribute("name", FormHelper.getNodeVersionParamName()).addAttribute("value", String.valueOf(node.getVersion())));
    }
}
