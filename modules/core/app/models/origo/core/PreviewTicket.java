package models.origo.core;

import main.origo.core.helpers.CoreSettingsHelper;
import org.joda.time.DateTime;
import org.joda.time.Period;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "preview_ticket")
public class PreviewTicket extends Model<Meta> {

    public static final String TYPE = "origo.preview_ticket";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String userId;

    public String ticket;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date validUntil;

    public PreviewTicket() {
        super(TYPE);
    }

    public boolean isValid() {
        Period previewPeriod = CoreSettingsHelper.getPreviewTicketPeriod();
        return new DateTime(validUntil).isAfter(DateTime.now().minus(previewPeriod));
    }

    public void updateValidUntil() {
        Period previewPeriod = CoreSettingsHelper.getPreviewTicketPeriod();
        if (validUntil != null) {
            validUntil = new DateTime(validUntil).plus(previewPeriod).toDate();
        } else {
            validUntil = DateTime.now().plus(previewPeriod).toDate();
        }
    }

    public static Content findWithTicket(String ticket) {
        try {
            return (Content) JPA.em().createQuery("from "+PreviewTicket.class.getName()+" where ticket=:ticket").
                    setParameter("ticket", ticket).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
