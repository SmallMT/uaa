package com.easted.sy.user.archieves.uaa.web.view;


import com.easted.sy.user.archieves.uaa.domain.ClientDetails;
import com.easted.sy.user.archieves.uaa.domain.RealName;
import com.easted.sy.user.archieves.uaa.repository.RealNameRepository;
import com.easted.sy.user.archieves.uaa.repository.UserRepository;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(value = "/realNameManagement")
public class RealNameManagementController {


    private RealNameRepository realNameRepository;
    private UserRepository userRepository;

    public RealNameManagementController(RealNameRepository realNameRepository, UserRepository userRepository) {
        this.realNameRepository = realNameRepository;
        this.userRepository = userRepository;
    }

    /**
     * 主页
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "realNameManagement/index";
    }

    /**
     * 获取所有数据信息
     * @param input
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/realNames", method = RequestMethod.POST)
    public DataTablesOutput<RealName> getAllClientDetails(@Valid @RequestBody DataTablesInput input) {
        return realNameRepository.findAll(input);

    }

    /**
     * 通过
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/pass")
    public ResponseEntity pass(@RequestBody Map map){
        realNameRepository.pass(Integer.parseInt(map.get("id").toString()));
        /*将结果回填到用户表*/
       RealName realName= realNameRepository.findOne(Integer.parseInt(map.get("id").toString()));
        userRepository.updateVerified(true,realName.getLogin(),realName.getName(),realName.getIdentity());
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 不通过
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/notPass")
    public ResponseEntity notPass(@RequestBody Map map){
        realNameRepository.notPass(Integer.parseInt(map.get("id").toString()));
        RealName realName= realNameRepository.findOne(Integer.parseInt(map.get("id").toString()));
        userRepository.updateVerified(false,realName.getLogin(),null,null);
        return new ResponseEntity(HttpStatus.OK);
    }


}
