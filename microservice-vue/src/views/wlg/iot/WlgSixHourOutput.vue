<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
            <el-form-item label="机台号" prop="machineName">
              <el-select v-model="machineNames"
                         placeholder="请选择机台号"
                         multiple
                         collapse-tags
                         :size="size">
                <el-checkbox v-model="okAllChecked" @change='okSelectAll'>全选</el-checkbox>
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
                  type="daterange"
                  :shortcuts="shortcuts"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  :size="size">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary"
                         :loading="selectLoading"
                         @click="getByDateAndMachineNames">查询
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                </template>
              </el-button>
            </el-form-item>
        </el-form>

        <el-form  :size="size">

        </el-form>

        <!--        <el-row v-for="(val, key, index) in paramNames" :key="index">-->
        <!--          <div :id="val"-->
        <!--               style="margin-top: 10px;height: 600px; width: 1280px"></div>-->
        <!--        </el-row>-->

      </div>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="machineName" label="机台号" width="180" />
        <el-table-column prop="materialName" label="材料号" width="180" />
        <el-table-column prop="projectName" label="项目" width="180" />
        <el-table-column prop="modelName" label="模具" width="180" />
        <el-table-column prop="cycleName" label="周期" width="180" />
        <el-table-column prop="periodName" label="阶段" width="180" />
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="avgCycle" label="平均周期" width="180" />
        <el-table-column prop="inputQty" label="投入数" width="180" />
        <el-table-column prop="startWaferId" label="起始模次号" width="180" />
        <el-table-column prop="endWaferId" label="截止模次号" width="180" />
        <el-table-column prop="avgCycle" label="平均周期" width="180" />

        <el-table-column prop="brokenOk" label="碎裂可流转" width="180">
          <template v-slot="scope">
            <el-input-number v-model="scope.row.brokenOk" v-show="scope.row.iseditor" />
            <span v-show="!scope.row.iseditor">{{scope.row.brokenOk}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="brokenNg" label="碎裂不可流转" width="180">
          <template v-slot="scope">
            <el-input-number v-model="scope.row.brokenNg" v-show="scope.row.iseditor" />
            <span v-show="!scope.row.iseditor">{{scope.row.brokenNg}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="outputQty" label="产出" width="180" />
        <el-table-column label="操作" width="180">
          <template v-slot="scope">
            <el-button type="warning" @click="edit(scope.row, scope)">编辑</el-button>
            <el-button type="danger" @click="save(scope.row)">确认</el-button>
          </template>
        </el-table-column>
      </el-table>


    </div>
  </div>
</template>

<script>

import {
  getByDateAndMachineName,
  getMachineName,
  getMoldParamValue,
  getWaferIds
} from "@/api/wlg/iot/moldingMachineParamData";
import * as echarts from 'echarts';

export default {
  name: "WlgSixHourOutput",
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
      tableData: [],
      machineNames:[],
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
      paramNameArray: [
        "Customer_Input1_0",
        "customer_temperature_1",
        "customer_temperature_2",
        "customer_temperature_3",
        "customer_temperature_4",
        "customer_temperature_5",
        "customer_temperature_6",
        "customer_temperature_7",
        "customer_temperature_8",
        "exchange_heater_dutycycle_actual_0",
        "exchange_temp_actual_0",
        "exchange_temp_control_enabled_0",
        "exchange_temp_control_setpoint_actual_0",
        "intermediate_heater_dutycycle_actual_0",
        "intermediate_temp_actual_0",
        "intermediate_temp_control_enabled_0",
        "intermediate_temp_control_setpoint_actual_0",
        "lc_forming_valve_open_0",
        "lc_nitrogen_valve_open_0",
        "lc_pressure_actual_0",
        "lc_vacuum_valve_open_0",
        "lc_vent_valve_open_0",
        "lower_guard_temp_actual_0",
        "lower_mold_temp_actual_0",
        "lower_moldcore_section_dutycycle_actual_1",
        "lower_moldcore_section_dutycycle_actual_10",
        "lower_moldcore_section_dutycycle_actual_2",
        "lower_moldcore_section_dutycycle_actual_3",
        "lower_moldcore_section_dutycycle_actual_4",
        "lower_moldcore_section_dutycycle_actual_5",
        "lower_moldcore_section_dutycycle_actual_6",
        "lower_moldcore_section_dutycycle_actual_7",
        "lower_moldcore_section_dutycycle_actual_8",
        "lower_moldcore_section_dutycycle_actual_9",
        "lower_moldcore_section_temp_actual_1",
        "lower_moldcore_section_temp_actual_10",
        "lower_moldcore_section_temp_actual_2",
        "lower_moldcore_section_temp_actual_3",
        "lower_moldcore_section_temp_actual_4",
        "lower_moldcore_section_temp_actual_5",
        "lower_moldcore_section_temp_actual_6",
        "lower_moldcore_section_temp_actual_7",
        "lower_moldcore_section_temp_actual_8",
        "lower_moldcore_section_temp_actual_9",
        "lower_moldcore_temp_control_enabled_0",
        "lower_moldcore_temp_control_setpoint_0",
        "mc_forming_valve_open_0",
        "mc_nitrogen_valve_open_0",
        "mc_pressure_actual_0",
        "mc_pressure_control_enabled_0",
        "mc_pressure_control_setpoint_actual_0",
        "mc_pressure_control_type_0",
        "mc_proportional_valve_actual_0",
        "mc_vacuum_valve_open_0",
        "press_force_actual_0",
        "press_force_control_enabled_0",
        "press_force_control_setpoint_0",
        "press_force_no_deadweight_0",
        "press_force_raw_0",
        "press_position_actual_0",
        "sideforce_counterpressure_actual_0",
        "sideforce_lowerU_setpoint_actual_0",
        "sideforce_lowerV_setpoint_actual_0",
        "sideforce_lowerW_setpoint_actual_0",
        "sideforce_upperU_setpoint_actual_0",
        "sideforce_upperV_setpoint_actual_0",
        "sideforce_upperW_setpoint_actual_0",
        "upper_guard_temp_actual_0",
        "upper_mold_temp_actual_0",
        "upper_moldcore_section_dutycycle_actual_1",
        "upper_moldcore_section_dutycycle_actual_10",
        "upper_moldcore_section_dutycycle_actual_2",
        "upper_moldcore_section_dutycycle_actual_3",
        "upper_moldcore_section_dutycycle_actual_4",
        "upper_moldcore_section_dutycycle_actual_5",
        "upper_moldcore_section_dutycycle_actual_6",
        "upper_moldcore_section_dutycycle_actual_7",
        "upper_moldcore_section_dutycycle_actual_8",
        "upper_moldcore_section_dutycycle_actual_9",
        "upper_moldcore_section_temp_actual_1",
        "upper_moldcore_section_temp_actual_10",
        "upper_moldcore_section_temp_actual_2",
        "upper_moldcore_section_temp_actual_3",
        "upper_moldcore_section_temp_actual_4",
        "upper_moldcore_section_temp_actual_5",
        "upper_moldcore_section_temp_actual_6",
        "upper_moldcore_section_temp_actual_7",
        "upper_moldcore_section_temp_actual_8",
        "upper_moldcore_section_temp_actual_9",
        "upper_moldcore_temp_control_enabled_0",
        "upper_moldcore_temp_control_setpoint_0",
        "vacuumhead_heater_dutycycle_actual_0",
        "vacuumhead_temp_actual_0",
        "vacuumhead_temp_control_enabled_0",
        "vacuumhead_temp_control_setpoint_actual_0"
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
      },
        {
        text: '最近一周',
        value: () => {
          const end = new Date()
          const start = new Date()
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
          return [start, end]
        },
      },
        {
          text: '最近一个月',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            return [start, end]
          },
        }],
    }
  },
  methods: {

    edit(row, index) {
      row.iseditor = true;
    },
    save(row, index) {
      row.iseditor = false;
    },
    okSelectAll() {
      this.machineNames = []
      if (this.okAllChecked) {
        this.machineNameArray.map(item => {
          this.machineNames.push(item.machineName)
        })
      } else {
        this.machineNames = []
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
    getByDateAndMachineNames(){
      this.selectLoading = true
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      getByDateAndMachineName({startTime: startTime, endTime: endTime, machineNames: this.machineNames}).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.tableData = responseData.data
          this.selectLoading = false
        }
      }).catch((err) => {
        this.$message.error(err.message)
        this.selectLoading = false
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
          this.tableData = responseData.data
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
        if(isFirst){
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
    }
  },
  mounted() {
    this.getMachineName();
  }
}
</script>
