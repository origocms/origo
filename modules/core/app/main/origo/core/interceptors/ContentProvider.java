package main.origo.core.interceptors;

import main.origo.core.actions.Component;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.Element;
import models.origo.core.Content;
import org.apache.commons.lang3.StringUtils;
import play.api.templates.Html;

@Interceptor
public class ContentProvider {

    @Provides(with = Content.TYPE)
    public static Element loadContent(Provides.Context context) {

        String referenceId = (String) context.args.get("identifier");

        if (!StringUtils.isBlank(referenceId)) {
            Content content = Content.findWithIdentifier(referenceId);
            if (content != null) {

                Html value = processContent(content);

                return new Element.Panel().setId(content.identifier).setBody(value);

            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

    private static Html processContent(Content content) {
        Html value = Html.apply(content.value);

        if (content.value.contains(Component.COMPONENT_MARKER)) {
            Component component = Component.getWrappedComponent();
            if (component != null) {
                int idx = content.value.indexOf(Component.COMPONENT_MARKER);
                String start = content.value.substring(idx);
                String end = content.value.substring(idx+Component.COMPONENT_MARKER.length());
                value = Html.apply(start).$plus(component.body).$plus(Html.apply(end));
            }
        }
        return value;
    }

}
