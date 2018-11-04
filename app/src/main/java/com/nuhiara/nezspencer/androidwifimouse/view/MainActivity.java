package com.nuhiara.nezspencer.androidwifimouse.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.databinding.ActivityMainBinding;
import com.nuhiara.nezspencer.androidwifimouse.utility.Status;
import com.nuhiara.nezspencer.androidwifimouse.viewmodel.MainActivityViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static final String SERVER_IP="192.168.43.201";
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel
                .class);
        viewModel.getConnectionStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                switch (status.getState()) {
                    case IDLE:
                        stopLoadingProgress();
                        break;
                    case SUCCESS:
                        stopLoadingProgress();
                        Toast.makeText(MainActivity.this, status.getMsg(), Toast.LENGTH_LONG).show();
                        startMouseActivity();
                        break;
                    case ERROR:
                        stopLoadingProgress();
                        showErrorToast(status.getMsg());
                        break;
                    case LOADING:
                        showLoadingProgress();
                        break;
                }
            }
        });
        binding.editServerIp.setText(SERVER_IP);
    }

    public String getIPaddress() {
        return binding.editServerIp.getText().toString();
    }


    public int getPortNumber() {
        if (TextUtils.isEmpty(binding.editPortNumber.getText().toString()))
            return 0;

        return Integer.parseInt(binding.editPortNumber.getText().toString());
    }

    public void showPortNumberError(int resID) {
        binding.editPortNumber.setError(getString(resID));
    }

    public void showIPaddressError(int resID) {
        binding.editServerIp.setError(getString(resID));
    }

    public void showLoadingProgress() {
        progressDialog.show();
    }

    public void stopLoadingProgress() {
        progressDialog.dismiss();
    }

    public void startMouseActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, MouseActivity.class));
            }
        });
    }

    public void connectSocket(View view){
        viewModel.connect(getIPAddressOverWifi(), getPortNumber());
    }

    public  String getIPAddressOverWifi(){
        WifiManager manager=(WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
    }

    private void showErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
