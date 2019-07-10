package com.xuexi.job.task;

import com.xuexi.job.pojo.JobInfo;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

//webmagic
//让spring管理
@Component
public class JobProcessor implements PageProcessor {
    private String url = "https://search.51job.com/list/000000,000000,0000,01%252C32,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    //解析页面
    @Override
    public void process(Page page) {
        //解析页面,获取招聘信息详情的url 地址
        List<Selectable> list = page.getHtml().css("div#resultList div.el").nodes();
        //判断获取到的详情页是否为空,如果为空,表示这是招聘详情页
        if (list.size() == 0) {
            //解析页面获取招聘详情信息,并保存
            this.saveJobInfo(page);
        } else {
            //如果不为空,解析详情页url地址,放到任务队列中
            for (Selectable selectable : list) {
                //获取url地址
                String jobInfoUrl = selectable.links().toString();
//                System.out.println(jobInfoUrl);
                //把获取到的url地址放入到任务队列中
                page.addTargetRequest(jobInfoUrl);
            }
            //获取下一页的url
            String bkUrl = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            //将url 放入到任务队列中
            page.addTargetRequest(bkUrl);
            System.out.println(bkUrl);
        }

        String html = page.getHtml().toString();
//        System.out.println(html);
    }

    //解析页面获取详情信息,解析详情信息,保存数据
    private void saveJobInfo(Page page) {
        //创建招聘详情对象
        JobInfo jobInfo = new JobInfo();
        //解析页面
        Html html = page.getHtml();
        //         上海    2年经验    大专    招10人    07-10发布
        String content = Jsoup.parse(html.css("div.cn p").regex(".*发布").toString()).text();

        //获取数据封装到对象中(通过html获取属性里面的结果,通过jsoup获取文本内容)
        jobInfo.setCompanyName(html.css("div.cn p.cname a", "text").toString());
        jobInfo.setCompanyAddr(Jsoup.parse(html.css("div.bmsg ").nodes().get(1).toString()).text());
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());
        jobInfo.setJobName(html.css("div.cn h1", "title").toString());
        jobInfo.setJobAddr(content.substring(0, 2));
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        //获取薪资(通过薪资的工具类)
        Integer[] salary = MathSalary.getSalary(html.css("div.cn strong", "text").toString());

        jobInfo.setSalaryMin(salary[0]);
        jobInfo.setSalaryMax(salary[1]);
        jobInfo.setUrl(page.getUrl().toString());
        jobInfo.setTime(content.substring(content.length() - 7, content.length() - 2));

        //把结果保存
        page.putField("JobInfo", jobInfo);

    }

    private Site site = Site.me()
            .setCharset("GBK")//设置编码
            .setTimeOut(10 * 1000)//超时时间
            .setRetrySleepTime(3000)
            .setRetryTimes(3);//重试次数

    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private SpringDataPipeline springDataPipeline;

    //initialDelay 任务启动后等待多久执行方法
    //fixedDelay 每个多久执行这个方法
    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    public void process() {
        Spider.create(new JobProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000))) //对多少条数据进行去重(布隆过滤器),设置的越小月哦容易误判,设置的越大越占内存
                .thread(10) //开启10个现成
                .addPipeline(springDataPipeline)
                .run();
    }
}
