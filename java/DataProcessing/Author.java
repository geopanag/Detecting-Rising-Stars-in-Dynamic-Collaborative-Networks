package DataProcessing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Giwrgos
 */
public class Author {

    String name;
    String id;
    String affiliation;
    String field;
    String city;
    String country;
    Boolean started;

    public Author(String name, String id, String affiliation, String field, String city, String country, Boolean started) {
        this.name = name;
        this.id = id;
        this.affiliation = affiliation;
        this.field = field;
        this.city = city;
        this.country = country;
        this.started = started;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public Boolean isStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    

}
