package origo.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CronListener {
    /**
     * @return
     */
    int secondsBetweenExecutions() default 86400;

    /**
     * @return
     */
    int secondsUntilFirstExecution() default 0;


}
