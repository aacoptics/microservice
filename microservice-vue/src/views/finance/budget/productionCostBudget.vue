<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item label="文件名称" prop="excelName">
            <el-input v-model="filters.excelName" clearable placeholder="文件名称"></el-input>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button type="primary" @click="findPage(null)" :loading="queryLoading">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>

            <el-button type="info"
                       @click="handleOpenExcelUpload">Excel上传
              <template #icon>
                <font-awesome-icon :icon="['fas','file-lines']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <BudgetLogTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false" 
                @findPage="findPage" @handleDelete="handleDelete" @handlePreview="handlePreview">
      </BudgetLogTable>


      <el-dialog v-model="excelUploadDialogVisible" :close-on-click-modal="false" :title="'生产成本预算Excel上传'"
                 width="400px">
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
                style="width:350px"
            >
              <span>{{ progressContent }}</span>
            </el-progress>
            <div style="padding-top: 20px;">
              <el-button :loading="downloadTemplateLoading" :size="size" style="position: absolute;left: 20px;"
                         type="primary"
                         @click="downloadTemplate">模板下载
              </el-button>
              <el-button :size="size" type="success" @click="cancelExcelUpload">关闭</el-button>
            </div>
          </slot>
        </div>
      </el-dialog>

      <el-dialog v-model="previewDialogVisible" :close-on-click-modal="false" title="生产成本预算预览"
                 width="75%">
          <div>
            <QueryAllTable id="condDataTable" ref="queryAllTable" :columns="productionCostBudgetColumns" :data="productionCostBudgetDataResult"
                     :height="550" :highlightCurrentRow="true"  v-loading="previewLoading" border
                     :stripe="true">
            </QueryAllTable>

          </div>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" @click="cancelPreview" type="success">取消</el-button>
          </slot>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import BudgetLogTable from "@/views/finance/budget/budgetLogTable";
import QueryAllTable from "@/components/QueryAllTable";

import {findProductionCostBudgetPage, downloadTemplate, uploadExcel} from "@/api/finance/budget/productionCostBudget";
import {deleteBudgetUploadLog, findBudgetUploadLogPage,} from "@/api/finance/budget/budgetUploadLog";
import {getResponseDataMessage} from "@/utils/commonUtils";


export default {
  name: "productionCostBudget",
  components: {BudgetLogTable, QueryAllTable},
  data() {
    return {
      size: "default",
      filters: {
        excelName: "",
      },
      columns: [
        {prop: "excelName", label: "文件名称", minWidth: 110},
        {prop: "uploadTime", label: "上传时间", minWidth: 100, formatter: this.dateTimeFormat},
        {prop: "uploadUser", label: "上传用户", minWidth: 100},
        {prop: "status", label: "状态", minWidth: 100, formatter: this.statusFormat},
        // {prop: "updatedBy", label: "更新人", minWidth: 100},
        // {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        // {prop: "createdBy", label: "创建人", minWidth: 120},
        // {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],

      productionCostBudgetColumns: [
        // {prop: "seq", label: "序号", minWidth: 80},
        {prop: "businessDivision", label: "事业部", minWidth: 150},
        {prop: "productLine", label: "产品线", minWidth: 120},
        {prop: "dataVersion", label: "数据版本", minWidth: 150},
        {prop: "itemSeq", label: "科目序号", minWidth: 120},
        {prop: "category1", label: "分类1", minWidth: 120},
        {prop: "category2", label: "分类2", minWidth: 260},
        {prop: "category3", label: "分类3", minWidth: 150},
        {prop: "unit", label: "单位", minWidth: 120},
        {prop: "validation", label: "校验", minWidth: 120},
        {prop: "202201", label: "202201", minWidth: 120},
        {prop: "202202", label: "202202", minWidth: 120},
        {prop: "202203", label: "202203", minWidth: 120},
        {prop: "202204", label: "202204", minWidth: 120},
        {prop: "202205", label: "202205", minWidth: 120},
        {prop: "202206", label: "202206", minWidth: 120},
        {prop: "202207", label: "202207", minWidth: 120},
        {prop: "202208", label: "202208", minWidth: 120},
        {prop: "202209", label: "202209", minWidth: 120},
        {prop: "202210", label: "202210", minWidth: 120},
        {prop: "202211", label: "202211", minWidth: 120},
        {prop: "202212", label: "202212", minWidth: 120},
        {prop: "ytd", label: "YTD", minWidth: 120},
        {prop: "ytg", label: "YTG", minWidth: 120},
        {prop: "2022小计", label: "2022小计", minWidth: 120},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      productionCostBudgetDataResult: {},

      downloadTemplateLoading: false,
      previewLoading: false,
      queryLoading:false,

      excelUploadDialogVisible: false,
      previewDialogVisible: false,

      progressPercentage: 0,
      progressContent: "",
      pregressDuration: 6,
      progressStatus: "",
    };
  },
  mounted() {
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.type = '生产成本预算';
      this.pageRequest.excelName = this.filters.excelName;
      this.queryLoading = true;
      findBudgetUploadLogPage(this.pageRequest)
          .then((res) => {
            this.queryLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },

    findDetail: function (uploadLogRow) {
      if (uploadLogRow == null) {
        return;
      }
      let params = {};
      params.uploadLogId = uploadLogRow.id;
      this.productionCostBudgetDataResult = {};
      this.previewLoading = true;
      findProductionCostBudgetPage(params)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              for(var i=0; i<responseData.data.columns.length; i++)
              {
                responseData.data.columns[i].formatter = this.percentFormat;
              }
              this.productionCostBudgetColumns = responseData.data.columns;
              this.productionCostBudgetDataResult.records = responseData.data.data;
            }
            else
            {
              this.$message({
                    message:
                        "预览失败 " + getResponseDataMessage(responseData),
                    type: "error",
                  });
            }
            this.previewLoading = false;
          });
    },
    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteBudgetUploadLog(data.params[0]).then(data.callback);
    },
    handleOpenExcelUpload: function () {
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
    downloadTemplate() {
      this.downloadTemplateLoading = true;
      downloadTemplate().then(res => {

        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', '2023年生产成本预算模版_xx事业部_20221014 v4.xlsx');
        document.body.appendChild(link);
        link.click();

        this.downloadTemplateLoading = false;
      });
    },
    cancelExcelUpload() {
      this.excelUploadDialogVisible = false;
    },

    // 取消
    cancel() {
      this.dialogVisible = false;
    },
    // 显示编辑界面
    handlePreview: function (params) {
      if(params.row.status == 0)
      {
        this.$message({
                    message:
                        "已失效，不能预览！ ",
                    type: "error",
                  });
        return;
      }
      this.previewDialogVisible = true;
      this.findDetail(params.row);
    },
    cancelPreview()
    {
      this.previewDialogVisible = false;
    },
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm:ss");
    },
    // 百分数格式化
    percentFormat: function (row, column) {
      if(column.no >= 9)
      {
        if(row[column.property] != undefined && row[column.property] != null)
        {
          if(row.unit == '%')
          {
            return Number(row[column.property]*100).toFixed(2)+'%';
          }
          else
          {
            return Math.round(row[column.property]);
          }
        }
      }
      return row[column.property];
    },
    statusFormat: function (row, column) {
      if(row[column.property] == 1)
      {
        return '生效中';
      }
      else if(row[column.property] == 0)
      {
        return '已失效';
      }
    },
  },
};
</script>
