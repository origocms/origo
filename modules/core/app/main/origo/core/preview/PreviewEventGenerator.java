package main.origo.core.preview;

import main.origo.core.ModuleException;
import main.origo.core.NoSuchProviderException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.NodeContext;
import main.origo.core.event.ProvidesEventGenerator;
import play.Logger;

public class PreviewEventGenerator {

    public static final String REQUEST_PREVIEW_TOKEN = "origo.preview.token";

    public static Ticket getValidTicket() throws ModuleException, NodeLoadException {
        Ticket ticket = (Ticket) NodeContext.current().attributes.get(REQUEST_PREVIEW_TOKEN);
        if (ticket == null) {
            try {
                ticket = ProvidesEventGenerator.triggerInterceptor(NodeContext.current().node, Core.Type.PREVIEW, Core.With.PREVIEW_TOKEN);
                if (ticket != null) {
                    NodeContext.current().attributes.put(REQUEST_PREVIEW_TOKEN, ticket);
                }
            } catch (NoSuchProviderException e) {
                Logger.error("No provider for PreviewTicket's available.", e);
            }
        }

        return ticket;
    }

}
