package main.origo.core.interceptors;

import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.Element;
import models.origo.core.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Interceptor
public class TextProvider {

    @Provides(type = Core.Type.CONTENT, with = Text.TYPE)
    public static Element loadText(Node node, String withType, Map<String, Object> args) {

        String referenceId = (String) args.get("identifier");
        if (!StringUtils.isBlank(referenceId)) {
            Text text = Text.findWithIdentifier(referenceId);
            if (text != null) {
                return new Element.Panel().setId(text.identifier).setBody(text.value);
            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
