package models.origo.core;

import com.google.common.collect.Lists;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "handlers")
public class EventHandler extends Model<EventHandler> {

    public static final String TYPE = "EventHandler";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String annotation;
    public String nodeType;
    public String withType;
    public String handlerClass;

    public EventHandler() {
        super(TYPE);
    }

    public static EventHandler findWithNodeTypeAndWithType(String nodeType, String withType) {
        try {
            return (EventHandler) JPA.em().
                    createQuery("from models.origo.core.EventHandler an where an.nodeType=:nodeType and an.withType=:withType").
                    setParameter("nodeType", nodeType).
                    setParameter("withType", withType).
                    getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static EventHandler findWithAnnotationAndWithType(String annotation, String withType) {
        try {
            return (EventHandler) JPA.em().
                    createQuery("from models.origo.core.EventHandler eh where eh.annotation=:annotation and eh.withType=:withType").
                    setParameter("annotation", annotation).
                    setParameter("withType", withType).
                    getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<String> findEventTypes() {
        return JPA.em().
                createQuery("select distinct nodeType from models.origo.core.EventHandler").
                getResultList();
    }

    public static List<EventHandler> findAllWithAnnotation(String annotation) {
        try {
            return JPA.em().createQuery("select distinct eh from models.origo.core.EventHandler eh where eh.annotation=:annotation").
                    setParameter("annotation", annotation).
                    getResultList();
        } catch (NoResultException e) {
            return Lists.newArrayList();
        }
    }

    public static boolean existsWithAnnotationAndNodeType(String annotation, String nodeType) {
        try {
            return 0 < (Long) JPA.em().createQuery("select count(*) from models.origo.core.EventHandler eh where eh.annotation=:annotation and eh.nodeType=:nodeType").
                    setParameter("annotation", annotation).
                    setParameter("nodeType", nodeType).
                    getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
    }

    public static List<EventHandler> findAllWithAnnotationAndNodeType(String annotation, String nodeType) {
        try {
            return JPA.em().createQuery("select distinct eh from models.origo.core.EventHandler eh where eh.annotation=:annotation and eh.nodeType=:nodeType").
                    setParameter("annotation", annotation).
                    setParameter("nodeType", nodeType).
                    getResultList();
        } catch (NoResultException e) {
            return Lists.newArrayList();
        }
    }

    public static List<EventHandler> findAllWithEventType(String nodeType) {
        try {
            return JPA.em().createQuery("select distinct eh from models.origo.core.EventHandler eh where eh.nodeType=:nodeType").
                    setParameter("nodeType", nodeType).
                    getResultList();
        } catch (NoResultException e) {
            return Lists.newArrayList();
        }
    }

    public static List<EventHandler> findAll() {
        try {
            return JPA.em().createQuery("select distinct eh from models.origo.core.EventHandler eh").getResultList();
        } catch (NoResultException e) {
            return Lists.newArrayList();
        }
    }

}
