package com.easted.sy.user.archieves.uaa.web.view;

import com.easted.sy.user.archieves.uaa.domain.ClientDetails;
import com.easted.sy.user.archieves.uaa.repository.ClientDetailsRepository;
import com.easted.sy.user.archieves.uaa.service.util.RandomUtil;
import com.easted.sy.user.archieves.uaa.web.view.vm.RegisterVM;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

/**
 * OAuth 客户端管理
 */

@Controller
@RequestMapping(value = "clientManagement")
public class ClientManagementController {

    private ClientDetailsRepository clientDetailsRepository;

    public ClientManagementController(ClientDetailsRepository clientDetailsRepository) {
        this.clientDetailsRepository = clientDetailsRepository;
    }

    @RequestMapping()
    public String clientManagement() {
        return "clientManagement/index";
    }

    /**
     * 添加页面
     * @param model
     * @param result 失败信息
     * @return
     */

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model,@ModelAttribute("result") String result) {

        if (!model.containsAttribute("clientDetails")) {
            model.addAttribute("clientDetails", new ClientDetails());
        }
        model.addAttribute("result",result);
        return "clientManagement/add";
    }

    /**
     * 编辑页面
     * @param id
     * @param model
     * @param result
     * @return
     */
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, Model model, @ModelAttribute("result") String result){
      ClientDetails clientDetails=  clientDetailsRepository.findOne(id);
      model.addAttribute("clientDetails",clientDetails);
      return "clientManagement/edit";
    }



    /**
     * 处理添加 post请求
     *
     * @param clientDetails
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "addProcess", method = RequestMethod.POST)
    public String addProcess(@ModelAttribute(name = "clientDetails") @Valid ClientDetails clientDetails, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.clientDetails", bindingResult);
            redirectAttributes.addFlashAttribute("clientDetails", clientDetails);
            return "redirect:/clientManagement/add";
        }

//        检测是否app_id是否可用
        String msg = "";

        if (clientDetailsRepository.findAllByAppId(clientDetails.getAppId()).isPresent()) {
            msg = "app id 已经存在";
            redirectAttributes.addFlashAttribute("result", msg);
            redirectAttributes.addFlashAttribute("clientDetails", clientDetails);
            return "redirect:/clientManagement/add";
        } else {
            clientDetails.setAppSecret(RandomStringUtils.randomAlphanumeric(20));
            clientDetailsRepository.save(clientDetails);
            msg = "添加成功";
        }

        redirectAttributes.addFlashAttribute("result", msg);
        redirectAttributes.addFlashAttribute("clientDetails", clientDetails);
        return "redirect:/clientManagement";
    }


    /**
     * 处理编辑 post请求
     *
     * @param clientDetails
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "editProcess", method = RequestMethod.POST)
    public String editProcess(@ModelAttribute(name = "clientDetails") @Valid ClientDetails clientDetails, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.clientDetails", bindingResult);
            redirectAttributes.addFlashAttribute("clientDetails", clientDetails);
            return "redirect:/clientManagement/edit/"+clientDetails.getId();
        }
        clientDetails.setAppSecret(RandomStringUtils.randomAlphanumeric(20));
        clientDetailsRepository.save(clientDetails);

        redirectAttributes.addFlashAttribute("clientDetails", clientDetails);
        return "redirect:/clientManagement";
    }



    /**
     * 获取所有客户端的信息
     * @param input
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clientDetails", method = RequestMethod.POST)
    public DataTablesOutput<ClientDetails> getAllClientDetails(@Valid @RequestBody DataTablesInput input) {
        return clientDetailsRepository.findAll(input);

    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    public ResponseEntity delete(@RequestBody Map map){
        clientDetailsRepository.delete(Integer.parseInt(map.get("id").toString()));
        return new ResponseEntity(HttpStatus.OK);
    }


}
