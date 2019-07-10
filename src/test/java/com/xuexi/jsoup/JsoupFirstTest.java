package com.xuexi.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * jsoup可以向httpclient一样发送请求,但对多线程和连接池集成效果不好,所以只用jsoup的解析方法
 */
public class JsoupFirstTest {
    /**
     * jsoup解析url
     * @throws Exception
     */
    @Test
    public void testUrl() throws Exception {
        //解析URL地址(第一个参数是访问的url,第二个参数是访问的超时时间)
        Document document = Jsoup.parse(new URL("http://www.itcast.cn"), 1000);
        //使用标签选择器获取title标签中的内容
        Elements title = document.getElementsByTag("title");
        String text = title.first().text();

        System.out.println(text);
    }

    /**
     * jsoup解析文件
     * @throws Exception
     */
    @Test
    public void parseTest() throws Exception {
        //解析文件
        Document document = Jsoup.parse(new File("C:\\Users\\AllenJ\\Desktop\\123.html"), "utf-8");
        String text = document.getElementsByTag("title").first().text();
        System.out.println(text);
    }


    /**
     * jsoup解析文件
     * @throws Exception
     */
    @Test
    public void domParseTest() throws Exception {
        //解析文件,获取document对象
        Document document = Jsoup.parse(new File("C:\\Users\\AllenJ\\Desktop\\123.html"), "utf-8");
       //获取元素
        //1.根据id获取元素
        Element element = document.getElementById("xxlxmap");
        //2.根据元素标签获取元素内容
        Elements elementsByTag = document.getElementsByTag("span");
        //3.根据class获取元素内容
        Elements documentElementsByClass = document.getElementsByClass("span1");
        //4.根据属性值获取元素内容
        String abc = document.getElementsByAttribute("abc").first().text();
        //5.根据属性名和属性值获取元素内容
        String s = document.getElementsByAttributeValue("abc", "123").first().text();

        System.out.println(abc);


        //元素中获取数据
        String id = element.id();
        //从元素中获取className
        Set<String> strings = element.classNames();
//        for (String string : strings) {
//            System.out.println(string);
//        }
        //通过属性名字获取元素中属性的值
        String id1 = element.attr("id");
        //获取元素中所有的属性
        Attributes attributes = element.attributes();
        String s1 = attributes.toString();
        //从元素中获取文本内容
        String text = element.text();
    }


    /**
     * 元素选择器
     * @throws Exception
     */
    @Test
    public void selectTest() throws Exception {
        //解析文件,获取document对象
        Document document = Jsoup.parse(new File("C:\\Users\\AllenJ\\Desktop\\123.html"), "utf-8");
        //元素+id 组合  el#id
        String text = document.select("h3#city").first().text();
        //元素加class   li.class_a
        document.select("li.class_a").first().text();
        //元素加属性名  span[abc]
        document.select("span[abc]").first().text();
        //任意组合
        document.select("span[abc].s_name").first();
        //查找某个元素下的子元素 用空格分离
        document.select(".class_a li").first();
        //查找某元素的直接下级
        document.select(".class_a > ul > li").first();
        //查找某元素下的所有子元素 *
        document.select(".city_con > ul > *");
    }






}
