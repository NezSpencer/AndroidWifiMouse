package com.nuhiara.nezspencer.androidwifimouse.utility;

public class Status {

    public static final Status Loading = new Status(State.LOADING);
    public static final Status Default = new Status(State.IDLE);
    public static Status error(String msg) {return new Status(State.ERROR, msg);}

    public static Status Ok(String msg) {
        return new Status(State.ERROR, msg);
    }

    public enum State {
        SUCCESS, ERROR, IDLE, LOADING
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
