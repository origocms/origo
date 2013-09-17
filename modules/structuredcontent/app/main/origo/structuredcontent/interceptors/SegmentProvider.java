package main.origo.structuredcontent.interceptors;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.ContentHelper;
import main.origo.core.ui.Element;
import models.origo.core.Content;
import models.origo.structuredcontent.Segment;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Interceptor
public class SegmentProvider {

    public static final String SEGMENT = "segment";

    @Provides(type = SEGMENT, with = Content.TYPE)
    public static Element createSegment(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        Segment segment = (Segment) args.get("segment");
        if (!StringUtils.isBlank(segment.referenceId)) {
            Content content = ContentHelper.loadContent(node, segment.referenceId);
            if (content != null) {
                return new Element.Panel().setId(content.identifier).setBody(content.value);
            }
        }
        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
