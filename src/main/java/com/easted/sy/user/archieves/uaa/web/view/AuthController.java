package com.easted.sy.user.archieves.uaa.web.view;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private DefaultKaptcha captchaProducer;

    public AuthController(DefaultKaptcha kaptcha) {
        this.captchaProducer=kaptcha;
    }

    /**
     * 登录界面
     * @return
     */

    @RequestMapping(value = "/login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/randCode")
    public void getRandCode(HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();


        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();

        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        System.out.println("******************验证码是: " + code + "******************");

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(bi, "jpeg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
