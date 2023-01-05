package com.aacoptics.temphumidity.config;

import com.aacoptics.temphumidity.service.TemphumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class StaticScheduleTask {

    @Autowired
    public TemphumidityService service;

    //3.添加定时任务
    @Scheduled(cron = "0 */30 * * * ?")
    //或直接指定时间间隔，例如：5秒
    public void updateElectricData()  {
        System.out.println("任务开始时间: " + LocalDateTime.now() + " updateElectricData()");
        try {
            service.updateElectricData();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("任务异常时间: " + LocalDateTime.now() + " updateElectricData()，执行异常：" + e.getMessage());
        }
        System.out.println("任务结束时间: " + LocalDateTime.now() + " updateElectricData()");
    }

    @Scheduled(cron = "0 */5 * * * ?")
    //或直接指定时间间隔，例如：5秒
    public void updateTempHumidityData()  {
        System.out.println("任务开始时间: " + LocalDateTime.now() + " updateTempHumidityData()");
        try {
            service.updateTempHumidityData();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("任务异常时间: " + LocalDateTime.now() + " updateTempHumidityData()，执行异常：" + e.getMessage());
        }
        System.out.println("任务结束时间: " + LocalDateTime.now() + " updateTempHumidityData()");
    }
}
