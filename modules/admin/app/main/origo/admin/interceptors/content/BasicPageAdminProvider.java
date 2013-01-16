package main.origo.admin.interceptors.content;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.Node;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.*;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.forms.EditorHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.internal.CachedThemeVariant;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.BasicPage;
import models.origo.core.Content;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Admin provider for the BasicPage type. It provides a dashboard element, a method to list existing pages, a method
 * to edit a page's content and a method to store the content.
 * TODO: Needs validation
 */
@Interceptor
public class BasicPageAdminProvider {

    private static final String BASE_TYPE = Admin.With.CONTENT_PAGE + ".basicpage";
    private static final String LIST_TYPE = BASE_TYPE + ".list";
    private static final String EDIT_TYPE = BASE_TYPE + ".edit";

    private static final String TITLE_PARAM = "origo-basicpageform-title";
    private static final String PUBLISH_DATE_PARAM = "origo-basicpageform-publish-date";
    private static final String PUBLISH_TIME_PARAM = "origo-basicpageform-publish-time";
    private static final String UNPUBLISH_DATE_PARAM = "origo-basicpageform-unpublish-date";
    private static final String UNPUBLISH_TIME_PARAM = "origo-basicpageform-unpublish-time";
    private static final String THEME_VARIANT_PARAM = "origo-basicpageform-theme-variant";
    private static final String LEAD_PARAM = "origo-basicpageform-lead";
    private static final String BODY_PARAM = "origo-basicpageform-body";

    /**
     * Dashboard element for the content dashboard page.
     *
     * @return a Element that contains a dashboard element.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Admin.With.CONTENT_PAGE)
    public static Element createDashboardItem(Provides.Context context) {

        return new Admin.DashboardItem().addAttribute("class", "item").
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.Heading4().setWeight(10).setBody("Basic Page").addAttribute("class", "title")).
                        addChild(new Element.Paragraph().setWeight(20).setBody("Basic pages have a lead and a body").addAttribute("class", "description")).
                        addChild(new Element.Anchor().setWeight(30).setBody("List All").addAttribute("href", getProviderUrl()).addAttribute("class", "link"))
                );
    }

    /**
     * Provides a type with the static name 'origo.admin.basicpage.list'.
     *
     * @param context contains a root node with an node id
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Core.Type.NODE, with = LIST_TYPE)
    public static Node createListPage(Provides.Context context) {
        AdminPage page = new AdminPage((RootNode)context.node());
        page.setTitle("List Basic Pages");
        page.addElement(DashboardHelper.createBreadcrumb(BASE_TYPE), AdminTheme.topMeta());

        return page;
    }

    /**
     * Adds content to the nodes with the static name 'origo.admin.basicpage.list'.
     *
     * @param context contains a node of the type 'origo.admin.basicpage.list'.
     */
    @OnLoad(type = Core.Type.NODE, with = LIST_TYPE)
    public static void createListPage(OnLoad.Context context) {
        List<BasicPage> basicPages = BasicPage.findAllLatestVersions();

        Element panelElement = new Element.Panel().setWeight(10).addAttribute("class", "panel pages");
        for (BasicPage page : basicPages) {
            String editURL = routes.Dashboard.pageWithTypeAndIdentifier(Admin.With.CONTENT_PAGE, EDIT_TYPE, page.getNodeId()).url();
            Element panel = new Element.Panel().
                    addChild(new Element.Anchor().setWeight(10).setBody(page.getTitle()).addAttribute("href", editURL)).
                    addChild(new Element.Text().setWeight(20).setBody(" (" + page.nodeId + " / " + page.getVersion() + ")"));
            panelElement.addChild(panel);
        }
        context.node().addElement(panelElement);
    }

    @Admin.Navigation(alias="/content/pages/basic", key="breadcrumb.origo.admin.dashboard.content.basicpage")
    public static String getProviderUrl() {
        return routes.Dashboard.pageWithType(Admin.With.CONTENT_PAGE, LIST_TYPE).url();
    }

    /**
     * Provides a type with the static name 'origo.admin.basicpage.edit'.
     *
     * @param context containing a root node with an node id
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Core.Type.NODE, with = EDIT_TYPE)
    public static Node createEditPage(Provides.Context context) {
        AdminPage page;

        if (context.node().getVersion() == null || context.node().getVersion() == 0) {
            page = new AdminPage(RootNode.findLatestVersionWithNodeId(context.node().getNodeId()).copy());
        } else {
            page = new AdminPage((RootNode) context.node());
        }

        // TODO: Look up themevariant (and also meta) from DB instead of resetting here.
        page.rootNode.themeVariant = null;
        page.setTitle("Edit Basic Page");
        page.addElement(DashboardHelper.createBreadcrumb(BASE_TYPE), AdminTheme.topMeta());
        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = EDIT_TYPE)
    public static void loadEditPage(OnLoad.Context context) {
        context.node().addElement(FormHelper.createFormElement(context.node(), BASE_TYPE));
    }

    /**
     * Adds content to the nodes with the static name 'origo.admin.basicpage.edit'.
     *
     * @param context a node of the type 'origo.admin.basicpage.edit'.
     */
    @OnLoad(type = Core.Type.FORM, with = BASE_TYPE, after = true)
    public static void loadEditForm(OnLoad.Context.ElementContext context) {
        BasicPage basicPage = BasicPage.findLatestVersion(context.node().getNodeId());
        if (basicPage == null) {
            context.node().addElement(new Element.Paragraph().setWeight(10).setBody("Page '" + context.node().getNodeId() + "' does not exist."));
            return;
        }
        basicPage.rootNode = RootNode.findWithNodeIdAndSpecificVersion(context.node().getNodeId(), context.node().getVersion());

        Content leadContent = Content.findWithIdentifier(basicPage.leadReferenceId);
        Content bodyContent = Content.findWithIdentifier(basicPage.bodyReferenceId);

        context.element().setId("basicpageform").addAttribute("class", "origo-basicpageform, form");

        /**
         * Basic Options
         */

        context.element().addChild(new Element.Legend().setBody("Basic Information"));

        Element titleElement = new Element.Panel().setWeight(10).addAttribute("class", "field").
                addChild(new Element.Label().setWeight(10).setBody("Title").addAttribute("for", TITLE_PARAM)).
                addChild(new Element.InputText().setWeight(20).addAttribute("name", TITLE_PARAM).addAttribute("value", basicPage.getTitle()));
        context.element().addChild(titleElement);

        Element themeInputSelectElement = new Element.InputSelect();
        for (CachedThemeVariant themeVariant : ThemeRepository.getAvailableThemeVariants()) {
            Element optionElement = new Element.InputSelectOption().setBody(themeVariant.variantId);
            if (StringUtils.isEmpty(basicPage.rootNode.themeVariant)) {
                if (themeVariant.variantId.equals(CoreSettingsHelper.getThemeVariant())) {
                    optionElement.addAttribute("selected", "selected");
                }
            } else {
                if (themeVariant.variantId.equals(basicPage.rootNode.themeVariant)) {
                    optionElement.addAttribute("selected", "selected");
                }
            }
            themeInputSelectElement.addChild(optionElement);
        }
        Element themeVariantElement = new Element.Panel().setWeight(20).addAttribute("class", "field").
                addChild(new Element.Label().setWeight(10).setBody("Theme Variant").addAttribute("for", THEME_VARIANT_PARAM)).
                addChild(themeInputSelectElement.setWeight(25).addAttribute("class", "themeSelector").
                        addAttribute("name", THEME_VARIANT_PARAM));
        context.element().addChild(themeVariantElement);

        /**
         * Publishing options
         */
        context.element().addChild(new Element.Legend().setBody("Publish"));

        String datePattern = Messages.get("date.format");
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        Element publishElement = new Element.Panel().setWeight(15).addAttribute("class", "field").
                addChild(new Element.Panel().addAttribute("class", "panel split-left").
                        addChild(new Element.Label().setWeight(10).setBody("From Date").
                                addAttribute("for", "date-"+PUBLISH_DATE_PARAM)
                        ).
                        addChild(new Element.InputText(Date.class).setId("date-"+PUBLISH_DATE_PARAM).
                                addAttribute("name", PUBLISH_DATE_PARAM).
                                addAttribute("value", formattedIfNotNull(dateFormat, basicPage.getDatePublished())).
                                addAttribute("placeholder", datePattern.toLowerCase())
                        )
                ).
                addChild(new Element.Panel().addAttribute("class", "panel split-right").
                        addChild(new Element.Label().setWeight(10).setBody("Until Date").
                                addAttribute("for", "date-"+UNPUBLISH_DATE_PARAM)
                        ).
                        addChild(new Element.InputText(Date.class).setId("date-"+UNPUBLISH_DATE_PARAM).
                                addAttribute("name", UNPUBLISH_DATE_PARAM).
                                addAttribute("value", formattedIfNotNull(dateFormat, basicPage.getDateUnpublished())).
                                addAttribute("placeholder", datePattern.toLowerCase()))
                );
        context.element().addChild(publishElement);

        String timePattern = Messages.get("time.format");
        DateFormat timeFormat = new SimpleDateFormat(timePattern);
        Element publishTimeElement = new Element.Panel().setWeight(15).addAttribute("class", "field").
                addChild(new Element.Panel().addAttribute("class", "panel split-left").
                        addChild(new Element.Label().setWeight(10).setBody("From Time").
                                addAttribute("for", "date-"+PUBLISH_TIME_PARAM)
                        ).
                        addChild(new Element.InputText().setId("date-"+PUBLISH_TIME_PARAM).
                                addAttribute("name", PUBLISH_TIME_PARAM).
                                addAttribute("value", formattedIfNotNull(timeFormat, basicPage.getDatePublished())).
                                addAttribute("placeholder", timePattern.toLowerCase()))
                ).
                addChild(new Element.Panel().addAttribute("class", "panel split-right").
                        addChild(new Element.Label().setWeight(10).setBody("Until Time").
                                addAttribute("for", "date-"+UNPUBLISH_TIME_PARAM)
                        ).
                        addChild(new Element.InputText().setId("date-"+UNPUBLISH_TIME_PARAM).
                                addAttribute("name", UNPUBLISH_TIME_PARAM).
                                addAttribute("value", formattedIfNotNull(timeFormat, basicPage.getDateUnpublished())).
                                addAttribute("placeholder", timePattern.toLowerCase()))
                );
        context.element().addChild(publishTimeElement);

        /**
         * Content
         */
        context.element().addChild(new Element.Legend().setBody("Content"));

        Element leadElement = new Element.Panel().setWeight(20).addAttribute("class", "field").
                addChild(new Element.Label().setWeight(10).setBody("Lead").addAttribute("for", LEAD_PARAM)).
                addChild(EditorHelper.createRichTextEditor(context.node(), leadContent).setWeight(20).addAttribute("class", "editor richtext").
                        addAttribute("name", LEAD_PARAM).addAttribute("cols", "80").addAttribute("rows", "10"));
        context.element().addChild(leadElement);

        Element bodyElement = new Element.Panel().setWeight(30).addAttribute("class", "field").
                addChild(new Element.Label().setWeight(10).setBody("Body").addAttribute("for", BODY_PARAM)).
                addChild(EditorHelper.createRichTextEditor(context.node(), bodyContent).setWeight(20).addAttribute("class", "editor richtext").
                        addAttribute("name", BODY_PARAM).addAttribute("cols", "80").addAttribute("rows", "20"));
        context.element().addChild(bodyElement);

        Element actionPanel = new Element.Panel().setWeight(40).addAttribute("class", "well well-large").
                addChild(new Element.Panel().
                        addAttribute("class", "pull-left").
                        addChild(new Element.Anchor().setWeight(20).
                                addAttribute("class", "btn").
                                addAttribute("href", getProviderUrl()).
                                setBody("Cancel")
                        )
                ).
                addChild(new Element.Panel().
                        addAttribute("class", "pull-right").
                        addChild(new Element.InputSubmit().setWeight(10).addAttribute("class", "btn btn-primary").addAttribute("value", "Save")).
                        addChild(new Element.InputReset().setWeight(15).addAttribute("class", "btn").addAttribute("value", "Reset"))
                );
        context.element().addChild(actionPanel);

    }

    private static String formattedIfNotNull(DateFormat dateFormat, Date date) {
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * Hooks in to the submit process and stores a BasicPage when it is submitted.
     */
    @OnSubmit(with = BASE_TYPE)
    public static void storePage(OnSubmit.Context context) {

        Form form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();

        String nodeId = FormHelper.getNodeId(data);
        Integer version = FormHelper.getNodeVersion(data);
        RootNode oldRootNode = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);
        if (oldRootNode == null) {
            throw new RuntimeException("Root node with id='" + nodeId + "' does not exist");
        }

        BasicPage latestVersion = BasicPage.findLatestVersion(nodeId);
        latestVersion.rootNode = oldRootNode;

        // TODO: Validation

        boolean newVersion = false;

        if (!latestVersion.title.equals(data.get(TITLE_PARAM))) {
            newVersion = true;
        }

        if (latestVersion.getThemeVariant() == null || !latestVersion.getThemeVariant().equalsIgnoreCase(data.get(THEME_VARIANT_PARAM))) {
            newVersion = true;
        }

        Content leadContent = Content.findWithIdentifier(latestVersion.leadReferenceId);
        if (!leadContent.value.equals(data.get(LEAD_PARAM))) {
            newVersion = true;
        }

        Content bodyContent = Content.findWithIdentifier(latestVersion.bodyReferenceId);
        if (!bodyContent.value.equals(data.get(BODY_PARAM))) {
            newVersion = true;
        }

        if (newVersion) {

            BasicPage newPageVersion = latestVersion.copy();

            // Properties
            newPageVersion.title = data.get(TITLE_PARAM);
            newPageVersion.rootNode.themeVariant = data.get(THEME_VARIANT_PARAM);
            newPageVersion.rootNode.publish = parseDate(data.get(PUBLISH_DATE_PARAM), data.get(PUBLISH_TIME_PARAM));
            newPageVersion.rootNode.unPublish = parseDate(data.get(UNPUBLISH_DATE_PARAM), data.get(UNPUBLISH_TIME_PARAM));

            // Lead Content
            Content newLeadContent = new Content();
            newLeadContent.value = data.get(LEAD_PARAM);
            newPageVersion.leadReferenceId = newLeadContent.identifier;
            newLeadContent.save();

            // Body Content
            Content newBodyContent = new Content();
            newBodyContent.value = data.get(BODY_PARAM);
            newPageVersion.bodyReferenceId = newBodyContent.identifier;
            newBodyContent.save();

            newPageVersion.rootNode.save();
            newPageVersion.save();
        } else {

            // Properties
            latestVersion.rootNode.publish = parseDate(data.get(PUBLISH_DATE_PARAM), data.get(PUBLISH_TIME_PARAM));
            latestVersion.rootNode.unPublish = parseDate(data.get(UNPUBLISH_DATE_PARAM), data.get(UNPUBLISH_TIME_PARAM));

            latestVersion.rootNode.save();
            latestVersion.save();
        }

    }

    private static Date parseDate(String dateValue, String timeValue) {
        if (StringUtils.isNotBlank(dateValue)) {
            String datePattern = Messages.get("date.format");
            DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(datePattern);
            DateTime date = dateFormatter.parseDateTime(dateValue);

            if (StringUtils.isNotBlank(timeValue)) {
                String timePattern = Messages.get("time.format");
                DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(timePattern);
                DateTime time = timeFormatter.parseDateTime(timeValue);

                return DateTime.now().withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth()).withTime(time.getHourOfDay(), time.getMinuteOfHour(), 0, 0).toDate();
            }
            return DateTime.now().withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth()).toDate();
        }

        return null;
    }

    /**
     * Handling the routing at the end of the submit process, it redirects to listing the pages.
     */
    @SubmitState(with = BASE_TYPE)
    public static Result handleSuccess(SubmitState.Context context) {
        return Controller.redirect(routes.Dashboard.pageWithType(Admin.With.CONTENT_PAGE, LIST_TYPE));
    }

}
