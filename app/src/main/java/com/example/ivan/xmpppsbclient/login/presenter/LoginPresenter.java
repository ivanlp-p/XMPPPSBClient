package com.example.ivan.xmpppsbclient.login.presenter;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface LoginPresenter  {

    void getAccess(String login, String password);
    boolean canFindUser();
}
