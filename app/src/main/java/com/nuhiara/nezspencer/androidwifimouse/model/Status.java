package com.nuhiara.nezspencer.androidwifimouse.model;

public class Status {

    public static final Status Ok = new Status(State.SUCCESS);
    public static final Status Default = new Status(State.IDLE);
    public static Status error(String msg) {return new Status(State.ERROR, msg);}

    enum State {
        SUCCESS, ERROR, IDLE
    }

    private final String msg;
    private final State state;

    private Status(State state){
        this(state, null);
    }
    private Status(State state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public State getState() {
        return state;
    }
}
