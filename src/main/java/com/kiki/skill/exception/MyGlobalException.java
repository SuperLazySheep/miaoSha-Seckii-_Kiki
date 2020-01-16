package com.kiki.skill.exception;

import com.kiki.skill.result.MsgCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义处理异常
 */
@Getter
@Setter
public class MyGlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private MsgCode cm;

    public MyGlobalException(MsgCode cm){
        super(cm.toString());
        this.cm = cm;
    }
}
