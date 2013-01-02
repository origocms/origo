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
        EditorHelper.triggerBeforeRichTextEditorLoaded(Core.With.EDITOR, node, content);
        Element richTextEditor = EditorHelper.triggerProvidesRichTextEditorInterceptor(Core.With.EDITOR, node, content);
        EditorHelper.triggerAfterRichTextEditorLoaded(Core.With.EDITOR, node, richTextEditor, content);
        return richTextEditor;
    }

    /*
    * Convenience methods for hooks with RICHTEXT_EDITOR type
    */
    public static Element triggerProvidesRichTextEditorInterceptor(String withType, Node node, Content content) {
        return ProvidesEventGenerator.triggerInterceptor(Core.Type.NODE, withType, node, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerBeforeRichTextEditorLoaded(String withType, Node node, Content content) {
        OnLoadEventGenerator.triggerBeforeInterceptor(Core.Type.NODE, withType, node, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerAfterRichTextEditorLoaded(String withType, Node node, Element element, Content content) {
        OnLoadEventGenerator.triggerAfterInterceptor(Core.Type.NODE, withType, node, Collections.<String, Object>singletonMap("content", content), element);
    }


}
