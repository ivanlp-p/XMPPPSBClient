package com.example.ivan.xmpppsbclient.login.presenter;

import com.example.ivan.xmpppsbclient.login.view.LoginView;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface LoginPresenter extends MvpPresenter<LoginView> {

    void getAccess(String login, String password);
    boolean canFindUser();
}
