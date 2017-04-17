package com.nuhiara.nezspencer.androidwifimouse.view;

import android.app.IntentService;
import android.content.Intent;

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


        double xyCoordinates[]=null;
        String btn =null;
        if (intent.hasExtra(Constants.KEY_DATA))
            xyCoordinates = intent.getDoubleArrayExtra(Constants.KEY_DATA);

        else if (intent.hasExtra(Constants.KEY_BUTTON))
            btn = intent.getStringExtra(Constants.KEY_BUTTON);

        try {
            PrintWriter printWriter = new PrintWriter(GlobalVariables.appSocket.getOutputStream(),true);
            if (xyCoordinates != null)
            {
                double xCoord = xyCoordinates[0];
                double yCoord = xyCoordinates[1];

                printWriter.write(""+xCoord+"_"+yCoord);
            }

            else if (btn != null)
            {
                printWriter.write(btn);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
