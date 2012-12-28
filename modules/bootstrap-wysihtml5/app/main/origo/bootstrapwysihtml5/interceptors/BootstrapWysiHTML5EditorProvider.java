package main.origo.bootstrapwysihtml5.interceptors;

import controllers.routes;
import main.origo.core.annotations.*;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;
import models.origo.core.Content;
import play.api.templates.Html;
import views.html.origo.bootstrapwysihtml5.decorators.forms.wysi.bootstrapwysihtml5_script;
import views.html.origo.bootstrapwysihtml5.decorators.forms.wysi.bootstrapwysihtml5_templates_script;

@Interceptor
public class BootstrapWysiHTML5EditorProvider {

    public static final String EDITOR_TYPE = "origo.bootstrapwysihtml5.editor.bootstrapwysihtml5";
    private static final String JS_LOADED = "bootstrapwysihtml_js_loaded";

    public static class BootstrapWysiHtml5CustomTemplatesElement extends Element {

        public BootstrapWysiHtml5CustomTemplatesElement() {
            super("wysihtml5_editor_templates_script");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return bootstrapwysihtml5_templates_script.render(this.id);
        }
    }

    public static class BootstrapWysiHtml5ScriptElement extends Element {

        public BootstrapWysiHtml5ScriptElement() {
            super("wysihtml5_editor_script");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return bootstrapwysihtml5_script.render(this.id);
        }
    }

    @Provides(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static Element createEditor(Provides.Context context) {
        Content content = (Content) context.args.get("content");
        return new Element.InputTextArea().setId(content.identifier);
    }

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static void setupEditor(OnLoad.Context context) {
        if (!context.attributes.containsKey(JS_LOADED)) {
            String wysiHtmlScript = routes.Assets.at("javascripts/origo/bootstrapwysihtml5/wysihtml5-0.4.0pre.min.js").url();
            context.node.addTailUIElement(new Element.Script().setWeight(9999).addAttribute("src", wysiHtmlScript));
            String parserRulesScript = routes.Assets.at("javascripts/origo/bootstrapwysihtml5/parser_rules/advanced.js").url();
            context.node.addTailUIElement(new Element.Script().setWeight(9999).addAttribute("src", parserRulesScript));

            String bootstrapWysiHtmlScript = routes.Assets.at("javascripts/origo/bootstrapwysihtml5/bootstrap-wysihtml5.min.js").url();
            context.node.addTailUIElement(new Element.Script().setWeight(9999).addAttribute("src", bootstrapWysiHtmlScript));

            String mainStyle = routes.Assets.at("stylesheets/origo/bootstrapwysihtml5/bootstrap-wysihtml5.min.css").url();
            context.node.addHeadUIElement(new Element.Link().addAttribute("href", mainStyle));

            context.attributes.put(JS_LOADED, true);
        }
        Content content = (Content) context.args.get("content");
        context.element.setId(content.identifier).setBody(content.value);
    }

    @OnInsertElement(with = Element.InputTextArea.class, after = true)
    public static void insertScript(OnInsertElement.Context context) {
        if (context.attributes.containsKey(JS_LOADED)) {
            if (!context.attributes.containsKey("templates_created")) {
                context.node.addTailUIElement(new BootstrapWysiHtml5CustomTemplatesElement());
                context.attributes.put("templates_created", true);
            }
            context.node.addTailUIElement(new BootstrapWysiHtml5ScriptElement().setId(context.element.id));
        }
    }

}
