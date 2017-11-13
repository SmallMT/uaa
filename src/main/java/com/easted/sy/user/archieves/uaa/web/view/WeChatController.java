package com.easted.sy.user.archieves.uaa.web.view;

import com.easted.sy.user.archieves.uaa.domain.User;
import com.easted.sy.user.archieves.uaa.repository.UserRepository;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;


/**
 * 微信
 */
@Controller
@RequestMapping(value = "/myAccount/WeChat")
public class WeChatController {


    @Autowired
    private WxMpService wxService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 绑定微信页面
     * @param login
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/bind/{login}")
    public String bind(@PathVariable("login")String login, Model model) throws WxErrorException {
        model.addAttribute("login",login);
       // model.addAttribute("imageURL",url);
        return "/myAccount/wechat/bind";
    }

    /**
     * 返回绑定临时二维码
     */
    @RequestMapping(value = "/bind/qrCode")
    public void qRCode(HttpServletResponse response, Principal principal){
        FileInputStream fis = null;
        response.setContentType("image/gif");
        try {
            OutputStream out = response.getOutputStream();
            WxMpQrcodeService wxMpQrcodeService= wxService.getQrcodeService();
            WxMpQrCodeTicket wxMpQrCodeTicket= wxMpQrcodeService.qrCodeCreateTmpTicket(principal.getName(),1000);
            File file= wxMpQrcodeService.qrCodePicture(wxMpQrCodeTicket);
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 解绑微信页面
     * @param login
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/unbind/{login}")
    public String unBind(@PathVariable("login")String login, Model model){
        model.addAttribute("login",login);
        //微信uniqueid
       User user= userRepository.findOneByLogin(login).get();
       model.addAttribute("wechat",user.getWeChat());
        return "/myAccount/wechat/unbind";
    }



}
