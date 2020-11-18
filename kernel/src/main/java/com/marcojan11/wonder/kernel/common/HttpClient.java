package com.marcojan11.wonder.kernel.common;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Slf4j
public class HttpClient {

    //超时时间
    public static final int TIMEOUT_SOCKET = 5000;
    public static final int TIMEOUT_CONNECTION = 5000;
    public static final int TIMEOUT_CONNECTION_REQUEST = 5000;

    public static String getResponseByGetMethod(String url) {
        return getResponseByGetMethod(url, "GB2312");
    }

    /**
     * 建立连接对像，如为HTTPS URL则采用信任所有服务的方式
     *
     * @param url
     * @return
     */
    protected static CloseableHttpClient makeClientByURL(String url) {

        CloseableHttpClient client = null;

        if (url.substring(0, 5).toUpperCase().equals("HTTPS")) {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                HttpClientBuilder httpClientBuilder = HttpClients.custom();
                SSLContext sslcontext = SSLContexts.custom()
                        .loadTrustMaterial(trustStore, new TrustStrategy() {

                            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                return true;
                            }
                        })
                        .build();
                SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, new X509HostnameVerifier() {

                    public void verify(String host, SSLSocket ssl) throws IOException {

                    }

                    public void verify(String host, X509Certificate cert) throws SSLException {

                    }

                    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

                    }

                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
                httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
                httpClientBuilder.setSslcontext(sslcontext);
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SOCKET).setConnectionRequestTimeout(TIMEOUT_CONNECTION_REQUEST).
                        setConnectTimeout(TIMEOUT_CONNECTION).setStaleConnectionCheckEnabled(true).build();//设置请求和传输超时时间
                httpClientBuilder.setDefaultRequestConfig(requestConfig);
                client = httpClientBuilder.build();
            } catch (Throwable e) {
                log.error("make httpclient obj error,MSG[{}]", e.getMessage(), e);
                throw new RuntimeException("make httpclient obj error", e);
            }
        } else {
            client = HttpClients.createDefault();
        }
        return client;
    }

    /**
     * 根据GET方法得到页面的返回值
     *
     * @param url     目的地址
     * @param charSet 编码格式
     * @return String 内容
     */
    public static String getResponseByGetMethod(String url, String charSet) {
        CloseableHttpClient client = makeClientByURL(url);
        HttpGet httpget = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SOCKET).setConnectionRequestTimeout(TIMEOUT_CONNECTION_REQUEST).
                setConnectTimeout(TIMEOUT_CONNECTION).setStaleConnectionCheckEnabled(true).build();//设置请求和传输超时时间
        httpget.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(httpget);
            try {
                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    Charset charset = contentType.getCharset();

                    String content = EntityUtils.toString(response.getEntity());

                    return convertStringCode4get(content, charset, charSet);
                } else {
                    log.error("访问页面出错, URL:{} 返回状态码:{} 错误信息:{}", url, statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    return "";
                }
            } finally {
                response.close();
            }

        } catch (IOException e) {
            log.error("请求该页面出现异常:" + e.getMessage(), e);
            throw new RuntimeException("execute http get request failed! url:" + url, e);
        } finally {
            IOUtils.closeQuietly(client);
        }
    }

    /**
     * 设置POST提交的参数
     *
     * @param method   POST方法
     * @param paramMap 参数映射
     */
    private static void setRequestBody(HttpPost method, Map<String, String> paramMap) {
        Set<String> keySet = paramMap.keySet();
        Iterator<String> it = keySet.iterator();
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        while (it.hasNext()) {
            String key = it.next();
            String value = paramMap.get(key);
            // 设置参数
            NameValuePair nameValuePair = new BasicNameValuePair(key, value);
            paramList.add(nameValuePair);
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Consts.UTF_8);
        method.setEntity(entity);
    }

    /**
     * 设置POST提交的参数
     *
     * @param method      POST方法
     * @param requestBody 参数映射
     */
    private static void setRequestBody(HttpPost method, String requestBody) {

        StringEntity se = new StringEntity(requestBody, Consts.UTF_8);
        se.setContentType("application/json");
        method.setEntity(se);
    }

    public static String getResponseByPostMethod(String url, Map<String, String> parmMap) {
        return getResponseByPostMethod(url, "utf-8", parmMap);
    }


    public static String getResponseByGetMethod(String url, Map<String, String> parmMap) {
        StringBuffer paramStr = new StringBuffer("");
        //按顺序循环集合。
        for (Map.Entry<String, String> item : parmMap.entrySet()) {
            if (paramStr.toString().equals("")) {
                paramStr.append("?");
            } else {
                paramStr.append("&");
            }
            paramStr.append(item.getKey().trim()).append("=").append(item.getValue().trim());
        }

        //访问前和访问后都打印日志
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        long startTime = System.currentTimeMillis();
        log.info("HttpClientUtil.MONITOR: uuid:{}, url:{}, ts:{}, params:{}", new Object[]{uuid, url, startTime, paramStr});

        String resultJson = getResponseByGetMethod(url + paramStr, "utf-8");

        long endTime = System.currentTimeMillis();
        log.info("HttpClientUtil.MONITOR: uuid:{}, url:{}, ts:{}, costTime:{}, param:{}, resultJson:{}",
                new Object[]{uuid, url, endTime, Dates.getCostTimeStrByStartTime(startTime, endTime), paramStr, resultJson});
        return resultJson;
    }

    /**
     * 以POST方法得到网页内容
     *
     * @param url      目的地址
     * @param charSet  编码格式
     * @param paramMap post 提交的参数，key 参数名称,velue 参数名,均为String类型
     * @return String 返回内容
     */
    public static String getResponseByPostMethod(String url, String charSet, Map<String, String> paramMap) {
        CloseableHttpClient client = makeClientByURL(url);

        HttpPost httppost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SOCKET).setConnectionRequestTimeout(TIMEOUT_CONNECTION_REQUEST).
                setConnectTimeout(TIMEOUT_CONNECTION).setStaleConnectionCheckEnabled(true).build();//设置请求和传输超时时间
        httppost.setConfig(requestConfig);
        if (paramMap != null) {
            setRequestBody(httppost, paramMap);

        }

        try {
            CloseableHttpResponse response = client.execute(httppost);
            try {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    Charset charset = contentType.getCharset();
                    String charsetFrom;
                    if (charset == null) {
                        charsetFrom = charSet;
                    } else {
                        charsetFrom = charset.name();
                    }
                    String content = EntityUtils.toString(entity);
                    return convertStringCode(content, charsetFrom, charSet);
                } else {
                    log.error("访问页面出错, URL:{} 返回状态码:{} 错误信息:{}", url, statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    return "";
                }
            } finally {
                response.close();
            }


        } catch (IOException e) {
            log.error("请求该页面出现异常:" + e.getMessage(), e);
            throw new RuntimeException("execute http post request failed! url:" + url, e);

        } finally {
            IOUtils.closeQuietly(client);
        }
    }


    public static String getAruisResponseByPostMethod(String url, String charSet, String requestBody, String appId, String appsecret) {


        CloseableHttpClient client = makeClientByURL(url);
        try {
            HttpPost httppost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SOCKET).setConnectionRequestTimeout(TIMEOUT_CONNECTION_REQUEST).
                    setConnectTimeout(TIMEOUT_CONNECTION).setStaleConnectionCheckEnabled(true).build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);

            if (requestBody != null) {
                setRequestBody(httppost, requestBody);
            }
            httppost.setHeader("Authorization", "Basic " + new BASE64Encoder().encode(String.format("%s:%s", appId, appsecret).getBytes(charSet)));
            CloseableHttpResponse response = client.execute(httppost);
            try {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    Charset charset = contentType.getCharset();
                    String charsetFrom;
                    if (charset == null) {
                        charsetFrom = charSet;
                    } else {
                        charsetFrom = charset.name();
                    }
                    String content = EntityUtils.toString(entity);
                    return convertStringCode(content, charsetFrom, charSet);
                } else {
                    log.error("访问页面出错, URL:{} 返回状态码:{} 错误信息:{}", url, statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    return "";
                }
            } finally {
                response.close();
            }


        } catch (IOException e) {
            log.error("请求该页面出现异常:" + e.getMessage(), e);
            throw new RuntimeException("execute http post request failed! url:" + url, e);

        } finally {
            IOUtils.closeQuietly(client);
        }
    }

    /**
     * 以POST方法得到网页内容
     *
     * @param url     目的地址
     * @param charSet 编码格式
     * @return String 返回内容
     */
    public static String getResponseByPostMethod(String url, String charSet, String requestBody) {
        CloseableHttpClient client = makeClientByURL(url);

        HttpPost httppost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SOCKET).setConnectionRequestTimeout(TIMEOUT_CONNECTION_REQUEST).
                setConnectTimeout(TIMEOUT_CONNECTION).setStaleConnectionCheckEnabled(true).build();//设置请求和传输超时时间
        httppost.setConfig(requestConfig);

        if (requestBody != null) {
            setRequestBody(httppost, requestBody);
        }
        try {
            CloseableHttpResponse response = client.execute(httppost);
            try {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    Charset charset = contentType.getCharset();
                    String charsetFrom;
                    if (charset == null) {
                        charsetFrom = charSet;
                    } else {
                        charsetFrom = charset.name();
                    }
                    String content = EntityUtils.toString(entity);
                    return convertStringCode(content, charsetFrom, charSet);
                } else {
                    log.error("访问页面出错, URL:{} 返回状态码:{} 错误信息:{}", url, statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    return "";
                }
            } finally {
                response.close();
            }


        } catch (IOException e) {
            log.error("请求该页面出现异常:" + e.getMessage(), e);
            throw new RuntimeException("execute http post request failed! url:" + url, e);

        } finally {
            IOUtils.closeQuietly(client);
        }
    }

    public static String getResponseByPostMethod(String url, Map<String, String> headers, Map<String, String> paraMap) {

        String charSet = "utf-8";
        CloseableHttpClient client = makeClientByURL(url);

        HttpPost httppost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SOCKET).setConnectionRequestTimeout(TIMEOUT_CONNECTION_REQUEST).
                setConnectTimeout(TIMEOUT_CONNECTION).setStaleConnectionCheckEnabled(true).build();//设置请求和传输超时时间

        Set<String> keys = headers.keySet();
        for (String key : keys)
            httppost.setHeader(key, headers.get(key));

        if (paraMap != null) {
            setRequestBody(httppost, paraMap);
        }
        try {
            log.info("httppost - " + httppost.toString());

            CloseableHttpResponse response = client.execute(httppost);
            log.info("response - " + response.toString());

            try {
                StatusLine statusLine = response.getStatusLine();
                log.info("statusLine - " + statusLine);

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    Charset charset = contentType.getCharset();
                    String charsetFrom;
                    if (charset == null) {
                        charsetFrom = charSet;
                    } else {
                        charsetFrom = charset.name();
                    }
                    String content = EntityUtils.toString(entity);
                    return convertStringCode(content, charsetFrom, charSet);
                } else {
                    log.error("访问页面出错, URL:{} 返回状态码:{} 错误信息:{}", url, statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    return "";
                }
            } finally {
                response.close();
            }


        } catch (IOException e) {
            log.error("请求该页面出现异常:" + e.getMessage(), e);
            throw new RuntimeException("execute http post request failed! url:" + url, e);

        } finally {
            IOUtils.closeQuietly(client);
        }
    }


    public static String getResponseByPostMethod(String url, Map<String, String> headers, String requestBody) {

        String charSet = "utf-8";
        CloseableHttpClient client = makeClientByURL(url);

        HttpPost httppost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SOCKET).setConnectionRequestTimeout(TIMEOUT_CONNECTION_REQUEST).
                setConnectTimeout(TIMEOUT_CONNECTION).setStaleConnectionCheckEnabled(true).build();//设置请求和传输超时时间

        Set<String> keys = headers.keySet();
        for (String key : keys)
            httppost.setHeader(key, headers.get(key));

        //httppost.setConfig(requestConfig);

        if (requestBody != null) {
            setRequestBody(httppost, requestBody);
        }
        try {
            log.info("httppost - " + httppost.toString());

            CloseableHttpResponse response = client.execute(httppost);
            log.info("response - " + response.toString());

            try {
                StatusLine statusLine = response.getStatusLine();
                log.info("statusLine - " + statusLine);

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    Charset charset = contentType.getCharset();
                    String charsetFrom;
                    if (charset == null) {
                        charsetFrom = charSet;
                    } else {
                        charsetFrom = charset.name();
                    }
                    String content = EntityUtils.toString(entity);
                    return convertStringCode(content, charsetFrom, charSet);
                } else {
                    log.error("访问页面出错, URL:{} 返回状态码:{} 错误信息:{}", url, statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    return "";
                }
            } finally {
                response.close();
            }


        } catch (IOException e) {
            log.error("请求该页面出现异常:" + e.getMessage(), e);
            throw new RuntimeException("execute http post request failed! url:" + url, e);

        } finally {
            IOUtils.closeQuietly(client);
        }
    }


    /**
     * 转换编码格式
     *
     * @param source     源字符串
     * @param srcEncode  源字符串编码格式
     * @param destEncode 需要转换的编码格式
     * @return String
     */
    private static String convertStringCode(String source, String srcEncode, String destEncode) {
        if (source != null && !"".equals(source)) {
            try {
                return new String(source.getBytes(srcEncode), destEncode);
            } catch (UnsupportedEncodingException e) {
                log.error("UnsupportedEncodingException", e);
                return "";
            }
        } else {
            return "";
        }
    }

    private static String convertStringCode4get(String source, Charset srcEncode, String destEncode) {
        try {
            if (Strings.isNullOrEmpty(source)) {
                return "";
            }
            if (srcEncode != null && !Strings.isNullOrEmpty(srcEncode.name()) && !Strings.isNullOrEmpty(destEncode)) {
                return new String(source.getBytes(srcEncode.name()), destEncode);
            } else {
                return source;
            }
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException", e);
            return "";
        }
    }


    public static byte[] getResponseByGetMethodWithAttachment(String url) {
        StringBuffer paramStr = new StringBuffer("");

        CloseableHttpClient client = makeClientByURL(url + paramStr);
        HttpGet httpget = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SOCKET).setConnectionRequestTimeout(TIMEOUT_CONNECTION_REQUEST).
                setConnectTimeout(TIMEOUT_CONNECTION).setStaleConnectionCheckEnabled(true).build();//设置请求和传输超时时间
        httpget.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(httpget);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                response.getEntity().writeTo(bos);
            }
            return bos.toByteArray();

        } catch (Exception e) {
            log.error("请求该页面出现异常:" + e.getMessage(), e);
            throw new RuntimeException("execute http get request failed! url:" + url, e);

        } finally {
            IOUtils.closeQuietly(client);
        }
    }
}
