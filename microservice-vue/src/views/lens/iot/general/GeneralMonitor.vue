<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 10px">
      <!--      <el-col :span="8">-->
      <!--        <el-card shadow="hover" class="mgb20" style="height:280px;">-->
      <!--          <div class="user-info">-->
      <!--            <img src="../../assets/img/avator.png" class="user-avator" alt/>-->
      <!--            <div class="user-info-cont">-->
      <!--              <div class="user-info-name">{{ userRealName }}</div>-->
      <!--              <div>{{ role }}</div>-->
      <!--            </div>-->
      <!--          </div>-->
      <!--          <p style="font-family: 'Microsoft YaHei',serif;font-weight: bold;font-size: 24px;margin-top: 40px">勇创数据大脑</p>-->
      <!--          <p style="font-family: 'Microsoft YaHei',serif;font-weight: bold;font-size: 24px">AAC让车间设施"智能感知"</p>-->
      <!--        </el-card>-->
      <!--      </el-col>-->
      <el-col :span="24">
        <el-card shadow="hover" style="height:280px;" id="totalStatusCount">
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" style="height:500px;">
          <div class="card-header" style="font-weight: bold">
            <span>注塑机</span>
          </div>
          <el-card v-for="(item, index) of getFanucCount" :key="index" style="margin-top: 10px">
            <div class="card-header" style="margin-bottom: 10px;">
              <span style="cursor: pointer" @click="onCardClick(item.floor, 'fanuc')">{{
                  item.floor.replace('F', '楼')
                }}</span>
            </div>
            <el-row align="middle" style="text-align: center">
              <el-col :span="5">
                <span class="typeItem_orderNum"
                      style="background-color: rgba(59,162,114,1);cursor: pointer"
                      @click="onCountClick(item.floor, 'fanuc', '02')">{{ item['02'] ? item['02'] : 0 }}</span>
              </el-col>
              <el-col :span="5">
                <span class="typeItem_orderNum"
                      style="background-color: rgba(250,200,88,1);cursor: pointer"
                      @click="onCountClick(item.floor, 'fanuc', '17')">{{ item['17'] ? item['17'] : 0 }}</span>
              </el-col>
              <el-col :span="4">
                <span class="typeItem_orderNum"
                      style="background-color: rgba(238,102,102,1);cursor: pointer"
                      @click="onCountClick(item.floor, 'fanuc', '03')">{{ item['03'] ? item['03'] : 0 }}</span>
              </el-col>
              <el-col :span="5">
                <span class="typeItem_orderNum" style="background-color: gray;cursor: pointer"
                      @click="onCountClick(item.floor, 'fanuc', '-1')">{{ item['-1'] ? item['-1'] : 0 }}</span>
              </el-col>
              <el-col :span="5">
                <span class="typeItem_orderNum"
                      style="background-color: rgba(252,132,82,1);cursor: pointer"
                      @click="onCountClick(item.floor, 'fanuc', 'default')">{{
                    getFanucTotalCount(item.floor)
                    - (item['02'] ? item['02'] : 0)
                    - (item['17'] ? item['17'] : 0)
                    - (item['03'] ? item['03'] : 0)
                    - (item['-1'] ? item['-1'] : 0)
                  }}</span>
              </el-col>
            </el-row>
            <el-row align="middle"
                    style="text-align: center;font-family: 'Microsoft YaHei',serif;font-size: 11px;font-weight:bold;margin-top: 10px">
              <el-col :span="5">
                <span>自动运转</span>
              </el-col>
              <el-col :span="5">
                <span>低温保持</span>
              </el-col>
              <el-col :span="4">
                <span>报警</span>
              </el-col>
              <el-col :span="5">
                <span>离线</span>
              </el-col>
              <el-col :span="5">
                <span>其他</span>
              </el-col>
            </el-row>
          </el-card>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" style="height:500px;">
          <div class="card-header" style="font-weight: bold">
            <span>包料机</span>
          </div>
          <el-card v-for="(item, index) of getLensPackerCount" :key="index" style="margin-top: 10px">
            <div class="card-header" style="margin-bottom: 10px">
              <span style="cursor: pointer"
                    @click="onCardClick(item.floor, 'lensPacker')">{{ item.floor.replace('F', '楼') }}</span>
            </div>
            <el-row align="middle" style="text-align: center">
              <el-col :span="8">
                <span class="typeItem_orderNum"
                      style="background-color: rgba(59,162,114,1);cursor: pointer"
                      @click="onCountClick(item.floor, 'lensPacker', '正常运行')">{{ item['1'] ? item['1'] : 0 }}</span>
              </el-col>
              <el-col :span="8">
                <span class="typeItem_orderNum"
                      style="background-color: rgba(238,102,102,1);cursor: pointer"
                      @click="onCountClick(item.floor, 'lensPacker', '报警')">{{ item['2'] ? item['2'] : 0 }}</span>
              </el-col>
              <el-col :span="8">
                <span class="typeItem_orderNum"
                      style="background-color: gray;cursor: pointer"
                      @click="onCountClick(item.floor, 'lensPacker', '设备离线')">{{ item['0'] ? item['0'] : 0 }}</span>
              </el-col>
            </el-row>
            <el-row align="middle"
                    style="text-align: center;font-family: 'Microsoft YaHei',serif;font-size: 11px;font-weight: bold;margin-top: 10px">
              <el-col :span="8">
                <span>自动运转</span>
              </el-col>
              <el-col :span="8">
                <span>报警</span>
              </el-col>
              <el-col :span="8">
                <span>离线</span>
              </el-col>
            </el-row>
          </el-card>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" style="height:500px;">
          <div class="card-header" style="font-weight: bold">
            <span>镀膜机</span>
          </div>
          <el-card v-for="(item, index) of getCoatingCount" :key="index" style="margin-top: 10px">
            <div class="card-header" style="margin-bottom: 10px">
              <span style="cursor: pointer" @click="onCardClick(item.floor, 'coating')">{{ item.floor }}</span>
            </div>
            <el-row align="middle" style="text-align: center">
              <el-col :span="8">
                <span class="typeItem_orderNum"
                      style="background-color: rgba(59,162,114,1);cursor: pointer"
                      @click="onCountClick(item.floor, 'coating', '正常运行')">{{ item['0'] ? item['0'] : 0 }}</span>
              </el-col>
              <el-col :span="8">
                <span class="typeItem_orderNum"
                      style="background-color: rgba(154,96,180,1);cursor: pointer"
                      @click="onCountClick(item.floor, 'coating', '上料预警')">{{ item['1'] ? item['1'] : 0 }}</span>
              </el-col>
              <el-col :span="8">
                <span class="typeItem_orderNum"
                      style="background-color: gray;cursor: pointer"
                      @click="onCountClick(item.floor, 'coating', '设备离线')">{{ item['-1'] ? item['-1'] : 0 }}</span>
              </el-col>
            </el-row>
            <el-row align="middle"
                    style="text-align: center;font-family: 'Microsoft YaHei',serif;font-size: 11px;font-weight: bold;margin-top: 10px">
              <el-col :span="8">
                <span>自动运转</span>
              </el-col>
              <el-col :span="8">
                <span>上料预警</span>
              </el-col>
              <el-col :span="8">
                <span>离线</span>
              </el-col>
            </el-row>
          </el-card>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getFanucStatusCount} from "@/api/lens/iot/fanucNe"
import {getLensPackerStatusCount} from "@/api/lens/iot/lenspackerXny"
import {getCoatingStatusCount} from "@/api/lens/iot/coatingNe"
import * as echarts from 'echarts';

export default {
  name: "dashboard",
  data() {
    return {
      lensPacker: {},
      fanuc: {},
      coating: {}
    };
  },
  created() {
    this.timer = setInterval(() => {
      this.getStatusCount()
    }, 10000)
  },
  beforeUnmount() {
    clearInterval(this.timer);
  },
  mounted() {
    this.getStatusCount()
  },
  computed: {
    getTotalCount() {
      const totalJson = {
        automatic: {fanuc: 0, lensPacker: 0, coating: 0},
        alarm: {fanuc: 0, lensPacker: 0, coating: 0},
        feed: {fanuc: 0, lensPacker: 0, coating: 0},
        disconnect: {fanuc: 0, lensPacker: 0, coating: 0},
        others: {fanuc: 0, lensPacker: 0, coating: 0}
      }
      for (const floor in this.fanuc) {
        for (const status in this.fanuc[floor]) {
          if (status === '02') {
            totalJson.automatic.fanuc += this.fanuc[floor][status]
          } else if (status === '03') {
            totalJson.alarm.fanuc += this.fanuc[floor][status]
          } else if (status === '-1') {
            totalJson.disconnect.fanuc += this.fanuc[floor][status]
          } else {
            if (typeof this.fanuc[floor][status] === 'number' && !isNaN(this.fanuc[floor][status]))
              totalJson.others.fanuc += this.fanuc[floor][status]
          }
        }
      }

      for (const floor in this.lensPacker) {
        for (const status in this.lensPacker[floor]) {
          if (status === '1') {
            totalJson.automatic.lensPacker += this.lensPacker[floor][status]
          } else if (status === '2') {
            totalJson.alarm.lensPacker += this.lensPacker[floor][status]
          } else if (status === '0') {
            totalJson.disconnect.lensPacker += this.lensPacker[floor][status]
          }
        }
      }

      for (const floor in this.coating) {
        for (const status in this.coating[floor]) {
          if (status === '0') {
            totalJson.automatic.coating += this.coating[floor][status]
          } else if (status === '1') {
            totalJson.feed.coating += this.coating[floor][status]
          } else if (status === '-1') {
            totalJson.disconnect.coating += this.coating[floor][status]
          }
        }
      }

      return totalJson
    },
    getFanucCount() {
      const totalJson = []
      for (const floor in this.fanuc) {
        const floorData = this.fanuc[floor]
        floorData.floor = floor
        totalJson.push(floorData)
      }
      return totalJson
    },
    getLensPackerCount() {
      const totalJson = []
      for (const floor in this.lensPacker) {
        const floorData = this.lensPacker[floor]
        floorData.floor = floor
        totalJson.push(floorData)
      }
      return totalJson
    },
    getCoatingCount() {
      const totalJson = []
      for (const floor in this.coating) {
        const floorData = this.coating[floor]
        floorData.floor = floor
        totalJson.push(floorData)
      }
      return totalJson
    }
  },
  methods: {
    getStatusCount() {
      getFanucStatusCount().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.fanuc = responseData.data;

          getLensPackerStatusCount().then((response) => {
            const responseData = response.data
            if (responseData.code === '000000') {
              this.lensPacker = responseData.data;

              getCoatingStatusCount().then((response) => {
                const responseData = response.data
                if (responseData.code === '000000') {
                  this.coating = responseData.data;

                  setTimeout(() => { //延时加载echarts初始化函数
                    this.refreshTotalCountData()
                  }, 0)
                }
              })
            }
          })
        }
      })
    },
    zero_format() {
      return function (params) {
        if (params.value > 2) {
          return params.value;
        } else {
          return '';
        }
      }
    },
    drawTotalCountChart() {
      const chartDom = document.getElementById('totalStatusCount');
      const myChart = echarts.init(chartDom);
      let option;
      const totalCount = this.getTotalCount
      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {            // Use axis to trigger tooltip
            type: 'shadow'        // 'shadow' as default; can also be 'line' or 'shadow'
          }
        },
        legend: {
          data: ['自动运转', '设备报警', '上料预警', '离线', '其他']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: ['注塑机', '包料机', '镀膜机']
        },
        series: [
          {
            name: '自动运转',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.automatic.fanuc, totalCount.automatic.lensPacker, totalCount.automatic.coating]
          },
          {
            name: '设备报警',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.alarm.fanuc, totalCount.alarm.lensPacker, totalCount.alarm.coating]
          },
          {
            name: '上料预警',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.feed.fanuc, totalCount.feed.lensPacker, totalCount.feed.coating]
          },
          {
            name: '离线',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.disconnect.fanuc, totalCount.disconnect.lensPacker, totalCount.disconnect.coating]
          },
          {
            name: '其他',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.others.fanuc, totalCount.others.lensPacker, totalCount.others.coating]
          }
        ],
        color: ["rgba(59,162,114,1)", "rgba(238,102,102,1)", "rgba(154,96,180,1)", "gray", "rgba(252,132,82,1)"]

      };

      option && myChart.setOption(option);

    },
    refreshTotalCountData() {
      const chartDom = document.getElementById('totalStatusCount');
      let myChart = undefined;
      try {
        myChart = echarts.getInstanceByDom(chartDom)
      } catch {
        return;
      }
      if (!myChart) {
        this.drawTotalCountChart()
        return;
      }
      const totalCount = this.getTotalCount
      myChart.setOption({
        series: [
          {
            name: '自动运转',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.automatic.fanuc, totalCount.automatic.lensPacker, totalCount.automatic.coating]
          },
          {
            name: '设备报警',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.alarm.fanuc, totalCount.alarm.lensPacker, totalCount.alarm.coating]
          },
          {
            name: '上料预警',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.feed.fanuc, totalCount.feed.lensPacker, totalCount.feed.coating]
          },
          {
            name: '离线',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.disconnect.fanuc, totalCount.disconnect.lensPacker, totalCount.disconnect.coating]
          },
          {
            name: '其他',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
              formatter: this.zero_format()
            },
            emphasis: {
              focus: 'series'
            },
            data: [totalCount.others.fanuc, totalCount.others.lensPacker, totalCount.others.coating]
          }
        ],
      });
      // const option = myChart.getOption();
      // option.series[0].data = data;
      // myChart.setOption(option);
    },
    getFanucTotalCount(floor) {
      let nums = 0
      if (this.fanuc[floor]) {
        for (const status in this.fanuc[floor]) {
          if (typeof this.fanuc[floor][status] === 'number' && !isNaN(this.fanuc[floor][status])) {
            nums += this.fanuc[floor][status]
          }
        }
      }
      return nums
    },
    onCardClick(floor, machineType) {
      if (machineType === 'fanuc') {
        this.$router.push('/fanuc' + floor + '?position=3B' + floor)
      } else if (machineType === 'lensPacker') {
        this.$router.push('/lensPacker' + floor + '?position=3B' + floor)
      } else if (machineType === 'coating') {
        this.$router.push('/coating' + floor.substring(0, 1) + '?position=' + floor.substring(0, 1))
      }
    },
    onCountClick(floor, machineType, status) {
      switch (machineType) {
        case "fanuc":
          this.$router.push({name: 'Fanuc3B' + floor, query: {position: '3B' + floor}, params: {status: status}})
          break;
        case "lensPacker":
          this.$router.push({name: 'lensPacker3B' + floor, query: {position: '3B' + floor}, params: {status: status}})
          break;
        case "coating":
          this.$router.push({
            name: 'coatingMonitor' + floor.substring(0, 1),
            query: {position: floor.substring(0, 1)},
            params: {status: status}
          })
          break;
      }

    }
  }
}
</script>

<style scoped>

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-avator {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}
</style>
