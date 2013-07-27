package main.origo.core.security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import play.mvc.Http;
import play.mvc.Result;

public class OrigoDeadboltHandler extends AbstractDeadboltHandler {

    @Override
    public Result beforeAuthCheck(Http.Context context) {
        return null;
    }

    @Override
    public Subject getSubject(Http.Context context) {
        return AuthorizationEventGenerator.triggerProvidesSubjectInterceptor();
    }

    @Override
    public Result onAuthFailure(Http.Context context, String content) {
        return AuthorizationEventGenerator.triggerProvidesAuthFailure();
    }

    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context) {
        return super.getDynamicResourceHandler(context);
    }
}
