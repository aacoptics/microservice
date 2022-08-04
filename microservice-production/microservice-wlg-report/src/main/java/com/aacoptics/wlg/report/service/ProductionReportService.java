package com.aacoptics.wlg.report.service;


import com.aacoptics.wlg.report.entity.param.ProductionDayReportQueryParam;
import com.aacoptics.wlg.report.entity.param.ProductionMonthReportQueryParam;
import com.aacoptics.wlg.report.entity.param.ProductionProjectReportQueryParam;
import com.alibaba.fastjson.JSONArray;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProductionReportService {

    /**
     * 根据条件查询生产月度汇总数据
     *
     * @return
     */
    List<Map<String, Object>> queryProductionMonthReportByCondition(ProductionMonthReportQueryParam productionMonthReportQueryParam);


    /**
     * 根据条件查询月度汇总数据标题
     *
     * @return
     */
    JSONArray queryProductionMonthReportTitleByCondition(ProductionMonthReportQueryParam productionMonthReportQueryParam);


    /**
     * 根据条件查询生产日报表数据
     *
     * @return
     */
    List<Map<String, Object>> queryProductionDayReportByCondition(ProductionDayReportQueryParam productionDayReportQueryParam);



    /**
     * 根据条件查询单个项目报表表头
     *
     * @return
     */
    JSONArray queryProductionProjectReportTitleByCondition(ProductionProjectReportQueryParam productionProjectReportQueryParam);

    /**
     * 根据条件查询单个项目生产报表数据
     *
     * @return
     */
    List<Map<String, Object>> queryProductionProjectReportByCondition(ProductionProjectReportQueryParam productionProjectReportQueryParam);


    /**
     * 查询需要推送到钉钉的生产数据
     *
     * @return
     */
    List<Map<String, Object>> queryProductionDayDataByDate(LocalDate monthStart, LocalDate productionDate);


    /**
     * 获取区间日期
     * @return
     */
    List<String> findProductionReportDateByCondition(String projectName, String mold, String cycle, LocalDate dateStart, LocalDate dateEnd);


    /**
     * 查询需要以表格图片形式推送到钉钉的生产数据
     *
     * @return
     */
    List<Map<String, Object>> findProductionDayReportDataByDate(LocalDate monthStart, LocalDate monthEnd, LocalDate productionDate);


    /**
     * 查询客户需求
     *
     * @param monthStart 需求月份开始日期
     * @return
     */
    public List<Map<String, Object>> findCustomerRequirementDataByDate(LocalDate monthStart, LocalDate productionDate);


    /**
     * 查询客户需求
     *
     * @param productionDate 当前日期
     * @return
     */
    public List<Map<String, Object>> findProductionCurrentDayDataByDate(LocalDate productionDate);


    /**
     * 查询目标交货数据
     *
     * @param monthStart 需求月份开始日期
     *
     * @return
     */
    public List<Map<String, Object>> findTargetDeliveryDataByDate(LocalDate monthStart, LocalDate productionDate);


    /**
     * 查询目标交货料号
     *
     * @param monthStart 需求月份开始日期
     *
     * @return
     */
    public List<String> findItemNumberListByDate(LocalDate monthStart, LocalDate productionDate);

    /**
     * 查询项目，料号关系
     *
     * @param monthStart 需求月份开始日期
     *
     * @return
     */
    public Map<String, String> findProjectNameItemNumberMap(LocalDate monthStart, LocalDate productionDate);

    /**
     * 查询商务项目，内部项目映射关系
     *
     * @param businessProjectList 商务项目列表
     *
     * @return
     */
    public Map<String, String> findProjectMapList(List<String> businessProjectList);

    /**
     * 查询生产汇总数据，包括目标生产数量，实际生产数量，实际出货率
     *
     * @param monthStart 需求月份开始日期
     *
     * @return
     */
    public List<Map<String, Object>> findProductionSummaryDataByDate(LocalDate monthStart, LocalDate productionDate);


    /**
     * 获取实际出货数据
     *
     * @param monthStart 需求月份开始日期
     *
     * @return
     */
    public List<Map<String, Object>> findDeliveryDataByDate(LocalDate monthStart, LocalDate productionDate, List<String> itemNumberList);

    /**
     * 获取实际结存数据
     *
     * @param shiftStart 班次开始时间
     * @param shiftEnd 班次结束时间
     * @param projectNameList 需要统计的项目
     *
     * @return
     */
    public List<Map<String, Object>> findWarehouseBalanceDataByDate(LocalDateTime shiftStart, LocalDateTime shiftEnd, List<String> projectNameList);


}
