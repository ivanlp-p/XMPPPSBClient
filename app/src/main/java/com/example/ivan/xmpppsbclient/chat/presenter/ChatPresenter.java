package com.example.ivan.xmpppsbclient.chat.presenter;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface ChatPresenter{

    void getChatWithUser(String user);
    void sendMessage(String message);
}
