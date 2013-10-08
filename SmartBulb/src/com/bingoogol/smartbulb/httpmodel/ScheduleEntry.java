package com.bingoogol.smartbulb.httpmodel;

import java.io.Serializable;

/**
 * Schedule
 * 
 * @author USER
 */
public class ScheduleEntry implements Serializable {
	private static final long serialVersionUID = -7505230515879074212L;

	private String id;

	private String name;

	private String description;

	private Command command;

	private String time; // YYYY-MM-ddTHH:mm:ss

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String convertToJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");

		boolean needComma = false;
		if (this.name != null && !this.name.equals("")) {
			buffer.append("\"name\":" + "\"" + this.name + "\"");
			needComma = true;
		}

		if (this.description != null && !this.description.equals("")) {
			if (needComma) {
				buffer.append(",");
			}
			buffer.append("\"description\":" + "\"" + this.description + "\"");
			needComma = true;
		}

		if (this.command != null && !this.command.equals("")) {
			if (needComma) {
				buffer.append(",");
			}
			buffer.append("\"command\":" + this.command.convertToJSON());
			needComma = true;
		}
		if (this.time != null) {
			if (needComma) {
				buffer.append(",");
			}
			buffer.append("\"time\":" + "\"" + this.time + "\"");
		}
		buffer.append("}");

		return buffer.toString();
	}
}
