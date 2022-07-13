<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-row>
            <el-form-item label="项目" prop="projectName">
              <el-input
                  v-model="filters.projectName"
                  :size="size"
                  clearable
                  placeholder="请输入项目"
              />
            </el-form-item>

            <el-form-item label="日期 从" prop="productionDateStart">
              <el-date-picker v-model="filters.productionDateStart" auto-complete="off" type="date"
                              value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
            <el-form-item label="到" prop="productionDateEnd">
              <el-date-picker v-model="filters.productionDateEnd" auto-complete="off" type="date"
                              value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="queryLoading"
                       type="primary"
                       @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="info"
                       @click="handleOpenExcelUpload">Excel导入
              <template #icon>
                <font-awesome-icon :icon="['fas','file-lines']"/>
              </template>
            </el-button>
          </el-form-item>

        </el-form>
      </div>
      <QueryAllTable id="condDataTable" ref="queryTable" :columns="columns" :data="pageResult"
                     :height="550" :highlightCurrentRow="true"
                     :stripe="true"
                     @findPage="findPage">
      </QueryAllTable>

      <el-dialog v-model="excelUploadDialogVisible" :close-on-click-modal="false" :title="'生产汇总Excel导入'"
                 width="500px">
        <el-upload
            :before-upload="beforeUpload"
            :http-request="submitExcelUpload"
            :multiple="false"
            :show-file-list="false"
            accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            action=""
            class="upload-demo"
            drag>
          <font-awesome-icon :icon="['fas','cloud-arrow-up']" class="el-icon--upload"/>
          <div class="el-upload__text">将Excel文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-progress
                :duration="pregressDuration"
                :indeterminate="true"
                :percentage="progressPercentage"
                :status="progressStatus"
                :stroke-width="20"
                :text-inside="true"
                style="width:450px"
            >
              <span>{{ progressContent }}</span>
            </el-progress>
            <div style="padding-top: 20px;">
              <el-button :loading="downloadTemplateLoading" :size="size" style="position: absolute;left: 20px;"
                         type="primary" @click="downloadTemplate('actual')">生产实际模板下载
              </el-button>
              <el-button :loading="downloadTemplateLoading" :size="size" style="position: absolute;left: 150px;"
                         type="primary" @click="downloadTemplate('target')">生产目标模板下载
              </el-button>
              <el-button :size="size" right type="success" @click="cancelExcelUpload">关闭</el-button>
            </div>
          </slot>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>

import QueryAllTable from "@/components/QueryAllTable";
import {downloadTemplate, findProductionSummaryPage, uploadExcel} from "@/api/wlg/report/productionSummary";

export default {
  name: "productionSummary",
  components: {QueryAllTable},
  data() {
    return {
      size: 'default',
      queryLoading: false,
      downloadTemplateLoading: false,

      progressPercentage: 0,
      progressContent: "",
      pregressDuration: 6,
      progressStatus: "",

      filters: {
        projectName: '',
        productionDateStart: '',
        productionDateEnd: '',
      },
      columns: [
        {prop: "__row_number__", label: "序号", minWidth: 80},
        {prop: "projectName", label: "项目", minWidth: 120},
        {prop: "productionDate", label: "月份", minWidth: 120, formatter: this.dateFormat},
        {prop: "targetQty", label: "目标产出数量", minWidth: 100},
        {prop: "actualQty", label: "实际产出数量", minWidth: 120},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      excelUploadDialogVisible: false,
    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      // if (data !== null) {
      //   this.pageRequest = data.pageRequest
      // }
      this.pageRequest.projectName = this.filters.projectName;
      this.pageRequest.productionDateStart = this.filters.productionDateStart;
      this.pageRequest.productionDateEnd = this.filters.productionDateEnd;

      this.queryLoading = true;
      findProductionSummaryPage(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.columns = responseData.data.titleResult;
          this.pageResult.records = responseData.data.queryResult;
          this.$message.success(responseData.msg)
        } else {
          this.pageResult.records = [];
          this.$message.error(responseData.msg + "," + responseData.data);
        }
        this.queryLoading = false;
      }).then(data != null ? data.callback : '')
    },

    handleOpenExcelUpload: function () {
      this.excelUploadDialogVisible = true;
      this.progressPercentage = 0;
      this.progressContent = "";
      this.pregressDuration = 6;
    },

    submitExcelUpload(params) {
      this.progressPercentage = 50;
      this.progressContent = "Excel导入中，请稍等...";
      this.progressStatus = "";
      this.pregressDuration = 6;

      uploadExcel(params).then((response) => {
        const responseData = response.data

        this.progressPercentage = 100;
        this.pregressDuration = 0;
        if (responseData.code === '000000') {
          this.$message.success('上传成功！')

          this.progressContent = "导入成功";
          this.progressStatus = "success"
        } else {
          this.$message.error('上传失败！' + responseData.msg + "," + responseData.data)

          this.progressContent = "导入失败";
          this.progressStatus = "exception";
        }
      }).catch((err) => {
        this.$message.error(err)
      })
    },
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel'
      if (!isExcel) {
        this.$message.error('只能上传xlsx, xls格式的文件！')
        return false
      }
    },
    cancelExcelUpload() {
      this.excelUploadDialogVisible = false;
    },
    downloadTemplate(type) {
      this.downloadTemplateLoading = true;
      let params = {};
      params.type = type;
      downloadTemplate(params).then(res => {

        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', '生产汇总模板' + "-" + new Date().getTime() + ".xlsx");
        document.body.appendChild(link);
        link.click();

        this.downloadTemplateLoading = false;
      });
    },
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD')
    },
    getCurrentMonthFirst() {
      var date = new Date()
      date.setDate(1)
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10) month = '0' + month
      if (day < 10) day = '0' + day
      this.filters.productionDateStart = date.getFullYear() + '-' + month + '-' + day
    },
    getCurrentMonthLast() {
      var date = new Date()
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10) month = '0' + month
      if (day < 10) day = '0' + day
      this.filters.productionDateEnd = date.getFullYear() + '-' + month + '-' + day
    },

  },
  mounted() {
    this.getCurrentMonthFirst();
    this.getCurrentMonthLast();
  }
}
</script>
