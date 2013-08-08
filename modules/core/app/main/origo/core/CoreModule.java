package main.origo.core;

import com.google.common.collect.Lists;
import main.origo.core.annotations.*;
import main.origo.core.annotations.forms.*;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.EncryptionHelper;
import main.origo.core.helpers.SessionHelper;
import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import main.origo.core.internal.AnnotationProcessor;
import main.origo.core.themes.DefaultTheme;
import models.origo.core.Settings;
import play.mvc.Result;

import java.util.List;

@Module(name=CoreModule.NAME, order=0, packages = "main.origo.core")
@Module.Version(major = 0, minor = 1, patch = 0)
public class CoreModule {

    public static final String NAME = "origo.core";

    @Module.Init
    public static void init() {
        Settings settings = Settings.load();
        settings.setValueIfMissing(CoreSettingsHelper.Keys.THEME, DefaultTheme.ID);
        settings.setValueIfMissing(CoreSettingsHelper.Keys.THEME_VARIANT, "default-main_only");
        settings.setValueIfMissing(CoreSettingsHelper.Keys.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
        settings.setValueIfMissing(CoreSettingsHelper.Keys.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);
        settings.save();

        EncryptionHelper.register();
        SessionHelper.register();
    }

    @Module.Annotations
    public static List<AnnotationProcessor.Prototype> annotations() {
        List<AnnotationProcessor.Prototype> annotations = Lists.newArrayList();

        // Basic types
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Provides.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, OnLoad.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnInsertElement.class, null, OnInsertElement.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnRemoveElement.class, null, OnRemoveElement.Context.class));

        // Form handling types
        annotations.add(new AnnotationProcessor.Prototype(OnSubmit.class, Boolean.class, OnSubmit.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitHandler.class, Result.class, SubmitHandler.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitState.class, Result.class, SubmitState.Context.class));

        // Data types
        annotations.add(new AnnotationProcessor.Prototype(OnCreate.class, null, OnCreate.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnUpdate.class, null, OnUpdate.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnDelete.class, null, OnDelete.Context.class));

        return annotations;
    }
}
