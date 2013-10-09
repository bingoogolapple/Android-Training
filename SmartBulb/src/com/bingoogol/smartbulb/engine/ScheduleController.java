package com.bingoogol.smartbulb.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bingoogol.smartbulb.domain.http.Command;
import com.bingoogol.smartbulb.domain.http.ScheduleEntry;
import com.bingoogol.smartbulb.domain.http.State;

/**
 * Schedule Api
 * 
 * @author USER
 */
public class ScheduleController {
	/**
	 * 获取被添加到桥接器的所有时间表（计划任务）列表
	 * 
	 * @return 返回所有时间表对象列表
	 */
	@SuppressWarnings("unchecked")
	public List<ScheduleEntry> getAllSchedules() {
		String url = "/schedules";
		String response = HueRestClient.getInstance().get(url);

		List<ScheduleEntry> allSchedules = new ArrayList<ScheduleEntry>();
		try {
			JSONObject jo = new JSONObject(response);
			Iterator<String> i = jo.keys();
			while (i.hasNext()) {
				ScheduleEntry schedule = new ScheduleEntry();
				String id = i.next();
				schedule.setId(id);
				JSONObject value = jo.getJSONObject(id);
				schedule.setName(value.getString("name"));
				allSchedules.add(schedule);
			}
		} catch (JSONException e) {
			Log.e("getAllSchedules()", e.getLocalizedMessage());
		}
		return allSchedules;
	}

	/**
	 * 创建一个新的时间表（计划任务）
	 * 
	 * @param schedule
	 *            要新添加的时间表对象
	 * @return 返回true表示创建时间表成功
	 */
	@SuppressWarnings("unchecked")
	public boolean createSchedule(ScheduleEntry schedule) {
		String url = "/schedules";
		String jsonBody = schedule.convertToJson();
		try {
			String response = HueRestClient.getInstance().post(url, jsonBody);
			JSONArray ja = new JSONArray(response);
			JSONObject jo = ja.getJSONObject(0);
			Iterator<String> i = jo.keys();
			if (i.hasNext()) {
				String key = i.next();
				if ("success".equals(key)) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.e("createSchedule()", e.getLocalizedMessage());
		}

		return false;
	}

	/**
	 * 获取指定的时间表的属性
	 * 
	 * @param id
	 *            指定时间表的id
	 * @return 返回包含指定时间表信息的Schedule实体对象
	 */
	public ScheduleEntry getScheduleAttributes(String id) {
		String url = "/schedules" + "/" + id;
		String response = HueRestClient.getInstance().get(url);
		ScheduleEntry schedule = null;
		try {
			JSONObject jsonSchedule = new JSONObject(response);
			if (jsonSchedule != null) {
				schedule = new ScheduleEntry();
				schedule.setName(jsonSchedule.getString("name"));
				schedule.setDescription(jsonSchedule.getString("description"));
				schedule.setTime(jsonSchedule.getString("time"));

				JSONObject jsonCommand = jsonSchedule.getJSONObject("command");
				if (jsonCommand != null) {
					Command command = new Command();
					command.setAddress(jsonCommand.getString("address"));
					command.setMethod(jsonCommand.getString("method"));

					JSONObject jsonBody = jsonCommand.getJSONObject("body");
					if (jsonBody != null) {
						State body = new State();
						body.setOn(jsonBody.getBoolean("on"));
						body.setBri(jsonBody.getInt("bri"));
						body.setHue(jsonBody.getLong("hue"));
						body.setCt(jsonBody.getLong("ct"));
						body.setSat(jsonBody.getInt("sat"));

						JSONArray xy = jsonBody.getJSONArray("xy");
						body.setXy(xy.getDouble(0), xy.getDouble(1));

						body.setEffect(jsonBody.getString("effect"));
						body.setAlert(jsonBody.getString("alert"));
						body.setColormode(jsonBody.getString("colormode"));
						command.setBody(body);
					}
					schedule.setCommand(command);
				}
			}
		} catch (Exception e) {
			Log.e("getScheduleAttributes()", e.getLocalizedMessage());
		}

		return schedule;
	}

	/**
	 * 设置指定的时间表的属性
	 * 
	 * @param schedule
	 *            要设置的时间表的属性值
	 * @return 返回true表示修改时间表属性成功
	 */
	@SuppressWarnings("unchecked")
	public boolean setScheduleAttributes(ScheduleEntry schedule) {
		String url = "/schedules" + "/" + schedule.getId();
		String jsonArgs = schedule.convertToJson();
		String response = HueRestClient.getInstance().put(url, jsonArgs);
		try {
			JSONArray ja = new JSONArray(response);
			JSONObject jo = ja.getJSONObject(0);
			Iterator<String> i = jo.keys();
			if (i.hasNext()) {
				String key = i.next();
				if ("success".equals(key)) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.e("setScheduleAttributes()", e.getLocalizedMessage());
		}

		return false;
	}

	/**
	 * 删除指定的时间表的属性
	 * 
	 * @param id
	 *            指定时间表的id
	 * @return 返回true表示删除时间表成功
	 */
	@SuppressWarnings("unchecked")
	public boolean deleteSchedule(String id) {
		String url = "/schedules" + "/" + id;
		String response = HueRestClient.getInstance().delete(url);
		try {
			JSONArray ja = new JSONArray(response);
			JSONObject jo = ja.getJSONObject(0);
			Iterator<String> i = jo.keys();
			if (i.hasNext()) {
				String key = i.next();
				if ("success".equals(key)) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.e("deleteSchedule()", e.getLocalizedMessage());
		}

		return false;
	}
}
