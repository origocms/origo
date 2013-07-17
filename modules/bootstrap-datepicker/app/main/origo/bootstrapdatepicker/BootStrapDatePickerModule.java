package main.origo.bootstrapdatepicker;

import main.origo.core.CoreModule;
import main.origo.core.annotations.Module;
import main.origo.core.internal.AnnotationProcessor;

import java.util.Collections;
import java.util.List;

@Module(name= BootstrapDatePickerModule.NAME, order=100, packages = "main.origo.bootstrapdatepicker")
@Module.Version(major = 0, minor = 1, patch = 0)
public class BootstrapDatePickerModule {

    public static final String NAME = "origo.bootstrap.datepicker";

    @Module.Dependencies
    public static List<AnnotationProcessor.Dependency> dependencies() {
        return Collections.singletonList(new AnnotationProcessor.Dependency(CoreModule.NAME, 0, 1));
    }
}
