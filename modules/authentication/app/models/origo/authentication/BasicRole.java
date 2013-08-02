package models.origo.authentication;

import be.objectify.deadbolt.core.models.Role;
import models.origo.core.Model;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity(name = "basicRole")
@Table(name = "role")
public class BasicRole extends Model<BasicRole> implements Role {

    public static final String TYPE = "origo.authentication.BasicRole";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;

    public BasicRole() {
        super(TYPE);
    }

    @Override
    public String getName() {
        return name;
    }

    public static BasicRole findWithId(long id) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicRole.class.getName()+" bn where bn.id=:id", BasicRole.class);
            query.setParameter("id", id);
            return (BasicRole) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<BasicRole> list() {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicRole.class.getName()+" bn", BasicRole.class);
            //noinspection unchecked
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

}
