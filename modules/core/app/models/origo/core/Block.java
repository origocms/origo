package models.origo.core;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;

@Entity
@Table(name="segments")
public class Block extends Model<Block> {

    public static final String TYPE = "block";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String identifier;

    @Constraints.Required
    public String type;

    @Constraints.Required
    public String referenceId;

    public Block() {
        super(TYPE);
    }

    public static Block findWithIdentifier(String identifier) {
        //noinspection unchecked
        return (Block) JPA.em().
                createQuery("select distinct s from "+Block.class.getName()+" s where s.identifier = :identifier").
                setParameter("identifier", identifier).
                getSingleResult();
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", type='" + type + '\'' +
                ", referenceId='" + referenceId + '\'' +
                '}';
    }

}
