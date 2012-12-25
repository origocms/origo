package main.origo.core.internal;

import main.origo.core.helpers.SettingsCoreHelper;
import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import main.origo.core.interceptors.forms.WysiHTML5EditorProvider;
import models.origo.core.Settings;
import org.springframework.stereotype.Component;
import play.db.jpa.JPA;
import play.libs.F;

import javax.annotation.PostConstruct;

@Component
public class CoreBootStrap {

    @PostConstruct
    public void doJob() {
        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                Settings settings = Settings.load();
                SettingsCoreHelper.setValueIfMissing(settings, SettingsCoreHelper.Keys.THEME_VARIANT, "default-main_and_left_columns");
                SettingsCoreHelper.setValueIfMissing(settings, SettingsCoreHelper.Keys.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
                SettingsCoreHelper.setValueIfMissing(settings, SettingsCoreHelper.Keys.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);
                SettingsCoreHelper.setValueIfMissing(settings, SettingsCoreHelper.Keys.RICHTEXT_EDITOR_TYPE, WysiHTML5EditorProvider.EDITOR_TYPE);
                settings.save();
            }
        });
    }
}
