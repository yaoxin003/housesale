package com.yx.house.service.apigateway.controller;

import com.yx.house.service.apigateway.common.ResultMsg;
import com.yx.house.service.apigateway.common.UserContext;
import com.yx.house.service.apigateway.helper.UserHelper;
import com.yx.house.service.apigateway.model.Agency;
import com.yx.house.service.apigateway.model.User;
import com.yx.house.service.apigateway.service.AccountService;
import com.yx.house.service.apigateway.service.AgencyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/24/12:33
 */
@Controller
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private AgencyService agencyService;

    //注册
    @RequestMapping(value="accounts/register",method = {RequestMethod.POST,RequestMethod.GET})
    public String accountsSubmit(User account, ModelMap modelMap){
        if( account==null || account.getName() == null){
            return "/user/accounts/register";
        }
        ResultMsg retMsg = UserHelper.validate(account);
        logger.debug("【retMsg=】" + retMsg);
        if(retMsg.isSuccess()){
            boolean exist = accountService.isExist(account.getEmail());
            logger.debug("【exist=】" + exist);
            if(!exist){
                accountService.addAccount(account);
                modelMap.put("email",account.getEmail());
                return "/user/accounts/registerSubmit";
            }else{
                retMsg.setErrorMsg("邮箱已注册");
                return "redirect:/accounts/register?" + retMsg.asUrlParams();
            }
        }else{
            return "redirect:/accounts/register?" + retMsg.asUrlParams();
        }
    }

    //登录
    @RequestMapping("accounts/signin")
    public String loginSubmit(HttpServletRequest req){
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if(username==null || password==null){
            req.setAttribute("target",req.getParameter("target"));
            return "/user/accounts/signin";
        }
        User user = accountService.auth(username,password);
        if(user==null){
            return "redirect:/accounts/signin?username=" + username + "&"
                    + ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        }else{
            UserContext.setUser(user);
            return StringUtils.isNotBlank(req.getParameter("target")) ?
                    "redirect:" + req.getParameter("target") : "redirect:/index";
        }
    }

    //退出
    @RequestMapping("accounts/logout")
    public String logout(HttpServletRequest request){
        User user = UserContext.getUser();
        logger.debug("【context.user=】" + user);
        if(user != null){
            accountService.logout(user.getToken());
        }
        return "redirect:/index";
    }

    //个人详情
    @RequestMapping("accounts/profile")
    public String profile(ModelMap modelMap){
        List<Agency> list = agencyService.getAllAgency();
        modelMap.addAttribute("agencyList",list);
        return "/user/accounts/profile";
    }

    @RequestMapping("accounts/profileSubmit")
    public String profileSubmit(HttpServletRequest request,User updateUser,ModelMap modelMap){
        if(updateUser != null){
            return "redirect:/accounts/profile?" +
                    ResultMsg.errorMsg("邮箱不能为空").asUrlParams();
        }
        User user = accountService.updateUser(updateUser);
        UserContext.setUser(updateUser);
        return "redirect:/accounts/profile?" +
                ResultMsg.successMsg("更新成功").asUrlParams();
    }



}
