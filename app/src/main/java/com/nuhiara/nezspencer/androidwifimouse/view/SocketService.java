package com.nuhiara.nezspencer.androidwifimouse.view;

import android.app.IntentService;
import android.content.Intent;

import com.nuhiara.nezspencer.androidwifimouse.utility.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by nezspencer on 2/4/17.
 */

public class SocketService extends IntentService {

    public SocketService() {
        super("SocketService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String ipAddress = intent.getStringExtra(Constants.KEY_IP);
        int portNumber = intent.getIntExtra(Constants.KEY_PORT,2222);


        try {
            Socket clientSocket=new Socket(ipAddress,portNumber);

            PrintWriter writer=new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader reader=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        }
        catch (IOException ex){

        }
    }
}
