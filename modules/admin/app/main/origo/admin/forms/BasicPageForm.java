package main.origo.admin.forms;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import play.data.validation.Constraints;

import java.util.List;

public class BasicPageForm {

    // General
    @Constraints.Required
    public String title;

    public String themeVariant;

    // Content
    @Constraints.Required
    public List<String> identifiers;

    @Constraints.Required
    public List<String> regions;

    @Constraints.Required
    public List<String> weights;

    // Publish
    public LocalDate publishDate;
    public LocalTime publishTime;

    public LocalDate unpublishDate;
    public LocalTime unpublishTime;

}
