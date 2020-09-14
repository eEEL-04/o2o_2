package com.jhc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.o2o.cache.JedisUtil;
import com.jhc.o2o.dao.ShopCategoryDao;
import com.jhc.o2o.entity.ShopCategory;
import com.jhc.o2o.exceptions.ShopOperationException;
import com.jhc.o2o.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil jedisUtil;

    private static Logger longger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);


    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        //定义redis的key前缀
        String key = SCLISTKEY;
        //定义接收对象
        List<ShopCategory> shopCategoryList = null;
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        //拼接出redis的key
        if (shopCategoryCondition == null) {
            //若查询条件为空，则列出所有首页大类
            key = key + "_allfirstlevel";
        } else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null &&
                shopCategoryCondition.getParent().getShopCategoryId() != null) {
            //若parentId不为空，则列出该parentId 下的所有子类别
            key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else if (shopCategoryCondition != null) {
            //列出所有子类别
            key = key + "_allsecondlevel";
        }

        //判断key是否存在
        if (!jedisKeys.exists(key)) {
            //不存在则从数据库里提取相应的数据
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            //将相关的实体类礼盒转换成String，存入redis里对应的key中
            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                longger.error(e.getMessage());
                throw new ShopOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonString);
            jedisUtil.expire(key, 3600);
        } else {
            //存在则直接从redis里面取出相应的数据
            String jsonString = jedisStrings.get(key);
            //指定要将String转换成的集合类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                //将相关key对应的value里的String转换成对象的实体类集合
                shopCategoryList = mapper.readValue(jsonString, javaType);
            } catch (Exception e) {
                e.printStackTrace();
                longger.error(e.getMessage());
                throw new ShopOperationException(e.getMessage());
            }
        }
        return shopCategoryList;
    }
}
