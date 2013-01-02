package main.origo.core.internal;

import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
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
                settings.setValueIfMissing(CoreSettingsHelper.Keys.THEME_VARIANT, "default-main_and_left_columns");
                settings.setValueIfMissing(CoreSettingsHelper.Keys.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
                settings.setValueIfMissing(CoreSettingsHelper.Keys.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);
                settings.save();
            }
        });
    }
}
