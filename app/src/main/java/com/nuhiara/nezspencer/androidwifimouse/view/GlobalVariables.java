package com.nuhiara.nezspencer.androidwifimouse.view;

import android.app.Application;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by nezspencer on 2/4/17.
 */

public class GlobalVariables extends Application {

    public static Socket appSocket;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Socket getAppSocket() {
        return appSocket;
    }

    public static void setAppSocket(Socket socket){
        appSocket=socket;
    }

    public static void closeSocket()
    {
        try {
            if (appSocket!=null)
                appSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
