package com.mylicense.license.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LicenseParamBean implements Serializable {

    private static final long serialVersionUID = 7936955851459800436L;
    /**
     * 客户服务器机器码(加密)
     */
    private String machineCode;

    /**
     * 证书失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * License 文件生成存储路径
     */
    private String licensePath;
}
