package com.example.ivan.xmpppsbclient.chat.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.chat.db.StorIOForMessages;
import com.example.ivan.xmpppsbclient.chat.view.ChatView;
import com.example.ivan.xmpppsbclient.common.XMPPPSBApplication;
import com.example.ivan.xmpppsbclient.enrities.MessageDB;
import com.example.ivan.xmpppsbclient.enrities.RosterEntryDecorator;
import com.example.ivan.xmpppsbclient.utils.SharedPreferencesHelper;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.List;

import javax.inject.Inject;

import jp.bassaer.chatmessageview.models.User;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

@InjectViewState
public class ChatPresenterImpl extends MvpPresenter<ChatView>
{
    private static final String TAG = "ChatPresenterImpl";

    @Inject
    OpenfireConnection connection;
    @Inject
    Context context;
    @Inject
    StorIOForMessages storIOForMessages;
    @Inject
    SharedPreferencesHelper prefsHelper;

    private ChatManager chatManager;
    private RosterEntryDecorator rosterContact;
    private Chat newChat;
    private User user;
    private User contact;
    private VCardManager vCardManager;

    public ChatPresenterImpl() {
        XMPPPSBApplication.component().inject(this);
    }

    public void loadHistory(RosterEntryDecorator rosterContact){
        this.rosterContact = rosterContact;

        vCardManager = connection.getVCardManager();

        VCard userVCard = null;
        VCard contactVCard = null;
        try {
            userVCard = vCardManager.loadVCard();
            contactVCard = vCardManager.loadVCard(rosterContact.getUserJid());
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        if (userVCard != null && contactVCard != null) {
            user = new User(0, userVCard.getNickName(), BitmapFactory.decodeResource(context.getResources(),R.drawable.face_1));
            contact = new User(rosterContact.getId(), contactVCard.getNickName(), BitmapFactory.decodeResource(context.getResources(), R.drawable.face_2));
        } else {
            user = new User(0, prefsHelper.getUsername(), BitmapFactory.decodeResource(context.getResources(),R.drawable.face_1));
            contact = new User(rosterContact.getId(), rosterContact.getName(), BitmapFactory.decodeResource(context.getResources(), R.drawable.face_2));
        }

        List<MessageDB> messages = storIOForMessages.loadMessages(rosterContact.getName());

        if (messages != null) {
            getViewState().showHistory(messages, user, contact);
        }
    }

    public void getChatWithUser(RosterEntryDecorator contactRosterEntry) {
        chatManager = connection.getChatManager();

        newChat = chatManager.createChat(contactRosterEntry.getUserJid(), new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                Log.d(TAG, "new chat From = " + message.getFrom() + " Body = " + message.getBody());

                MessageDB messageDB = new MessageDB(
                        storIOForMessages.getMessageMaxId() + 1,
                        rosterContact.getName(),
                        System.currentTimeMillis(),
                        true,
                        message.getBody()
                );

                storIOForMessages.addMessage(messageDB);

                android.os.Message msg = handler.obtainMessage();
                msg.obj = message;
                handler.sendMessage(msg);
            }
        });
    }

    public void sendMessage(String message) {
        try {
            Message newMessage = new Message();
            newMessage.setBody(message);
            DefaultExtensionElement msgTimeSend = new DefaultExtensionElement("time", "urn:xmpp:time");
            String msgTimeStamp = String.valueOf(System.currentTimeMillis());
            msgTimeSend.setValue("time", msgTimeStamp);
            newMessage.addExtension(msgTimeSend);

            newChat.sendMessage(newMessage);

            Log.d(TAG, rosterContact.getName());

            MessageDB messageDB = new MessageDB(
                    storIOForMessages.getMessageMaxId() + 1,
                    rosterContact.getName(),
                    System.currentTimeMillis(),
                    false,
                    message
            );

            storIOForMessages.addMessage(messageDB);
            getViewState().showSendingMessage(user, message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Message message = (Message) msg.obj;
            getViewState().showIncomingMessage(contact, message.getBody());
        }
    };
}
