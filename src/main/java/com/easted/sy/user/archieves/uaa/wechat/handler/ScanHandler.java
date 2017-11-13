package com.easted.sy.user.archieves.uaa.wechat.handler;

import com.easted.sy.user.archieves.uaa.event.WeChatBindEvent;
import com.easted.sy.user.archieves.uaa.repository.UserRepository;
import com.easted.sy.user.archieves.uaa.web.websocket.dto.WeChatDTO;
import com.easted.sy.user.archieves.uaa.wechat.builder.TextBuilder;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 扫描二维码事件
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class ScanHandler extends AbstractHandler implements ApplicationContextAware {

    /**
     * 定义Spring上下文对象
     */
    private ApplicationContext m_applicationContext = null;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext
     * (org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext _applicationContext)
        throws BeansException {
        this.m_applicationContext = _applicationContext;

    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), null);

        /*扫描后获取 unionId*/
        String unionId= userWxInfo.getUnionId();

        /*登录用户名*/
        String login= wxMessage.getEventKey();

        /*设置微信信息*/
        userRepository.setWeChat(login,unionId);

        WeChatDTO weChatDTO=new WeChatDTO();
        weChatDTO.setBinded(true);
        weChatDTO.setNickName(userWxInfo.getNickname());

        /*设置绑定事件*/
        WeChatBindEvent weChatBindEvent=new WeChatBindEvent(weChatDTO);
        m_applicationContext.publishEvent(weChatBindEvent);

        return new TextBuilder().build("绑定成功",wxMessage,wxMpService);
    }
}
