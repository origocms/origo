package main.origo.admin.interceptors.content;

import main.origo.core.Node;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import models.origo.core.Alias;
import org.apache.commons.lang3.StringUtils;
import play.data.DynamicForm;

import java.util.Map;

@Interceptor
public class AliasPageAdminProvider {

    private static final String USE_ALIAS_PARAM = "use_alias";
    private static final String ALIAS_VALUE_PARAM = "alias";

    @OnInsertElement(with = Element.FieldSet.class, after = true)
    public static void addAliasFieldSet(Node node, Element parent, Element element) {
        // TODO: Hard coded for now, should be moved to configuration
        if (BasicPageAdminProvider.TYPE.equals(node.nodeType()) && element.getId().equals("content")) {

            Alias alias = Alias.findFirstAliasForPageId(node.nodeId());

            Element.InputCheckbox useAliasCheckbox = new Element.InputCheckbox(Boolean.class).
                    addAttribute("name", USE_ALIAS_PARAM).
                    addAttribute("value", "true");
            if (alias != null) {
                useAliasCheckbox.addAttribute("checked", "checked");
            }
            parent.addChild(new Element.FieldSet().setWeight(100).
                    addChild(new Element.Legend().setBody("Alias")).
                    addChild(new Element.Panel().
                            addChild(new Element.Label().addAttribute("class", "checkbox").
                                    addChild(useAliasCheckbox).
                                    setBody("Add Alias"))).
                    addChild(new Element.Panel().setWeight(20).addAttribute("class", "field").
                            addChild(new Element.Label().setWeight(10).setBody("URL part").addAttribute("for", "text-" + ALIAS_VALUE_PARAM))).
                    addChild(new Element.InputText().setId("text-" + ALIAS_VALUE_PARAM).addAttribute("name", ALIAS_VALUE_PARAM).addAttribute("value", alias != null ? alias.path : ""))
            );
        }
    }

    /**
     * Hooks in to the submit process and stores an alias for a page when the page is submitted.
     */
    @OnSubmit(weight = 1000)
    public static Boolean storeAlias() {

        DynamicForm form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();
        if (!StringUtils.isEmpty(data.get(USE_ALIAS_PARAM))) {
            if (data.containsKey(ALIAS_VALUE_PARAM)) {
                String nodeId = FormHelper.getNodeId(data);
                String path = getUrlPart(data);
                Alias alias = Alias.findFirstAliasForPageId(nodeId);
                if (alias != null) {
                    alias.path = path;
                    alias.update();
                } else {
                    new Alias(path, nodeId).create();
                }
            }
        } else {
            String nodeId = FormHelper.getNodeId(data);
            Alias alias = Alias.findFirstAliasForPageId(nodeId);
            if (alias != null) {
                alias.delete();
            }
        }
        return true;
    }

    private static String getUrlPart(Map<String, String> data) {
        String path = data.get(ALIAS_VALUE_PARAM);
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }
}