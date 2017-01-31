package com.example.ivan.xmpppsbclient.common;

import android.app.Application;

import com.example.ivan.xmpppsbclient.chat.presenter.ChatPresenterImpl;
import com.example.ivan.xmpppsbclient.login.presenter.LoginPresenterImpl;
import com.example.ivan.xmpppsbclient.userslist.presenter.MainPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public class XMPPPSBApplication extends Application {

    protected static ApplicationComponent component;

    @Singleton
    @Component(modules = {
            AndroidModule.class,
            XMPPModule.class})
    public interface ApplicationComponent {
        void inject(LoginPresenterImpl loginPresenter);
        void inject(MainPresenterImpl mainPresenter);
        void inject(ChatPresenterImpl chatPresenter);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerXMPPPSBApplication_ApplicationComponent
                .builder()
                .androidModule(new AndroidModule(this))
                .xMPPModule(new XMPPModule())
                .build();
    }

    public static ApplicationComponent component() {

        return component;
    }
}
