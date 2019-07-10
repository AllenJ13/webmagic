package com.xuexi.crawler.test;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 爬虫测试案例
 */
public class CrawlerFirst {
    public static void main(String[] args) throws IOException {
        //1.打开浏览器,创建一个HTTPCLIENT对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2.输入网址,发起get对象
        HttpGet httpGet = new HttpGet("http://itcast.cn");
        //3.发送请求,返回响应
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //4.解析响应获取数据
        //判断状态码是否是200
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = response.getEntity();
            String content = EntityUtils.toString(httpEntity, "utf-8");
            System.out.println(content);
        }
    }
}
