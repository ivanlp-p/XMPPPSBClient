package com.example.ivan.xmpppsbclient.userslist.presenter;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface MainPresenter{

    void getUserList();
    void getAccess();
    void getChatWithUser(String userJid, String userName);
}
