package dev.leonkim.hellojpa.domain.Inheritance;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "bMovie")
@DiscriminatorValue("M")
public class Movie extends Item {
    private String director;
    private String actor;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
