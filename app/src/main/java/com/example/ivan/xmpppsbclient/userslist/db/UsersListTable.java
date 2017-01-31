package com.example.ivan.xmpppsbclient.userslist.db;

/**
 * Created by I.Laukhin on 26.01.2017.
 */

public class UsersListTable {

    public static final String TABLE_USERS_LIST = "table_user_list";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_USERGROUP_NAME = "usergroup_name";

    public static final String COLUMN_USERSLIST = "userlist";

    private UsersListTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String createTableQuery() {
        return "CREATE TABLE " + TABLE_USERS_LIST + " ("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERGROUP_NAME + " TEXT NOT NULL, "
                + COLUMN_USERSLIST  + " TEXT NOT NULL "
                + ");";
    }
}
