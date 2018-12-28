package com.titan.ycslzy.entity;

import java.util.StringTokenizer;

/**
 * 巡检事件信息表
 * Created by li on 2017/10/18.
 */

public class SceneInfo implements java.io.Serializable {

    public String getXJ_SJMC() {
        return XJ_SJMC;
    }

    public void setXJ_SJMC(String XJ_SJMC) {
        this.XJ_SJMC = XJ_SJMC;
    }

    public String getXJ_SBBH() {
        return XJ_SBBH;
    }

    public void setXJ_SBBH(String XJ_SBBH) {
        this.XJ_SBBH = XJ_SBBH;
    }

    public String getXJ_ZPDZ() {
        return XJ_ZPDZ;
    }

    public void setXJ_ZPDZ(String XJ_ZPDZ) {
        this.XJ_ZPDZ = XJ_ZPDZ;
    }

    public String getXJ_MSXX() {
        return XJ_MSXX;
    }

    public void setXJ_MSXX(String XJ_MSXX) {
        this.XJ_MSXX = XJ_MSXX;
    }

    public String getXJ_SCRQ() {
        return XJ_SCRQ;
    }

    public void setXJ_SCRQ(String XJ_SCRQ) {
        this.XJ_SCRQ = XJ_SCRQ;
    }

    public String getXJ_JD() {
        return XJ_JD;
    }

    public void setXJ_JD(String XJ_JD) {
        this.XJ_JD = XJ_JD;
    }

    public String getXJ_WD() {
        return XJ_WD;
    }

    public void setXJ_WD(String XJ_WD) {
        this.XJ_WD = XJ_WD;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    private String XJ_SJMC;
    private String XJ_SBBH;
    private String XJ_ZPDZ;
    private String XJ_MSXX;
    private String XJ_SCRQ;
    private String XJ_JD;
    private String XJ_WD;
    private String REMARK;

    public SceneInfo(){

    }

    public SceneInfo(String XJ_SJMC,String XJ_SBBH,String XJ_ZPDZ,String XJ_MSXX,String XJ_SCRQ,String XJ_JD,String XJ_WD,String REMARK){
        this.XJ_SJMC = XJ_SJMC;
        this.XJ_SBBH = XJ_SBBH;
        this.XJ_ZPDZ = XJ_ZPDZ;
        this.XJ_MSXX = XJ_MSXX;
        this.XJ_SCRQ = XJ_SCRQ;
        this.XJ_JD = XJ_JD;
        this.XJ_WD = XJ_WD;
        this.REMARK = REMARK;
    }

}
