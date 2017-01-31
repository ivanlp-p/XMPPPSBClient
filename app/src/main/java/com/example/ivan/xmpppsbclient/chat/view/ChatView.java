package com.example.ivan.xmpppsbclient.chat.view;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by I.Laukhin on 21.01.2017.
 */
@StateStrategyType(OneExecutionStateStrategy.class)
public interface ChatView extends MvpView {

    void showSendingMessage(String message);
    void showIncomingMessage(String message);
}
