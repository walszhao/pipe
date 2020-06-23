package com.wals.pipe.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wals.pipe.contants.BaseConstants;
import com.wals.pipe.entity.AdminUser;
import com.wals.pipe.entity.User;
import com.wals.pipe.mapper.AdminUserMapper;
import com.wals.pipe.mapper.UserMapper;
import com.wals.pipe.utils.AES;
import com.wals.pipe.utils.HttpUtils;
import com.wals.pipe.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.xml.ws.Action;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-06 09:12:00
 */
@Service("userService")
@Slf4j
public class UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;


    public void insertUser(User user){
        log.info("开始创建");
        userMapper.insert(user);
    }



    public Result wxLogin(String param) throws  Exception{
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        try {
            JSONObject paramJson  = JSON.parseObject(param);
            String code = paramJson.get("code").toString();

            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("appid", BaseConstants.APP_ID);
            paramMap.put("secret", BaseConstants.APP_SECRET);
            paramMap.put("js_code", code);
            paramMap.put("grant_type","authorization_code");

            String result = HttpUtils.get(url, paramMap);

            JSONObject resultJson  =  JSON.parseObject(result);
            String openId = resultJson.getString("openid");
            String sessionKey = resultJson.getString("session_key");

            //根据open id 查用户

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("openid",openId);

            User queryUser = userMapper.selectOne(queryWrapper);

            Integer userId;


            if (queryUser != null){

                userId = queryUser.getId();

                //更新
                queryUser.setUpdateTime(new Date());
                queryUser.setSessionKey(sessionKey);
                userMapper.updateById(queryUser);
                if(!verifyPhoneNumber(queryUser)){
                    return Result.getInstance("loss_phone_number",false, userId);
                }

            }else{
                //插入
                User  user = new User();
                user.setOpenid(openId);
                user.setSessionKey(sessionKey);
                user.setCreateTime(new Date());
                userMapper.insert(user);
                userId = user.getId();
                return Result.getInstance("loss_phone_number",false, userId);
            }

            log.info(result);
            return Result.getInstance("成功",true,userId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getInstance("请求出错",false);
        }

    }

    private Boolean verifyPhoneNumber(User user){
        String phoneNumber = user.getPhoneNumber();
        if (StringUtils.isNotBlank(phoneNumber)){
            return true;
        }else{
            return false;
        }
    }


    public AdminUser login(String username, String password){
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password",password );

        return adminUserMapper.selectOne(queryWrapper);

    }

    public AdminUser getAdminUserInfo(int id){
        return adminUserMapper.selectById(id);

    }

    public Result updateAdminUser(AdminUser adminUser){
        adminUserMapper.updateById(adminUser);
        return Result.getInstance(true,true);
    }


    public Result updatePhone(String param) {
        // 获取用户手机号需进行解密操作

        Result result = new Result();
        JSONObject paramJson  = JSON.parseObject(param);
        String encryptedData =  paramJson.getString("encryptedData");
        String iv = paramJson.getString("iv");

        String userId = paramJson.getString("userId");

        User user = userMapper.selectById(userId);

        String sessionKey  = user.getSessionKey();

        try {
            byte[] resultByte = AES.decrypt(Base64.decodeBase64(encryptedData),
                    Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));

            if (null != resultByte && resultByte.length > 0) {

                String userInfoStr = new String(resultByte, "UTF-8");
                log.info(userInfoStr);

                JSONObject phoneJson  = JSON.parseObject(userInfoStr);
                user.setPhoneNumber(phoneJson.getString("purePhoneNumber"));

                userMapper.updateById(user);

                result.setSuccess(true);
                result.setMsg("更新成功");
            }else {
                result.setSuccess(false);
                result.setMsg("解密发生异常");
            }
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg("发生异常");
        }
        return  result;

    }
}