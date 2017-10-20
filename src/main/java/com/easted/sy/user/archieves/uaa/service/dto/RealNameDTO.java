package com.easted.sy.user.archieves.uaa.service.dto;

public class RealNameDTO {

    private String login;

    private String identity;

    private String backFileURL;

    private String frontFileURL;

    private String selfieFileURL;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getBackFileURL() {
        return backFileURL;
    }

    public void setBackFileURL(String backFileURL) {
        this.backFileURL = backFileURL;
    }

    public String getFrontFileURL() {
        return frontFileURL;
    }

    public void setFrontFileURL(String frontFileURL) {
        this.frontFileURL = frontFileURL;
    }

    public String getSelfieFileURL() {
        return selfieFileURL;
    }

    public void setSelfieFileURL(String selfieFileURL) {
        this.selfieFileURL = selfieFileURL;
    }
}
