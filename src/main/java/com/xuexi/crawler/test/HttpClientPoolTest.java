package com.xuexi.crawler.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;

import java.io.IOException;

public class HttpClientPoolTest {
    public static void main(String[] args) {
        //创建连接池管理器
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        //设置连接数(连接池有多少个连接)
        manager.setMaxTotal(100);
        //设置每个主机的最大连接数(不是爬取一个主机的数据,可以多个访问连接同时爬取,合理分配资源)
        manager.setDefaultMaxPerRoute(10);

        //使用连接池管理器发送请求
        doGet(manager);
        doGet(manager);
    }

    private static void doGet(PoolingHttpClientConnectionManager manager)  {
        //不是每次创建新的HttpClient,而是从连接池中获取
        //1.获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(manager).build();
        //创建httpGet对象(封装httpGet信息)
        HttpGet httpGet = new HttpGet("http://www.itcast.cn");
        CloseableHttpResponse httpResponse=null;
        try {
            //发送请求,获得返的response对象
             httpResponse = httpClient.execute(httpGet);
             //发送成功
             if(httpResponse.getStatusLine().getStatusCode()==200){
                 //将response返回的实例对象转化成utf-8的字符集编码
                 String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                 System.out.println(content.length());
             }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //由连接池管理,不能关闭
           /* try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }

    }
}
