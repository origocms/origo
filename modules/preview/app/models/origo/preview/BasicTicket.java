package models.origo.preview;

import main.origo.core.User;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.preview.Ticket;
import models.origo.core.Model;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import tyrex.services.UUID;

import javax.persistence.*;

@Entity
@Table(name = "ticket")
public class BasicTicket extends Model<BasicTicket> implements Ticket {

    public static final String TYPE = "origo.preview.ticket";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String userId;

    @Constraints.Required
    public String token;

    @Constraints.Required
    private long validUntil;

    @Constraints.Required
    public long preview;

    public BasicTicket() {
        super(TYPE);
    }

    public boolean isValid() {
        Period previewPeriod = CoreSettingsHelper.getPreviewTicketPeriod();
        return new DateTime(validUntil).isAfter(DateTime.now(DateTimeZone.UTC).minus(previewPeriod));
    }

    public static DateTime updateValidUntil() {
        Period previewPeriod = CoreSettingsHelper.getPreviewTicketPeriod();
        return DateTime.now(DateTimeZone.UTC).plus(previewPeriod);
    }

    public static BasicTicket findWithToken(String token) {
        try {
            return (BasicTicket) JPA.em().createQuery("from "+BasicTicket.class.getName()+" pt where pt.token=:token").
                    setParameter("token", token).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicTicket getIfValid(User user, String token) {
        assert(user != null);
        BasicTicket basicTicket = findWithToken(token);
        if (basicTicket != null) {
            if (basicTicket.userId.equals(user.getIdentifier()) && basicTicket.isValid()) {
                basicTicket.validUntil = updateValidUntil().getMillis();
                return basicTicket;
            }
            basicTicket.delete();
        }
        return null;
    }

    public static BasicTicket createInstance(User user, DateTime preview) {
        assert(user != null);
        BasicTicket basicTicket = new BasicTicket();
        basicTicket.validUntil = updateValidUntil().getMillis();
        basicTicket.token = UUID.create();
        basicTicket.userId = user.getIdentifier();
        basicTicket.preview = preview.getMillis();
        return basicTicket.create();
    }

    public Long id() {
        return id;
    }

    public String userId() {
        return userId;
    }

    public String token() {
        return token;
    }

    public DateTime validUntil() {
        if (validUntil > 0) {
            return new DateTime().withMillis(validUntil);
        }
        return null;
    }

    public DateTime preview() {
        if (preview > 0) {
            return new DateTime().withMillis(preview);
        }
        return null;
    }
}
