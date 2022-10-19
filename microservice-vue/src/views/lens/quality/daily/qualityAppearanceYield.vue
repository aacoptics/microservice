<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-row>
            <el-col :span="5">
              <el-form-item label="线体" prop="line">
                <el-select v-model="filters.line" allow-create clearable filterable placeholder="线体">
                  <el-option
                      v-for="item in lineOptions"
                      :key="item"
                      :label="item"
                      :value="item"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
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
          <el-row align="middle" class="float-right" justify="center">
            <el-form-item>
              <el-button size="small" type="info" @click="handleOpenExcelUpload">导入
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'upload']"/>
                </template>
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button :loading="exportReportLoading" type="success" @click="exportReportExcelData('外观良率报表')">
                导出报表
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'download']"/>
                </template>
              </el-button>
            </el-form-item>
          </el-row>
        </el-form>
      </div>

      <el-tag type="success">外观良率汇总</el-tag>
      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :show-operation="false" :showBatchDelete="false"
                :stripe="false" @findPage="findPage">
      </SysTable>

      <el-row class="w-full">
        <el-col :span="24">
          <div id="lineChart" class="w-full h-full" style="height: 300px"></div>
        </el-col>
      </el-row>

      <el-tag class="mt-10" type="success">外观良率跟踪</el-tag>
      <SysTable ref="detailSysTable" :cellStyle="changeCellStyle" :columns="detailColumns" :data="detailPageResult"
                :height="400" :highlightCurrentRow="true" :pageSize="100000000" :pageSizes="[100000000]"
                :rowClassName="tableRowClassName" :showBatchDelete="false"
                :showOperation="false"
                :showPagination="false"
                :spanMethod="objectSpanMethod"
                :stripe="false" @findPage="detailFindPage">
      </SysTable>

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
  listDetailHeaders,
  listDetailSummary,
  listHeaders,
  listLine,
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
        line: "",
        startAppearanceDate: date2str(new Date().setDate(1)) + "T00:00:00",
        endAppearanceDate: date2str(new Date().setDate(new Date().getDate() - 1)) + "T00:00:00"
      },
      columns: [],
      detailColumns: [],
      pageRequest: {current: 1, size: 100000000},
      pageResult: {},
      detailPageResult: {},
      excelUploadDialogVisible: false,
      exportLoading: false,
      exportReportLoading: false,
      lineChartData: {},
      lineOptions: [],
      spanAll: {},
    };
  },
  mounted() {
    listLine().then(response => {
      if (response.data.data.length > 0) {
        this.lineOptions = response.data.data
      }
    })
  },
  methods: {
    getSpanNum(curName, data) {
      const spanArray = []
      let pos = 0
      data.forEach((val, i) => {
        if (i === 0) {
          spanArray.push(1)
          pos = 0
        } else {
          // 判断当前列数据与下一行的该列数据是否相同
          if (data[i][curName] === data[i - 1][curName]) {
            // 每一列每一行的合并数量
            spanArray[pos] += 1
            spanArray.push(0)
          } else {
            spanArray.push(1)
            pos = i
          }
        }
      })
      // 把合并数据放入spanAll里面
      this.spanAll[curName] = spanArray
      console.log(this.spanAll);
    },
    objectSpanMethod: function ({
                                  row,
                                  column,
                                  rowIndex,    // 需要合并的开始行
                                  columnIndex, // 需要合并的列
                                }) { // 合并单元格

      if (column.label === "线体") {
        const rowNum = this.spanAll["线体"][rowIndex];
        // 列合并
        const colNum = rowNum > 0 ? 1 : 0
        return {
          rowspan: rowNum,
          colspan: colNum,
        }
      }
    },
    tableRowClassName(row) { // 行样式
      return row.row['型号'] === '汇总' ? 'gray-row' : '';
    },
    changeCellStyle(row) { // 单元格样式
      //列的label的名称
      if (row.column.label === "汇总") {
        return {'background': '#E5E8E8'} //修改的样式
      } else {
        return {}
      }
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }

      this.pageRequest.line = this.filters.line
      this.pageRequest.startAppearanceDate = this.filters.startAppearanceDate != null ? date2str(this.filters.startAppearanceDate) + "T00:00:00" : null;
      this.pageRequest.endAppearanceDate = this.filters.endAppearanceDate != null ? date2str(this.filters.endAppearanceDate) + "T00:00:00" : null;

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
              this.drawLineChart()
            }
          })
          .then(data != null ? data.callback : "");
      this.detailFindPage(null);
    },
    // 获取分页数据
    detailFindPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.line = this.filters.line
      this.pageRequest.startAppearanceDate = this.filters.startAppearanceDate != null ? date2str(this.filters.startAppearanceDate) + "T00:00:00" : null;
      this.pageRequest.endAppearanceDate = this.filters.endAppearanceDate != null ? date2str(this.filters.endAppearanceDate) + "T00:00:00" : null;

      listDetailHeaders(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.detailColumns = responseData.data.map(c => {
                c.formatter = this.percentFormat
                return c
              });
            } else {
              this.$message({message: "操作失败" + getResponseDataMessage(responseData), type: "error",});
            }
          })
      listDetailSummary(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.detailPageResult = responseData.data;
              this.getSpanNum("线体", responseData.data.records)
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
      this.pageRequest.line = this.filters.line
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
    percentFormat: function (row, column) {
      if (row[column.property] == null) return '-';
      if (typeof row[column.property] === 'number') {
        let value = row[column.property] * 100;
        let valuePercent = value.toFixed(2);
        return `${valuePercent - value === 0 ? value : valuePercent}%`;
      } else
        return row[column.property];
    },
  },
};
</script>
