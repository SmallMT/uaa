package com.easted.sy.user.archieves.uaa.web.view;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "management/userManagement")
public class UserManagementController {

    @RequestMapping()
    public String userManagement(){
        return "userManagement";
    }
}
