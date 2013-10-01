package main.origo.core.interceptors;

import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.Element;
import models.origo.core.Segment;
import models.origo.core.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Interceptor
public class TextProvider {

    @Provides(type = Core.Type.CONTENT, with = Text.TYPE)
    public static Element loadText(Node node, String withType, Segment segment, Map<String, Object> args) {

        if (segment != null && StringUtils.isNotBlank(segment.referenceId)) {
            Text text = Text.findWithIdentifier(segment.referenceId);
            if (text != null) {
                return new Element.Panel().setId(segment.identifier).addChild(new Element.Paragraph().setId(text.identifier).setBody(text.value));
            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
