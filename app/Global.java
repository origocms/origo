import main.origo.core.internal.AnnotationProcessor;
import play.Application;
import play.Configuration;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.F;

import java.io.File;

public class Global extends GlobalSettings {

    @Override
    public void beforeStart(Application application) {
        super.beforeStart(application);
    }

    @Override
    public Configuration onLoadConfig(Configuration configuration, File file, ClassLoader classLoader) {
        return super.onLoadConfig(configuration, file, classLoader);
    }

    @Override
    public void onStart(play.Application application) {
        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() {
                AnnotationProcessor.initialize();
            }
        });
    }
}
