package com.bingoogol.smartbulb.domain.http;

import java.io.Serializable;

/**
 * Group Attributes
 * 
 * @author USER
 */
public class GroupEntry implements Serializable {

	private static final long serialVersionUID = -916823535023270322L;

	private String id;
	private String name;
	private String[] lights;
	private State action;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getLights() {
		return lights;
	}

	public void setLights(String[] lights) {
		this.lights = lights;
	}

	public State getAction() {
		return action;
	}

	public void setAction(State action) {
		this.action = action;
	}

	public String convertToJson() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"name\":");
		sb.append("\"" + this.name + "\"");

		if (this.lights != null && this.lights.length > 0) {
			sb.append("," + "\"lights\":");
			sb.append("[");
			for (int i = 0; i < this.lights.length; i++) {
				sb.append("\"" + this.lights[i] + "\"");
				if (i != this.lights.length - 1) {
					sb.append(",");
				}
			}
			sb.append("]");
		}
		sb.append("}");
		return sb.toString();
	}
}
