package com.wals.pipe.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wals.pipe.entity.AdminUser;
import com.wals.pipe.service.UserService;
import com.wals.pipe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-05-04 12:05:00
 */

@Controller
public class AdminController {


    @Autowired
    private UserService userService;


    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "newbee_mall_goods");
        return "admin/newbee_mall_goods";
    }

    @GetMapping({"/login"})
    public String login() {
            return "admin/login";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = userService.getAdminUserInfo(loginUserId);
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("username", adminUser.getUsername());
        request.setAttribute("nickName", adminUser.getNickName());
        request.setAttribute("id", adminUser.getId());
        return "admin/profile";
    }


    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        HttpSession session) {

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        AdminUser adminUser = userService.login(userName, password);
        if (adminUser != null) {
            session.setAttribute("loginUser", userName);
            session.setAttribute("loginUserId", adminUser.getId());
            session.setAttribute("nickName", adminUser.getNickName());

            //session过期时间设置为7200秒 即两小时
            session.setMaxInactiveInterval(60 * 60 * 2);
            return "redirect:/index";
        } else {
            session.setAttribute("errorMsg", "登陆失败!");
            return "admin/login";
        }
    }




    @PostMapping("/updateAdminUserInfo")
    @ResponseBody
    public Result updateAdminUserInfo(@RequestBody AdminUser adminUser){
        Result result = Result.getInstance();
        String originalPassword  = adminUser.getOriginalPassword();
        if (StringUtils.isNotBlank(originalPassword)){
            AdminUser adminUserDb = userService.getAdminUserInfo(adminUser.getId());
            String passwordDb  = adminUserDb.getPassword();
            if (passwordDb.equals(originalPassword)){
                adminUser.setPassword(adminUser.getNewPassword());
                result = userService.updateAdminUser(adminUser);
            }else{
                result.setSuccess(false);
                result.setMsg("原密码错误！");
            }
        }else{
            result = userService.updateAdminUser(adminUser);
        }
        return result;
    }
}