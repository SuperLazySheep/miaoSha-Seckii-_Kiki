package com.kiki.skill.controller;

import com.kiki.skill.result.MsgCode;
import com.kiki.skill.result.ResultData;
import com.kiki.skill.service.SkillUserService;
import com.kiki.skill.util.ValidatorUtil;
import com.kiki.skill.vo.LoginVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Api("登陆接口")
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    SkillUserService skillUserService;

    @RequestMapping("/to_login1")
    public String login(){
        return "login";
    }

    @RequestMapping("/to_login2")
    public String login2(){
        return "login2";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public ResultData<Boolean> doLogin(HttpServletResponse response, @Validated LoginVo loginVo){ // jsp303效验
//        String password = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        if(StringUtils.isEmpty(password)){
//            return ResultData.error(MsgCode.PASSWORD_EMPTY);
//        }
//        if(StringUtils.isEmpty(mobile)){
//            return ResultData.error(MsgCode.MOBILE_EMPTY);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return ResultData.error(MsgCode.MOBILE_ERROR);
//        }
        log.info(loginVo.toString());
        //登录
       skillUserService.login(response,loginVo);
       return ResultData.success(true);
    }
}
