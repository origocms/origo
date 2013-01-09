package main.origo.core.event;

import main.origo.core.internal.CachedAnnotation;
import models.origo.core.EventHandler;
import play.Logger;

import java.lang.annotation.Annotation;
import java.util.List;

public class EventGeneratorUtils {

    public static CachedAnnotation selectEventHandler(Class<? extends Annotation> annotationType, String nodeType, String withType, List<CachedAnnotation> cachedAnnotations) {
        if (cachedAnnotations.isEmpty()) {
            return null;
        }

        EventHandler storedEventHandlerType = EventHandler.findWithNodeTypeAndWithType(nodeType, withType);
        if (storedEventHandlerType == null) {
            return setFirstEventHandlerAsDefault(annotationType, nodeType, withType, cachedAnnotations);
        }

        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            if (storedEventHandlerType.handlerClass.equals(cachedAnnotation.method.getDeclaringClass().getName())) {
                return cachedAnnotation;
            }
        }
        Logger.error("The stored EventHandler [" + storedEventHandlerType + "] is not available, resetting the value for event [" + withType + "]");
        return setFirstEventHandlerAsDefault(annotationType, nodeType, withType, cachedAnnotations);
    }

    private static CachedAnnotation setFirstEventHandlerAsDefault(Class<? extends Annotation> annotationType, String nodeType, String withType, List<CachedAnnotation> providers) {
        CachedAnnotation annotation = providers.iterator().next();
        Logger.info("Setting ["+annotation.method.getDeclaringClass().getName()+"] as default for event ["+ nodeType +"] with ["+withType+"]");
        EventHandler eventHandler = new EventHandler();
        eventHandler.nodeType = nodeType;
        eventHandler.withType = withType;
        eventHandler.annotation = annotationType.getName();
        eventHandler.handlerClass = annotation.method.getDeclaringClass().getName();
        eventHandler.save();
        return annotation;
    }
}
