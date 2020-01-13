package com.mylicense.license.manager;

import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 自定义LicenseManager
 * 增加额外的服务器硬件信息校验
 */
@Slf4j
public class CustomLicenseManager extends LicenseManager {

    public CustomLicenseManager() {
    }

    public CustomLicenseManager(LicenseParam param) {
        super(param);
    }

    /**
     * 重写create方法
     *
     * @param content
     * @param notary
     * @return
     * @throws Exception
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        initialize(content);

        // 自定义
        this.validateCreate(content);

        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    /**
     * 校验生成证书的参数信息
     *
     * @param content
     * @throws LicenseContentException
     */
    protected synchronized void validateCreate(final LicenseContent content) throws LicenseContentException {
        final Date now = new Date();
        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();
        if (null != notAfter && now.after(notAfter)) {
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final String consumerType = content.getConsumerType();
        if (null == consumerType) {
            throw new LicenseContentException("用户类型不能为空");
        }
    }
}
