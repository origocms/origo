package models.origo.structuredcontent;

import main.origo.core.event.forms.OnCreateEventGenerator;
import main.origo.core.event.forms.OnDeleteEventGenerator;
import main.origo.core.event.forms.OnUpdateEventGenerator;
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

    public Segment create() {
        OnCreateEventGenerator.triggerBeforeInterceptors("segment", this);
        JPA.em().persist(this);
        OnCreateEventGenerator.triggerAfterInterceptors("segment", this);
        return this;
    }

    public Segment update() {
        OnUpdateEventGenerator.triggerBeforeInterceptors("segment", this);
        JPA.em().merge(this);
        OnUpdateEventGenerator.triggerAfterInterceptors("segment", this);
        return this;
    }

    public void delete() {
        OnDeleteEventGenerator.triggerBeforeInterceptors("segment", this);
        JPA.em().remove(this);
        OnDeleteEventGenerator.triggerAfterInterceptors("segment", this);
    }
}
