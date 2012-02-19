package origo.core;

import origo.core.annotations.Decorates;
import origo.core.annotations.Listener;
import origo.core.annotations.OnLoad;
import origo.core.annotations.Provides;
import origo.core.annotations.forms.*;
import play.Application;
import play.Logger;
import play.Play;
import play.Plugin;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Scans classes that are compiled for methods annotated with trigger/add-ons/listener annotations and caches them.
 * At startup it scans all classes in the classpath.
 */
public class CoreAnnotationsPlugin extends Plugin {

    private final Application application;

    public CoreAnnotationsPlugin(Application application) {
        this.application = application;
    }
    
    @Override
    public void onStart() {

        Logger.error("Bla bla");
        
        Listeners.invalidate();

        Set<String> annotatedTypes = application.getTypesAnnotatedWith("", Listener.class);
        
        //List<Class> modifiedJavaClasses = AnnotationPluginHelper.getJavaClasses(modifiedClasses);
        findAndAddListenerAnnotation(annotatedTypes, Provides.class);
        /*
        findAndAddListenerAnnotation(Decorates.class);
        findAndAddListenerAnnotation(OnLoad.class);
        findAndAddListenerAnnotation(OnSubmit.class);
        findAndAddListenerAnnotation(SubmitHandler.class);
        findAndAddListenerAnnotation(SubmitState.class);
        findAndAddListenerAnnotation(OnLoadForm.class);
        findAndAddListenerAnnotation(ProvidesForm.class);
        */
    }

    /**
     * Finds all methods annotated with the specified annotationClass and adds it to the cache.
     *
     * @param annotationClass a method annotation that provides a hook/trigger/listener.
     */
    // TODO: should take a method to be triggered for additional checks of validity (checking return type, checking parameter types, etc).
    private void findAndAddListenerAnnotation(Set<String> annotatedTypes, Class<? extends Annotation> annotationClass) {
        for (String className : annotatedTypes) {
            Listeners.addListener(annotationClass, className);
        }
    }

}
