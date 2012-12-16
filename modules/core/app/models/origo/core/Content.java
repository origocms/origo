package models.origo.core;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    @Column(name = "content")
    @Lob
    public String value;

    public Content() {
        this.identifier = UUID.randomUUID().toString();
    }

    public static Content findWithIdentifier(String identifier) {
        try {
            return (Content) JPA.em().createQuery("from models.origo.core.Content where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Content save() {
        JPA.em().merge(this);
        return this;
    }
}
