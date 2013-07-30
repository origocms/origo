package main.origo.core.security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import com.google.common.collect.Maps;
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

    @Override
    public boolean isAllowed(String name, String meta, DeadboltHandler deadboltHandler, Http.Context context) {
        NodeContext.set();
        try {
            NodeContext.current().attributes.put(Security.Params.AUTH_PATH, Http.Context.current().request().path());
            Map<String, Object> args = Maps.newHashMap();
            args.put(Security.Params.AUTH_HANDLER, name);
            args.put(Security.Params.AUTH_META, meta);
            Boolean isAllowed = ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_CHECK, args);
            return !(isAllowed == null || !isAllowed);
        } catch (NodeLoadException e) {
            ExceptionUtil.assertExceptionHandling(e);
            Logger.error("Authorization check failed, returning false", e);
            return false;
        } catch (ModuleException e) {
            ExceptionUtil.assertExceptionHandling(e);
            Logger.error("Authorization check failed, returning false", e);
            return false;
        } catch (RuntimeException e) {
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
