package com.xuexi.job.dao;

import com.xuexi.job.pojo.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

//dao实现类继承JpaRepository<JobInfo, Long>第一个泛型是需要操作的实体类,第二个泛型是对应的主键数据类型
public interface JobInfoDao extends JpaRepository<JobInfo, Long> {
}
