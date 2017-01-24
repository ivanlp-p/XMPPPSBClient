package com.example.ivan.xmpppsbclient.login.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface LoginView extends MvpView {

    void startUserListActivity();
    void showWarningToast();
}
