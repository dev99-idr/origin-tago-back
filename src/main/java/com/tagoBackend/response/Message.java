package com.tagoBackend.response;

import lombok.Data;

@Data
public class Message {
    private StatusEnum status;
    private String message;
    private Object data;
    // private JSONObject data;

    public Message(){
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }
}