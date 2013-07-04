package models.origo.core;

import main.origo.core.annotations.Core;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "content")
public class Content extends Model<Content> {

    public static final String TYPE = Core.With.CONTENT_PAGE+".content";

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
        super(TYPE);
        this.identifier = UUID.randomUUID().toString();
        this.value = "";
    }

    public static Content findWithIdentifier(String identifier) {
        try {
            return (Content) JPA.em().createQuery("from "+Content.class.getName()+" where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
