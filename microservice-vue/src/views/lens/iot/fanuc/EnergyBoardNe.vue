<template>
  <div>
    <div class="aac-container">
      <el-row>
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form :inline="true" :size="size">
            <el-form-item>
              <el-button type="primary" @click="refreshPage">刷新
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-row>
      <el-row>

        <el-col :span="6">
          <div id="gaugeChart"
               style="margin-top: 10px;height: 600px; width: 100%"></div>
        </el-col>
        <el-col :span="18">
          <div id="lineChart"
               style="margin-top: 10px;height: 600px; width: 100%"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import {getCurrentEnergy, getLastMouthEnergy} from "@/api/lens/iot/fanucNe";

export default {
  name: "energyBoard",
  data() {
    this.pieChart = null
    this.lineChart = null
    return {
      size: 'default',
      firstLoad: true,
      pieChartData: 0,
      lineChartData: {
        name: [],
        value: []
      },
    }
  },
  methods: {
    refreshPage() {
      this.$nextTick(() => {
        this.drawChart();
      })
    },
    // 获取分页数据

    drawChart: function () {
      getCurrentEnergy().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          const res = responseData.data
          this.pieChartData = res

          this.drawPieChart()
        }
      })

      getLastMouthEnergy().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          const res = responseData.data
          this.lineChartData = {
            name: [],
            value: []
          }
          res.forEach((item) => {
            this.lineChartData.name.push(item.moldStartDate)
            this.lineChartData.value.push(item.energy)
          })
          this.drawLineChart()
        }
      })
    },
    drawPieChart() {
      const chartDom = document.getElementById('gaugeChart');
      if (this.pieChart != null && this.pieChart !== "" && this.pieChart !== undefined) {
        this.pieChart.dispose();//销毁
      }
      this.pieChart = echarts.init(chartDom);
      let option;

      option = {
        title: {
          text: '当月电量累计'
        },
        tooltip: {
          formatter: '{a} <br/>{c}{b}'
        },
        series: [
          {
            min: 0,
            max: 100000,
            name: '当月累计',
            type: 'gauge',
            detail: {
              valueAnimation: true,
              formatter: function formatNum(value) {
                if (!value && value !== 0) return 0;

                let str = value.toString();
                let reg = str.indexOf(".") > -1 ? /(\d)(?=(\d{3})+\.)/g : /(\d)(?=(?:\d{3})+$)/g;
                return str.replace(reg, "$1,")
              }
            },
            data: [
              {
                value: this.pieChartData,
                name: 'KWh'
              }
            ]
          }
        ]
      };

      option && this.pieChart.setOption(option);
    },
    drawLineChart() {
      const chartDom = document.getElementById('lineChart');
      if (this.lineChart != null && this.lineChart !== "" && this.lineChart !== undefined) {
        this.lineChart.dispose();//销毁
      }
      this.lineChart = echarts.init(chartDom);
      let option;

      option = {
        title: {
          text: '近一个月电量统计'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.lineChartData.name
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: this.lineChartData.value,
            type: 'line',
            smooth: true
          }
        ]
      };
      option && this.lineChart.setOption(option);
    }
  },
  activated() {
    if (!this.firstLoad) {
      this.$nextTick(() => {
        this.drawChart();
      })
    }
    this.firstLoad = false
  },
  mounted() {
    this.$nextTick(() => {
      this.drawChart();
    })
  }
}
</script>
