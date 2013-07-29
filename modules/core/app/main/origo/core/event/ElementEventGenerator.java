package main.origo.core.event;

import com.google.common.collect.Lists;
import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.annotations.OnRemoveElement;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
import play.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ElementEventGenerator {

    public static void triggerBeforeInsert(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(element.getClass(), element.getInputType(), true);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(NodeContext.current().node, parent, element));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + annotation.method.toString() + "]", e.getCause());
            }
        }
    }

    public static void triggerAfterInsert(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(element.getClass(), element.getInputType(), false);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(NodeContext.current().node, parent, element));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + annotation.method.toString() + "]", e.getCause());
            }
        }
    }

    public static void triggerBeforeRemove(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnRemoveInterceptors(element.getClass(), element.getInputType(), true);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnRemoveElement.Context(NodeContext.current().node, parent, element));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + annotation.method.toString() + "]", e.getCause());
            }
        }
    }

    public static void triggerAfterRemove(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnRemoveInterceptors(element.getClass(), element.getInputType(), false);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnRemoveElement.Context(NodeContext.current().node, parent, element));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + annotation.method.toString() + "]", e.getCause());
            }
        }
    }

    private static List<CachedAnnotation> findOnInsertInterceptors(final Class withType, final Class inputType, final boolean before) {
        List<CachedAnnotation> interceptors = Lists.newArrayList(InterceptorRepository.getInterceptors(OnInsertElement.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnInsertElement annotation = (OnInsertElement) cachedAnnotation.annotation;
                return before != annotation.after() && (annotation.with().equals(withType) || withType == null) &&
                        (inputType == null || annotation.input().equals(inputType));
            }
        }));
        Collections.sort(interceptors, new Comparator<CachedAnnotation>() {
            @Override
            public int compare(CachedAnnotation o1, CachedAnnotation o2) {
                int weight1 = ((OnInsertElement) o1.annotation).weight();
                int weight2 = ((OnInsertElement) o2.annotation).weight();
                return new Integer(weight1).compareTo(weight2);
            }
        });
        return interceptors;
    }

    private static List<CachedAnnotation> findOnRemoveInterceptors(final Class withType, final Class inputType, final boolean before) {
        List<CachedAnnotation> interceptors = Lists.newArrayList(InterceptorRepository.getInterceptors(OnRemoveElement.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnRemoveElement annotation = (OnRemoveElement) cachedAnnotation.annotation;
                return before != annotation.after() && (annotation.with().equals(withType) || withType == null) &&
                        (inputType == null || annotation.input().equals(inputType));
            }
        }));
        Collections.sort(interceptors, new Comparator<CachedAnnotation>() {
            @Override
            public int compare(CachedAnnotation o1, CachedAnnotation o2) {
                int weight1 = ((OnRemoveElement) o1.annotation).weight();
                int weight2 = ((OnRemoveElement) o2.annotation).weight();
                return new Integer(weight1).compareTo(weight2);
            }
        });
        return interceptors;
    }
}
