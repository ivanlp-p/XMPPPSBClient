package com.example.ivan.xmpppsbclient.userslist.presenter;

import com.example.ivan.xmpppsbclient.userslist.view.MainView;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface MainPresenter extends MvpPresenter<MainView> {

    void getUserList();
    void getAccess();
    void getChatWithUser(String userJid, String userName);
}
