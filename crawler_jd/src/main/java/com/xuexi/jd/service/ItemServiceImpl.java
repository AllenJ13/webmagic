package com.xuexi.jd.service;

import com.xuexi.jd.dao.ItemDao;
import com.xuexi.jd.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemDao itemDao;


    @Transactional
    public void save(Item item) {
        itemDao.save(item);
    }


    public List<Item> findAll(Item item) {
        //声明查询条件
        Example<Item> example = Example.of(item);
        //根据条件查询数据
        List<Item> itemList = itemDao.findAll(example);
        return itemList;
    }

}
