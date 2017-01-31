package com.example.ivan.xmpppsbclient.userslist.db;

import android.database.sqlite.SQLiteOpenHelper;

import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;
import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecoratorSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

/**
 * Created by I.Laukhin on 26.01.2017.
 */

public class StorIOForUsersGroup {

    private DefaultStorIOSQLite defaultStorIOSQLite;

    public StorIOForUsersGroup(SQLiteOpenHelper sqLiteOpenHelper) {

        defaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(RosterGroupDecorator.class, new RosterGroupDecoratorSQLiteTypeMapping())
                .build();
    }

    public void addUsersGroup(RosterGroupDecorator rosterGroupDecorator) {
        defaultStorIOSQLite
                .put()
                .object(rosterGroupDecorator)
                .prepare()
                .executeAsBlocking();
    }

    public void deleteUsersGroup(RosterGroupDecorator rosterGroupDecorator) {
        defaultStorIOSQLite
                .delete()
                .object(rosterGroupDecorator)
                .prepare()
                .executeAsBlocking();
    }

    public List<RosterGroupDecorator> loadUsersGroupFromDb() {
        return defaultStorIOSQLite
                .get()
                .listOfObjects(RosterGroupDecorator.class)
                .withQuery(
                        Query.builder()
                                .table(UsersListTable.TABLE_USERS_LIST)
                                .build())
                .prepare()
                .executeAsBlocking();
    }

    public RosterGroupDecorator loadUsersGroupByName(String groupName) {
        RosterGroupDecorator rosterGroupDecorator = defaultStorIOSQLite
                .get()
                .object(RosterGroupDecorator.class)
                .withQuery(Query.builder()
                        .table(UsersListTable.TABLE_USERS_LIST)
                        .where(UsersListTable.COLUMN_USERGROUP_NAME + " = ?")
                        .whereArgs(groupName)
                        .build())
                .prepare()
                .executeAsBlocking();

        return rosterGroupDecorator;
    }

    public boolean searchUsersGroupByName(String groupName) {
        RosterGroupDecorator rosterGroupDecorator = defaultStorIOSQLite
                .get()
                .object(RosterGroupDecorator.class)
                .withQuery(Query.builder()
                        .table(UsersListTable.TABLE_USERS_LIST)
                        .where(UsersListTable.COLUMN_USERGROUP_NAME + " = ?")
                        .whereArgs(groupName)
                        .build())
                .prepare()
                .executeAsBlocking();

        if (rosterGroupDecorator != null) {
            return true;
        } else {
            return false;
        }
    }

    public int getUsersGroupMaxId() {
        RosterGroupDecorator rosterGroupDecorator = defaultStorIOSQLite
                .get()
                .object(RosterGroupDecorator.class)
                .withQuery(Query.builder()
                        .table(UsersListTable.TABLE_USERS_LIST)
                        .orderBy(UsersListTable.COLUMN_ID + " DESC")
                        .build())
                .prepare()
                .executeAsBlocking();

        if (rosterGroupDecorator != null) {
            return rosterGroupDecorator.getId();
        } else {
            return 0;
        }
    }

    /*public void updateUsersInGroup(String groupName, RosterEntryDecorator entry) {
        defaultStorIOSQLite
                .lowLevel()
                .update(UpdateQuery.builder()
                        .table(UsersListTable.TABLE_USERS_LIST)
                        .where())

    }*/
}
