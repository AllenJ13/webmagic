package com.xuexi.webmagic;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

//需要实现PageProcessor接口
public class JobProcessor implements PageProcessor {
    //解析页面(三种方式可以配合使用)
    public void process(Page page) {
        //解析page返回的数据,并把解析的结果放回到ResultItems中
        //css选择器
//       page.putField("div", page.getHtml().css("ul#list-646").all());

        //xpath解析
//        page.putField("div2", page.getHtml().xpath("//div[@id=J_cate]/ul/").all());

        //  正则表达式
//        page.putField("div3", page.getHtml().css("div#J_cate a").regex(".*家.*").all());

        //处理结果API获取一条数据
        page.putField("div4", page.getHtml().css("div#J_cate a").regex(".*家.*").get());

        page.putField("div5", page.getHtml().css("div#J_cate a").regex(".*家.*").toString());

        //获取链接,访问获取链接之后的页面的数据
        page.addTargetRequests(page.getHtml().css("div#J_cate").links().all());
        page.putField("url", page.getHtml().css("div#categorys-2014").all());
    }

    private Site site = Site.me()
            .setCharset("utf-8")//设置编码
            .setTimeOut(10000)//10s设置超时时间,单位ms毫秒
            .setRetrySleepTime(1000)//设置重试时间ms
            .setRetryTimes(3);    //设置重试次数

    public Site getSite() {
        return site;
    }

    //主函数执行爬虫
    public static void main(String[] args) {
        Spider.create(new JobProcessor())
                //设置需要爬取的页面
                .addUrl("https://www.jd.com/")
                //将获取结果输出到文件中
                .addPipeline(new FilePipeline("C:\\Users\\AllenJ\\Desktop\\images\\result\\"))
                //设置有5个线程处理
                .thread(5)
                //执行爬虫
                .run();
    }
}
