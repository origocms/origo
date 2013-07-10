package main.origo.structuredcontent;

import main.origo.core.annotations.Module;
import main.origo.core.internal.AnnotationProcessor;

import java.util.Collections;
import java.util.List;

@Module(name="origo.structuredcontent", order=10, packages = "main.origo.structuredcontent")
@Module.Version(major = 0, minor = 1, patch = 0)
public class StructuredContentModule {

    @Module.Dependencies
    public static List<AnnotationProcessor.Dependency> dependencies() {
        return Collections.singletonList(new AnnotationProcessor.Dependency("origo.core", 0, 1));
    }

}
