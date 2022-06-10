<template>
  <div>
    <div class="aac-container">
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form ref="dataForm" :inline="true" :size="size" :model="filters" :rules="dataFormRules">
            <el-row>
              <el-form-item label="客户" prop="customer">
                <el-select v-model="filters.customer" clearable placeholder="请选择客户">
                  <el-option
                      v-for="item in customerOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="订单号"  prop="orderNo">
                <el-input v-model="filters.orderNo" placeholder="请输入订单号"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="findPage(null)">查询
                  <template #icon>
                    <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                  </template>
                </el-button>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="快递单"  prop="expressNo">
                <el-input v-model="filters.expressNo" placeholder="请输入快递单"></el-input>
              </el-form-item>
              <el-form-item label="ASN单/出货"  prop="asnNo">
                <el-input v-model="filters.asnNo" placeholder="请输入ASN单/出货"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="success" @click="uploadPackageInfo">信息上传
                  <template #icon>
                    <font-awesome-icon :icon="['fa', 'cloud-upload']"/>
                  </template>
                </el-button>
              </el-form-item>
            </el-row>
          </el-form>
        </div>
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
import {getAccessLogByTime, getLastMouthTotalCount, getLastWeekMenuCount} from "@/api/system/menu";
import {findUserInfoPage, findUserRolesById, handleAdd, handleUpdate} from "@/api/system/user";
import {getDict} from "@/api/system/dictData";
import {ElMessageBox} from "element-plus";
import {getShipmentInfos} from "@/api/lens/package/packageApi";

export default {
  name: "menuAccessLog",
  components: {SysTable},
  data() {
    return {
      size: 'default',
      filters: {
        customer: '',
        orderNo: '',
        expressNo: '',
        asnNo: ''
      },
      customerOptions: [],
      dataFormRules: {
        orderNo: [{required: true, message: '请输入订单号', trigger: 'blur'}],
        expressNo: [{required: true, message: '请输入快递单', trigger: 'blur'}],
        asnNo: [{required: true, message: '请输入ASN单/出货', trigger: 'blur'}],
        customer: [{required: true, message: "请选择客户", trigger: "blur"},],
      },
      columns: [
        {prop: "customer", label: "客户", minWidth: 80},
        {prop: "customerMaterialNo", label: "客户料号", minWidth: 80},
        {prop: "batchName", label: "品名", minWidth: 80},
        {prop: "supplier", label: "供应商", minWidth: 120},
        {prop: "outerBox", label: "大箱号", minWidth: 160},
        {prop: "outerBoxQty", label: "大箱数量", minWidth: 80},
        {prop: "outerBoxTime", label: "大箱时间", minWidth: 100, formatter: this.dateFormat},
        {prop: "spotTicket", label: "现品票", minWidth: 120},
        {prop: "spotQty", label: "现品票数量", minWidth: 160},
        {prop: "spotTime", label: "现品票时间", minWidth: 80, formatter: this.dateFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
    }
  },
  methods: {
    uploadPackageInfo(){
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          ElMessageBox.confirm('确认上传至客户吗？')
              .then(() => {
                done()
              })
              .catch(() => {
                // catch error
              })
        }
      })
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.customer = this.filters.customer
      this.pageRequest.orderNo = this.filters.orderNo
      getShipmentInfos(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },

    drawChart: function () {
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
      if (this.pieChart != null && this.pieChart !== "" && this.pieChart !== undefined) {
        this.pieChart.dispose();//销毁
      }
      this.pieChart = echarts.init(chartDom);
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
            label: {
              formatter: '{b}：{d}%'
            },
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
      option && this.lineChart.setOption(option);
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD')
    }
  },
  mounted() {
    getDict("package_customer").then(response => {
      this.customerOptions = response.data.data
    })
  },
  activated() {
    if(!this.firstLoad) {
      this.dateTimePickerValue = [
        new Date(new Date().getTime() - 3600 * 1000 * 24 * 7),
        new Date(),
      ]
      this.findPage(null)
    }
    this.firstLoad = false
  }
}
</script>
