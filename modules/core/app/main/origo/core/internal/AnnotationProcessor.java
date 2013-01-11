package main.origo.core.internal;

import com.google.common.collect.Lists;
import main.origo.core.InitializationException;
import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleRepository;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.*;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import play.Logger;
import play.api.templates.Html;
import play.mvc.Result;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationProcessor {

    public static void initialize() {
        InterceptorRepository.invalidate();
        ThemeRepository.invalidate();
        ModuleRepository.invalidate();
        scanAndInitModules();
        scanModuleAnnotations();
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

    private static void scanAndInitModules() {
        Reflections reflections = new Reflections("");
        Set<Class<?>> modules = reflections.getTypesAnnotatedWith(Module.class);

        for (Class c : modules) {
            //noinspection unchecked
            Module moduleAnnotation = (Module) c.getAnnotation(Module.class);
            CachedModule cachedModule = ModuleRepository.add(moduleAnnotation, c);
            try {
                cachedModule.initMethod.invoke(CachedModule.class);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InitializationException("Unable to init module", e);
            }
        }
    }

    private static void scanModuleAnnotations() {
        List<CachedModule> modules = ModuleRepository.getAll();
        List<Prototype> prototypes = Lists.newArrayList();
        for (CachedModule module : modules) {
            try {
                if (module.annotationsMethod != null) {
                    //noinspection unchecked
                    prototypes.addAll((List<Prototype>)module.annotationsMethod.invoke(module.clazz));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InitializationException("Unable to get annotations from module", e);
            }
        }
        Reflections reflections = new Reflections("");

        Set<Class<?>> interceptors = reflections.getTypesAnnotatedWith(Interceptor.class);

        for(Prototype prototype : prototypes) {
            scanEventHandlers(interceptors, prototype);
        }

        // Themes and Decorators
        Set<Class<?>> themes = reflections.getTypesAnnotatedWith(Theme.class);
        scanThemes(themes);

        scanDecorators(interceptors);
        scanDecorators(themes);
    }

    public static void scanEventHandlers(Set<Class<?>> classes, Prototype prototype) {

        Class<? extends Annotation> annotationClass = prototype.annotation;
        Class[] parameterTypes = prototype.expectedParameterTypes;
        Class returnType = prototype.expectedReturnType;

        Logger.debug("Processing [" + annotationClass.getSimpleName() + "]");
        Logger.debug("------------------------------------------------");

        for (Class c : classes) {
            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(annotationClass));
            for (Method m : methods) {
                Class[] pc = m.getParameterTypes();
                if (pc.length != parameterTypes.length) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                            " is annotated with '" + annotationClass.getName() +
                            "' but the method does not match the required signature (different amount of parameters)");
                }
                for (int i=0; i<pc.length; i++) {
                    if (!pc[i].equals(parameterTypes[i])) {
                        throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                                " is annotated with '" + annotationClass.getName() +
                                "' but the method does not match the required signature (parameter '"+parameterTypes[i].getName()+"' has the wrong type)");
                    }
                }
                if (returnType != null && !returnType.isAssignableFrom(m.getReturnType())) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                            " is annotated with '" + annotationClass.getName() +
                            "' but the method does not match the required signature (wrong return type, epected ["+returnType+"] and found ["+m.getReturnType()+"])");
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
            //noinspection unchecked
            Theme themeAnnotation = (Theme) c.getAnnotation(Theme.class);
            ThemeRepository.addTheme(themeAnnotation.id(), c);

            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(ThemeVariant.class));

            for (Method m : methods) {

                assertCorrectSignature(m, Result.class, ThemeVariant.class, ThemeVariant.Context.class);

                ThemeVariant themeVariant = m.getAnnotation(ThemeVariant.class);
                ThemeRepository.addThemeVariant(themeAnnotation.id(), themeVariant.id(), themeVariant.regions(), m);
            }
        }
    }

    private static void scanDecorators(Set<Class<?>> classes) {

        for (Class c : classes) {
            //noinspection unchecked
            Theme themeAnnotation = (Theme) c.getAnnotation(Theme.class);

            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(Decorates.class));

            for (Method m : methods) {

                assertCorrectSignature(m, Html.class, Decorates.class, Decorates.Context.class);

                Decorates decorates = m.getAnnotation(Decorates.class);
                if (themeAnnotation != null) {
                    ThemeRepository.addDecorator(themeAnnotation.id(), decorates.types(), m, decorates.input());
                } else {
                    ThemeRepository.addDecorator(decorates.types(), m, decorates.input());
                }
            }
        }
    }

    private static void assertCorrectSignature(Method m, Class returnType, Class<? extends Annotation> annotationClass, Class... parameterTypes) {
        Class[] pc = m.getParameterTypes();
        if (pc.length != parameterTypes.length) {
            throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                    " is annotated with '" + annotationClass.getName() +
                    "' but the method does not match the required signature (different amount of parameters)");
        }
        for (int i=0; i<pc.length; i++) {
            if (!pc[i].equals(parameterTypes[i])) {
                throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                        " is annotated with '" + annotationClass.getName() +
                        "' but the method does not match the required signature (parameter '"+parameterTypes[i].getName()+"' has the wrong type)");
            }
        }
        if (!m.getReturnType().equals(returnType)) {
            throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                    " is annotated with '" + annotationClass.getName() +
                    "' but the method does not match the required signature (wrong return type)");
        }
    }

    public static class Prototype {
        public final Class<? extends Annotation> annotation;
        public final Class[] expectedParameterTypes;
        public final Class expectedReturnType;

        public Prototype(Class<? extends Annotation> annotation, Class expectedReturnType, Class... expectedParameterTypes) {
            this.annotation = annotation;
            this.expectedParameterTypes = expectedParameterTypes;
            this.expectedReturnType = expectedReturnType;
        }

    }

    public static class Dependency {
        public final String name;
        public final int major;
        public final int minor;
        public final int patch;

        public Dependency(String name, int major, int minor) {
            this(name, major, minor, -1);
        }

        public Dependency(String name, int major, int minor, int patch) {
            this.name = name;
            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }
    }

}
