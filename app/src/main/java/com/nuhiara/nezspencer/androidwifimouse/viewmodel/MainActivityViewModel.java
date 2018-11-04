package com.nuhiara.nezspencer.androidwifimouse.viewmodel;

import com.nuhiara.nezspencer.androidwifimouse.utility.Status;
import com.nuhiara.nezspencer.androidwifimouse.view.GlobalVariables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
        if (serverIp == null || serverIp.trim().equals(""))
            connectionStatus.postValue(Status.error("Ip Address is empty"));
        else if (port == 0)
            connectionStatus.postValue(Status.error("port is empty"));
        else {
            connectionStatus.setValue(Status.Loading);
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
                GlobalVariables.appSocket.connect(new InetSocketAddress(ipAddress, portNumber),
                        5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(GlobalVariables.appSocket.getInputStream()));

            String fromServer=reader.readLine();
            connectionStatus.postValue(Status.Ok(fromServer));

        }
        catch (IOException ex){
            ex.printStackTrace();
            connectionStatus.postValue(Status.error("Could not connect to server"));
        }
    }
}
