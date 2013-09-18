package main.origo.core;

import com.google.common.collect.Lists;
import main.origo.core.annotations.*;
import main.origo.core.annotations.forms.*;
import main.origo.core.formatters.Formats;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.EncryptionHelper;
import main.origo.core.helpers.SessionHelper;
import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import main.origo.core.internal.AnnotationProcessor;
import main.origo.core.themes.DefaultTheme;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;
import models.origo.core.Content;
import models.origo.core.Settings;
import play.data.Form;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

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

        Formats.register();

        EncryptionHelper.register();
        SessionHelper.register();
    }

    @Module.Annotations
    public static List<AnnotationProcessor.Prototype> annotations() {
        List<AnnotationProcessor.Prototype> annotations = Lists.newArrayList();

        // Basic types
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Navigation.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Form.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Content.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Navigation.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Form.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Form.class, Element.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Element.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Navigation.class, NavigationElement.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Content.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnInsertElement.class, null, OnInsertElement.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnRemoveElement.class, null, OnRemoveElement.Context.class));

        // Form handling types
        annotations.add(new AnnotationProcessor.Prototype(OnSubmit.class, Boolean.class, OnSubmit.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitHandler.class, Result.class, SubmitHandler.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Processing.class, Validation.Result.class, Validation.Processing.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Node.class, Validation.Failure.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitState.class, Result.class, SubmitState.Context.class));

        // Data types
        annotations.add(new AnnotationProcessor.Prototype(OnCreate.class, null, OnCreate.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnUpdate.class, null, OnUpdate.Context.class));
        annotations.add(new AnnotationProcessor.Prototype(OnDelete.class, null, OnDelete.Context.class));

        return annotations;
    }
}
