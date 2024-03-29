package com.ext.huya.dto;

public class Command {

    private String command;
    private String[] data;
    private String reqId;

    public Command(String[] data) {
        this("subscribeNotice", data);
    }

    public Command(String command, String[] data) {
        this.command = command;
        this.data = data;
        insReqId();
    }

    private void insReqId() {
        this.reqId = "123456789";
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }
}
