package main.origo.preview.interceptors;

import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.preview.Ticket;
import main.origo.core.ui.Element;
import main.origo.preview.helpers.PreviewTicketHelper;
import models.origo.preview.BasicTicket;

@Interceptor
public class PreviewTokenInterceptor {

    private static final String COMMENT_LOADED = "preview_comment_loaded";

    @OnLoad
    public static void loadNode(OnLoad.Context context) throws ModuleException, NodeLoadException {
        if (!context.attributes.containsKey(COMMENT_LOADED) && PreviewTicketHelper.hasTicket() && PreviewTicketHelper.verifyCurrent()) {
            context.node.addHeadElement(new Element.Comment().setBody(getComment()));
            context.attributes.put(COMMENT_LOADED, true);
        }
    }

    private static String getComment() throws ModuleException, NodeLoadException {
        BasicTicket basicTicket = PreviewTicketHelper.getCurrent();
        return "Preview-Ticket { token: \""+ basicTicket.token+"\", valid-until: \"\\/Date("+ basicTicket.validUntil().getMillis()+")\\/\" }";
    }

    @Provides(type = Core.Type.PREVIEW, with = Core.With.PREVIEW_TOKEN)
    public static Ticket getCurrentToken(Provides.Context context) throws ModuleException, NodeLoadException {
        return PreviewTicketHelper.getCurrent();
    }
}
