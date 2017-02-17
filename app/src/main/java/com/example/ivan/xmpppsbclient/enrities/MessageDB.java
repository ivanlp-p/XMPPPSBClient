package com.example.ivan.xmpppsbclient.enrities;

import com.example.ivan.xmpppsbclient.chat.db.MessagesTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by I.Laukhin on 16.02.2017.
 */

@StorIOSQLiteType(table = MessagesTable.TABLE_MESSAGES)
public class MessageDB {

    @StorIOSQLiteColumn(name = MessagesTable.COLUMN_ID, key = true)
    protected int id;
    @StorIOSQLiteColumn(name = MessagesTable.COLUMN_CONTACT)
    protected String contact;
    @StorIOSQLiteColumn(name = MessagesTable.COLUMN_TIMESTAMP)
    protected long timestamp;
    @StorIOSQLiteColumn(name = MessagesTable.COLUMN_INCOMING)
    protected boolean incoming;
    @StorIOSQLiteColumn(name = MessagesTable.COLUMN_MESSAGE_TEXT)
    protected String messageText;

    public MessageDB() {
    }

    public MessageDB(int id, String contact, long timestamp, boolean incoming, String messageText) {
        this.id = id;
        this.contact = contact;
        this.timestamp = timestamp;
        this.incoming = incoming;
        this.messageText = messageText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
