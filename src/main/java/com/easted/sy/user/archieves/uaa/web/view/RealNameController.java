package com.easted.sy.user.archieves.uaa.web.view;


import com.easted.sy.user.archieves.uaa.domain.RealName;
import com.easted.sy.user.archieves.uaa.repository.RealNameRepository;
import com.easted.sy.user.archieves.uaa.service.util.RandomUtil;
import com.easted.sy.user.archieves.uaa.tools.PassWordCreate;
import com.easted.sy.user.archieves.uaa.web.view.vm.RealNameVM;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping(value = "/myAccount/realName")
public class RealNameController {

//    @Autowired
//    private RealNameValidator realNameValidator;
//
//    @InitBinder
//    protected void initBinderFileBucket(WebDataBinder binder) {
//        binder.setValidator(realNameValidator);
//    }

    private RealNameRepository realNameRepository;

    public RealNameController(RealNameRepository realNameRepository) {
        this.realNameRepository = realNameRepository;
    }

    @Value("${realName.filePath}")
    private  String dirPath;

    /**
     * 处理实名认证post请求
     * @param realNameVM
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/realNameProcess")
    public String realName(@Valid @ModelAttribute(name = "realNameVM") RealNameVM realNameVM, Principal principal, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.realNameVM", bindingResult);
            redirectAttributes.addFlashAttribute("realNameVM",realNameVM);
            return "redirect:/myAccount/realName";
        }
        /*请求没 没毛病*/
        File dir=new File(dirPath+"//"+principal.getName());
        if (!dir.exists()){
            dir.mkdir();

        }

        try {
            File selfieFile,frontFile,backFile;

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Random random=new Random(100000);

            PassWordCreate passWordCreate=new PassWordCreate();
            selfieFile=new File(dirPath+"//"+principal.getName()+"//"+ simpleDateFormat.format(new Date())+passWordCreate.createPassWord(10)+realNameVM.getSelfieFile().getOriginalFilename());
            frontFile=new File(dirPath+"//"+principal.getName()+"//"+simpleDateFormat.format(new Date())+passWordCreate.createPassWord(10)+realNameVM.getFrontFile().getOriginalFilename());
            backFile=new File(dirPath+"//"+principal.getName()+"//"+simpleDateFormat.format(new Date())+passWordCreate.createPassWord(10)+realNameVM.getBackFile().getOriginalFilename());

            realNameVM.getSelfieFile().transferTo(selfieFile);
            realNameVM.getFrontFile().transferTo(frontFile);
            realNameVM.getBackFile().transferTo(backFile);

            RealName realName=new RealName();
            realName.setBackImage(backFile.getName());
            realName.setFrontImage(frontFile.getName());
            realName.setSelfieImage(selfieFile.getName());
            realName.setLogin(principal.getName());
            realName.setName(realNameVM.getName());
            realName.setIdentity(realNameVM.getIdentity());

            realNameRepository.save(realName);
            redirectAttributes.addAttribute("result","保存成功");

//            保存信息到数据库中
        } catch (IOException e) {
            redirectAttributes.addAttribute("result",e.getMessage());
            e.printStackTrace();
        }


        return "redirect:/myAccount/realName";
    }
}
