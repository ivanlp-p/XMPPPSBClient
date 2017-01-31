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
}
