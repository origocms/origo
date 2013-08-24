package main.origo.preview;

import main.origo.core.CoreModule;
import main.origo.core.annotations.Module;
import main.origo.core.internal.AnnotationProcessor;

import java.util.Collections;
import java.util.List;

@Module(name=PreviewModule.NAME, order=100, packages = "main.origo.preview")
@Module.Version(major = 0, minor = 1, patch = 0)
public class PreviewModule {

    public static final String NAME = "origo.preview";

    @Module.Dependencies
    public static List<AnnotationProcessor.Dependency> dependencies() {
        return Collections.singletonList(new AnnotationProcessor.Dependency(CoreModule.NAME, 0, 1));
    }

}
