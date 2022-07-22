<template>
  <div>
    <div class="aac-container">
      <el-row style="margin-bottom: 20px">
        <el-date-picker
            v-model="dateTimePickerValue"
            type="datetimerange"
            :shortcuts="shortcuts"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            size="large">
        </el-date-picker>
        <el-button type="primary" size="large" style="margin-left: 10px"
                   @click="getAlarmData">查询
          <template #icon>
            <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
          </template>
        </el-button>
      </el-row>
      <el-row>
        <el-col :span="12">
          <div style="margin-bottom: 10px">
            <el-button type="success" size="small"
                       @click="exportExcel('#alarmDetails', 'AlarmDetailsData.xlsx')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </div>
          <el-table
              :data="lensPackerAlarmDetails"
              stripe
              border
              id="alarmDetails"
              style="width: 95%"
              height="600"
              v-loading="alarmDetailLoading">
            <el-table-column
                prop="monitMcName"
                label="机台名"
                width="85">
            </el-table-column>
            <el-table-column
                prop="alarmCode"
                label="报警种类"
                width="80">
            </el-table-column>
            <el-table-column
                prop="startTime"
                label="开始时间"
                width="160"
                :formatter="dateFormat">
            </el-table-column>
            <el-table-column
                prop="endTime"
                label="结束时间"
                width="160"
                :formatter="dateFormat">
            </el-table-column>
            <el-table-column
                prop="description"
                label="报警描述">
            </el-table-column>
            <el-table-column
                prop="duration"
                label="单次报警处理时间">
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="12">
          <div style="margin-bottom: 10px">
            <el-button type="success" size="small"
                       @click="exportExcel('#alarmCount', 'AlarmCountData.xlsx')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </div>
          <el-table
              :data="lensPackerAlarmCount"
              stripe
              border
              id="alarmCount"
              style="width: 95%"
              height="600"
              v-loading="alarmCountLoading">
            <el-table-column
                prop="monitMcName"
                label="机台名"
                width="85">
            </el-table-column>
            <el-table-column
                prop="alarmCode"
                label="报警种类"
                width="80">
            </el-table-column>
            <el-table-column
                prop="description"
                label="报警描述">
            </el-table-column>
            <el-table-column
                prop="duration"
                label="处理时间S">
            </el-table-column>
            <el-table-column
                prop="alarmCount"
                label="报警次数">
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
import {getAlarmDetail, getAlarmCount} from "@/api/lens/iot/lenspackerXny";

export default {
  name: "LensPackerAlarmPage",
  data() {
    return {
      alarmDetailLoading: false,
      alarmCountLoading: false,
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
      lensPackerAlarmDetails: [],
      lensPackerAlarmCount: []
    }
  },
  methods: {
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm:ss')
    },
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
    getLensPackerAlarmDetail() {
      if (this.dateTimePickerValue.length !== 2) {
        this.$message.error("请选择查询时间段");
      } else {
        const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
        const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
        getAlarmDetail(startTime, endTime).then((response) => {
          const responseData = response.data
          if (responseData.code === '000000') {
            this.lensPackerAlarmDetails = responseData.data;
          }
          this.alarmDetailLoading = false
        }).catch(() => {
          this.alarmDetailLoading = false
        })
      }
    },
    getLensPackerAlarmCount() {
      if (this.dateTimePickerValue.length !== 2) {
        this.$message.error("请选择查询时间段");
      } else {
        const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
        const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
        getAlarmCount(startTime, endTime).then((response) => {
          const responseData = response.data
          if (responseData.code === '000000') {
            this.lensPackerAlarmCount = responseData.data;
          }
          this.alarmCountLoading = false
        }).catch(() => {
          this.alarmCountLoading = false
        })
      }
    },
    getAlarmData() {
      this.alarmDetailLoading = true
      this.alarmCountLoading = true
      this.getLensPackerAlarmCount()
      this.getLensPackerAlarmDetail()
    }
  }
}
</script>

<style scoped>

</style>
