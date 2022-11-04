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
                           placeholder="请选择机台号"
                           @change="getWaferIdArray">
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
                    type="datetimerange"
                    @change="getWaferIdArray">
                </el-date-picker>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="OK模次号" prop="waferId">
                <el-select
                    v-model="okWaferIds"
                    :size="size"
                    :v-loading="selectLoading"
                    collapse-tags
                    multiple
                    placeholder="请选择OK模次号"
                    @change="okChangeSelect"
                >
                  <el-checkbox v-model="okAllChecked" @change='okSelectAll'>全选</el-checkbox>
                  <el-option
                      v-for="item in waferIdArray"
                      :key="item.waferId"
                      :label="item.waferId"
                      :value="item.waferId"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="NG模次号" prop="waferId">
                <el-select
                    v-model="ngWaferIds"
                    :size="size"
                    :v-loading="selectLoading"
                    collapse-tags
                    multiple
                    placeholder="请选择NG模次号"
                    @change="ngChangeSelect"
                >
                  <el-checkbox v-model="ngAllChecked" @change='ngSelectAll'>全选</el-checkbox>
                  <el-option
                      v-for="item in waferIdArray"
                      :key="item.waferId"
                      :label="item.waferId"
                      :value="item.waferId"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="参数名" prop="paramName">
                <el-select
                    v-model="paramNames"
                    :size="size"
                    :v-loading="selectLoading"
                    collapse-tags
                    multiple
                    placeholder="请选择参数"
                >
                  <!--                <el-option-->
                  <!--                    v-for="item in paramNameArray"-->
                  <!--                    :key="item.paramName"-->
                  <!--                    :label="item.paramName"-->
                  <!--                    :value="item.paramName"-->
                  <!--                />-->
                  <el-option
                      v-for="item in paramNameArray"
                      :key="item"
                      :label="item"
                      :value="item"
                  />
                </el-select>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="对齐阶段" prop="recipePhase">
                <el-select
                    v-model="alignRecipePhase"
                    :size="size"
                    :v-loading="selectLoading"
                    placeholder="请选择需要对齐的阶段"
                    @change="okChangeSelect"
                >
                  <el-option
                      v-for="item in allRecipePhase"
                      :key="item"
                      :label="item"
                      :value="item"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="对齐模次号" prop="waferId">
                <el-select
                    v-model="alignWaferId"
                    :size="size"
                    :v-loading="selectLoading"
                    placeholder="请选择对齐的模次号"
                >
                  <el-option
                      v-for="item in allWaferIdsIncludeStatus"
                      :key="item.waferId"
                      :label="item.waferId"
                      :value="item.waferId"
                  />
                </el-select>
              </el-form-item>
            </el-row>
          </el-form>

          <el-form :inline="true" :size="size">
            <el-form-item>
              <el-button :loading="selectLoading"
                         type="primary"
                         @click="drawAllChart()">查询
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                </template>
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button :loading="selectLoading"
                         type="success"
                         @click="alainAllChart">对齐
                <template #icon>
                  <font-awesome-icon :icon="['fas','align-justify']"/>
                </template>
              </el-button>
            </el-form-item>
          </el-form>

          <!--        <el-row v-for="(val, key, index) in paramNames" :key="index">-->
          <!--          <div :id="val"-->
          <!--               style="margin-top: 10px;height: 600px; width: 1280px"></div>-->
          <!--        </el-row>-->

        </div>


      </el-row>
      <el-row>
        <el-col :span="24">
          <div id="lineChart"
               style="margin-top: 10px;height: 600px; width: 100%"></div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-table :data="analysisData" style="width: 100%">
            <el-table-column label="机台号" prop="machineName" width="180"/>
            <el-table-column label="wafer id" prop="waferId" width="180"/>
            <el-table-column label="阶段" prop="recipePhase" width="180"/>
            <el-table-column label="配方" prop="recipeName" width="180"/>
            <el-table-column label="参数" prop="paramName" width="180"/>
            <el-table-column label="平均值" prop="avgValue" width="180"/>
            <el-table-column label="标准差" prop="stdValue" width="180"/>
            <el-table-column label="创建时间" prop="createTime" width="180"/>
          </el-table>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div id="avgLineChart"
               style="margin-top: 10px;height: 600px; width: 100%"></div>
        </el-col>
        <el-col :span="12">
          <div id="stdLineChart"
               style="margin-top: 10px;height: 600px; width: 100%"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>

import {getAnalysisData, getMachineName, getMoldParamValue, getWaferIds} from "@/api/wlg/iot/moldingMachineParamData";
import * as echarts from 'echarts';

export default {
  name: "productionPlan",
  computed: {
    allWaferIdsIncludeStatus() {
      const res = []
      this.okWaferIds.forEach((item) => {
        res.push(
            {
              waferId: item,
              status: 'ok'
            }
        )
      })
      this.ngWaferIds.forEach((item) => {
        if (this.okWaferIds.indexOf(item) === -1)
          res.push(
              {
                waferId: item,
                status: 'ng'
              }
          )
      })
      return res;
    },
    allRecipePhase() {
      const res = []
      for (const val in this.paramValue) {
        this.paramValue[val].forEach((item) => {
          if (res.indexOf(item[7]) === -1 && item[7] !== 'recipePhase')
            res.push(item[7])
        })
      }
      return res
    },
    allParamValue() {
      let res = []
      let isFirst = true
      for (const val in this.paramValue) {
        if (!isFirst) {
          const tempValue = Object.assign([], this.paramValue[val])
          tempValue.shift()
          res = res.concat(tempValue)
        } else {
          res = res.concat(this.paramValue[val])
          isFirst = false
        }
      }
      return res
    }
  },
  data() {
    return {
      okAllChecked: false,
      ngAllChecked: false,
      alignRecipePhase: '',
      alignWaferId: '',
      size: 'small',
      machineNameArray: [],
      okWaferIds: [],
      ngWaferIds: [],
      waferIdArray: [],
      paramValue: {},
      analysisData: [],
      paramNameArray: [
        "Customer_Input1"
        , "customer_temperature_0"
        , "customer_temperature_1"
        , "customer_temperature_2"
        , "customer_temperature_3"
        , "customer_temperature_4"
        , "customer_temperature_5"
        , "customer_temperature_6"
        , "customer_temperature_7"
        , "exchange_heater_dutycycle_actual"
        , "exchange_temp_actual"
        , "exchange_temp_control_enabled"
        , "exchange_temp_control_setpoint_actual"
        , "intermediate_heater_dutycycle_actual"
        , "intermediate_temp_actual"
        , "intermediate_temp_control_enabled"
        , "intermediate_temp_control_setpoint_actual"
        , "lc_forming_valve_open"
        , "lc_nitrogen_valve_open"
        , "lc_pressure_actual"
        , "lc_vacuum_valve_open"
        , "lc_vent_valve_open"
        , "lower_guard_temp_actual"
        , "lower_mold_temp_actual"
        , "lower_moldcore_section_dutycycle_actual_0"
        , "lower_moldcore_section_dutycycle_actual_1"
        , "lower_moldcore_section_dutycycle_actual_2"
        , "lower_moldcore_section_dutycycle_actual_3"
        , "lower_moldcore_section_dutycycle_actual_4"
        , "lower_moldcore_section_dutycycle_actual_5"
        , "lower_moldcore_section_dutycycle_actual_6"
        , "lower_moldcore_section_dutycycle_actual_7"
        , "lower_moldcore_section_dutycycle_actual_8"
        , "lower_moldcore_section_dutycycle_actual_9"
        , "lower_moldcore_section_temp_actual_0"
        , "lower_moldcore_section_temp_actual_1"
        , "lower_moldcore_section_temp_actual_2"
        , "lower_moldcore_section_temp_actual_3"
        , "lower_moldcore_section_temp_actual_4"
        , "lower_moldcore_section_temp_actual_5"
        , "lower_moldcore_section_temp_actual_6"
        , "lower_moldcore_section_temp_actual_7"
        , "lower_moldcore_section_temp_actual_8"
        , "lower_moldcore_section_temp_actual_9"
        , "lower_moldcore_temp_control_enabled"
        , "lower_moldcore_temp_control_setpoint"
        , "mc_forming_valve_open"
        , "mc_nitrogen_valve_open"
        , "mc_pressure_actual"
        , "mc_pressure_control_enabled"
        , "mc_pressure_control_setpoint_actual"
        , "mc_pressure_control_type"
        , "mc_proportional_valve_actual"
        , "mc_vacuum_valve_open"
        , "press_force_actual"
        , "press_force_control_enabled"
        , "press_force_control_setpoint"
        , "press_force_no_deadweight"
        , "press_force_raw"
        , "press_position_actual"
        , "sideforce_counterpressure_actual"
        , "sideforce_lowerU_setpoint_actual"
        , "sideforce_lowerV_setpoint_actual"
        , "sideforce_lowerW_setpoint_actual"
        , "sideforce_upperU_setpoint_actual"
        , "sideforce_upperV_setpoint_actual"
        , "sideforce_upperW_setpoint_actual"
        , "upper_guard_temp_actual"
        , "upper_mold_temp_actual"
        , "upper_moldcore_section_dutycycle_actual_0"
        , "upper_moldcore_section_dutycycle_actual_1"
        , "upper_moldcore_section_dutycycle_actual_2"
        , "upper_moldcore_section_dutycycle_actual_3"
        , "upper_moldcore_section_dutycycle_actual_4"
        , "upper_moldcore_section_dutycycle_actual_5"
        , "upper_moldcore_section_dutycycle_actual_6"
        , "upper_moldcore_section_dutycycle_actual_7"
        , "upper_moldcore_section_dutycycle_actual_8"
        , "upper_moldcore_section_dutycycle_actual_9"
        , "upper_moldcore_section_temp_actual_0"
        , "upper_moldcore_section_temp_actual_1"
        , "upper_moldcore_section_temp_actual_2"
        , "upper_moldcore_section_temp_actual_3"
        , "upper_moldcore_section_temp_actual_4"
        , "upper_moldcore_section_temp_actual_5"
        , "upper_moldcore_section_temp_actual_6"
        , "upper_moldcore_section_temp_actual_7"
        , "upper_moldcore_section_temp_actual_8"
        , "upper_moldcore_section_temp_actual_9"
        , "upper_moldcore_temp_control_enabled"
        , "upper_moldcore_temp_control_setpoint"
        , "vacuumhead_heater_dutycycle_actual"
        , "vacuumhead_temp_actual"
        , "vacuumhead_temp_control_enabled"
        , "vacuumhead_temp_control_setpoint_actual"
      ],
      //paramValueArray:[],
      selectLoading: false,
      formParam: {
        waferIds: []
      },
      dateTimePickerValue: [],
      paramNames: [],
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
    okSelectAll() {
      this.okWaferIds = []
      if (this.okAllChecked) {
        this.waferIdArray.map(item => {
          this.okWaferIds.push(item.waferId)
        })
      } else {
        this.okWaferIds = []
      }
    },
    okChangeSelect(val) {
      if (val.length === this.waferIdArray.length) {
        this.okAllChecked = true
      } else {
        this.okAllChecked = false
      }
    },

    ngSelectAll() {
      this.ngWaferIds = []
      if (this.ngAllChecked) {
        this.waferIdArray.map(item => {
          this.ngWaferIds.push(item.waferId)
        })
      } else {
        this.ngWaferIds = []
      }
    },
    ngChangeSelect(val) {
      if (val.length === this.waferIdArray.length) {
        this.ngAllChecked = true
      } else {
        this.ngAllChecked = false
      }
    },
    getMachineName() {
      getMachineName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameArray = responseData.data
        }
      })
    },

    getMoldingAnalysisData() {
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      getAnalysisData({
        machineName: this.formParam.machineName,
        paramNames: this.paramNames,
        startTime: startTime,
        endTime: endTime
      }).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.analysisData = responseData.data
          const legendInfo = []
          const serialAvgDataArray = []
          const serialStdDataArray = []
          const xAxisInfo = []
          let tempParam = ''
          let serialAvgData = {data: []}
          let serialStdData = {data: []}
          this.analysisData.forEach(item => {
            if(item.paramName !== tempParam){
              legendInfo.push(item.paramName)
              if(serialAvgData.data.length > 0)
                serialAvgDataArray.push(serialAvgData)
              if(serialStdData.data.length > 0)
                serialStdDataArray.push(serialStdData)
              serialAvgData = {
                name: item.paramName,
                data: [],
                type: 'line',
                smooth: true
              }

              serialStdData = {
                name: item.paramName,
                data: [],
                type: 'line',
                smooth: true
              }
              tempParam = item.paramName;
            }
            if(xAxisInfo.indexOf(item.waferId) === -1)
              xAxisInfo.push(item.waferId)
            serialAvgData.data.push(item.avgValue)
            serialStdData.data.push(item.stdValue)
          })
          this.drawAvgLineChart(xAxisInfo, legendInfo, serialAvgDataArray)
          this.drawStdLineChart(xAxisInfo, legendInfo, serialStdDataArray)
        }
      })
    },

    getWaferIdArray() {
      this.selectLoading = true
      if (!this.formParam.machineName || this.formParam.machineName === "") {
        this.$message.error('请先选择机台号！')
        this.selectLoading = false
      }
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      getWaferIds(this.formParam.machineName, startTime, endTime).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.waferIdArray = responseData.data
          this.selectLoading = false
        }
      }).catch((err) => {
        this.$message.error(err.message)
        this.selectLoading = false
      })
    },

    // getParamNames(val) {
    //   // this.selectLoading = true
    //   // getMoldParamName(this.formParam).then((response) => {
    //   //   const responseData = response.data
    //   //   if (responseData.code === '000000') {
    //   //     this.paramNameArray = responseData.data
    //   //     this.selectLoading = false
    //   //   }
    //   // }).catch((err) => {
    //   //   this.$message.error(err.message)
    //   //   this.selectLoading = false
    //   // })
    //   //this.changeSelect(val)
    // },

    getParamValue(paramName) {
      this.selectLoading = true
      this.formParam.paramName = paramName
      const waferIdRes = []
      this.allWaferIdsIncludeStatus.forEach((item) => {
        waferIdRes.push(item.waferId)
      })
      this.formParam.waferIds = waferIdRes
      getMoldParamValue(this.formParam).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.paramValue[paramName] = responseData.data
          //this.paramValueArray = responseData.data
          this.drawLineChart(0)
          console.log(this.allParamValue)
          this.selectLoading = false
        }
      }).catch((err) => {
        this.$message.error(err.message)
        this.selectLoading = false
      })
    },

    drawAllChart() {
      this.paramValue = {}
      this.paramNames.forEach(item => {
        this.getParamValue(item)
      })
      this.getMoldingAnalysisData()
    },

    alainAllChart() {
      if (!this.alignRecipePhase || this.alignRecipePhase === '' ||
          !this.alignWaferId || this.alignWaferId === '') {
        this.$message.error('请选择对齐的模次号和阶段！')
        return
      }
      const minStampArray = this.alignParamValue()
      let isFirst = true
      this.paramNames.forEach(item => {
        if (isFirst) {
          this.drawLineChart(minStampArray[item])
          isFirst = false
        }
      })
    },

    alignParamValue() {
      const alignShiftStamp = {}
      const waferMinStamp = {}
      const waferMinStampArray = {}
      for (const val in this.paramValue) {
        this.paramValue[val].forEach((item) => {
          if (item[7] !== 'recipePhase') {
            if (!alignShiftStamp[val])
              alignShiftStamp[val] = {}
            if (!alignShiftStamp[val][item[7]])
              alignShiftStamp[val][item[7]] = {}
            if (!alignShiftStamp[val][item[7]][item[1]] || alignShiftStamp[val][item[7]][item[1]] > item[5])
              alignShiftStamp[val][item[7]][item[1]] = item[5]
          }
        })
      }

      for (const val in this.paramValue) {
        this.paramValue[val].forEach((item) => {
          if (item[7] !== 'recipePhase') {
            if (item[1] !== this.alignWaferId) {
              item[5] = alignShiftStamp[val][this.alignRecipePhase][this.alignWaferId] - alignShiftStamp[val][this.alignRecipePhase][item[1]] + item[5]
            }
            if (!waferMinStamp[val]) {
              waferMinStamp[val] = {}
            }
            if (!waferMinStamp[val][item[1]] || waferMinStamp[val][item[1]] > item[5]) {
              waferMinStamp[val][item[1]] = item[5]
            }
          }
        })
      }
      for (const val in waferMinStamp) {
        for (const waferVal in waferMinStamp[val]) {
          if (!waferMinStampArray[val] || waferMinStampArray[val] < waferMinStamp[val][waferVal])
            waferMinStampArray[val] = waferMinStamp[val][waferVal]
        }
      }
      return waferMinStampArray
    },

    // drawLineChart(paramName){
    //   const chartDom = document.getElementById(paramName);
    //   const myChart = echarts.init(chartDom);
    //   let option;
    //
    //   run(this.paramValueArray)
    //
    //
    //   function run(_rawData) {
    //     option = {
    //       dataset: [
    //         {
    //           id: 'dataset_raw',
    //           source: _rawData
    //         },
    //         {
    //           id: 'dataset_since_1950_of_germany',
    //           fromDatasetId: 'dataset_raw',
    //           transform: {
    //             type: 'filter',
    //             config: {
    //               and: [
    //                 { dimension: 'plcTimeStamp', gte: 0 },
    //                 { dimension: 'waferId', '=': '3816' }
    //               ]
    //             }
    //           }
    //         },
    //         {
    //           id: 'dataset_since_1950_of_france',
    //           fromDatasetId: 'dataset_raw',
    //           transform: {
    //             type: 'filter',
    //             config: {
    //               and: [
    //                 { dimension: 'plcTimeStamp', gte: 0 },
    //                 { dimension: 'waferId', '=': '3817' }
    //               ]
    //             }
    //           }
    //         }
    //       ],
    //       title: {
    //         text: paramName
    //       },
    //       tooltip: {
    //         trigger: 'axis'
    //       },
    //       xAxis: {
    //         type: 'category',
    //         nameLocation: 'middle'
    //       },
    //       yAxis: {
    //         name: '值'
    //       },
    //       series: [
    //         {
    //           type: 'line',
    //           datasetId: 'dataset_since_1950_of_germany',
    //           showSymbol: false,
    //           encode: {
    //             x: 'plcTimeStamp',
    //             y: 'paramValue',
    //             itemName: 'plcTimeStamp',
    //             tooltip: ['paramValue']
    //           }
    //         },
    //         {
    //           type: 'line',
    //           datasetId: 'dataset_since_1950_of_france',
    //           showSymbol: false,
    //           encode: {
    //             x: 'plcTimeStamp',
    //             y: 'paramValue',
    //             itemName: 'plcTimeStamp',
    //             tooltip: ['paramValue']
    //           }
    //         }
    //       ]
    //     };
    //     myChart.setOption(option);
    //   }
    //
    //   option && myChart.setOption(option);
    // }

    drawLineChart(startStamp) {
      const chartDom = document.getElementById('lineChart');
      const myChart = echarts.init(chartDom);
      let option;

      run(this.allParamValue, this.allWaferIdsIncludeStatus, this.paramNames)


      function run(_rawData, waferIds, paramsInfo) {

        const waferIdRes = [];
        const datasetWithFilters = [];
        const seriesList = [];
        const yAxisList = [];
        const yAxisIndexList = [];
        echarts.util.each(paramsInfo, function (item) {
          yAxisIndexList.push(item + '_value')
          const i = yAxisIndexList.indexOf(item + '_value')
          if (i === 0)
            yAxisList.push({
              type: 'value',
              name: '值',
              yAxisIndex: i,
              alignTicks: true,
              scale: true,
              axisLine: {
                show: true,
                lineStyle: {
                  color: '#5470C6'
                }
              },
              nameTextStyle: {
                fontSize: '16px'
              }
            })
          else {
            yAxisList.push({
              name: '值',
              yAxisIndex: i,
              alignTicks: true,
              scale: true,
              axisLine: {
                show: true,
                lineStyle: {
                  color: '#5470C6'
                }
              },
              position: 'right',
              offset: (i - 1) * 80,
              nameTextStyle: {
                fontSize: '16px'
              }
            })
          }
        });
        echarts.util.each(waferIds, function (waferIdInfo) {
          echarts.util.each(paramsInfo, function (item) {
            const paramWaferIdInfo = waferIdInfo.waferId + '-' + item
            waferIdRes.push(paramWaferIdInfo);
            const datasetId = 'dataset_' + paramWaferIdInfo;
            datasetWithFilters.push({
              id: datasetId,
              fromDatasetId: 'dataset_raw',
              transform: {
                type: 'filter',
                config: {
                  and: [
                    {dimension: 'plcTimeStamp', gte: startStamp},
                    {dimension: 'paramWaferId', '=': paramWaferIdInfo}
                  ]
                }
              }
            });
            seriesList.push({
              type: 'line',
              datasetId: datasetId,
              showSymbol: false,
              yAxisIndex: yAxisIndexList.indexOf(item + '_value'),
              yAxisName: item + '_value',
              itemStyle: {
                normal: {
                  lineStyle: {
                    width: 2,
                    type: waferIdInfo.status === 'ok' ? 'solid' : 'dotted' //'dotted'点型虚线 'solid'实线 'dashed'线性虚线
                  }
                }
              },
              name: paramWaferIdInfo,
              encode: {
                x: 'plcTimeStamp',
                y: 'paramValue',
                label: ['paramWaferId', 'paramValue'],
                itemName: 'plcTimeStamp',
                tooltip: ['paramValue', 'recipePhase']
              }
            });
          })

        });
        option = {
          dataset: [
            {
              id: 'dataset_raw',
              source: _rawData
            },
            ...datasetWithFilters
          ],
          title: {
            text: '模造机参数曲线'
          },
          legend: {
            data: waferIdRes,
            bottom: 0,
            type: 'scroll',
            orient: 'horizontal'
          },
          toolbox: {
            feature: {
              dataZoom: {
                yAxisIndex: 'none'
              },
              restore: {},
              saveAsImage: {}
            }
          },
          tooltip: {
            order: 'valueDesc',
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            },
            confine: true,
            // formatter: function (params) {
            //   if (params instanceof Array) {
            //     let str = '';
            //     str += `${params[0].axisValue}<br/>`;
            //     params.forEach((m, index) => {
            //       str +=  `<span class="chart-tooltip-color" style="display: inline-block; margin-right: 10px; background-color: ${m.color}; width: 10px; height: 10px; border-radius:100%; margin-right: 5px"></span>`;
            //       str += `${m.seriesName}: 参数值：${m.data[3]} 步骤：${m.data[7]}`;
            //       str += `${index % 3 === 0 ? '<br/>' : ''}`; //一排放几个可根据实际情况改变
            //     });
            //     return str;
            //   }
            // }
          },
          xAxis: {
            type: 'category',
            name: '秒',
            nameTextStyle: {
              fontSize: '16px',
              padding: [0, 0, 0, 15]
            }
          },
          // yAxis: {
          //   name: '参数值',
          //   scale: true,
          //   nameTextStyle: {
          //     fontSize: '16px'
          //   }
          // },
          yAxis: yAxisList,
          grid: {
            right: 140
          },
          series: seriesList
        };
        myChart.setOption(option, true);
      }

      option && myChart.setOption(option, true);
    },

    drawAvgLineChart(xAxisInfo, legendInfo, serialAvgDataArray) {
      const chartDom = document.getElementById('avgLineChart');
      const myChart = echarts.init(chartDom);
      let option;

      option = {
        xAxis: {
          type: 'category',
          data: xAxisInfo
        },
        yAxis: {
          type: 'value',
          scale: true
        },
        legend: {
          data: legendInfo
        },

        tooltip: {
          order: 'valueDesc',
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          confine: true,
        },
        series: serialAvgDataArray
      };

      option && myChart.setOption(option);
    },

    drawStdLineChart(xAxisInfo, legendInfo, serialStdDataArray) {
      const chartDom = document.getElementById('stdLineChart');
      const myChart = echarts.init(chartDom);
      let option;

      option = {
        xAxis: {
          type: 'category',
          data: xAxisInfo
        },
        yAxis: {
          type: 'value',
          scale: true
        },
        legend: {
          data: legendInfo
        },

        tooltip: {
          order: 'valueDesc',
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          confine: true,
        },
        series: serialStdDataArray
      };

      option && myChart.setOption(option, true);
    }
  },
  mounted() {
    this.getMachineName();
  }
}
</script>
