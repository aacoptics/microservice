<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-row>
            <el-form-item label="项目" prop="projectName">
              <el-input
                  v-model="filters.projectName"
                  placeholder="请输入项目"
                  clearable
                  :size="size"
              />
            </el-form-item>

          <el-form-item label="月份 从" prop="requirementDateStart">
            <el-date-picker v-model="filters.requirementDateStart" type="month" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
          </el-form-item>
          <el-form-item label="到" prop="requirementDateEnd">
            <el-date-picker v-model="filters.requirementDateEnd" type="month" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
          </el-form-item>
          </el-row>
        </el-form>
          <el-form :inline="true" :size="size">
            <el-form-item>
              <el-button type="primary"
                :loading="queryLoading"
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
      <QueryTable id="condDataTable" :height="550" :highlightCurrentRow="true" :stripe="true"
                :data="pageResult" :columns="columns"
                ref="queryTable"
                @findPage="findPage" >
      </QueryTable>

      <el-dialog :title="'客户需求Excel导入'" width="400px" v-model="excelUploadDialogVisible"
                 :close-on-click-modal="false">
          <el-upload
              class="upload-demo"
              :before-upload="beforeUpload"
              accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
              :http-request="submitExcelUpload"
              action=""
              :multiple="false"
              :show-file-list="false"
              drag>
            <font-awesome-icon class="el-icon--upload" :icon="['fas','cloud-arrow-up']"/>
            <div class="el-upload__text">将Excel文件拖到此处，或<em>点击上传</em></div>
          </el-upload>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-progress
            style="width:350px"
              :percentage="progressPercentage"
              :text-inside="true"
              :indeterminate="true"
              :stroke-width="20"
              :duration="pregressDuration"
              :status="progressStatus"
            >
              <span>{{progressContent}}</span>
            </el-progress>
            <div style="padding-top: 20px;">
            <el-button  type="primary" :size="size"  @click="downloadTemplate" style="position: absolute;left: 20px;" :loading="downloadTemplateLoading">模板下载</el-button>
            <el-button type="success" :size="size"  @click="cancelExcelUpload" right>关闭</el-button>
            </div>
          </slot>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>

import QueryTable from "@/components/QueryTable";
import {uploadExcel, findCustomerRequirementPage, downloadTemplate} from "@/api/wlg/report/customerRequirement";

export default {
  name: "customerRequirement",
  components: {QueryTable},
  data() {
    return {
      size: 'default',
      queryLoading: false,
      downloadTemplateLoading:false,

      progressPercentage: 0,
      progressContent:"",
      pregressDuration: 6,
      progressStatus: "",

      filters: {
        projectName: '',
        requirementDateStart:'',
        requirementDateEnd:'',
      },
      columns: [
        {prop: "__row_number__", label: "序号", minWidth: 80},
        {prop: "projectName", label: "项目", minWidth: 120},
        {prop: "requirementDate", label: "需求月份", minWidth: 120, formatter:this.dateFormat},
        {prop: "qty", label: "需求数量", minWidth: 100},
        {prop: "targetYield", label: "目标总产量", minWidth: 120},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      excelUploadDialogVisible: false,
    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.projectName = this.filters.projectName;
      this.pageRequest.requirementDateStart = this.filters.requirementDateStart;
      this.pageRequest.requirementDateEnd = this.filters.requirementDateEnd;

      this.queryLoading = true;
      findCustomerRequirementPage(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
          this.$message.success(responseData.msg)
        } else
        {
          this.pageResult = [];
          this.$message.error(responseData.msg + "," + responseData.data);
        }
        this.queryLoading = false;
      }).then(data != null ? data.callback : '')
    },

    handleOpenExcelUpload:function()
    {
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
    cancelExcelUpload()
    {
      this.excelUploadDialogVisible = false;
    },
    downloadTemplate()
    {
      this.downloadTemplateLoading = true;
      downloadTemplate().then(res => {

          let url = window.URL.createObjectURL(new Blob([res.data],{type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
          let link = document.createElement('a');
          link.style.display = 'none';
          link.href = url;
          link.setAttribute('download', '客户需求模板' + "-" + new Date().getTime() + ".xlsx");
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
      return this.$moment(row[column.property]).format('YYYY-MM')
    },
    getCurrentMonthFirst () {
      var date = new Date()
      date.setDate(1)
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10)  month = '0' + month
      if (day < 10)  day = '0' + day
      this.filters.requirementDateStart = date.getFullYear() + '-' + month + '-' + day
    },
    getCurrentMonthLast () {
      var date = new Date()
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10)  month = '0' + month
      if (day < 10)  day = '0' + day
      this.filters.requirementDateEnd = date.getFullYear() + '-' + month + '-' + day
    },

  },
  mounted()
  {
    this.getCurrentMonthFirst();
    this.getCurrentMonthLast();
  }
}
</script>
