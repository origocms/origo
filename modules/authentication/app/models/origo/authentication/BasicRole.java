package models.origo.authentication;

import be.objectify.deadbolt.core.models.Role;
import models.origo.core.Model;

import javax.persistence.*;

@Entity(name = "basicRole")
@Table(name = "role")
public class BasicRole extends Model<BasicRole> implements Role {

    public static final String TYPE = "origo.authentication.BasicRole";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;

    public BasicRole() {
        super(TYPE);
    }

    @Override
    public String getName() {
        return name;
    }
}
