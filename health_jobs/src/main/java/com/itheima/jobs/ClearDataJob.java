package com.itheima.jobs;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.OrderSettingService;


import org.springframework.web.bind.annotation.RestController;

@RestController
 public class ClearDataJob {

    @Reference
    private OrderSettingService settingService;

    /**
       * 清理数据
      * @param
       */
//  定时定时清理预约设置历史数据
    public void deleteIncreament(){
        System.out.println("每月最后一天凌晨2点执行一次清理任务");
        settingService.CleanByLastDate();
    }

}