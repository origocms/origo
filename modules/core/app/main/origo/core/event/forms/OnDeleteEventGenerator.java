package main.origo.core.event.forms;

import com.google.common.collect.Lists;
import main.origo.core.InterceptorRepository;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.forms.OnDelete;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.internal.ReflectionInvoker;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class OnDeleteEventGenerator {

    public static void triggerBeforeInterceptors(String withType, Navigation navigation) {
        triggerBeforeInterceptors(withType, navigation, Collections.<String, Object>emptyMap());
    }

    public static void triggerBeforeInterceptors(String withType, Navigation navigation, Map<String, Object> args) {
        triggerInterceptors(withType, navigation, args, false);
    }

    public static void triggerAfterInterceptors(String withType, Navigation navigation) {
        triggerAfterInterceptors(withType, navigation, Collections.<String, Object>emptyMap());
    }

    public static void triggerAfterInterceptors(String withType, Navigation navigation, Map<String, Object> args) {
        triggerInterceptors(withType, navigation, args, true);
    }

    private static void triggerInterceptors(String withType, Navigation navigation, Map<String, Object> args, boolean after) {
        List<CachedAnnotation> cachedAnnotations = findOnPostInterceptorsWithType(withType, after);
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            ReflectionInvoker.execute(cachedAnnotation, navigation, args);
        }
    }

    public static void triggerBeforeInterceptors(String withType, Node node) {
        triggerBeforeInterceptors(withType, node, Collections.<String, Object>emptyMap());
    }

    public static void triggerBeforeInterceptors(String withType, Node node, Map<String, Object> args) {
        triggerInterceptors(withType, node, args, false);
    }

    public static void triggerAfterInterceptors(String withType, Node node) {
        triggerAfterInterceptors(withType, node, Collections.<String, Object>emptyMap());
    }

    public static void triggerAfterInterceptors(String withType, Node node, Map<String, Object> args) {
        triggerInterceptors(withType, node, args, true);
    }

    private static void triggerInterceptors(String withType, Node node, Map<String, Object> args, boolean after) {
        List<CachedAnnotation> cachedAnnotations = findOnPostInterceptorsWithType(withType, after);
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            ReflectionInvoker.execute(cachedAnnotation, node, args);
        }
    }

    public static void triggerBeforeInterceptors(String withType, Object object) {
        triggerBeforeInterceptors(withType, object, Collections.<String, Object>emptyMap());
    }

    public static void triggerBeforeInterceptors(String withType, Object object, Map<String, Object> args) {
        triggerInterceptors(withType, object, args, false);
    }

    public static void triggerAfterInterceptors(String withType, Object object) {
        triggerAfterInterceptors(withType, object, Collections.<String, Object>emptyMap());
    }

    public static void triggerAfterInterceptors(String withType, Object object, Map<String, Object> args) {
        triggerInterceptors(withType, object, args, true);
    }

    private static void triggerInterceptors(String withType, Object object, Map<String, Object> args, boolean after) {
        List<CachedAnnotation> cachedAnnotations = findOnPostInterceptorsWithType(withType, after);
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            ReflectionInvoker.execute(cachedAnnotation, object, args);
        }
    }

    private static List<CachedAnnotation> findOnPostInterceptorsWithType(final String withType, final boolean after) {
        List<CachedAnnotation> onPostInterceptors = Lists.newArrayList(InterceptorRepository.getInterceptors(OnDelete.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation interceptor) {
                OnDelete annotation = (OnDelete) interceptor.annotation;
                return (StringUtils.isEmpty(annotation.with()) || annotation.with().equals(withType)) && annotation.after() == after;
            }
        }));
        if (onPostInterceptors.isEmpty()) {
            Logger.trace("No @OnDelete interceptor for with=" + withType + "' and after='"+after+"'");
        }
        Collections.sort(onPostInterceptors, new Comparator<CachedAnnotation>() {
            @Override
            public int compare(CachedAnnotation o1, CachedAnnotation o2) {
                int weight1 = ((OnDelete) o1.annotation).weight();
                int weight2 = ((OnDelete) o2.annotation).weight();
                return new Integer(weight1).compareTo(weight2);
            }
        });
        return onPostInterceptors;
    }
}
