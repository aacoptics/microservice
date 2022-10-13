<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-row>
            <el-col :span="5">
              <el-form-item label="项目" prop="project">
                <el-select v-model="filters.project" allow-create clearable filterable placeholder="项目">
                  <el-option
                      v-for="item in projectOptions"
                      :key="item"
                      :label="item"
                      :value="item"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="开始时间" prop="startSortingDate">
                <el-date-picker v-model="filters.startSortingDate" auto-complete="off"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="终止时间" prop="endSortingDate">
                <el-date-picker v-model="filters.endSortingDate" auto-complete="off"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="exportLoading" type="primary" @click="exportExcelData('越南筛选数据模板')">导出模板
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
          <el-row align="middle" class="float-right" justify="center">
            <el-form-item>
              <el-button size="small" type="info" @click="handleOpenExcelUpload">导入
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'upload']"/>
                </template>
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button :loading="exportReportLoading" type="success"
                         @click="exportReportExcelData('越南筛选数据报表')">导出报表
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'download']"/>
                </template>
              </el-button>
            </el-form-item>
          </el-row>
        </el-form>
      </div>

      <SysTable v-if="projectCountVisible" id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400"
                :highlightCurrentRow="true"
                :pageSize="projectCount"
                :pageSizes="[projectCount, projectCount*2, projectCount*3]" :show-operation="false"
                :showBatchDelete="false" :span-method="objectSpanMethod"
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
import {date2str, getResponseDataMessage, isFloat} from "@/utils/commonUtils";
import {
  exportExcel,
  listHeaders,
  listLineChat,
  listProject,
  listSummary,
  listSummaryExportExcel,
  uploadExcel
} from "@/api/lens/quality/qualitySortingData";
import {ElMessageBox} from "element-plus";
import * as echarts from "echarts";
import {getDict} from "@/api/system/dictData";

export default {
  name: "qualitySortingData",
  components: {SysTable},
  data() {
    this.lineChart = null
    return {
      size: "default",
      filters: {
        project: 'CAMMSYS 165220A01',
        startSortingDate: date2str(new Date().setDate(1)) + "T00:00:00",
        endSortingDate: date2str(new Date()) + "T00:00:00"
      },
      columns: [],
      pageRequest: {current: 1, size: 15},
      pageResult: {},
      excelUploadDialogVisible: false,
      exportLoading: false,
      exportReportLoading: false,
      lineChartData: {},
      avrStr: "",
      projectCount: 10,
      projectCountVisible: false,
      projectOptions: [],
    };
  },
  mounted() {
    getDict("quality_sorting_data").then(response => {
      this.projectCount = response.data.data.length + 1
      this.pageRequest.size = response.data.data.length + 1
      this.projectCountVisible = true
    })
    listProject().then(response => {
      if (response.data.data.length > 0) {
        this.projectOptions = response.data.data
      }
    })
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
        listProject().then(response => {
          if (response.data.data.length > 0) {
            this.projectOptions = response.data.data
            this.filters.project = response.data.data[0]
          }
        })
      }
      this.pageRequest.project = this.filters.project
      this.pageRequest.startSortingDate = this.filters.startSortingDate != null ? date2str(this.filters.startSortingDate) + "T00:00:00" : null;
      this.pageRequest.endSortingDate = this.filters.endSortingDate != null ? date2str(this.filters.endSortingDate) + "T00:00:00" : null;

      listHeaders(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.columns = responseData.data.map(c => {
                c.formatter = this.percentFormat
                return c
              });
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
              this.avrStr = Object.keys(responseData.data).filter(s => s.includes("AVR"))[0]
              this.drawLineChart()
            }
          })
          .then(data != null ? data.callback : "");
    },
    objectSpanMethod: function ({
                                  row,
                                  column,
                                  rowIndex,    // 需要合并的开始行
                                  columnIndex, // 需要合并的列
                                }) {
      if (columnIndex === 0) {
        if ((rowIndex % this.projectCount) === 0) {
          return {
            rowspan: this.projectCount,
            colspan: 1,
          }
        } else {
          return {
            rowspan: 0,
            colspan: 0,
          }
        }
      }
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
      this.pageRequest.project = this.filters.project
      this.pageRequest.startSortingDate = this.filters.startSortingDate != null ? date2str(this.filters.startSortingDate) + "T00:00:00" : null;
      this.pageRequest.endSortingDate = this.filters.endSortingDate != null ? date2str(this.filters.endSortingDate) + "T00:00:00" : null;

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
          text: '越南筛选数据折线图'
        },
        tooltip: {
          trigger: 'axis',
          formatter: '{b0}<br/>{a0}: {c0}<br />{a1}: {c1}%<br />{a2}: {c2}%'//展示百分比  五条折线
        },
        legend: {
          data: ['Sorting QTY', 'NG RATIO(%)', this.avrStr]
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
          boundaryGap: ['20%', '20%'],
          data: this.lineChartData['key'],
          axisLine: {show: false},
          axisTick: {show: false},
        },
        yAxis: [
          {
            type: 'value'
          },
          {
            type: 'value',
            axisLabel: {
              show: true,
              interval: 'auto',
              formatter: '{value}%',
            }
          }
        ],
        series: [
          {
            name: 'Sorting QTY',
            type: 'bar',
            barWidth: 30,
            yAxisIndex: 0,
            data: this.lineChartData['Sorting QTY']
          },
          {
            name: 'NG RATIO(%)',
            type: 'line',
            yAxisIndex: 1,
            data: this.lineChartData['NG RATIO(%)']
          },
          {
            name: this.avrStr,
            type: 'line',
            yAxisIndex: 1,
            data: this.lineChartData[this.avrStr],
            symbol: 'path://M196.23936 769.8432a40.96 40.96 0 1 0 57.91744 57.91744L512 569.91744l257.8432 257.8432a40.96 40.96 0 0 0 57.91744-57.91744L569.91744 512l257.8432-257.8432a40.96 40.96 0 0 0-57.91744-57.91744L512 454.08256 254.1568 196.1984a40.96 40.96 0 0 0-57.91744 57.91744l257.8432 257.8432-257.8432 257.8432z',     //设定为实心点
            symbolSize: 6,   //设定实心点的大小
          },
        ]
      };
      option.yAxis[0].max = Math.ceil(Math.max(Math.max(...this.lineChartData['Sorting QTY'])))
      option.yAxis[0].min = 0
      option.yAxis[1].max = Math.ceil(Math.max(Math.max(...this.lineChartData['NG RATIO(%)']), Math.max(...this.lineChartData[this.avrStr])))
      option.yAxis[1].min = Math.floor(Math.min(Math.min(...this.lineChartData['NG RATIO(%)']), Math.min(...this.lineChartData[this.avrStr])))
      // option.yAxis.max = 100 - option.yAxis.max < 1 ? 100 : option.yAxis.max + 1;
      // option.yAxis.min = option.yAxis.min < 1 ? 0 : option.yAxis.min - 1;
      option && this.lineChart.setOption(option);
    },
    percentFormat: function (row, column) {
      if (row[column.property] == null) return '-';
      if (typeof row[column.property] === 'number' && isFloat(row[column.property])) {
        let value = row[column.property] * 100;
        let valuePercent = value.toFixed(2);
        return `${valuePercent - value === 0 ? value : valuePercent}%`;
      } else
        return row[column.property];
    },
  },
};
</script>
