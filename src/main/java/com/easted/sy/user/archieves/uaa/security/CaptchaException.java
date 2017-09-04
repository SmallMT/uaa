package com.easted.sy.user.archieves.uaa.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class CaptchaException extends AuthenticationServiceException {
    public CaptchaException(String msg) {
        super(msg);
    }

    public CaptchaException(String msg, Throwable t) {
        super(msg, t);
    }
}
