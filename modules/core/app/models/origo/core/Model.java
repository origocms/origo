package models.origo.core;

import main.origo.core.event.forms.OnCreateEventGenerator;
import main.origo.core.event.forms.OnDeleteEventGenerator;
import main.origo.core.event.forms.OnUpdateEventGenerator;
import play.db.jpa.JPA;

public class Model<T> {

    protected static ThreadLocal<Boolean> PROCESSING = new ThreadLocal<>();

    protected String TYPE;

    public Model(String type) {
        PROCESSING.set(false);
        this.TYPE = type;
    }

    public final T create() {
        if (!PROCESSING.get()) {
            PROCESSING.set(true);
            try {
                doCreate((T)this);
            } finally {
                PROCESSING.set(false);
            }
        }
        return (T)this;
    }

    protected void doCreate(T t) {
        OnCreateEventGenerator.triggerBeforeInterceptors(TYPE, this);
        JPA.em().persist(t);
        OnCreateEventGenerator.triggerAfterInterceptors(TYPE, this);
    }

    public final T update() {
        if (!PROCESSING.get()) {
            PROCESSING.set(true);
            try {
                doUpdate((T)this);
            } finally {
                PROCESSING.set(false);
            }
        }
        return (T)this;
    }

    protected void doUpdate(T t) {
        OnUpdateEventGenerator.triggerBeforeInterceptors(TYPE, this);
        JPA.em().merge(t);
        OnUpdateEventGenerator.triggerAfterInterceptors(TYPE, this);
    }

    public final void delete() {
        if (!PROCESSING.get()) {
            PROCESSING.set(true);
            try {
                doDelete((T)this);
            } finally {
                PROCESSING.set(false);
            }
        }
    }

    protected void doDelete(T t) {
        OnDeleteEventGenerator.triggerBeforeInterceptors(TYPE, this);
        JPA.em().remove(t);
        OnDeleteEventGenerator.triggerAfterInterceptors(TYPE, this);
    }

}
