package com.easted.sy.user.archieves.uaa.web.websocket;

import com.easted.sy.user.archieves.uaa.event.WeChatBindEvent;
import com.easted.sy.user.archieves.uaa.web.websocket.dto.ActivityDTO;
import com.easted.sy.user.archieves.uaa.web.websocket.dto.WeChatDTO;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.logging.Logger;

/**
 * 监听WeChatBindEvent事件
 */
@Controller
public class WeChatSocketService implements ApplicationListener<WeChatBindEvent>{

    private static final Logger logger=Logger.getLogger("WeChatSocketService");

    private final SimpMessageSendingOperations messagingTemplate;

    public WeChatSocketService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }



//    @SubscribeMapping("/topic/bindWechat")   //订阅路由
//    @SendTo("/topic/tracker")    //发送目标
//    public WeChatBindEvent sendBindEvent(@Payload WeChatDTO weChatDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal){
//
//        weChatDTO.setBinded(true);
//
//    }


    /**
     * 接收WeChatEvent 事件,如果已绑定，发送给客户端
     * @param event
     */
    @Override
    public void onApplicationEvent(WeChatBindEvent event) {
        logger.info(((WeChatDTO)event.getSource()).getNickName());
        messagingTemplate.convertAndSend("/topic/wechat",event.getSource());
    }
}
