package main.origo.admin;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminSettingsHelper;
import main.origo.admin.helpers.NavigationHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.CoreModule;
import main.origo.core.annotations.Module;
import main.origo.core.internal.AnnotationProcessor;
import models.origo.core.Settings;

import java.util.Collections;
import java.util.List;

@Module(name = AdminModule.NAME, order = 100, packages = "main.origo.admin")
@Module.Version(major = 0, minor = 1, patch = 0)
public class AdminModule {

    public static final String NAME = "origo.admin";

    @Module.Init
    public static void init() {
        Settings settings = Settings.load();
        settings.setValueIfMissing(AdminSettingsHelper.Keys.HOME_DASHBOARD_TYPE, Admin.With.FRONT_PAGE);
        settings.setValueIfMissing(AdminSettingsHelper.Keys.THEME_VARIANT, AdminTheme.DEFAULT_VARIANT_NAME);
        settings.save();
        NavigationHelper.loadNavigationStructure();
    }

    @Module.Annotations
    public static List<AnnotationProcessor.Prototype> annotations() {
        return Collections.singletonList(new AnnotationProcessor.Prototype(Admin.Navigation.class, String.class));
    }

    @Module.Dependencies
    public static List<AnnotationProcessor.Dependency> dependencies() {
        return Collections.singletonList(new AnnotationProcessor.Dependency(CoreModule.NAME, 0, 1));
    }
}
