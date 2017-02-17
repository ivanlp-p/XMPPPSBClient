package com.example.ivan.xmpppsbclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ivan.xmpppsbclient.chat.db.MessagesTable;
import com.example.ivan.xmpppsbclient.userslist.db.UsersListTable;

/**
 * Created by I.Laukhin on 26.01.2017.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "XMPPPSB";
    private static final int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UsersListTable.createTableQuery());
        db.execSQL(MessagesTable.createTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
