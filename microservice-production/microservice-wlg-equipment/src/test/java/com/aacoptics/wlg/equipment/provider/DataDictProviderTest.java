package com.aacoptics.wlg.equipment.provider;

import com.aacoptics.common.core.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class DataDictProviderTest {

    @Resource
    DataDictProvider dataDictProvider;

    @Test
    void getDataDictList() {
       Result result = dataDictProvider.getDataDictList("wlg_em_maintenance_order_status");
       if(result.isFail())
       {
           System.out.println(result.getMsg());
       }

       System.out.println(result);
    }
}