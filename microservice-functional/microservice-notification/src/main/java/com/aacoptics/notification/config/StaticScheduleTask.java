//package com.aacoptics.notification.config;
//
//import com.aacoptics.notification.schedule.DynamicNotifyTask;
//import com.aacoptics.notification.service.PlanService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
//public class StaticScheduleTask  implements SchedulingConfigurer {
//
//    @Resource
//    PlanService planService;
//
//    @Resource
//    DynamicNotifyTask task;
//
//    private static ScheduledTaskRegistrar taskRegistrar;
//
//    //3.添加定时任务，每天凌晨2点触发
//    @Scheduled(cron = "0 0 2 * * ?")
//    //或直接指定时间间隔，例如：5秒
//    public void refreshDashboards()
//    {
//        String resultMSG = planService.updateUser();
//        System.out.println("任务时间: " + LocalDateTime.now() + " 任务信息：" + resultMSG);
//    }
//
//    //3.添加定时任务，每隔5分钟触发
//    @Scheduled(cron = "0/5 * * * * ? ")
//    //或直接指定时间间隔，例如：5秒
//    public void refreshTaskRegister()
//    {
//        taskRegistrar.destroy();
//
//        List<Map<String, Object>> scheduleList = planService.queryScheduledPlan();
//
//        for (Map<String, Object> scheduleMap : scheduleList) {
//
//            String planCron = scheduleMap.get("planCron") + "";
//            String planKey = scheduleMap.get("planKey") + "";
//            task.setPlanKey(planKey);
//
//            // 可以通过改变数据库数据进而实现动态改变执行周期
//            taskRegistrar.addTriggerTask(task,
//                    triggerContext -> {
//                        return new CronTrigger(planCron).nextExecutionTime(triggerContext);
//                    }
//            );
//            taskRegistrar.afterPropertiesSet();
//        }
//    }
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//
//        // this.taskRegistrar = taskRegistrar;
//
//        /*List<Map<String, Object>> scheduleList = planService.queryScheduledPlan();
//        for (Map<String, Object> scheduleMap : scheduleList) {
//
//            String planCron = scheduleMap.get("planCron") + "";
//            String planKey = scheduleMap.get("planKey") + "";
//            task.setPlanKey(planKey);
//
//            // 可以通过改变数据库数据进而实现动态改变执行周期
//            taskRegistrar.addTriggerTask(task,
//                    triggerContext -> {
//                        return new CronTrigger(planCron).nextExecutionTime(triggerContext);
//                    }
//            );
//        }*/
//
//        StaticScheduleTask.taskRegistrar = taskRegistrar;
//    }
//}
