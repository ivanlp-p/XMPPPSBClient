package com.example.ivan.xmpppsbclient.common;

import android.app.Application;

import com.example.ivan.xmpppsbclient.chat.presenter.ChatPresenteImpl;
import com.example.ivan.xmpppsbclient.chat.view.ChatActivity;
import com.example.ivan.xmpppsbclient.login.presenter.LoginPresenterImpl;
import com.example.ivan.xmpppsbclient.login.view.LoginActivity;
import com.example.ivan.xmpppsbclient.userslist.presenter.MainPresenterImpl;
import com.example.ivan.xmpppsbclient.userslist.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public class XMPPPSBApplication extends Application {

    protected ApplicationComponent component;

    @Singleton
    @Component(modules = {
            AndroidModule.class,
            XMPPModule.class})
    public interface ApplicationComponent {
        void inject(LoginActivity activity);
        void inject(MainActivity activity);
        void inject(ChatActivity activity);

        LoginPresenterImpl loginPresenter();
        MainPresenterImpl mainPresenter();
        ChatPresenteImpl chatPresenter();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ApplicationComponent component() {

        synchronized (this) {
            if (component == null) {
                component = createComponent();
            }
        }

        return component;
    }

    private ApplicationComponent createComponent() {
        return DaggerXMPPPSBApplication_ApplicationComponent
                .builder()
                .androidModule(new AndroidModule(this))
                .xMPPModule(new XMPPModule())
                .build();
    }
}
