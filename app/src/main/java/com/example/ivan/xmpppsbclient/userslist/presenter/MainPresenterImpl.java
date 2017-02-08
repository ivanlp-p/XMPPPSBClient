package com.example.ivan.xmpppsbclient.userslist.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.ivan.xmpppsbclient.common.XMPPPSBApplication;
import com.example.ivan.xmpppsbclient.enrities.RosterEntryDecorator;
import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;
import com.example.ivan.xmpppsbclient.userslist.db.StorIOForUsersGroup;
import com.example.ivan.xmpppsbclient.userslist.view.MainView;
import com.example.ivan.xmpppsbclient.utils.SharedPreferencesHelper;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;


@InjectViewState
public class MainPresenterImpl extends MvpPresenter<MainView> {
    private static final int STATUS_CONNECTED = 1;
    private static final int UPDATE_PRESENCE = 2;
    private static final String TAG = "MainPresenterImpl";

    @Inject
    SharedPreferencesHelper prefsHelper;
    @Inject
    OpenfireConnection connection;
    @Inject
    StorIOForUsersGroup storIOForUsersGroup;

    private Message msg;

    @Inject
    public MainPresenterImpl() {
        XMPPPSBApplication.component().inject(this);
    }


    public void getAccess() {

        if (storIOForUsersGroup.loadUsersGroupFromDb().size() != 0) {
            List<RosterGroupDecorator> dbUsersGroups = storIOForUsersGroup.loadUsersGroupFromDb();

            getViewState().showUserList(dbUsersGroups);
        }

        if (!connection.isAuthenticated())
            connectThread.start();
    }


    public void getUserList() {
        Presence presence = new Presence(Presence.Type.available);
        connection.sendPresence(presence);

        final List<RosterGroupDecorator> updatedUsersGroup = storIOForUsersGroup.loadUsersGroupFromDb();
        getViewState().showUserList(updatedUsersGroup);

        Roster roster = connection.getRoster();

        roster.addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<String> addresses) {

            }

            @Override
            public void entriesUpdated(Collection<String> addresses) {

            }

            @Override
            public void entriesDeleted(Collection<String> addresses) {

            }

            @Override
            public void presenceChanged(Presence presence) {
                msg = handler.obtainMessage(UPDATE_PRESENCE);
                handler.sendMessage(msg);
            }
        });



        /*ChatManager chatManager = connection.getChatManager();

        chatManager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                if (!createdLocally) {
                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
                            String usetJid = chat.getParticipant();
                            Iterator<org.jivesoftware.smack.packet.Message.Body> iterator = message.getBodies().iterator();

                            while (iterator.hasNext()) {
                                Log.d(TAG, iterator.next().getMessage() + "");
                            }
                            Log.d(TAG, usetJid);
                        }
                    });
                }
            }
        });*/
    }

    public void getChatWithUser(String userJid, String userName) {

        getViewState().showChatWithUser(userJid, userName);
    }

    public void getOfflineMessages() {
        OfflineMessageManager offlineManager = connection.getOfflineMessageManager();

        try {
            Log.d(TAG,"" + offlineManager.supportsFlexibleRetrieval());
            Log.d(TAG,"message count " + offlineManager.getMessageCount());

            for (org.jivesoftware.smack.packet.Message message : offlineManager.getMessages()) {
                List<String> updateNewMessageList = new ArrayList<>();
                String userJid = message.getFrom();
                String userJidWithoutClient = message.getFrom().split("/")[0];

                for (org.jivesoftware.smack.packet.Message newMessage : offlineManager.getMessages()) {
                    if (newMessage.getFrom().equals(userJid)) {
                        updateNewMessageList.add(newMessage.getBody());
                    }
                }

                if (updateNewMessageList.size() > 0) {
                    List<RosterGroupDecorator> usersGroup = storIOForUsersGroup.loadUsersGroupFromDb();
                    for (RosterGroupDecorator group : usersGroup) {
                        for (RosterEntryDecorator entry : group.getChildList()) {
                            Log.d(TAG, "db userJid " + entry.getUserJid());
                            Log.d(TAG, "msg userJid " + userJidWithoutClient);
                            if (entry.getUserJid().equals(userJidWithoutClient)) {
                                /*if (entry.getUnreadMeassageFromUser().size() != 0) {
                                    List<String> newMessages = entry.getUnreadMeassageFromUser();
                                    newMessages.addAll(updateNewMessageList);
                                    entry.setUnreadMeassageFromUser(newMessages);
                                } else {
                                    entry.setUnreadMeassageFromUser(updateNewMessageList);
                                }*/
                                group.updateNewMessagesFromUser(userJidWithoutClient, updateNewMessageList);
                                storIOForUsersGroup.addUsersGroup(group);
                            }

                        }

                    }
                }
                Log.d(TAG, message.getFrom());
                Log.d(TAG, message.getBody());

            }
            offlineManager.deleteMessages();
        } catch (SmackException.NoResponseException e) {
            Log.d(TAG, "SmackException.NoResponseException");
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            Log.d(TAG, "XMPPException.XMPPErrorException");
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            Log.d(TAG, "SmackException.NotConnectedException");
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    connectThread.interrupt();
                    getOfflineMessages();
                    getUserList();
                    break;
                case 2:
                    getViewState().showUserList(storIOForUsersGroup.loadUsersGroupFromDb());
                    break;
            }
        }
    };

    private Thread connectThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();

            if (connection.connectToOpenfire(prefsHelper.getLogin(), prefsHelper.getPassword())) {

                while (connection.getUsersGroup().size() == 0) {

                }

                List<RosterGroup> rosterGroups = connection.getUsersGroup();

                int id;

                for (RosterGroup rosterGroup : rosterGroups) {

                    RosterGroupDecorator rosterGroupDecorator;

                    if (!storIOForUsersGroup.searchUsersGroupByName(rosterGroup.getName())) {

                        id = storIOForUsersGroup.getUsersGroupMaxId() + 1;
                        rosterGroupDecorator = new RosterGroupDecorator(id, rosterGroup.getName(), rosterGroup.getEntries());
                        storIOForUsersGroup.addUsersGroup(rosterGroupDecorator);

                    } else if (storIOForUsersGroup.searchUsersGroupByName(rosterGroup.getName()) &&
                            storIOForUsersGroup.loadUsersGroupByName(rosterGroup.getName()).getChildList().size() != rosterGroup.getEntries().size()) {

                        id = storIOForUsersGroup.loadUsersGroupByName(rosterGroup.getName()).getId();

                        rosterGroupDecorator = new RosterGroupDecorator(id, rosterGroup.getName(), rosterGroup.getEntries());

                        storIOForUsersGroup.addUsersGroup(rosterGroupDecorator);
                    }
                }

                msg = handler.obtainMessage(STATUS_CONNECTED);
                handler.sendMessage(msg);
            }

            Looper.loop();
        }
    });
}
