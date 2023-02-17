<template>
  <div>
    <div class="aac-container">
      <el-row>
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form ref="formParam" :size="size" label-width="100px" :model="formParam" :rules="queryFormRules">
            <el-row>
              <el-form-item label="机台号" prop="machineNo">
                <el-select v-model="formParam.machineNo"
                           :size="size"
                           placeholder="请选择机台号"
                           @change="getCycleNos">
                  <el-option
                      v-for="item in machineNoArray"
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
                    type="datetimerange"
                    @change="getCycleNos">
                </el-date-picker>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="模次" prop="cycleNos" >
                <el-select
                    v-model="formParam.cycleNos"
                    :size="size"
                    collapse-tags
                    filterable
                    clearable
                    multiple
                    collapse-tags-tooltip
                    placeholder="请选择模次"
                >
                  <el-option
                      v-for="item in cycleNoArray"
                      :key="item"
                      :label="item"
                      :value="item"
                  />
                </el-select>
              </el-form-item>
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
              <el-button :loading="exportLoading"
                         type="success"
                     @click="handleDownload">导出
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
                        style="margin-top: 5px;height: 600px;  width: 100%"></div>

          </el-card>
        </el-row>
    </div>
  </div>
</template>

<script>

import {getCycleNosByTime, getWaveDataByCycleNo, downloadExcel} from "@/api/lens/iot/fanucParamData";
import * as echarts from 'echarts';

export default {
  name: "FanucWavePageNe",
  computed: {

  },
  data() {
    return {
      size: 'default',
      machineNoArray: ["4FM01"],
      waveData: [],
      queryFormRules: {
        machineName: [{required: true, message: "请选择机台号", trigger: "change"}],
        dateTimePickerValue: [{required: true, type: 'datetime',  message: "请选择时间", trigger: "change"}],
        cycleNos: [{required: true, message: "请选择模次", trigger: "change"}],
        paramNames: [{required: true, message: "请选择参数", trigger: "change"}],
      },

      paramNameArray: [
        {key: "injectPressure", value: "射出压"},
        {key: "analogInput1", value: "喷嘴压"}
      ],

      cycleNoArray:[],
      queryLoading: false,
      exportLoading: false,
      formParam: {
        machineNo: "4FM01",
        dateTimePickerValue: null,
        paramNames: null,
        cycleNos: null,
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
    getCycleNos() {
      const startTime = this.$moment(this.formParam.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.formParam.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      getCycleNosByTime({
        machineNo: this.formParam.machineNo,
        startTime: startTime,
        endTime: endTime}).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.cycleNoArray = responseData.data
        }
      })
    },
    drawAllChart() {
      this.getFanucWaveData()
    },
    getFanucWaveData() {
      this.$refs.formParam.validate((valid) => {
      if (!valid) {
        return;
      }});
      this.queryLoading = true;
      getWaveDataByCycleNo(this.formParam).then((response) => {
        this.queryLoading = false;
        const responseData = response.data
        if (responseData.code === '000000') {
          this.waveData = responseData.data;

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
      const chartDom = document.getElementById(elementId);
      const myChart = echarts.init(chartDom);
      const paramName = this.paramNameArray.filter(x => x.key === elementId)[0].value
      let option;
      run(this.waveData[elementId], this.formParam.cycleNos, paramName)
      function run(_rawData, _cycleNos, _paramName) {
        const datasetWithFilters = []
        const seriesList = []
        const cycleNos = []

        echarts.util.each(_cycleNos, function (item) {
          const datasetId = 'dataset_' + item;
          cycleNos.push(item.toString())
          datasetWithFilters.push({
            id: datasetId,
            fromDatasetId: 'dataset_raw',
            transform: {
              type: 'filter',
              config: {
                and: [
                  { dimension: 'timeStamp', gte: 0 },
                  { dimension: '模次号', '=': item.toString() }
                ]
              }
            }
          })

          seriesList.push({
            type: 'line',
            name: item.toString(),
            datasetId: datasetId,
            showSymbol: false,
            encode: {
              x: 'timeStamp',
              y: '参数值',
              itemName: 'timeStamp',
              label: ['模次号', '参数值'],
              tooltip: ['模次号', '参数值']
            }
          });
        })

        option = {
          dataset: [
            {
              id: 'dataset_raw',
              source: _rawData
            },
            ...datasetWithFilters
          ],
          legend: {
            data: cycleNos,
            type: 'scroll',
            orient: 'horizontal'
          },
          title: {
            text: paramName
          },
          tooltip: {
            order: 'valueDesc',
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            },
            confine: true,
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
          xAxis: {
            type: 'category',
            name: '毫秒',
            nameTextStyle: {
              padding: [0, 0, 0, 15]
            }

          },
          yAxis: {
            type: 'value',
            name: '值',
            scale: true,
            axisLine: {
              show: true
            },
          },
          series: seriesList
        };
        myChart.setOption(option);
      }

      option && myChart.setOption(option);
    },

    handleDownload: function () {
      this.$refs.formParam.validate((valid) => {
        if (!valid) {
          return;
        }});
      this.exportLoading = true;
      downloadExcel(this.formParam)
          .then((response) => {
            this.exportLoading = false;
            if (response.headers['content-type'] === 'APPLICATION/OCTET-STREAM') {
              let filename = '注塑机波形明细.xlsx'
              let url = window.URL.createObjectURL(new Blob([response.data]))
              let link = document.createElement('a')
              link.style.display = 'none'
              link.href = url
              link.setAttribute('download', filename)
              document.body.appendChild(link)
              link.click()
            }
          })
    },

  }
}
</script>
