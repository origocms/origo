package main.origo.admin.interceptors;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminHelper;
import main.origo.core.Node;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Types;
import main.origo.core.annotations.forms.OnLoadForm;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.helpers.forms.EditorHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.UIElement;
import models.origo.admin.AdminPage;
import models.origo.core.BasicPage;
import models.origo.core.Content;
import models.origo.core.RootNode;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Admin provider for the BasicPage type. It provides a dashboard element, a method to list existing pages, a method
 * to edit a page's content and a method to store the content.
 * TODO: Needs validation
 */
@Interceptor
public class BasicPageAdminProvider {

    private static final String BASE_TYPE = "origo.admin.basicpage";
    private static final String LIST_TYPE = BASE_TYPE + ".list";
    private static final String EDIT_TYPE = BASE_TYPE + ".edit";
    private static final String DASHBOARD_TYPE = BASE_TYPE + ".dashboard";

    private static final String TITLE_PARAM = "origo-basicpageform-title";
    private static final String LEAD_PARAM = "origo-basicpageform-lead";
    private static final String BODY_PARAM = "origo-basicpageform-body";

    /**
     * Dashboard element for the admin front page.
     *
     * @return a UIElement that contains a dashboard element.
     */
    @Provides(type = Admin.DASHBOARD, with = DASHBOARD_TYPE)
    public static UIElement createDashboardItem(Provides.Context context) {

        String url = AdminHelper.getURLForAdminAction(BASE_TYPE + ".list");

        return new UIElement(Admin.DASHBOARD).addAttribute("class", "dashboard").
                addChild(new UIElement(UIElement.PANEL, 10).
                        addChild(new UIElement(UIElement.HEADING4, 10, "Basic Page").addAttribute("class", "title")).
                        addChild(new UIElement(UIElement.PARAGRAPH, 20, "Basic pages have a lead and a body").addAttribute("class", "description")).
                        addChild(new UIElement(UIElement.ANCHOR, 30, "List All").addAttribute("href", url).addAttribute("class", "link"))
                );
    }

    /**
     * Provides a type with the static name 'origo.admin.basicpage.list'.
     *
     * @param context contains a root node with an node id
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Types.NODE, with = LIST_TYPE)
    public static Node createListPage(Provides.Context context) {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("List Basic Pages");
        page.rootNode = (RootNode) context.node;
        return page;
    }

    /**
     * Adds content to the nodes with the static name 'origo.admin.basicpage.list'.
     *
     * @param context contains a node of the type 'origo.admin.basicpage.list'.
     */
    @OnLoad(type = Types.NODE, with = LIST_TYPE)
    public static void createListPage(OnLoad.Context context) {
        List<BasicPage> basicPages = BasicPage.findAllLatestVersions();

        UIElement panelElement = new UIElement(UIElement.PANEL, 10).addAttribute("class", "panel pages");
        for (BasicPage page : basicPages) {
            String editURL = AdminHelper.getURLForAdminAction(EDIT_TYPE, page.getNodeId());
            UIElement panel = new UIElement(UIElement.PANEL).
                    addChild(new UIElement(UIElement.ANCHOR, 10, page.getTitle()).addAttribute("href", editURL)).
                    addChild(new UIElement(UIElement.TEXT, 20, " (" + page.nodeId + " / " + page.getVersion() + ")"));
            panelElement.addChild(panel);
        }
        context.node.addUIElement(panelElement);
    }

    /**
     * Provides a type with the static name 'origo.admin.basicpage.edit'.
     *
     * @param context containing a root node with an node id
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Types.NODE, with = EDIT_TYPE)
    public static Node createEditPage(Provides.Context context) {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Edit Basic Page");
        if (context.node.getVersion() == null || context.node.getVersion() == 0) {
            page.rootNode = RootNode.findLatestVersionWithNodeId(context.node.getNodeId()).copy();
        } else {
            page.rootNode = (RootNode) context.node;
        }
        page.addUIElement(FormHelper.createFormElement(page, BASE_TYPE));
        return page;
    }

    /**
     * Adds content to the nodes with the static name 'origo.admin.basicpage.edit'.
     *
     * @param context       a node of the type 'origo.admin.basicpage.edit'.
     */
    @OnLoadForm(with = BASE_TYPE)
    public static void loadEditForm(OnLoadForm.Context context) {
        BasicPage basicPage = BasicPage.findLatestVersion(context.node.getNodeId());
        if (basicPage == null) {
            context.node.addUIElement(new UIElement(UIElement.PARAGRAPH, 10, "Page '" + context.node.getNodeId() + "' does not exist."));
            return;
        }

        Content leadContent = Content.findWithIdentifier(basicPage.leadReferenceId);
        Content bodyContent = Content.findWithIdentifier(basicPage.bodyReferenceId);

        context.formElement.setId("basicpageform").addAttribute("class", "origo-basicpageform, form");

        UIElement titleElement = new UIElement(UIElement.PANEL, 10).addAttribute("class", "field");
        titleElement.addChild(new UIElement(UIElement.LABEL, 10, "Title").addAttribute("for", TITLE_PARAM));
        titleElement.addChild(new UIElement(UIElement.INPUT_TEXT, 20).addAttribute("name", TITLE_PARAM).addAttribute("value", basicPage.getTitle()));
        context.formElement.addChild(titleElement);

        UIElement leadElement = new UIElement(UIElement.PANEL, 20).addAttribute("class", "field");
        leadElement.addChild(new UIElement(UIElement.LABEL, 10, "Lead").addAttribute("for", LEAD_PARAM));
        leadElement.addChild(EditorHelper.createRichTextEditor(context.node, leadContent).setWeight(20).addAttribute("class", "editor richtext").
                addAttribute("name", LEAD_PARAM).addAttribute("cols", "80").addAttribute("rows", "10"));
        context.formElement.addChild(leadElement);

        UIElement bodyElement = new UIElement(UIElement.PANEL, 30).addAttribute("class", "field");
        bodyElement.addChild(new UIElement(UIElement.LABEL, 10, "Body").addAttribute("for", BODY_PARAM));
        bodyElement.addChild(EditorHelper.createRichTextEditor(context.node, bodyContent).setWeight(20).addAttribute("class", "editor richtext").
                addAttribute("name", BODY_PARAM).addAttribute("cols", "80").addAttribute("rows", "20"));
        context.formElement.addChild(bodyElement);

        UIElement actionPanel = new UIElement(UIElement.PANEL, 40).addAttribute("class", "field");
        actionPanel.addChild(new UIElement(UIElement.INPUT_BUTTON, 10, "Save").addAttribute("type", "submit"));
        context.formElement.addChild(actionPanel);
    }

    /**
     * Hooks in to the submit process and stores a BasicPage when it is submitted.
     */
    @OnSubmit(with = BASE_TYPE)
    public static void storePage(OnSubmit.Context context) {

        DynamicForm form = DynamicForm.form().bindFromRequest();

        String nodeId = FormHelper.getNodeId(form);
        Integer version = FormHelper.getNodeVersion(form);
        RootNode oldRootNode = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);
        if (oldRootNode == null) {
            throw new RuntimeException("Root node with id=\'" + nodeId + "\' does not exist");
        }

        BasicPage latestVersion = BasicPage.findLatestVersion(nodeId);
        latestVersion.rootNode = oldRootNode;
        BasicPage newPageVersion = latestVersion.copy();

        boolean changed = false;
        Content leadContent = Content.findWithIdentifier(newPageVersion.leadReferenceId);
        if (!leadContent.value.equals(form.get(LEAD_PARAM))) {
            Content newLeadContent = new Content();
            newLeadContent.value = form.get(LEAD_PARAM);
            newPageVersion.leadReferenceId = newLeadContent.identifier;
            newLeadContent.save();
            changed = true;
        }
        Content bodyContent = Content.findWithIdentifier(newPageVersion.bodyReferenceId);
        if (!bodyContent.value.equals(form.get(BODY_PARAM))) {
            Content newBodyContent = new Content();
            newBodyContent.value = form.get(BODY_PARAM);
            newPageVersion.bodyReferenceId = newBodyContent.identifier;
            newBodyContent.save();
            changed = true;
        }

        if (!newPageVersion.title.equals(form.get(TITLE_PARAM))) {
            newPageVersion.title = form.get(TITLE_PARAM);
            changed = true;
        }

        if (changed) {
            newPageVersion.rootNode.save();
            newPageVersion.save();
        }

    }

    /**
     * Handling the routing at the end of the submit process, it redirects to listing the pages.
     */
    @SubmitState(with = BASE_TYPE)
    public static Result handleSuccess(SubmitState.Context context) {
        String endpointURL = AdminHelper.getURLForAdminAction(LIST_TYPE);
        return Controller.redirect(endpointURL);
    }
}
