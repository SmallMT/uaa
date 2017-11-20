package com.easted.sy.user.archieves.uaa.web.view.vm;

import javax.validation.constraints.NotNull;

public class BindTelVM {
    @NotNull
    private String tel;
    @NotNull
    private String code;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
