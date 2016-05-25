package com.chan.today.utils;

import android.os.Handler;
import android.util.Log;

import com.chan.today.bean.TodayBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史上的今天调用示例代码 － 聚合数据 在线接口文档：http://www.juhe.cn/docs/63
 **/

public class JuheDemo {
	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	// 配置您申请的KEY
	public static final String APPKEY = "ba92d0d84486ad31ab62cbf8a94aea36";

	// 1.事件列表
	public static JSONObject getRequest1(int month, int day) {
		String result = null;
		String url = "http://api.juheapi.com/japi/toh";// 请求接口地址
		Map params = new HashMap();// 请求参数
		params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
		params.put("v", "1.0");// 版本，当前：1.0
		params.put("month", month);// 月份，如：10
		params.put("day", day);// 日，如：1
		JSONObject object = null;
		try {
			result = net(url, params, "GET");
			object = new JSONObject(result);
			if (object.getInt("error_code") == 0) {
				Log.d("tag", "error_code == 0 :" + object.get("result"));
			} else {
				Log.d("tag",
						object.get("error_code") + ":" + object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}

	// 2.根据ID查询事件详情
	public static void getRequest2() {
		String result = null;
		String url = "http://api.juheapi.com/japi/tohdet";// 请求接口地址
		Map params = new HashMap();// 请求参数
		params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
		params.put("v", "");// 版本，当前：1.0
		params.put("id", "");// 事件ID

		try {
			result = net(url, params, "GET");
			JSONObject object = new JSONObject(result);
			if (object.getInt("error_code") == 0) {
				System.out.println(object.get("result"));
			} else {
				System.out.println(object.get("error_code") + ":"
						+ object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param strUrl
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map params, String method)
			throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				// strUrl = strUrl + "?" + urlencode(params);
				strUrl = strUrl + "?" + "key=" + APPKEY + "&v=1.0&month="
						+ params.get("month") + "&day=" + params.get("day");
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try {
					DataOutputStream out = new DataOutputStream(
							conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	// 将map型转为请求参数型
	public static String urlencode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=")
						.append(URLEncoder.encode(i.getValue() + "", "UTF-8"))
						.append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 解析Json数据,并传入todayList
	 * 
	 * @param jsonObject
	 *            待解析的JsonObject
	 * @param todayList
	 *            要接受数据的list
	 */
	public static void parseJsonData(JSONObject jsonObject,
	                                 List<TodayBean> todayList, Handler handler) {
		try {
			JSONArray result = jsonObject.getJSONArray("result");
			for (int i = 0; i < result.length(); i++) {
				TodayBean bean = new TodayBean ();
				JSONObject todayObject = result.getJSONObject(i);
				bean.setDay(todayObject.getString("day"));
				bean.setDes(todayObject.getString("des"));
				bean.setId(todayObject.getString("_id"));
				bean.setLunar(todayObject.getString("lunar"));
				bean.setMonth(todayObject.getString("month"));
				bean.setPic(todayObject.getString("pic"));
				bean.setTitle(todayObject.getString("title"));
				bean.setYear(todayObject.getString("year"));
				todayList.add(bean);
			}

			handler.sendEmptyMessage (DataFactory.GETDATA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}