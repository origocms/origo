package main.origo.core.internal;

import com.google.common.collect.Sets;
import play.api.templates.Html;
import play.mvc.Content;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class ReflectionInvoker {

    public static <T> T execute(CachedAnnotation cachedAnnotation, Object... args) {

        try {
            Class[] parameterTypes = cachedAnnotation.method.getParameterTypes();
            Set<Object> validatedArgs = getValidatedArgs(parameterTypes, args);

            assertAllArgsFound(parameterTypes, validatedArgs, args);

            //noinspection unchecked
            return (T) cachedAnnotation.method.invoke(null, validatedArgs.toArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]: "+ e.getCause());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]: "+ e.getMessage());
        }

    }

    public static Html execute(CachedDecorator decorator, Object... args) {
        try {
            Class[] parameterTypes = decorator.method.getParameterTypes();
            Set<Object> validatedArgs = getValidatedArgs(parameterTypes, args);

            assertAllArgsFound(parameterTypes, validatedArgs, args);

            //noinspection unchecked
            return (Html) decorator.method.invoke(null, validatedArgs.toArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke method [" + decorator.method.toString() + "]: "+ e.getCause());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to invoke method [" + decorator.method.toString() + "]: "+ e.getMessage());
        }

    }

    public static Content execute(CachedThemeVariant cachedThemeVariant, Object... args) {
        try {
            Class[] parameterTypes = cachedThemeVariant.templateMethod.getParameterTypes();
            Set<Object> validatedArgs = getValidatedArgs(parameterTypes, args);

            assertAllArgsFound(parameterTypes, validatedArgs, args);

            //noinspection unchecked
            return (Content) cachedThemeVariant.templateMethod.invoke(null, validatedArgs.toArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke method [" + cachedThemeVariant.templateMethod.toString() + "]: "+ e.getCause());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to invoke method [" + cachedThemeVariant.templateMethod.toString() + "]: "+ e.getMessage());
        }
    }

    private static Set<Object> getValidatedArgs(Class[] parameterTypes, Object[] args) {
        Set<Object> validatedArgs = Sets.newLinkedHashSet();
        Set<Object> extraArgs = Sets.newHashSet();
        for (Class parameterType : parameterTypes) {
            for (int l = extraArgs.size() + validatedArgs.size(); l < args.length; l++) {
                //noinspection unchecked
                if (args[l] == null || parameterType.isAssignableFrom(args[l].getClass())) {
                    validatedArgs.add(args[l]);
                    break;
                } else {
                    extraArgs.add(args[l]);
                }
            }
        }
        return validatedArgs;
    }

    private static void assertAllArgsFound(Class[] parameterTypes, Set<Object> validatedArgs, Object[] args) {
        if (parameterTypes.length != validatedArgs.size()) {
            throw new RuntimeException("Unable to match up parameter types with arguments, " +
                    "parameter types = "+ Arrays.toString(parameterTypes)+", arguments = "+Arrays.toString(getArgTypes(args)));
        }
    }

    private static Class[] getArgTypes(Object[] args) {
        Set<Class> types = Sets.newHashSet();
        for (Object o : args) {
            types.add(o.getClass());
        }
        return types.toArray(new Class[types.size()]);
    }
}
