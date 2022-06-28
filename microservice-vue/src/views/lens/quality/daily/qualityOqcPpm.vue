<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="厂区" prop="site">
                <el-input v-model="filters.site" clearable placeholder="厂区"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="项目" prop="project">
                <el-input v-model="filters.project" clearable placeholder="项目"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="开始时间" prop="startOqcTime">
                <el-date-picker v-model="filters.startOqcTime" auto-complete="off" type="datetime"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="终止时间" prop="endOqcTime">
                <el-date-picker v-model="filters.endOqcTime" auto-complete="off" type="datetime"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="数据类型" prop="oqcType">
                <el-input v-model="filters.oqcType" clearable placeholder="数据类型"></el-input>
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
            <el-button :loading="exportLoading" type="primary" @click="exportExcelData('质量OqcPpm模板')">导出模板
              <template #icon>
                <font-awesome-icon :icon="['fas', 'download']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="findPage()">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item class="float-right">
            <el-button :loading="exportReportLoading" type="success" @click="exportReportExcelData('OqcPpm报表')">导出报表
              <template #icon>
                <font-awesome-icon :icon="['fas', 'download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="dataList">
        <!-- 动态生成列 -->
        <el-table-column
            v-for="(item,index) in theadList"
            :key="index"
            :label="item"
            prop="colprops"
            align="center"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.colprops | transToValue(index) }}</div>
          </template>
        </el-table-column>
        <!-- 动态生成列结束 -->
      </el-table>

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
import {getResponseDataMessage} from "@/utils/commonUtils";
import {exportExcel, listHeaders, uploadExcel} from "@/api/lens/quality/qualityOqcPpm";

export default {
  name: "qualityOqcPpm",
  components: {SysTable},
  filters: {
    transToValue(val, index) {
      return val[index]
    }
  },
  data() {
    return {
      size: "default",
      filters: {
        site: "",
        project: "",
        startOqcTime: "",
        endOqcTime: "",
        oqcType: "",
      },
      theadList: [],
      dataList: [],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      excelUploadDialogVisible: false,
      exportLoading: false,
      exportReportLoading: false
    };
  },
  methods: {
    // 获取分页数据
    findPage: function () {
      this.pageRequest.site = this.filters.site;
      this.pageRequest.project = this.filters.project;
      this.pageRequest.startOqcTime = this.filters.startOqcTime;
      this.pageRequest.endOqcTime = this.filters.endOqcTime;
      this.pageRequest.oqcType = this.filters.oqcType;

      listHeaders(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.theadList = responseData.data;
            }
          })
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
          alert('上传成功！' + getResponseDataMessage(responseData, '\n'))
        } else {
          alert('上传失败！' + getResponseDataMessage(responseData, '\n'))
        }
        this.findPage(null);
        this.excelUploadDialogVisible = false;
      }).catch((err) => {
        alert(err)
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
      this.pageRequest.milType = this.filters.milType;
      this.pageRequest.site = this.filters.site;
      this.pageRequest.project = this.filters.project;
      this.pageRequest.startEventHappenDate = this.filters.startEventHappenDate;
      this.pageRequest.endEventHappenDate = this.filters.endEventHappenDate;
      this.pageRequest.riskType = this.filters.riskType;
      this.pageRequest.severityLevel = this.filters.severityLevel;
      this.pageRequest.status = this.filters.status;
      this.pageRequest.responsibilities = this.filters.responsibilities;

      this.exportReportLoading = true;
      listQualityMilExportExcel(this.pageRequest).then(res => {
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
    // 取消
    cancel() {
      this.dialogVisible = false;
    },
    // 时间格式化
    dateFormat: function (row, column) {
      if (row[column.property] == null) return '-';
      return this.$moment(row[column.property]).format("YYYY-MM-DD");
    },
    dateTimeFormat: function (row, column) {
      if (row[column.property] == null) return '-';
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
  },
};
</script>
