package main.origo.admin.interceptors;

import controllers.routes;
import main.origo.core.annotations.*;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;
import models.origo.core.Content;
import play.api.templates.Html;
import views.html.origo.admin.decorators.forms.wysi.wysihtml5;

@Interceptor
public class WysiHTML5EditorProvider {

    public static final String EDITOR_TYPE = "origo.admin.editor.wysihtml5";

    public static class WysiHtml5EditorElement extends Element {

        public WysiHtml5EditorElement() {
            super("wysihtml5_editor");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return wysihtml5.render(this.id);
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
        context.parent.addChild(new WysiHtml5EditorElement());
    }

    @OnInsertElement(with = Element.InputTextArea.class, after = true)
    public static void insertScript(OnInsertElement.Context context) {
        context.parent.addChild(new Element.Script().addAttribute("type", "text/javascript").
                setBody(
                        "  new wysihtml5.Editor(\"" + context.element.id + "\", {\n" +
                                "      toolbar:        \"toolbar-" + context.element.id + "\",\n" +
                                //"      stylesheets:    \"css/stylesheet.css\",\n" +
                                "      parserRules:    wysihtml5ParserRules\n" +
                                "  });\n"
                ));
    }

    @Provides(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static Element createEditor(Provides.Context context) {
        return new Element.InputTextArea();
    }

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static Element addContent(OnLoad.Context context) {
        Content content = (Content) context.args.get("content");
        context.element.setBody(content.value);
        return null;
    }

}
