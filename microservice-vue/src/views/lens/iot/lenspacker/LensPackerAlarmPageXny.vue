<template>
  <div>
    <div class="aac-container">
      <div style="margin-bottom: 10px">
        <el-date-picker
            v-model="dateTimePickerValue"
            :shortcuts="shortcuts"
            end-placeholder="结束日期"
            range-separator="至"
            start-placeholder="开始日期"
            type="datetimerange">
        </el-date-picker>
        <el-button style="margin-left: 10px" type="primary"
                   @click="getAlarmData">查询
          <template #icon>
            <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
          </template>
        </el-button>
      </div>
      <el-row>
        <el-col :span="12">
          <div style="margin-bottom: 10px">
            <el-button size="small" type="success"
                       @click="exportExcel('#alarmDetails', 'AlarmDetailsData.xlsx')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </div>
          <el-table
              id="alarmDetails"
              v-loading="alarmDetailLoading"
              :data="lensPackerAlarmDetails"
              border
              height="600"
              stripe
              style="width: 95%">
            <el-table-column
                label="机台名"
                prop="monitMcName"
                width="85">
            </el-table-column>
            <el-table-column
                label="报警种类"
                prop="alarmCode"
                width="80">
            </el-table-column>
            <el-table-column
                :formatter="dateFormat"
                label="开始时间"
                prop="startTime"
                width="160">
            </el-table-column>
            <el-table-column
                :formatter="dateFormat"
                label="结束时间"
                prop="endTime"
                width="160">
            </el-table-column>
            <el-table-column
                label="报警描述"
                prop="description">
            </el-table-column>
            <el-table-column
                label="单次报警处理时间"
                prop="duration">
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="12">
          <div style="margin-bottom: 10px">
            <el-button size="small" type="success"
                       @click="exportExcel('#alarmCount', 'AlarmCountData.xlsx')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </div>
          <el-table
              id="alarmCount"
              v-loading="alarmCountLoading"
              :data="lensPackerAlarmCount"
              border
              height="600"
              stripe
              style="width: 95%">
            <el-table-column
                label="机台名"
                prop="monitMcName"
                width="85">
            </el-table-column>
            <el-table-column
                label="报警种类"
                prop="alarmCode"
                width="80">
            </el-table-column>
            <el-table-column
                label="报警描述"
                prop="description">
            </el-table-column>
            <el-table-column
                label="处理时间S"
                prop="duration">
            </el-table-column>
            <el-table-column
                label="报警次数"
                prop="alarmCount">
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
import {getAlarmCount, getAlarmDetail} from "@/api/lens/iot/lenspackerXny";

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
