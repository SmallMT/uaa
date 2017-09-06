package com.easted.sy.user.archieves.uaa.web.view;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 新闻模块
 */

@Controller
@RequestMapping(value = "/news")
public class NewsController {


    @RequestMapping(method = RequestMethod.GET)
    public String newsPage(){
        return "newsPage";
    }

}
