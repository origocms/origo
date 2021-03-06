package main.origo.admin.themes;

import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderedNode;
import main.origo.core.ui.RenderingContext;
import models.origo.core.Meta;
import play.api.templates.Html;
import play.mvc.Content;
import views.html.origo.admin.themes.AdminTheme.variant_main_and_left_columns;
import views.html.origo.admin.themes.AdminTheme.variant_main_only;
import views.html.origo.admin.themes.AdminTheme.variant_three_columns;
import views.html.origo.core.decorators.base;

import java.util.Collections;

@Theme(id = "admin")
public class AdminTheme {

    public static final String DEFAULT_VARIANT_NAME = "admin-main_only";
    public static final String LEFT_AND_MAIN_COLUMNS_VARIANT_NAME = "admin-main_and_left_columns";
    public static final String THREE_COLUMNS_VARIANT_NAME = "admin-three_columns";

    @ThemeVariant(id = DEFAULT_VARIANT_NAME, regions = {"top", "main"})
    public static Content renderDefaultTemplate(RenderedNode renderedNode) {
        return variant_main_only.render(renderedNode);
    }

    @ThemeVariant(id = LEFT_AND_MAIN_COLUMNS_VARIANT_NAME, regions = {"top", "main", "left"})
    public static Content renderDefaultMainAndLeftColumnTemplate(RenderedNode renderedNode) {
        return variant_main_and_left_columns.render(renderedNode);
    }

    @ThemeVariant(id = THREE_COLUMNS_VARIANT_NAME, regions = {"top", "main", "left", "right"})
    public static Content renderDefaultThreeColumnTemplate(RenderedNode renderedNode) {
        return variant_three_columns.render(renderedNode);
    }

    public static String defaultDashboardClasses() {
        return "dashboard";
    }

    @Decorates(types = Admin.Dashboard.class)
    public static Html decorateDashboard(Element element, RenderingContext renderingContext) {
        Html body = ThemeHelper.decorateChildren(element, renderingContext);
        return base.render("div", element, body,
                ElementHelper.combineAttributes(element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardClasses() + " row-fluid")));
    }

    public static String defaultDashboardItemClasses() {
        return "dashboard-item item";
    }

    @Decorates(types = Admin.DashboardItem.class)
    public static Html decorateDashboardItem(Element element, RenderingContext renderingContext) {
        Html body = ThemeHelper.decorateChildren(element, renderingContext);
        return base.render("div", element, body,
                ElementHelper.combineAttributes(element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardItemClasses() + " span3")));
    }

    @Decorates(types = {Element.InputSubmit.class, Element.InputButton.class, Element.InputReset.class})
    public static Html decorateButton(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "btn");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Error.class})
    public static Html decorateEmphasisError(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "text-error");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Warning.class})
    public static Html decorateEmphasisWarning(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "text-warning");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Info.class})
    public static Html decorateEmphasisInfo(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "text-info");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Success.class})
    public static Html decorateEmphasisSuccess(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "text-success");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Field.class})
    public static Html decorateField(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "control-group");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Help.class})
    public static Html decorateHelp(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "help-inline");
        return element.decorate(renderingContext);
    }

    // TODO: Load this from database instead
    public static Meta centerColumnMeta() {
        Meta meta = new Meta();
        meta.region = "center";
        meta.weight = 100;
        return meta;
    }

    // TODO: Load this from database instead
    public static Meta leftColumnMeta() {
        Meta meta = new Meta();
        meta.region = "left";
        meta.weight = 100;
        return meta;
    }

    // TODO: Load this from database instead
    public static Meta getRightColumnMeta() {
        Meta meta = new Meta();
        meta.region = "right";
        meta.weight = 100;
        return meta;
    }

    // TODO: Load this from database instead
    public static Meta topMeta() {
        Meta meta = new Meta();
        meta.region = "top";
        meta.weight = 100;
        return meta;
    }
}
