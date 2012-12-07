package com.origocms.core.helpers;

import com.origocms.core.CachedDecorator;
import com.origocms.core.CachedThemeVariant;
import com.origocms.core.ui.RenderedNode;
import play.Logger;
import play.mvc.Result;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Helper methods for invoking the triggers/listeners/hooks.
 */
public class ReflectionHelper {

    /**
     * Uses reflection to invoke the annotated method.
     *
     * @param method     a cached annotated method
     * @param parameters possible parameters for this invocation
     * @return whatever object the listener returns, if any.
     */
    public static Object invokeMethod(Method method, Map<Class, Object> parameters) {
        Class[] parameterTypes = method.getParameterTypes();
        Object[] foundParameters;
        try {
            foundParameters = getInvocationParameters(parameterTypes, parameters);
        } catch (UnknownParameterTypeException e) {
            throw new RuntimeException("Method \'" + method + "\' has a parameter of type \'" + e.getParameterType() + "\' specified. No parameter of that type exists in the calling parameters");
        }
        try {
            return method.invoke(foundParameters);
        } catch (Exception e) {
            Logger.error("Unable to invoke method \'" + method.getDeclaringClass().getName() + "." + method.getName() + "\'");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Tries to match up the parameter types to known entities and constructs the array of parameters.
     *
     * @param parameterTypes an array of types this trigger method takes
     * @param parameters     possible parameters for this invocation
     * @return a list of parameters matching the parameter types
     * @throws UnknownParameterTypeException is thrown when there is no matching parameter for a specified parameter type in the method
     */
    @SuppressWarnings("unchecked")
    public static Object[] getInvocationParameters(Class[] parameterTypes, Map<Class, Object> parameters) throws UnknownParameterTypeException {
        List foundParameters = new ArrayList();

        for (Class parameterType : parameterTypes) {
            Object parameter = findParameter(parameters, parameterType);
            if (parameter != null) {
                foundParameters.add(parameter);
            }
        }

        return foundParameters.toArray();
    }

    //TODO: Cache this instead of looking it up every time
    private static Object findParameter(Map<Class, Object> parameters, Class parameterType) throws UnknownParameterTypeException {
        if (!Object.class.equals(parameterType) && parameterType != null) {
            // Check if the parameter map contains the class from parameterType
            Object parameter = parameters.get(parameterType);
            if (parameter != null) {
                return parameter;
            } else {
                parameter = findParameterOfSuperType(parameters, parameterType);
                if (parameter != null) {
                    return parameter;
                }
            }
        }
        throw new UnknownParameterTypeException("No parameter of type \'" + parameterType + "\' was found and it has no super class or interface", parameterType);
    }

    private static Object findParameterOfSuperType(Map<Class, Object> parameters, Class parameterType) {
        if (parameterType != null) {
            for (Class cls : parameterType.getInterfaces()) {
                Object parameter = parameters.get(cls);
                if (parameter != null) {
                    return parameter;
                }
                parameter = findParameterOfSuperType(parameters, cls.getSuperclass());
                if (parameter != null) {
                    return parameter;
                }
            }
        }
        return null;
    }

    public static String invokeDecorator(CachedDecorator decorator, Map<Class, Object> parameters) {
        return (String) invokeMethod(decorator.method, parameters);
    }

    public static Result invokeTemplate(RenderedNode renderedNode) {
        try {
            return (Result) invokeMethod(renderedNode.getTemplate().templateMethod, Collections.<Class, Object>singletonMap(RenderedNode.class, renderedNode));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
