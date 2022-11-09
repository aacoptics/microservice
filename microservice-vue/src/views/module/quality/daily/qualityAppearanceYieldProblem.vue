<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :label-width="100" :size="size">
          <el-row>
            <el-col :span="6">
              <el-form-item label="工段或项目" prop="workshopProject">
                <el-input v-model="filters.workshopProject" clearable placeholder="工段或项目"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="开始时间" prop="startAppearanceDate">
                <el-date-picker v-model="filters.startAppearanceDate" auto-complete="off"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="终止时间" prop="endAppearanceDate">
                <el-date-picker v-model="filters.endAppearanceDate" auto-complete="off"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="PD" prop="projectPd">
                <el-input v-model="filters.projectPd" clearable placeholder="PD"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :label-width="100" :size="size">
          <el-row>
            <el-col :span="6">
              <el-form-item label="QC" prop="projectQc">
                <el-input v-model="filters.projectQc" clearable placeholder="QC"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="SOP" prop="projectSop">
                <el-input v-model="filters.projectSop" clearable placeholder="SOP"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="设备" prop="projectEquipment">
                <el-input v-model="filters.projectEquipment" clearable placeholder="设备"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="生产经理" prop="projectManager">
                <el-select v-model="filters.projectManager" clearable filterable placeholder="生产经理">
                  <el-option
                      v-for="item in userOptions"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :label-width="100" :size="size">
          <el-form-item>
            <el-button :loading="exportLoading" type="primary" @click="exportExcelData('质量良率问题模板')">导出模板
              <template #icon>
                <font-awesome-icon :icon="['fas', 'download']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="handleAdd">新增
              <template #icon>
                <font-awesome-icon :icon="['fas', 'plus']"/>
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
              <el-button :loading="exportReportLoading" type="success" @click="exportReportExcelData('良率问题报表')">
                导出报表
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'download']"/>
                </template>
              </el-button>
            </el-form-item>
          </el-row>
        </el-form>
      </div>
      <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :show-batch-delete="false" :show-operation="true"
                :stripe="false" @findPage="findPage" @handleDelete="handleDelete" @handleEdit="handleEdit">
      </SysTable>

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="90%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="8">
              <el-form-item label="日期" prop="appearanceDate">
                <el-date-picker v-model="dataForm.appearanceDate" :disabled="!operation" auto-complete="off"
                                type="datetime">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="工段或项目" prop="workshopProject">
                <el-input v-model="dataForm.workshopProject" :disabled="!operation" auto-complete="off"
                          clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外观良率" prop="appearanceYield">
                <el-input v-model="dataForm.appearanceYield" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="PD" prop="projectPd">
                <el-input v-model="dataForm.projectPd" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="QC" prop="projectQc">
                <el-input v-model="dataForm.projectQc" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="S&OP生产" prop="projectSop">
                <el-input v-model="dataForm.projectSop" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="设备" prop="projectEquipment">
                <el-input v-model="dataForm.projectEquipment" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="生产经理" prop="projectManager">
                <el-select v-model="dataForm.projectManager" clearable filterable placeholder="生产经理">
                  <el-option
                      v-for="item in userOptions"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="问题描述" prop="questionDescription">
                <el-input v-model="dataForm.questionDescription" :disabled="!operation" auto-complete="off" autosize
                          clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="原因分析" prop="reasonAnalysis">
                <el-input v-model="dataForm.reasonAnalysis" auto-complete="off" autosize
                          clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="措施" prop="correctiveAction">
                <el-input v-model="dataForm.correctiveAction" auto-complete="off" autosize
                          clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="备注" prop="remark">
                <el-input v-model="dataForm.remark" auto-complete="off" autosize
                          clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="计划完成时间" prop="planFinishDate">
                <el-date-picker v-model="dataForm.planFinishDate" auto-complete="off" type="datetime">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="实际完成时间" prop="actualFinishDate">
                <el-date-picker v-model="dataForm.actualFinishDate" auto-complete="off" type="datetime">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" @click="cancel">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitForm">提交</el-button>
          </slot>
        </div>
      </el-dialog>

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
  findQualityAppearanceYieldProblemPage,
  handleAdd,
  handleDelete,
  handleUpdate,
  listExportExcel,
  uploadExcel
} from "@/api/module/quality/daily/qualityAppearanceYieldProblem";
import {listAllUser} from "@/api/module/quality/daily/qualityMil";
import {ElMessageBox} from "element-plus";

export default {
  name: "qualityAppearanceYieldProblem",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        workshopProject: "",
        startAppearanceDate: null,
        endAppearanceDate: null,
        projectPd: "",
        projectQc: "",
        projectSop: "",
        projectEquipment: "",
        projectManager: "",
      },
      columns: [
        {prop: "appearanceDate", label: "日期", minWidth: 100, formatter: this.dateFormat},
        {prop: "workshopProject", label: "工段或项目", minWidth: 150},
        {prop: "appearanceYield", label: "外观良率", minWidth: 150},
        {prop: "projectPd", label: "PD", minWidth: 100},
        {prop: "projectQc", label: "QC", minWidth: 100},
        {prop: "projectSop", label: "S&OP生产", minWidth: 120},
        {prop: "projectEquipment", label: "设备", minWidth: 100},
        {prop: "projectManager", label: "生产经理", minWidth: 120},
        {prop: "questionDescription", label: "问题描述", minWidth: 120},
        {prop: "reasonAnalysis", label: "原因分析", minWidth: 120},
        {prop: "correctiveAction", label: "措施", minWidth: 120},
        {prop: "planFinishDate", label: "计划完成时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "actualFinishDate", label: "实际完成时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "remark", label: "备注", minWidth: 120},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      excelUploadDialogVisible: false,
      exportLoading: false,
      exportReportLoading: false,
      dataFormRules: {
        appearanceDate: [{required: true, message: '请输入日期', trigger: 'blur'}],
        workshopProject: [{required: true, message: '请输入工段或项目', trigger: 'blur'}],
        questionDescription: [{required: true, message: '请输入问题描述', trigger: 'blur'}],
      },
      userOptions: [],
      // 新增编辑界面数据
      dataForm: {
        id: "0",
        appearanceDate: "",
        workshopProject: "",
        appearanceYield: "",
        projectPd: "",
        projectQc: "",
        projectSop: "",
        projectEquipment: "",
        projectManager: "",
        questionDescription: "",
        reasonAnalysis: "",
        correctiveAction: "",
        planFinishDate: "",
        actualFinishDate: "",
        remark: "",
      },
    };
  },
  mounted() {
    listAllUser().then(response => {
      this.userOptions = response.data.data
    })
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.workshopProject = this.filters.workshopProject;
      this.pageRequest.startAppearanceDate = this.filters.startAppearanceDate != null ? date2str(this.filters.startAppearanceDate) + "T00:00:00" : null;
      this.pageRequest.endAppearanceDate = this.filters.endAppearanceDate != null ? date2str(this.filters.endAppearanceDate) + "T00:00:00" : null;
      this.pageRequest.projectPd = this.filters.projectPd;
      this.pageRequest.projectQc = this.filters.projectQc;
      this.pageRequest.projectSop = this.filters.projectSop;
      this.pageRequest.projectEquipment = this.filters.projectEquipment;
      this.pageRequest.projectManager = this.filters.projectManager;

      findQualityAppearanceYieldProblemPage(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },

    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        handleDelete(data.params[0]).then(data.callback)
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.operation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: null,
        appearanceDate: date2str(new Date()) + "T00:00:00",
        workshopProject: "",
        appearanceYield: "",
        projectPd: "",
        projectQc: "",
        projectSop: "",
        projectEquipment: "",
        projectManager: "",
        questionDescription: "",
        reasonAnalysis: "",
        correctiveAction: "",
        planFinishDate: "",
        actualFinishDate: "",
        remark: "",
      };
    },

    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.operation = false;
      this.dataForm = Object.assign({}, params.row);
    },
    // 编辑
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.operation) {
              handleAdd(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.dialogVisible = false;
                  this.$refs["dataForm"].resetFields();
                } else {
                  this.$message({message: "操作失败 " + getResponseDataMessage(responseData), type: "error",});
                }
                this.findPage(null);
              });
            } else {
              handleUpdate(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.dialogVisible = false;
                  this.$refs["dataForm"].resetFields();
                } else {
                  this.$message({message: "操作失败" + getResponseDataMessage(responseData), type: "error",});
                }
                this.findPage(null);
              })
            }
          })
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
      this.pageRequest.workshopProject = this.filters.workshopProject;
      this.pageRequest.startAppearanceDate = this.filters.startAppearanceDate != null ? date2str(this.filters.startAppearanceDate) + "T00:00:00" : null;
      this.pageRequest.endAppearanceDate = this.filters.endAppearanceDate != null ? date2str(this.filters.endAppearanceDate) + "T00:00:00" : null;
      this.pageRequest.projectPd = this.filters.projectPd;
      this.pageRequest.projectQc = this.filters.projectQc;
      this.pageRequest.projectSop = this.filters.projectSop;
      this.pageRequest.projectEquipment = this.filters.projectEquipment;
      this.pageRequest.projectManager = this.filters.projectManager;

      this.exportReportLoading = true;
      listExportExcel(this.pageRequest).then(res => {
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
    }
  },
};
</script>
