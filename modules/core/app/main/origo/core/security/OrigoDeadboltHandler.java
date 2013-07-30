package main.origo.core.security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.event.NodeContext;
import play.mvc.Http;
import play.mvc.Result;

public class OrigoDeadboltHandler extends AbstractDeadboltHandler {

    @Override
    public Result beforeAuthCheck(Http.Context context) {
        NodeContext.set();
        try {
            return AuthEventGenerator.triggerAuthenticationCheck(context.request().path());
        } catch (NodeLoadException e) {
            throw new RuntimeException(e);
        } catch (ModuleException e) {
            throw new RuntimeException(e);
        } finally {
            NodeContext.clear();
        }
    }

    @Override
    public Subject getSubject(Http.Context context) {
        NodeContext.set();
        try {
            return AuthEventGenerator.triggerProvidesSubjectInterceptor();
        } catch (NodeLoadException e) {
            throw new RuntimeException(e);
        } catch (ModuleException e) {
            throw new RuntimeException(e);
        } finally {
            NodeContext.clear();
        }
    }

    @Override
    public Result onAuthFailure(Http.Context context, String content) {
        NodeContext.set();
        try {
            return AuthEventGenerator.triggerProvidesAuthorizationFailure();
        } catch (NodeLoadException e) {
            throw new RuntimeException(e);
        } catch (ModuleException e) {
            throw new RuntimeException(e);
        } finally {
            NodeContext.clear();
        }
    }

    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context) {
        return new OrigoDynamicResourceHandler();
    }
}
