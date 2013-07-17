package main.origo.bootstrapwysihtml5;

import main.origo.core.CoreModule;
import main.origo.core.annotations.Module;
import main.origo.core.internal.AnnotationProcessor;

import java.util.Collections;
import java.util.List;

@Module(name="origo.bootstrap.wysihtml5", order=100, packages = "main.origo.bootstrapwysihtml5")
@Module.Version(major = 0, minor = 1, patch = 0)
public class BootstrapWysiHtml5Module {

    @Module.Dependencies
    public static List<AnnotationProcessor.Dependency> dependencies() {
        return Collections.singletonList(new AnnotationProcessor.Dependency(CoreModule.NAME, 0, 1));
    }
}
