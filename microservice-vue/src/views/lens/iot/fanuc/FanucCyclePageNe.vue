<template>
  <div>
    <div class="aac-container">
      <el-form :inline="true" :size="size">
        <el-form-item label="业务发生日期 从" prop="factDate">
          <el-date-picker v-model="factTime.startTime" auto-complete="off" placeholder="请选择开始时间"
                          type="datetime"></el-date-picker>
        </el-form-item>
        <el-form-item label="到" prop="factDate">
          <el-date-picker v-model="factTime.endTime" auto-complete="off" placeholder="请选择结束时间"
                          type="datetime"></el-date-picker>
        </el-form-item>
      </el-form>
      <el-form :inline="true" :size="size">
        <el-form-item>
          <el-button :loading="cycleListLoading" icon="el-icon-search"
                     type="primary" @click="getCycleList">查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-download" type="success"
                     @click="exportExcel('#cycleList', 'CycleList.xlsx')">导出
          </el-button>
        </el-form-item>
      </el-form>

      <el-table
          id="cycleList"
          v-loading="cycleListLoading"
          :data="cycleList"
          border
          height="600"
          stripe
          style="width: 95%">
        <el-table-column
            label="机台名"
            min-width="85"
            prop="monitMcName">
        </el-table-column>
        <el-table-column
            label="周期"
            min-width="85"
            prop="monitCycle">
        </el-table-column>
        <el-table-column
            :formatter="dateFormat"
            label="创建时间"
            min-width="160"
            prop="dbCreateTime">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import {getAllCycleList} from "@/api/lens/iot/fanucNe";
import FileSaver from 'file-saver'
import XLSX from 'xlsx'

export default {
  name: "FanucCyclePage",
  computed: {},
  methods: {
    exportExcel(tableId, excelFileName) {
      const wb = XLSX.utils.table_to_book(document.querySelector(tableId));
      const wbOut = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'});
      try {
        FileSaver.saveAs(new Blob([wbOut], {type: 'application/octet-stream'}), excelFileName)
      } catch (e) {
        if (typeof console !== 'undefined') console.log(e, wbOut)
      }
      return wbOut
    },
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm:ss')
    },
    getCycleList() {
      if (this.factTime.startTime === '' || this.factTime.startTime == null || this.factTime.endTime === '' || this.factTime.endTime == null) {
        this.$message.error("请选择查询时间段");
      } else {
        this.cycleListLoading = true
        getAllCycleList(this.factTime.startTime, this.factTime.endTime).then((response) => {
          const responseData = response.data
          if (responseData.code === '000000') {
            this.cycleList = responseData.data;
          }
          this.cycleListLoading = false
        }).catch(() => {
          this.cycleListLoading = false
        })
      }
    }
  },
  data() {
    return {
      factTime: {
        startTime: '',
        endTime: ''
      },
      cycleList: [],
      cycleListLoading: false
    }
  }
};
</script>
