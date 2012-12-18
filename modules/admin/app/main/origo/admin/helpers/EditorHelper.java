package main.origo.admin.helpers;

import main.origo.admin.annotations.Admin;
import main.origo.core.Node;
import main.origo.core.helpers.OnLoadHelper;
import main.origo.core.helpers.ProvidesHelper;
import main.origo.core.ui.UIElement;
import models.origo.core.Content;

import java.util.Collections;

public class EditorHelper {

    /*
    * Convenience methods for hooks with RICHTEXT_EDITOR type
    */
    public static UIElement triggerProvidesRichTextEditorListener(String withType, Node node, Content content) {
        return ProvidesHelper.triggerInterceptor(Admin.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerBeforeRichTextEditorLoaded(String withType, Node node, Content content) {
        OnLoadHelper.triggerBeforeInterceptor(Admin.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerAfterRichTextEditorLoaded(String withType, Node node, UIElement uiElement, Content content) {
        OnLoadHelper.triggerAfterInterceptor(Admin.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content), uiElement);
    }


}
