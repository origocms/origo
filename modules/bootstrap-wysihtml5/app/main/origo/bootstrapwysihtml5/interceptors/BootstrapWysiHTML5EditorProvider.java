package main.origo.bootstrapwysihtml5.interceptors;

import controllers.routes;
import main.origo.core.Node;
import main.origo.core.annotations.*;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.ProviderHelper;
import main.origo.core.ui.DecorationContext;
import main.origo.core.ui.Element;
import models.origo.core.Text;
import play.api.templates.Html;
import views.html.origo.bootstrapwysihtml5.decorators.forms.wysi.bootstrapwysihtml5_script;
import views.html.origo.bootstrapwysihtml5.decorators.forms.wysi.bootstrapwysihtml5_templates_script;

import java.util.Map;

@Interceptor
public class BootstrapWysiHTML5EditorProvider {

    private static final String JS_LOADED = "bootstrapwysihtml_js_loaded";

    public static class BootstrapWysiHtml5CustomTemplatesElement extends Element {

        public BootstrapWysiHtml5CustomTemplatesElement() {
            super("wysihtml5_editor_templates_script");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return bootstrapwysihtml5_templates_script.render(this.id);
        }
    }

    public static class BootstrapWysiHtml5ScriptElement extends Element {

        public BootstrapWysiHtml5ScriptElement() {
            super("wysihtml5_editor_script");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return bootstrapwysihtml5_script.render(this.id);
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

    @OnLoad(type = Core.Type.NODE, with = Core.With.EDITOR)
    public static void setupEditor(Node node, String withType, Map<String, Object> args) {
        if (ProviderHelper.isProvider(withType, BootstrapWysiHTML5EditorProvider.class) && !NodeContext.current().attributes.containsKey(JS_LOADED)) {
            String wysiHtmlScript = routes.Assets.at("javascripts/origo/bootstrapwysihtml5/wysihtml5-0.4.0pre.min.js").url();
            node.addTailElement(new Element.Script().setWeight(9999).addAttribute("src", wysiHtmlScript));
            String parserRulesScript = routes.Assets.at("javascripts/origo/bootstrapwysihtml5/parser_rules/advanced.js").url();
            node.addTailElement(new Element.Script().setWeight(9999).addAttribute("src", parserRulesScript));
            String customizationScript = routes.Assets.at("javascripts/origo/bootstrapwysihtml5/main.js").url();
            node.addTailElement(new Element.Script().setWeight(9999).addAttribute("src", customizationScript));

            String bootstrapWysiHtmlScript = routes.Assets.at("javascripts/origo/bootstrapwysihtml5/bootstrap-wysihtml5.js").url();
            node.addTailElement(new Element.Script().setWeight(9999).addAttribute("src", bootstrapWysiHtmlScript));

            String bootstrapWysiHtml5Stylesheet = routes.Assets.at("stylesheets/origo/bootstrapwysihtml5/bootstrap-wysihtml5.min.css").url();
            node.addHeadElement(new Element.Link().addAttribute("href", bootstrapWysiHtml5Stylesheet).addAttribute("rel", "stylesheet"));

            String customizationStylesheet = routes.Assets.at("stylesheets/origo/bootstrapwysihtml5/main.min.css").url();
            node.addHeadElement(new Element.Link().addAttribute("href", customizationStylesheet).addAttribute("rel", "stylesheet"));

            NodeContext.current().attributes.put(JS_LOADED, true);
        }
    }

    @OnInsertElement(with = Element.InputTextArea.class, after = true)
    public static void insertScript(Node node, Element parent, Element element) {
        if (NodeContext.current().attributes.containsKey(JS_LOADED)) {
            if (!NodeContext.current().attributes.containsKey("templates_created")) {
                node.addTailElement(new BootstrapWysiHtml5CustomTemplatesElement());
                NodeContext.current().attributes.put("templates_created", true);
            }
            NodeContext.current().node.addTailElement(new BootstrapWysiHtml5ScriptElement().setId(element.id));
        }
    }

}
