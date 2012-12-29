package main.origo.admin.ui;

import main.origo.core.helpers.ElementHelper;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;
import play.api.templates.Html;
import views.html.origo.admin.decorators.forms.controlgroup;

public class AdminElement {

    public static class ControlGroup extends Element {

        protected ControlGroup() {
            super("control_group");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return controlgroup.render(this, ElementHelper.getHtmlFromBody(this), getAttributes());
        }
    }

}
