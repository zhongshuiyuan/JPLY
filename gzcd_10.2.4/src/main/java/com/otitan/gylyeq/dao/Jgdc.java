package com.otitan.gylyeq.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "JGDC_TB".
 */
public class Jgdc
{

	private String OBJECTID;
	private String KZDH;
	/** Not-null value. */
	private String SZDM;
	private double XIONGJING;
	private double PJMSG;
	private double JGDM;
	private double GQZS;
	private double GQXJ;
	private String BEIZHU;

	public Jgdc()
	{
	}

	public Jgdc(String KZDH)
	{
		this.KZDH = KZDH;
	}

	public Jgdc(String OBJECTID, String KZDH, String SZDM, double XIONGJING,
			double PJMSG, double JGDM, double GQZS, double GQXJ, String BEIZHU)
	{
		this.OBJECTID = OBJECTID;
		this.KZDH = KZDH;
		this.SZDM = SZDM;
		this.XIONGJING = XIONGJING;
		this.PJMSG = PJMSG;
		this.JGDM = JGDM;
		this.GQZS = GQZS;
		this.GQXJ = GQXJ;
		this.BEIZHU = BEIZHU;
	}

	public String[] toStringArray()
	{
		String[] arrary = new String[]
		{ OBJECTID, KZDH, SZDM, XIONGJING + "", PJMSG + "", JGDM + "",
				GQZS + "", GQXJ + "", BEIZHU };
		return arrary;
	}

	public String getOBJECTID()
	{
		return OBJECTID;
	}

	public void setOBJECTID(String OBJECTID)
	{
		this.OBJECTID = OBJECTID;
	}

	public String getKZDH()
	{
		return KZDH;
	}

	public void setKZDH(String KZDH)
	{
		this.KZDH = KZDH;
	}

	/** Not-null value. */
	public String getSZDM()
	{
		return SZDM;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setSZDM(String SZDM)
	{
		this.SZDM = SZDM;
	}

	public double getXIONGJING()
	{
		return XIONGJING;
	}

	public void setXIONGJING(double XIONGJING)
	{
		this.XIONGJING = XIONGJING;
	}

	public double getPJMSG()
	{
		return PJMSG;
	}

	public void setPJMSG(double PJMSG)
	{
		this.PJMSG = PJMSG;
	}

	public double getJGDM()
	{
		return JGDM;
	}

	public void setJGDM(double JGDM)
	{
		this.JGDM = JGDM;
	}

	public double getGQZS()
	{
		return GQZS;
	}

	public void setGQZS(double GQZS)
	{
		this.GQZS = GQZS;
	}

	public double getGQXJ()
	{
		return GQXJ;
	}

	public void setGQXJ(double GQXJ)
	{
		this.GQXJ = GQXJ;
	}

	public String getBEIZHU()
	{
		return BEIZHU;
	}

	public void setBEIZHU(String BEIZHU)
	{
		this.BEIZHU = BEIZHU;
	}

}
