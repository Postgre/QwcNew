package com.gservfocus.qwc.net.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.gservfocus.qwc.net.WSError;

public class HttpUtils {
	/**
	 * get «Î«Û
	 * */
	public static String doGet(String url) {
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpClient hc = new DefaultHttpClient();
			HttpResponse ht = hc.execute(httpGet);
			if (ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity he = ht.getEntity();
				InputStream is = he.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String response = "";
				String readLine = null;
				while ((readLine = br.readLine()) != null) {
					response = response + readLine;
				}
				is.close();
				br.close();
				return response;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

}
