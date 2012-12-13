package main.origo.core;

import com.google.common.base.Predicates;
import main.origo.core.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import play.Logger;
import play.Play;
import play.mvc.Result;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationProcessor {

    public static void scan() {
        Listeners.invalidate();
        String packagesToScan = Play.application().configuration().getString("origo.scan.packages");
        scan(getFilterString(packagesToScan));
        if (Logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            Map<Annotation,List<CachedAnnotation>> listenerMap = Listeners.getListenerMap();
            for (Annotation a : listenerMap.keySet()) {
                sb.append(" - ").append(a.annotationType().getSimpleName()).append(" ").append(listenerMap.get(a).size()).append("\n");
                count += listenerMap.get(a).size();
            }
            Logger.debug("Listeners registered: " + count + "\n" + sb.toString());

            Map<String,CachedTheme> themesMap = Themes.getThemesMap();
            sb = new StringBuilder("Themes registered: ").append(themesMap.size()).append("\n");
            for (String themeId : themesMap.keySet()) {
                sb.append(" - ").append(themeId).append("\n");
                sb.append("    - ").append("variants").append(" ").append(themesMap.get(themeId).getThemeVariants().size()).append("\n");
                sb.append("    - ").append("decorators").append(" ").append(themesMap.get(themeId).getDecorators().size()).append("\n");
            }
            Logger.debug(sb.toString());
        }
    }

    private static void scan(String filter) {
        Reflections reflections = new Reflections("");

        Set<Class<?>> interceptors = reflections.getTypesAnnotatedWith(Interceptor.class);
        scanInterceptors(interceptors, Provides.class, void.class, Provides.Context.class);
        scanInterceptors(interceptors, OnLoad.class, void.class, OnLoad.Context.class);

        Set<Class<?>> themes = reflections.getTypesAnnotatedWith(Theme.class);
        scanThemes(themes);

        scanThemeParts(themes, Decorates.class, String.class, Decorates.Context.class, new AddThemePartFunction() {
            @Override
            public void add(Theme theme, Annotation annotation, Class declaringClass, MethodHandle methodHandle) {
                Decorates decorates = (Decorates) annotation;
                Themes.addDecorator(theme.id(), decorates.type(), declaringClass, methodHandle);
            }
        });
        scanThemeParts(themes, ThemeVariant.class, Result.class, ThemeVariant.Context.class, new AddThemePartFunction() {
            @Override
            public void add(Theme theme, Annotation annotation, Class declaringClass, MethodHandle methodHandle) {
                ThemeVariant themeVariant = (ThemeVariant) annotation;
                Themes.addThemeVariant(theme.id(), themeVariant.id(), themeVariant.regions(), declaringClass, methodHandle);
            }
        });
    }

    private static void scanInterceptors(Set<Class<?>> classes, Class<? extends Annotation> annotationClass, Class returnType, Class contextClass) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        for(Class c : classes) {
            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(annotationClass));
            for (Method m : methods) {
                MethodType methodType = MethodType.methodType(returnType, contextClass);
                try {
                    MethodHandle methodHandle = lookup.findStatic(m.getDeclaringClass(), m.getName(), methodType);
                    Listeners.addListener(m.getAnnotation(annotationClass), m.getDeclaringClass(), methodHandle);
                } catch (NoSuchMethodException | IllegalAccessException e) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                            " is annotated with '" + annotationClass.getName() +
                            "' but the method signature does not match the required signature", e);
                }
            }
        }
    }

    private static void scanThemes(Set<Class<?>> classes) {

        for(Class c : classes) {
            Theme themeAnnotation = (Theme)c.getAnnotation(Theme.class);
            Themes.addTheme(themeAnnotation.id(), c);
        }
    }

    private static void scanThemeParts(Set<Class<?>> classes, Class<? extends Annotation> annotationClass, Class returnType, Class contextClass, AddThemePartFunction addThemepartFunction) {

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        for(Class c : classes) {
            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(annotationClass));

            Theme themeAnnotation = (Theme)c.getAnnotation(Theme.class);

            for (Method m : methods) {

                MethodType methodType = MethodType.methodType(returnType, contextClass);
                try {
                    MethodHandle methodHandle = lookup.findStatic(c, m.getName(), methodType);
                    Annotation annotation = m.getAnnotation(annotationClass);
                    addThemepartFunction.add(themeAnnotation, annotation, c, methodHandle);
                } catch (NoSuchMethodException | IllegalAccessException e) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() +
                            "' is annotated with '" + annotationClass.getName() +
                            "' but the method signature does not match the required signature", e);
                }
            }
        }
    }

    private static interface AddThemePartFunction {
        public void add(Theme theme, Annotation annotation, Class declaringClass, MethodHandle methodHandle);
    }

    private static String getFilterString(String filter) {
        StringBuilder result = new StringBuilder("+main.origo.core");
        if (StringUtils.isNotBlank(filter)) {
            result.append(",").append(filter);
        }
        return result.toString();
    }

}
