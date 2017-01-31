package com.example.ivan.xmpppsbclient.login.presenter;

import android.os.Looper;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.ivan.xmpppsbclient.common.XMPPPSBApplication;
import com.example.ivan.xmpppsbclient.login.view.LoginView;
import com.example.ivan.xmpppsbclient.utils.SharedPreferencesHelper;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;

import javax.inject.Inject;

/**
 * Created by I.Laukhin on 21.01.2017.
 */
@InjectViewState
public class LoginPresenterImpl extends MvpPresenter<LoginView> {

    @Inject
    SharedPreferencesHelper prefsHelper;
    @Inject
    OpenfireConnection connection;

    private String username;
    private String password;

    public LoginPresenterImpl() {
        XMPPPSBApplication.component().inject(this);
    }

    public void getAccess(String login, String password) {

            username = login.split("@")[0];
            this.password = password;

            connectThread.start();

            getViewState().showProgressDialog();
    }

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

                getViewState().startUserListActivity();
            }

            Looper.loop();
        }
    });
}
