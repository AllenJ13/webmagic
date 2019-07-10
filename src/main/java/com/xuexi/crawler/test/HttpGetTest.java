package com.xuexi.crawler.test;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * 爬虫核心代码
 */
public class HttpGetTest {
    public static void main(String[] args) throws Exception {
        //1.创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求地址http://yun.itheima.com/search?keys=java
        //创建urIbuilder
        URIBuilder uriBuilder = new URIBuilder("http://yun.itheima.com/search");
        //设置参数(可以连续设置多个参数)
        uriBuilder.setParameter("keys","java");

        //2.创建httpGet对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        //3.使用httpclient发起请求,获取response响应
        CloseableHttpResponse response=null;
        try {
             response = httpClient.execute(httpGet);
            //4.解析相应
            if(response.getStatusLine().getStatusCode()==200){
                String content = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(content);
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
