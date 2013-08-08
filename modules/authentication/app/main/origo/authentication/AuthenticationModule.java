package main.origo.authentication;

import main.origo.core.CoreModule;
import main.origo.core.annotations.Module;
import main.origo.core.internal.AnnotationProcessor;

import java.util.Collections;
import java.util.List;

@Module(name = AuthenticationModule.NAME, order=10, packages = "main.origo.authentication")
@Module.Version(major = 0, minor = 1, patch = 0)
public class AuthenticationModule {

    public static final String NAME = "origo.authentication";

    @Module.Dependencies
    public static List<AnnotationProcessor.Dependency> dependencies() {
        return Collections.singletonList(new AnnotationProcessor.Dependency(CoreModule.NAME, 0, 1));
    }

}
