package main.origo.core.interceptors;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.ContentHelper;
import main.origo.core.ui.Element;
import models.origo.core.Segment;
import models.origo.core.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Interceptor
public class SegmentProvider {

    public static final String TYPE = "segment";

    @Provides(type = TYPE, with = Text.TYPE)
    public static Element createSegment(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        Segment segment = (Segment) args.get("segment");
        if (segment != null && !StringUtils.isBlank(segment.referenceId)) {
            Element element = ContentHelper.loadContent(node, segment.type, segment.referenceId);
            if (element != null) {
                return element;
            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
