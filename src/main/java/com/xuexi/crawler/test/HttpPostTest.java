package com.xuexi.crawler.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 *
 * 爬虫核心代码
 */
public class HttpPostTest {
    public static void main(String[] args) throws Exception {
        //1.创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求地址http://yun.itheima.com/search?keys=java
        //创建urIbuilder
        URIBuilder uriBuilder = new URIBuilder("http://yun.itheima.com/search");
        //设置参数(可以连续设置多个参数)
        uriBuilder.setParameter("keys","java");

        //2.创建httpGet对象
        HttpPost httpPost = new HttpPost(uriBuilder.build());

        //3.使用httpclient发起请求,获取response响应
        CloseableHttpResponse response=null;
        try {
             response = httpClient.execute(httpPost);
            //4.解析相应
            if(response.getStatusLine().getStatusCode()==200){
                String content = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
