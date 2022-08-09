<template>
  <div>
    <div class="aac-container">
<!--      <el-tabs v-model="temperaturePlots" @tab-click="handleClick">-->
<!--        <el-tab-pane label="Spindle Temperature" name="spindle">-->
<!--          <div id="spindle" style="border:1px solid blue;height:600px;width:100%"></div>-->
<!--        </el-tab-pane>-->
<!--        <el-tab-pane label="Air Shower Temperature" name="airShower">-->
<!--          <div id="airShower" style="border:1px solid blue;height:600px;width:100%"></div>-->
<!--        </el-tab-pane>-->
<!--        <el-tab-pane label="Bearing Temperature" name="bearing">-->
<!--          <div id="bearing" style="border:1px solid blue;height:600px;width:100%"></div>-->
<!--        </el-tab-pane>-->
<!--        <el-tab-pane label="Motor Temperature" name="motor">-->
<!--          <div id="motor" style="border:1px solid blue;height:600px;width:100%"></div>-->
<!--        </el-tab-pane>-->
<!--      </el-tabs>-->
      <el-button type="primary" icon="el-icon-refresh" style="margin-bottom:20px" @click="refreshData">Refresh</el-button>
      <div id="spindle" style="border:1px solid blue;height:600px;width:100%"></div>
      <div id="airShower" style="border:1px solid blue;height:600px;width:100%"></div>
      <div id="bearing" style="border:1px solid blue;height:600px;width:100%"></div>
      <div id="motor" style="border:1px solid blue;height:600px;width:100%"></div>

    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import {
  getSpindleTemperature, getAirTemperature,
  getBearingTemperature,getMotorTemperature
} from "@/api/lens/iot/czech";
export default {
  name: "temperaturePlots",
  props: {
    machineNo: String,
  },
  data() {
    return {
      temperaturePlots: 'spindle',
      machineName: '',
      // airShowerPlot: '',
      // spindlePlot: '',
      // bearingPlot: '',
      // motorPlot: '',
      spindleDate: [],
      spindleTemperature: [],
      spindleTopTemperature: [],
      spindleBottomTemperature: [],
      airDate: [],
      airTemperature: [],
      airTopTemperature: [],
      airBottomTemperature: [],
      bearingDate: [],
      bearingTemperature: [],
      motorDate: [],
      motorTemperature: []
    }
  },
  created() {
    this.machineName = this.machineNo;

  },
  mounted() {
    this.getAirInfo();
    this.getSpindleInfo();
    this.getBearingInfo();
    this.getMotorInfo();
  },

  methods: {
    getFormatDate(date) {
      var seperator1 = "-";
      var seperator2 = ":";
      var month = date.getMonth() + 1;
      var strDate = date.getDate();
      var hour = date.getHours();
      var minutes = date.getMinutes();
      if(month >= 1 && month <= 9) {
        month = "0" + month;
      }
      if(strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
      }
      if(hour >= 0 && hour <= 9) {
        hour = "0" + hour;
      }
      if(minutes >= 0 && minutes <= 9) {
        minutes = "0" + hour;
      }
      var formatDate = date.getFullYear() + seperator1 + month + seperator1 + strDate
      + " " + hour + seperator2 + minutes + seperator2 + date.getSeconds();
      return formatDate
    },

    refreshData() {
      this.spindleDate = [];
      this.spindleTemperature = [];
      this.spindleTopTemperature = [];
      this.spindleBottomTemperature = [];
      this.airDate = [];
      this.airTemperature = [];
      this.airTopTemperature = [];
      this.airBottomTemperature = [];
      this.bearingDate = [];
      this.bearingTemperature = [];
      this.motorDate = [];
      this.motorTemperature = [];
      this.getAirInfo();
      this.getSpindleInfo();
      this.getBearingInfo();
      this.getMotorInfo();
    },

    getSpindleInfo() {
      // var endTime = this.getFormatDate(new Date());
      // var front12hour = new Date(new Date().getTime() - 12 * 60 * 60 * 1000);
      // var startTime = this.getFormatDate(front12hour);

      var endTime = this.getFormatDate(new Date(new Date().getTime() - 6 * 60 * 60 * 1000));
      var startTime = this.getFormatDate(new Date(new Date().getTime() - 18 * 60 * 60 * 1000));
      getSpindleTemperature(startTime, endTime, this.machineName).then((response) => {
            const responseData = response.data
            if (responseData.code === '000000') {
              responseData.data.forEach(item => {
                this.spindleDate.push(item.time);
                this.spindleTemperature.push(item.temperature);
                this.spindleTopTemperature.push(21.015);
                this.spindleBottomTemperature.push(20.985);
              })
              var arr = this.spindleTemperature.slice(0);
              console.log(arr);
              arr.sort(function (a, b) {
                return a - b;
              });
              const min = arr[0];
              console.log("min=" + min);
              const max = arr[arr.length - 1];
              console.log("max=" + max);
              //console.log(arr);
              this.drawSpindlePlot(this.machineName, min, max)
            }
      })

    },
    getAirInfo() {
      // var endTime = this.getFormatDate(new Date());
      // var front12hour = new Date(new Date().getTime() - 12 * 60 * 60 * 1000);
      // var startTime = this.getFormatDate(front12hour);

      var endTime = this.getFormatDate(new Date(new Date().getTime() - 6 * 60 * 60 * 1000));
      var startTime = this.getFormatDate(new Date(new Date().getTime() - 18 * 60 * 60 * 1000));
      getAirTemperature(startTime, endTime, this.machineName).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          responseData.data.forEach(item => {
            this.airDate.push(item.time);
            this.airTemperature.push(item.temperature);
            this.airTopTemperature.push(21.425);
            this.airBottomTemperature.push(21.375);
          })
          var arr = this.airTemperature.slice(0);
          arr.sort(function (a, b) {
            return a - b;
          });
          const min = arr[0];
          const max = arr[arr.length - 1];
          this.drawAirShowerPlot(this.machineName, min, max)
        }
      })
    },
    getBearingInfo() {
      // var endTime = this.getFormatDate(new Date());
      // var front12hour = new Date(new Date().getTime() - 12 * 60 * 60 * 1000);
      // var startTime = this.getFormatDate(front12hour);
      var endTime = this.getFormatDate(new Date(new Date().getTime() - 6 * 60 * 60 * 1000));
      var startTime = this.getFormatDate(new Date(new Date().getTime() - 18 * 60 * 60 * 1000));
      getBearingTemperature(startTime, endTime, this.machineName).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          responseData.data.forEach(item => {
            this.bearingDate.push(item.time);
            this.bearingTemperature.push(item.temperature);
          })
          this.drawBearingPlot(this.machineName)
        }
      })
    },
    getMotorInfo() {
      // var endTime = this.getFormatDate(new Date());
      // var front12hour = new Date(new Date().getTime() - 12 * 60 * 60 * 1000);
      // var startTime = this.getFormatDate(front12hour);
      var endTime = this.getFormatDate(new Date(new Date().getTime() - 6 * 60 * 60 * 1000));
      var startTime = this.getFormatDate(new Date(new Date().getTime() - 18 * 60 * 60 * 1000));
      getMotorTemperature(startTime, endTime, this.machineName).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          responseData.data.forEach(item => {
            this.motorDate.push(item.time);
            this.motorTemperature.push(item.temperature);
          })
          this.drawMotorPlot(this.machineName)
        }
      })
    },

    drawAirShowerPlot(machineName, min, max) {
      var chartDom = document.getElementById('airShower');
      var airShowerPlot = echarts.init(chartDom);
      var reg = new RegExp(/^\d+(?:\.\d{0,4})?/);
      var minValue = Number((min - (max-min)/10).toString().match(reg));
      var maxValue = Number((max + (max-min)/10).toString().match(reg));
      if(minValue > 21.375) {
        minValue = 21.375 - (21.425-21.375)/20
      }
      if(maxValue < 21.425) {
        maxValue = 21.425 + (21.425-21.375)/20
      }
      var intervalValue = Number(((maxValue-minValue)/20).toString().match(reg));
      var option;

      option = {
        title: {
          text: machineName + ' Air Shower',
          textAlign: 'center',
          x: 'center',
          y: 'top'
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['airTemperature']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.airDate,
          axisLabel: {
            margin: 10,
            interval: 100000,
            showMinLabel: true,
            showMaxLabel: true,
          },
        },
        yAxis: {
          type: 'value',
          min: minValue,
          max: maxValue,
          interval: intervalValue
        },
        dataZoom: [
          {
            type: 'slider',
            xAxisIndex: 0,
          },
          {
            type: 'inside',
            xAxisIndex: 0,
            zoomOnMouseWheel: 'alt'
          }
        ],
        series: [
          {
            name: 'Air Shower',
            type: 'line',
            data: this.airTemperature
          },
          {
            name: 'Air Shower Top',
            type: 'line',
            data: this.airTopTemperature
          },
          {
            name: 'Air Shower Bottom',
            type: 'line',
            data: this.airBottomTemperature
          }
        ]
      };
      airShowerPlot.setOption(option);
    },
    drawSpindlePlot(machineName, min, max) {
      var chartDom = document.getElementById('spindle');
      var spindlePlot = echarts.init(chartDom);
      var reg = new RegExp(/^\d+(?:\.\d{0,4})?/);
      var minValue = Number((min - (max-min)/10).toString().match(reg));
      var maxValue = Number((max + (max-min)/10).toString().match(reg));
      if(minValue > 20.985) {
        minValue = 20.985 - (21.015-20.985)/20
      }
      if(maxValue < 21.015) {
        maxValue = 21.015 + (21.015-20.985)/20
      }
      var intervalValue = Number(((maxValue-minValue)/20).toString().match(reg));

      var option;
      option = {
        title: {
          text: machineName + ' spindle',
          textAlign: 'center',
          x: 'center',
          y: 'top'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['Temperature']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.spindleDate,
          axisLabel: {
            margin: 10,
            interval: 100000,
            showMinLabel: true,
            showMaxLabel: true,
          }
        },
        yAxis: {
          type: 'value',
          min: minValue,
          max: maxValue,
          interval: intervalValue,
        },
        dataZoom: [
          {
            type: 'slider',
            xAxisIndex: 0,
          },
          {
            type: 'inside',
            xAxisIndex: 0,
            zoomOnMouseWheel: 'alt'
          }
        ],
        series: [
          {
            name: 'Spindle',
            type: 'line',
            data: this.spindleTemperature
          },
          {
            name: 'Spindle Top',
            type: 'line',
            data: this.spindleTopTemperature
          },
          {
            name: 'Spindle Bottom',
            type: 'line',
            data: this.spindleBottomTemperature
          }
        ]
      };
      spindlePlot.setOption(option);
    },
    drawBearingPlot(machineName) {
      var chartDom = document.getElementById('bearing');
      var bearingPlot = echarts.init(chartDom);
      var option;
      option = {
        title: {
          text: machineName + ' Bearing',
          textAlign: 'center',
          x: 'center',
          y: 'top'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['Temperature']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.bearingDate,
          axisLabel: {
            margin: 10,
            interval: 100000,
            showMinLabel: true,
            showMaxLabel: true,
          }
        },
        yAxis: {
          type: 'value',
          min: 20.965,
          max: 21.035,
          interval: 0.002
        },
        dataZoom: [
          {
            type: 'slider',
            xAxisIndex: 0,
          },
          {
            type: 'inside',
            xAxisIndex: 0,
            zoomOnMouseWheel: 'alt'
          }
        ],
        series: [
          {
            name: 'Bearing',
            type: 'line',
            data: this.bearingTemperature
          }
        ]
      };
      bearingPlot.setOption(option);
    },
    drawMotorPlot(machineName) {
      console.log(this.motorTemperature)
      var chartDom = document.getElementById('motor');
      var motorPlot = echarts.init(chartDom);
      var option;
      option = {
        title: {
          text: machineName + ' Motor',
          textAlign: 'center',
          x: 'center',
          y: 'top'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['Temperature']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.motorDate,
          axisLabel: {
            margin: 10,
            interval: 100000,
            showMinLabel: true,
            showMaxLabel: true,
          }
        },
        yAxis: {
          type: 'value',
          min: 20.985,
          max: 21.035,
          interval: 0.002
        },
        dataZoom: [
          {
            type: 'slider',
            xAxisIndex: 0,
          },
          {
            type: 'inside',
            xAxisIndex: 0,
            zoomOnMouseWheel: 'alt'
          }
        ],
        series: [
          {
            name: 'Motor',
            type: 'line',
            data: this.motorTemperature
          }
        ]
      };
      motorPlot.setOption(option);
    },

  }
}
</script>

<style scoped>

</style>
