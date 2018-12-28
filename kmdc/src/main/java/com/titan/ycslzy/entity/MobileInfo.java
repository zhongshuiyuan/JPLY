package com.titan.ycslzy.entity;

import java.io.Serializable;

/**
 * 设备信息表
 * Created by li on 2017/10/17.
 */

public class MobileInfo implements Serializable {

    public String getSBH() {
        return SBH;
    }

    public void setSBH(String SBH) {
        this.SBH = SBH;
    }

    public String getSYZNAME() {
        return SYZNAME;
    }

    public void setSYZNAME(String SYZNAME) {
        this.SYZNAME = SYZNAME;
    }

    public String getSYZPHONE() {
        return SYZPHONE;
    }

    public void setSYZPHONE(String SYZPHONE) {
        this.SYZPHONE = SYZPHONE;
    }

    public String getDQXXID() {
        return DQXXID;
    }

    public void setDQXXID(String DQXXID) {
        this.DQXXID = DQXXID;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public String getDJTIME() {
        return DJTIME;
    }

    public void setDJTIME(String DJTIME) {
        this.DJTIME = DJTIME;
    }

    public String getSBMC() {
        return SBMC;
    }

    public void setSBMC(String SBMC) {
        this.SBMC = SBMC;
    }

    public String getXLH() {
        return XLH;
    }

    public void setXLH(String XLH) {
        this.XLH = XLH;
    }

    private String SBH;
    private String SYZNAME;
    private String SYZPHONE;
    private String DQXXID;
    private String DZ;
    private String DJTIME;
    private String SBMC;
    private String XLH;

    public MobileInfo(){

    }

    public MobileInfo(String SBH,String SYZNAME,String SYZPHONE,String DQXXID,String DZ,String DJTIME,String SBMC,String XLH){
        this.SBH = SBH;
        this.SYZNAME = SYZNAME;
        this.SYZPHONE = SYZPHONE;
        this.DQXXID = DQXXID;
        this.DZ = DZ;
        this.DJTIME = DJTIME;
        this.SBMC = SBMC;
        this.XLH = XLH;
    }

}
