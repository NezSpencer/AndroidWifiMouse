package com.nuhiara.nezspencer.androidwifimouse.view;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.nuhiara.nezspencer.androidwifimouse.GlobalVariables;
import com.nuhiara.nezspencer.androidwifimouse.utility.Constants;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by nezspencer on 2/4/17.
 */

public class SocketService extends IntentService {

    public SocketService() {
        super("SocketService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Log.e("LOGGER"," inside service");
        int xyCoordinates[]=null;
        String btn =null;
        if (intent.hasExtra(Constants.KEY_DATA))
        {
            xyCoordinates = intent.getIntArrayExtra(Constants.KEY_DATA);
            Log.e("LOGGER"," "+xyCoordinates.length);
            Log.e("LOGGER"," coordinates gotten "+xyCoordinates[0]+","+xyCoordinates[1]);
        }


        else if (intent.hasExtra(Constants.KEY_BUTTON))
        {
            Log.e("LOGGER","click sending to pc");
            btn = intent.getStringExtra(Constants.KEY_BUTTON);
        }


        try {
            Log.e("LOGGER"," inside service "+GlobalVariables.appSocket.isConnected());
            PrintWriter printWriter = new PrintWriter(GlobalVariables.appSocket.getOutputStream(),true);
            if (xyCoordinates != null)
            {
                int xCoord = xyCoordinates[0];
                int yCoord = xyCoordinates[1];

                Log.e("LOGGER"," sending to pc");
                printWriter.write(""+xCoord+"_"+yCoord+"\n");
                printWriter.flush();
                Log.e("LOGGER"," sent to pc");
            }

            else if (btn != null)
            {
                Log.e("LOGGER","click sending to pc");
                printWriter.write(btn+"\n");
                printWriter.flush();
                Log.e("LOGGER"," click sent to pc");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
