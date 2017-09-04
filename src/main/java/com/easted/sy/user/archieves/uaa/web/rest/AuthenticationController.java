package com.easted.sy.user.archieves.uaa.web.rest;


import com.easted.sy.user.archieves.uaa.service.UserService;
import com.easted.sy.user.archieves.uaa.web.rest.vm.LoginVM;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    //TODO api 实现
    @RequestMapping(value = "api/authentication",method = RequestMethod.POST)
    public HttpEntity auth(LoginVM loginVM, HttpServletResponse response, HttpServletRequest request) throws AuthenticationException {

        HttpSession session=request.getSession();
       // session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!code.equals(loginVM.getCode())){
            throw  new AuthenticationException("验证码错误");
        }else {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(loginVM.getUsername(),loginVM.getPassword());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }


        return null;
    }
}
