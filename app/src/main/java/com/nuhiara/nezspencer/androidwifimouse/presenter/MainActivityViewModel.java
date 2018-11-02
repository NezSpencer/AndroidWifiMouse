package com.nuhiara.nezspencer.androidwifimouse.presenter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import com.nuhiara.nezspencer.androidwifimouse.GlobalVariables;
import com.nuhiara.nezspencer.androidwifimouse.model.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Status> connectionStatus;
    private String ipAddress;
    private int portNumber;

    public MutableLiveData<Status> getConnectionStatus() {
        if (connectionStatus == null) {
            connectionStatus = new MutableLiveData<>();
            connectionStatus.setValue(Status.Default);
        }
        return connectionStatus;
    }

    public void setConnectionStatus(MutableLiveData<Status> connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public void connect(final String serverIp, final int port){
        if (TextUtils.isEmpty(serverIp))
            connectionStatus.postValue(Status.error("Ip Address is empty"));
        else if (port == 0)
            connectionStatus.postValue(Status.error("port is empty"));
        else {

            this.ipAddress = serverIp;
            this.portNumber = port;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    createConnection();
                }
            }).start();
        }
    }

    private void createConnection()
    {
        try {
            GlobalVariables.appSocket=new Socket(ipAddress,portNumber);
            if (!GlobalVariables.appSocket.isConnected())
                GlobalVariables.appSocket.connect(new InetSocketAddress(ipAddress,portNumber));
            BufferedReader reader = new BufferedReader(new InputStreamReader(GlobalVariables.appSocket.getInputStream()));

            String fromServer=reader.readLine();
            connectionStatus.postValue(Status.Ok);
            //giveSuccessmsgFromServer(fromServer);

        }
        catch (IOException ex){
            ex.printStackTrace();
            connectionStatus.postValue(Status.error("Could not connect to server"));
        }
    }
}
