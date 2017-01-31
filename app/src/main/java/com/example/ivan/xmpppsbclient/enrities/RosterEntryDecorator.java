package com.example.ivan.xmpppsbclient.enrities;

/**
 * Created by I.Laukhin on 27.01.2017.
 */

public class RosterEntryDecorator {

    private int id;
    private String user;
    private String name;

    public RosterEntryDecorator(int id, String user, String name) {
        this.id = id;
        this.user = user;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
