package com.example.ivan.xmpppsbclient.userslist.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;
import com.example.ivan.xmpppsbclient.userslist.view.MainView;
import com.example.ivan.xmpppsbclient.utils.SharedPreferencesHelper;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.jivesoftware.smack.roster.RosterGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public class MainPresenterImpl
        extends MvpBasePresenter<MainView>
        implements MainPresenter
{
    private static final int STATUS_CONNECTED = 1;
    private static final String TAG = "MainPresenterImpl";

    private SharedPreferencesHelper prefsHelper;
    private OpenfireConnection connection;

    private Message msg;

    @Inject
    public MainPresenterImpl(SharedPreferencesHelper prefsHelper, OpenfireConnection connection) {
        this.prefsHelper = prefsHelper;
        this.connection = connection;
    }

    @Override
    public void getAccess() {
        connectThread.start();
    }

    @Override
    public void getUserList() {
        List<RosterGroupDecorator> usersGroup = new ArrayList<>();

        while (connection.getUsersGroup().size() == 0){

        }
        List<RosterGroup> rosterGroups = connection.getUsersGroup();

        for (RosterGroup rosterGroup : rosterGroups) {
            usersGroup.add(new RosterGroupDecorator(rosterGroup.getName(), rosterGroup.getEntries()));
        }

        if (isViewAttached()) {
            getView().showUserList(usersGroup);
        }
    }

    @Override
    public void getChatWithUser(String userJid, String userName) {
        if (isViewAttached()) {
            getView().showChatWithUser(userJid, userName);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == STATUS_CONNECTED) {
                Log.d(TAG, "handler get msg");
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
