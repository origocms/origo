package main.origo.core.preview;

import org.joda.time.DateTime;

public interface Ticket {

    boolean isValid();

    String userId();

    String token();

    DateTime validUntil();

    DateTime preview();

}
