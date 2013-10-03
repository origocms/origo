package models.origo.core;

import main.origo.core.State;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A release keeps one or several changes connected so they can be published at the same time.
 */
@Entity
@Table(name = "node_release")
public class Release extends Model<Release> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * A name to keep easily recognizable
     */
    @Column(unique = true)
    public String name;

    /**
     * The date this version should be available for public viewing
     *
     * @return a date
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date publish;

    /**
     * The date this version should be removed from public viewing
     *
     * @return a date
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date unPublish;

    @Enumerated
    public State state;

    public Release() {
        super("release");
    }

    public Release(State state) {
        this();
        this.state = state;
    }

    public static Release findWithName(String name) {
        try {
            return (Release) JPA.em().
                    createQuery("select r from " + Release.class.getName() + " r where r.name = :name").
                    setParameter("name", name).
                    getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<Release> findAllUnpublished() {
        try {
            //noinspection unchecked
            return (List<Release>)JPA.em().
                    createQuery("select r from " + Release.class.getName() + " r " +
                            "where r.publish = null or r.publish < :today and" +
                            "r.unPublish = null or r.unPublish >= :today)").
                    setParameter("today", new Date()).
                    getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static List<Release> findAll() {
        try {
            //noinspection unchecked
            return (List<Release>)JPA.em().
                    createQuery("select r from " + Release.class.getName() + " r ").
                    getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
