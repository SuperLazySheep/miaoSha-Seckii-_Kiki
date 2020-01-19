package com.kiki.skill.controller;

import com.kiki.skill.domain.User;
import com.kiki.skill.rabbitmq.MQSender;
import com.kiki.skill.redis.RedisService;
import com.kiki.skill.redis.UserKey;
import com.kiki.skill.result.MsgCode;
import com.kiki.skill.result.ResultData;
import com.kiki.skill.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  create TestClass
 * @Author KiKi(sonQQ)
 * @TIme 29/11
 */
@Api(value = "Test接口")
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQSender mqSender;

    @RequestMapping("/template")
   //@ResponseBody
    public String template(Model model){
        model.addAttribute("name","song");
        return "hello";
    }

    @RequestMapping("/success")
    @ResponseBody
    public ResultData<String> success(){
        return ResultData.success("我是你爸爸");
    }

    @RequestMapping("/error")
    @ResponseBody
    public ResultData<MsgCode> error(){
        return ResultData.success(MsgCode.SERVER_ERROR);
    }

    //测试druid连接数据库设置
    @RequestMapping("/user")
    @ResponseBody
    public ResultData<User> findUserById(){
        User user = userService.findUserById(1);
        return ResultData.success(user);
    }

    //测试redis连接设置
    @RequestMapping("/redis1")
    @ResponseBody
    public ResultData<User> getRedis1(){
        User user = redisService.get(UserKey.idKey, "1", User.class);
        return ResultData.success(user);
    }
    //测试redis连接设置
    @RequestMapping("/redis2")
    @ResponseBody
    public ResultData<Boolean> getRedis2(){
        User user = new User(16,"宋奇奇");
        boolean b = redisService.set(UserKey.idKey, "1", user);
        return ResultData.success(b);
    }

    //测试redis连接设置
    @RequestMapping("/del")
    @ResponseBody
    public ResultData<Long> getdel(){
//        Long del = redisService.del(UserKey.idKey, "1");
//        return ResultData.success(del);
        return null;
    }

    //测试redis连接设置
    @RequestMapping("/incr")
    @ResponseBody
    public ResultData<Long> getincr(){
        Long incr = redisService.incr(UserKey.idKey, "1");
        return ResultData.success(incr);
    }

    //测试redis连接设置
    @RequestMapping("/decr")
    @ResponseBody
    public ResultData<Long> getdecr(){
        Long decr = redisService.decr(UserKey.idKey, "1");
        return ResultData.success(decr);
    }

    //测试mq
//    @RequestMapping("/mq")
//    @ResponseBody
//    public ResultData getMQ(){
//        //mqSender.send("test mq data");
//       // mqSender.sendTopic("test  mq data");
//        //mqSender.sendFanout("TEst Data MQ");
//        mqSender.sendHeader("TEst Data MQ");
//        return ResultData.success("成功");
//    }
}
