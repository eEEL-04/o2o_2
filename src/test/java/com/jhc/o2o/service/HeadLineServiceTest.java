package com.jhc.o2o.service;

import com.jhc.o2o.entity.HeadLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineServiceTest {
    @Autowired
    private HeadLineService headLineService;

    @Test
    public void testGetHeadlineList() throws IOException {
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        List<HeadLine> headlineList = headLineService.getHeadlineList(headLineCondition);
        for (HeadLine headLine : headlineList) {
            System.out.println(headLine);
        }

    }
}
