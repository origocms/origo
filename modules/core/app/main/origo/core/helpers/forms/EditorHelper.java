package main.origo.core.helpers.forms;

import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.Element;
import models.origo.core.Content;

import java.util.Collections;

public class EditorHelper {

    public static Element createRichTextEditor(Node node, Content content) {
        EditorHelper.triggerBeforeRichTextEditorLoaded(node, Core.With.EDITOR, content);
        Element richTextEditor = EditorHelper.triggerProvidesRichTextEditorInterceptor(node, Core.With.EDITOR, content);
        EditorHelper.triggerAfterRichTextEditorLoaded(node, Core.With.EDITOR, richTextEditor, content);
        return richTextEditor;
    }

    /*
    * Convenience methods for hooks with RICHTEXT_EDITOR type
    */
    public static Element triggerProvidesRichTextEditorInterceptor(Node node, String withType, Content content) {
        return ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NODE, withType, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerBeforeRichTextEditorLoaded(Node node, String withType, Content content) {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.NODE, withType, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerAfterRichTextEditorLoaded(Node node, String withType, Element element, Content content) {
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NODE, withType, element, Collections.<String, Object>singletonMap("content", content));
    }


}
