package main.origo.core.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import main.origo.core.InitializationException;
import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleRepository;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.*;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderedNode;
import main.origo.core.ui.RenderingContext;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import play.Logger;
import play.api.templates.Html;
import play.mvc.Content;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class AnnotationProcessor {

    public static void initialize() {
        InterceptorRepository.invalidate();
        ThemeRepository.invalidate();
        ModuleRepository.invalidate();
        scanModules();
        scanModuleSuppliedAnnotations();
        initModules();
        if (Logger.isDebugEnabled()) {

            StringBuilder sb = new StringBuilder();
            for (CachedModule cachedModule : ModuleRepository.getAll()) {
                sb.append(" - ").append(cachedModule).append("\n");
            }
            Logger.debug("Modules registered: " + ModuleRepository.getAll().size() + "\n" + sb.toString());

            sb = new StringBuilder();
            int count = 0;
            Map<Class<? extends Annotation>, Set<CachedAnnotation>> interceptorMap = InterceptorRepository.getInterceptorMap();
            for (Class<? extends Annotation> a : interceptorMap.keySet()) {
                Set<CachedAnnotation> interceptors = interceptorMap.get(a);
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

    private static void scanModules() {

        final List<Class<?>> modulesClasses = getSortedModuleClasses();

        // First pass: Add all modules
        for (Class c : modulesClasses) {
            //noinspection unchecked
            ModuleRepository.add((Module) c.getAnnotation(Module.class), c);
        }
        // Second pass: Verify dependencies
        for (Class c : modulesClasses) {
            Module moduleAnnotation = (Module) c.getAnnotation(Module.class);
            assertModuleDependencies(ModuleRepository.getModule(moduleAnnotation.name()));
        }
    }

    private static void initModules() {
        // Third pass: Init all modules
        for (CachedModule cachedModule : ModuleRepository.getAll()) {
            try {
                if (cachedModule.initMethod != null) {
                    cachedModule.initMethod.invoke(CachedModule.class);
                }
            } catch (InvocationTargetException e) {
                if (e.getCause() != null) {
                    if (e.getCause() instanceof InitializationException) {
                        throw (InitializationException) e.getCause();
                    }
                    throw new InitializationException("Unable to init module", e.getCause());
                }
                throw new InitializationException("Unable to init module", e);
            } catch (IllegalAccessException e) {
                throw new InitializationException("Unable to init module", e);
            }
        }
    }

    private static void scanModuleSuppliedAnnotations() {

        Map<Class<? extends Annotation>, List<Prototype>> annotationPrototypes = Maps.newHashMap();

        for (CachedModule module : ModuleRepository.getAll()) {

            Logger.trace("------------------------------------------------");
            Logger.trace("- Processing module '" + module.name + "' (" + Arrays.toString(module.annotation.packages()) + ")");
            Logger.trace("------------------------------------------------");

            for (String packageToScan : module.annotation.packages()) {

                Reflections reflections = new Reflections(
                        new ConfigurationBuilder()
                                .addUrls(ClasspathHelper.forPackage(packageToScan))
                                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));

                Set<Class<?>> interceptors = reflections.getTypesAnnotatedWith(Interceptor.class);

                try {
                    if (module.annotationsMethod != null) {
                        //noinspection unchecked
                        List<Prototype> prototypes = (List<Prototype>) module.annotationsMethod.invoke(module.clazz);
                        for (Prototype prototype : prototypes) {
                            if (!annotationPrototypes.containsKey(prototype.annotation)) {
                                annotationPrototypes.put(prototype.annotation, Lists.<Prototype>newArrayList());
                            }
                            List<Prototype> list = annotationPrototypes.get(prototype.annotation);
                            list.add(prototype);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InitializationException("Unable to get annotations from module", e);
                }

                for (Class<? extends Annotation> annotation : annotationPrototypes.keySet()) {
                    scanEventHandlers(module, interceptors, annotation, annotationPrototypes.get(annotation));
                }

                // Themes and Decorators
                Set<Class<?>> themes = reflections.getTypesAnnotatedWith(Theme.class);
                scanThemes(themes);

                scanDecorators(interceptors);
                scanDecorators(themes);
            }
        }
    }

    public static void scanEventHandlers(CachedModule module, Set<Class<?>> classes, Class<? extends Annotation> annotationClass, List<Prototype> prototypes) {

        Logger.trace("- - Processing [" + annotationClass.getSimpleName() + "]");

        Set<Method> unmatchedMethods = Sets.newHashSet();

        // First pass to find methods matching a prototype
        for (Class c : classes) {

            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(annotationClass));
            for (Method m : methods) {

                // Basic sanity checks
                if (!Modifier.isStatic(m.getModifiers())) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() +
                            "' is annotated with '" + annotationClass.getName() +
                            "' but the method is not static");
                }
                if (Modifier.isPrivate(m.getModifiers()) || Modifier.isProtected(m.getModifiers())) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() +
                            "' is annotated with '" + annotationClass.getName() +
                            "' but the method is not public");
                }

                boolean matched = false;
                // Check if parameters and return type matches
                for (Prototype prototype : prototypes) {
                    if (matchPrototype(prototype, m)) {
                        Relationship relationship = m.getAnnotation(Relationship.class);
                        if (relationship != null) {
                            InterceptorRepository.add(module, m.getAnnotation(annotationClass), m, relationship);
                        } else {
                            InterceptorRepository.add(module, m.getAnnotation(annotationClass), m);
                        }
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    unmatchedMethods.add(m);
                }

            }
        }

        // Second pass to give some more information about what methods failed
        if (!unmatchedMethods.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Method m : unmatchedMethods) {
                for (Prototype prototype : prototypes) {
                    // Check parameters first
                    Class[] parameterTypes = prototype.expectedParameterTypes;
                    Class[] pc = m.getParameterTypes();
                    if (pc.length != parameterTypes.length) {
                        sb.append("Method '").append(m.getDeclaringClass()).append(".").append(m.getName()).append("' is annotated with '").append(annotationClass.getName()).append("' but the method does not match the required signature (different amount of parameters)\n");
                        break;
                    }
                    for (int i = 0; i < pc.length; i++) {
                        //noinspection unchecked
                        if (!parameterTypes[i].isAssignableFrom(pc[i])) {
                            sb.append("Method '").append(m.getDeclaringClass()).append(".").append(m.getName()).append("' is annotated with '").append(annotationClass.getName()).append("' but the method does not match the required signature (parameter '").append(parameterTypes[i].getName()).append("' has the wrong type)\n");
                            break;
                        }
                    }
                    // Parameters match so check return type
                    Class returnType = prototype.expectedReturnType;
                    //noinspection unchecked
                    if (returnType != null && !returnType.isAssignableFrom(m.getReturnType())) {
                        sb.append("Method '").append(m.getDeclaringClass()).append(".").append(m.getName()).append("' is annotated with '").append(annotationClass.getName()).append("' but the method does not match the required signature (wrong return type, expected [").append(returnType).append("] and found [").append(m.getReturnType()).append("])");
                        break;
                    }
                }
            }
            if (sb.length() > 0) {
                throw new InitializationException(sb.toString());
            }
        }

        Logger.trace(" ");
    }

    private static boolean matchPrototype(Prototype prototype, Method m) {

        Class returnType = prototype.expectedReturnType;

        if (!isParametersMatching(prototype, m)) {
            return false;
        }

        //noinspection unchecked
        if (returnType != null && !returnType.isAssignableFrom(m.getReturnType())) {
            return false;
        }

        Logger.trace("- - - Found '" + m.getDeclaringClass() + "." + m.getName() + "'");

        return true;
    }

    private static boolean isParametersMatching(Prototype prototype, Method m) {
        Class[] expectedParameterTypes = prototype.expectedParameterTypes;
        Class[] pc = m.getParameterTypes();
        if (pc.length != expectedParameterTypes.length) {
            return false;
        }
        for (int i = 0; i < pc.length; i++) {
            //noinspection unchecked
            if (!expectedParameterTypes[i].isAssignableFrom(pc[i])) {
                return false;
            }
        }
        return true;
    }

    private static void scanThemes(Set<Class<?>> classes) {

        for (Class c : classes) {
            //noinspection unchecked
            Theme themeAnnotation = (Theme) c.getAnnotation(Theme.class);
            ThemeRepository.addTheme(themeAnnotation.id(), c);

            Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(ThemeVariant.class));

            for (Method m : methods) {

                assertCorrectSignature(m, Content.class, ThemeVariant.class, RenderedNode.class);

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

                assertCorrectSignature(m, Html.class, Decorates.class, Element.class, RenderingContext.class);

                Decorates decorates = m.getAnnotation(Decorates.class);
                if (themeAnnotation != null) {
                    ThemeRepository.addDecorator(themeAnnotation.id(), decorates.types(), m, decorates.input());
                } else {
                    ThemeRepository.addDecorator(decorates.types(), m, decorates.input());
                }
            }
        }
    }

    private static void assertModuleDependencies(CachedModule module) {
        try {
            if (module.dependenciesMethod == null) {
                Logger.debug("Module '" + module.name + "' has no dependencies");
                return;
            }

            @SuppressWarnings("unchecked") Collection<Dependency> dependencies = (Collection<Dependency>) module.dependenciesMethod.invoke(module);

            for (Dependency dependency : dependencies) {
                CachedModule provider = ModuleRepository.getModule(dependency.name);
                if (provider == null) {
                    throw new InitializationException("Module '" + module.name + "' requires '" + dependency + "' but it is not installed");
                }
                Logger.debug("Module '" + module.name + "' requires module '" + dependency + "' ");
                if (dependency.minimum) {
                    if (dependency.major < provider.moduleVersion.major()) {
                        if (dependency.minor < provider.moduleVersion.minor()) {
                            if (dependency.patch < provider.moduleVersion.patch()) {
                                throw new InitializationException("Module '" + module.name + "' requires '" + dependency + "' but the installed version is " + provider.version());
                            }
                        }
                    }
                } else {
                    if (dependency.major > provider.moduleVersion.major()) {
                        if (dependency.minor > provider.moduleVersion.minor()) {
                            if (dependency.patch > provider.moduleVersion.patch()) {
                                throw new InitializationException("Module '" + module.name + "' requires '" + dependency + "' but the installed version is " + provider.version());
                            }
                        }
                    }
                }
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InitializationException("Unable to get annotations from module", e);
        }
    }

    private static void assertCorrectSignature(Method m, Class returnType, Class<? extends Annotation> annotationClass, Class... parameterTypes) {
        Class[] pc = m.getParameterTypes();
        if (pc.length != parameterTypes.length) {
            throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                    " is annotated with '" + annotationClass.getName() +
                    "' but the method does not match the required signature (different amount of parameters)");
        }
        for (int i = 0; i < pc.length; i++) {
            if (!pc[i].equals(parameterTypes[i])) {
                throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                        " is annotated with '" + annotationClass.getName() +
                        "' but the method does not match the required signature (parameter '" + parameterTypes[i].getName() + "' has the wrong type)");
            }
        }
        if (!m.getReturnType().equals(returnType)) {
            throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() + "' in " +
                    " is annotated with '" + annotationClass.getName() +
                    "' but the method does not match the required signature (wrong return type)");
        }
    }

    private static List<Class<?>> getSortedModuleClasses() {
        Reflections reflections = new Reflections("");
        final Set<Class<?>> modulesClasses = reflections.getTypesAnnotatedWith(Module.class);

        final List<Class<?>> sortedModulesClasses = new ArrayList<>();
        sortedModulesClasses.addAll(modulesClasses);
        Collections.sort(sortedModulesClasses, new Comparator<Class<?>>() {
            @Override
            public int compare(Class c1, Class c2) {
                Module m1 = (Module) c1.getAnnotation(Module.class);
                Module m2 = (Module) c2.getAnnotation(Module.class);
                return new Integer(m1.order()).compareTo(m2.order());
            }
        });
        return sortedModulesClasses;
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
        public final boolean minimum;
        public final int major;
        public final int minor;
        public final int patch;

        public Dependency(String name, int major, int minor) {
            this(name, true, major, minor);
        }

        public Dependency(String name, boolean minimum, int major, int minor) {
            this(name, minimum, major, minor, 0);
        }

        public Dependency(String name, boolean minimum, int major, int minor, int patch) {
            this.name = name;
            this.minimum = minimum;
            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }

        public String toString() {
            return "Module " + name + " (" + version() + ")";
        }

        public String version() {
            return (minimum ? ">=" : "<") + major + (minor != -1 ? "." + minor : "") + (patch != -1 ? "." + patch : "");
        }
    }
}
