package com.easted.sy.user.archieves.uaa.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * OAuth 客户端管理
 */

@Controller
@RequestMapping(value = "clientManagement")
public class ClientManagementController {

    @RequestMapping()
    public String clientManagement(){
        return "clientManagement/index";
    }


    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String add(){
        return "clientManagement/add";
    }

}
