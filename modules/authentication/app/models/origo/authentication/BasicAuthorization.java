package models.origo.authentication;

import models.origo.core.Model;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "basicAuthorization")
@Table(name = "authorization_basic")
public class BasicAuthorization extends Model<BasicAuthorization> {

    public static final String TYPE = "origo.authentication.BasicAuthorization";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    /**
     * Path can be either an alias, nodeId or part of a url (without host).
     */
    @Column(unique = true)
    public String path;

    @ElementCollection
    @CollectionTable(name = "authorization_basic_roles")
    public Set<String> roles = new HashSet<>();

    public BasicAuthorization() {
        super(TYPE);
    }

    public static BasicAuthorization findWithId(long id) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicAuthorization.class.getName()+" bn where bn.id=:id");
            query.setParameter("id", id);
            return (BasicAuthorization) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicAuthorization findWithPath(String path) {
        try {
            final Query query = JPA.em().createQuery("select ba from "+BasicAuthorization.class.getName()+" ba where ba.path=:path");
            query.setParameter("path", path);
            return (BasicAuthorization) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
