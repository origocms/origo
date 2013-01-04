package models.origo.core;

import com.google.common.collect.Lists;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "handlers")
public class EventHandler {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String eventName;
    public String withType;
    public String handlerClass;

    public static EventHandler findWithWithType(String withType) {
        try {
            final Query query = JPA.em().createQuery("from models.origo.core.EventHandler an where an.withType=:withType");
            query.setParameter("withType", withType);
            return (EventHandler) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<String> findEventTypes() {
        return JPA.em().createQuery("select distinct eventName from models.origo.core.EventHandler").
                getResultList();
    }

    public static List<EventHandler> findAllWithEventType(String eventName) {
        try {
            return JPA.em().createQuery("from models.origo.core.EventHandler where eventName=:eventName").
                    setParameter("eventName", eventName).
                    getResultList();
        } catch (NoResultException e) {
            return Lists.newArrayList();
        }
    }

    public static List<EventHandler> findAll() {
        try {
            return JPA.em().createQuery("from models.origo.core.EventHandler").getResultList();
        } catch (NoResultException e) {
            return Lists.newArrayList();
        }
    }

    public EventHandler save() {
        JPA.em().persist(this);
        return this;
    }
}
