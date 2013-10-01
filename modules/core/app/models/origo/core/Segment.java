package models.origo.core;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;

@Entity
@Table(name="segments")
public class Segment extends Model<Segment> {

    public static final String TYPE = "segment";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String identifier;

    @Constraints.Required
    public String type;

    @Constraints.Required
    public String referenceId;

    public Segment() {
        super(TYPE);
    }

    public static Segment findWithIdentifier(String identifier) {
        //noinspection unchecked
        return (Segment) JPA.em().
                createQuery("select distinct s from "+Segment.class.getName()+" s where s.identifier = :identifier").
                setParameter("identifier", identifier).
                getSingleResult();
    }

    @Override
    public String toString() {
        return "Segment{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", type='" + type + '\'' +
                ", referenceId='" + referenceId + '\'' +
                '}';
    }

}
