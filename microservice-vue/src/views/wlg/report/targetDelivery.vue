<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="项目" prop="projectName">
            <el-input
                v-model="filters.projectName"
                placeholder="请输入项目"
                clearable
                :size="size"
            />
          </el-form-item>
              <el-form-item label="日期 从" prop="deliveryDate">
            <el-date-picker v-model="filters.deliveryDateStart" type="date" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
          </el-form-item>
          <el-form-item label="到" prop="deliveryDate">
            <el-date-picker v-model="filters.deliveryDateEnd" type="date" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
          </el-form-item>

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
      <QueryAllTable id="condDataTable" :height="550" :highlightCurrentRow="true" :stripe="true"
                :data="pageResult" :columns="columns"
                ref="queryTable"
                @findPage="findPage" >
      </QueryAllTable>

      <el-dialog :title="'目标交货Excel导入'" width="400px" v-model="excelUploadDialogVisible"

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
              <el-button type="success" :size="size"  @click="cancelExcelUpload">关闭</el-button>
            </div>
          </slot>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>

import QueryAllTable from "@/components/QueryAllTable";
import {uploadExcel, findTargetDeliveryPage, queryTargetDeliveryTitleByMonth, downloadTemplate} from "@/api/wlg/report/targetDelivery";

export default {
  name: "targetDelivery",
  components: {QueryAllTable},
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
        deliveryDateStart:'',
        deliveryDateEnd:'',
      },
      columns: [
        {prop: "itemNumber", label: "物料号", minWidth: 110},
        {prop: "itemDescription", label: "物料描述", minWidth: 150},
        {prop: "projectName", label: "项目", minWidth: 120},
        {prop: "deliveryDate", label: "日期", minWidth: 120},
        {prop: "deliveryQty", label: "数量", minWidth: 120},
      ],
      pageRequest: {},
      pageResult: {},

      excelUploadDialogVisible: false,
    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {

      this.pageRequest.projectName = this.filters.projectName;
      this.pageRequest.deliveryDateStart = this.filters.deliveryDateStart;
      this.pageRequest.deliveryDateEnd = this.filters.deliveryDateEnd;

      this.queryLoading = true;
      queryTargetDeliveryTitleByMonth(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.columns = responseData.data
        }
      }).then(data != null ? data.callback : '')

      findTargetDeliveryPage(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult.records = responseData.data
          this.$message.success(responseData.msg)
        }
        else
        {
          this.pageResult.records = [];
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
      this.progressStatus = "";
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
    downloadTemplate()
    {
      this.downloadTemplateLoading = true;
      downloadTemplate().then(res => {

          let url = window.URL.createObjectURL(new Blob([res.data],{type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
          let link = document.createElement('a');
          link.style.display = 'none';
          link.href = url;
          link.setAttribute('download', '目标交货模板' + "-" + new Date().getTime() + ".xlsx");
          document.body.appendChild(link);
          link.click();

          this.downloadTemplateLoading = false;
      });
    },
    cancelExcelUpload()
    {
      this.excelUploadDialogVisible = false;
    },
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD')
    },
    getCurrentMonthFirst () {
      var date = new Date()
      date.setDate(1)
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10)  month = '0' + month
      if (day < 10)  day = '0' + day
      this.filters.deliveryDateStart = date.getFullYear() + '-' + month + '-' + day
    },
    getCurrentMonthLast () {
      var date = new Date()
      var month = parseInt(date.getMonth() + 1)
      var day = date.getDate()
      if (month < 10)  month = '0' + month
      if (day < 10)  day = '0' + day
      this.filters.deliveryDateEnd = date.getFullYear() + '-' + month + '-' + day
    },

  },
  mounted()
  {
    this.getCurrentMonthFirst();
    this.getCurrentMonthLast();
  }
}
</script>
