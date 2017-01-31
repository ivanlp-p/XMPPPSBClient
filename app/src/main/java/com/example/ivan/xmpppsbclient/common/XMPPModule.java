package com.example.ivan.xmpppsbclient.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.ivan.xmpppsbclient.db.DbOpenHelper;
import com.example.ivan.xmpppsbclient.userslist.db.StorIOForUsersGroup;
import com.example.ivan.xmpppsbclient.utils.SharedPreferencesHelper;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

@Module
public class XMPPModule {

    @Provides
    @Singleton
    public OpenfireConnection provideOpenfireConnection(Context context) {
        return new OpenfireConnection(context);
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    public SharedPreferencesHelper provideSharedPreferencesHelper(SharedPreferences preferences) {
        return new SharedPreferencesHelper(preferences);
    }

    @Provides
    @Singleton
    public DbOpenHelper provideDbOpenHelper(Context context) {
        return new DbOpenHelper(context);
    }

    @Provides
    @Singleton
    public StorIOForUsersGroup provideStorIOForUsersGroup(DbOpenHelper dbOpenHelper) {
        return new StorIOForUsersGroup(dbOpenHelper);
    }
}
