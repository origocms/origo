package models.origo.core;

import com.google.common.collect.Maps;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "handlers")
public class EventHandler {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String eventName;
    public String withType;
    public String handlerClass;

    public static EventHandler findWithType(String withType) {
        try {
            final Query query = JPA.em().createQuery("from models.origo.core.EventHandler an where an.withType=:withType");
            query.setParameter("withType", withType);
            return (EventHandler) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static Map<String, EventHandler> findAll() {
        try {
            List<EventHandler> allEventHandlers = JPA.em().createQuery("from models.origo.core.EventHandler").getResultList();
            Map<String, EventHandler> results = Maps.newHashMap();
            for (EventHandler eventHandler : allEventHandlers) {
                results.put(eventHandler.withType, eventHandler);
            }
            return results;
        } catch (NoResultException e) {
            return null;
        }
    }

    public EventHandler save() {
        JPA.em().persist(this);
        return this;
    }
}
