<template>
  <div>
    <div class="aac-container">
      <el-row>
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form ref="formParam" :size="size" label-width="100px" :model="formParam" :rules="queryFormRules">
            <el-row>
              <el-form-item label="机台号" prop="machineName">
                <el-select v-model="formParam.machineName"
                           :size="size" filterable clearable
                           placeholder="请选择机台号">
                  <el-option
                      v-for="item in machineNameArray"
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
            <el-row>
              <el-form-item label="参数名" prop="paramNames" >
                <el-select
                    v-model="formParam.paramNames"
                    :size="size"
                    collapse-tags
                    filterable
                    clearable
                    multiple
                    collapse-tags-tooltip
                    placeholder="请选择参数"
                >
                      <el-option
                          v-for="item in paramNameArray"
                          :key="item.key"
                          :label="item.value"
                          :value="item.key"
                      />
                  <!-- <el-option
                      v-for="item in paramNameArray"
                      :key="item"
                      :label="item"
                      :value="item"
                  /> -->
                </el-select>
              </el-form-item>
            </el-row>

          </el-form>

          <el-form :inline="true" :size="size">
            <el-form-item>
              <el-button :loading="queryLoading"
                         type="primary"
                         @click="drawAllChart()">查询
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
          <el-row v-for="(val, key, index) in formParam.paramNames" :key="index">
            <el-card class="box-card" style="margin-top: 10px; width:100%">
                   <div :id="val"
                        style="margin-top: 5px;height: 400px;  width: 100%"></div>

          </el-card>
        </el-row>
        <el-card class="box-card" style="margin-top: 10px; width:100%">
        <el-table
          id="paramDataList"
          v-loading="queryLoading"
          :data="analysisData"
          border
          height="600"
          stripe
          style="width: 100%">
        <el-table-column label="机台名" min-width="85" prop="monitMcName"></el-table-column>
        <el-table-column label="时间" min-width="180" prop="monitDateTime"></el-table-column>
        <el-table-column label="vp压力" min-width="85" prop="monitVPPrs"></el-table-column>
        <el-table-column label="vp位置" min-width="85" prop="monitVPPos"></el-table-column>
        <el-table-column label="逆流" min-width="85" prop="monitBackflw"></el-table-column>
        <el-table-column label="计量时间" min-width="85" prop="monitRecovTime"></el-table-column>
        <el-table-column label="峰值时间" min-width="85" prop="monitPeakT"></el-table-column>
        <el-table-column label="峰值压力" min-width="85" prop="monitPeakPrs"></el-table-column>
        <el-table-column label="最小缓冲" min-width="85" prop="monitPeakPos"></el-table-column>
        <el-table-column label="模温1" min-width="85" prop="monitMold1"></el-table-column>
        <el-table-column label="模温2" min-width="85" prop="monitMold2"></el-table-column>
        <el-table-column label="模温3" min-width="85" prop="monitMold3"></el-table-column>
        <el-table-column label="模温4" min-width="85" prop="monitMold4"></el-table-column>
        <el-table-column label="模温5" min-width="85" prop="monitMold5"></el-table-column>
        <el-table-column label="模温6" min-width="85" prop="monitMold6"></el-table-column>
        <el-table-column label="模温7" min-width="85" prop="monitMold7"></el-table-column>
        <el-table-column label="模温8" min-width="85" prop="monitMold8"></el-table-column>
        <el-table-column label="射出时间" min-width="85" prop="monitInjTime"></el-table-column>
        <el-table-column label="射出开始位置" min-width="150" prop="monitInjStartPos"></el-table-column>
        <el-table-column label="周期" min-width="85" prop="monitCycle"></el-table-column>
        <el-table-column label="料筒1温度" min-width="100" prop="monitBarrel1"></el-table-column>
        <el-table-column label="料筒2温度" min-width="100" prop="monitBarrel2"></el-table-column>
        <el-table-column label="料筒3温度" min-width="100" prop="monitBarrel3"></el-table-column>
        <el-table-column label="料筒4温度" min-width="100" prop="monit_barrel4"></el-table-column>
        <el-table-column label="喷嘴温度" min-width="85" prop="monitNozzle"></el-table-column>
      </el-table>
    </el-card>
    </div>
  </div>
</template>

<script>

import {getAnalysisData} from "@/api/lens/iot/fanucParamData";
import {selectEquips} from "@/api/lens/iot/fanucNe";
import * as echarts from 'echarts';
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


      analysisData: [],
      queryFormRules: {
        machineName: [{required: true, message: "请选择机台号", trigger: "change"}],
        dateTimePickerValue: [{required: true, type: 'datetime',  message: "请选择时间", trigger: "change"}],
        paramNames: [{required: true, message: "请选择参数", trigger: "change"}],
      },

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
      //paramValueArray:[],
      queryLoading: false,
      formParam: {
        machineName: null,
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

    selectEquips() {
      selectEquips().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameArray = responseData.data
        }
      })
    },
    drawAllChart() {
      this.getFanucAnalysisData()
    },

    getFanucAnalysisData() {
      this.$refs.formParam.validate((valid) => {
      if (!valid) {
        return;
      }});
      const startTime = this.$moment(this.formParam.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.formParam.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');

      this.queryLoading = true;
      getAnalysisData({
        machineName: this.formParam.machineName,
        paramNames: this.formParam.paramNames,
        startTime: startTime,
        endTime: endTime
      }).then((response) => {
        this.queryLoading = false;
        const responseData = response.data
        if (responseData.code === '000000') {
          this.analysisData = responseData.data;

          if(this.formParam.paramNames != null && this.formParam.paramNames.length > 0)
          {
            this.formParam.paramNames.forEach(item => {
              this.drawLineChart(item);
            })
          }
          else{
            this.paramNameArray.forEach(item => {
              this.drawLineChart(item.key);
            })
          }
        }
      })
    },

    drawLineChart(elementId) {
      const xAxisInfo = [];
      const serialData = [];

      let titleText = "注塑机参数曲线"
      this.paramNameArray.forEach(item =>{
        if(item.key == elementId)
        {
          titleText = "注塑机参数曲线[" +  item.value + "]";
          return;
        }
      })


      this.analysisData.forEach(item => {
        xAxisInfo.push(item.monitDateTime);
        serialData.push(item[elementId]);
      })

      const chartDom = document.getElementById(elementId);
      const myChart = echarts.init(chartDom);
      let option = {
          title: {
            text: titleText
          },
          legend: {
            data: ["参数"],
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
          },
          xAxis: {
            type: 'category',
            name: '时间',
            nameTextStyle: {
              fontSize: '16px',
              padding: [0, 0, 0, 15]
            },
            data: xAxisInfo
          },
          yAxis: {
            name: '值',
            type: 'value',
            scale: true
          },
          // yAxis: yAxisList,
          grid: {
            right: 140
          },
          series: {
             data: serialData,
             type: 'line',
             smooth: true
          }
        };
        myChart.setOption(option, true);
    },

  },
  mounted() {
    this.selectEquips();
  }
}
</script>
