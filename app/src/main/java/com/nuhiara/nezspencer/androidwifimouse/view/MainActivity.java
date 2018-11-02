package com.nuhiara.nezspencer.androidwifimouse.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.databinding.ActivityMainBinding;
import com.nuhiara.nezspencer.androidwifimouse.presenter.MainActivityPresenter;
import com.nuhiara.nezspencer.androidwifimouse.presenter.MainActivityViewModel;
import com.nuhiara.nezspencer.androidwifimouse.utility.Utils;
import com.nuhiara.nezspencer.androidwifimouse.view.interfaces.MainActivityInterface;

public class MainActivity extends AppCompatActivity {

    MainActivityPresenter presenter;
    private ProgressDialog progressDialog;
    private static final String SERVER_IP="192.168.43.201";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);
        MainActivityViewModel viewModel =

        Log.e("IPAddress is: ",Utils.getIPAddress(true));
        binding.editServerIp.setText(SERVER_IP);

        Log.e("IPAddress2 is ",getIPAddressOverWifi());



        presenter=new MainActivityPresenter(this);
    }

    @Override
    public String getIPaddress() {
        return binding.editServerIp.getText().toString();
    }

    @Override
    public void showSuccessMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getPortNumber() {
        if (TextUtils.isEmpty(binding.editPortNumber.getText().toString()))
            return 0;

        return Integer.parseInt(binding.editPortNumber.getText().toString());
    }

    @Override
    public void showPortNumberError(int resID) {
        binding.editPortNumber.setError(getString(resID));
    }

    @Override
    public void showIPaddressError(int resID) {
        binding.editServerIp.setError(getString(resID));
    }

    @Override
    public void showLoadingProgress() {
        progressDialog.show();
    }

    @Override
    public void stopLoadingProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void startMouseActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,Mouse.class));
            }
        });

    }

    public void connectSocket(View view){
        presenter.onConnectButtonClicked();
    }

    public  String getIPAddressOverWifi(){
        WifiManager manager=(WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        return Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
    }

}
