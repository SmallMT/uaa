package com.easted.sy.user.archieves.uaa.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing the user's key and password.
 */
public class KeyAndPasswordVM {

    @NotNull
    private String key;

    @NotNull
    @Size(min = 4,max = 50)
    private String newPassword;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
