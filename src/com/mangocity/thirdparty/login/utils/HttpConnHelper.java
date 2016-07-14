package com.mangocity.thirdparty.login.utils;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * HTTP请求工具类
 * 
 * @author zhangkun
 */
public class HttpConnHelper {
	private static final Logger log = Logger.getLogger(HttpConnHelper.class);

	/**
	 * GET请求
	 */
	public static String doHttpRequest(String url) {
		if (StringUtils.isEmpty(url)) {
			return "";
		}
		HttpClient client = new DefaultHttpClient();
		HttpRequestBase request = null;
		String content = "";
		try {
			request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity, HTTP.UTF_8);
		} catch (ClientProtocolException e) {
			log.error("doHttpGetRequest ClientProtocolException.", e);
		} catch (IllegalStateException e) {
			log.error("doHttpGetRequest IllegalStateException.", e);
		} catch (IOException e) {
			log.error("doHttpGetRequest IOException.", e);
		} finally {
			if (request != null && !request.isAborted()) {
				request.abort();
			}
			client.getConnectionManager().shutdown();
		}
		return content;
	}
	

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param postParams
	 * @return HTTP响应返回内容
	 */
	public static String doHttpRequest(String url, Object postParams) {
		if (StringUtils.isEmpty(url)) {
			return "";
		}
		if (postParams == null) {
			return doHttpRequest(url);
		}
		HttpClient client = new DefaultHttpClient();
		HttpRequestBase request = null;
		HttpEntity entity = null;
		String content = "";
		try {
            log.info("请求url="+url);
			request = new HttpPost(url);
			if (postParams instanceof Map) {
				Map<String, String> params = (Map<String, String>) postParams;
				setPostParams(request, params);
			} else if (postParams instanceof String) {
				String params = (String) postParams;
				setPostParams(request, params);
			}
			HttpResponse response = client.execute(request);
			entity = response.getEntity();
			content = EntityUtils.toString(entity, HTTP.UTF_8);
		} catch (ClientProtocolException e) {
			log.error("doHttpPostRequest ClientProtocolException.", e);
		} catch (IllegalStateException e) {
			log.error("doHttpPostRequest IllegalStateException.", e);
		} catch (UnsupportedEncodingException e) {
			log.error("doHttpPostRequest UnsupportedEncodingException.", e);
		} catch (IOException e) {
			log.error("doHttpPostRequest IOException.", e);
		} catch (Exception e) {
			log.error("doHttpPostRequest Exception.", e);
		} finally {
			if (request != null && !request.isAborted()) {
				request.abort();
			}
			client.getConnectionManager().shutdown();
		}
		return content;
	}
	
	public  static  String  dorequest(String  url,List<BasicNameValuePair>  formParams){
		System.out.println("ssssssssss");
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httpPost.setEntity(urlEncodedFormEntity);
			HttpResponse httpResponse = null;
			httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				String content = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.println("Response content:" + content);
				return  content;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭连接，释放资源  
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * 设置POST请求参数
	 * 
	 * @param request
	 * @param postParams
	 * @throws java.io.UnsupportedEncodingException
	 */
	private static void setPostParams(HttpRequestBase request,
			Map<String, String> postParams) throws UnsupportedEncodingException {
		List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : postParams.entrySet()) {
			postData.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		AbstractHttpEntity entity = new UrlEncodedFormEntity(postData,
				HTTP.UTF_8);
		((HttpPost) request).setEntity(entity);
	}

	/**
	 * 设置POST请求参数
	 * 
	 * @param request
	 * @param postParams
	 * @throws java.io.UnsupportedEncodingException
	 */
	private static void setPostParams(HttpRequestBase request, String postParams)
			throws UnsupportedEncodingException {
		AbstractHttpEntity entity = new StringEntity(postParams, HTTP.UTF_8);
		((HttpPost) request).setEntity(entity);
	}

	/**
	 * 获取post的字符串数据
	 * 
	 * @param paramsMap
	 */
	public static String getPostParams(Map<String, String> paramsMap) {
		StringBuilder paramsStr = new StringBuilder("");
		if (paramsMap == null) {
			return "";
		}
		int index = 0;
		// 将请求参数拼接成a=xx&b=xx&c=xx的形式
		for (Iterator<Map.Entry<String, String>> it = paramsMap.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = StringUtils.isEmpty(entry.getValue()) ? "" : entry
					.getValue();
			if (index == 0) {
				paramsStr.append(key).append("=").append(value);
			} else {
				paramsStr.append("&").append(key).append("=").append(value);
			}
			index++;
		}
		if (paramsStr.length() > 0) {
			// 将请求参数字符串并上服务者密匙进行MD5数字签名
			//String encryptKey = MbrUtil.getMd5Str(paramsStr.toString());
			// 并拼接到请求字符串中
			//paramsStr.append("&encryptKey=").append(encryptKey);
		}
		return paramsStr.toString();
	}
	/**
	 * 发起https请求,客户端直接信任所有证书
	 */
	public static String httpsRequest(String requestUrl, Object output) {
		if (StringUtils.isEmpty(requestUrl)) {
			return "";
		}
		if (output==null) {
			return httpsRequest(requestUrl);
		}
		HttpClient httpclient = null;
		HttpPost httpPost = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			} };
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, tm, new SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			httpclient = new DefaultHttpClient();
			//通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上 
			httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, ssf)); 
			httpPost = new HttpPost(requestUrl);
			HttpParams httpParameters = new BasicHttpParams();
			httpPost.setParams(httpParameters);
			// 设置超时时间
			HttpConnectionParams
					.setConnectionTimeout(httpParameters, 30 * 1000);
			HttpConnectionParams.setSoTimeout(httpParameters, 30 * 1000);
			// 设置请求参数
			List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
			if (output instanceof Map) {
				Map<String, String> paramsMap = (Map<String, String>) output;
				Set<String> keys = paramsMap.keySet();
				for (Iterator<String> i = keys.iterator(); i.hasNext();) {
					String key = (String) i.next();
					pairs.add(new BasicNameValuePair(key, paramsMap.get(key)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			} else if (output instanceof String) {
				String postParams = (String) output;
				httpPost.setEntity(new StringEntity(postParams, "UTF-8"));
			}
			// 执行请求
			HttpResponse httpRespons = httpclient.execute(httpPost);
			// 获取返回值
			HttpEntity entity = httpRespons.getEntity();
			String content = EntityUtils.toString(entity, HTTP.UTF_8);
			return content;
		} catch (ConnectException ce) {
			log.error("httpsRequest ConnectException.", ce);
		} catch (Exception e) {
			log.error("httpsRequest Exception.", e);
		} finally {
			if (httpPost != null && !httpPost.isAborted()) {
				httpPost.abort();
			}
			if (httpclient != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
		return "";
	}
	/**
	 * 发起https get请求,客户端直接信任所有证书
	 */
	public static String httpsRequest(String requestUrl) {
		if (StringUtils.isEmpty(requestUrl)) {
			return "";
		}
		HttpClient httpclient = null;
		HttpGet httpGet = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			} };
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, tm, new SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			httpclient = new DefaultHttpClient();
			//通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上 
			httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, ssf)); 
			httpGet = new HttpGet(requestUrl);
			HttpParams httpParameters = new BasicHttpParams();
			httpGet.setParams(httpParameters);
			// 设置超时时间
			HttpConnectionParams
					.setConnectionTimeout(httpParameters, 30 * 1000);
			HttpConnectionParams.setSoTimeout(httpParameters, 30 * 1000);
			// 执行请求
			HttpResponse httpRespons = httpclient.execute(httpGet);
			// 获取返回值
			HttpEntity entity = httpRespons.getEntity();
			String content = EntityUtils.toString(entity, HTTP.UTF_8);
			return content;
		} catch (ConnectException ce) {
			log.error("httpsRequest ConnectException.", ce);
		} catch (Exception e) {
			log.error("httpsRequest Exception.", e);
		} finally {
			if (httpGet != null && !httpGet.isAborted()) {
				httpGet.abort();
			}
			if (httpclient != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
		return "";
	}
	/**
	 * 发起https请求,加载服务器端证书
	 * 
	 * @param
	 * @return String
	 */
	public static String httpsRequestWithTrust(String requestUrl, Object output) {
		if (StringUtils.isEmpty(requestUrl)) {
			return "";
		}
		HttpClient httpclient = null;
		HttpPost httpPost = null;
		BufferedReader reader = null;
		try {
			// 下载的证书放到项目中的config目录中
			InputStream is = HttpConnHelper.class
					.getResourceAsStream("/mangoStore.p12");
			log.info("is==" + is);
			log.info("storePath="
					+ HttpConnHelper.class.getResource("/mangoStore.p12")
							.getPath());
			// InputStream is = new FileInputStream("D:\\mangoStore.p12");
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(is, "changeit".toCharArray());
			SSLSocketFactory socketFactory = new SSLSocketFactory(
					keyStore, "changeit");
			Scheme sch = new Scheme("https", 443, socketFactory);
			httpclient = new DefaultHttpClient();
			httpclient.getConnectionManager().getSchemeRegistry().register(sch);
			httpPost = new HttpPost(requestUrl);
			HttpParams httpParameters = new BasicHttpParams();
			httpPost.setParams(httpParameters);
			// 设置超时时间
			HttpConnectionParams
					.setConnectionTimeout(httpParameters, 30 * 1000);
			HttpConnectionParams.setSoTimeout(httpParameters, 30 * 1000);
			// 设置请求参数
			List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
			if (output instanceof Map) {
				Map<String, String> paramsMap = (Map<String, String>) output;
				Set<String> keys = paramsMap.keySet();
				for (Iterator<String> i = keys.iterator(); i.hasNext();) {
					String key = (String) i.next();
					pairs.add(new BasicNameValuePair(key, paramsMap.get(key)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			} else if (output instanceof String) {
				String postParams = (String) output;
				httpPost.setEntity(new StringEntity(postParams, "UTF-8"));
			}
			// 执行请求
			HttpResponse httpRespons = httpclient.execute(httpPost);
			// 获取返回值
			InputStream inStream = httpRespons.getEntity().getContent();
			reader = new BufferedReader(
					new InputStreamReader(inStream, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			String ret = sb.toString();
			return ret;
		} catch (Exception e) {
			log.error("httpsRequestWithTrust Exception", e);
		} finally {
			if (httpPost != null && !httpPost.isAborted()) {
				httpPost.abort();
			}
			if (httpclient != null)
				httpclient.getConnectionManager().shutdown();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
				reader = null;
			}
		}
		return "";
	}

/*	public static void main(String[] args) throws Exception {

		String imbrSvrUrl = "http://www.mangocity.com/IMBR/memberClientServlet";
		String passengerJson = "{\"certid\":\"123123123\",\"certtype\":\"PSP\",\"psgname\":\"张三\",\"psgtype\":\"ADT\",\"psgsex\":\"U\"}";
		String postData = "op=" + 216 + "&mbrId=" + 35346384 + "&passenger="
				+ passengerJson + "&timeStamp=" + MbrUtil.getTimeStamp()
				+ "&source=" + MbrConst.SOURCE;
		postData += "&encryptKey=" + MbrUtil.getMd5Str(postData);
		try {
			String respXML = HttpConnHelper.doHttpRequest(imbrSvrUrl, postData);
			// 解析XML
			Document doc = DocumentHelper.parseText(respXML);
			Element root = doc.getRootElement();
			String code = root.elementText("code");
			System.out.println(code);
		} catch (Exception e) {
			log.error("MemberFacadeImpl addHisPassenger Exception", e);
		}
	}*/
}
