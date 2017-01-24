package com.example.ivan.xmpppsbclient.xmpp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ivan.xmpppsbclient.utils.SharedPreferencesHelper;


/**
 * Created by I.Laukhin on 22.01.2017.
 */

public class OpenfireService extends Service {

    private static final String TAG = "OpenfireService";

    public static final String UI_AUTHENTICATED = "com.blikoon.rooster.uiauthenticated";
    public static final String SEND_MESSAGE = "com.blikoon.rooster.sendmessage";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_TO = "b_to";

    public static final String NEW_MESSAGE = "com.blikoon.rooster.newmessage";
    public static final String BUNDLE_FROM_ID = "b_from";

    public static OpenfireConnection.ConnectionState connectionState;
    public static OpenfireConnection.LoggedInState loggedInState;
    private boolean active;
    private Thread thread;
    private Handler handler;
    private SharedPreferencesHelper prefsHelper;

    private OpenfireConnection connection;

    public OpenfireService(SharedPreferencesHelper prefsHelper) {
        this.prefsHelper = prefsHelper;
    }

    public static OpenfireConnection.ConnectionState getState() {

        if (connectionState == null) {
            return OpenfireConnection.ConnectionState.DISCONNECTED;
        }

        return connectionState;
    }

    public static OpenfireConnection.LoggedInState getLoggedInState() {

        if (loggedInState == null) {
            return OpenfireConnection.LoggedInState.LOGGED_OUT;
        }

        return loggedInState;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    private void connectToOpenfire() {
        Log.d(TAG, "connectToOpenfire");

        if (connection == null) {
            connection = new OpenfireConnection(this);
        }

        connection.connectToOpenfire(prefsHelper.getLogin(), prefsHelper.getPassword());
    }

    public void start() {
        Log.d(TAG," Service Start() function called.");

        if (!active) {
            active = true;

            if (thread == null || !thread.isAlive()) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        handler = new Handler();
                        connectToOpenfire();

                        Looper.loop();
                    }
                });

                thread.start();
            }
        }
    }

    public void stop() {
        Log.d(TAG,"stop()");

        active = false;

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand()");
        start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy()");
        super.onDestroy();
        stop();
    }
}
