package com.easted.sy.user.archieves.uaa.web.websocket.dto;

public class WeChatDTO {
    private boolean binded;
    private String nickName;

    public boolean isBinded() {
        return binded;
    }

    public void setBinded(boolean binded) {
        this.binded = binded;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
