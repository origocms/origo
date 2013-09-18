package main.origo.core.event;

import com.google.common.collect.Lists;
import main.origo.core.*;
import main.origo.core.annotations.OnLoad;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;
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
                try {
                    cachedAnnotation.method.invoke(null, node, withType, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                try {
                    cachedAnnotation.method.invoke(null, node, withType, navigation, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Form form, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                try {
                    cachedAnnotation.method.invoke(null, node, withType, form, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Element element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                try {
                    cachedAnnotation.method.invoke(null, node, withType, element, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                try {
                    cachedAnnotation.method.invoke(null, node, withType, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                try {
                    cachedAnnotation.method.invoke(null, node, withType, navigation, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Form form, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                try {
                    cachedAnnotation.method.invoke(null, node, withType, form, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Element element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                try {
                    cachedAnnotation.method.invoke(null, node, withType, element, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Navigation navigation, NavigationElement element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                try {
                    cachedAnnotation.method.invoke(null, node, withType, navigation, element, args);
                } catch (Exception e) {
                    getCause(cachedAnnotation, e);
                }
            }
        }
    }

    private static void getCause(CachedAnnotation cachedAnnotation, Exception e) throws ModuleException, NodeLoadException {
        if (e.getCause() instanceof ModuleException) {
            throw (ModuleException) e.getCause();
        } else if (e.getCause() instanceof NodeLoadException) {
            throw (NodeLoadException) e.getCause();
        } else if (e instanceof IllegalArgumentException) {
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]: "+ e.getMessage());
        } else {
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
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
