import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.F;

public class Global extends GlobalSettings {

    private static ApplicationContext applicationContext;

    @Override
    public void onStart(play.Application application) {
        applicationContext = new ClassPathXmlApplicationContext("components.xml");
        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                new InitialTestData().create();
            }
        });
    }

    @Override
    public <A> A getControllerInstance(Class<A> aClass) throws Exception {
        if (applicationContext == null) {
            throw new IllegalStateException("application context is not initialized");
        }
        try {
            return applicationContext.getBean(aClass);
        } catch (NoSuchBeanDefinitionException e) {
            return super.getControllerInstance(aClass);
        }
    }

}
