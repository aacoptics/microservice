<template>
  <div>
    <div class="aac-container">
      <el-row>
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form ref="formParam" :size="size" label-width="100px" :model="formParam" :rules="queryFormRules">
            <el-row>
              <el-form-item label="机台号" prop="machineNames">
                <el-select v-model="formParam.machineNames"
                           :size="size" 
                           filterable 
                           clearable
                           placeholder="请选择机台号">
                  <el-option
                      v-for="item in machineNameArray"
                      :key="item"
                      :label="item"
                      :value="item"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="项目号" prop="moldFileNames">
                <el-select v-model="formParam.moldFileNames"
                           :size="size" 
                           filterable 
                           clearable
                           placeholder="请选择项目号">
                  <el-option
                      v-for="item in moldFileNameArray"
                      :key="item"
                      :label="item"
                      :value="item"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="时间" prop="dateTimePickerValue">
                <el-date-picker
                    v-model="formParam.dateTimePickerValue"
                    :shortcuts="shortcuts"
                    :size="size"
                    end-placeholder="结束日期"
                    range-separator="至"
                    start-placeholder="开始日期"
                    type="datetimerange">
                </el-date-picker>
              </el-form-item>
            </el-row>

          </el-form>

          <el-form :inline="true" :size="size">
            <el-form-item>
              <el-button :loading="queryLoading"
                         type="primary"
                         @click="getFanucCheckDataEveryDay()">查询
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                </template>
              </el-button>
              <el-button type="success"
                     @click="exportExcel('#paramDataList', 'paramDataList.xlsx')">导出
            <template #icon>
              <font-awesome-icon :icon="['fas','download']"/>
            </template>
          </el-button>
            </el-form-item>
          </el-form>

        </div>
      </el-row>
        <el-table
          id="paramDataList"
          v-loading="queryLoading"
          :data="analysisData"
          border
          height="600"
          stripe
          style="width: 100%">
        <el-table-column label="机台名" min-width="85" prop="monitMcName"></el-table-column>
        <el-table-column label="班次" min-width="180" prop="monitDateTime"></el-table-column>
        <el-table-column label="项目号" min-width="85" prop="monitVPPrs"></el-table-column>
        <el-table-column label="循环时间(s)" min-width="100" prop="monitVPPos"></el-table-column>
        <el-table-column label="循环数" min-width="85" prop="monitBackflw"></el-table-column>
        <el-table-column label="V-P压力(kgf/cm2)" min-width="150" prop="monitRecovTime"></el-table-column>
        <el-table-column label="V-P位置(mm)" min-width="110" prop="monitPeakT"></el-table-column>
        <el-table-column label="计量时间(s)	" min-width="100" prop="monitPeakPrs"></el-table-column>
        <el-table-column label="射出时间(s)" min-width="100" prop="monitPeakPos"></el-table-column>
        <el-table-column label="最小缓冲(mm)" min-width="120" prop="monitMold1"></el-table-column>
        <el-table-column label="取数时间" min-width="85" prop="monitMold2"></el-table-column>
        <el-table-column label="喷嘴1温度(℃)" min-width="120" prop="monitMold3"></el-table-column>
        <el-table-column label="料筒1温度(℃)" min-width="120" prop="monitMold4"></el-table-column>
        <el-table-column label="料筒2温度(℃)" min-width="120" prop="monitMold5"></el-table-column>
        <el-table-column label="料筒3温度(℃)" min-width="120" prop="monitMold6"></el-table-column>
        <el-table-column label="料斗下温度(℃)" min-width="120" prop="monitMold7"></el-table-column>
        <el-table-column label="自动模厚锁模力(TON)" min-width="180" prop="monitMold8"></el-table-column>
        <el-table-column label="喷嘴1设定温度(℃)" min-width="150" prop="monitInjTime"></el-table-column>
        <el-table-column label="料筒1设定温度(℃)" min-width="150" prop="monitInjStartPos"></el-table-column>
        <el-table-column label="料筒2设定温度(℃)" min-width="150" prop="monitCycle"></el-table-column>
        <el-table-column label="料筒3设定温度(℃)" min-width="150" prop="monitBarrel1"></el-table-column>
        <el-table-column label="料斗下设定温度(℃)" min-width="150" prop="monitBarrel2"></el-table-column>
        <el-table-column label="背压1(kgf/cm2)" min-width="130" prop="monitBarrel3"></el-table-column>
        <el-table-column label="螺杆转速1(mm/s)" min-width="150" prop="monit_barrel4"></el-table-column>
        <el-table-column label="计量切换位置1(mm)" min-width="160" prop="monitNozzle"></el-table-column>
        <el-table-column label="减压距离(mm)" min-width="130" prop="monitNozzle"></el-table-column>
        <el-table-column label="减压速度(mm/s)" min-width="130" prop="monitNozzle"></el-table-column>
        <el-table-column label="冷却时间(s)" min-width="120" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出速度1(mm/s)" min-width="140" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出速度2(mm/s)" min-width="140" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出速度3(mm/s)" min-width="140" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出速度4(mm/s)" min-width="140" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出速度5(mm/s)" min-width="140" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出切换位置1(mm)" min-width="180" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出切换位置2(mm)" min-width="180" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出切换位置3(mm)" min-width="180" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出切换位置4(mm)" min-width="180" prop="monitNozzle"></el-table-column>
        <el-table-column label="射出控制模式" min-width="120" prop="monitNozzle"></el-table-column>
        <el-table-column label="切换位置" min-width="85" prop="monitNozzle"></el-table-column>
        <el-table-column label="保压1(kgf/cm2)" min-width="130" prop="monitNozzle"></el-table-column>
        <el-table-column label="保压2(kgf/cm2)" min-width="130" prop="monitNozzle"></el-table-column>
        <el-table-column label="保压3(kgf/cm2)" min-width="130" prop="monitNozzle"></el-table-column>
        <el-table-column label="保压4(kgf/cm2)" min-width="130" prop="monitNozzle"></el-table-column>
        <el-table-column label="保压时间1(s)" min-width="120" prop="monitNozzle"></el-table-column>
        <el-table-column label="保压时间2(s)" min-width="120" prop="monitNozzle"></el-table-column>
        <el-table-column label="保压时间3(s)" min-width="120" prop="monitNozzle"></el-table-column>
        <el-table-column label="保压时间4(s)" min-width="120" prop="monitNozzle"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>

import {getAllMachineName, getAllMoldFileName, getFanucCheckDataEveryDay} from "@/api/lens/iot/fanucInspectionSz";
import FileSaver from 'file-saver'
import XLSX from 'xlsx'

export default {
  name: "fanucParamDashboard",
  computed: {

  },
  data() {
    return {
      size: 'default',
      machineNameArray: [],
      moldFileNameArray:[],
      analysisData: [],
      queryFormRules: {
        dateTimePickerValue: [
          {required: true, type: 'array',  message: "请选择时间", trigger: "change", },
        ]},
      paramNameArray: [
        {key: "monitVPPrs", value: "vp压力"},
        {key: "monitVPPos", value: "vp位置"},
        {key: "monitBackflw", value: "逆流"},
        {key: "monitRecovTime", value: "计量时间"},
        {key: "monitPeakT", value: "峰值时间"},
        {key: "monitPeakPrs", value: "峰值压力"},
        {key: "monitPeakPos", value: "最小缓冲"},
        {key: "monitMold8", value: "模温8"},
        {key: "monitMold7", value: "模温7"},
        {key: "monitMold6", value: "模温6"},
        {key: "monitMold5", value: "模温5"},
        {key: "monitMold4", value: "模温4"},
        {key: "monitMold3", value: "模温3"},
        {key: "monitMold2", value: "模温2"},
        {key: "monitMold1", value: "模温1"},
        {key: "monitInjTime", value: "射出时间"},
        {key: "monitInjStartPos", value: "射出开始位置"},
        {key: "monitCycle", value: "周期"},
        {key: "monitBarrel1", value: "料筒1温度"},
        {key: "monitBarrel2", value: "料筒2温度"},
        {key: "monitBarrel3", value: "料筒3温度"},
        {key: "monit_barrel4", value: "料筒4温度"},
        {key: "monitNozzle", value: "喷嘴温度"}
      ],
      queryLoading: false,
      formParam: {
        machineNames: null,
        moldFileNames: null,
        dateTimePickerValue: null,
        paramNames: null,
      },

      shortcuts: [{
        text: '最近一天',
        value: (() => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24);
          return [start, end]
        })()
      }],

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

    getAllMachineName() {
      getAllMachineName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameArray = responseData.data
        }
      })
    },
    getAllMoldFileName() {
      getAllMoldFileName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.moldFileNameArray = responseData.data
        }
      })
    },
    getFanucCheckDataEveryDay() {
      this.$refs.formParam.validate((valid) => {
      if (!valid) {
        return;
      }});
      const startTime = this.$moment(this.formParam.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.formParam.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');

      this.queryLoading = true;
      getFanucCheckDataEveryDay({
        machineNames: this.formParam.machineNames,
        moldFileNames: this.formParam.moldFileNames,
        startTime: startTime,
        endTime: endTime
      }).then((response) => {
        this.queryLoading = false;
        const responseData = response.data
        if (responseData.code === '000000') {
          this.analysisData = responseData.data;
        }
      })
    },


  },
  mounted() {
    this.getAllMachineName();
    this.getAllMoldFileName();
  }
}
</script>
