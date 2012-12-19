package main.origo.admin.interceptors;

import controllers.routes;
import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.UIElement;
import models.origo.core.Content;

/**
 * This is the default editor in the system. A module can provide a different editor provider by changing it in the settings.
 */
@Interceptor
public class TinyMCEEditorProvider {

    public static final String EDITOR_TYPE = "origo.admin.editor.tinymce";

    @Provides(type = Admin.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static UIElement createDashboardItem(Provides.Context context) {


        String script = routes.Assets.at("javascripts/tiny_mce/tiny_mce.js").url();
        if (script != null) {
            context.node.addHeadUIElement(new UIElement(UIElement.SCRIPT, 10).addAttribute("type", "text/javascript").addAttribute("src", script));
            context.node.addHeadUIElement(new UIElement(UIElement.SCRIPT, 20, "tinyMCE.init({ mode:\"textareas\" });</script>").addAttribute("type", "text/javascript"));
        }

        return new UIElement(UIElement.INPUT_TEXTAREA);
    }

    @OnLoad(type = Admin.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static void addContent(OnLoad.Context context) {
        Content content = (Content) context.args.get("content");
        context.uiElement.setBody(content.value);
    }

/*
    public static class StaticRouter extends Router {
        public static String getStaticURL(String file) {
            return Router.getBaseUrl() + file;
        }
    }
*/

}
