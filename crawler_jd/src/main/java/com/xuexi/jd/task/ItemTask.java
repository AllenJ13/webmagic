package com.xuexi.jd.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuexi.jd.pojo.Item;
import com.xuexi.jd.service.ItemService;
import com.xuexi.jd.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class ItemTask {
    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ItemService itemService;

    /**
     * 解析json的工具类
     */
    @Autowired
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 当下载任务完成后间隔多长时间进行下一次任务
     */
    @Scheduled(fixedDelay = 100 * 1000) //单位毫秒
    public void itemTask() throws Exception {
        //声明要解析的初始地址
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E6%89%8B%E6%9C%BA&cid2=653&cid3=655&s=57&click=0page=";

        //按照页面对手机的搜索结果进行遍历解析
        for (int i = 1; i < 10; i += 2) {
            String html = httpUtils.doGetHtml(url + i);

            //解析页面获取商品数据并存储
            this.parse(html);
        }

        System.out.println("手机数据抓取完成");
    }

    //解析页面获取商品
    private void parse(String html) throws Exception {
        //解析html 获取document 对象
        Document document = Jsoup.parse(html, "utf-8");
        //获取spu elements
        Elements spuEles = document.select("div#J_goodsList > ul > li");

        //获取spu 信息
        for (Element spuEle : spuEles) {
            //spu
            long spu = Long.parseLong(spuEle.attr("data-spu"));
            //获取sku信息
            Elements skuEles = spuEle.select("li.ps-item");
            for (Element skuEle : skuEles) {
                long sku = Long.parseLong(skuEle.select("img").attr("data-sku"));

                //根据sku查询商品数据
                Item item = new Item();
                item.setSku(sku);
                List<Item> itemList = itemService.findAll(item);
                // 如果商品存在就进行下一个循环,该商品已存在
                if (itemList.size() > 0) {
                    continue;
                }
                //设置商品的spu
                item.setSpu(spu);
                //获取商品详情的url
                String itemUrl = "https://item.jd.com/" + sku + ".html";
                item.setUrl(itemUrl);
                //获取商品的图片
                String picUrl = "https:" + skuEle.select("img[data-sku]").first().attr("data-lazy-img");
                picUrl = picUrl.replace("/n9/", "/n1/");
                httpUtils.doGetImage(picUrl);
                item.setPic(picUrl);
                //获取商品的价格
                String priceJson = httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
                double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                item.setPrice(price);
                //获取商品的标题(进入商品详情页面获取商品标题)
                String itemInfo = httpUtils.doGetHtml(item.getUrl());
                Document itemdoc = Jsoup.parse(itemInfo);
                String title = itemdoc.select("div.sku-name").text();
                item.setTitle(title);
                //获取商品的创建
                item.setCreated(new Date());
                //获取商品的更新时间
                item.setUpdated(item.getCreated());
                //保存商品数据到数据库中
                itemService.save(item);
            }
        }

    }
}
