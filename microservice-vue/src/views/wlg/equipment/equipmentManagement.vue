<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="资产编码" prop="mchCode">
            <el-input v-model="filters.mchCode" clearable placeholder="资产编码"></el-input>
          </el-form-item>
          <el-form-item label="资产名称" prop="mchName">
            <el-input v-model="filters.mchName" clearable placeholder="资产名称"></el-input>
          </el-form-item>
          <el-form-item label="规格" prop="spec">
            <el-input v-model="filters.spec" clearable placeholder="规格"></el-input>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="型号" prop="typeVersion">
            <el-input v-model="filters.typeVersion" clearable placeholder="型号"></el-input>
          </el-form-item>
          <el-form-item label="出厂编码" prop="factoryNo">
            <el-input v-model="filters.factoryNo" clearable placeholder="出厂编码"></el-input>
          </el-form-item>
          <el-form-item label="位置编码" prop="locationNo">
            <el-input v-model="filters.locationNo" clearable placeholder="位置编码"></el-input>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="资产管理员" prop="assetManagerId">
            <el-input v-model="filters.assetManagerId" clearable placeholder="资产管理员"></el-input>
          </el-form-item>
          <el-form-item label="设备管理员" prop="mchManagerId">
            <el-input v-model="filters.mchManagerId" clearable placeholder="设备管理员"></el-input>
          </el-form-item>
          <!-- <el-form-item label="责任人" prop="dutyPersonId">
            <el-input v-model="filters.dutyPersonId" clearable placeholder="责任人"></el-input>
          </el-form-item> -->
          <el-form-item label="设备状态" prop="status" label-width="100px">
                <el-select v-model="filters.status" clearable filterable placeholder="设备状态"
                           style="width:90%">
                  <el-option
                      v-for="item in equipmentStatusOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="设备编号" prop="equipNumber">
            <el-input v-model="filters.equipNumber" clearable placeholder="设备编号"></el-input>
          </el-form-item>
          <el-form-item label="设备属性" prop="equipCategory">
            <el-select v-model="filters.equipCategory" clearable filterable placeholder="设备属性" style="width:90%">
              <el-option
                  v-for="item in equipCategoryOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
              >
              </el-option>
            </el-select>
          </el-form-item>
            <el-form-item label="工段类型" prop="sectionType" label-width="80px">
                <el-select v-model="filters.sectionType" clearable filterable placeholder="工段类型"
                           style="width:90%">
                  <el-option
                      v-for="item in sectionTypeOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item>
            <el-button :loading="queryLoading" type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
            <el-button type="info"
                       @click="handleOpenExcelUpload">Excel导入
              <template #icon>
                <font-awesome-icon :icon="['fas','file-lines']"/>
              </template>
            </el-button>
            <el-button :loading="exportLoading" :size="size" type="success"
                       @click="exportExcelData('设备清单')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <SysTable id="condDataTable" ref="sysTable" :cell-style="{'text-align':'left'}" :columns="columns"
                :data="pageResult" :header-cell-style="{'text-align':'center'}" :height="400"
                :highlightCurrentRow="true" :show-operation="true" :showBatchDelete="false"
                :showOperationDel="false" :stripe="true" border @findPage="findPage"
                @handleEdit="handleEdit">
      </SysTable>


      <el-dialog v-model="excelUploadDialogVisible" :close-on-click-modal="false" :title="'设备清单Excel导入'"

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

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="40%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="资产编码" prop="mchCode">
                <el-input v-model="dataForm.mchCode" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="资产名称" prop="mchName">
                <el-input v-model="dataForm.mchName" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="规格" prop="spec">
                <el-input v-model="dataForm.spec" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="型号" prop="typeVersion">
                <el-input v-model="dataForm.typeVersion" :disabled="!operation" auto-complete="off"
                          clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备编号" prop="equipNumber">
                <el-input v-model="dataForm.equipNumber" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备负责人" prop="equipDuty">
                <el-select v-model="dataForm.equipDuty" clearable filterable placeholder="设备负责人"
                           style="width:100%">
                  <el-option
                      v-for="item in userOptions"
                      :key="item.username"
                      :label="item.username + '（' + item.name + '）'"
                      :value="item.username"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备负责人经理" prop="equipDutyManager">
                <el-select v-model="dataForm.equipDutyManager" clearable filterable placeholder="设备负责人经理"
                           style="width:100%">
                  <el-option
                      v-for="item in userOptions"
                      :key="item.username"
                      :label="item.username + '（' + item.name + '）'"
                      :value="item.username"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备属性" prop="equipCategory">
                <el-select v-model="dataForm.equipCategory" clearable filterable placeholder="设备属性"
                           style="width:100%">
                  <el-option
                      v-for="item in equipCategoryOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备状态" prop="status">
                <el-select v-model="dataForm.status" clearable filterable placeholder="设备状态"
                           style="width:100%">
                  <el-option
                      v-for="item in equipmentStatusOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="工段类型" prop="sectionType">
                <el-select v-model="dataForm.sectionType" clearable filterable placeholder="工段类型"
                           style="width:100%">
                  <el-option
                      v-for="item in sectionTypeOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
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
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";

import {getAllUser} from "@/api/system/user"
import {
  convertUser,
  downloadTemplate,
  exportEquipmentExcel,
  findEquipmentManagementPage,
  handleAdd,
  handleUpdate,
  uploadExcel
} from "@/api/wlg/equipment/equipmentManagement";
import {getDict, selectDictLabel} from "@/api/system/dictData";

export default {
  name: "equipmentManagement",
  components: {SysTable},
  data() {
    return {
      size: "default",

      exportLoading: false,
      editLoading: false,
      queryLoading: false,

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示

      filters: {
        mchCode: "",
        mchName: "",
        spec: "",
        typeVersion: "",
        factoryNo: "",
        locationNo: "",
        assetManagerId: "",
        mchManagerId: "",
        dutyPersonId: "",
        equipNumber: "",
        equipCategory: "",
        status: "",
        sectionType: "",
      },
      dataForm: {
        id: 0,
        mchCode: "",
        mchName: "",
        spec: "",
        equipNumber: "",
        equipDuty: "",
        equipDutyManager: "",
        equipCategory: "",
        status: "",
        sectionType: ""
      },

      equipmentStatusOptions: [],
      userOptions: [],
      equipCategoryOptions: [],
      sectionTypeOptions: [],

      progressPercentage: 0,
      progressContent: "",
      pregressDuration: 6,
      progressStatus: "",
      excelUploadDialogVisible: false,
      downloadTemplateLoading: false,

      columns: [
        {prop: "mchCode", label: "资产编码", minWidth: 110},
        {prop: "mchName", label: "资产名称", minWidth: 120},
        {prop: "spec", label: "规格", minWidth: 120},
        {prop: "typeVersion", label: "型号", minWidth: 150},
        {prop: "equipNumber", label: "设备编号", minWidth: 150},
        {prop: "equipDuty", label: "设备负责人", minWidth: 150, formatter: this.userFormat},
        {prop: "equipDutyManager", label: "设备负责人经理", minWidth: 150, formatter: this.userFormat},
        {prop: "equipCategory", label: "设备属性", minWidth: 120,},
        {prop: "status", label: "状态", minWidth: 100, formatter: this.statusFormat},
        {prop: "sectionType", label: "工段类型", minWidth: 100, formatter: this.sectionTypeFormat},
        {prop: "equipStateDb", label: "资产状态编码", minWidth: 120},
        {prop: "equipState", label: "资产状态", minWidth: 100},
        {prop: "assetGeneralCode", label: "资产使用性质编码", minWidth: 140},
        {prop: "assetGeneralDesc", label: "资产使用性质名称", minWidth: 140},
        {prop: "assetClassifyCode", label: "资产分类别编码", minWidth: 140},
        {prop: "assetClassifyDesc", label: "资产分类别名称", minWidth: 140},
        {prop: "majorBigCode", label: "专业大类编码", minWidth: 120},
        {prop: "majorBigDesc", label: "专业大类名称", minWidth: 220},
        {prop: "majorSmallCode", label: "专业小类编码", minWidth: 120},
        {prop: "majorSmallDesc", label: "专业小类名称", minWidth: 120},
        {prop: "factoryNo", label: "出厂编码", minWidth: 150},
        {prop: "locationNo", label: "位置编码", minWidth: 100},
        {prop: "areaCode", label: "地区编码", minWidth: 100},
        {prop: "areaName", label: "地区名称", minWidth: 100},
        {prop: "factoryId", label: "厂区编码", minWidth: 100},
        {prop: "factoryName", label: "厂区名称", minWidth: 100},
        {prop: "buildingId", label: "楼栋编码", minWidth: 100},
        {prop: "buildingName", label: "楼栋名称", minWidth: 100},
        {prop: "floorCode", label: "楼层编码", minWidth: 100},
        {prop: "floorName", label: "楼层名称", minWidth: 100},
        {prop: "assetManagerId", label: "资产管理员", minWidth: 150, formatter: this.userFormat},
        {prop: "mchManagerId", label: "设备管理员", minWidth: 150, formatter: this.userFormat},
        {prop: "dutyPersonId", label: "责任人", minWidth: 150, formatter: this.userFormat},
        {prop: "deptManagerId", label: "部门经理", minWidth: 150, formatter: this.userFormat},
        {prop: "deptDirectorId", label: "部门总监", minWidth: 150, formatter: this.userFormat},
        {prop: "vicePresidentId", label: "部门VP", minWidth: 150, formatter: this.userFormat},
        {prop: "lastInspectionDatetime", label: "最后点检日期", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "lastMaintenanceDatetime", label: "最后保养日期", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        // {prop: "createdBy", label: "创建人", minWidth: 120},
        // {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

    };
  },
  mounted() {
    getDict("wlg_em_equipment_status").then(response => {
      this.equipmentStatusOptions = response.data.data
    })
    getDict("wlg_em_equipment_category").then(response => {
      this.equipCategoryOptions = response.data.data
    })
    getDict("wlg_em_section_type").then(response => {
      this.sectionTypeOptions = response.data.data
    })
    getAllUser().then(response => {
      this.userOptions = response.data.data
    })
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.mchCode = this.filters.mchCode;
      this.pageRequest.mchName = this.filters.mchName;
      this.pageRequest.spec = this.filters.spec;
      this.pageRequest.typeVersion = this.filters.typeVersion;
      this.pageRequest.factoryNo = this.filters.factoryNo;
      this.pageRequest.locationNo = this.filters.locationNo;
      this.pageRequest.assetManagerId = this.filters.assetManagerId;
      this.pageRequest.mchManagerId = this.filters.mchManagerId;
      this.pageRequest.dutyPersonId = this.filters.dutyPersonId;
      this.pageRequest.equipNumber = this.filters.equipNumber;
      this.pageRequest.equipCategory = this.filters.equipCategory;
      this.pageRequest.status = this.filters.status;

      this.queryLoading = true;
      findEquipmentManagementPage(this.pageRequest)
          .then((res) => {
            this.queryLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },

    exportExcelData(excelFileName) {
      let pageRequest = {};
      pageRequest.mchCode = this.filters.mchCode;
      pageRequest.mchName = this.filters.mchName;
      pageRequest.spec = this.filters.spec;
      pageRequest.typeVersion = this.filters.typeVersion;
      pageRequest.factoryNo = this.filters.factoryNo;
      pageRequest.locationNo = this.filters.locationNo;
      pageRequest.assetManagerId = this.filters.assetManagerId;
      pageRequest.mchManagerId = this.filters.mchManagerId;
      pageRequest.dutyPersonId = this.filters.dutyPersonId;
      pageRequest.equipNumber = this.filters.equipNumber;

      this.exportLoading = true;
      exportEquipmentExcel(pageRequest).then(res => {
        this.exportLoading = false;
        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', excelFileName + "-" + new Date().getTime() + ".xlsx");
        document.body.appendChild(link);
        link.click();
      });
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
                  this.$message({
                    message:
                        "操作失败 " + getResponseDataMessage(responseData),
                    type: "error",
                  });
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
                  this.$message({
                    message:
                        "操作失败, " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
                this.findPage(null);
              });
            }
          });
        }
      });
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
          this.progressStatus = "success";
          this.findPage(null);
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
        link.setAttribute('download', '设备清单Excel导入模板.xlsx');
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

    statusFormat: function (row) {
      return selectDictLabel(this.equipmentStatusOptions, row.status);
    },
    sectionTypeFormat: function (row) {
      return selectDictLabel(this.sectionTypeOptions, row.sectionType);
    },
    userFormat: function (row, column, cellValue) {
      return convertUser(this.userOptions, cellValue)
    },
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
  },
};
</script>
