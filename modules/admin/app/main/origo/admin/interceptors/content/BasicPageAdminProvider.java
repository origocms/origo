package main.origo.admin.interceptors.content;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.forms.BasicPageForm;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.helpers.forms.AdminFormHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.admin.utils.DateUtil;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.event.forms.OnCreateEventGenerator;
import main.origo.core.event.forms.OnUpdateEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.forms.EditorHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.internal.CachedThemeVariant;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.BasicPage;
import models.origo.core.RootNode;
import models.origo.core.Text;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Date;
import java.util.Map;

/**
 * Admin provider for the BasicPage type. It provides a dashboard element, a method to list existing pages, a method
 * to edit a page's content and a method to store the content.
 * TODO: Needs validation
 */
@Interceptor
public class BasicPageAdminProvider {

    private static final String TITLE_PARAM = "title";
    private static final String PUBLISH_DATE_PARAM = "publishDate";
    private static final String PUBLISH_TIME_PARAM = "publishTime";
    private static final String UNPUBLISH_DATE_PARAM = "unpublishDate";
    private static final String UNPUBLISH_TIME_PARAM = "unpublishTime";
    private static final String THEME_VARIANT_PARAM = "themeVariant";
    private static final String LEAD_PARAM = "leadText";
    private static final String BODY_PARAM = "bodyText";

    /**
     * Provides a type with the static name 'content.basicpage'.
     * This will create the AdminPage for the UI to render.
     *
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Admin.Type.ADMIN_NODE, with = BasicPage.TYPE)
    public static Node createPage(RootNode node, String withType, Map<String, Object> args) {
        AdminPage page = AdminPage.create(node, BasicPage.TYPE);

        // TODO: Look up themevariant (and also meta) from DB instead of resetting here.
        page.themeVariant = null;
        page.setTitle("Basic Page");
        return page;
    }

    /**
     * This will create a new or load a BasicPage for editing
     */
    @OnLoad(type = Admin.Type.ADMIN_NODE, with = BasicPage.TYPE)
    public static void loadNewPage(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        node.addElement(DashboardHelper.createBreadcrumb(Admin.With.CONTENT_PAGE, BasicPage.TYPE), AdminTheme.topMeta());

        Form<BasicPageForm> form = FormHelper.getValidationResult(BasicPageForm.class);

        if (form.hasErrors()) {

            node.addElement(AdminFormHelper.createFormElement(node, BasicPage.TYPE));
            return;

        }

        BasicPageForm page = new BasicPageForm();
        if (StringUtils.isNotEmpty(node.nodeId())) {
            BasicPage basicPage = BasicPage.findLatestVersion(node.nodeId());
            if (basicPage == null) {
                node.addElement(new Element.Paragraph().setWeight(10).setBody("Page '" + node.nodeId() + "' does not exist."));
                return;
            }
            RootNode rootNode = RootNode.findWithNodeIdAndSpecificVersion(node.nodeId(), node.version());

            Text leadText = Text.findWithIdentifier(basicPage.leadReferenceId);
            Text bodyText = Text.findWithIdentifier(basicPage.bodyReferenceId);

            if (rootNode.published() != null) {
                page.publishDate = LocalDate.fromDateFields(rootNode.published());
                page.publishTime = LocalTime.fromDateFields(rootNode.published());
            }
            if (rootNode.unpublished() != null) {
                page.unpublishDate = LocalDate.fromDateFields(rootNode.unpublished());
                page.unpublishTime = LocalTime.fromDateFields(rootNode.unpublished());
            }
            page.title = basicPage.title();
            page.themeVariant = basicPage.themeVariant();
            page.leadText = leadText.value;
            page.bodyText = bodyText.value;
        }

        form = form.fill(page);
        node.addElement(AdminFormHelper.createFormElement(node, BasicPage.TYPE, form));

    }


    /**
     * Adds content to the nodes with the static name 'origo.admin.basicpage.edit'.
     */
    @OnLoad(type = Core.Type.FORM, with = BasicPage.TYPE, after = true)
    public static void loadEditForm(Node node, String withType, Form<BasicPageForm> form, Element element, Map<String, Object> args) {

        element.setId("basicpageform").addAttribute("class", "origo-basicpageform, form");

        Element globalErrors = FormHelper.createGlobalErrorElement();
        if (globalErrors != null) {
            element.addChild(globalErrors);
        }

        /**
         * Basic Options
         */

        Element basicFieldSet = new Element.FieldSet().setId("basic");
        element.addChild(basicFieldSet);

        basicFieldSet.addChild(new Element.Legend().setBody("Basic Information"));

        Element themeInputSelectElement = new Element.InputSelect();
        String themeVariantFormValue = FormHelper.getFieldValue(form, THEME_VARIANT_PARAM);
        for (CachedThemeVariant themeVariant : ThemeRepository.getAvailableThemeVariants()) {
            Element optionElement = new Element.InputSelectOption().setBody(themeVariant.variantId);
            if (StringUtils.isEmpty(themeVariantFormValue)) {
                if (themeVariant.variantId.equals(CoreSettingsHelper.getThemeVariant())) {
                    optionElement.addAttribute("selected", "selected");
                }
            } else {
                if (themeVariant.variantId.equals(themeVariantFormValue)) {
                    optionElement.addAttribute("selected", "selected");
                }
            }
            themeInputSelectElement.addChild(optionElement);
        }

        basicFieldSet.addChild(new Element.Panel().addAttribute("class", "row-fluid").
                addChild(
                        FormHelper.createField(form,
                                new Element.Label().setWeight(10).setBody("Title").addAttribute("for", TITLE_PARAM),
                                new Element.InputText().setWeight(20).addAttribute("name", TITLE_PARAM)
                        ).addAttribute("class", "span6")
                ).
                addChild(
                        FormHelper.createField(form,
                                new Element.Label().setWeight(10).setBody("Theme Variant").addAttribute("for", THEME_VARIANT_PARAM),
                                themeInputSelectElement.setWeight(25).addAttribute("class", "themeSelector").
                                        addAttribute("name", THEME_VARIANT_PARAM)
                        ).addAttribute("class", "span6")
                )
        );

        /**
         * Content
         */


        Element contentFieldSet = new Element.FieldSet().setId("content");
        element.addChild(contentFieldSet);

        contentFieldSet.addChild(new Element.Legend().setBody("Content"));

        try {
            contentFieldSet.addChild(
                    FormHelper.createField(form,
                            new Element.Label().setWeight(10).setBody("Lead").addAttribute("for", LEAD_PARAM),
                            EditorHelper.createRichTextEditor(node, new Text(FormHelper.getFieldValue(form, LEAD_PARAM))).
                                    setWeight(20).addAttribute("class", "editor richtext").
                                    addAttribute("name", LEAD_PARAM).addAttribute("cols", "80").addAttribute("rows", "10")
                    )
            );

            contentFieldSet.addChild(
                    FormHelper.createField(form,
                            new Element.Label().setWeight(10).setBody("Body").addAttribute("for", BODY_PARAM),
                            EditorHelper.createRichTextEditor(node, new Text(FormHelper.getFieldValue(form, BODY_PARAM))).
                                    setWeight(20).addAttribute("class", "editor richtext").
                                    addAttribute("name", BODY_PARAM).addAttribute("cols", "80").addAttribute("rows", "20")
                    )
            );
        } catch (ModuleException e) {
            // TODO: recover somehow?
            Logger.error("Unable to load node", e);
        } catch (NodeLoadException  e) {
            // TODO: recover somehow?
            Logger.error("Unable to load node", e);
        }

        /**
         * Publishing options
         */


        Element publishingFieldSet = new Element.FieldSet().setId("publishing").setWeight(50);
        element.addChild(publishingFieldSet);

        publishingFieldSet.addChild(new Element.Legend().setBody("Publish"));

        String datePattern = Messages.get("date.format");
        Element publishElement = new Element.Panel().setWeight(15).addAttribute("class", "field").
                addChild(new Element.Panel().addAttribute("class", "panel split-left").
                        addChild(new Element.Label().setWeight(10).setBody("From Date").
                                addAttribute("for", "date-" + PUBLISH_DATE_PARAM)
                        ).
                        addChild(new Element.InputText(Date.class).setId("date-" + PUBLISH_DATE_PARAM).
                                addAttribute("name", PUBLISH_DATE_PARAM).
                                addAttribute("value", FormHelper.getFieldValue(form, PUBLISH_DATE_PARAM)).
                                addAttribute("placeholder", datePattern.toLowerCase())
                        )
                ).
                addChild(new Element.Panel().addAttribute("class", "panel split-right").
                        addChild(new Element.Label().setWeight(10).setBody("Until Date").
                                addAttribute("for", "date-" + UNPUBLISH_DATE_PARAM)
                        ).
                        addChild(new Element.InputText(Date.class).setId("date-" + UNPUBLISH_DATE_PARAM).
                                addAttribute("name", UNPUBLISH_DATE_PARAM).
                                addAttribute("value", FormHelper.getFieldValue(form, UNPUBLISH_DATE_PARAM)).
                                addAttribute("placeholder", datePattern.toLowerCase()))
                );
        publishingFieldSet.addChild(publishElement);

        String timePattern = Messages.get("time.format");
        Element publishTimeElement = new Element.Panel().setWeight(15).addAttribute("class", "field").
                addChild(new Element.Panel().addAttribute("class", "panel split-left").
                        addChild(new Element.Label().setWeight(10).setBody("From Time").
                                addAttribute("for", "date-" + PUBLISH_TIME_PARAM)
                        ).
                        addChild(new Element.InputText().setId("date-" + PUBLISH_TIME_PARAM).
                                addAttribute("name", PUBLISH_TIME_PARAM).
                                addAttribute("value", FormHelper.getFieldValue(form, PUBLISH_TIME_PARAM)).
                                addAttribute("placeholder", timePattern.toLowerCase()))
                ).
                addChild(new Element.Panel().addAttribute("class", "panel split-right").
                        addChild(new Element.Label().setWeight(10).setBody("Until Time").
                                addAttribute("for", "date-" + UNPUBLISH_TIME_PARAM)
                        ).
                        addChild(new Element.InputText().setId("date-" + UNPUBLISH_TIME_PARAM).
                                addAttribute("name", UNPUBLISH_TIME_PARAM).
                                addAttribute("value", FormHelper.getFieldValue(form, UNPUBLISH_TIME_PARAM)).
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

    /**
     * Hooks in to the submit process and stores a BasicPage when it is submitted.
     */
    @OnSubmit(with = BasicPage.TYPE, validate = BasicPageForm.class)
    public static Boolean storePage(Form<BasicPageForm> form) {

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
            latestVersion.nodeId = oldRootNode.nodeId();
        }
        latestVersion.rootNode = oldRootNode;

        // TODO: Validation

        boolean newVersion = false;

        if (!latestVersion.title.equals(data.get(TITLE_PARAM))) {
            newVersion = true;
        }

        if (latestVersion.themeVariant == null || !latestVersion.themeVariant.equalsIgnoreCase(data.get(THEME_VARIANT_PARAM))) {
            newVersion = true;
        }

        Text leadText = Text.findWithIdentifier(latestVersion.leadReferenceId);
        if (leadText == null || !leadText.value.equals(data.get(LEAD_PARAM).trim())) {
            newVersion = true;
        }

        Text bodyText = Text.findWithIdentifier(latestVersion.bodyReferenceId);
        if (bodyText == null || !bodyText.value.equals(data.get(BODY_PARAM).trim())) {
            newVersion = true;
        }

        if (newVersion) {

            BasicPage newPageVersion = latestVersion.copy();

            // Properties
            newPageVersion.title = data.get(TITLE_PARAM);
            newPageVersion.themeVariant = data.get(THEME_VARIANT_PARAM);
            newPageVersion.rootNode.published(DateUtil.parseDate(data.get(PUBLISH_DATE_PARAM), data.get(PUBLISH_TIME_PARAM)));
            newPageVersion.rootNode.unpublished(DateUtil.parseDate(data.get(UNPUBLISH_DATE_PARAM), data.get(UNPUBLISH_TIME_PARAM)));
            newPageVersion.rootNode.nodeType(BasicPage.TYPE);

            // Lead Content
            Text newLeadText = new Text();
            newLeadText.value = data.get(LEAD_PARAM);
            newPageVersion.leadReferenceId = newLeadText.identifier;
            newLeadText.create();

            // Body Content
            Text newBodyText = new Text();
            newBodyText.value = data.get(BODY_PARAM);
            newPageVersion.bodyReferenceId = newBodyText.identifier;
            newBodyText.create();

            if (oldRootNode.version() == 0) {
                newPageVersion.rootNode.create();
                newPageVersion.create();
            } else {
                newPageVersion.rootNode.update();
                newPageVersion.update();
            }

            if (oldRootNode.version() == 0) {
                OnCreateEventGenerator.triggerAfterInterceptors(BasicPage.TYPE, newPageVersion);
            } else {
                OnUpdateEventGenerator.triggerAfterInterceptors(BasicPage.TYPE, newPageVersion);
            }

        } else {

            OnUpdateEventGenerator.triggerBeforeInterceptors(BasicPage.TYPE, latestVersion);

            // Properties
            latestVersion.rootNode.published(DateUtil.parseDate(data.get(PUBLISH_DATE_PARAM), data.get(PUBLISH_TIME_PARAM)));
            latestVersion.rootNode.unpublished(DateUtil.parseDate(data.get(UNPUBLISH_DATE_PARAM), data.get(UNPUBLISH_TIME_PARAM)));

            latestVersion.rootNode.update();
            latestVersion.update();

            OnUpdateEventGenerator.triggerAfterInterceptors(BasicPage.TYPE, latestVersion);
        }

        return true;
    }

    public static String getProviderUrl() {
        return routes.Dashboard.dashboard(Admin.With.CONTENT_PAGE).absoluteURL(Http.Context.current().request());
    }

    @Validation.Failure(with = BasicPage.TYPE)
    public static Node validationFailure(RootNode node, Map<String, Object> args) {
        return createPage(node, BasicPage.TYPE, args);
    }

    /**
     * Handling the routing at the end of the submit process, it redirects to listing the pages.
     */
    @SubmitState(with = BasicPage.TYPE)
    public static Result handleSuccess() {
        return Controller.redirect(routes.Dashboard.dashboard(Admin.With.CONTENT_PAGE));
    }


}
