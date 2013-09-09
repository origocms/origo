package forms.origo.preview;

import main.origo.core.formatters.Formats;
import models.origo.preview.BasicTicket;
import org.joda.time.DateTime;
import play.data.Form;
import play.data.validation.Constraints;

public class PreviewTokenForm {

    @Formats.DateTimePattern(key="date.time.format")
    @Constraints.Required
    public DateTime preview;

    public static Form<PreviewTokenForm> fill(BasicTicket basicTicket) {
        PreviewTokenForm form = new PreviewTokenForm();
        form.preview = basicTicket.preview();
        return new Form(forms.origo.preview.PreviewTokenForm.class).fill(form);
    }

    public DateTime getPreview() {
        return preview;
    }

    public void setPreview(DateTime preview) {
        this.preview = preview;
    }
}
