package models.origo.preview;

import main.origo.core.User;
import main.origo.core.helpers.CoreSettingsHelper;
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
public class Ticket extends Model<Ticket> {

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

    public Ticket() {
        super(TYPE);
    }

    public boolean isValid() {
        Period previewPeriod = CoreSettingsHelper.getPreviewTicketPeriod();
        return new DateTime(validUntil).isAfter(DateTime.now(DateTimeZone.UTC).minus(previewPeriod));
    }

    public static DateTime getUpdatedValidUntil() {
        Period previewPeriod = CoreSettingsHelper.getPreviewTicketPeriod();
        return DateTime.now(DateTimeZone.UTC).plus(previewPeriod);
    }

    public static Ticket findWithToken(String token) {
        try {
            return (Ticket) JPA.em().createQuery("from "+Ticket.class.getName()+" pt where pt.token=:token").
                    setParameter("token", token).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static Ticket getIfValid(User user, String token) {
        assert(user != null);
        Ticket ticket = findWithToken(token);
        if (ticket != null) {
            if (ticket.userId.equals(user.getIdentifier()) && ticket.isValid()) {
                ticket.validUntil = getUpdatedValidUntil().getMillis();
                return ticket;
            }
            ticket.delete();
        }
        return null;
    }

    public static Ticket createInstance(User user, DateTime preview) {
        assert(user != null);
        Ticket ticket = new Ticket();
        ticket.validUntil = getUpdatedValidUntil().getMillis();
        ticket.token = UUID.create();
        ticket.userId = user.getIdentifier();
        ticket.preview = preview.getMillis();
        return ticket.create();
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public DateTime getValidUntil() {
        if (validUntil > 0) {
            return new DateTime().withMillis(validUntil);
        }
        return null;
    }

    public DateTime getPreview() {
        if (preview > 0) {
            return new DateTime().withMillis(preview);
        }
        return null;
    }
}
