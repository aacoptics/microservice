<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="项目(project)" prop="projectName">
                <el-input v-model="filters.projectName" clearable placeholder="项目(project)"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="模具(mold)" prop="moldName">
                <el-input v-model="filters.moldName" clearable placeholder="模具(mold)"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="日期(date)" prop="currentDate">
                <el-date-picker v-model="filters.currentDate" auto-complete="off"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="findPageLoading" type="primary" @click="findPage(null)">查询(select)
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-row align="middle" class="float-right" justify="center">
            <el-form-item>
              <el-button :loading="exportReportLoading" type="success"
                         @click="exportReportExcelData('WLG-IPQC-2H-报表(WLG-IPQC-2H-report)')">导出报表(export)
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'download']"/>
                </template>
              </el-button>
            </el-form-item>
          </el-row>
        </el-form>
      </div>

      <SysTable id="condDataTable" ref="sysTable" :cellStyle="changeCellStyle" :columns="columns"
                :data="pageResult" :height="400" :highlightCurrentRow="true" :pageSize="100000000"
                :pageSizes="[100000000]" :show-operation="false"
                :showBatchDelete="false"
                :showPagination="false"
                :stripe="false" @findPage="findPage">
      </SysTable>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {date2str} from "@/utils/commonUtils";
import {
  qualityIpqcTwoHourReport,
  qualityIpqcTwoHourReportExportExcel
} from "@/api/wlg/manager/wlgQualityIpqcTwoHourReport";

export default {
  name: "wlgQualityIpqcTwoHourReport",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        projectName: "",
        moldName: "",
        currentDate: date2str(new Date().setDate(new Date().getDate() - 1)),
      },
      columns: [],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      errorResults: [],
      findPageLoading: false,
      exportReportLoading: false,
    };
  },
  methods: {
    changeCellStyle(row) { // 单元格样式
      //列的label的名称
      if (this.errorResults.filter(e => e["t1"] === row.row["id"] && e["t2"] === row.column.label).length > 0) {
        return {'background': 'red'} //修改的样式
      } else if (this.errorResults.filter(e => e["t1"] === row.row['id'] && e["t2"] === "两小时内未送测同一模次面型和芯厚").length > 0
          && ["项目号\n(project)", "模具\n(mold)", "周期\n(cycle)", "班序\n(Class order)"].includes(row.column.label)) {
        return {'background': 'yellow'} //修改的样式
      } else {
        return {}
      }
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.findPageLoading = true;
      this.pageRequest.projectName = this.filters.projectName;
      this.pageRequest.moldName = this.filters.moldName;
      this.pageRequest.currentDate = this.filters.currentDate != null ? date2str(this.filters.currentDate) : null;

      qualityIpqcTwoHourReport(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.columns = responseData.data['sysTableColumnDTOs'];
              this.pageResult = responseData.data['dataRecords'];
              this.errorResults = responseData.data['errorResults'];
            }
            this.findPageLoading = false;
          })
          .then(data != null ? data.callback : "");
    },
    exportReportExcelData(excelFileName) {
      this.pageRequest.projectName = this.filters.projectName;
      this.pageRequest.moldName = this.filters.moldName;
      this.pageRequest.currentDate = this.filters.currentDate != null ? date2str(this.filters.currentDate) : null;

      this.exportReportLoading = true;
      qualityIpqcTwoHourReportExportExcel(this.pageRequest).then(res => {
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
  },
};
</script>
<style>
.el-table .cell {
  white-space: pre-line;
}
</style>
