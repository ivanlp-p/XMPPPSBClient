package com.example.ivan.xmpppsbclient.chat.presenter;

import android.os.Handler;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.ivan.xmpppsbclient.chat.view.ChatView;
import com.example.ivan.xmpppsbclient.common.XMPPPSBApplication;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import javax.inject.Inject;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

@InjectViewState
public class ChatPresenterImpl extends MvpPresenter<ChatView>
{
    private static final String TAG = "ChatPresenterImpl";

    @Inject
    OpenfireConnection connection;

    private ChatManager chatManager;
    private Chat newChat;

    public ChatPresenterImpl() {
        XMPPPSBApplication.component().inject(this);
    }

    public void getChatWithUser(String user) {
        chatManager = connection.createChatManager();

        newChat = chatManager.createChat(user, new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                Log.d(TAG, "new chat From = " + message.getFrom() + " Body = " + message.getBody());

                android.os.Message msg = handler.obtainMessage();
                msg.obj = message;
                handler.sendMessage(msg);
            }
        });
    }

    public void sendMessage(String message) {
        try {
            newChat.sendMessage(message);
            getViewState().showSendingMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Message message = (Message) msg.obj;
            getViewState().showIncomingMessage(message.getBody());
        }
    };
}
