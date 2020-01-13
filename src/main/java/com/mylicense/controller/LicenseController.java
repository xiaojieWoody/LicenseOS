package com.mylicense.controller;

import com.mylicense.common.ResMsg;
import com.mylicense.config.LicenseConfig;
import com.mylicense.license.param.LicenseCreatorParam;
import com.mylicense.license.param.LicenseParamBean;
import com.mylicense.service.ILicenseCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/server")
public class LicenseController {
    
    @Autowired
    private ILicenseCreateService licenseCreateService;

    /**
     * 浏览器下载生成的证书
     * @param param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/downloadLicense",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResMsg generateLicense(@RequestBody LicenseParamBean param) throws Exception {

        // 验证参数
        if(StringUtils.isEmpty(param.getExpiryTime()) || StringUtils.isEmpty(param.getMachineCode())) {
            return new ResMsg(-1, "fail", "请输入机器码和有效期", null);
        }

        // 获取生成License文件所需参数
        LicenseCreatorParam licenseParam = licenseCreateService.getLicenseParam(param);

        // 创建License文件
        boolean result = licenseCreateService.generateLicense(licenseParam);

        if(result){
            return new ResMsg(200, "success","",null);
        }else{
            return new ResMsg(-1, "fail","证书文件生成失败！",null);
        }
    }

    /**
     * 生成证书并存储到指定目录
     * @param param
     * @return
     */
    @RequestMapping(value = "/storeLicense", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResMsg generateLicenseToDir(@RequestBody LicenseParamBean param) throws Exception {
        // 验证参数
        if(StringUtils.isEmpty(param.getExpiryTime()) || StringUtils.isEmpty(param.getMachineCode())) {
            return new ResMsg(-1, "fail", "请输入机器码和有效期", null);
        }

        // 获取生成License文件所需参数
        LicenseCreatorParam licenseParam = licenseCreateService.getLicenseParam(param);

        // 创建License文件
        boolean result = licenseCreateService.generateLicenseToDir(licenseParam);

        if(result){
            return new ResMsg(200, "success","",null);
        }else{
            return new ResMsg(-1, "fail","证书文件生成失败！",null);
        }
    }

}
