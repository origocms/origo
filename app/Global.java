import main.origo.core.internal.AnnotationProcessor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    @Override
    public void onStart(play.Application application) {
        AnnotationProcessor.initialize();
    }

}
