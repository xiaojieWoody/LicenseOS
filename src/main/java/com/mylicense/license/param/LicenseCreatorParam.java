package com.mylicense.license.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class LicenseCreatorParam implements Serializable {

    private static final long serialVersionUID = 4988225563911616338L;

    /**
     * 证书subject
     */
    private String subject;

    /**
     * 密钥别称
     */
    private String privateAlias;

    /**
     * 密钥密码（保密）
     */
    private String keyPass;

    /**
     * 访问秘钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    private String privateKeysStorePath;

    /**
     * 证书生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issuedTime = new Date();

    /**
     * 证书失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * 用户类型
     */
    private String consumerType = "user";

    /**
     * 用户数量
     */
    private Integer consumerAmount = 1;

    /**
     * 描述信息
     */
    private String description = "";

    /**
     * 额外的服务器硬件校验信息
     * 如果使用类的话，该类的包名会存入License文件中，
     * 那么，则客户端解密验证License时也需要相同包路径下的该类
     */
//    private LicenseCheckModel licenseCheckModel;
    private String licenseCheckModel;
}
