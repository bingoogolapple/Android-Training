package com.bingoogol.smartbulb.httpmodel;

import java.io.Serializable;

/**
 * Command
 * 
 * @author USER
 */
public class Command implements Serializable {
	private static final long serialVersionUID = 6910863015297435927L;

	private String address;

	private String method;

	private State body;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public State getBody() {
		return body;
	}

	public void setBody(State body) {
		this.body = body;
	}

	public String convertToJSON() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"address\":" + "\"" + this.address + "\",");
		buffer.append("\"method\":" + "\"" + this.method + "\",");
		buffer.append("\"body\":" + this.body.convertToJSON());
		buffer.append("}");
		return buffer.toString();
	}
}
