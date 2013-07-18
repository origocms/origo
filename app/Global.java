import main.origo.core.internal.AnnotationProcessor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.F;

public class Global extends GlobalSettings {

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
