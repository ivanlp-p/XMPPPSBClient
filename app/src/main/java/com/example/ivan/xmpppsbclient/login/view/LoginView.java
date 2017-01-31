package com.example.ivan.xmpppsbclient.login.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by I.Laukhin on 21.01.2017.
 */
@StateStrategyType(SkipStrategy.class)
public interface LoginView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startUserListActivity();
    void showWarningToast();
    void showProgressDialog();
}
