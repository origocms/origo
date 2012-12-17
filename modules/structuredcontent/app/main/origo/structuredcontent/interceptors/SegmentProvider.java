package main.origo.structuredcontent.interceptors;

import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.UIElement;
import models.origo.core.Content;
import models.origo.structuredcontent.Segment;
import org.apache.commons.lang3.StringUtils;

@Interceptor
public class SegmentProvider {

    public static final String SEGMENT = "segment";

    @Provides(type = SEGMENT, with = "models.origo.core.Content")
    public static UIElement createSegment(Provides.Context context) {
        Segment segment = (Segment) context.args.get("segment");
        if (!StringUtils.isBlank(segment.referenceId)) {
            Content content = Content.findWithIdentifier(segment.referenceId);
            if (content != null) {
                return new UIElement(content.identifier, UIElement.PARAGRAPH, content.value);
            }
        }
        //TODO: Handle this somehow, in dev/admin maybe show a UIElement with a warning message and in prod swallow error?
        return null;
    }

}
