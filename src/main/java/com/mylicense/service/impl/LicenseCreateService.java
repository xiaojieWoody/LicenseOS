package com.mylicense.service.impl;

import com.alibaba.fastjson.JSON;
import com.mylicense.common.SpringContextUtils;
import com.mylicense.config.LicenseConfig;
import com.mylicense.license.manager.CustomLicenseManager;
import com.mylicense.license.model.LicenseCheckModel;
import com.mylicense.license.param.CustomKeyStoreParam;
import com.mylicense.license.param.LicenseCreatorParam;
import com.mylicense.license.param.LicenseParamBean;
import com.mylicense.service.ILicenseCreateService;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Date;
import java.util.prefs.Preferences;

@Slf4j
@Service
public class LicenseCreateService implements ILicenseCreateService {

    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN");
    private final static String licensePrefix = "license";
    private final static String licenseSuffix = ".dat";

    private LicenseCreatorParam param;

    /**
     * 生成License文件存储在某个目录下
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public boolean generateLicenseToDir(LicenseCreatorParam param) throws Exception {
        this.param = param;

        try {
            // 证书生成参数 初始化 LicenseManager
            LicenseManager licenseManager = new CustomLicenseManager(initLicenseParam());
            // 证书正文信息
            LicenseContent licenseContent = initLicenseContent();
            // 生成License文件，存储在指定目录
            licenseManager.store(licenseContent, new File(param.getLicensePath() + File.separator + licensePrefix + licenseSuffix));
            return true;
        } catch (Exception e) {
            log.error(MessageFormat.format("证书生成失败：{0}",param),e);
            return false;
        }
    }

    /**
     * 通过浏览器下载License文件
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public boolean generateLicense(LicenseCreatorParam param) throws Exception {
        this.param = param;

        // 临时文件
        File f = null;
        OutputStream toClient = null;

        try {
            // 证书生成参数 初始化 LicenseManager
            LicenseManager licenseManager = new CustomLicenseManager(initLicenseParam());
            // 证书正文信息
            LicenseContent licenseContent = initLicenseContent();

            // 浏览器下载
            // 创建临时文件
            f = File.createTempFile(licensePrefix, licenseSuffix);
            // 证书存储到文件中
            licenseManager.store(licenseContent,f);
            byte[] bytes = Files.readAllBytes(f.toPath());
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.addHeader("Content-Disposition", "attachment;filename="  + licensePrefix + licenseSuffix);
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("*/*");
            toClient.write(bytes);
            toClient.flush();

            return true;
        } catch (Exception e) {
            log.error(MessageFormat.format("证书生成失败：{0}",param),e);
            return false;
        } finally {
            if(f != null) {
                f.deleteOnExit();
            }
            if(toClient != null) {
                toClient.close();
            }
        }
    }

    /**
     * 获取生成License文件所需参数
     * @param param
     * @return
     */
    @Override
    public LicenseCreatorParam getLicenseParam(LicenseParamBean param) {
        LicenseCreatorParam licenseCreatorParam = new LicenseCreatorParam();
        String machineCode = param.getMachineCode();
        // License硬件信息
        byte[] decode = Base64.getUrlDecoder().decode(machineCode);
        LicenseCheckModel licenseCheckModel = (LicenseCheckModel) JSON.parseObject(decode, LicenseCheckModel.class);

        // 客户服务器硬件信息
        licenseCreatorParam.setLicenseCheckModel(JSON.toJSONString(licenseCheckModel));
        // License其他信息
        LicenseConfig licenseConfig = SpringContextUtils.getBeanByClass(LicenseConfig.class);
        BeanUtils.copyProperties(licenseConfig, licenseCreatorParam);
        // License签发时间
        licenseCreatorParam.setIssuedTime(new Date());
        // License过期时间
        licenseCreatorParam.setExpiryTime(param.getExpiryTime());
        // License文件生成后存储位置
        if(!StringUtils.isEmpty(param.getLicensePath())) {
            licenseCreatorParam.setLicensePath(param.getLicensePath());
        }

        return licenseCreatorParam;
    }

    /**
     * 初始化证书生成参数
     * @return
     */
    private LicenseParam initLicenseParam() {

        // 用来存储少量数据
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);
        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());
        // 密钥存储参数
        KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseCreator.class
                ,param.getPrivateKeysStorePath()
                ,param.getPrivateAlias()
                ,param.getStorePass()
                ,param.getKeyPass());
        LicenseParam licenseParam = new DefaultLicenseParam(param.getSubject()
                ,preferences
                ,privateStoreParam
                ,cipherParam);
        return licenseParam;
    }

    /**
     * 设置证书生成正文信息
     * @return
     */
    private LicenseContent initLicenseContent() {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);

        licenseContent.setSubject(param.getSubject());
        licenseContent.setIssued(param.getIssuedTime());
        licenseContent.setNotBefore(param.getIssuedTime());
        licenseContent.setNotAfter(param.getExpiryTime());
        licenseContent.setConsumerType(param.getConsumerType());
        licenseContent.setConsumerAmount(param.getConsumerAmount());
        licenseContent.setInfo(param.getDescription());
        //扩展校验服务器硬件信息
        licenseContent.setExtra(param.getLicenseCheckModel());

        return licenseContent;
    }
}
