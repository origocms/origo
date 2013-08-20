package models.origo.core;

import main.origo.core.User;
import main.origo.core.formatters.Formats;
import main.origo.core.helpers.CoreSettingsHelper;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import play.db.jpa.JPA;
import tyrex.services.UUID;

import javax.persistence.*;

@Entity
@Table(name = "preview_ticket")
public class PreviewTicket extends Model<PreviewTicket> {

    public static final String TYPE = "origo.preview_ticket";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String userId;

    public String token;

    @Formats.DateTimePattern(key="date.format")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    public LocalDateTime validUntil;

    @Formats.DateTimePattern(key="date.format")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    public LocalDateTime preview;

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
            validUntil = validUntil.plus(previewPeriod);
        } else {
            validUntil = LocalDateTime.now().plus(previewPeriod);
        }
    }

    public static PreviewTicket findWithToken(String token) {
        try {
            return (PreviewTicket) JPA.em().createQuery("from "+PreviewTicket.class.getName()+" pt where pt.token=:token").
                    setParameter("token", token).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static PreviewTicket getIfValid(User user, String token) {
        PreviewTicket ticket = findWithToken(token);
        if (ticket != null) {
            if (ticket.userId.equals(user.getIdentifier()) && ticket.isValid()) {
                ticket.updateValidUntil();
                return ticket;
            }
            ticket.delete();
        }
        return null;
    }

    public static PreviewTicket createInstance(User user, LocalDateTime preview) {
        PreviewTicket ticket = new PreviewTicket();
        ticket.updateValidUntil();
        ticket.token = UUID.create();
        ticket.userId = user.getIdentifier();
        ticket.preview = preview;
        return ticket.create();
    }
}
