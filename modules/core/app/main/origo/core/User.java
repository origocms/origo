package main.origo.core;

import be.objectify.deadbolt.core.models.Subject;

public interface User extends Subject {

    String name();

    String type();

}
