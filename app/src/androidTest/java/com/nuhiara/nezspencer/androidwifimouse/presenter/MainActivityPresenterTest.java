package com.nuhiara.nezspencer.androidwifimouse.presenter;

import com.nuhiara.nezspencer.androidwifimouse.view.MainActivity;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


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
}