package main.origo.core.event;

import main.origo.core.internal.CachedAnnotation;
import models.origo.core.EventHandler;
import play.Logger;

import java.lang.annotation.Annotation;
import java.util.List;

public class EventGeneratorUtils {

    public static CachedAnnotation selectEventHandler(Class<? extends Annotation> annotationType, String withType, List<CachedAnnotation> cachedAnnotations) {
        if (cachedAnnotations.isEmpty()) {
            return null;
        }

        EventHandler storedEventEventHandlerType = EventHandler.findWithWithType(withType);
        if (storedEventEventHandlerType == null) {
            return setFirstEventHandlerAsDefault(annotationType, withType, cachedAnnotations);
        }

        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            if (storedEventEventHandlerType.handlerClass.equals(cachedAnnotation.method.getDeclaringClass().getName())) {
                return cachedAnnotation;
            }
        }
        Logger.error("The stored EventHandler [" + storedEventEventHandlerType + "] is not available, resetting the value for type [" + withType + "]");
        return setFirstEventHandlerAsDefault(annotationType, withType, cachedAnnotations);
    }

    private static CachedAnnotation setFirstEventHandlerAsDefault(Class<? extends Annotation> annotationType, String withType, List<CachedAnnotation> providers) {
        CachedAnnotation annotation = providers.iterator().next();
        Logger.info("Setting ["+annotation.method.getDeclaringClass().getName()+"] as default for type ["+withType+"]");
        EventHandler eventHandler = new EventHandler();
        eventHandler.withType = withType;
        eventHandler.eventName = annotationType.getName();
        eventHandler.handlerClass = annotation.method.getDeclaringClass().getName();
        eventHandler.save();
        return annotation;
    }
}
