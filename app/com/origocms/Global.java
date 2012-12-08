package com.origocms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

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
        return applicationContext.getBean(aClass);
    }

}
