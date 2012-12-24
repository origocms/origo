package main.origo.core.helpers.forms;

import controllers.origo.core.routes;
import main.origo.core.Node;
import main.origo.core.helpers.SettingsCoreHelper;
import main.origo.core.ui.Element;
import play.api.mvc.Call;

import java.util.Map;

public class FormHelper {

    private static final String NODE_ID = "_core_node_id";
    private static final String NODE_VERSION = "_core_node_version";

    public static Element createFormElement(Node node, String withType) {
        return createFormElement(SettingsCoreHelper.getDefaultFormType(), node, withType);
    }

    public static Element createFormElement(String formType, Node node, String nodeType) {
        OnLoadFormHelper.triggerBeforeInterceptor(formType, node);
        OnLoadFormHelper.triggerBeforeInterceptor(nodeType, node);
        Element formElement = ProvidesFormHelper.triggerInterceptor(formType, node);
        addNodeIdAndVersion(formElement, node);
        OnLoadFormHelper.triggerAfterInterceptor(nodeType, node, formElement);
        OnLoadFormHelper.triggerAfterInterceptor(formType, node, formElement);
        return formElement;
    }

    public static Call getPostURL() {
        return routes.Submit.submit();
    }

    public static String getNodeIdParamName() {
        return NODE_ID;
    }

    public static String getNodeVersionParamName() {
        return NODE_VERSION;
    }

    public static String getNodeId(Map<String,String> data) {
        return data.get(NODE_ID);
    }

    public static Integer getNodeVersion(Map<String,String> data) {
        try {
            return Integer.parseInt(data.get(NODE_VERSION));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Version is not a number: " + e.getLocalizedMessage(), e);
        }
    }

    public static void addNodeIdAndVersion(Element form, Node node) {
        form.
                addChild(new Element.InputHidden().addAttribute("name", FormHelper.getNodeIdParamName()).addAttribute("value", node.getNodeId())).
                addChild(new Element.InputHidden().addAttribute("name", FormHelper.getNodeVersionParamName()).addAttribute("value", String.valueOf(node.getVersion())));
    }
}
