package com.easted.sy.user.archieves.uaa.web.rest.vm;

import org.hibernate.validator.constraints.Email;

public class ForgetPasswordVM {

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
