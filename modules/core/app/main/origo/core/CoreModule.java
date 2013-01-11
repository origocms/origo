package main.origo.core;

import com.google.common.collect.Lists;
import main.origo.core.annotations.*;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import main.origo.core.internal.AnnotationProcessor;
import models.origo.core.Settings;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Result;

import java.util.List;

@Module(name="core", order=0)
@Module.Version(major = 0, minor = 1, patch = 0)
public class CoreModule {

    @Module.Init
    public static void init() {
        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                Settings settings = Settings.load();
                settings.setValueIfMissing(CoreSettingsHelper.Keys.THEME_VARIANT, "default-main_and_left_columns");
                settings.setValueIfMissing(CoreSettingsHelper.Keys.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
                settings.setValueIfMissing(CoreSettingsHelper.Keys.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);
                settings.save();
            }
        });
    }

    @Module.Annotations
    public static List<AnnotationProcessor.Prototype> annotations() {
        List<AnnotationProcessor.Prototype> annotations = Lists.newArrayList();

        // Basic types
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Provides.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, OnLoad.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnInsertElement.class, null, OnInsertElement.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnRemoveElement.class, null, OnRemoveElement.Context.class));

        // Form types
        annotations.add(new AnnotationProcessor.Prototype(OnSubmit.class, null, OnSubmit.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitHandler.class, Result.class, SubmitHandler.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitState.class, Result.class, SubmitState.Context.class));

        return annotations;
    }
}
