<template>
  <div>
    <div class="aac-container">
      <div style="margin-bottom: 10px">
        <el-date-picker
            v-model="dateTimePickerValue"
            type="datetimerange"
            :shortcuts="shortcuts"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
        </el-date-picker>
        <el-button type="primary" style="margin-left: 10px"
                   @click="getLensPackerCapacityData">查询
          <template #icon>
            <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
          </template>
        </el-button>
        <el-button type="success"
                   @click="exportExcel('#capacityTable', 'CapacityData.xlsx')">导出
          <template #icon>
            <font-awesome-icon :icon="['fas','download']"/>
          </template>
        </el-button>
      </div>
      <el-row>
        <el-col :span="24">
          <el-table
              :data="lensPackerCapacity"
              stripe
              border
              id="capacityTable"
              style="width: 95%"
              height="600"
              v-loading="capacityLoading">
            <el-table-column
                prop="machineName"
                label="机台号">
            </el-table-column>
            <el-table-column
                prop="capacity"
                label="产能">
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import XLSX from "xlsx";
import FileSaver from 'file-saver'
import {getMachineCapacity} from "@/api/lens/iot/lenspackerXny";

export default {
  name: "LensPackerAlarmPage",
  data() {
    return {
      capacityLoading: false,
      dateTimePickerValue: [],
      shortcuts: [{
        text: '最近一天',
        value: (() => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24);
          return [start, end]
        })()
      }],
      lensPackerCapacity: [],
    }
  },
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
    getLensPackerCapacityData() {
      this.capacityLoading = true
      if (this.dateTimePickerValue.length !== 2) {
        this.$message.error("请选择查询时间段");
      } else {
        const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
        const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
        getMachineCapacity(startTime, endTime).then((response) => {
          const responseData = response.data
          if (responseData.code === '000000') {
            this.lensPackerCapacity = responseData.data;
          }
          this.capacityLoading = false
        }).catch(() => {
          this.capacityLoading = false
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
