package com.praktikum.mahasiswa;


import java.io.*;

import org.json.*;
import org.apache.http.*;
import org.apache.http.params.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.net.SocketException;
import java.util.List;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

public class JSONParser {
	static InputStream is= null;
	static JSONObject jObj = null;
	static String json = "";
	
	//in milisecond = 10 detik
	int timeout=10000;
	
	//constructor
	public JSONParser(){
		//timeout = new Values().gettimeout();
	}
	
	//function get json from url
	//by making HTTP POST or GET method
	public JSONObject makeHttpRequest(String url, 
			String method,List<NameValuePair> params){
		try{
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
			HttpConnectionParams.setSoTimeout(httpParameters, timeout);
			
		if(method == "POST"){
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost =new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();			
		
		}else if(method == "GET"){
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			String paramString = URLEncodedUtils.format(params, "utf-8");
			url += "?" + paramString;
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent(); 
			}
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(SocketException ste)
		{
			Log.e("Timeout Exception :", ste.toString());
		}
		catch (IOException e){
			e.printStackTrace();
			
		}
		
	try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) !=null){
			sb.append(line+ "\n");
		}
		is.close();
		json = sb.toString();
		}catch (Exception e){
			Log.e("Buffer Error", "Error converting result"+e.toString());
			
		}
		// try parse the string to a JSON object
		try {
		jObj = new JSONObject(json);
		} catch (JSONException e) {
		Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		// return JSON String
		return jObj;
		}
		}