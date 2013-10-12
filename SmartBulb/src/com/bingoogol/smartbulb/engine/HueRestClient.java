package com.bingoogol.smartbulb.engine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;

/**
 * 灯泡访问RestFul客户端类，单例
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class HueRestClient {
	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	private static final String TAG = "HueRestClient";

	private String userName = "newdeveloper";

	private static HueRestClient instance;

	private HueRestClient() {
	}

	/**
	 * 单例模式（懒汉式），获取HueRestClient类唯一实例
	 * 
	 * @return HueRestClient实例
	 */
	public synchronized static HueRestClient getInstance() {
		if (instance == null) {
			instance = new HueRestClient();
		}
		return instance;
	}

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * 设置用户名
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 以get方式发送请求调用api
	 * 
	 * @param url
	 *            表示相应api的相对地址
	 * @return 返回Json格式的响应数据
	 */
	public synchronized String get(String path) {
		String jsonResult = "";
		InputStream is = null;
		try {
			HttpURLConnection conn = getHttpURLConnection(path);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				jsonResult = this.read(is);
				Log.i("Get", jsonResult);
			}

		} catch (MalformedURLException e) {
			Logger.e("Get", "URL格式错误");
			e.printStackTrace();
		} catch (IOException e) {
			Logger.e("Get", "连接网络发生错误" + e.getMessage());
			e.printStackTrace();
		} finally {
			closeStream(is);
		}

		return jsonResult;
	}

	/**
	 * 以post方法发送请求调用api
	 * 
	 * @param url
	 * @param jsonBody
	 *            json格式的请求数据
	 * @return 返回Json格式的响应数据
	 */
	public synchronized String post(String path, String jsonBody) {
		String jsonResult = "";
		OutputStream os = null;
		InputStream is = null;

		try {
			HttpURLConnection conn = getHttpURLConnection(path);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty("Content-type", CONTENT_TYPE);

			conn.setRequestMethod("POST");
			os = conn.getOutputStream();
			os.write(jsonBody.getBytes());
			os.flush();

			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				jsonResult = this.read(is);
				Log.i("Post", jsonResult);
			}
		} catch (MalformedURLException e) {
			Logger.e(TAG, "URL格式错误");
			e.printStackTrace();
		} catch (IOException e) {
			Logger.e(TAG, "网络链接错误");
			e.printStackTrace();
		} finally {
			closeStream(is);
			closeStream(os);
		}
		return jsonResult;
	}

	/**
	 * 以put方法发送请求调用api
	 * 
	 * 
	 * @param url
	 * @param jsonBody
	 *            json格式的请求数据
	 * @return 返回Json格式的响应数据
	 */
	public synchronized String put(String path, String jsonBody) {
		String jsonResult = "";
		OutputStream os = null;
		InputStream is = null;
		try {
			HttpURLConnection conn = getHttpURLConnection(path);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-type", CONTENT_TYPE);
			conn.setRequestMethod("PUT");
			os = conn.getOutputStream();
			os.write(jsonBody.getBytes());
			os.flush();

			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				jsonResult = this.read(is);
			}
		} catch (MalformedURLException e) {
			Logger.e(TAG, "URL格式错误");
			e.printStackTrace();
		} catch (IOException e) {
			Logger.e(TAG, "连接网络发生错误:");
		} finally {
			closeStream(is);
			closeStream(os);
		}
		return jsonResult;
	}

	/**
	 * 以delete方式发送请求调用api
	 * 
	 * @param url
	 *            表示相应api的相对地址
	 * @return 返回Json格式的响应数据
	 */
	public synchronized String delete(String path) {
		String jsonResult = "";
		InputStream is = null;
		try {
			HttpURLConnection conn = getHttpURLConnection(path);
			conn.setRequestMethod("DELETE");
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				jsonResult = this.read(is);
			}
		} catch (MalformedURLException e) {
			Logger.e(TAG, "URL格式错误");
			e.printStackTrace();
		} catch (IOException e) {
			Logger.e(TAG, "连接网络发生错误" + e.getMessage());
			e.printStackTrace();
		} finally {
			closeStream(is);
		}

		return jsonResult;
	}

	/**
	 * 获得HttpURLConnection连接实例
	 * 
	 * @param strURL
	 * @return 连接
	 * @throws IOException
	 */
	private HttpURLConnection getHttpURLConnection(String strURL) throws IOException {
		URL url = new URL(this.getAbsoluteURL(strURL));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(Constants.net.CONNECTTIMEOUT);
		conn.setReadTimeout(Constants.net.READTIMEOUT);
		return conn;
	}

	/**
	 * 从输入流中读出文本信息
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public String read(InputStream is) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[128];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		String text = new String(out.toByteArray(), "utf-8");
		out.flush();
		this.closeStream(out);
		return text;
	}

	/**
	 * 关闭IO流
	 * 
	 * @param obj
	 */
	private void closeStream(Object obj) {
		if (obj != null && obj instanceof InputStream) {
			InputStream is = (InputStream) obj;
			try {
				is.close();
			} catch (IOException e) {
				Logger.e(TAG, "输入流关闭错误");
			}
		} else if (obj != null && obj instanceof OutputStream) {
			OutputStream os = (OutputStream) obj;
			try {
				os.close();
			} catch (IOException e) {
				Logger.e(TAG, "输出流关闭错误");
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获取完整的URL
	 * 
	 * @param relativeURL
	 * @return
	 */
	private String getAbsoluteURL(String relativeURL) {
		String absoluteUrl = "";
		if (relativeURL == null || "".equals(relativeURL)) {
			absoluteUrl = "http://" + Constants.IPADDRESS + "/api/";
		} else {
			absoluteUrl = "http://" + Constants.IPADDRESS + "/api/" + this.getUserName() + relativeURL;
		}
		Logger.i(TAG, "absoluteURL >> " + absoluteUrl);
		return absoluteUrl;
	}
}
