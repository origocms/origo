package models.origo.core;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "content", uniqueConstraints = @UniqueConstraint(name = "contentVersion", columnNames = {"identifier", "version"}))
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public Integer version;

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
        @SuppressWarnings("unchecked")
        List<Content> contents = JPA.em().createQuery("from models.origo.core.Content where identifier=:identifier order by version desc").
                setParameter("identifier", identifier).getResultList();
        if (contents.isEmpty()) {
            return null;
        }
        return contents.iterator().next();

    }

    public Content save() {
        JPA.em().merge(this);
        return this;
    }
}
