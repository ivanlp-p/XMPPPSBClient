package com.example.ivan.xmpppsbclient.login.presenter;

import android.os.Looper;

import com.example.ivan.xmpppsbclient.login.view.LoginView;
import com.example.ivan.xmpppsbclient.utils.SharedPreferencesHelper;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public class LoginPresenterImpl
        extends MvpBasePresenter<LoginView>
        implements LoginPresenter {

    private SharedPreferencesHelper prefsHelper;
    private OpenfireConnection connection;

    private String username;
    private String password;

    @Inject
    public LoginPresenterImpl(SharedPreferencesHelper prefsHelper, OpenfireConnection connection) {

        this.prefsHelper = prefsHelper;
        this.connection = connection;
    }

    @Override
    public void getAccess(String login, String password) {

        if (isViewAttached()) {
            username = login.split("@")[0];
            this.password = password;

            connectThread.start();
        }
    }

    @Override
    public boolean canFindUser() {

        if (prefsHelper.getLogin() != "") {
            return true;
        }

        return false;
    }

    private Thread connectThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();

            if (connection.connectToOpenfire(username, password)) {
                prefsHelper.setLogin(username);
                prefsHelper.setPassword(password);

                getView().startUserListActivity();
            }

            Looper.loop();
        }
    });
}
