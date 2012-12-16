package models.origo.structuredcontent;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
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
        return JPA.em().
                createQuery("select distinct s from models.origo.structuredcontent.Segment s where s.nodeId = :nodeId and s.version = :version").
                setParameter("nodeId", nodeId).setParameter("version", version).
                getResultList();
    }

    @Override
    public String toString() {
        return new StringBuilder().
                append("Segment {").
                append("nodeId='").append(nodeId).append("', ").
                append("version=").append(version).append(", ").
                append('}').toString();
    }
}
