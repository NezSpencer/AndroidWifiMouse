package com.nuhiara.nezspencer.androidwifimouse.presenter;

import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by nezspencer on 1/31/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class MainActivityPresenterTest {

    private MainActivityPresenter presenter;

    @Mock private MainActivity mView;


    @Before
    public void setUp() throws Exception{
        presenter=new MainActivityPresenter(mView);
    }


    @Test
    public void shouldShowErrorIfIPaddressFieldIsEmpty() throws Exception{
        when(mView.getIPaddress()).thenReturn("");
        presenter.onConnectButtonClicked();

        verify(mView).showIPaddressError(R.string.ip_address_empty_error);
    }

    @Test
    public void shouldShowErrorIfPortNUmberIsEmpty() throws Exception{
        when(mView.getIPaddress()).thenReturn("192.168.0.1");
        when(mView.getPortNumber()).thenReturn(0);

        presenter.onConnectButtonClicked();

        verify(mView).showPortNumberError(R.string.empty_port_number_error);

    }

    @Test
    public void shouldPassIfIPaddressAndPortNumberAreValid() throws Exception{
        when(mView.getIPaddress()).thenReturn("192.168.0.1");
        when(mView.getPortNumber()).thenReturn(8080);
        presenter.onConnectButtonClicked();
        presenter.giveSuccessmsgFromServer("Connection Successful");

        verify(mView).startMouseActivity();
    }

    /*@Test
    public void shouldShowSuccessMessageUponConnection() throws Exception{
        when(mView.getIPaddress()).thenReturn("192.168.0.1");
        when(mView.getPortNumber()).thenReturn(8080);
        presenter.onConnectButtonClicked();

        verify(mView).showSuccessMessage("success");
    }*/
}