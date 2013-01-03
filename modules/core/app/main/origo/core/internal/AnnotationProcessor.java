package main.origo.core.internal;

import main.origo.core.InitializationException;
import main.origo.core.InterceptorRepository;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.*;
import main.origo.core.annotations.forms.*;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import play.Logger;
import play.api.templates.Html;
import play.mvc.Result;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationProcessor {

    public static void initialize() {
        InterceptorRepository.invalidate();
        ThemeRepository.invalidate();
        scan();
        if (Logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            Map<Class<? extends Annotation>, List<CachedAnnotation>> interceptorMap = InterceptorRepository.getInterceptorMap();
            for (Class<? extends Annotation> a : interceptorMap.keySet()) {
                List<CachedAnnotation> interceptors = interceptorMap.get(a);
                sb.append(" - ").append(a.getName()).append(" ").append(interceptors.size()).append("\n");
                count += interceptors.size();
            }
            Logger.debug("Interceptors registered: " + count + "\n" + sb.toString());

            Map<String, CachedTheme> themesMap = ThemeRepository.getThemesMap();
            sb = new StringBuilder("Themes registered: ").append(themesMap.size()).append("\n");
            for (String themeId : themesMap.keySet()) {
                sb.append(" - ").append(themeId).append("\n");
                sb.append("    - ").append("variants").append(" ").append(themesMap.get(themeId).getThemeVariants().size()).append("\n");
                sb.append("    - ").append("decorators").append(" ").append(themesMap.get(themeId).getDecorators().size()).append("\n");
            }
            Logger.debug(sb.toString());
        }
    }

    private static void scan() {
        Reflections reflections = new Reflections("");

        Set<Class<?>> interceptors = reflections.getTypesAnnotatedWith(Interceptor.class);
        scanEventHandlers(interceptors, Provides.class, Provides.Context.class);
        scanEventHandlers(interceptors, OnLoad.class, OnLoad.Context.class);
        scanEventHandlers(interceptors, ProvidesForm.class, ProvidesForm.Context.class);
        scanEventHandlers(interceptors, OnLoadForm.class, OnLoadForm.Context.class);
        scanEventHandlers(interceptors, OnSubmit.class, OnSubmit.Context.class);
        scanEventHandlers(interceptors, SubmitHandler.class, SubmitHandler.Context.class);
        scanEventHandlers(interceptors, SubmitState.class, SubmitState.Context.class);
        scanEventHandlers(interceptors, OnInsertElement.class, OnInsertElement.Context.class);
        scanEventHandlers(interceptors, OnRemoveElement.class, OnRemoveElement.Context.class);

        Set<Class<?>> themes = reflections.getTypesAnnotatedWith(Theme.class);
        scanThemes(themes);

        scanDecorators(interceptors);
        scanDecorators(themes);
    }

    public static void scanEventHandlers(Set<Class<?>> classes, Class<? extends Annotation> annotationClass, Class contextClass) {

        Logger.debug("Processing [" + annotationClass.getSimpleName() + "]");
        Logger.debug("------------------------------------------------");

        for (Class c : classes) {
            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(annotationClass));
            for (Method m : methods) {
                Class[] pc = m.getParameterTypes();
                if (pc.length > 1 || !pc[0].equals(contextClass)) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                            " is annotated with '" + annotationClass.getName() +
                            "' but the method does not match the required signature");
                }

                Logger.debug("Analyzing '" + m.getDeclaringClass() + "." + m.getName() + "'");

                Relationship relationship = m.getAnnotation(Relationship.class);
                if (relationship != null) {
                    InterceptorRepository.add(m.getAnnotation(annotationClass), m, relationship);
                } else {
                    InterceptorRepository.add(m.getAnnotation(annotationClass), m);
                }
            }
        }
        Logger.debug(" ");
    }

    private static void scanThemes(Set<Class<?>> classes) {

        for (Class c : classes) {
            Theme themeAnnotation = (Theme) c.getAnnotation(Theme.class);
            ThemeRepository.addTheme(themeAnnotation.id(), c);

            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(ThemeVariant.class));

            for (Method m : methods) {

                assertCorrectSignature(m, Result.class, ThemeVariant.Context.class, ThemeVariant.class);

                ThemeVariant themeVariant = m.getAnnotation(ThemeVariant.class);
                ThemeRepository.addThemeVariant(themeAnnotation.id(), themeVariant.id(), themeVariant.regions(), m);
            }
        }
    }

    private static void scanDecorators(Set<Class<?>> classes) {

        for (Class c : classes) {
            Theme themeAnnotation = (Theme) c.getAnnotation(Theme.class);

            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(Decorates.class));

            for (Method m : methods) {

                assertCorrectSignature(m, Html.class, Decorates.Context.class, Decorates.class);

                Decorates decorates = m.getAnnotation(Decorates.class);
                if (themeAnnotation != null) {
                    ThemeRepository.addDecorator(themeAnnotation.id(), decorates.types(), m, decorates.input());
                } else {
                    ThemeRepository.addDecorator(decorates.types(), m, decorates.input());
                }
            }
        }
    }

    private static void assertCorrectSignature(Method m, Class returnType, Class contextClass, Class<? extends Annotation> annotationClass) {
        Class[] pc = m.getParameterTypes();
        if (pc.length > 1 || !pc[0].equals(contextClass) || !m.getReturnType().equals(returnType)) {
            throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                    " is annotated with '" + annotationClass.getName() +
                    "' but the method does not match the required signature");
        }
    }

}
