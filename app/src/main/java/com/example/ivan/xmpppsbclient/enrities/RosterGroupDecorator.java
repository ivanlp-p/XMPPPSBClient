package com.example.ivan.xmpppsbclient.enrities;

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
            userOfflineList.add(new RosterEntryDecorator(i, rosterEntries.get(i).getUser(), rosterEntries.get(i).getName()));
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
}
