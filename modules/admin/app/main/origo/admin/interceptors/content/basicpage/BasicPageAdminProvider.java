package main.origo.admin.interceptors.content.basicpage;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.*;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.event.forms.OnCreateEventGenerator;
import main.origo.core.event.forms.OnUpdateEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.forms.EditorHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.internal.CachedThemeVariant;
import main.origo.core.ui.Element;
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
import play.mvc.Http;
import play.mvc.Result;
import views.html.origo.admin.dashboard_item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Admin provider for the BasicPage type. It provides a dashboard element, a method to list existing pages, a method
 * to edit a page's content and a method to store the content.
 * TODO: Needs validation
 */
@Interceptor
public class BasicPageAdminProvider {

    public static final String BASE_TYPE = Core.With.CONTENT_PAGE + ".basicpage";
    public static final String LIST_TYPE = BASE_TYPE + ".list";
    public static final String EDIT_TYPE = BASE_TYPE + ".edit";
    public static final String NEW_TYPE = BASE_TYPE + ".new";

    private static final String TITLE_PARAM = "origo-basicpageform-title";
    private static final String PUBLISH_DATE_PARAM = "origo-basicpageform-publish-date";
    private static final String PUBLISH_TIME_PARAM = "origo-basicpageform-publish-time";
    private static final String UNPUBLISH_DATE_PARAM = "origo-basicpageform-unpublish-date";
    private static final String UNPUBLISH_TIME_PARAM = "origo-basicpageform-unpublish-time";
    private static final String THEME_VARIANT_PARAM = "origo-basicpageform-theme-variant";
    private static final String LEAD_PARAM = "origo-basicpageform-lead";
    private static final String BODY_PARAM = "origo-basicpageform-body";
    private static final String TYPE = "models.origo.core.BasicPage";

    /**
     * Dashboard element for the content dashboard page.
     *
     * @return a Element that contains a dashboard element.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Core.With.CONTENT_PAGE)
    public static Element createDashboardItem(Provides.Context context) {
        return new Admin.DashboardItem().
                addChild(new Element.Raw().setBody(dashboard_item.render("Basic Page", "Basic pages have a lead and a body", getProviderUrl(), "List All")));
    }

    @Admin.Navigation(alias = "/content/pages/basic", key = "breadcrumb.origo.admin.dashboard.content.basicpage")
    public static String getProviderUrl() {
        return routes.Dashboard.pageWithType(Core.With.CONTENT_PAGE, LIST_TYPE).absoluteURL(Http.Context.current().request());
    }

    /**
     * Adds content to the nodes with the static name 'origo.admin.basicpage.edit'.
     *
     * @param context a node of the type 'origo.admin.basicpage.edit'.
     */
    @OnLoad(type = Core.Type.FORM, with = BASE_TYPE, after = true)
    public static void loadEditForm(OnLoad.Context context) {

        BasicPage basicPage = (BasicPage) context.attributes.get("page");
        Content leadContent = (Content) context.attributes.get("lead");
        Content bodyContent = (Content) context.attributes.get("body");

        Element element = (Element) context.args.get("element");
        element.setId("basicpageform").addAttribute("class", "origo-basicpageform, form");

        /**
         * Basic Options
         */

        Element basicFieldSet = new Element.FieldSet().setId("basic");
        element.addChild(basicFieldSet);

        basicFieldSet.addChild(new Element.Legend().setBody("Basic Information"));

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

        basicFieldSet.addChild(new Element.Panel().addAttribute("class", "row-fluid").
                addChild(new Element.Panel().setWeight(10).addAttribute("class", "field span3").
                        addChild(new Element.Label().setWeight(10).setBody("Title").addAttribute("for", TITLE_PARAM)).
                        addChild(new Element.InputText().setWeight(20).addAttribute("name", TITLE_PARAM).addAttribute("value", basicPage.getTitle()))).
                addChild(new Element.Panel().setWeight(20).addAttribute("class", "field").
                        addChild(new Element.Label().setWeight(10).setBody("Theme Variant").addAttribute("for", THEME_VARIANT_PARAM)).
                        addChild(themeInputSelectElement.setWeight(25).addAttribute("class", "themeSelector").
                                addAttribute("name", THEME_VARIANT_PARAM))));

        /**
         * Content
         */

        Element contentFieldSet = new Element.FieldSet().setId("content");
        element.addChild(contentFieldSet);

        contentFieldSet.addChild(new Element.Legend().setBody("Content"));

        Element leadElement = new Element.Panel().setWeight(20).addAttribute("class", "field").
                addChild(new Element.Label().setWeight(10).setBody("Lead").addAttribute("for", LEAD_PARAM)).
                addChild(EditorHelper.createRichTextEditor(context.node, leadContent).setWeight(20).addAttribute("class", "editor richtext").
                        addAttribute("name", LEAD_PARAM).addAttribute("cols", "80").addAttribute("rows", "10"));
        contentFieldSet.addChild(leadElement);

        Element bodyElement = new Element.Panel().setWeight(30).addAttribute("class", "field").
                addChild(new Element.Label().setWeight(10).setBody("Body").addAttribute("for", BODY_PARAM)).
                addChild(EditorHelper.createRichTextEditor(context.node, bodyContent).setWeight(20).addAttribute("class", "editor richtext").
                        addAttribute("name", BODY_PARAM).addAttribute("cols", "80").addAttribute("rows", "20"));
        contentFieldSet.addChild(bodyElement);

        /**
         * Publishing options
         */

        Element publishingFieldSet = new Element.FieldSet().setId("publishing").setWeight(50);
        element.addChild(publishingFieldSet);

        publishingFieldSet.addChild(new Element.Legend().setBody("Publish"));

        String datePattern = Messages.get("date.format");
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        Element publishElement = new Element.Panel().setWeight(15).addAttribute("class", "field").
                addChild(new Element.Panel().addAttribute("class", "panel split-left").
                        addChild(new Element.Label().setWeight(10).setBody("From Date").
                                addAttribute("for", "date-" + PUBLISH_DATE_PARAM)
                        ).
                        addChild(new Element.InputText(Date.class).setId("date-" + PUBLISH_DATE_PARAM).
                                addAttribute("name", PUBLISH_DATE_PARAM).
                                addAttribute("value", formattedIfNotNull(dateFormat, basicPage.getDatePublished())).
                                addAttribute("placeholder", datePattern.toLowerCase())
                        )
                ).
                addChild(new Element.Panel().addAttribute("class", "panel split-right").
                        addChild(new Element.Label().setWeight(10).setBody("Until Date").
                                addAttribute("for", "date-" + UNPUBLISH_DATE_PARAM)
                        ).
                        addChild(new Element.InputText(Date.class).setId("date-" + UNPUBLISH_DATE_PARAM).
                                addAttribute("name", UNPUBLISH_DATE_PARAM).
                                addAttribute("value", formattedIfNotNull(dateFormat, basicPage.getDateUnpublished())).
                                addAttribute("placeholder", datePattern.toLowerCase()))
                );
        publishingFieldSet.addChild(publishElement);

        String timePattern = Messages.get("time.format");
        DateFormat timeFormat = new SimpleDateFormat(timePattern);
        Element publishTimeElement = new Element.Panel().setWeight(15).addAttribute("class", "field").
                addChild(new Element.Panel().addAttribute("class", "panel split-left").
                        addChild(new Element.Label().setWeight(10).setBody("From Time").
                                addAttribute("for", "date-" + PUBLISH_TIME_PARAM)
                        ).
                        addChild(new Element.InputText().setId("date-" + PUBLISH_TIME_PARAM).
                                addAttribute("name", PUBLISH_TIME_PARAM).
                                addAttribute("value", formattedIfNotNull(timeFormat, basicPage.getDatePublished())).
                                addAttribute("placeholder", timePattern.toLowerCase()))
                ).
                addChild(new Element.Panel().addAttribute("class", "panel split-right").
                        addChild(new Element.Label().setWeight(10).setBody("Until Time").
                                addAttribute("for", "date-" + UNPUBLISH_TIME_PARAM)
                        ).
                        addChild(new Element.InputText().setId("date-" + UNPUBLISH_TIME_PARAM).
                                addAttribute("name", UNPUBLISH_TIME_PARAM).
                                addAttribute("value", formattedIfNotNull(timeFormat, basicPage.getDateUnpublished())).
                                addAttribute("placeholder", timePattern.toLowerCase()))
                );
        publishingFieldSet.addChild(publishTimeElement);

        element.addChild(new Element.Panel().setId("actions").setWeight(1000).addAttribute("class", "well well-large").
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
                ));

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
            oldRootNode = new RootNode(nodeId, 0);
        }

        BasicPage latestVersion = BasicPage.findLatestVersion(nodeId);
        if (latestVersion == null) {
            latestVersion = new BasicPage();
            latestVersion.nodeId = oldRootNode.getNodeId();
        }
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
        if (leadContent == null || !leadContent.value.equals(data.get(LEAD_PARAM).trim())) {
            newVersion = true;
        }

        Content bodyContent = Content.findWithIdentifier(latestVersion.bodyReferenceId);
        if (bodyContent == null || !bodyContent.value.equals(data.get(BODY_PARAM).trim())) {
            newVersion = true;
        }

        if (newVersion) {

            BasicPage newPageVersion = latestVersion.copy();

            // Properties
            newPageVersion.title = data.get(TITLE_PARAM);
            newPageVersion.rootNode.themeVariant = data.get(THEME_VARIANT_PARAM);
            newPageVersion.rootNode.publish = parseDate(data.get(PUBLISH_DATE_PARAM), data.get(PUBLISH_TIME_PARAM));
            newPageVersion.rootNode.unPublish = parseDate(data.get(UNPUBLISH_DATE_PARAM), data.get(UNPUBLISH_TIME_PARAM));
            newPageVersion.rootNode.nodeType = BasicPage.class.getName();

            // Lead Content
            Content newLeadContent = new Content();
            newLeadContent.value = data.get(LEAD_PARAM);
            newPageVersion.leadReferenceId = newLeadContent.identifier;
            newLeadContent.create();

            // Body Content
            Content newBodyContent = new Content();
            newBodyContent.value = data.get(BODY_PARAM);
            newPageVersion.bodyReferenceId = newBodyContent.identifier;
            newBodyContent.create();

            if (oldRootNode.version == 0) {
                newPageVersion.rootNode.create();
                newPageVersion.create();
            } else {
                newPageVersion.rootNode.update();
                newPageVersion.update();
            }

            if (oldRootNode.version == 0) {
                OnCreateEventGenerator.triggerAfterInterceptors(TYPE, newPageVersion);
            } else {
                OnUpdateEventGenerator.triggerAfterInterceptors(TYPE, newPageVersion);
            }

        } else {

            OnUpdateEventGenerator.triggerBeforeInterceptors(TYPE, latestVersion);

            // Properties
            latestVersion.rootNode.publish = parseDate(data.get(PUBLISH_DATE_PARAM), data.get(PUBLISH_TIME_PARAM));
            latestVersion.rootNode.unPublish = parseDate(data.get(UNPUBLISH_DATE_PARAM), data.get(UNPUBLISH_TIME_PARAM));

            latestVersion.rootNode.update();
            latestVersion.update();

            OnUpdateEventGenerator.triggerAfterInterceptors(TYPE, latestVersion);
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
        return Controller.redirect(routes.Dashboard.pageWithType(Core.With.CONTENT_PAGE, LIST_TYPE));
    }

}
