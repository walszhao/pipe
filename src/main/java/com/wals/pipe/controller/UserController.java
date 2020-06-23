package com.wals.pipe.controller;

import com.wals.pipe.entity.User;
import com.wals.pipe.service.UserService;
import com.wals.pipe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-06 08:54:00
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/updatePhone")
    public Result updatePhone(@RequestBody String param) {
        return userService.updatePhone(param);

    }

    @GetMapping("/get")
    public String get() {
        return "我爱刘飒";
    }


}