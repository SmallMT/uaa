package com.easted.sy.user.archieves.uaa.web.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easted.sy.user.archieves.uaa.domain.BindAgent;
import com.easted.sy.user.archieves.uaa.domain.BindEnterprise;
import com.easted.sy.user.archieves.uaa.domain.RealName;
import com.easted.sy.user.archieves.uaa.domain.User;
import com.easted.sy.user.archieves.uaa.repository.BindAgentRepository;
import com.easted.sy.user.archieves.uaa.repository.BindEnterpriseRepository;
import com.easted.sy.user.archieves.uaa.repository.RealNameRepository;
import com.easted.sy.user.archieves.uaa.repository.UserRepository;
import com.easted.sy.user.archieves.uaa.web.view.vm.*;
import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/myAccount")
public class MyAccountController {

    private Logger logger = Logger.getLogger(MyAccountController.class.getName());

    private UserRepository userRepository;
    private RealNameRepository realNameRepository;
    private BindEnterpriseRepository bindEnterpriseRepository;

    private BindAgentRepository bindAgentRepository;

    public MyAccountController(UserRepository userRepository, RealNameRepository realNameRepository, BindEnterpriseRepository bindEnterpriseRepository, BindAgentRepository bindAgentRepository) {
        this.userRepository = userRepository;
        this.realNameRepository = realNameRepository;
        this.bindEnterpriseRepository = bindEnterpriseRepository;
        this.bindAgentRepository = bindAgentRepository;
    }

    public static final MediaType JSON
        = MediaType.parse("application/json; charset=utf-8");

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model, Principal principal) {
        String login = principal.getName();

        User user = userRepository.findOneByLogin(login).get();
        Map map = new HashMap();
        map.put("login", login);
        map.put("password", user.getPassword());
        map.put("email", user.getEmail());
        map.put("name", user.getName());
        map.put("identity", user.getIdentity());
        map.put("tel", user.getTel());
        map.put("weChat",user.getWeChat());

        model.addAllAttributes(map);
        return "myAccount/index";
    }


    /**
     * 实名认证页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/realName", method = RequestMethod.GET)
    public String realName(Model model, Principal principal) {
        RealName realName = realNameRepository.findByLogin(principal.getName());

        if (!model.containsAttribute("realNameVM")) {
            RealNameVM realNameVM = new RealNameVM();
            if (realName != null) {
                if ("不通过".equals(realName.getState())) {
                    realNameVM.setId(realName.getId());
                }
            }
            model.addAttribute("realNameVM", realNameVM);
        }

        if (realName != null) {
            model.addAttribute("state", realName.getState() == null ? "未审核" : realName.getState());//不通过、通过、未审核
        } else {
            model.addAttribute("state", "未提交");//未提交

        }

        return "myAccount/realName";
    }

    /**
     * 绑定手机号码页面
     *
     * @return
     */
    @RequestMapping(value = "/bindTel", method = RequestMethod.GET)
    public String bindTel(Principal principal, Model model) {
        User user = userRepository.findOneByLogin(principal.getName()).get();
        if (user.getTel() != null) {
            model.addAttribute("bindTelResult", user.getTel());
        }

        if (!model.containsAttribute("bindTelVM")) {
            model.addAttribute("bindTelVM", new BindTelVM());
        }
        return "myAccount/bindTel";
    }


    /**
     * 获取短信验证码
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/bindTel/verificationCode", method = RequestMethod.POST)
    public HashMap getcode(@org.springframework.web.bind.annotation.RequestBody Map map) throws IOException {
        HashMap result = new HashMap();
        //请求地址
        String url = "https://api.leancloud.cn/1.1/requestSmsCode";

        //请求体参数
        JSONObject obj = new JSONObject();
        obj.put("mobilePhoneNumber", map.get("tel"));
        obj.put("name", "三亚市政务中心");
        obj.put("op", "手机绑定");
        obj.put("ttl", 1);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, obj.toJSONString());
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .header("X-LC-Id", "iej5bQfs0fnQ1jUiIoCRVNLS-gzGzoHsz")
            .addHeader("X-LC-Key", "YjEaXzNDJpha3MRlNleVxuDF")
            .addHeader("Content-Type", "application/json")
            .build();

        String bodyString = client.newCall(request).execute().body().string();
        JSONObject json = com.alibaba.fastjson.JSON.parseObject(bodyString);
        if (json == null) {
            result.put("error", "系统异常");
            result.put("success", false);
        } else if (json.containsKey("error")) {
            result.put("code", "" + json.get("code"));
            if (json.getString("code").equals("601")){
                result.put("error", "验证码发送请求已超过今日上限");
            }else {
                result.put("error", json.getString("error"));

            }
            result.put("success", false);
        } else {
            logger.info("[getcode] 获取短信验证码成功");
            result.put("success", true);
        }
        logger.info("[getcode] 退出方法");
        return result;
    }

    /**
     * 短信验证码验证
     *
     * @param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/bindTel/processVerifyCode", method = RequestMethod.POST)
    public String verifyCode(Principal principal,@Valid @ModelAttribute(name = "bindTelVM")BindTelVM bindTelVM, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        logger.info("[verifyCode] 进入方法");

        String msg;
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerVM", bindingResult);
            redirectAttributes.addFlashAttribute("bindTelVM",bindTelVM);
            return "redirect:/myAccount/bindTel/verificationCode";
        }

        //请求地址
        String url = "https://api.leancloud.cn/1.1/verifySmsCode/" + bindTelVM.getCode();
        logger.info("[verifyCode] 验证手机验证码地址为：" + url);


        JSONObject obj = new JSONObject();
        obj.put("mobilePhoneNumber", bindTelVM.getTel());

        /*http请求*/
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, obj.toJSONString());
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .header("X-LC-Id", "iej5bQfs0fnQ1jUiIoCRVNLS-gzGzoHsz")
            .addHeader("X-LC-Key", "YjEaXzNDJpha3MRlNleVxuDF")
            .addHeader("Content-Type", "application/json")
            .build();

        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();

        JSONObject json = com.alibaba.fastjson.JSON.parseObject(responseBody);
        if (json == null) {
            msg="绑定失败：系统异常";
        } else if (json.containsKey("error")) {
            msg="绑定失败：系统异常";
        } else {
            logger.info("[verifyCode] 短信验证码正确");
            msg="绑定成功";


            /*将手机号码写入数据库*/
            User user = userRepository.findOneByLogin(principal.getName()).get();
            user.setTel(bindTelVM.getTel());
            userRepository.save(user);

        }

        redirectAttributes.addFlashAttribute("result",msg);
        redirectAttributes.addFlashAttribute("bindTelVM",bindTelVM);
        return "redirect:/myAccount/bindTel";
    }


    ////////////////////////////////////////////////////绑定企业///////////////////////////////////////////////////

    /**
     * 绑定企业信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/bindEnterprise", method = RequestMethod.GET)
    public String bindEnterprise(Model model) {

        if (!model.containsAttribute("bindEnterpriseVM")) {
            model.addAttribute("bindEnterpriseVM", new BindEnterpriseVM());
        }
        return "myAccount/bindEnterprise";
    }


    /**
     * 处理关联企业操作
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/processBindEnterprise", method = RequestMethod.POST)
    public String processBindEnterprise(Principal principal, @Valid @ModelAttribute(name = "bindEnterpriseVM") BindEnterpriseVM bindEnterpriseVM, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        boolean isFound = true;

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bindEnterpriseVM", bindingResult);
            redirectAttributes.addFlashAttribute("bindEnterpriseVM", bindEnterpriseVM);
            return "redirect:/myAccount/bindEnterprise";
        }

        //如果没有实名认证
        User user = userRepository.findOneByLogin(principal.getName()).get();
        String creditcode, ename;
        if (!user.isVerified()) {
            redirectAttributes.addFlashAttribute("result", "您还没有进行实名认证");
            return "redirect:/myAccount/bindEnterprise";
        } else if (user.getTel() == null) {
            redirectAttributes.addFlashAttribute("result", "您还没有绑定电话号码");
            return "redirect:/myAccount/bindEnterprise";
        } else if (user.isVerified() && user.getTel() != null) {
            /*检测是否匹配*/
            JSONObject obj = new JSONObject();
            obj.put("id", user.getIdentity());
            /*http请求*/
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("id",user.getIdentity());
            RequestBody formBody = builder.build();

            Request request = new Request.Builder()
               // .url("http://199.224.20.101:8079/legalperinfo")
                .url("http://10.10.130.81:8079/legalperinfo")
                .post(formBody)
                .addHeader("Content-Type", "application/json")
                .build();

            Response response = client.newCall(request).execute();

            String responseBody = response.body().string();
            JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(responseBody);
            JSONArray jsonArray = jsonObject.getJSONArray("legalperinfos");

            if (jsonArray.size() != 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    creditcode = item.getString("Ecreditcode");
                    ename = item.getString("Ename");
                    /*判断当前用户是否已经被绑定*/
                    BindEnterprise bindedEnterprise = bindEnterpriseRepository.findBindEnterpriseByUserAndCreditCode(user, creditcode);
                    /*当前身份证号码应用的用户没有被绑定到当前企业*/
                    if (bindedEnterprise == null) {
                        // 将信息填写到数据库中
                        BindEnterprise bindEnterprise = new BindEnterprise();
                        bindEnterprise.setCreditCode(bindEnterpriseVM.getCreditCode());
                        bindEnterprise.setEnterpriseName(bindEnterpriseVM.getEnterpriseName());
                        bindEnterprise.setUser(userRepository.findOneByLogin(principal.getName()).get());
                        bindedEnterprise.setState("已验证");
                        bindedEnterprise.setLegalPerson(true);
                        bindEnterpriseRepository.save(bindEnterprise);
                        redirectAttributes.addFlashAttribute("result", "绑定成功");
                        return "redirect:/myAccount/bindEnterprise";
                    } else { //当前身份证号码关联的用户已经被绑定到当前企业
                        redirectAttributes.addFlashAttribute("result", "该企业已经被绑定，不能重复绑定");
                        return "redirect:/myAccount/bindEnterprise";
                    }
                }

            } else {   //法人对应的身份证号码没有对应的企业信息
                //
                BindEnterprise bindEnterprise=new BindEnterprise();

                bindEnterprise.setCreditCode(bindEnterpriseVM.getCreditCode());
                bindEnterprise.setEnterpriseName(bindEnterpriseVM.getEnterpriseName());
                bindEnterprise.setUser(userRepository.findOneByLogin(principal.getName()).get());
                bindEnterprise.setState("未验证");
                bindEnterpriseRepository.save(bindEnterprise);
                redirectAttributes.addFlashAttribute("result", "添加成功，绑定信息待认证");
                return "redirect:/myAccount/bindEnterprise";
            }
        }
        return "redirect:/myAccount/bindEnterprise";
    }



    /**
     * 查看该用户所绑定的企业
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/bindEnterprise/binded", method = RequestMethod.GET)
    public String bindedEnterprise(Model model, Principal principal) {
        User user = userRepository.findOneByLogin(principal.getName()).get();

        List<BindEnterprise> bindEnterpriseList = bindEnterpriseRepository.findBindEnterprisesByUser(user);
        model.addAttribute("bindEnterpriseList", bindEnterpriseList);
        return "myAccount/enterpriseBinded";
    }


    ////////////////////////////////////////解绑企业/////////////////////////////////////////////////////

    /**
     * 企业法人解绑已绑定的企业
     * @param map
     * @return
     */
    @RequestMapping(value = "/bindEnterprise/binded/processUnBind", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity processUnBind(@org.springframework.web.bind.annotation.RequestBody Map map) {
        String creditCode = map.get("creditCode").toString();
        bindEnterpriseRepository.deleteBindEnterpriseByCreditCode(creditCode);
        return new ResponseEntity(HttpStatus.OK);
    }


    ///////////////////////////////////////////////绑定代办人//////////////////////////////////////////////

    /**
     * 绑定代办人的页面
     *
     * @param creditCode
     * @return
     */
    @RequestMapping(value = "/bindEnterprise/binded/bindAgent/{creditCode}", method = RequestMethod.GET)
    public String bindAgent(Model model, @PathVariable("creditCode") String creditCode) {
        if (!model.containsAttribute("bindAgentVM")) {
            BindAgentVM bindAgentVM = new BindAgentVM();
            bindAgentVM.setCreditCode(creditCode);
            model.addAttribute("bindAgentVM", bindAgentVM);
        }
        return "myAccount/bindAgent";
    }

    /**
     * 已绑定办件人的页面
     *
     * @return
     */
    @RequestMapping(value = "/bindEnterprise/binded/bindAgent/{creditCode}/binded", method = RequestMethod.GET)
    public String agentBinded(Model model, Principal principal, @PathVariable("creditCode") String creditCode) {
        /*法人用户信息*/
        User user = userRepository.findOneByLogin(principal.getName()).get();
        /*该法人绑定的所有企业*/
        BindEnterprise bindEnterprise = bindEnterpriseRepository.findBindEnterpriseByUserAndCreditCode(user, creditCode);
        /*该企业绑定的办件人*/
        List<BindAgent> bindAgentList = bindAgentRepository.findBindAgentsByBindEnterprise(bindEnterprise);
        model.addAttribute("bindAgentList", bindAgentList);

        return "myAccount/agentBinded";
    }

    /**
     * 处理绑定办件人
     *
     * @return
     */
    @RequestMapping(value = "/processBindAgent", method = RequestMethod.POST)
    public String processBindAgent(Principal principal, @Valid @ModelAttribute(name = "bindAgentVM") BindAgentVM
        bindAgentVM, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        /*如果有错误，返回添加页面*/
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bindAgentVM", bindingResult);
            redirectAttributes.addFlashAttribute("bindAgentVM", bindAgentVM);
            return "redirect:/myAccount/bindEnterprise/binded/bindAgent/" + bindAgentVM.getCreditCode();
        }

        String result;

        /*根据身份证ID检测该用户是否存在*/

        /*被绑定人*/
        User user = userRepository.findUserByIdentity(bindAgentVM.getIdentity());

        /*法人*/
        User legalUser = userRepository.findOneByLogin(principal.getName()).get();
        /*被绑定的企业*/
        BindEnterprise bindEnterprise = bindEnterpriseRepository.findBindEnterpriseByUserAndCreditCode(legalUser, bindAgentVM.getCreditCode());

        if (user == null) { //没有找到用户或者没有进行实名认证
            result = "没有找到身份证绑定的用户信息，请保证用户已注册并且已经实名认证。";
            redirectAttributes.addFlashAttribute("bindAgentVM", bindAgentVM);
        } else if (user.getTel() == null) {  //没有绑定手机号码
            result = "该用户没有进行实名认证，请保证该用户已经实名认证。";
            redirectAttributes.addFlashAttribute("bindAgentVM", bindAgentVM);

        } else if (bindAgentRepository.findByUserAndBindEnterprise(user, bindEnterprise) != null) {
            result = "该身份证的用户已经被您绑定，不能重复绑定";
            redirectAttributes.addFlashAttribute("bindAgentVM", bindAgentVM);
        } else if (!user.getTel().equals(bindAgentVM.getTel())) {
            result = "手机号码不匹配。";
            redirectAttributes.addFlashAttribute("bindAgentVM", bindAgentVM);
        } else if (!user.getName().equals(bindAgentVM.getName())) {
            result = "姓名不匹配。";
            redirectAttributes.addFlashAttribute("bindAgentVM", bindAgentVM);
        } else { //将该用户设置为当前企业的经办人
//            User user1 = userRepository.findOneByLogin(principal.getName()).get();
//            userRepository.setMyEnterprise(user.getLogin(), user1.getEnterpriseName(), user1.getCreditCode(), false);
            BindAgent bindAgent = new BindAgent();
            bindAgent.setUser(user);
            bindAgent.setBindEnterprise(bindEnterprise);
            bindAgentRepository.save(bindAgent);
            result = "绑定办件人成功";
        }

        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/myAccount/bindEnterprise/binded/bindAgent/" + bindAgentVM.getCreditCode();

    }

    ///////////////////////////////////////////////解绑代办人//////////////////////////////////////////////

    /**
     * 处理解绑办件人
     *
     * @return
     */
    @RequestMapping(value = "/bindEnterprise/binded/bindAgent/{creditCode}/binded/processUnBindAgent", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity processUnBindAgent(@org.springframework.web.bind.annotation.RequestBody Map
                                                 map, @PathVariable("creditCode") String creditCode, Model model) {
        Integer id = Integer.parseInt(map.get("id").toString());
        model.addAttribute("creditCode", creditCode);
        bindAgentRepository.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }



}
