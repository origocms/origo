package main.origo.preview.helpers;

import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.User;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.SessionHelper;
import main.origo.core.security.Security;
import main.origo.core.security.SecurityEventGenerator;
import models.origo.preview.Ticket;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class PreviewTicketHelper {

    private static final String SESSION_PREVIEW_TICKET_KEY = "origo.session.preview_ticket";

    public static boolean hasTicket() {
        return SessionHelper.get(SESSION_PREVIEW_TICKET_KEY) != null;
    }

    public static boolean verifyCurrent() throws NodeLoadException, ModuleException {
        return getCurrent() != null;
    }

    public static Ticket getCurrent() throws NodeLoadException, ModuleException {
        String previewToken = SessionHelper.get(SESSION_PREVIEW_TICKET_KEY);
        if (StringUtils.isNotBlank(previewToken)) {
            User user = SecurityEventGenerator.triggerCurrentUserInterceptor();
            if (user != null) {
                return Ticket.getIfValid(user, previewToken);
            }
        }
        return null;
    }

    public static Ticket updateTicket(DateTime preview) throws ModuleException, NodeLoadException {
        Ticket ticket = getCurrent();
        if (ticket != null) {
            ticket.preview = preview.getMillis();
            return ticket.update();
        }
        return null;
    }

    public static Ticket createNewTicket(DateTime preview) {
        User user = (User) NodeContext.current().attributes.get(Security.Params.AUTH_USER);
        if (user != null) {
            Ticket ticket = Ticket.createInstance(user, preview);
            SessionHelper.set(SESSION_PREVIEW_TICKET_KEY, ticket.token);
            return ticket;
        }
        return null;
    }
}
