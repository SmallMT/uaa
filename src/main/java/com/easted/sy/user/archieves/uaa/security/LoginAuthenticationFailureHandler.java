package com.easted.sy.user.archieves.uaa.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//    public static final String CODE_ERROR_URL = "http://199.224.20.102:9090/login?code_error";
//    public static final String EXPIRED_URL = "http://199.224.20.102:9090/login?expired";
//    public static final String LOCKED_URL = "http://199.224.20.102:9090/login?locked";
//    public static final String DISABLED_URL = "http://199.224.20.102:9090/login?disabled";
//    public static final String NotActivated="http://199.224.20.102:9090/login?NotActivated";
//
//    public static final String PASS_ERROR_URL = "http://199.224.20.102:9090/login?pass_error";


    public static final String CODE_ERROR_URL = "http://localhost:9090/login?code_error";
    public static final String EXPIRED_URL = "http://localhost:9090/login?expired";
    public static final String LOCKED_URL = "http://localhost:9090/login?locked";
    public static final String DISABLED_URL = "http://localhost:9090/login?disabled";
    public static final String NotActivated="http://localhost:9090/login?NotActivated";

    public static final String PASS_ERROR_URL = "http://localhost:9090/login?pass_error";

    public LoginAuthenticationFailureHandler() {
    }

    public LoginAuthenticationFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
    }
}
