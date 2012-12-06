package com.origocms.core.models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "content")
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    @Column(name = "content")
    @Lob
    public String value;

    public Content() {
        this.identifier = UUID.randomUUID().toString();
    }

    public static Content findWithIdentifier(String identifier) {
        return JPA.em().find(Content.class, identifier);
    }

}
