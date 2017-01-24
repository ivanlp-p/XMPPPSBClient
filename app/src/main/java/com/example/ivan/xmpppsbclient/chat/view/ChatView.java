package com.example.ivan.xmpppsbclient.chat.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface ChatView extends MvpView{

    void showSendingMessage(String message);
    void showIncomingMessage(String message);

}
