package com.origocms.core;

import com.origocms.core.configurations.SpringConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    private static ApplicationContext applicationContext;

    @Override
    public void onStart(play.Application application) {
        applicationContext = new ClassPathXmlApplicationContext("components.xml");
    }

    @Override
    public <A> A getControllerInstance(Class<A> aClass) throws Exception {
        if (applicationContext == null) {
            throw new IllegalStateException("application context is not initialized");
        }
        return applicationContext.getBean(aClass);
    }
}
