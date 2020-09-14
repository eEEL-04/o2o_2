package com.jhc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.o2o.cache.JedisUtil;
import com.jhc.o2o.dao.HeadLineDao;
import com.jhc.o2o.entity.HeadLine;
import com.jhc.o2o.exceptions.HeadLineOperationException;
import com.jhc.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil jedisUtil;

    private static Logger longger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Override
    public List<HeadLine> getHeadlineList(HeadLine headLineCondition) throws IOException {
        //定义redis的key前缀
        String key = HLLISTKEY;
        //定义接收对象
        List<HeadLine> headLineList = null;
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        //拼接出redis的key
        if (headLineCondition!=null && headLineCondition.getEnableStatus()!=null){
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        //判断key是否存在
        if (!jedisKeys.exists(key)){
            //不存在则从数据库里提取相应的数据
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            //将相关的实体类礼盒转换成String，存入redis里对应的key中
            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(headLineList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                longger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
            jedisUtil.expire(key,3600);
        }else {
            //存在则直接从redis里面取出相应的数据
            String jsonString = jedisStrings.get(key);
            //指定要将String转换成的集合类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            try {
                //将相关key对应的value里的String转换成对象的实体类集合
                headLineList=mapper.readValue(jsonString,javaType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                longger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }

        }
        return headLineList;
    }
}
