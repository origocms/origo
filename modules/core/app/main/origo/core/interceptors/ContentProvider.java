package main.origo.core.interceptors;

import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import models.origo.core.Content;
import org.apache.commons.lang3.StringUtils;

@Interceptor
public class ContentProvider {

    @Provides(with = Content.TYPE)
    public static Content loadContent(Provides.Context context) {

        String referenceId = (String) context.args.get("identifier");

        if (!StringUtils.isBlank(referenceId)) {
            return Content.findWithIdentifier(referenceId);
        }

        return null;
    }

}
