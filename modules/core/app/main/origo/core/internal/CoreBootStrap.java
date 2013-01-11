package main.origo.core.internal;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CoreBootStrap {

    @PostConstruct
    public void doJob() {
        AnnotationProcessor.initialize();
    }
}
