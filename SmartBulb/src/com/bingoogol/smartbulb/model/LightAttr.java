package com.bingoogol.smartbulb.model;


public class LightAttr {
	private int id;
	private int tid;
	private String type;
	private String name;
	private String modelid;
	private String swversion;

	private int state;
	private int bri;
	private long hue;
	private int sat;
	private double xy_x;
	private double xy_y;
	private long ct;
	private String alert;
	private String effect;
	private long transitiontime;
	private String colormode;

	public LightAttr() {

	}

	public LightAttr(int id, int tid, String type, String name, String modelid,
			String swversion, int state, int bri, long hue, int sat,
			double xy_x, double xy_y, int ct, String alert, String effect,
			long transitiontime, String colormode) {
		this.id = id;
		this.tid = tid;
		this.type = type;
		this.name = name;
		this.modelid = modelid;
		this.swversion = swversion;
		this.state = state;
		this.bri = bri;
		this.hue = hue;
		this.sat = sat;
		this.xy_x = xy_x;
		this.xy_y = xy_y;
		this.ct = ct;
		this.alert = alert;
		this.effect = effect;
		this.transitiontime = transitiontime;
		this.colormode = colormode;
	}

	public LightAttr(int tid, String type, String name, String modelid,
			String swversion, int state, int bri, long hue, int sat,
			double xy_x, double xy_y, int ct, String alert, String effect,
			long transitiontime, String colormode) {
		this.tid = tid;
		this.type = type;
		this.name = name;
		this.modelid = modelid;
		this.swversion = swversion;
		this.state = state;
		this.bri = bri;
		this.hue = hue;
		this.sat = sat;
		this.xy_x = xy_x;
		this.xy_y = xy_y;
		this.ct = ct;
		this.alert = alert;
		this.effect = effect;
		this.transitiontime = transitiontime;
		this.colormode = colormode;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModelid() {
		return modelid;
	}

	public void setModelid(String modelid) {
		this.modelid = modelid;
	}

	public String getSwversion() {
		return swversion;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setSwversion(String swversion) {
		this.swversion = swversion;
	}

	public int getBri() {
		return bri;
	}

	public void setBri(int bri) {
		this.bri = bri;
	}

	public long getHue() {
		return hue;
	}

	public void setHue(long hue) {
		this.hue = hue;
	}

	public int getSat() {
		return sat;
	}

	public void setSat(int sat) {
		this.sat = sat;
	}

	public double getXy_x() {
		return xy_x;
	}

	public void setXy_x(double xy_x) {
		this.xy_x = xy_x;
	}

	public double getXy_y() {
		return xy_y;
	}

	public void setXy_y(double xy_y) {
		this.xy_y = xy_y;
	}

	public long getCt() {
		return ct;
	}

	public void setCt(long ct) {
		this.ct = ct;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public long getTransitiontime() {
		return transitiontime;
	}

	public void setTransitiontime(long transitiontime) {
		this.transitiontime = transitiontime;
	}

	public String getColormode() {
		return colormode;
	}

	public void setColormode(String colormode) {
		this.colormode = colormode;
	}

}
