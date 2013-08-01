package main.origo.core.event.forms;

import com.google.common.collect.Lists;
import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.internal.CachedAnnotation;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class OnSubmitEventGenerator {

    public static boolean triggerInterceptors(String withType) throws NodeLoadException, ModuleException {
        return triggerInterceptors(withType, Collections.<String, Object>emptyMap());
    }

    public static boolean triggerInterceptors(String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        List<CachedAnnotation> cachedAnnotations = findOnPostInterceptorsWithType(withType);
        if (Logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
                sb.append(" - ").append(cachedAnnotation.method.getDeclaringClass()).append(".").append(cachedAnnotation.method.getName()).append("\n");
            }
            Logger.trace("OnSubmitHandler(s) about to be triggered(in order):\n" + sb.toString());
        }
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            try {
                //noinspection unchecked
                if (!(Boolean)cachedAnnotation.method.invoke(null, new OnSubmit.Context(args))) {
                    return false;
                }
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof ModuleException) {
                    throw (ModuleException) e.getCause();
                } else if (e.getCause() instanceof NodeLoadException) {
                    throw (NodeLoadException) e.getCause();
                } else {
                    throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
            }
        }
        return true;
    }

    private static List<CachedAnnotation> findOnPostInterceptorsWithType(final String withType) {
        List<CachedAnnotation> onPostInterceptors = Lists.newArrayList(InterceptorRepository.getInterceptors(OnSubmit.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation interceptor) {
                OnSubmit annotation = (OnSubmit) interceptor.annotation;
                return StringUtils.isEmpty(annotation.with()) || annotation.with().equals(withType);
            }
        }));
        if (onPostInterceptors.isEmpty()) {
            Logger.warn("No @OnSubmit interceptor for with=" + withType + "'");
        }
        Collections.sort(onPostInterceptors, new Comparator<CachedAnnotation>() {
            @Override
            public int compare(CachedAnnotation o1, CachedAnnotation o2) {
                int weight1 = ((OnSubmit) o1.annotation).weight();
                int weight2 = ((OnSubmit) o2.annotation).weight();
                return new Integer(weight1).compareTo(weight2);
            }
        });
        return onPostInterceptors;
    }


}
