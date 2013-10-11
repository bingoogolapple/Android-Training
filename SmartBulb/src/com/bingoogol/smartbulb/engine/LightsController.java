package com.bingoogol.smartbulb.engine;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.bingoogol.smartbulb.domain.http.LightEntry;
import com.bingoogol.smartbulb.domain.http.State;
import com.bingoogol.smartbulb.engine.LightHandler.LightCallback;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;

/**
 * Light控制器
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class LightsController {

	protected static final String TAG = "LightsController";

	/**
	 * 获取当前桥接器链接的所有灯的信息，每个点灯信息包括一个唯一id和指定的名称
	 * 
	 * @param lightCallback
	 *            操作灯泡的回调接口
	 */
	@SuppressWarnings("unchecked")
	public void getAllLights(LightCallback lightCallback) {
		final LightHandler lightHandler = new LightHandler(lightCallback);
		new Thread() {
			public void run() {
				Message msg = lightHandler.obtainMessage(Constants.what.FAILURE);
				String url = "/lights";
				String response = HueRestClient.getInstance().get(url);
				Logger.i(TAG, "response >> " + response);
				ArrayList<LightEntry> allLights = new ArrayList<LightEntry>();
				try {
					JSONObject jsonObject = new JSONObject(response);
					Iterator<String> keys = jsonObject.keys();
					while (keys.hasNext()) {
						LightEntry light = new LightEntry();
						String id = keys.next();
						light.setId(id);
						JSONObject value = jsonObject.getJSONObject(id);
						light.setName(value.getString("name"));
						allLights.add(light);
					}
					if (allLights.size() > 0) {
						msg.what = Constants.what.SUCCESS;
						msg.obj = allLights;
					}
				} catch (JSONException e) {
					try {
						JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
						Iterator<String> keys = jsonObject.keys();
						if (keys.hasNext()) {
							String key = keys.next();
							JSONObject jsonValue = jsonObject.getJSONObject(key);
							if ("error".equals(key)) {
								if ("1".equals(jsonValue.getString("type"))) {
									msg.what = Constants.what.UNAUTHORIZED;
								}
							}
						}
					} catch (JSONException e2) {
						msg.what = Constants.what.WIFIERROR;
						Log.e(TAG, e.getLocalizedMessage());
					}
				} finally {
					lightHandler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * 获得指定灯的属性和状态
	 * 
	 * @param id
	 *            指定灯的id
	 * @param lightCallback
	 *            操作灯泡的回调接口
	 */
	public void getLightAttributesAndState(final String id, LightCallback lightCallback) {
		final LightHandler lightHandler = new LightHandler(lightCallback);
		final State state = new State();
		new Thread() {
			public void run() {
				Message msg = lightHandler.obtainMessage(Constants.what.FAILURE);
				String url = "/lights/" + id;
				String response = HueRestClient.getInstance().get(url);
				Logger.i(TAG, "response >> " + response);
				LightEntry light = null;
				try {
					JSONObject jo = new JSONObject(response);
					if (jo != null) {
						light = new LightEntry();
						light.setType(jo.getString("type"));
						light.setName(jo.getString("name"));
						light.setModelid(jo.getString("modelid"));
						light.setSwversion(jo.getString("swversion"));
						JSONObject stateJo = jo.getJSONObject("state");
						if (stateJo != null) {
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
							light.setState(state);
							msg.what = Constants.what.SUCCESS;
							msg.obj = light;
						}
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getLocalizedMessage());
					try {
						JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
						@SuppressWarnings("unchecked")
						Iterator<String> keys = jsonObject.keys();
						if (keys.hasNext()) {
							String key = keys.next();
							JSONObject jsonValue = jsonObject.getJSONObject(key);
							if ("error".equals(key)) {
								if ("1".equals(jsonValue.getString("type"))) {
									msg.what = Constants.what.UNAUTHORIZED;
								}
							}
						}
					} catch (Exception e2) {
						Logger.e(TAG, e2.getLocalizedMessage());
						msg.what = Constants.what.WIFIERROR;
					}
				} finally {
					lightHandler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * 设置灯的状态
	 * 
	 * @param id
	 *            制定灯的id
	 * @param state
	 *            指定灯的状态State实体
	 * @param lightCallback
	 *            操作灯泡的回调接口
	 */
	@SuppressWarnings("unchecked")
	public void setLightState(final String id, final State state, LightCallback lightCallback) {
		final LightHandler lightHandler = new LightHandler(lightCallback);
		new Thread() {
			public void run() {
				Message msg = lightHandler.obtainMessage(Constants.what.FAILURE);
				String url = "/lights" + "/" + id + "/state";
				String jsonBody = state.convertToJSON();
				HueRestClient client = HueRestClient.getInstance();
				String response = client.put(url, jsonBody);
				Log.i(TAG, "response >> " + response);
				try {
					JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
					Iterator<String> keys = jsonObject.keys();
					if (keys.hasNext()) {
						String key = keys.next();
						JSONObject jsonValue = jsonObject.getJSONObject(key);
						if ("error".equals(key)) {
							if ("1".equals(jsonValue.getString("type"))) {
								msg.what = Constants.what.UNAUTHORIZED;
							}
						} else {
							JSONArray ja = new JSONArray(response);
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo = ja.getJSONObject(i);
								Iterator<String> iter = jo.keys();
								if (iter.hasNext()) {
									if ("success".equals(iter.next())) {
										msg.what = Constants.what.SUCCESS;
									}
								}
							}
						}
					}
				} catch (Exception e) {
					msg.what = Constants.what.WIFIERROR;
				} finally {
					lightHandler.sendMessage(msg);
				}
			};
		}.start();
	}
}
