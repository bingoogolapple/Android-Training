package com.bingoogol.smartbulb.domain.http;

import java.io.Serializable;

/**
 * State Of a light
 * 
 * @author USER
 */
public class State implements Serializable {

	private static final long serialVersionUID = -2054378391277793129L;

	// 表示灯的开关状态
	private boolean on = false;

	// 亮度 0-255
	private int bri = -1;

	// 色相
	private long hue = -1;

	// 饱和度
	private int sat = -1;

	// The first entry is the x coordinate and the second entry is the y
	// coordinate. Both x and y must be between 0 and 1.
	private double[] xy = new double[2];

	// 色温
	private long ct = -1;

	private String alert = ""; // none,selected

	private String effect = ""; // colorloop

	private long transitiontime = -1;

	private String colormode = "";

	public State() {
		this.xy[0] = -1f;
		this.xy[1] = -1f;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public int getBri() {
		return bri;
	}

	public void setBri(int bri) {
		this.bri = bri;
		if (bri > 0) {
			this.on = true;
		}
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

	public double[] getXy() {
		return xy;
	}

	public void setXy(double x, double y) {
		this.xy[0] = x;
		this.xy[1] = y;
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

	public String convertToJSON() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		sb.append("\"on\":");
		sb.append(this.on + ",");

		boolean needComma = false;
		if (this.bri >= 0) {
			sb.append("\"bri\":");
			sb.append(this.bri);
			needComma = true;
		}

		if (this.hue >= 0) {
			if (needComma) {
				sb.append(",");
			}
			sb.append("\"hue\":");
			sb.append(this.hue);
			needComma = true;
		}

		if (this.sat >= 0) {
			if (needComma) {
				sb.append(",");
			}
			sb.append("\"sat\":");
			sb.append(this.sat);
			needComma = true;
		}

		if (this.xy[0] >= 0 && this.xy[1] >= 0) {
			if (needComma) {
				sb.append(",");
			}
			sb.append("\"xy\":");
			sb.append("[" + this.xy[0] + "," + this.xy[1] + "]");
			needComma = true;
		}

		if (this.ct >= 0) {
			if (needComma) {
				sb.append(",");
			}
			sb.append("\"ct\":");
			sb.append(this.ct);
			needComma = true;
		}

		if (this.alert != null && !this.alert.equals("")) {
			if (needComma) {
				sb.append(",");
			}
			sb.append("\"alert\":");
			sb.append("\"" + this.alert + "\"");
			needComma = true;
		}

		if (this.effect != null && !this.effect.equals("")) {
			if (needComma) {
				sb.append(",");
			}
			sb.append("\"effect\":");
			sb.append("\"" + this.effect + "\"");
			needComma = true;
		}

		if (this.transitiontime >= 0) {
			if (needComma) {
				sb.append(",");
			}
			sb.append("\"transitiontime\":");
			sb.append(this.transitiontime);
		}

		sb.append("}");
		return sb.toString();
	}
}