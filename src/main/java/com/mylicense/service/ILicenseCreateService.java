package com.mylicense.service;

import com.mylicense.license.param.LicenseCreatorParam;
import com.mylicense.license.param.LicenseParamBean;

public interface ILicenseCreateService {

    /**
     * 生成License文件存储在某个目录下
     * @param param
     * @return
     * @throws Exception
     */
    boolean generateLicenseToDir(LicenseCreatorParam param) throws Exception;

    /**
     * 通过浏览器下载License文件
     * @param param
     * @return
     * @throws Exception
     */
    boolean generateLicense(LicenseCreatorParam param) throws Exception;

    /**
     * 获取生成License文件所需参数
     * @param param
     * @return
     */
    LicenseCreatorParam getLicenseParam(LicenseParamBean param);
}
