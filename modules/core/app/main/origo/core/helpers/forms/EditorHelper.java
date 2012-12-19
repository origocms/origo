package main.origo.core.helpers.forms;

import main.origo.core.Node;
import main.origo.core.annotations.Types;
import main.origo.core.helpers.OnLoadHelper;
import main.origo.core.helpers.ProvidesHelper;
import main.origo.core.helpers.SettingsHelper;
import main.origo.core.ui.UIElement;
import models.origo.core.Content;

import java.util.Collections;

public class EditorHelper {

    public static UIElement createRichTextEditor(Node node, Content content) {
        String editorType = SettingsHelper.Admin.getEditorType();
        EditorHelper.triggerBeforeRichTextEditorLoaded(editorType, node, content);
        UIElement richTextEditor = EditorHelper.triggerProvidesRichTextEditorInterceptor(editorType, node, content);
        EditorHelper.triggerAfterRichTextEditorLoaded(editorType, node, richTextEditor, content);
        return richTextEditor;
    }

    /*
    * Convenience methods for hooks with RICHTEXT_EDITOR type
    */
    public static UIElement triggerProvidesRichTextEditorInterceptor(String withType, Node node, Content content) {
        return ProvidesHelper.triggerInterceptor(Types.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerBeforeRichTextEditorLoaded(String withType, Node node, Content content) {
        OnLoadHelper.triggerBeforeInterceptor(Types.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerAfterRichTextEditorLoaded(String withType, Node node, UIElement uiElement, Content content) {
        OnLoadHelper.triggerAfterInterceptor(Types.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content), uiElement);
    }


}
