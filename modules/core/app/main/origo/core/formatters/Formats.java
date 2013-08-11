package main.origo.core.formatters;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.api.i18n.Lang;
import play.data.format.Formatters;
import play.i18n.Messages;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class Formats {

    /**
     * Defines the format for a <code>Date</code> field.
     */
    @Target({FIELD})
    @Retention(RUNTIME)
    @play.data.Form.Display(name="format.date", attributes={"key"})
    public static @interface DateTimePattern {

        /**
         * Date pattern, as specified for {@link java.text.SimpleDateFormat}.
         */
        String key();
    }

    /**
     * Annotation formatter, triggered by the <code>@DateTime</code> annotation.
     */
    public static class AnnotationLocalDateFormatter extends Formatters.AnnotationFormatter<DateTimePattern,LocalDate> {

        /**
         * Binds the field - constructs a concrete value from submitted data.
         *
         * @param annotation the annotation that triggered this formatter
         * @param text the field text
         * @param locale the current <code>Locale</code>
         * @return a new value
         */
        public LocalDate parse(DateTimePattern annotation, String text, Locale locale) throws java.text.ParseException {
            if(text == null || text.trim().isEmpty()) {
                return null;
            }
            DateTimeFormatter fmt = DateTimeFormat.forPattern(Messages.get(new Lang(locale.getLanguage(), locale.getCountry()), annotation.key()));
            return fmt.parseLocalDate(text);
        }

        /**
         * Unbinds this field - converts a concrete value to plain string
         *
         * @param annotation the annotation that triggered this formatter
         * @param value the value to unbind
         * @param locale the current <code>Locale</code>
         * @return printable version of the value
         */
        public String print(DateTimePattern annotation, LocalDate value, Locale locale) {
            if(value == null) {
                return "";
            }
            DateTimeFormatter fmt = DateTimeFormat.forPattern(Messages.get(new Lang(locale.getLanguage(), locale.getCountry()), annotation.key()));
            return fmt.print(value);
        }

    }

    public static void register() {
        Formatters.register(LocalDate.class, new AnnotationLocalDateFormatter());
    }
}
