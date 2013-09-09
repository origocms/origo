package main.origo.core.preview;

import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.NodeContext;
import main.origo.core.event.ProvidesEventGenerator;

public class PreviewEventGenerator {

    public static Ticket getValidTicket() throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(NodeContext.current().node, Core.Type.PREVIEW, Core.With.PREVIEW_TOKEN);
    }

}
