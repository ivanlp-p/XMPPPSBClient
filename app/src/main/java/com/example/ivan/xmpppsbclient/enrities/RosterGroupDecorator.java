package com.example.ivan.xmpppsbclient.enrities;

import android.util.Log;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.ivan.xmpppsbclient.userslist.db.UsersListTable;
import com.example.ivan.xmpppsbclient.utils.SerializeHelper;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I.Laukhin on 23.01.2017.
 */
@StorIOSQLiteType(table = UsersListTable.TABLE_USERS_LIST)
public class RosterGroupDecorator implements Parent<RosterEntryDecorator> {

    @StorIOSQLiteColumn(name = UsersListTable.COLUMN_ID, key = true)
    protected int id;

    @StorIOSQLiteColumn(name = UsersListTable.COLUMN_USERGROUP_NAME)
    protected String name;

    @StorIOSQLiteColumn(name = UsersListTable.COLUMN_USERSLIST)
    protected String rosterEntriesListString;

    protected List<RosterEntry> rosterEntries;

    public RosterGroupDecorator() {
    }

    public RosterGroupDecorator(int id, String name, List<RosterEntry> rosterEntries) {
        this.id = id;
        this.name = name;
        this.rosterEntries = rosterEntries;

        SerializeHelper serializeHelper = SerializeHelper.getInstance();

        List<RosterEntryDecorator> userOfflineList = new ArrayList<>();

        for (int i = 0; i < rosterEntries.size(); i++) {
            userOfflineList.add(new RosterEntryDecorator(i+1, rosterEntries.get(i).getUser(), rosterEntries.get(i).getName(), new ArrayList<String>()));
        }

        rosterEntriesListString = serializeHelper.serializeWithGson(userOfflineList);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<RosterEntryDecorator> getChildList() {
        return SerializeHelper.getInstance().deserializeUsersGroup(rosterEntriesListString);
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public void updateNewMessagesFromUser(String userJid, List<String> newMessagesList) {
        List<RosterEntryDecorator> rosterEntryList = SerializeHelper.getInstance().deserializeUsersGroup(rosterEntriesListString);

        RosterEntryDecorator updateRosterEntry = null;

        for (RosterEntryDecorator entry : rosterEntryList) {
            if (entry.getUserJid().equals(userJid)) {
                updateRosterEntry = entry;
            }
        }

        if (updateRosterEntry != null) {
            if (updateRosterEntry.getUnreadMeassageFromUser().size() != 0) {
                List<String> updateNewMessagesList = updateRosterEntry.getUnreadMeassageFromUser();
                updateNewMessagesList.addAll(newMessagesList);
                updateRosterEntry.setUnreadMeassageFromUser(updateNewMessagesList);
            } else {
                updateRosterEntry.setUnreadMeassageFromUser(newMessagesList);
            }
        }

        rosterEntriesListString = SerializeHelper.getInstance().serializeWithGson(rosterEntryList);

        Log.d("RosterDec", "size = " + SerializeHelper.getInstance().deserializeUsersGroup(rosterEntriesListString).get(0).getUnreadMeassageFromUser().size());
    }
}
