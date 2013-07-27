package models.origo.authentication;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import com.google.common.collect.Lists;
import main.origo.core.User;
import models.origo.core.Model;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.*;
import org.jasypt.hibernate4.type.EncryptedStringType;
import play.db.jpa.JPA;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@TypeDef(
        name="encryptedString",
        typeClass=EncryptedStringType.class,
        parameters={@org.hibernate.annotations.Parameter(name="encryptorRegisteredName",
                value="strongHibernateStringEncryptor")}
)
@Entity(name = "basicUser")
@Table(name = "user")
public class BasicUser extends Model<BasicUser> implements User {

    public static final String TYPE = "origo.authentication.BasicUser";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;

    public String email;

    @Type(type="encryptedString")
    @JsonIgnore
    public String password;

    @OneToMany
    public Set<BasicRole> roles;

    public BasicUser() {
        super(TYPE);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public List<? extends Role> getRoles() {
        return Collections.unmodifiableList(Lists.newArrayList(roles));
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    public static BasicUser findWithId(long id) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicUser.class.getName()+" bn where bn.id=:id");
            query.setParameter("id", id);
            return (BasicUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicUser findWithEmail(String email) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicUser.class.getName()+" bn where bn.email=:email");
            query.setParameter("email", email);
            return (BasicUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}
