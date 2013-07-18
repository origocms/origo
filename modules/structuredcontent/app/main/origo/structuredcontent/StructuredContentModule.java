package main.origo.structuredcontent;

import main.origo.core.CoreModule;
import main.origo.core.annotations.Module;
import main.origo.core.internal.AnnotationProcessor;

import java.util.Collections;
import java.util.List;

@Module(name= StructuredContentModule.NAME, order=10, packages = "main.origo.structuredcontent")
@Module.Version(major = 0, minor = 1, patch = 0)
public class StructuredContentModule {

    public static final String NAME = "origo.structuredcontent";

    @Module.Dependencies
    public static List<AnnotationProcessor.Dependency> dependencies() {
        return Collections.singletonList(new AnnotationProcessor.Dependency(CoreModule.NAME, 0, 1));
    }

}
