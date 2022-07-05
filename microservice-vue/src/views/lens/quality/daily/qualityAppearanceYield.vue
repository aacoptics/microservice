<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-row>
            <el-col :span="5">
              <el-form-item label="开始时间" prop="startAppearanceDate">
                <el-date-picker v-model="filters.startAppearanceDate" auto-complete="off"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="终止时间" prop="endAppearanceDate">
                <el-date-picker v-model="filters.endAppearanceDate" auto-complete="off"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button type="info" @click="handleOpenExcelUpload">Excel导入
              <template #icon>
                <font-awesome-icon :icon="['fas', 'upload']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button :loading="exportLoading" type="primary" @click="exportExcelData('质量外观良率模板')">导出模板
              <template #icon>
                <font-awesome-icon :icon="['fas', 'download']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item class="float-right">
            <el-button :loading="exportReportLoading" type="success" @click="exportReportExcelData('外观良率报表')">导出报表
              <template #icon>
                <font-awesome-icon :icon="['fas', 'download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :show-operation="false" :showBatchDelete="false"
                :stripe="false" @findPage="findPage">
      </SysTable>

      <el-row class="w-full">
        <el-col :span="24">
          <div id="lineChart" class="w-full h-full" style="height: 300px"></div>
        </el-col>
      </el-row>

      <el-dialog v-model="excelUploadDialogVisible" :close-on-click-modal="false" :title="'Excel导入'"
                 width="25%">
        <el-upload
            :before-upload="beforeUpload"
            :http-request="submitExcelUpload"
            :multiple="false"
            :show-file-list="false"
            accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            action=""
            class="upload-demo"
            drag>
          <font-awesome-icon :icon="['fas', 'cloud-arrow-up']" size="6x"/>
          <div class="el-upload__text">将Excel文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {date2str, getResponseDataMessage} from "@/utils/commonUtils";
import {
  exportExcel,
  listHeaders,
  listLineChat,
  listSummary,
  listSummaryExportExcel,
  uploadExcel
} from "@/api/lens/quality/qualityAppearanceYield";
import {ElMessageBox} from "element-plus";
import * as echarts from "echarts";

export default {
  name: "qualityAppearanceYield",
  components: {SysTable},
  data() {
    this.lineChart = null
    return {
      size: "default",
      filters: {
        startAppearanceDate: date2str(new Date().setDate(new Date().getDate() - 6)) + "T00:00:00",
        endAppearanceDate: date2str(new Date()) + "T00:00:00"
      },
      columns: [],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      excelUploadDialogVisible: false,
      exportLoading: false,
      exportReportLoading: false,
      lineChartData: {},
    };
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }

      this.pageRequest.startAppearanceDate = this.filters.startAppearanceDate != null ? date2str(this.filters.startAppearanceDate) + "T00:00:00" : null;
      this.pageRequest.endAppearanceDate = this.filters.endAppearanceDate != null ? date2str(this.filters.endAppearanceDate) + "T00:00:00" : null;

      listHeaders(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.columns = responseData.data;
            } else {
              this.$message({message: "操作失败" + getResponseDataMessage(responseData), type: "error",});
            }
          })
      listSummary(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
      listLineChat(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.lineChartData = responseData.data;
              this.drawLineChart()
            }
          })
          .then(data != null ? data.callback : "");
    },
    handleOpenExcelUpload: function () {
      this.excelUploadDialogVisible = true
    },
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel'
      if (!isExcel) {
        this.$message.error('只能上传xlsx, xls格式的文件！')
        return false
      }
    },
    submitExcelUpload(params) {
      uploadExcel(params).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          ElMessageBox.alert('上传成功！' + getResponseDataMessage(responseData, '</br>'), '上传信息', {
            dangerouslyUseHTMLString: true
          })
        } else {
          ElMessageBox.alert('上传失败！' + getResponseDataMessage(responseData, '</br>'), '上传信息', {
            dangerouslyUseHTMLString: true
          })
        }
        this.findPage(null);
        this.excelUploadDialogVisible = false;
      }).catch((err) => {
        ElMessageBox.alert(err, '上传信息', {
          dangerouslyUseHTMLString: true,
          type: 'error'
        })
        this.excelUploadDialogVisible = false;
      })
    },
    exportExcelData(excelFileName) {
      this.exportLoading = true;
      exportExcel().then(res => {
        this.exportLoading = false;
        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', excelFileName + ".xlsx");
        document.body.appendChild(link);
        link.click();
      })
    },
    exportReportExcelData(excelFileName) {
      this.pageRequest.startAppearanceDate = this.filters.startAppearanceDate != null ? date2str(this.filters.startAppearanceDate) + "T00:00:00" : null;
      this.pageRequest.endAppearanceDate = this.filters.endAppearanceDate != null ? date2str(this.filters.endAppearanceDate) + "T00:00:00" : null;

      this.exportReportLoading = true;
      listSummaryExportExcel(this.pageRequest).then(res => {
        this.exportReportLoading = false;
        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', excelFileName + "-" + new Date().getTime() + ".xlsx");
        document.body.appendChild(link);
        link.click();
      });
    },
    drawLineChart() {
      const chartDom = document.getElementById('lineChart');
      if (this.lineChart != null) {
        this.lineChart.dispose();//销毁
      }
      this.lineChart = echarts.init(chartDom);
      let option = {
        title: {
          text: '外观良率折线图'
        },
        tooltip: {
          trigger: 'axis',
          formatter: '{b0}<br/>{a0}: {c0}%<br />{a1}: {c1}%<br />{a2}: {c2}%'//展示百分比  五条折线
        },
        legend: {
          data: ['最终目标', '目标', '汇总']
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
          data: this.columns.map(item => item.label).slice(1)
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            show: true,
            interval: 'auto',
            formatter: '{value}%',
          }
        },
        series: [
          {
            name: '最终目标',
            type: 'line',
            data: this.lineChartData['最终目标']
          },
          {
            name: '目标',
            type: 'line',
            data: this.lineChartData['目标']
          },
          {
            name: '汇总',
            type: 'line',
            data: this.lineChartData['汇总']
          }
        ]
      };
      option.yAxis.max = Math.ceil(Math.max(Math.max(...this.lineChartData['最终目标']), Math.max(...this.lineChartData['目标']), Math.max(...this.lineChartData['汇总'])))
      option.yAxis.min = Math.floor(Math.min(Math.min(...this.lineChartData['最终目标']), Math.min(...this.lineChartData['目标']), Math.min(...this.lineChartData['汇总'])))
      // option.yAxis.max = 100 - option.yAxis.max < 1 ? 100 : option.yAxis.max + 1;
      // option.yAxis.min = option.yAxis.min < 1 ? 0 : option.yAxis.min - 1;
      option && this.lineChart.setOption(option);
    },
  },
};
</script>
