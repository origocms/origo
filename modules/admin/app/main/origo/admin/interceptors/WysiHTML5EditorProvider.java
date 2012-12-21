package main.origo.admin.interceptors;

import controllers.routes;
import main.origo.core.annotations.*;
import main.origo.core.ui.UIElement;
import models.origo.core.Content;
import views.html.origo.admin.decorators.forms.wysi.wysihtml5;

@Interceptor
public class WysiHTML5EditorProvider {

    public static final String EDITOR_TYPE = "origo.admin.editor.wysihtml5";

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static void setupEditor(OnLoad.Context context) {
        String mainScript = routes.Assets.at("javascripts/wysihtml5/wysihtml5-0.4.0pre.min.js").url();
        context.node.addHeadUIElement(new UIElement(UIElement.SCRIPT, 9999).addAttribute("type", "text/javascript").addAttribute("src", mainScript));
        String parserRulesScript = routes.Assets.at("javascripts/wysihtml5/parser_rules/advanced.js").url();
        context.node.addHeadUIElement(new UIElement(UIElement.SCRIPT, 9999).addAttribute("type", "text/javascript").addAttribute("src", parserRulesScript));
    }

    @OnInsertElement(with = UIElement.INPUT_TEXTAREA)
    public static void insertToolbar(OnInsertElement.Context context) {
        context.parent.addChild(new UIElement(wysihtml5.render(context.element.id)));
    }

    @OnInsertElement(with = UIElement.INPUT_TEXTAREA, after = true)
    public static void insertScript(OnInsertElement.Context context) {
        context.parent.addChild(new UIElement(UIElement.SCRIPT).addAttribute("type", "text/javascript").
                setBody(
                        "  new wysihtml5.Editor(\"" + context.element.id + "\", {\n" +
                                "      toolbar:        \"toolbar-" + context.element.id + "\",\n" +
                                //"      stylesheets:    \"css/stylesheet.css\",\n" +
                                "      parserRules:    wysihtml5ParserRules\n" +
                                "  });\n"
                ));
    }

    @Provides(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static UIElement createEditor(Provides.Context context) {

        return new UIElement(UIElement.INPUT_TEXTAREA);

    }

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static UIElement addContent(OnLoad.Context context) {
        Content content = (Content) context.args.get("content");
        context.uiElement.setBody(content.value);
        return null;
    }

}
