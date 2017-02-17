package com.example.ivan.xmpppsbclient.chat.view;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.ivan.xmpppsbclient.enrities.MessageDB;

import java.util.List;

import jp.bassaer.chatmessageview.models.User;

/**
 * Created by I.Laukhin on 21.01.2017.
 */
@StateStrategyType(OneExecutionStateStrategy.class)
public interface ChatView extends MvpView {

    void showHistory(List<MessageDB> messages, User user, User contact);
    void showSendingMessage(User user, String message);
    void showIncomingMessage(User contact, String message);
}
