package com.example.ivan.xmpppsbclient.enrities;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

/**
 * Created by I.Laukhin on 23.01.2017.
 */

public class RosterGroupDecorator implements Parent<RosterEntry> {

    private String name;
    private List<RosterEntry> rosterEntries;


    public RosterGroupDecorator(String name, List<RosterEntry> rosterEntries) {
        this.name = name;
        this.rosterEntries = rosterEntries;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<RosterEntry> getChildList() {
        return rosterEntries;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
