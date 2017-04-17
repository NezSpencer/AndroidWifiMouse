package com.nuhiara.nezspencer.androidwifimouse.presenter;

import com.nuhiara.nezspencer.androidwifimouse.view.interfaces.MouseActivityContract;

/**
 * Created by nezspencer on 3/31/17.
 */

public class MousePresenter {

    private MouseActivityContract mouseActivityContract;
    private int phoneWidth;
    private int phoneHeight;
    private int pcWidth;
    private int pcHeight;

    public MousePresenter(MouseActivityContract contract) {

        mouseActivityContract=contract;
    }

    public void passPhoneandPcDimension(int androidwidth, int androidheight, int compWidth, int
            compHeight){
        phoneHeight=androidheight;
        phoneWidth=androidwidth;
        pcWidth=compWidth;
        pcHeight=compHeight;
    }

    public void calculateMovement(int x, int y)
    {

    }
}
