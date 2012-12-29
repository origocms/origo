package main.origo.core.helpers.forms;

import main.origo.core.Node;
import main.origo.core.annotations.Types;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.interceptors.forms.TinyMCEEditorProvider;
import main.origo.core.interceptors.forms.WysiHTML5EditorProvider;
import main.origo.core.ui.Element;
import models.origo.core.Content;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.util.Collections;

public class EditorHelper {

    public static Element createRichTextEditor(Node node, Content content) {
        String editorType = CoreSettingsHelper.getEditorType();
        if (StringUtils.isBlank(editorType) ) {
            Logger.debug("Editor type not set, using default "+ WysiHTML5EditorProvider.class.getName());
            editorType = TinyMCEEditorProvider.EDITOR_TYPE;
        }
        EditorHelper.triggerBeforeRichTextEditorLoaded(editorType, node, content);
        Element richTextEditor = EditorHelper.triggerProvidesRichTextEditorInterceptor(editorType, node, content);
        EditorHelper.triggerAfterRichTextEditorLoaded(editorType, node, richTextEditor, content);
        return richTextEditor;
    }

    /*
    * Convenience methods for hooks with RICHTEXT_EDITOR type
    */
    public static Element triggerProvidesRichTextEditorInterceptor(String withType, Node node, Content content) {
        return ProvidesEventGenerator.triggerInterceptor(Types.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerBeforeRichTextEditorLoaded(String withType, Node node, Content content) {
        OnLoadEventGenerator.triggerBeforeInterceptor(Types.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content));
    }

    public static void triggerAfterRichTextEditorLoaded(String withType, Node node, Element element, Content content) {
        OnLoadEventGenerator.triggerAfterInterceptor(Types.RICHTEXT_EDITOR, withType, node, Collections.<String, Object>singletonMap("content", content), element);
    }


}
