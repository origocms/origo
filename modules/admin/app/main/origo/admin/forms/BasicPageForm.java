package main.origo.admin.forms;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import play.data.validation.Constraints;

public class BasicPageForm {

    @Constraints.Required
    public String title;

    @Constraints.Required
    public String leadText;

    @Constraints.Required
    public String bodyText;

    public String themeVariant;

    public LocalDate publishDate;
    public LocalTime publishTime;

    public LocalDate unpublishDate;
    public LocalTime unpublishTime;

}
