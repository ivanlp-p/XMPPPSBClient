package com.example.ivan.xmpppsbclient.xmpp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class OpenfireConnection implements ConnectionListener {

    private static final String TAG = "OpenfireConnection";
    private static final String IP_ADDRESS = "192.168.0.46"; //192.168.1.35   192.168.0.46    192.168.1.186
    private static final int PORT = 5222;

    private Context context;

    private AbstractXMPPConnection connection;
    private Roster roster;
    private BroadcastReceiver uiThreadMessageReceiver;

    public static ConnectionState connectionState;

    public enum ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED
    }

    public enum LoggedInState {
        LOGGED_IN, LOGGED_OUT
    }

    public OpenfireConnection(Context context) {
        this.context = context;
    }

    public boolean connectToOpenfire(String login, String password) {

        boolean authorizationGranted = false;

        SmackConfiguration.DEBUG = true;
        XMPPTCPConnectionConfiguration XMPPConf = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(login, password)
                .setServiceName(IP_ADDRESS)
                .setHost(IP_ADDRESS)
                .setPort(PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setResource("test")
                .setSendPresence(false)
                .setDebuggerEnabled(true)
                .build();


        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");

        setupUiThreadBroadCastMessageReceiver();

        connection = new XMPPTCPConnection(XMPPConf);
        try {
            connection.connect();
            connection.setPacketReplyTimeout(10000);
            connection.login();

            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
            reconnectionManager.setEnabledPerDefault(true);
            reconnectionManager.enableAutomaticReconnection();

            authorizationGranted = true;
        } catch (SmackException e) {
            authorizationGranted = false;
            e.printStackTrace();
        } catch (IOException e) {
            authorizationGranted = false;
            e.printStackTrace();
        } catch (XMPPException e) {
            authorizationGranted = false;
            e.printStackTrace();
        }

        return authorizationGranted;
    }

    private void setupUiThreadBroadCastMessageReceiver() {
        uiThreadMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(OpenfireService.SEND_MESSAGE))
                {
                    sendMessage(intent.getStringExtra(OpenfireService.BUNDLE_MESSAGE_BODY),
                            intent.getStringExtra(OpenfireService.BUNDLE_TO));
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(OpenfireService.SEND_MESSAGE);
        context.registerReceiver(uiThreadMessageReceiver, filter);
    }

    private void sendMessage(String body, String toUserId) {
        Log.d(TAG,"Sending message to :" + toUserId);

        Chat chat = ChatManager.getInstanceFor(connection).createChat(toUserId);

        try {
            chat.sendMessage(body);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

  /*  @Override
    public void processMessage(Chat chat, Message message) {
        Log.d(TAG,"message.getBody() :" + message.getBody());
        Log.d(TAG,"message.getFrom() :" + message.getFrom());

        String fromUserId = message.getFrom();
        String contactId = "";

        if (fromUserId.contains("/")) {
            contactId = fromUserId.split("/")[0];
            Log.d(TAG, "The real id is :" + contactId);
        } else {
            contactId = fromUserId;
        }

        Intent intent = new Intent(OpenfireService.NEW_MESSAGE);
        intent.setPackage(context.getPackageName());
        intent.putExtra(OpenfireService.BUNDLE_FROM_ID, contactId);
        intent.putExtra(OpenfireService.BUNDLE_MESSAGE_BODY, message.getBody());
        context.sendBroadcast(intent);

        Log.d(TAG, "Received message from :" + contactId +" broadcast sent.");
    }*/

    public Roster getRoster() {
        return Roster.getInstanceFor(connection);
    }

    public List<RosterGroup> getUsersGroup() {
        List<RosterGroup> groupList = new ArrayList<>();

        roster = Roster.getInstanceFor(connection);
        Collection<RosterGroup> rosterGroups = roster.getGroups();

        for (RosterGroup group : rosterGroups) {
            groupList.add(group);
        }

        return groupList;
    }

    public Presence getUserPresence(String user) {
        if (connection != null) {
            roster = Roster.getInstanceFor(connection);

            return roster.getPresence(user);
        } else return null;
    }

    public VCardManager getVCardManager() {
        return VCardManager.getInstanceFor(connection);
    }

    public boolean isAuthenticated() {

        if (connection != null) {
            return connection.isAuthenticated();
        } else {
            return false;
        }
    }

    public ChatManager getChatManager() {
        return ChatManager.getInstanceFor(connection);
    }

    public OfflineMessageManager getOfflineMessageManager() {
        return new OfflineMessageManager(connection);
    }

    public void sendPresence(Presence presence){
        try {
            connection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        Log.d(TAG, "Disconnecting from serser");

        if (connection != null) {
            connection.disconnect();
        }

        connection = null;

        if (uiThreadMessageReceiver != null) {
            context.unregisterReceiver(uiThreadMessageReceiver);
            uiThreadMessageReceiver = null;
        }
    }

    @Override
    public void connected(XMPPConnection connection) {
        OpenfireService.connectionState = ConnectionState.CONNECTED;
        connectionState = ConnectionState.CONNECTED;
        Log.d(TAG, "Connected Successfully");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        OpenfireService.connectionState = ConnectionState.CONNECTED;
        connectionState = ConnectionState.AUTHENTICATED;
        Log.d(TAG, "Authenticated Successfully");
    }

    @Override
    public void connectionClosed() {
        OpenfireService.connectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "Connectionclosed()");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        OpenfireService.connectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "ConnectionClosedOnError, error "+ e.toString());
    }

    @Override
    public void reconnectionSuccessful() {
        OpenfireService.connectionState = ConnectionState.CONNECTED;
        Log.d(TAG, "ReconnectionSuccessful()");
    }

    @Override
    public void reconnectingIn(int seconds) {
        OpenfireService.connectionState = ConnectionState.CONNECTING;
        Log.d(TAG, "ReconnectingIn()");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        OpenfireService.connectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "ReconnectionFailed()");
    }
}
