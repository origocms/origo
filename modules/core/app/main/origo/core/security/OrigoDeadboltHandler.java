package main.origo.core.security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.SimpleResult;

public class OrigoDeadboltHandler extends AbstractDeadboltHandler {

    @Override
    public F.Promise<SimpleResult> beforeAuthCheck(Http.Context context) {
        try {
            Result result = SecurityEventGenerator.triggerAuthenticationCheck(context.request().path());
            return F.Promise.pure((SimpleResult)result);
        } catch (NodeLoadException | ModuleException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Subject getSubject(Http.Context context) {
        try {
            return SecurityEventGenerator.triggerProvidesSubjectInterceptor();
        } catch (NodeLoadException | ModuleException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public F.Promise<SimpleResult> onAuthFailure(Http.Context context, String content) {
        try {
            F.Promise<SimpleResult> simpleResultPromise = SecurityEventGenerator.triggerProvidesAuthorizationFailure();
            return simpleResultPromise;
        } catch (NodeLoadException | ModuleException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context) {
        return new OrigoDynamicResourceHandler();
    }
}
