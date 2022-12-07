<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <!-- <el-row>
            <el-form-item label="工段类型" prop="sectionType">
              <el-input
                  v-model="filters.sectionType"
                  :size="size"
                  clearable
                  placeholder="请输入工段类型"
              />
            </el-form-item>
          </el-row> -->
          <el-row>
            <el-form-item label="日期 从" prop="createDateStart">
              <el-date-picker v-model="filters.createDateStart" auto-complete="off" type="date"
                              value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
            <el-form-item label="到" prop="createDateEnd">
              <el-date-picker v-model="filters.createDateEnd" auto-complete="off" type="date"
                              value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
          </el-row>
        </el-form>

        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="queryLoading" type="primary"
                       @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
            <el-button :loading="exportLoading" :size="size" type="success"
                       @click="exportExcelData('工段工单统计报表')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <QueryAllTable id="condDataTable" ref="queryAllTable" :columns="columns" :data="pageResult" :size="size"
                     :height="550" :highlightCurrentRow="true" border
                     :stripe="true"
                     @findPage="findPage">
      </QueryAllTable>
    </div>
  </div>
</template>

<script>

import QueryAllTable from "@/components/QueryAllTable";
import {
  exportSectionOrderCountExcel,
  findSectionOrderCount
} from "@/api/wlg/equipment/sectionOrderCountReport";

export default {
  name: "sectionOrderCountReport",
  components: {QueryAllTable},
  data() {
    return {
      size: 'default',
      queryLoading: false,
      exportLoading: false,

      filters: {
        sectionType: '',
        createDateStart: '',
        createDateEnd: '',
      },
      columns: [
        {prop: "sectionType", label: "工段类型", minWidth: 150},
        {prop: "inspectionOrderCount", label: "需点检工单数", minWidth: 150},
        {prop: "committedInspectionOrderCount", label: "已提交点检工单数", minWidth: 180},
        {prop: "confirmedInspectionOrderCount", label: "已确认点检工单数", minWidth: 180},
        {prop: "unfinishedInspectionOrderCount", label: "未完成点检工单数", minWidth: 180},
        {prop: "maintenanceOrderCount", label: "需保养工单数", minWidth: 150},
        {prop: "committedMaintenanceOrderCount", label: "已提交保养工单数", minWidth: 180},
        {prop: "confirmedMaintenanceOrderCount", label: "已确认保养工单数", minWidth: 180},
        {prop: "unfinishedMaintenanceOrderCount", label: "未完成保养工单数", minWidth: 180},
        {prop: "repairOrderCount", label: "需维修工单数", minWidth: 150},
        {prop: "committedRepairOrderCount", label: "已提交维修工单数", minWidth: 180},
        {prop: "confirmedRepairOrderCount", label: "已确认维修工单数", minWidth: 180},
        {prop: "unfinishedRepairOrderCount", label: "未完成维修工单数", minWidth: 180},
      ],
      pageRequest: {},
      pageResult: {
        tableData: []
      },

      excelUploadDialogVisible: false,
    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.sectionType = this.filters.sectionType;
      if(this.filters.createDateStart != null && this.filters.createDateStart != '')
      {
        this.pageRequest.createDateStart = this.filters.createDateStart + " 00:00:00";
      }
      if(this.filters.createDateEnd != null && this.filters.createDateEnd != '')
      {
        this.pageRequest.createDateEnd = this.filters.createDateEnd + " 23:59:59";
      }

      this.queryLoading = true;
      findSectionOrderCount(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult.records = responseData.data
          this.$message.success(responseData.msg)
        } else {
          this.pageResult.records = [];
          this.$message.error(responseData.msg + "," + responseData.data);
        }
        this.queryLoading = false;
      }).then(data != null ? data.callback : '')
    },
    exportExcelData(excelFileName) {
      this.pageRequest.sectionType = this.filters.sectionType;
      if(this.filters.createDateStart != null && this.filters.createDateStart != '')
      {
        this.pageRequest.createDateStart = this.filters.createDateStart + " 00:00:00";
      }
      if(this.filters.createDateEnd != null && this.filters.createDateEnd != '')
      {
        this.pageRequest.createDateEnd = this.filters.createDateEnd + " 23:59:59";
      }

      this.exportLoading = true;
      exportSectionOrderCountExcel(this.pageRequest).then(res => {
        this.exportLoading = false;
        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', excelFileName + "-" + new Date().getTime() + ".xlsx");
        document.body.appendChild(link);
        link.click();
      });
    },
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD')
    },
    getCurrentWeekFirst() {
      var date = new Date()
      date.setDate(1)
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10) month = '0' + month
      if (day < 10) day = '0' + day
      this.filters.createDateStart = date.getFullYear() + '-' + month + '-' + day
    },
    getCurrentWeekLast() {
      var date = new Date()
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10) month = '0' + month
      if (day < 10) day = '0' + day
      this.filters.createDateEnd = date.getFullYear() + '-' + month + '-' + day
    },
  },
  mounted() {
    this.getCurrentWeekFirst();
    this.getCurrentWeekLast();
  }
}
</script>
