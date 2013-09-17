package main.origo.core.interceptors;

import main.origo.core.Node;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import models.origo.core.Content;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Interceptor
public class ContentProvider {

    @Provides(with = Content.TYPE)
    public static Content loadContent(Node node, String withType, Map<String, Object> args) {

        String referenceId = (String) args.get("identifier");

        if (!StringUtils.isBlank(referenceId)) {
            return Content.findWithIdentifier(referenceId);
        }

        return null;
    }

}
