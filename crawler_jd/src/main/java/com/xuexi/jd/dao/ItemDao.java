package com.xuexi.jd.dao;

import com.xuexi.jd.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<T, ID> T 对应的是需要操作的实体类,ID是对应的主键类型
public interface ItemDao extends  JpaRepository<Item, Long> {
}
