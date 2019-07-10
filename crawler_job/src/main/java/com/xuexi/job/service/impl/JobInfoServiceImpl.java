package com.xuexi.job.service.impl;

import com.xuexi.job.dao.JobInfoDao;
import com.xuexi.job.pojo.JobInfo;
import com.xuexi.job.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//加service注解用spring容器管理
@Service
public class JobInfoServiceImpl implements JobInfoService {
    @Autowired
    private JobInfoDao jobInfoDao;

    @Transactional
    public void save(JobInfo jobInfo) {
        //查询原有数据
        //通过url和时间进行区分
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        List<JobInfo> list = findJobInfo(param);
        //判断数据中,是否有已存在的数据
        if (list.size() == 0) {
            //如果不存在,或者已经更新了,需要更新或新增数据库
            this.jobInfoDao.saveAndFlush(jobInfo);
        }
    }

    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        //设置查询条件
        Example example = Example.of(jobInfo);
        //执行查询
        List list = jobInfoDao.findAll(example);
        return list;
    }
}
