package com.example.ivan.xmpppsbclient.enrities;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by I.Laukhin on 27.01.2017.
 */

public class RosterEntryDecorator implements Serializable {

    private int id;
    private String userJid;
    private String name;
    private List<String> unreadMeassageFromUser;

    public RosterEntryDecorator(int id,
                                String userJid,
                                String name,
                                List<String> unreadMeassageFromUser) {
        this.id = id;
        this.userJid = userJid;
        this.name = name;
        this.unreadMeassageFromUser = unreadMeassageFromUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserJid() {
        return userJid;
    }

    public void setUserJid(String userJid) {
        this.userJid = userJid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUnreadMeassageFromUser() {
        return unreadMeassageFromUser;
    }

    public void setUnreadMeassageFromUser(List<String> unreadMeassageFromUser) {
        this.unreadMeassageFromUser = unreadMeassageFromUser;
    }
}
