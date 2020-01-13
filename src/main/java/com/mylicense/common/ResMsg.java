package com.mylicense.common;

import lombok.Data;

@Data
public class ResMsg {

    private Integer code;
    private String msg;
    private String errMsg;
    private Object res;

    public ResMsg(Integer code, String msg, String errMsg, Object res) {
        this.code = code;
        this.msg = msg;
        this.errMsg = errMsg;
        this.res = res;
    }
}
