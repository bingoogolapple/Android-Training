package com.bingoogol.smartbulb.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bingoogol.smartbulb.domain.http.GroupEntry;
import com.bingoogol.smartbulb.domain.http.State;

/**
 * Groups Api
 * 
 * @author USER
 */
public class GroupsController {
	/**
	 * 获取所有已被添加到桥接器的所有分组信息列表
	 * 
	 * @return 返回所有的分组信息，每个分组信息保护一个唯一id和分组名称
	 */
	@SuppressWarnings("unchecked")
	public List<GroupEntry> getAllGroups() {
		String url = "/groups";
		String response = HueRestClient.getInstance().get(url);

		List<GroupEntry> allGroups = new ArrayList<GroupEntry>();
		try {
			JSONObject jo = new JSONObject(response);
			Iterator<String> i = jo.keys();
			while (i.hasNext()) {
				GroupEntry group = new GroupEntry();
				String id = i.next();
				group.setId(id);
				JSONObject value = jo.getJSONObject(id);
				group.setName(value.getString("name"));
				allGroups.add(group);
			}
		} catch (JSONException e) {
			Log.e("getAllGroups()", e.getLocalizedMessage());
		}
		return allGroups;
	}

	/**
	 * This method is not supported in the 1.0 version of the API.
	 * 
	 */
	public void createGroup() {
		//
	}

	/**
	 * 获得指定分组的名称，电灯id列表等信息
	 * 
	 * @param id
	 *            分组id
	 */
	public GroupEntry getGroupAttributes(String id) {
		String url = "/groups" + "/" + id;
		String response = HueRestClient.getInstance().get(url);

		GroupEntry group = null;
		try {
			JSONObject jo = new JSONObject(response);
			if (jo != null) {
				group = new GroupEntry();
				group.setName(jo.getString("name"));

				JSONObject stateJo = jo.getJSONObject("action");
				if (stateJo != null) {
					State state = new State();
					state.setOn(stateJo.getBoolean("on"));
					state.setBri(stateJo.getInt("bri"));
					state.setHue(stateJo.getLong("hue"));
					state.setCt(stateJo.getLong("ct"));
					state.setSat(stateJo.getInt("sat"));

					JSONArray xy = stateJo.getJSONArray("xy");
					state.setXy(xy.getDouble(0), xy.getDouble(1));

					state.setEffect(stateJo.getString("effect"));
					state.setAlert(stateJo.getString("alert"));
					state.setColormode(stateJo.getString("colormode"));
					group.setAction(state);
				}

				JSONArray ja = jo.getJSONArray("lights");
				if (ja.length() > 0) {
					String[] lightIds = new String[ja.length()];
					for (int i = 0; i < ja.length(); i++) {
						lightIds[i] = ja.getString(i);
					}
					group.setLights(lightIds);
				}

			}
		} catch (JSONException e) {
			Log.e("getGroupAttributes()", e.getLocalizedMessage());
		}
		return group;
	}

	/**
	 * 允许用户更改指定分组的名称和电灯成员
	 * 
	 * @param group
	 * @return 返回true表示修改属性成功
	 */
	@SuppressWarnings("unchecked")
	public boolean setGroupAttributes(GroupEntry group) {
		String url = "/groups" + "/" + group.getId();
		String jsonBody = group.convertToJson();
		String response = HueRestClient.getInstance().put(url, jsonBody);

		try {
			JSONArray ja = new JSONArray(response);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				Iterator<String> iter = jo.keys();
				if (iter.hasNext()) {
					if (!"success".equals(iter.next())) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			Log.e("setGroupAttributes()", e.getLocalizedMessage());
		}
		return true;
	}

	/**
	 * 更改分组状态
	 * 
	 * @param id
	 * @param state
	 * @return 返回true表示修改属性成功
	 */
	@SuppressWarnings("unchecked")
	public boolean setGroupState(String id, State state) {
		String url = "/groups" + "/" + id + "/action";
		String jsonBody = state.convertToJSON();
		String response = HueRestClient.getInstance().put(url, jsonBody);

		try {
			JSONArray ja = new JSONArray(response);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				Iterator<String> iter = jo.keys();
				if (iter.hasNext()) {
					if (!"success".equals(iter.next())) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			Log.e("setGroupAttributes()", e.getLocalizedMessage());
		}
		return true;
	}

	/**
	 * This method is not supported in the 1.0 version of the API.
	 * 
	 * @param id
	 */
	public void deleteGroup(String id) {
		//
	}
}
