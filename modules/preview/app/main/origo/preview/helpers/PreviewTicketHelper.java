package main.origo.preview.helpers;

import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.User;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.SessionHelper;
import main.origo.core.security.Security;
import main.origo.core.security.SecurityEventGenerator;
import models.origo.preview.BasicTicket;
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

    public static BasicTicket getCurrent() throws NodeLoadException, ModuleException {
        String previewToken = SessionHelper.get(SESSION_PREVIEW_TICKET_KEY);
        if (StringUtils.isNotBlank(previewToken)) {
            User user = SecurityEventGenerator.triggerCurrentUserInterceptor();
            if (user != null) {
                return BasicTicket.getIfValid(user, previewToken);
            }
        }
        return null;
    }

    public static BasicTicket updateTicket(DateTime preview) throws ModuleException, NodeLoadException {
        BasicTicket basicTicket = getCurrent();
        if (basicTicket != null) {
            basicTicket.preview = preview.getMillis();
            return basicTicket.update();
        }
        return null;
    }

    public static BasicTicket createNewTicket(DateTime preview) {
        User user = (User) NodeContext.current().attributes.get(Security.Params.AUTH_USER);
        if (user != null) {
            BasicTicket basicTicket = BasicTicket.createInstance(user, preview);
            SessionHelper.set(SESSION_PREVIEW_TICKET_KEY, basicTicket.token);
            return basicTicket;
        }
        return null;
    }
}
