package com.xuexi.jd.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import sun.net.www.http.HttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 通过次注解,将HttpUtils 交给spring管理
 */
@Component
public class HttpUtils {

    /**
     * http连接池
     */
    private PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(100);
        //设置每个主机的最大连接数
        cm.setDefaultMaxPerRoute(10);
        //
    }

    /**
     * 根据请求地址下载页面数据
     *
     * @param url
     * @return 查询数据
     */
    public String doGetHtml(String url) {
        //1.获取HttpCleint对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //2.设置HttpGet 请求的对象
        HttpGet httpGet = new HttpGet(url);
        //设置请求头放置被拦截提示登录
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");

        //设置请求信息
        httpGet.setConfig(this.getConfig());
        //3.使用HttpClient发起请求,响应
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //判断响应体,entity是否为空,如果不为空就可以使用entityUtils
                if (httpResponse.getEntity() != null) {
                    String content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                    //4.解析响应,返回结果
                    return content;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                //httpclient不用关闭,因为是使用连接池管理的
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //返回空串
        return "";
    }

    /**
     * 下载图片
     *
     * @param url
     * @return 图片名称
     */
    public String doGetImage(String url) {

        //1.获取HttpCleint对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //2.设置HttpGet 请求的对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");


        //设置请求信息
        httpGet.setConfig(this.getConfig());
        //3.使用HttpClient发起请求,响应
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //判断响应体,entity是否为空,如果不为空就可以使用entityUtils
                if (httpResponse.getEntity() != null) {
                    //下载图片
                    //1.获取图片的后缀
                    String extName = url.substring(url.lastIndexOf("."));
                    //2.创建图片名并重命名图片
                    String picName = UUID.randomUUID().toString() + extName;
                    //3.下载图片
                    //3.1声明outputStream
                    OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\AllenJ\\Desktop\\images\\" + picName));
                    httpResponse.getEntity().writeTo(outputStream);
                    //4.返回图片名称
                    return picName;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                //httpclient不用关闭,因为是使用连接池管理的
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //返回空串
        return "";
    }

    /**
     * 设置请求信息
     *
     * @return
     */
    public RequestConfig getConfig() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000) //创建连接的最长时间
                .setConnectionRequestTimeout(500)//获取连接的最长时间
                .setSocketTimeout(10 * 1000)
                .build();//数据传输的最长时间
        return requestConfig;
    }
}
