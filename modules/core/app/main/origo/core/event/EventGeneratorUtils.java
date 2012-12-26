package main.origo.core.event;

import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.internal.CachedAnnotation;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.util.List;

public class EventGeneratorUtils {

    public static CachedAnnotation selectEventHandler(String withType, List<CachedAnnotation> cachedAnnotations) {
        if (cachedAnnotations.isEmpty()) {
            return null;
        }

        String storedEventHandlerType = CoreSettingsHelper.getEventHandler(withType);
        if (StringUtils.isBlank(storedEventHandlerType)) {
            return setFirstEventHandlerAsDefault(withType, cachedAnnotations);
        }

        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            if (storedEventHandlerType.equals(cachedAnnotation.method.getDeclaringClass().getName())) {
                return cachedAnnotation;
            }
        }
        Logger.error("The stored EventHandler [" + storedEventHandlerType + "] is not available, resetting the value for type [" + withType + "]");
        return setFirstEventHandlerAsDefault(withType, cachedAnnotations);
    }

    private static CachedAnnotation setFirstEventHandlerAsDefault(String withType, List<CachedAnnotation> providers) {
        CachedAnnotation annotation = providers.iterator().next();
        Logger.info("Setting ["+annotation.getClass().getName()+"] as default for type ["+withType+"]");
        CoreSettingsHelper.setEventHandler(withType, annotation.method.getDeclaringClass());
        return annotation;
    }
}
