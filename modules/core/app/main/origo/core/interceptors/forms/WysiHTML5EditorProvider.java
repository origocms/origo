package main.origo.core.interceptors.forms;

import controllers.routes;
import main.origo.core.Node;
import main.origo.core.annotations.*;
import main.origo.core.helpers.ProviderHelper;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;
import models.origo.core.Content;
import play.api.templates.Html;
import views.html.origo.core.decorators.forms.wysi.wysihtml5_script;
import views.html.origo.core.decorators.forms.wysi.wysihtml5_toolbar;

import java.util.Map;

@Interceptor
public class WysiHTML5EditorProvider {

    private static final String JS_LOADED = "wysihtml_js_loaded";

    public static class WysiHtml5ToolbarElement extends Element {

        public WysiHtml5ToolbarElement() {
            super("wysihtml5_editor_toolbar");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return wysihtml5_toolbar.render(this.id);
        }
    }

    public static class WysiHtml5ScriptElement extends Element {

        public WysiHtml5ScriptElement() {
            super("wysihtml5_editor_script");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return wysihtml5_script.render(this.id);
        }
    }

    @OnLoad(type = Core.Type.NODE, with = Core.With.EDITOR)
    public static void setupEditor(OnLoad.Context context) {
        if (ProviderHelper.isProvider(context.withType, WysiHTML5EditorProvider.class) && !context.attributes.containsKey(JS_LOADED)) {
            String mainScript = routes.Assets.at("javascripts/origo/wysihtml5/wysihtml5-0.4.0pre.min.js").url();
            context.node.addTailElement(new Element.Script().setWeight(9999).addAttribute("src", mainScript).addAttribute("type", "text/javascript"));
            String parserRulesScript = routes.Assets.at("javascripts/origo/wysihtml5/parser_rules/advanced.js").url();
            context.node.addTailElement(new Element.Script().setWeight(9999).addAttribute("src", parserRulesScript).addAttribute("type", "text/javascript"));

            context.attributes.put(JS_LOADED, true);
        }
    }

    @OnInsertElement(with = Element.InputTextArea.class)
    public static void insertToolbar(OnInsertElement.Context context) {
        if (context.attributes.containsKey(JS_LOADED)) {
            context.parent.addChild(new WysiHtml5ToolbarElement().setId(context.element.id));
        }
    }

    @OnInsertElement(with = Element.InputTextArea.class, after = true)
    public static void insertScript(OnInsertElement.Context context) {
        if (context.attributes.containsKey(JS_LOADED)) {
            context.node.addTailElement(new WysiHtml5ScriptElement().setId(context.element.id));
        }
    }

    @Provides(type = Core.Type.NODE, with = Core.With.EDITOR)
    public static Element createEditor(Node node, String withType, Map<String, Object> args) {
        Content content = (Content) args.get("content");
        if (content != null) {
            return new Element.InputTextArea().setId(content.identifier).setBody(content.value);
        } else {
            return new Element.InputTextArea();
        }
    }

}
