package com.bingoogol.training.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * 网络访问工具类
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class NetUtil {
	private static final String TAG = "NetUtil";

	public synchronized static String getByUrlConnection(String path) {
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		String result = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(Constants.net.CONNECTTIMEOUT);
			conn.setReadTimeout(Constants.net.READTIMEOUT);
			conn.setRequestMethod(Constants.net.GET);
			int code = conn.getResponseCode();
			if (code == 200) {
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				result = new String(baos.toByteArray(), Constants.CHARSET);
			} else {
				// 处理请求失败
			}
		} catch (Exception e) {
			Logger.e(TAG, e.getLocalizedMessage());
		} finally {
			close(is, baos);
		}
		return result;

	}

	public static void close(InputStream is, ByteArrayOutputStream baos) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (Exception e) {
			Logger.e(TAG, e.getLocalizedMessage());
		} finally {
			try {

			} catch (Exception e2) {
				Logger.e(TAG, e2.getLocalizedMessage());
			}
		}
	}

	public synchronized static String postByUrlConnection(String path) {
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		String result = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(Constants.net.CONNECTTIMEOUT);
			conn.setRequestMethod(Constants.net.POST);

		} catch (Exception e) {
			Logger.e(TAG, e.getLocalizedMessage());
		} finally {
			close(is, baos);
		}
		return result;
	}

	// public void postByUrlConnection(String path) throws Exception {
	// try {
	// URL url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(Constants.net.CONNECTTIMEOUT);
	// conn.setRequestMethod(Constants.net.POST);
	//
	// String data = "username=" +
	// URLEncoder.encode(username,Constants.charset.UTF8) + "&password=" +
	// password;
	// conn.setRequestProperty("Content-Type",
	// "application/x-www-form-urlencoded");
	// conn.setRequestProperty("Content-Length", data.length() + "");
	// conn.setDoOutput(true);
	// OutputStream os = conn.getOutputStream();
	// os.write(data.getBytes());
	//
	// int code = conn.getResponseCode();
	// if(code == 200) {
	// conn.getInputStream();
	// } else {
	//
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void getByHttpClient(String username, String password) {
		try {
			// 打开一个浏览器
			HttpClient client = new DefaultHttpClient();
			// 出入地址
			String path = "httpxxxoousername=" + URLEncoder.encode(username, Constants.CHARSET);
			HttpGet httpGet = new HttpGet(path);
			// 敲回车
			HttpResponse response = client.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				InputStream is = response.getEntity().getContent();
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void postByHttpClient(String username, String password) {
		try {
			// 1.打开一个浏览器
			HttpClient client = new DefaultHttpClient();
			// 2.出入地址
			String path = "httpxxxoousername=" + URLEncoder.encode(username, Constants.CHARSET);
			HttpPost httpPost = new HttpPost(path);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("username", username));
			parameters.add(new BasicNameValuePair("password", password));
			httpPost.setEntity(new UrlEncodedFormEntity(parameters, Constants.CHARSET));
			// 3.敲回车
			HttpResponse response = client.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				InputStream is = response.getEntity().getContent();
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}