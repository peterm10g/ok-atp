package com.lsh.atp.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP 请求工具类
 *
 * @author : miaozhuang
 * @version : 1.0.0
 * @date : 2016/8/24
 * @see : TODO
 */
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 20000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);

        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        //
        requestConfig = configBuilder.build();
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url 请求地址
     * @return String
     */
    public static String doGet(String url) {

        return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url 请求URL
     * @return String
     */
    public static String doGet(String url,Map<String, Object> params) {

        return doGet(url, params,new HashMap<String, String>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url 请求地址
     * @param params 请求参数
     * @return String
     */
    public static String doGet(String url, Map<String, Object> params,Map<String, String> headMap) {
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }

        apiUrl += param;
        String result = null;
        HttpClient httpclient = HttpClients.custom().build();
        try {
            HttpGet httpGet = new HttpGet(apiUrl);

            // head设置
            if (headMap != null && !headMap.isEmpty()) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }

            HttpResponse response = httpclient.execute(httpGet);
            logger.info("response String is : " + response.toString());

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");

            } else {

                logger.info("response.getStatusLine().getStatusCode() is : " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param apiUrl 请求地址
     * @return String
     */
    public static String doPost(String apiUrl) {

        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP）不带head
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return String
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {

        return doPost(apiUrl,params,null);
    }


    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return String
     */
    public static String doPost(String apiUrl, Map<String, Object> params,Map<String, String> headMap) {
        //连接池的httpclient
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connMgr).build();
        //默认的httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        logger.info("apiUrl is " + apiUrl );

        try {

            httpPost.setConfig(requestConfig);

            // head设置
            if (headMap != null && !headMap.isEmpty()) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            List<NameValuePair> pairList = new ArrayList<>(params.size());

            for (Map.Entry<String, Object> entry : params.entrySet()) {

                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }

            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);

            logger.info("response String is : " + response.toString());

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity entity = response.getEntity();
                httpStr = EntityUtils.toString(entity, "UTF-8");

            } else {

                logger.info("response.getStatusLine().getStatusCode() is : " + response.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * @param apiUrl 请求地址
     * @param bodyJson 请求体
     * @return String
     */
    public static String doPostBody(String apiUrl,String bodyJson) {

        return doPostBody(apiUrl,bodyJson,new HashMap<String, String>());
    }


    /**
     * 发送 POST 请求（HTTP），JSON形式
     * @param apiUrl 请求URL
     * @param bodyJson 请求体
     * @param headMap 请求头
     * @return String
     */
    public static String doPostBody(String apiUrl, String bodyJson, Map<String, String> headMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);

            // head设置

            if (headMap != null && !headMap.isEmpty()) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            // 解决中文乱码问题
            StringEntity stringEntity = new StringEntity(bodyJson, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);

            logger.info("response String is : " + response.toString());

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity entity = response.getEntity();
                httpStr = EntityUtils.toString(entity, "UTF-8");
                logger.info("httpStr is " + httpStr);
            }else {

                logger.info("response.getStatusLine().getStatusCode() is : " + response.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }
}
