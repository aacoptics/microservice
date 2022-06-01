<template>
  <div>
    <div class="aac-container">
      <el-row>
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form :inline="true" :size="size">
            <el-form-item>
              <el-button type="primary" @click="findPage(null)">刷新
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                </template>
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-row>
      <el-row>
        <el-col :span="12">
          <div id="pieChart"
               style="margin-top: 10px;height: 300px; width: 100%"></div>
        </el-col>
        <el-col :span="12">
          <div id="lineChart"
               style="margin-top: 10px;height: 300px; width: 100%"></div>
        </el-col>
      </el-row>
      <el-date-picker
          v-model="dateTimePickerValue"
          type="datetimerange"
          :shortcuts="shortcuts"
          :size="size"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          class="float-right mb-4"
          @change="findPage(null)"
      />
      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false" :showOperation="false"
                :stripe="false"
                @findPage="findPage">
      </SysTable>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import * as echarts from 'echarts';
import {getLastMouthTotalCount, getAccessLogByTime, getLastWeekMenuCount} from "@/api/system/menu";
import {findUserRolesById} from "@/api/system/user";

export default {
  name: "menuAccessLog",
  components: {SysTable},
  data() {
    return {
      size: 'default',
      filters: {
        username: ''
      },
      columns: [
        {prop: "title", label: "菜单名称", minWidth: 80},
        {prop: "username", label: "访问人工号", minWidth: 80},
        {prop: "realName", label: "访问人姓名", minWidth: 80},
        {prop: "os", label: "操作系统", minWidth: 160},
        {prop: "platform", label: "平台", minWidth: 80},
        {prop: "browser", label: "浏览器", minWidth: 100},
        {prop: "version", label: "版本", minWidth: 120},
        {prop: "accessTime", label: "访问时间", minWidth: 120, formatter: this.dateFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      pieChartData: [],
      lineChartData: {
        name: [],
        value: []
      },
      dateTimePickerValue: [
        new Date(new Date().getTime() - 3600 * 1000 * 24 * 7),
        new Date(),
      ],
      shortcuts: [
        {
          text: '近一周',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            return [start, end]
          },
        },
        {
          text: '近一个月',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            return [start, end]
          },
        },
        {
          text: '近三个月',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            return [start, end]
          },
        },
      ]
    }
  },
  methods: {
    handleUserSelectChange(val) {
      if (val == null || val.val == null) {
        this.currentUserRoles = []
        this.dataForm.roleIds = []
        return
      }
      this.selectUser = val.val
      findUserRolesById(val.val.id).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.currentUserRoles = responseData.data
          this.getCurrentUserRoleIds()
        }
      })
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }

      if(this.dateTimePickerValue === null)
      {
        this.$message.warning('必须选则查询的时间范围！')
        return
      }

      this.pageRequest.startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      this.pageRequest.endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      getAccessLogByTime(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },

    drawChart: function (){
      getLastWeekMenuCount().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          const res = responseData.data
          this.pieChartData = []
          res.forEach((item) => {
            this.pieChartData.push({value: item.totalCount, name: item.title})
          })
          this.drawPieChart()
        }
      })

      getLastMouthTotalCount().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          const res = responseData.data
          this.lineChartData = {
            name: [],
            value: []
          }
          res.forEach((item) => {
            this.lineChartData.name.push(item.accessDate)
            this.lineChartData.value.push(item.totalCount)
          })
          this.drawLineChart()
        }
      })
    },
    drawPieChart() {
      const chartDom = document.getElementById('pieChart');
      const myChart = echarts.init(chartDom);
      let option;

      option = {
        title: {
          text: '近一周访问统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          type: 'scroll'
        },
        series: [
          {
            name: '受访菜单',
            type: 'pie',
            radius: '50%',
            data: this.pieChartData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      option && myChart.setOption(option);
    },
    drawLineChart() {
      const chartDom = document.getElementById('lineChart');
      const myChart = echarts.init(chartDom);
      let option;

      option = {
        title: {
          text: '近一个月访问趋势统计'
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
      option && myChart.setOption(option);
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    }
  },
  mounted() {
    this.drawChart();
  }
}
</script>
