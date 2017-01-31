package com.example.ivan.xmpppsbclient.userslist.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.ivan.xmpppsbclient.common.XMPPPSBApplication;
import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;
import com.example.ivan.xmpppsbclient.userslist.db.StorIOForUsersGroup;
import com.example.ivan.xmpppsbclient.userslist.view.MainView;
import com.example.ivan.xmpppsbclient.utils.SharedPreferencesHelper;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;

import org.jivesoftware.smack.roster.RosterGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

@InjectViewState
public class MainPresenterImpl extends MvpPresenter<MainView> {
    private static final int STATUS_CONNECTED = 1;
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
            List<RosterGroupDecorator> dbUserGroups = storIOForUsersGroup.loadUsersGroupFromDb();

            getViewState().showUserList(dbUserGroups);

        }
        connectThread.start();
    }


    public void getUserList() {
        List<RosterGroupDecorator> usersGroup = new ArrayList<>();

        while (connection.getUsersGroup().size() == 0) {

        }
        List<RosterGroup> rosterGroups = connection.getUsersGroup();

        int id = 1;

        for (RosterGroup rosterGroup : rosterGroups) {


            RosterGroupDecorator rosterGroupDecorator =
                    new RosterGroupDecorator(id, rosterGroup.getName(), rosterGroup.getEntries());

            usersGroup.add(rosterGroupDecorator);

            id++;
//            if (rosterGroup.getName().equals(storIOForUsersGroup.loadUsersGroupFromDb().get(0).ge))
            storIOForUsersGroup.addUsersGroup(rosterGroupDecorator);
        }

        getViewState().showUserList(usersGroup);
    }

    public void getChatWithUser(String userJid, String userName) {

        getViewState().showChatWithUser(userJid, userName);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == STATUS_CONNECTED) {
                Log.d(TAG, "handler get msg");
                connectThread.interrupt();
                getUserList();
            }
        }
    };

    private Thread connectThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();


            if (connection.connectToOpenfire(prefsHelper.getLogin(), prefsHelper.getPassword())) {
                msg = handler.obtainMessage(STATUS_CONNECTED);
                handler.sendMessage(msg);
            }

            Looper.loop();
        }
    });
}
