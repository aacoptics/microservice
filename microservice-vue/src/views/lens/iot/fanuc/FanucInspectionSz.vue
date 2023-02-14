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
                           :filter-method="machineNameFilter"
                           collapse-tags
                           multiple
                           @clear="clearMachineNameFilter"
                           @blur="resetMachineNameArray"
                           placeholder="请选择机台号">
                  <el-checkbox v-model="okAllMachineNameChecked" @change='okSelectAllMachineName'>全选</el-checkbox>
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
                           :filter-method="moldFileNameFilter"
                           clearable
                           @clear="clearMoldFileNameFilter"
                           @blur="resetMoldFileNameArray"
                           multiple
                           collapse-tags
                           placeholder="请选择项目号">
                  <el-checkbox v-model="okAllMoldFileNameChecked" @change='okSelectAllMoldFileName'>全选</el-checkbox>
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
                     @click="exportExcel('#inspectionDataList', '注塑每日点检表.xlsx')">导出
            <template #icon>
              <font-awesome-icon :icon="['fas','download']"/>
            </template>
          </el-button>
            </el-form-item>
          </el-form>

        </div>
      </el-row>
        <el-table
          id="inspectionDataList"
          v-loading="queryLoading"
          :data="analysisData"
          border
          height="600"
          stripe
          style="width: 100%">
        <el-table-column label="机台名" min-width="85" prop="machineName"></el-table-column>
        <el-table-column label="班次" min-width="180" prop="shiftDate"></el-table-column>
        <el-table-column label="项目号" min-width="180" prop="moldFileName"></el-table-column>
        <el-table-column label="循环时间(s)" min-width="100" prop="monitCycle"></el-table-column>
        <el-table-column label="循环数" min-width="85" prop="monitCycleCount"></el-table-column>
        <el-table-column label="V-P压力(kgf/cm2)" min-width="150" prop="monitVPPrs"></el-table-column>
        <el-table-column label="V-P位置(mm)" min-width="110" prop="monitVPPos"></el-table-column>
        <el-table-column label="计量时间(s)	" min-width="100" prop="monitRecovTime"></el-table-column>
        <el-table-column label="射出时间(s)" min-width="100" prop="monitInjTime"></el-table-column>
        <el-table-column label="最小缓冲(mm)" min-width="120" prop="monitMCushion"></el-table-column>
        <el-table-column label="取数时间" min-width="180" prop="dbCreateTime"></el-table-column>
        <el-table-column label="喷嘴1温度(℃)" min-width="120" prop="monitNozzle"></el-table-column>
        <el-table-column label="料筒1温度(℃)" min-width="120" prop="monitBarrel1"></el-table-column>
        <el-table-column label="料筒2温度(℃)" min-width="120" prop="monitBarrel2"></el-table-column>
        <el-table-column label="料筒3温度(℃)" min-width="120" prop="monitBarrel3"></el-table-column>
        <el-table-column label="料斗下温度(℃)" min-width="120" prop="monitFeedTh"></el-table-column>
        <el-table-column label="自动模厚锁模力(TON)" min-width="180" prop="condAutoDieHForce"></el-table-column>
        <el-table-column label="喷嘴1设定温度(℃)" min-width="150" prop="condNozzle1Set"></el-table-column>
        <el-table-column label="料筒1设定温度(℃)" min-width="150" prop="condBarrel1Set"></el-table-column>
        <el-table-column label="料筒2设定温度(℃)" min-width="150" prop="condBarrel2Set"></el-table-column>
        <el-table-column label="料筒3设定温度(℃)" min-width="150" prop="condBarrel3Set"></el-table-column>
        <el-table-column label="料斗下设定温度(℃)" min-width="150" prop="condFeedThroatSet"></el-table-column>
        <el-table-column label="背压1(kgf/cm2)" min-width="130" prop="condBackPres1"></el-table-column>
        <el-table-column label="螺杆转速1(mm/s)" min-width="150" prop="condScrewRotate1"></el-table-column>
        <el-table-column label="计量切换位置1(mm)" min-width="160" prop="condExtrdSwPos1"></el-table-column>
        <el-table-column label="减压距离(mm)" min-width="130" prop="condDcmpDist"></el-table-column>
        <el-table-column label="减压速度(mm/s)" min-width="130" prop="condDcmpVel"></el-table-column>
        <el-table-column label="冷却时间(s)" min-width="120" prop="condCoolTime1"></el-table-column>
        <el-table-column label="射出速度1(mm/s)" min-width="140" prop="condInjSpeed1"></el-table-column>
        <el-table-column label="射出速度2(mm/s)" min-width="140" prop="condInjSpeed2"></el-table-column>
        <el-table-column label="射出速度3(mm/s)" min-width="140" prop="condInjSpeed3"></el-table-column>
        <el-table-column label="射出速度4(mm/s)" min-width="140" prop="condInjSpeed4"></el-table-column>
        <el-table-column label="射出速度5(mm/s)" min-width="140" prop="condInjSpeed5"></el-table-column>
        <el-table-column label="射出切换位置1(mm)" min-width="180" prop="condInjSwitchPos1"></el-table-column>
        <el-table-column label="射出切换位置2(mm)" min-width="180" prop="condInjSwitchPos2"></el-table-column>
        <el-table-column label="射出切换位置3(mm)" min-width="180" prop="condInjSwitchPos3"></el-table-column>
        <el-table-column label="射出切换位置4(mm)" min-width="180" prop="condInjSwitchPos4"></el-table-column>
        <el-table-column label="射出控制模式" min-width="120" prop="condInjectMode"></el-table-column>
        <el-table-column label="切换位置" min-width="85" prop="condTransPosition"></el-table-column>
        <el-table-column label="保压1(kgf/cm2)" min-width="130" prop="condPackPres1"></el-table-column>
        <el-table-column label="保压2(kgf/cm2)" min-width="130" prop="condPackPres2"></el-table-column>
        <el-table-column label="保压3(kgf/cm2)" min-width="130" prop="condPackPres3"></el-table-column>
        <el-table-column label="保压4(kgf/cm2)" min-width="130" prop="condPackPres4"></el-table-column>
        <el-table-column label="保压时间1(s)" min-width="120" prop="condPackTime1"></el-table-column>
        <el-table-column label="保压时间2(s)" min-width="120" prop="condPackTime2"></el-table-column>
        <el-table-column label="保压时间3(s)" min-width="120" prop="condPackTime3"></el-table-column>
        <el-table-column label="保压时间4(s)" min-width="120" prop="condPackTime4"></el-table-column>
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
      machineNameArrayCopy: [],
      moldFileNameArray:[],
      moldFileNameArrayCopy:[],
      analysisData: [],

      okAllMachineNameChecked: false,
      okAllMoldFileNameChecked: false,

      queryFormRules: {
        dateTimePickerValue: [
          {required: true, type: 'array',  message: "请选择时间", trigger: "change", },
        ]},
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
    okSelectAllMachineName() {
      this.formParam.machineNames = []
      if (this.okAllMachineNameChecked) {
        this.machineNameArray.map(item => {
          this.formParam.machineNames.push(item)
        })
      } else {
        this.formParam.machineNames = []
        this.machineNameArray = this.machineNameArrayCopy;
      }
    },
    okSelectAllMoldFileName() {
      this.formParam.moldFileNames = []
      if (this.okAllMoldFileNameChecked) {
        this.moldFileNameArray.map(item => {
          this.formParam.moldFileNames.push(item)
        })
      } else {
        this.formParam.moldFileNames = []
        this.moldFileNameArray = this.moldFileNameArrayCopy;
      }
    },
    machineNameFilter(val) {
      if (val) { //val存在
        this.machineNameArray = this.machineNameArrayCopy.filter((item) => {
          if (!!~item.indexOf(val) || !!~item.toUpperCase().indexOf(val.toUpperCase())) {
            return true
          }
        })
      } else { //val为空时，还原数组
        this.machineNameArray = this.machineNameArrayCopy;
      }
    },
    clearMachineNameFilter()
    {
      this.machineNameArray = this.machineNameArrayCopy;
      this.okAllMachineNameChecked = false;
    },
    resetMachineNameArray()
    {
      if(this.machineNameArray == null || this.machineNameArray.length == 0)
      {
        this.machineNameArray = this.machineNameArrayCopy;
      }
    },
    resetMoldFileNameArray()
    {
      if(this.moldFileNameArray == null || this.moldFileNameArray.length == 0)
      {
        this.moldFileNameArray = this.moldFileNameArrayCopy;
      }
    },
    moldFileNameFilter(val) {
      if (val) { //val存在
        this.moldFileNameArray = this.moldFileNameArrayCopy.filter((item) => {
          if (!!~item.indexOf(val) || !!~item.toUpperCase().indexOf(val.toUpperCase())) {
            return true
          }
        })
      } else { //val为空时，还原数组
        this.moldFileNameArray = this.moldFileNameArrayCopy;
      }
    },
    clearMoldFileNameFilter()
    {
      this.moldFileNameArray = this.moldFileNameArrayCopy;
      this.okAllMoldFileNameChecked = false;
    },
    
    getAllMachineName() {
      getAllMachineName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameArray = responseData.data;
          this.machineNameArrayCopy = this.machineNameArray
        }
      })
    },
    getAllMoldFileName() {
      getAllMoldFileName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.moldFileNameArray = responseData.data;
          this.moldFileNameArrayCopy = this.moldFileNameArray;
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
