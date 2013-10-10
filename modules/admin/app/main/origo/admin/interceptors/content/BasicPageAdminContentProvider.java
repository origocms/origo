package main.origo.admin.interceptors.content;

import main.origo.admin.annotations.Admin;
import main.origo.admin.forms.BasicPageForm;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import play.data.Form;

import java.util.Map;

@Interceptor
public class BasicPageAdminContentProvider {


    /*
     * Content
     */

    @OnLoad(with = Admin.With.TAB_BAR)
    public static void addTabItem(Node node, String withType, Element element, Map<String, Object> args) throws ModuleException, NodeLoadException {

        element.addChild(new Admin.TabItem().setWeight(300).addChild(new Element.Anchor().addAttribute("href", "#contentTab").setBody("Content")));

    }

    @OnLoad(with = Admin.With.TAB_CONTENT)
    public static void loadNewPage(Node node, String withType, Element element, Map<String, Object> args) throws ModuleException, NodeLoadException {

        Element contentFieldSet = new Element.FieldSet().setId("content").
                addChild(new Element.Legend().setBody("Content"));

        element.addChild(
                new Admin.TabPane().setId("contentTab").
                        addChild(new Element.Panel(new Element.Heading3().setBody("Content")).
                                addChild(contentFieldSet)
                        ));

        Form<BasicPageForm> form = FormHelper.getValidationResult(BasicPageForm.class);

        Element actionButton = new Element.DropDownButton(new Element.Button().setBody("Action").addChild(new Element.Span().addAttribute("class", "caret"))).
                addChildren(
                        new Element.Anchor().setBody("Add Content"),
                        new Element.Anchor().setBody("Remove Content"),
                        new Element.Divider(),
                        new Element.Anchor().setBody("Manage Content")
                );
        contentFieldSet.addChild(actionButton);

//        contentFieldSet.addChild(
//                FormHelper.createField(form,
//                        new Element.Label().setWeight(10).setBody("Lead").addAttribute("for", LEAD_PARAM),
//                        EditorHelper.createRichTextEditor(node, new Text(FormHelper.getFieldValue(form, LEAD_PARAM))).
//                                setWeight(20).addAttribute("class", "editor richtext").
//                                addAttribute("name", LEAD_PARAM).addAttribute("cols", "80").addAttribute("rows", "10")
//                )
//        );
//
//        contentFieldSet.addChild(
//                FormHelper.createField(form,
//                        new Element.Label().setWeight(10).setBody("Body").addAttribute("for", BODY_PARAM),
//                        EditorHelper.createRichTextEditor(node, new Text(FormHelper.getFieldValue(form, BODY_PARAM))).
//                                setWeight(20).addAttribute("class", "editor richtext").
//                                addAttribute("name", BODY_PARAM).addAttribute("cols", "80").addAttribute("rows", "20")
//                )
//        );
    }

}
