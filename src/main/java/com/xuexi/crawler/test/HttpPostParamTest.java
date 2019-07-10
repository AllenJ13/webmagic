package com.xuexi.crawler.test;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 爬虫核心代码
 */
public class HttpPostParamTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        //1.创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.创建httpPost对象
        //-------------------------封装post请求对象start---------------
        HttpPost httpPost = new HttpPost("http://yun.itheima.com/search");
        //声明list集合(用户封装表单中的参数)
        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("keys","java"));
        //创建表单的entity对象,第一个参数就是封装好的表单参数,第二个参数就是字符集
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,"uft-8");
        //设置表单中的entity到post请求中
        httpPost.setEntity(entity);
        //--------------------------封装post请求对象end---------------------------------------
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
