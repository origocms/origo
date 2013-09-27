package main.origo.core.helpers.forms;

import com.google.common.collect.Maps;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.Element;
import models.origo.core.Text;

public class EditorHelper {

    public static Element createRichTextEditor(Node node, Text text) throws NodeLoadException, ModuleException {
        EditorHelper.triggerBeforeRichTextEditorLoaded(node, Core.With.EDITOR, text);
        Element richTextEditor = EditorHelper.triggerProvidesRichTextEditorInterceptor(node, Core.With.EDITOR, text);
        EditorHelper.triggerAfterRichTextEditorLoaded(node, Core.With.EDITOR, richTextEditor);
        return richTextEditor;
    }

    /*
    * Convenience methods for hooks with RICHTEXT_EDITOR type
    */
    public static Element triggerProvidesRichTextEditorInterceptor(Node node, String withType, Text text) throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NODE, withType, text, Maps.<String, Object>newHashMap());
    }

    public static void triggerBeforeRichTextEditorLoaded(Node node, String withType, Text text) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.NODE, withType, text, Maps.<String, Object>newHashMap());
    }

    public static void triggerAfterRichTextEditorLoaded(Node node, String withType, Element element) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NODE, withType, element, Maps.<String, Object>newHashMap());
    }


}
