package com.example.ivan.xmpppsbclient.utils;

import android.content.SharedPreferences;

/**
 * Created by I.Laukhin on 22.01.2017.
 */

public class SharedPreferencesHelper {

    private static final String PREFERENCES_LOGIN = "preferences_login";
    private static final String PREFERENCES_PASSWORD = "preferences_password";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesHelper(SharedPreferences preferences) {
        this.preferences = preferences;
        editor = preferences.edit();
    }

    public void setLogin(String login) {
        editor.putString(PREFERENCES_LOGIN, login);
        editor.commit();
    }

    public String getLogin() {

        return preferences.getString(PREFERENCES_LOGIN, "");
    }

    public void setPassword(String password) {
        editor.putString(PREFERENCES_PASSWORD, password);
        editor.commit();
    }

    public String getPassword() {

        return preferences.getString(PREFERENCES_PASSWORD, "");
    }
}
