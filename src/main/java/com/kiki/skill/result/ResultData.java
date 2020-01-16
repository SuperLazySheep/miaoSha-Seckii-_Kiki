package com.kiki.skill.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 接口返会值类
 * @author kiki
 * @time 29/11
 * @param <T>
 */
@Setter
@Getter
public class ResultData<T> implements Serializable {
    private int code;
    private String msg;
    private T data;
    //success
    private ResultData (T data){
        if(data == null){
            this.code = MsgCode.EMPTY.getCode();
            this.msg = MsgCode.EMPTY.getMsg();
        }else{
            this.code = MsgCode.SUCCESS.getCode();
            this.msg = MsgCode.SUCCESS.getMsg();
            this.data = data;
        }
    }
    //error
    public ResultData (MsgCode msgCode)
    {
        if(msgCode == null) {
            return;
        }
        this.code = msgCode.getCode();
        this.msg = msgCode.getMsg();
    }

    public ResultData (int code, String msg, T  data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 响应成功
     */
    public static <T> ResultData<T> success(T data) {
        return new ResultData<T> (data);
    }

    /**
     * 响应失败
     */
    public static <T> ResultData<T> error(MsgCode msgCode){
        return new ResultData<T> (msgCode);
    }

}
