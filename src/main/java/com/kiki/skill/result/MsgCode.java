package com.kiki.skill.result;

import lombok.Getter;

/**
 * 消息类
 * @author kiki
 * @time 29/11
 */
@Getter
public class MsgCode {
    private int code;
    private String msg;
    //通用异常 5001xx
    public static MsgCode SUCCESS = new MsgCode(0,"success");
    public static MsgCode EMPTY = new MsgCode(203,"无数据");
    public static MsgCode SERVER_ERROR = new MsgCode(500100,"服务端异常哦！");
    public static MsgCode BIND_ERROR = new MsgCode(500101,"参数效验错误:%s");
    //登录模块异常 5002xx
    public static MsgCode SESSION_EMPTY = new MsgCode(500210,"Session不存在或者已经失效");
    public static MsgCode PASSWORD_EMPTY = new MsgCode(500211,"密码不能为空");
    public static MsgCode PASSWORD_ERROR = new MsgCode(500212,"密码错误");
    public static MsgCode MOBILE_EMPTY = new MsgCode(500213,"手机号不能为空");
    public static MsgCode MOBILE_ERROR = new MsgCode(500214,"手机号错误");
    //商品模块 5003xx

    //订单模块 5004xx
    public static MsgCode ORDER_NOT_EXIST = new MsgCode(500410,"订单不存在");

    //秒杀模块 5005xx
    public static MsgCode MIAO_SHA_OVER = new MsgCode(500510,"秒杀已经结束");
    public static MsgCode REPECT_MIAOSHA = new MsgCode(500214,"不能重复秒杀");

    public MsgCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    //返回一个带参数的验证码
    public MsgCode findArgs(Object... args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new MsgCode(code,message);
    }
}
