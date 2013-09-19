package main.origo.core.event.forms;

import com.google.common.collect.Lists;
import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.internal.InterceptorExecutor;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.data.Form;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class OnSubmitEventGenerator {

    public static boolean triggerInterceptors(String withType, Validation.Result validationResult) throws NodeLoadException, ModuleException {
        return triggerInterceptors(withType, validationResult, Collections.<String, Object>emptyMap());
    }

    public static boolean triggerInterceptors(String withType, Validation.Result validationResult, Map<String, Object> args) throws ModuleException, NodeLoadException {
        List<CachedAnnotation> cachedAnnotations = findOnPostInterceptorsWithType(withType);
        if (Logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
                sb.append(" - ").append(cachedAnnotation.method.getDeclaringClass()).append(".").append(cachedAnnotation.method.getName()).append("\n");
            }
            Logger.trace("OnSubmitHandler(s) about to be triggered(in order):\n" + sb.toString());
        }
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            Class validate = ((OnSubmit) cachedAnnotation.annotation).validate();
            if (validate != null) {
                Form form = validationResult.validatedClasses.get(validate);
                boolean proceed = InterceptorExecutor.execute(cachedAnnotation, withType, form, args);
                if (!proceed) {
                    return false;
                }
            } else {
                boolean proceed = InterceptorExecutor.execute(cachedAnnotation, withType, args);
                if (!proceed) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Class> getValidatedClasses(String withType) {
        List<CachedAnnotation> cachedAnnotations = findOnPostInterceptorsWithType(withType);
        List<Class> result = Lists.newArrayList();
        for (CachedAnnotation annotation : cachedAnnotations) {
            Class validate = ((OnSubmit) annotation.annotation).validate();
            if (validate != null) {
                result.add(validate);
            }
        }
        return result;
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
