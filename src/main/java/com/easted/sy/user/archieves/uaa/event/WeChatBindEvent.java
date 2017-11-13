package com.easted.sy.user.archieves.uaa.event;

import org.springframework.context.ApplicationEvent;


/**
 * 微信绑定成功事件
 */
public class WeChatBindEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public WeChatBindEvent(Object source) {
        super(source);
    }

}
