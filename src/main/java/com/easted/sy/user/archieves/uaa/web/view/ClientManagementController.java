package com.easted.sy.user.archieves.uaa.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * OAuth 客户端管理
 */

@Controller
@RequestMapping(value = "management/clientManagement")
public class ClientManagementController {

    @RequestMapping()
    public String clientManagement(){
        return "clientManagement";
    }

}
