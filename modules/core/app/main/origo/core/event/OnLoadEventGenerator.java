package main.origo.core.event;

import com.google.common.collect.Lists;
import main.origo.core.*;
import main.origo.core.annotations.OnLoad;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.internal.ReflectionInvoker;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;
import models.origo.core.Content;
import org.apache.commons.lang3.StringUtils;
import play.data.Form;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Helper to trigger \@OnLoad interceptors. Should not be used directly, use NodeHelper instead.
 *
 * @see main.origo.core.helpers.NodeHelper
 * @see main.origo.core.annotations.OnLoad
 */
public class OnLoadEventGenerator {

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, args);
            }
        }
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, navigation, args);
            }
        }
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Form form, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, form, args);
            }
        }
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Element element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, element, args);
            }
        }
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Content content, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, content, args);
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, args);
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, navigation, args);
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Form form, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, form, args);
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Element element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, element, args);
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Form form, Element element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, form, element, args);
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Navigation navigation, NavigationElement element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, navigation, element, args);
            }
        }
    }

    private static List<CachedAnnotation> findInterceptorForType(final String onLoadType, final String withType, final boolean after) {
        List<CachedAnnotation> interceptors = Lists.newArrayList(InterceptorRepository.getInterceptors(OnLoad.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnLoad annotation = ((OnLoad) cachedAnnotation.annotation);
                return annotation.type().equals(onLoadType) && annotation.after() == after &&
                        (StringUtils.isBlank(annotation.with()) || annotation.with().equals(withType));
            }
        }));
        Collections.sort(interceptors, new Comparator<CachedAnnotation>() {
            @Override
            public int compare(CachedAnnotation o1, CachedAnnotation o2) {
                int weight1 = ((OnLoad) o1.annotation).weight();
                int weight2 = ((OnLoad) o2.annotation).weight();
                return new Integer(weight1).compareTo(weight2);
            }
        });
        return interceptors;
    }

}
