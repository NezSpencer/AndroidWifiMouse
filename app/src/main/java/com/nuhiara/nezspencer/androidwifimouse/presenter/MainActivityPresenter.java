package com.nuhiara.nezspencer.androidwifimouse.presenter;

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

            proceedToConnect();
        }
    }

    private void giveEmptyPortNumberError() {
        mainActivityInterface.showPortNumberError(R.string.empty_port_number_error);
    }

    private void giveSuccessmsgFromServer(String msg){
        mainActivityInterface.showSuccessMessage(msg);
        proceedToConnect();
    }

    private void proceedToConnect() {

        //write code to connect on success call: startMouseActivity()
        mainActivityInterface.startMouseActivity();
    }

    private void giveEmptyServerIpError() {
        mainActivityInterface.showIPaddressError(R.string.ip_address_empty_error);
    }

    class ClientSocket {
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


                String fromServer=null;

                while ((fromServer=reader.readLine())!=null)
                {
                    giveSuccessmsgFromServer(fromServer);
                }

            }
            catch (IOException ex){

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
