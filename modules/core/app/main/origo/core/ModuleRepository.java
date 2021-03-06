package main.origo.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.annotations.Module;
import main.origo.core.internal.CachedModule;
import models.origo.core.Settings;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import play.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ModuleRepository {

    public static Map<String, CachedModule> modules = Maps.newHashMap();

    public static CachedModule add(Module module, Class c) {
        if (modules.containsKey(module.name())) {
            throw new InitializationException("Duplicate Module name ["+module.name()+"]");
        }
        if (module.order() <= 0 && !module.name().equals(CoreModule.NAME)) {
            throw new InitializationException("Order must be higher than 0");
        }

        //noinspection unchecked
        Module.Version moduleVersionAnnotation = (Module.Version) c.getAnnotation(Module.Version.class);

        Method initMethod = getSingleMethod(c, Module.Init.class);
        Method dependenciesMethod = getSingleMethod(c, Module.Dependencies.class);
        Method annotationsMethod = getSingleMethod(c, Module.Annotations.class);

        CachedModule value = new CachedModule(module.name(), c, module, moduleVersionAnnotation,
                initMethod, annotationsMethod, dependenciesMethod);
        modules.put(module.name(), value);
        return value;
    }

    private static Method getSingleMethod(Class c, Class<? extends Annotation> annotation) {
        Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(annotation));
        if (methods.size() == 1) {
            return methods.iterator().next();
        }
        return null;
    }

    public static CachedModule getModule(String name) {
        return modules.get(name);
    }

    public static void invalidate() {
        modules.clear();
    }

    public static List<CachedModule> getAll() {
        ArrayList<CachedModule> moduleList = Lists.newArrayList(modules.values());
        Collections.sort(moduleList);
        return moduleList;
    }

    public static boolean isEnabled(CachedModule module) {
        Settings settings = Settings.load();
        return settings.getValueAsBoolean("module." + module.name + ".enabled", true);
    }

    public static void enable(CachedModule module) {
        Settings settings = Settings.load();
        settings.setValue("module."+ module.name+ ".enabled", Boolean.toString(true));
        settings.save();
    }

    public static void enable(String moduleName) throws ModuleException {
        if (!modules.containsKey(moduleName)) {
            Logger.error("Unknown module '"+moduleName+"'");
            throw new ModuleException(moduleName, ModuleException.Cause.NOT_INSTALLED, "Unknown module '"+moduleName+"'");
        }

        enable(modules.get(moduleName));
    }
}
