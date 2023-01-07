<template>
  <div>
    <div class="aac-container">
      <el-row>
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form :size="size" label-width="100px">
            <el-row>
              <el-form-item label="机台号" prop="machineName">

                <el-select v-model="formParam.machineName"
                           :size="size"
                           placeholder="请选择机台号" clearable filterable multiple
                           style="width:300px">
                  <el-option
                      v-for="item in machineNameArray"
                      :key="item.machineName"
                      :label="item.machineName"
                      :value="item.machineName"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="时间" prop="dateTimePicker">
                <el-date-picker
                    v-model="dateTimePickerValue"
                    :shortcuts="shortcuts"
                    :size="size"
                    end-placeholder="结束日期"
                    range-separator="至"
                    start-placeholder="开始日期"
                    type="datetimerange">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="findPage(null)">查询
                  <template #icon>
                    <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                  </template>
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button type="success"
                           @click="exportExcel()">导出
                  <template #icon>
                    <font-awesome-icon :icon="['fas','download']"/>
                  </template>
                </el-button>
              </el-form-item>
            </el-row>
          </el-form>
        </div>
      </el-row>
      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :show-operation="false"
                :showBatchDelete="false" :stripe="false"
                @findPage="findPage">
      </SysTable>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {
  getMachineName,
  getMoldingMK4Data,
  exportMoldingMK4DataExcel
} from "@/api/wlg/iot/moldingMachineParamData";
export default {
  name: "WlgMoldingMK4DetailData",
  components: {SysTable},
  data() {
    return {
      columns: [
        {prop: "equipmentName", label: "机台号", minWidth: 110, fixed: "left"},
        {prop: "batchId", label: "批次号", minWidth: 110, fixed: "left"},
        {prop: "recipeName", label: "配方", minWidth: 200},
        {prop: "waferType", label: "wafer类型", minWidth: 110},
        {prop: "logFileStartTime", label: "开始时间", minWidth: 110, formatter: this.dateTimeFormat},
        {prop: "ph1UpHeatingRateSetPoint", label: "Ph1_UpHeatingRateSetpoint", minWidth: 110},
        {prop: "ph1LowHeatingRateSetPoint", label: "Ph1_LowHeatingRateSetpoint", minWidth: 110},
        {prop: "ph1Position", label: "Ph1_Position", minWidth: 110},
        {prop: "ph1Pressure", label: "Ph1_Pressure", minWidth: 110},
        {prop: "ph2TempUpSetPoint", label: "Ph2_TempUpSetpoint", minWidth: 110},
        {prop: "ph2TempLowSetPoint", label: "Ph2_TempLowSetpoint", minWidth: 110},
        {prop: "ph2Position", label: "Ph2_Position", minWidth: 110},
        {prop: "ph2ForceMax", label: "Ph2_ForceMax", minWidth: 110},
        {prop: "ph2ForceMin", label: "Ph2_ForceMin", minWidth: 110},
        {prop: "ph3TempUpSetPoint", label: "Ph3_TempUpSetpoint", minWidth: 110},
        {prop: "ph3TempLowSetPoint", label: "Ph3_TempLowSetpoint", minWidth: 110},
        {prop: "ph3Pressure", label: "Ph3_Pressure", minWidth: 110},
        {prop: "ph3PumpTime", label: "Ph3_PumpTime", minWidth: 110},
        {prop: "ph3TempUpActual", label: "Ph3_TempUpActual", minWidth: 110},
        {prop: "ph3TempLowActual", label: "Ph3_TempLowActual", minWidth: 110},
        {prop: "ph4Pressure", label: "Ph4_Pressure", minWidth: 110},
        {prop: "ph4TempUpSetPoint", label: "Ph4_TempUpSetpoint", minWidth: 110},
        {prop: "ph4TempLowSetPoint", label: "Ph4_TempLowSetpoint", minWidth: 110},
        {prop: "ph4TempUpActualMax", label: "Ph4_TempUpActualMax", minWidth: 110},
        {prop: "ph4TempUpActualMin", label: "Ph4_TempUpActualMin", minWidth: 110},
        {prop: "ph4TempLowActualMax", label: "Ph4_TempLowActualMax", minWidth: 110},
        {prop: "ph4TempLowActualMin", label: "Ph4_TempLowActualMin", minWidth: 110},
        {prop: "ph4SoakingTime", label: "Ph4_SoakingTime", minWidth: 110},
        {prop: "ph5Position", label: "Ph5_Position", minWidth: 110},
        {prop: "ph5ForceMax", label: "Ph5_ForceMax", minWidth: 110},
        {prop: "ph5ForceMin", label: "Ph5_ForceMin", minWidth: 110},
        {prop: "ph6TempUpSetPoint", label: "Ph6_TempUpSetpoint", minWidth: 110},
        {prop: "ph6TempLowSetPoint", label: "Ph6_TempLowSetpoint", minWidth: 110},
        {prop: "ph6FRaisingRate", label: "Ph6_FRaisingRate", minWidth: 110},
        {prop: "ph7TempUpSetPoint", label: "Ph7_TempUpSetpoint", minWidth: 110},
        {prop: "ph7TempLowSetPoint", label: "Ph7_TempLowSetpoint", minWidth: 110},
        {prop: "ph7TempUpActualMax", label: "Ph7_TempUpActualMax", minWidth: 110},
        {prop: "ph7TempUpActualMin", label: "Ph7_TempUpActualMin", minWidth: 110},
        {prop: "ph7TempLowActualMax", label: "Ph7_TempLowActualMax", minWidth: 110},
        {prop: "ph7TempLowActualMin", label: "Ph7_TempLowActualMin", minWidth: 110},
        {prop: "ph7Force", label: "Ph7_Force", minWidth: 110},
        {prop: "ph7Position", label: "Ph7_Position", minWidth: 110},
        {prop: "ph7Pressure", label: "Ph7_Pressure", minWidth: 110},
        {prop: "ph7MoldingTime", label: "Ph7_MoldingTime", minWidth: 110},
        {prop: "ph8UpCoolingRateSetPoint", label: "Ph8_UpCoolingRateSetpoint", minWidth: 110},
        {prop: "ph8LowCoolingRateSetPoint", label: "Ph8_LowCoolingRateSetpoint", minWidth: 110},
        {prop: "ph8Force", label: "Ph8_Force", minWidth: 110},
        {prop: "ph8Pressure", label: "Ph8_Pressure", minWidth: 110},
        {prop: "ph8TempUpActual", label: "Ph8_TempUpActual", minWidth: 110},
        {prop: "ph8TempLowActual", label: "Ph8_TempLowActual", minWidth: 110},
        {prop: "ph8TempUpLow", label: "Ph8_TempUpLow", minWidth: 110},
        {prop: "ph9UpCoolingRateSetPoint", label: "Ph9_UpCoolingRateSetpoint", minWidth: 110},
        {prop: "ph9LowCoolingRateSetPoint", label: "Ph9_LowCoolingRateSetpoint", minWidth: 110},
        {prop: "ph9TempUpActual", label: "Ph9_TempUpActual", minWidth: 110},
        {prop: "ph9TempLowActual", label: "Ph9_TempLowActual", minWidth: 110},
        {prop: "ph9TempUpLow", label: "Ph9_TempUpLow", minWidth: 110},
        {prop: "ph10TempUpActual30N", label: "Ph10_TempUpActual_30N", minWidth: 110},
        {prop: "ph10TempLowActual30N", label: "Ph10_TempLowActual_30N", minWidth: 110},
        {prop: "ph10TempUpLow30N", label: "Ph10_TempUpLow_30N", minWidth: 110},
        {prop: "ph10ForceNegative", label: "Ph10_ForceNegative", minWidth: 110},
        {prop: "ph10TimeNegative", label: "Ph10_TimeNegative", minWidth: 110},
        {prop: "ph10TempUpActualNegative", label: "Ph10_TempUpActual_Negative", minWidth: 110},
        {prop: "ph10TempLowActualNegative", label: "Ph10_TempLowActual_Negative", minWidth: 110},
        {prop: "ph10TempUpLowNegative", label: "Ph10_TempUpLow_Negative", minWidth: 110},
        {prop: "ph11Position", label: "Ph11_Position", minWidth: 110},
        {prop: "p12UpCoolingRateSetPoint", label: "Ph12_UpCoolingRateSetpoint", minWidth: 110},
        {prop: "p12LowCoolingRateSetPoint", label: "Ph12_LowCoolingRateSetpoint", minWidth: 110},
        {prop: "p12TempLowActual", label: "Ph12_TempLowActual", minWidth: 110},
        {prop: "ph12PickPlaceTempActual", label: "Ph12_PickPlaceTempActual", minWidth: 110},
        {prop: "ph12ExchangeTempActual", label: "Ph12_ExchangeTempActual", minWidth: 110},
        {prop: "tempUpActualFp1", label: "TempUpActual_Fp1", minWidth: 110},
        {prop: "tempLowActualFp1", label: "TempLowActual_Fp1", minWidth: 110},
        {prop: "tempUpLowFp1", label: "TempUpLow_Fp1", minWidth: 110},
        {prop: "tempUpHeatBedAverageFp1", label: "TempUpheatbedAverage_Fp1", minWidth: 110},
        {prop: "tempLowHeatBedAverageFp1", label: "TempLowheatbedAverage_Fp1", minWidth: 110},
        {prop: "tempUpActualFp2", label: "TempUpActual_Fp2", minWidth: 110},
        {prop: "tempLowActualFp2", label: "TempLowActual_Fp2", minWidth: 110},
        {prop: "tempUpLowFp2", label: "TempUpLow_Fp2", minWidth: 110},
        {prop: "tempUpHeatBedAverageFp2", label: "TempUpheatbedAverage_Fp2", minWidth: 110},
        {prop: "tempLowHeatBedAverageFp2", label: "TempLowheatbedAverage_Fp2", minWidth: 110},
        {prop: "tempUpActualFp3", label: "TempUpActual_Fp3", minWidth: 110},
        {prop: "tempLowActualFp3", label: "TempLowActual_Fp3", minWidth: 110},
        {prop: "tempUpLowFp3", label: "TempUpLow_Fp3", minWidth: 110},
        {prop: "tempUpHeatBedAverageFp3", label: "TempUpheatbedAverage_Fp3", minWidth: 110},
        {prop: "tempLowHeatBedAverageFp3", label: "TempLowheatbedAverage_Fp3", minWidth: 110},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      size: 'small',
      machineNameArray: [],
      formParam: {
        machineName: []
      },
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
    }
  },
  methods: {
    getMachineName() {
      getMachineName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameArray = responseData.data
        }
      })
    },

    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm:ss')
    },

    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      let params = {};
      params.machineName = this.formParam.machineName;
      params.startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      params.endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      params.current = this.pageRequest.current;
      params.size = this.pageRequest.size;
      getMoldingMK4Data(params).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },

    exportExcel() {
      let params = {};
      params.machineName = this.formParam.machineName;
      params.startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      params.endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      exportMoldingMK4DataExcel(params).then(res => {
        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', "模造机MK4数据报表-" + new Date().getTime() + ".xlsx");
        document.body.appendChild(link);
        link.click();
      });
    }
  },

  mounted() {
    this.getMachineName();
  }

}
</script>

<style scoped>

</style>