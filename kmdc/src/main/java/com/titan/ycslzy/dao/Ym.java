package com.titan.ycslzy.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "YMDC_TB".
 */
public class Ym {

    /** Not-null value. */
    private String YDH="";
    private String YMBH="";
    /** Not-null value. */
    private String SZDM="";
    private double XIONGJING=0.0;
    private double PJMSG=0.0;
    /** Not-null value. */
    private String LMZL="";
    /** Not-null value. */
    private String LMLX="";
    /** Not-null value. */
    private String SSLC="";
    private String BEIZHU="";

    public Ym() {
    }

    public Ym(String YMBH) {
        this.YMBH = YMBH;
    }

    public Ym(String YDH, String YMBH, String SZDM, double XIONGJING, Double PJMSG, String LMZL, String LMLX, String SSLC, String BEIZHU) {
        this.YDH = YDH;
        this.YMBH = YMBH;
        this.SZDM = SZDM;
        this.XIONGJING = XIONGJING;
        this.PJMSG = PJMSG;
        this.LMZL = LMZL;
        this.LMLX = LMLX;
        this.SSLC = SSLC;
        this.BEIZHU = BEIZHU;
    }

    /** Not-null value. */
    public String getYDH() {
        return YDH;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setYDH(String YDH) {
        this.YDH = YDH;
    }

    public String getYMBH() {
        return YMBH;
    }

    public void setYMBH(String YMBH) {
        this.YMBH = YMBH;
    }

    /** Not-null value. */
    public String getSZDM() {
        return SZDM;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSZDM(String SZDM) {
        this.SZDM = SZDM;
    }

    public double getXIONGJING() {
        return XIONGJING;
    }

    public void setXIONGJING(double XIONGJING) {
        this.XIONGJING = XIONGJING;
    }

    public Double getPJMSG() {
        return PJMSG;
    }

    public void setPJMSG(Double PJMSG) {
        this.PJMSG = PJMSG;
    }

    /** Not-null value. */
    public String getLMZL() {
        return LMZL;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLMZL(String LMZL) {
        this.LMZL = LMZL;
    }

    /** Not-null value. */
    public String getLMLX() {
        return LMLX;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLMLX(String LMLX) {
        this.LMLX = LMLX;
    }

    /** Not-null value. */
    public String getSSLC() {
        return SSLC;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSSLC(String SSLC) {
        this.SSLC = SSLC;
    }

    public String getBEIZHU() {
        return BEIZHU;
    }

    public void setBEIZHU(String BEIZHU) {
        this.BEIZHU = BEIZHU;
    }

}
