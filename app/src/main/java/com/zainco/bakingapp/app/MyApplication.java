package com.zainco.bakingapp.app;

import android.app.Application;

import com.zainco.bakingapp.receiver.ConnectivityReceiver;

/**
 * Created by Zain on 15/12/2017.
 */

public class MyApplication extends Application {

    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static MyApplication getInstance() {

        if (instance == null)
            return new MyApplication();
        return instance;
    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
