package com.easted.sy.user.archieves.uaa.web.view;


import com.easted.sy.user.archieves.uaa.domain.User;
import com.easted.sy.user.archieves.uaa.repository.UserRepository;
import com.easted.sy.user.archieves.uaa.service.dto.UserDTO;
import com.easted.sy.user.archieves.uaa.service.mapper.UserMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(value = "userManagement")
public class UserManagementController {



    private UserRepository userRepository;

    private UserMapper userMapper;


    public UserManagementController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String userManagement(){
        return "userManagement/index";
    }

    /**
     * 数据
     * @param input
     * @return
     */
    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @ResponseBody
    public DataTablesOutput<UserDTO> test(@Valid @RequestBody DataTablesInput input){
       return userRepository.findAll(input, new Converter<User, UserDTO>() {
           @Override
           public UserDTO convert(User source) {
               return new UserDTO(source);
           }
       });

    }

    /**
     * 添加用户界面
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String add(){
        return "userManagement/add";
    }

    /**
     * 编辑用户名为login的用户界面
     * @return
     */
    @RequestMapping(value = "/edit/{login}",method = RequestMethod.GET)
    public String edit(@PathVariable("login") String login){
        return "userManagement/edit";
    }
}
