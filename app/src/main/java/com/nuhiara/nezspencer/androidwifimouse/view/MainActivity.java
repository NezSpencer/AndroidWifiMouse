package com.nuhiara.nezspencer.androidwifimouse.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.view.interfaces.MainActivityInterface;

public class MainActivity extends AppCompatActivity implements MainActivityInterface{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void showLoadingProgress() {

    }

    @Override
    public void stopLoadingProgress() {

    }

    @Override
    public void showError(String error) {

    }
}
