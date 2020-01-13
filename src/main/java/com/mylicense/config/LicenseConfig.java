package com.mylicense.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "license")
public class LicenseConfig {

    private String privateKeysStorePath;
    private String subject;
    private String privateAlias;
    private String keyPass;
    private String storePass;
    private String consumerType;
    private String consumerAmount;
    private String description;

    /**
     * License 文件生成后存储路径
     */
    private String licensePath;

}
