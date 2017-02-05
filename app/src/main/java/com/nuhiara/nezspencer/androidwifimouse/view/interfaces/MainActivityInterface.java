package com.nuhiara.nezspencer.androidwifimouse.view.interfaces;

/**
 * Created by nezspencer on 1/20/17.
 */

public interface MainActivityInterface {


    void showLoadingProgress();

    void stopLoadingProgress();

    String getIPaddress();

    int getPortNumber();

    void showPortNumberError(int resID);

    void showIPaddressError(int resID);

    void startMouseActivity();

    void showSuccessMessage(String message);
}
