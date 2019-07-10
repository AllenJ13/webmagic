package com.xuexi.job.task;

import com.xuexi.job.pojo.JobInfo;
import com.xuexi.job.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {
    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的招聘详情对象( 根据存进去的key获取 page.putField("JobInfo", jobInfo);)
        JobInfo jobInfo = resultItems.get("JobInfo");
        //判断数据是否为空
        if (jobInfo != null) {
            jobInfoService.save(jobInfo);
        }
    }
}
