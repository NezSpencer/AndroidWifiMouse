package com.nuhiara.nezspencer.androidwifimouse.viewmodel;

import com.nuhiara.nezspencer.androidwifimouse.utility.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityViewModelTest {

    @Rule
    public TestRule testRule = new InstantTaskExecutorRule();
    private MainActivityViewModel mainActivityViewModel;
    private MutableLiveData<Status> connection;

    @Before
    public void setUp() {
        mainActivityViewModel = new MainActivityViewModel();
        connection = mainActivityViewModel.getConnectionStatus();
    }

    @Test
    public void showErrorWhenIpAddressFieldIsEmpty() {
        mainActivityViewModel.connect("", 1234);
        Assert.assertEquals(connection.getValue().getMsg(), "Ip Address is empty");

    }

    @Test
    public void showErrorWhenPortNumberIsZero() {
        mainActivityViewModel.connect("192.168.43.201", 0);
        Assert.assertEquals(connection.getValue().getMsg(), "port is empty");
    }

    @Test
    public void showLoadingWhenIpAndPortNumberAreCorrect() {
        mainActivityViewModel.connect("192.168.43.201", 3334);
        Assert.assertEquals(connection.getValue().getState(), Status.State.LOADING);
    }
}