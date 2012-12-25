package main.origo.core.interceptors.forms;

import controllers.routes;
import main.origo.core.annotations.*;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;
import models.origo.core.Content;
import play.api.templates.Html;
import views.html.origo.core.decorators.forms.wysi.wysihtml5_script;
import views.html.origo.core.decorators.forms.wysi.wysihtml5_toolbar;

@Interceptor
public class WysiHTML5EditorProvider {

    public static final String EDITOR_TYPE = "origo.admin.editor.wysihtml5";

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

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static void setupEditor(OnLoad.Context context) {
        String mainScript = routes.Assets.at("javascripts/wysihtml5/wysihtml5-0.4.0pre.min.js").url();
        context.node.addHeadUIElement(new Element.Script().setWeight(9999).addAttribute("type", "text/javascript").addAttribute("src", mainScript));
        String parserRulesScript = routes.Assets.at("javascripts/wysihtml5/parser_rules/advanced.js").url();
        context.node.addHeadUIElement(new Element.Script().setWeight(9999).addAttribute("type", "text/javascript").addAttribute("src", parserRulesScript));
    }

    @OnInsertElement(with = Element.InputTextArea.class)
    public static void insertToolbar(OnInsertElement.Context context) {
        context.parent.addChild(new WysiHtml5ToolbarElement().setId(context.element.id));
    }

    @OnInsertElement(with = Element.InputTextArea.class, after = true)
    public static void insertScript(OnInsertElement.Context context) {
        context.parent.addChild(new WysiHtml5ScriptElement().setId(context.element.id));
    }

    @Provides(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static Element createEditor(Provides.Context context) {
        Content content = (Content) context.args.get("content");
        return new Element.InputTextArea().setId(content.identifier);
    }

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static Element addContent(OnLoad.Context context) {
        Content content = (Content) context.args.get("content");
        context.element.setId(content.identifier).setBody(content.value);
        return null;
    }

}
