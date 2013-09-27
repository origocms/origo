package main.origo.core.interceptors.forms;

import controllers.routes;
import main.origo.core.Node;
import main.origo.core.annotations.*;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.ProviderHelper;
import main.origo.core.ui.DecorationContext;
import main.origo.core.ui.Element;
import models.origo.core.Text;
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
        public Html decorate(DecorationContext decorationContext) {
            return wysihtml5_toolbar.render(this.id);
        }
    }

    public static class WysiHtml5ScriptElement extends Element {

        public WysiHtml5ScriptElement() {
            super("wysihtml5_editor_script");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return wysihtml5_script.render(this.id);
        }
    }

    @OnLoad(type = Core.Type.NODE, with = Core.With.EDITOR)
    public static void setupEditor(Node node, String withType, Map<String, Object> args) {
        if (ProviderHelper.isProvider(withType, WysiHTML5EditorProvider.class) && !NodeContext.current().attributes.containsKey(JS_LOADED)) {
            String mainScript = routes.Assets.at("javascripts/origo/wysihtml5/wysihtml5-0.4.0pre.min.js").url();
            node.addTailElement(new Element.Script().setWeight(9999).addAttribute("src", mainScript).addAttribute("type", "text/javascript"));
            String parserRulesScript = routes.Assets.at("javascripts/origo/wysihtml5/parser_rules/advanced.js").url();
            node.addTailElement(new Element.Script().setWeight(9999).addAttribute("src", parserRulesScript).addAttribute("type", "text/javascript"));

            NodeContext.current().attributes.put(JS_LOADED, true);
        }
    }

    @OnInsertElement(with = Element.InputTextArea.class)
    public static void insertToolbar(Node node, Element parent, Element element, Map<String, Object> args) {
        if (NodeContext.current().attributes.containsKey(JS_LOADED)) {
            parent.addChild(new WysiHtml5ToolbarElement().setId(element.id));
        }
    }

    @OnInsertElement(with = Element.InputTextArea.class, after = true)
    public static void insertScript(Node node, Element parent, Element element, Map<String, Object> args) {
        if (NodeContext.current().attributes.containsKey(JS_LOADED)) {
            node.addTailElement(new WysiHtml5ScriptElement().setId(element.id));
        }
    }

    @Provides(type = Core.Type.NODE, with = Core.With.EDITOR)
    public static Element createEditor(Node node, String withType, Map<String, Object> args) {
        Text text = (Text) args.get("content");
        if (text != null) {
            return new Element.InputTextArea().setId(text.identifier).setBody(text.value);
        } else {
            return new Element.InputTextArea();
        }
    }

}
