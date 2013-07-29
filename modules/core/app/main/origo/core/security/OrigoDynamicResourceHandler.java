package main.origo.core.security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import com.google.common.collect.Maps;
import controllers.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.NodeContext;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.utils.ExceptionUtil;
import play.Logger;
import play.mvc.Http;

import java.util.Map;

public class OrigoDynamicResourceHandler implements DynamicResourceHandler {

    public static final String AUTH_HANDLER = "AUTHORIZATION_HANDLER";
    public static final String AUTH_META = "AUTHORIZATION_META";

    @Override
    public boolean isAllowed(String name, String meta, DeadboltHandler deadboltHandler, Http.Context context) {
        NodeContext.set();
        try {
            Map<String, Object> args = Maps.newHashMap();
            args.put(AUTH_HANDLER, name);
            args.put(AUTH_META, meta);
            Boolean isAllowed = ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_CHECK, args);
            return !(isAllowed == null || !isAllowed);
        } catch (NodeLoadException | ModuleException | RuntimeException e) {
            ExceptionUtil.assertExceptionHandling(e);
            Logger.error("Authorization check failed, returning false", e);
            return false;
        } finally {
            NodeContext.clear();
        }
    }

    @Override
    public boolean checkPermission(String permissionValue, DeadboltHandler deadboltHandler, Http.Context context) {
        Logger.warn("Permission check is not implemented in default OrigoDynamicResourceHandler");
        return false;
    }
}
