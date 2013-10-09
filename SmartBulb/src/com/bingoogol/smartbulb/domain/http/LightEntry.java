package com.bingoogol.smartbulb.domain.http;

import java.io.Serializable;

/**
 * Light Attributes
 * 
 * @author 李斌
 */
public class LightEntry implements Serializable, Comparable<LightEntry> {

	private static final long serialVersionUID = 7312139389339796315L;

	// 灯的状态
	private State state;

	// 灯泡id
	private String id;

	// 类型
	private String type;

	// 灯泡名称
	private String name;

	private String modelid;

	private String swversion;

	public State getState() {
		if(state == null) {
			state = new State();
		}
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setSwversion(String swversion) {
		this.swversion = swversion;
	}

	@Override
	public int compareTo(LightEntry le) {
		int id1 = Integer.valueOf(this.id);
		int id2 = Integer.valueOf(le.getId());
		return id1 - id2;
	}
}
