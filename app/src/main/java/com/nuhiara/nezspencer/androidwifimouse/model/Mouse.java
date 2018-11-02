package com.nuhiara.nezspencer.androidwifimouse.model;

import android.util.Pair;

public class Mouse {
    public enum Button{
        LEFT("left"),RIGHT("right");
        private String value;
        Button(String value) {
            this.value = value;
        }
    }

    private Button buttonClicked;
    private Pair<Integer, Integer> coordinate;

    public Pair<Integer, Integer> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Pair<Integer, Integer> coordinate) {
        this.coordinate = coordinate;
    }

    public String getButtonClicked() {
        return buttonClicked.value;
    }

    public void setButtonClicked(Button buttonClicked) {
        this.buttonClicked = buttonClicked;
    }
}
