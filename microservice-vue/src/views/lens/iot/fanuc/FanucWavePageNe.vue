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
    </div>
  </div>
</template>

<script>

import {getAnalysisData, getCycleNosByTime, getWaveDataByCycleNo} from "@/api/lens/iot/fanucParamData";
import {selectEquips} from "@/api/lens/iot/fanucNe";
import * as echarts from 'echarts';
import FileSaver from 'file-saver'
import XLSX from 'xlsx'

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
      let option;
      console.log(this.waveData[elementId])
      run(this.waveData[elementId], this.formParam.cycleNos)


      function run(_rawData, _cycleNos) {

        const _dataSet = [
          {
            id: 'dataset_raw',
            source: _rawData
          }
        ]
        const _serials = []
        _cycleNos.forEach((item) =>{
          const singleData = {
            id: item,
            fromDatasetId: 'dataset_raw',
            transform: {
              type: 'filter',
              config: {
                and: [
                  { dimension: 'timeStamp', gte: 0 },
                  { dimension: 'cycleCount', '=': item.toString() }
                ]
              }
            }
          }

          const singleSerial = {
            type: 'line',
            datasetId: 'dataset_' + item,
            showSymbol: true,
            encode: {
              x: 'timeStamp',
              y: 'paramValue',
              itemName: 'timeStamp',
              tooltip: ['cycleCount', 'paramValue']
            }
          }
          _dataSet.push(singleData)
          _serials.push(singleSerial)
        })

        option = {
          dataset: _dataSet,
          title: {
            text: 'Income of Germany and France since 1950'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            nameLocation: 'middle',

          },
          yAxis: {
            name: 'paramValue',
            scale: true
          },
          series: _serials
        };
        myChart.setOption(option);
      }

      option && myChart.setOption(option);
    },

  }
}
</script>
