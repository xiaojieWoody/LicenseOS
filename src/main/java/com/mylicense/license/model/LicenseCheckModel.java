package com.mylicense.license.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class LicenseCheckModel implements Serializable {

    private static final long serialVersionUID = -31983343976980894L;

    /**
     * 可被允许的MAC地址
     */
    private List<String> macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

}
