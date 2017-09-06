package com.easted.sy.user.archieves.uaa.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManagementController {

    @RequestMapping(value = "/management")
    public String management(){
        return "management";
    }
}
