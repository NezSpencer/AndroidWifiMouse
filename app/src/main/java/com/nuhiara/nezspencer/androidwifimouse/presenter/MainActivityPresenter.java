package com.nuhiara.nezspencer.androidwifimouse.presenter;

import com.nuhiara.nezspencer.androidwifimouse.GlobalVariables;
import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.view.interfaces.MainActivityInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by nezspencer on 1/20/17.
 */

public class MainActivityPresenter {

    private Thread socketThread;
    private ClientSocket clientSocket;
    private static MainActivityInterface mainActivityInterface;
    public MainActivityPresenter(MainActivityInterface mInterface) {
        mainActivityInterface=mInterface;
    }



    public void onConnectButtonClicked() {
        String serverIP= mainActivityInterface.getIPaddress();
        int portNo= mainActivityInterface.getPortNumber();

        if (serverIP.equalsIgnoreCase(""))
            giveEmptyServerIpError();
        else if (portNo==0)
            giveEmptyPortNumberError();
        else {

            proceedToConnect(serverIP,portNo);
        }
    }

    private void giveEmptyPortNumberError() {
        mainActivityInterface.showPortNumberError(R.string.empty_port_number_error);
    }

    private void giveSuccessmsgFromServer(String msg){
        mainActivityInterface.showSuccessMessage(msg);
        mainActivityInterface.startMouseActivity();
    }

    private void proceedToConnect(final String serverIp, final int portNumber) {

        //write code to connect on success call: startMouseActivity()
        socketThread=new Thread(new Runnable() {
            @Override
            public void run() {
                clientSocket=new ClientSocket(serverIp,portNumber);
                clientSocket.createConnection();
            }
        });
        socketThread.start();
    }

    private void giveEmptyServerIpError() {
        mainActivityInterface.showIPaddressError(R.string.ip_address_empty_error);
    }

   private class ClientSocket {
        String ipAddress;
        int portNumber;
        private Socket socket;
        private BufferedReader reader;

        public ClientSocket(String ipAddress, int portNumber){
            this.ipAddress=ipAddress;
            this.portNumber=portNumber;
        }

        public void createConnection()
        {
            try {
                socket=new Socket(ipAddress,portNumber);
                reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                GlobalVariables.setAppSocket(socket);


                String fromServer=null;

                while ((fromServer=reader.readLine())!=null)
                {
                    if (fromServer.equalsIgnoreCase("connection successful"))
                    {
                        giveSuccessmsgFromServer(fromServer);
                        break;
                    }

                }

            }
            catch (IOException ex){
                ex.printStackTrace();
            }
            finally {
                try {
                    if (socket!=null)
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
