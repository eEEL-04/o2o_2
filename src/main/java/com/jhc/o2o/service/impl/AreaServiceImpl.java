package com.jhc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.o2o.cache.JedisUtil;
import com.jhc.o2o.dao.AreaDao;
import com.jhc.o2o.entity.Area;
import com.jhc.o2o.exceptions.AreaOperationException;
import com.jhc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil jedisUtil;

    private static Logger longger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Override
    @Transactional
    public List<Area> getAreaList() {
        //定义redis的key前缀
        String key = AREALISTKEY;
        //定义接收对象
        List<Area> areaList = null;
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        //判断key是否存在
        if (!jedisKeys.exists(key)){
            //不存在则从数据库里提取相应的数据
            areaList = areaDao.queryArea();
            //将相关的实体类礼盒转换成String，存入redis里对应的key中
            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                longger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
            jedisUtil.expire(key,3600);
        }else {
            //存在则直接从redis里面取出相应的数据
            String jsonString = jedisStrings.get(key);
            //指定要将String转换成的集合类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
            try {
                //将相关key对应的value里的String转换成对象的实体类集合
                areaList = mapper.readValue(jsonString,javaType);
            } catch (Exception e) {
                e.printStackTrace();
                longger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }

        }
        return areaList;
    }
}
