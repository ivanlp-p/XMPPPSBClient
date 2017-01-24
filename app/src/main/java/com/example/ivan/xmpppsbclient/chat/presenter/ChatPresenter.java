package com.example.ivan.xmpppsbclient.chat.presenter;

import com.example.ivan.xmpppsbclient.chat.view.ChatView;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface ChatPresenter extends MvpPresenter<ChatView> {

    void getChatWithUser(String user);
    void sendMessage(String message);
}
