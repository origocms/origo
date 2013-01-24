package main.origo.admin.interceptors.content;

import com.google.common.collect.Sets;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.Alias;
import play.data.DynamicForm;

import java.util.Map;
import java.util.Set;

@Interceptor
public class AliasAdminProvider {

    private static final String ALIAS_PARAM = "alias";

    private static Set<String> types = Sets.newHashSet(
            BasicPageAdminProvider.EDIT_TYPE,
            BasicPageAdminProvider.NEW_TYPE
    );

    @OnInsertElement(with = Element.Fieldset.class, after = true)
    public static void addAliasFieldset(OnInsertElement.Context context) {
        AdminPage adminPage = (AdminPage) context.node();
        if (types.contains(adminPage.type) && context.element.getId().equals("basic")) {

            Alias alias = Alias.findFirstAliasForPageId(context.node().getNodeId());

            context.parent.addChild(new Element.Fieldset().
                    addChild(new Element.Legend().setBody("Alias")).
                    addChild(new Element.Panel().setWeight(20).addAttribute("class", "field").
                            addChild(new Element.Label().setWeight(10).setBody("URL part").addAttribute("for", "text-" + ALIAS_PARAM))).
                    addChild(new Element.InputText().setId("text-" + ALIAS_PARAM).addAttribute("name", ALIAS_PARAM).addAttribute("value", alias != null ? alias.path : ""))
            );
        }
    }

    /**
     * Hooks in to the submit process and stores a an alias for a page when the page is submitted.
     */
    @OnSubmit
    public static void storeAlias(OnSubmit.Context context) {

        DynamicForm form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();
        if (data.containsKey(ALIAS_PARAM)) {
            String nodeId = FormHelper.getNodeId(data);
            String path = getUrlPart(data);
            Alias alias = Alias.findFirstAliasForPageId(nodeId);
            if (alias != null) {
                alias.path = path;
            } else {
                new Alias(path, nodeId).save();
            }
        }

    }

    private static String getUrlPart(Map<String, String> data) {
        String path = data.get(ALIAS_PARAM);
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }
}