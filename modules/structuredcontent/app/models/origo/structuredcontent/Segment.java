package models.origo.structuredcontent;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="segments")
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String nodeId;

    @Constraints.Required
    public Integer version;

    @Constraints.Required
    public String type;

    @Constraints.Required
    public String referenceId;

    public static List<Segment> findWithNodeIdAndSpecificVersion(String nodeId, Integer version) {
        //noinspection unchecked
        return JPA.em().
                createQuery("select distinct s from models.origo.structuredcontent.Segment s where s.nodeId = :nodeId and s.version = :version").
                setParameter("nodeId", nodeId).setParameter("version", version).
                getResultList();
    }

    @Override
    public String toString() {
        return "Segment {" + "nodeId='" + nodeId + "', " + "version=" + version + ", " + '}';
    }

    public Segment save() {
        JPA.em().merge(this);
        return this;
    }
}
