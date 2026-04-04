package com.chatchosim.dto;

public class ChatResponse {

    private boolean success;
    private String reply;

    public ChatResponse() {
    }

    public ChatResponse(boolean success, String reply) {
        this.success = success;
        this.reply = reply;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReply() {
        return reply;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}