<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-row>
            <el-form-item label="项目" prop="projectName">
              <el-input
                  v-model="filters.projectName"
                  :size="size"
                  clearable
                  placeholder="请输入项目"
              />
            </el-form-item>
            <el-form-item label="模具" prop="mold">
              <el-input
                  v-model="filters.mold"
                  :size="size"
                  clearable
                  placeholder="请输入模具"
              />
            </el-form-item>
            <el-form-item label="周期" prop="cycle">
              <el-input
                  v-model="filters.cycle"
                  :size="size"
                  clearable
                  placeholder="请输入周期"
              />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="日期 从" prop="productionDate">
              <el-date-picker v-model="filters.productionDateStart" auto-complete="off" type="date"
                              value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
            <el-form-item label="到" prop="productionDate">
              <el-date-picker v-model="filters.productionDateEnd" auto-complete="off" type="date"
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
          </el-form-item>
          <el-form-item>
            <el-button :loading="exportLoading" :size="size" type="success"
                       @click="exportExcelData('每日产出报表')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <QueryAllTable id="condDataTable" ref="queryAllTable" :columns="columns" :data="pageResult"
                     :height="550" :highlightCurrentRow="true" :span-method="objectSpanMethod"
                     :stripe="true"
                     @findPage="findPage">
      </QueryAllTable>

    </div>
  </div>
</template>

<script>

import QueryAllTable from "@/components/QueryAllTable";
import {exportProductionDayExcel, findProductionDayReportPage} from "@/api/wlg/report/productionDayReport";

export default {
  name: "productionDayReport",
  components: {QueryAllTable},
  data() {
    return {
      size: 'default',
      queryLoading: false,
      exportLoading: false,
      filters: {
        projectName: '',
        mold: '',
        cycle: '',
        productionDateStart: '',
        productionDateEnd: '',
      },
      columns: [
        {prop: "seq", label: "序号", minWidth: 80},
        {prop: "productionDate", label: "日期", minWidth: 120, formatter: this.dateFormat},
        {prop: "projectName", label: "项目", minWidth: 120},
        {prop: "mold", label: "模具", minWidth: 110},
        {prop: "cycle", label: "周期", minWidth: 100},
        {prop: "JHXUESHU", label: "计划收料穴", minWidth: 120},
        {prop: "estimateHoleQty", label: "实际收料穴", minWidth: 120},
        {prop: "JHHDCHANCHU", label: "计划产出", minWidth: 120},
        {prop: "fpy", label: "预估直通率", minWidth: 120},
        {prop: "YUGUMOYA", label: "预估模压产出", minWidth: 120},
        {prop: "inventoryQty", label: "实际入库", minWidth: 120},
        // {prop: "afterOutputQty", label: "待入库", minWidth: 120},
      ],
      pageRequest: {},
      pageResult: {},

    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.projectName = this.filters.projectName;
      this.pageRequest.mold = this.filters.mold;
      this.pageRequest.cycle = this.filters.cycle;
      this.pageRequest.dateStart = this.filters.productionDateStart;
      this.pageRequest.dateEnd = this.filters.productionDateEnd;

      this.queryLoading = true;
      findProductionDayReportPage(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult.records = responseData.data
          this.$message.success(responseData.msg)
        } else {
          this.pageResult = [];
          this.$message.error(responseData.msg + "," + responseData.data);
        }
        this.queryLoading = false;
      }).then(data != null ? data.callback : '')
    },

    exportExcelData(excelFileName) {
      this.pageRequest.projectName = this.filters.projectName;
      this.pageRequest.mold = this.filters.mold;
      this.pageRequest.cycle = this.filters.cycle;
      this.pageRequest.dateStart = this.filters.productionDateStart;
      this.pageRequest.dateEnd = this.filters.productionDateEnd;

      this.exportLoading = true;
      exportProductionDayExcel(this.pageRequest).then(res => {
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

    objectSpanMethod: function ({row, column, rowIndex, columnIndex}) {
      console.log(row[column]);
      if (columnIndex == 2) {
        if (rowIndex % 2 == 0) {
          return {
            rowspan: 2,
            colspan: 1,
          }
        } else {
          return {
            rowspan: 0,
            colspan: 0,
          }
        }
      }
    },
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD')
    },
    getCurrentMonthFirst() {
      var date = new Date()
      date.setDate(1)
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10) month = '0' + month
      if (day < 10) day = '0' + day
      this.filters.productionDateStart = date.getFullYear() + '-' + month + '-' + day
    },
    getCurrentMonthLast() {
      var date = new Date()
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10) month = '0' + month
      if (day < 10) day = '0' + day
      this.filters.productionDateEnd = date.getFullYear() + '-' + month + '-' + day
    },

  },
  mounted() {
    this.getCurrentMonthFirst();
    this.getCurrentMonthLast();
  }
}
</script>
