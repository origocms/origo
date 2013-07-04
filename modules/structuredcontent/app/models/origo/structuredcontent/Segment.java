package models.origo.structuredcontent;

import main.origo.core.annotations.Core;
import models.origo.core.Model;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="segments")
public class Segment extends Model<Segment> {

    public static final String TYPE = Core.With.CONTENT_PAGE + ".segment";

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

    public Segment() {
        super(TYPE);
    }

    public static List<Segment> findWithNodeIdAndSpecificVersion(String nodeId, Integer version) {
        //noinspection unchecked
        return JPA.em().
                createQuery("select distinct s from "+Segment.class.getName()+" s where s.nodeId = :nodeId and s.version = :version").
                setParameter("nodeId", nodeId).setParameter("version", version).
                getResultList();
    }

    @Override
    public String toString() {
        return "Segment {" + "nodeId='" + nodeId + "', " + "version=" + version + ", " + '}';
    }

}
