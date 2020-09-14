package com.jhc.o2o.dao;

import com.jhc.o2o.entity.HeadLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineDaoTest {
    @Autowired
    private HeadLineDao headLineDao;
    
    @Test
    public void testAQueryHeadLine(){
        List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
        for (HeadLine headLine : headLineList) {
            System.out.println(headLine);
        }
    }
}
