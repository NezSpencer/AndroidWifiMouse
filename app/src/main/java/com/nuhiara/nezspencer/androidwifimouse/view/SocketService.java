package com.nuhiara.nezspencer.androidwifimouse.view;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.nuhiara.nezspencer.androidwifimouse.utility.Button;
import com.nuhiara.nezspencer.androidwifimouse.utility.Constants;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by nezspencer on 2/4/17.
 */

public class SocketService extends IntentService {

    private static final String TAG = SocketService.class.getName();

    public SocketService() {
        super("SocketService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Log.e(TAG, " inside service");
        int xyCoordinates[] = null;
        Button btn = null;
        if (intent.hasExtra(Constants.KEY_DATA)) {
            xyCoordinates = intent.getIntArrayExtra(Constants.KEY_DATA);
            Log.e(TAG, " " + xyCoordinates.length);
            Log.e(TAG, " coordinates gotten " + xyCoordinates[0] + "," + xyCoordinates[1]);
        } else if (intent.hasExtra(Constants.KEY_BUTTON)) {
            Log.e(TAG, "click sending to pc");
            btn = (Button) intent.getSerializableExtra(Constants.KEY_BUTTON);
        } else return;


        try {
            Log.e(TAG, " inside service " + GlobalVariables.appSocket.isConnected());
            PrintWriter printWriter = new PrintWriter(GlobalVariables.appSocket.getOutputStream(), true);
            if (xyCoordinates != null) {
                int xCoord = xyCoordinates[0];
                int yCoord = xyCoordinates[1];

                Log.e(TAG, " sending to pc");
                printWriter.write("" + xCoord + "_" + yCoord + "\n");
                printWriter.flush();
                Log.e(TAG, " sent to pc");
            } else if (btn != null) {
                Log.e(TAG, "click sending to pc");
                printWriter.write(btn.getValue() + "\n");
                printWriter.flush();
                Log.e(TAG, " click sent to pc");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
