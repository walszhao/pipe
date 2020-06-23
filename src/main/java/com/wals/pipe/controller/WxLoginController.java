package com.wals.pipe.controller;

import com.wals.pipe.service.UserService;
import com.wals.pipe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-14 21:35:00
 */

@RestController
@RequestMapping("/login")
public class WxLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/wxLogin")
    public Result wxLogin(@RequestBody String param) throws Exception{
        Result  result = userService.wxLogin(param);
        return result;
    }
}