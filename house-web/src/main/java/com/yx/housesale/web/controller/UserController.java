package com.yx.housesale.web.controller;

import com.yx.housesale.biz.service.HouseService;
import com.yx.housesale.biz.service.UserService;
import com.yx.housesale.common.constant.HouseConstant;
import com.yx.housesale.common.model.House;
import com.yx.housesale.common.model.User;
import com.yx.housesale.common.page.PageData;
import com.yx.housesale.common.page.PageParam;
import com.yx.housesale.common.page.Pagination;
import com.yx.housesale.common.result.ResultMsg;
import com.yx.housesale.common.utils.HashUtils;
import com.yx.housesale.web.helper.UserHelper;
import lombok.extern.log4j.Log4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/07/18:10
 */
@Controller
@Log4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HouseService houseService;

   /**
       * @description: 用户注册：
       * 正常流程（一个事务）：1.用户信息验证，2.添加用户，3.发送email
       * @author:  YX
       * @date:    2020/01/08 12:40
       * @param: user
       * @param: modelMap
       * @return: java.lang.String
       */
    @RequestMapping(value = "account/register")
    public String userRegister(User user, ModelMap modelMap){
        log.debug("【user=】" + user);
        if(user == null || user.getName() == null) {//注册
            return "user/account/register";
        }
        ResultMsg resultMsg = UserHelper.validate(user);
        if(resultMsg.isSuccess() && userService.addAccount(user)){//注册成功
            modelMap.put("email",user.getEmail());
            return "user/account/registerSubmit";
        }else{//注册失败
            return "redirect:/account/register" + "?" + resultMsg.asUrlParams();
        }
    }

    @RequestMapping("account/verify")
    public String verify(String key){
        log.debug("【key=】" + key);
        boolean result = userService.enable(key);
        if(result){
            log.debug("【激活成功】");
            return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
        }else{
            log.debug("【激活失败】");
            return "redirect:/account/register?" + ResultMsg.errorMsg(
                    "激活失败，请确认链接是否过期");
        }
    }
    /**
        * @description: 鉴权
        * @author:  YX
        * @date:    2020/01/12 10:04
        * @param: user
    * @param: request
        * @return: java.lang.String
        * @throws: 
        */
    @RequestMapping("account/signin")
    public String signin(User user,String target,ModelMap modelMap,HttpServletRequest request){
        log.debug("【user=】" + user + "，【target=】" + target);
        modelMap.put("target",target);
        if(user==null || StringUtils.isBlank(user.getEmail())
                || StringUtils.isBlank(user.getPasswd())){
            return "user/account/signin";
        }
        User dbUser = userService.auth(user.getEmail(),user.getPasswd());
        if(dbUser == null){
            log.debug("鉴权失败");
            String targetURL = "target=" + (StringUtils.isNotBlank(target) ? target : "");
            return "redirect:/account/signin?"
                    + ResultMsg.errorMsg("用户名或密码错误").asUrlParams()
                    + "&" + targetURL;
        }else {
            log.debug("鉴权成功");
            HttpSession session = request.getSession(true);
            session.setAttribute(HouseConstant.SESSION_USER_ATTRIBUTE,dbUser);
            String resURL = StringUtils.isNotBlank(target) ? "redirect:"+target : "redirect:/account/profile";
            log.debug("【resURL=】" + resURL);
            return resURL;
        }
    }

    /**
        * @description: 登出
        * @author:  YX
        * @date:    2020/01/12 17:43
        * @param: request
        * @return: java.lang.String
        * @throws: 
        */
    @RequestMapping("account/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        session.invalidate();
        return "redirect:/account/signin";
    }

    //------------------------个人信息页-----------------------------
    /**
        * @description: 更新个人信息，然后查询更新结果放入session中。
        * @author:  YX
        * @date:    2020/01/13 11:00
        * @param: null
        * @return:
        * @throws:
        */
    @RequestMapping("account/profile")
    public String profile(HttpServletRequest request,User user){
        log.debug("【user=】" + user);
        if(user == null || user.getId() == null){
            return "user/account/profile";
        }
        int i = userService.updateUserById(user);//更新
        if(i == 1){
            User newUser = this.getOneUser(user);//查询新数据
            request.getSession(true).setAttribute(HouseConstant.SESSION_USER_ATTRIBUTE,newUser);
            return "redirect:/account/profile?" + ResultMsg.successMsg("更新信息成功").asUrlParams();
        }
        return "redirect:/account/profile?" + ResultMsg.successMsg("更新失败").asUrlParams();
    }
/**
    * @description: 密码加盐加密后更新
    * @author:  YX
    * @date:    2020/01/13 15:27
    * @param: user
    * @return: java.lang.String
    * @throws: 
    */
    @RequestMapping("account/changePasswd")
    public String changePasswd(User user){
        log.debug("【user=】" + user);
        if(user!=null || user.getCurrPasswd()!=null || user.getPasswd()!=null ||
                user.getConfirmPasswd()!=null || !user.getPasswd().equals(user.getConfirmPasswd()) ){
            User auth = userService.auth(user.getEmail(), user.getCurrPasswd());
            if(auth != null){
                user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
                int count = userService.updateUserById(user);
                if(count == 1){
                    return "redirect:/account/profile?" + ResultMsg.successMsg("更新密码成功").asUrlParams();
                }
            }
        }
        return "redirect:/account/profile?" + ResultMsg.successMsg("更新密码错误").asUrlParams();
    }

    private User getOneUser(User user){
        User resUser = null;
        User queryUser = this.buildEmailUser(user);
        List<User> users = userService.getUsers(queryUser);
        if(users != null && !users.isEmpty()){
            resUser = users.get(0);
        }
        log.debug("【resUser=】" + resUser);
        return resUser;
    }

    private User buildEmailUser(User user){
        User queryUser = new User();
        queryUser.setEmail(user.getEmail());
        return queryUser;
    }

    @RequestMapping("agency/agentList")
    public String agentList(Integer pageSize,Integer pageNum,User query,ModelMap modelMap){
        log.debug("【pageSize=】" + pageSize + "，【pageNum=】" + pageNum);
        log.debug("【query=】" + query );
        PageData<User> ps = userService.getAgentList(query, PageParam.build(pageSize,pageNum));
        modelMap.put("ps",ps);
        modelMap.put("vo",query);
        log.debug("【ps=】" + ps );
        log.debug("【query=】" + query );
        return "user/agent/agentList";
    }

    @RequestMapping("/agency/agentDetail")
    public String agentDetail(Integer userId,ModelMap modelMap){
        if(userId != null){
            User user = userService.getAgentById(userId);
            House paramHouse = new House();
            paramHouse.setUserId(userId.longValue());
            paramHouse.setJoinHouseUser(true);
            PageData<House> ps = houseService.getHouseList(paramHouse, PageParam.build(3, 1));
            modelMap.put("user",user);
            modelMap.put("ps",ps);
            log.debug("【ps=】" + ps );
            log.debug("【user=】" + user );
        }
        return "user/agent/agentDetail";
    }


}
