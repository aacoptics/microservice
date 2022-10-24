package com.aacoptics.budget.report.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("fb_product_line_budget")
public class ProductLineBudget extends BasePo {

    /**
     * 事业部
     */
    @TableField(value = "business_division")
    private String businessDivision;


    /**
     * 产品线
     */
    @TableField(value = "product_line")
    private String productLine;

    /**
     * 数据版本
     */
    @TableField(value = "data_version")
    private String dataVersion;


    /**
     * 科目序号
     */
    @TableField(value = "item_seq")
    private Integer itemSeq;


    /**
     * 科目
     */
    @TableField(value = "cost_item")
    private String costItem;


    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;


    /**
     * 年份
     */
    @TableField(value = "year")
    private Integer year;

    /**
     * 手机类
     */
    @TableField(value = "month_01_phone_value")
    private BigDecimal month01PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_01_tv_value")
    private BigDecimal month01TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_01_watch_value")
    private BigDecimal month01WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_01_ar_vr_value")
    private BigDecimal month01ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_01_laptop_value")
    private BigDecimal month01LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_01_tablet_value")
    private BigDecimal month01TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_01_vehicle_value")
    private BigDecimal month01VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_01_iot_other_value")
    private BigDecimal month01IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_01_total_value")
    private BigDecimal month01TotalValue;


    /**
     * 手机类
     */
    @TableField(value = "month_02_phone_value")
    private BigDecimal month02PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_02_tv_value")
    private BigDecimal month02TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_02_watch_value")
    private BigDecimal month02WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_02_ar_vr_value")
    private BigDecimal month02ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_02_laptop_value")
    private BigDecimal month02LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_02_tablet_value")
    private BigDecimal month02TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_02_vehicle_value")
    private BigDecimal month02VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_02_iot_other_value")
    private BigDecimal month02IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_02_total_value")
    private BigDecimal month02TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_03_phone_value")
    private BigDecimal month03PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_03_tv_value")
    private BigDecimal month03TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_03_watch_value")
    private BigDecimal month03WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_03_ar_vr_value")
    private BigDecimal month03ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_03_laptop_value")
    private BigDecimal month03LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_03_tablet_value")
    private BigDecimal month03TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_03_vehicle_value")
    private BigDecimal month03VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_03_iot_other_value")
    private BigDecimal month03IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_03_total_value")
    private BigDecimal month03TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "q1_phone_value")
    private BigDecimal q1PhoneValue;

    /**
     * TV
     */
    @TableField(value = "q1_tv_value")
    private BigDecimal q1TvValue;

    /**
     * 手表
     */
    @TableField(value = "q1_watch_value")
    private BigDecimal q1WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "q1_ar_vr_value")
    private BigDecimal q1ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "q1_laptop_value")
    private BigDecimal q1LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "q1_tablet_value")
    private BigDecimal q1TabletValue;

    /**
     * 车载
     */
    @TableField(value = "q1_vehicle_value")
    private BigDecimal q1VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "q1_iot_other_value")
    private BigDecimal q1IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "q1_total_value")
    private BigDecimal q1TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_04_phone_value")
    private BigDecimal month04PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_04_tv_value")
    private BigDecimal month04TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_04_watch_value")
    private BigDecimal month04WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_04_ar_vr_value")
    private BigDecimal month04ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_04_laptop_value")
    private BigDecimal month04LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_04_tablet_value")
    private BigDecimal month04TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_04_vehicle_value")
    private BigDecimal month04VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_04_iot_other_value")
    private BigDecimal month04IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_04_total_value")
    private BigDecimal month04TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_05_phone_value")
    private BigDecimal month05PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_05_tv_value")
    private BigDecimal month05TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_05_watch_value")
    private BigDecimal month05WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_05_ar_vr_value")
    private BigDecimal month05ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_05_laptop_value")
    private BigDecimal month05LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_05_tablet_value")
    private BigDecimal month05TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_05_vehicle_value")
    private BigDecimal month05VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_05_iot_other_value")
    private BigDecimal month05IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_05_total_value")
    private BigDecimal month05TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_06_phone_value")
    private BigDecimal month06PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_06_tv_value")
    private BigDecimal month06TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_06_watch_value")
    private BigDecimal month06WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_06_ar_vr_value")
    private BigDecimal month06ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_06_laptop_value")
    private BigDecimal month06LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_06_tablet_value")
    private BigDecimal month06TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_06_vehicle_value")
    private BigDecimal month06VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_06_iot_other_value")
    private BigDecimal month06IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_06_total_value")
    private BigDecimal month06TotalValue;


    /**
     * 手机类
     */
    @TableField(value = "q2_phone_value")
    private BigDecimal q2PhoneValue;

    /**
     * TV
     */
    @TableField(value = "q2_tv_value")
    private BigDecimal q2TvValue;

    /**
     * 手表
     */
    @TableField(value = "q2_watch_value")
    private BigDecimal q2WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "q2_ar_vr_value")
    private BigDecimal q2ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "q2_laptop_value")
    private BigDecimal q2LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "q2_tablet_value")
    private BigDecimal q2TabletValue;

    /**
     * 车载
     */
    @TableField(value = "q2_vehicle_value")
    private BigDecimal q2VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "q2_iot_other_value")
    private BigDecimal q2IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "q2_total_value")
    private BigDecimal q2TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_07_phone_value")
    private BigDecimal month07PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_07_tv_value")
    private BigDecimal month07TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_07_watch_value")
    private BigDecimal month07WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_07_ar_vr_value")
    private BigDecimal month07ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_07_laptop_value")
    private BigDecimal month07LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_07_tablet_value")
    private BigDecimal month07TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_07_vehicle_value")
    private BigDecimal month07VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_07_iot_other_value")
    private BigDecimal month07IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_07_total_value")
    private BigDecimal month07TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_08_phone_value")
    private BigDecimal month08PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_08_tv_value")
    private BigDecimal month08TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_08_watch_value")
    private BigDecimal month08WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_08_ar_vr_value")
    private BigDecimal month08ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_08_laptop_value")
    private BigDecimal month08LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_08_tablet_value")
    private BigDecimal month08TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_08_vehicle_value")
    private BigDecimal month08VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_08_iot_other_value")
    private BigDecimal month08IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_08_total_value")
    private BigDecimal month08TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_09_phone_value")
    private BigDecimal month09PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_09_tv_value")
    private BigDecimal month09TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_09_watch_value")
    private BigDecimal month09WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_09_ar_vr_value")
    private BigDecimal month09ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_09_laptop_value")
    private BigDecimal month09LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_09_tablet_value")
    private BigDecimal month09TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_09_vehicle_value")
    private BigDecimal month09VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_09_iot_other_value")
    private BigDecimal month09IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_09_total_value")
    private BigDecimal month09TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "q3_phone_value")
    private BigDecimal q3PhoneValue;

    /**
     * TV
     */
    @TableField(value = "q3_tv_value")
    private BigDecimal q3TvValue;

    /**
     * 手表
     */
    @TableField(value = "q3_watch_value")
    private BigDecimal q3WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "q3_ar_vr_value")
    private BigDecimal q3ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "q3_laptop_value")
    private BigDecimal q3LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "q3_tablet_value")
    private BigDecimal q3TabletValue;

    /**
     * 车载
     */
    @TableField(value = "q3_vehicle_value")
    private BigDecimal q3VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "q3_iot_other_value")
    private BigDecimal q3IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "q3_total_value")
    private BigDecimal q3TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_10_phone_value")
    private BigDecimal month10PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_10_tv_value")
    private BigDecimal month10TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_10_watch_value")
    private BigDecimal month10WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_10_ar_vr_value")
    private BigDecimal month10ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_10_laptop_value")
    private BigDecimal month10LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_10_tablet_value")
    private BigDecimal month10TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_10_vehicle_value")
    private BigDecimal month10VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_10_iot_other_value")
    private BigDecimal month10IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_10_total_value")
    private BigDecimal month10TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_11_phone_value")
    private BigDecimal month11PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_11_tv_value")
    private BigDecimal month11TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_11_watch_value")
    private BigDecimal month11WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_11_ar_vr_value")
    private BigDecimal month11ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_11_laptop_value")
    private BigDecimal month11LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_11_tablet_value")
    private BigDecimal month11TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_11_vehicle_value")
    private BigDecimal month11VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_11_iot_other_value")
    private BigDecimal month11IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_11_total_value")
    private BigDecimal month11TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "month_12_phone_value")
    private BigDecimal month12PhoneValue;

    /**
     * TV
     */
    @TableField(value = "month_12_tv_value")
    private BigDecimal month12TvValue;

    /**
     * 手表
     */
    @TableField(value = "month_12_watch_value")
    private BigDecimal month12WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "month_12_ar_vr_value")
    private BigDecimal month12ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "month_12_laptop_value")
    private BigDecimal month12LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "month_12_tablet_value")
    private BigDecimal month12TabletValue;

    /**
     * 车载
     */
    @TableField(value = "month_12_vehicle_value")
    private BigDecimal month12VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "month_12_iot_other_value")
    private BigDecimal month12IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "month_12_total_value")
    private BigDecimal month12TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "q4_phone_value")
    private BigDecimal q4PhoneValue;

    /**
     * TV
     */
    @TableField(value = "q4_tv_value")
    private BigDecimal q4TvValue;

    /**
     * 手表
     */
    @TableField(value = "q4_watch_value")
    private BigDecimal q4WatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "q4_ar_vr_value")
    private BigDecimal q4ArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "q4_laptop_value")
    private BigDecimal q4LaptopValue;

    /**
     * 平板
     */
    @TableField(value = "q4_tablet_value")
    private BigDecimal q4TabletValue;

    /**
     * 车载
     */
    @TableField(value = "q4_vehicle_value")
    private BigDecimal q4VehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "q4_iot_other_value")
    private BigDecimal q4IotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "q4_total_value")
    private BigDecimal q4TotalValue;

    /**
     * 手机类
     */
    @TableField(value = "year_phone_value")
    private BigDecimal yearPhoneValue;

    /**
     * TV
     */
    @TableField(value = "year_tv_value")
    private BigDecimal yearTvValue;

    /**
     * 手表
     */
    @TableField(value = "year_watch_value")
    private BigDecimal yearWatchValue;

    /**
     * AR/VR
     */
    @TableField(value = "year_ar_vr_value")
    private BigDecimal yearArVrValue;

    /**
     * 笔电
     */
    @TableField(value = "year_laptop_value")
    private BigDecimal yearLaptopValue;

    /**
     * 平板
     */
    @TableField(value = "year_tablet_value")
    private BigDecimal yearTabletValue;

    /**
     * 车载
     */
    @TableField(value = "year_vehicle_value")
    private BigDecimal yearVehicleValue;

    /**
     * IOT/其他
     */
    @TableField(value = "year_iot_other_value")
    private BigDecimal yearIotOtherValue;


    /**
     * 小计
     */
    @TableField(value = "year_total_value")
    private BigDecimal yearTotalValue;


    /**
     * 上传日志ID
     */
    private Long uploadLogId;

}
