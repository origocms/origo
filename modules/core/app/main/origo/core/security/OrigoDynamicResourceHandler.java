package main.origo.core.security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.ProvidesEventGenerator;
import play.Logger;
import play.mvc.Http;

public class OrigoDynamicResourceHandler implements DynamicResourceHandler {

    @Override
    public boolean isAllowed(String name, String meta, DeadboltHandler deadboltHandler, Http.Context context) {
        try {
            return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_CHECK);
        } catch (NodeLoadException | ModuleException e) {
            Logger.error("Authorization check failed, returning false", e);
            return false;
        }
    }

    @Override
    public boolean checkPermission(String permissionValue, DeadboltHandler deadboltHandler, Http.Context context) {
        Logger.warn("Permission check is not implemented in default OrigoDynamicResourceHandler");
        return false;
    }
}
