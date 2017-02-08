package com.example.ivan.xmpppsbclient.userslist.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;

import java.util.List;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

@StateStrategyType(OneExecutionStateStrategy.class)
public interface MainView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showUserList(List<RosterGroupDecorator> rosterGroups);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showChatWithUser(String userJid, String userName);

    void updateUsersListWhenProcessNewChat(String userJid);
}
