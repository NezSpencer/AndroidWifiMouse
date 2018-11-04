package com.nuhiara.nezspencer.androidwifimouse.utility;

public enum Button {
    LEFT("left"), RIGHT("right");
    private String value;

    Button(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
