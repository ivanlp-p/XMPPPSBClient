package com.example.ivan.xmpppsbclient.userslist.view;

import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public interface MainView extends MvpView {

    void showUserList(List<RosterGroupDecorator> rosterGroups);
    void showChatWithUser(String userJid, String userName);
}
