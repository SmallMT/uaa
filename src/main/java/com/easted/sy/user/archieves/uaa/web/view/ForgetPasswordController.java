package com.easted.sy.user.archieves.uaa.web.view;


import com.easted.sy.user.archieves.uaa.domain.User;
import com.easted.sy.user.archieves.uaa.service.MailService;
import com.easted.sy.user.archieves.uaa.service.UserService;
import com.easted.sy.user.archieves.uaa.web.rest.vm.ForgetPasswordVM;
import com.easted.sy.user.archieves.uaa.web.rest.vm.KeyAndPasswordVM;
import com.easted.sy.user.archieves.uaa.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/forgetPassword")
public class ForgetPasswordController {


    @Autowired
    private UserService userService;


    @Autowired
    private MailService mailService;

    /**
     * 找回密码页面
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("forgetPasswordVM", new ForgetPasswordVM());
        return "forget_password";
    }

    /**
     * 处理找回密码邮件发送 post
     *
     * @param forgetPasswordVM
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/processSendEmail", method = RequestMethod.POST)
    public String processSendEmail(@Valid @ModelAttribute(name = "forgetPasswordVM") ForgetPasswordVM forgetPasswordVM, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.forgetPasswordVM", bindingResult);
            redirectAttributes.addFlashAttribute("forgetPasswordVM", forgetPasswordVM);
            return "redirect:/forgetPassword";
        }
        /*发送邮件*/
        String result;
        Optional<User> user=userService.requestPasswordReset(forgetPasswordVM.getEmail());
        if (user.isPresent()){
            mailService.sendPasswordResetMail(user.get());
            redirectAttributes.addFlashAttribute("result", "已发送到邮箱");

        }else {
            redirectAttributes.addFlashAttribute("result", "邮箱不存在");
        }
        return "redirect:/forgetPassword";


    }

    /**
     * 重置密码页面
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = "/resetPassword",method = RequestMethod.GET)
    public String resetPassword(String key,Model model){
        KeyAndPasswordVM keyAndPasswordVM=new KeyAndPasswordVM();
        keyAndPasswordVM.setKey(key);
        model.addAttribute("keyAndPasswordVM", keyAndPasswordVM);
        return "reset_password";

    }

    /**
     * 处理重置密码请求
     * @param keyAndPasswordVM
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/processResetPassword",method = RequestMethod.POST)
    public String processResetPassword(@Valid @ModelAttribute(name = "keyAndPasswordVM") KeyAndPasswordVM keyAndPasswordVM, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.keyAndPasswordVM", bindingResult);
            redirectAttributes.addFlashAttribute("keyAndPasswordVM", keyAndPasswordVM);
            return "redirect:/forgetPassword/resetPassword?key="+keyAndPasswordVM.getKey();
        }
        Optional<User> userOptional= userService.completePasswordReset(keyAndPasswordVM.getNewPassword(), keyAndPasswordVM.getKey());

        if (userOptional.isPresent()){
            redirectAttributes.addFlashAttribute("result", "重置密码成功");
        }else {
            redirectAttributes.addFlashAttribute("result", "重置密码失败");
        }
        return "redirect:/forgetPassword/resetPassword?key="+keyAndPasswordVM.getKey();


    }

}
