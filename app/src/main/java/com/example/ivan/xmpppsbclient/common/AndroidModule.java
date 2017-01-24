package com.example.ivan.xmpppsbclient.common;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

@Module
public class AndroidModule {

    private final XMPPPSBApplication application;

    public AndroidModule(XMPPPSBApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {return application;}
}
