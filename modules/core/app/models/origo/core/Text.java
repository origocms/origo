package models.origo.core;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "text")
public class Text extends Model<Text> {

    public static final String TYPE = "text";

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

    public Text() {
        super(TYPE);
        this.identifier = UUID.randomUUID().toString();
        this.value = "";
    }

    public Text(String value) {
        this();
        this.value = value;
    }

    public static Text findWithIdentifier(String identifier) {
        try {
            return (Text) JPA.em().createQuery("from "+Text.class.getName()+" where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Text detach() {
        JPA.em().detach(this);
        return this;
    }

}
