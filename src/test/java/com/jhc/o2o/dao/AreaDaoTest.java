package com.jhc.o2o.dao;

import com.jhc.o2o.entity.Area;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea(){
        List<Area> areaList = areaDao.queryArea();
        for (Area area : areaList) {
            System.out.println(area);
        }
    }
}
