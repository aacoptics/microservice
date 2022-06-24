package com.aacoptics.gaia.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("Z_Plan_Actual_PerPerson_Everday")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanActualPerPerson implements Serializable {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Site")
    private String site;

    @TableField("work_date")
    private String workDate;

    @TableField("Line_type")
    private String lineType;

    @TableField("Provide")
    private String provide;

    @TableField("station")
    private String station;

    @TableField("Production_Processes")
    private String productionProcesses;

    @TableField("EmployeeID")
    private String employeeID;

    @TableField("Name")
    private String name;

    @TableField("Gy_Class")
    private String gyClass;

    @TableField("salary_daily")
    private BigDecimal salaryDaily;

    @TableField("work_Time")
    private BigDecimal workTime;

    @TableField("split_work_Time")
    private BigDecimal splitWorkTime;

    @TableField("plan_daily_capacity_of_production")
    private BigDecimal planDailyCapacityOfProduction;

    @TableField("convert_daily_capacity_of_production")
    private BigDecimal convertDailyCapacityOfProduction;

    @TableField("salary_by_piece")
    private BigDecimal salaryByPiece;

    @TableField("price")
    private BigDecimal price;

    @TableField("Excess_Price")
    private BigDecimal excessPrice;

    @TableField("actual_daily_capacity_of_production")
    private BigDecimal actualDailyCapacityOfProduction;

    @TableField("capacity_of_production_deduct")
    private BigDecimal capacityOfProductionDeduct;

    @TableField("Comprehensive_capacity_of_production")
    private BigDecimal comprehensiveCapacityOfProduction;

    @TableField("Excess_capacity_of_production")
    private BigDecimal excessCapacityOfProduction;

    @TableField("salary")
    private BigDecimal salary;

    @TableField("gy_transfer_flg")
    private Integer gyTransferFlg;

    @TableField("transfer_err")
    private String transferErr;

    @TableField("creater")
    private String creator;

    @TableField("Createtime")
    private LocalDateTime createdTime;

    @TableField("Updatetime")
    private LocalDateTime updatetime;

    @TableField("Modify")
    private String modify;

    @TableField("dd_transfer_flg")
    private Integer ddTransferFlg;

    @TableField(exist = false)
    private Integer flag;

}
