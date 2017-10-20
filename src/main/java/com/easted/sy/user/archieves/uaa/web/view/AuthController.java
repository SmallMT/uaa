package com.easted.sy.user.archieves.uaa.web.view;

import com.easted.sy.user.archieves.uaa.domain.RealName;
import com.easted.sy.user.archieves.uaa.domain.User;
import com.easted.sy.user.archieves.uaa.repository.PersistentTokenRepository;
import com.easted.sy.user.archieves.uaa.repository.RealNameRepository;
import com.easted.sy.user.archieves.uaa.repository.UserRepository;
import com.easted.sy.user.archieves.uaa.service.MailService;
import com.easted.sy.user.archieves.uaa.service.UserService;
import com.easted.sy.user.archieves.uaa.web.rest.vm.ManagedUserVM;
import com.easted.sy.user.archieves.uaa.web.view.vm.RegisterVM;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import javafx.geometry.Pos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.jvm.hotspot.gc_implementation.parallelScavenge.PSOldGen;

import javax.imageio.ImageIO;
import javax.persistence.PreUpdate;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AuthController {

    private  PersistentTokenRepository persistentTokenRepository;
    private  MailService mailService;
    private  UserService userService;
    private  UserRepository userRepository;
    private DefaultKaptcha captchaProducer;
    private RealNameRepository realNameRepository;



    public AuthController(UserRepository userRepository,
                          UserService userService,
                          MailService mailService,
                          PersistentTokenRepository persistentTokenRepository,
                          DefaultKaptcha kaptcha,RealNameRepository realNameRepository) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.persistentTokenRepository = persistentTokenRepository;
        this.captchaProducer=kaptcha;
        this.realNameRepository=realNameRepository;
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

    /**
     * 注册界面
     * @param model 页面model
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register(Model model){

        if (!model.containsAttribute("registerVM")) {
            model.addAttribute("registerVM", new RegisterVM());
        }

        return "register";
    }

    /**
     * 注册处理
     * @param registerVM
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/processRegister",method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute(name = "registerVM")RegisterVM registerVM, BindingResult bindingResult,RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerVM", bindingResult);
            redirectAttributes.addFlashAttribute("registerVM",registerVM);
            return "redirect:/register";
        }

        /*检测账号是否被注册过*/
        String msg="";
        if (userRepository.findOneByLogin(registerVM.getLogin().toLowerCase()).isPresent()){
            msg="用户名已经存在";
        }
        else if (userRepository.findOneByEmail(registerVM.getEmail()).isPresent()){
            msg="邮箱已经存在";
        }else {
            User user = userService.createUser(
                registerVM.getLogin(),
                registerVM.getPassword(),
                null,
                null,
               registerVM.getEmail().toLowerCase(),
               null,
               null);

            mailService.sendActivationEmail(user);
            msg="注册完成";
        }
        redirectAttributes.addFlashAttribute("result",msg);
        redirectAttributes.addFlashAttribute("registerVM",registerVM);
        return "redirect:/register";

    };

    private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    /**
     * 激活界面
     * @return
     */
    @RequestMapping(value = "/activate",method = RequestMethod.GET)
    public String activate(){

        return  "activate";
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(Principal principal,Model model){
        Boolean isVerfied,telBind=false;
        User user=userRepository.findOneByLogin(principal.getName()).get();
        String verifiedResult,telResult;
        model.addAttribute("verifiedResult",user.getVerified());

        if (user.getVerified()==null){
            /*根据用户名查找实名认证信息*/
           RealName realName= realNameRepository.findByLogin(principal.getName());
           if (realName!=null){
               verifiedResult="您的实名认证信息正在被后台审合同,请耐心等候";
           }else {
               verifiedResult="未实名认证，点击进行实名认证";
           }
            isVerfied=false;
        }else {
            verifiedResult="已实名认证";
            isVerfied=true;
        }
        model.addAttribute("verifiedResult",verifiedResult);


        telResult=user.getTel()==null?"未绑定手机号码":"已绑定手机号码";
        telBind=user.getTel()==null?false:true;
        model.addAttribute("telResult",telResult);
        model.addAttribute("isHiden",isVerfied&&telBind);

        return "index";
    }

}
