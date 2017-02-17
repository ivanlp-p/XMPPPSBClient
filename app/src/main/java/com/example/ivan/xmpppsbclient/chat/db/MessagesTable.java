package com.example.ivan.xmpppsbclient.chat.db;

/**
 * Created by I.Laukhin on 16.02.2017.
 */

public class MessagesTable {

    public static final String TABLE_MESSAGES = "table_messages";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_INCOMING = "incoming";
    public static final String COLUMN_MESSAGE_TEXT = "message_text";

    private MessagesTable() {throw new IllegalStateException();}

    public static String createTableQuery() {
        return "CREATE TABLE " + TABLE_MESSAGES + " ("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CONTACT + " TEXT, "
                + COLUMN_TIMESTAMP  + " INTEGER NOT NULL, "
                + COLUMN_INCOMING  + " BOOLEAN, "
                + COLUMN_MESSAGE_TEXT  + " TEXT NOT NULL "
                + ");";
    }
}
