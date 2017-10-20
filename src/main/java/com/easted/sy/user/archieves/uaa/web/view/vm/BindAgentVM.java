package com.easted.sy.user.archieves.uaa.web.view.vm;

import javax.validation.constraints.NotNull;

public class BindAgentVM {

    @NotNull
    private String identity;

    @NotNull
    private String name;

    @NotNull
    private String tel;

    private String creditCode;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }
}
