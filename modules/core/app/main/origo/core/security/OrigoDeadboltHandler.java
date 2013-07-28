package main.origo.core.security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import play.mvc.Http;
import play.mvc.Result;

public class OrigoDeadboltHandler extends AbstractDeadboltHandler {

    @Override
    public Result beforeAuthCheck(Http.Context context) {
        try {
            return AuthorizationEventGenerator.triggerAuthenticationCheck();
        } catch (ModuleException | NodeLoadException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Subject getSubject(Http.Context context) {
        try {
            return AuthorizationEventGenerator.triggerProvidesSubjectInterceptor();
        } catch (ModuleException | NodeLoadException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result onAuthFailure(Http.Context context, String content) {
        try {
            return AuthorizationEventGenerator.triggerProvidesAuthorizationFailure();
        } catch (ModuleException | NodeLoadException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context) {
        return super.getDynamicResourceHandler(context);
    }
}
