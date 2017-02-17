package com.example.ivan.xmpppsbclient.chat.db;

import android.database.sqlite.SQLiteOpenHelper;

import com.example.ivan.xmpppsbclient.enrities.MessageDB;
import com.example.ivan.xmpppsbclient.enrities.MessageDBSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

/**
 * Created by I.Laukhin on 16.02.2017.
 */

public class StorIOForMessages {

    private DefaultStorIOSQLite defaultStorIOSQLite;

    public StorIOForMessages(SQLiteOpenHelper sqLiteOpenHelper) {

        defaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(MessageDB.class, new MessageDBSQLiteTypeMapping())
                .build();
    }

    public void addMessage(MessageDB messageDB) {
        defaultStorIOSQLite
                .put()
                .object(messageDB)
                .prepare()
                .executeAsBlocking();
    }

    public List<MessageDB> loadMessages(String contact) {
        return defaultStorIOSQLite
                .get()
                .listOfObjects(MessageDB.class)
                .withQuery(Query.builder()
                    .table(MessagesTable.TABLE_MESSAGES)
                    .where(MessagesTable.COLUMN_CONTACT + " = ?")
                    .whereArgs(contact)
                    .build())
                .prepare()
                .executeAsBlocking();
    }

    public int getMessageMaxId() {
        MessageDB messageDB = defaultStorIOSQLite
                .get()
                .object(MessageDB.class)
                .withQuery(Query.builder()
                    .table(MessagesTable.TABLE_MESSAGES)
                    .orderBy(MessagesTable.COLUMN_ID + " DESC")
                    .build())
                .prepare()
                .executeAsBlocking();

        if (messageDB != null) {
            return messageDB.getId();
        } else {
            return 0;
        }
    }
}
