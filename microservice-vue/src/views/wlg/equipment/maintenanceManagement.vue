<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="设备名称" prop="mchName">
            <el-input v-model="filters.mchName" clearable placeholder="设备名称"></el-input>
          </el-form-item>
          <el-form-item label="规格" prop="spec">
            <el-input v-model="filters.spec" clearable placeholder="规格"></el-input>
          </el-form-item>
          <el-form-item label="型号" prop="typeVersion">
            <el-input v-model="filters.typeVersion" clearable placeholder="型号"></el-input>
          </el-form-item>
          <!-- <el-form-item label="保养项" prop="maintenanceItem">
            <el-input v-model="filters.maintenanceItem" clearable placeholder="保养项"></el-input>
          </el-form-item> -->
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="findLoading" type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
            <el-button type="success" @click="handleAdd">新增
              <template #icon>
                <font-awesome-icon :icon="['fas', 'plus']"/>
              </template>
            </el-button>
            <el-button type="info"
                       @click="handleOpenExcelUpload">Excel导入
              <template #icon>
                <font-awesome-icon :icon="['fas','file-lines']"/>
              </template>
            </el-button>
            <el-button :loading="exportLoading" :size="size" type="success"
                       @click="exportExcelData('保养维护')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <SysTable id="condDataTable" ref="sysTable" :cell-style="{'text-align':'left'}" :columns="columns"
                :data="pageResult" :header-cell-style="{'text-align':'center'}" :height="400"
                :highlightCurrentRow="true" :showBatchDelete="false" :stripe="true"
                border
                @findPage="findPage" @handleCurrentChange="handleCurrentChange" @handleDelete="handleDelete"
                @handleEdit="handleEdit">
      </SysTable>
      <el-tabs style="margin-top: 50px;" type="border-card">
        <el-tab-pane label="保养项">
          <el-button style="margin-bottom: 10px;" type="success" @click="handleAddMaintenanceItem">新增保养项
            <template #icon>
              <font-awesome-icon :icon="['fas', 'plus']"/>
            </template>
          </el-button>
          <el-table v-loading="findDetailLoading" :cell-style="{'text-align':'left'}" :data="maintenanceItemTableData"
                    :header-cell-style="{'text-align':'center'}"
                    border size="small">
            <el-table-column label="保养项" prop="maintenanceItem" width="180"/>
            <el-table-column label="保养项判断标准" prop="maintenanceItemStandard" width="180"/>
            <el-table-column label="起始范围值" prop="minValue"/>
            <el-table-column label="截至范围值" prop="maxValue"/>
            <el-table-column label="更新人" prop="updatedBy"/>
            <el-table-column :formatter="dateTimeFormat" label="更新时间" prop="updatedTime"/>
            <el-table-column label="创建人" prop="createdBy"/>
            <el-table-column :formatter="dateTimeFormat" label="创建时间" prop="createdTime"/>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleMaintenanceItemEdit(scope.$index, scope.row)"
                >
                  <template #icon>
                    <font-awesome-icon :icon="['far', 'pen-to-square']"/>
                  </template>
                </el-button
                >
                <el-button
                    size="small"
                    type="danger"
                    @click="handleMaintenanceItemDelete(scope.$index, scope.row)"
                >
                  <template #icon>
                    <font-awesome-icon :icon="['far', 'trash-can']"/>
                  </template>
                </el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="isMaintenanceAddOperation?'新增':'编辑'"
                 destroy-on-close width="25%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="设备名称" prop="mchName">
                <el-select v-model="dataForm.mchName" clearable filterable placeholder="请选择设备名称"
                           style="width:100%" @change="selectMchName">
                  <el-option
                      v-for="item in mchNameOptions"
                      :key="item"
                      :label="item"
                      :value="item"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="规格" prop="spec">
                <el-select v-model="dataForm.spec" clearable filterable placeholder="请选择规格" style="width:100%"
                           @change="selectSpec">
                  <el-option
                      v-for="item in specOptions"
                      :key="item"
                      :label="item"
                      :value="item"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="型号" prop="typeVersion">
                <el-select v-model="dataForm.typeVersion" clearable filterable placeholder="请选择型号"
                           style="width:100%">
                  <el-option
                      v-for="item in typeVersionOptions"
                      :key="item"
                      :label="item"
                      :value="item"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="保养周期（周）" prop="maintenancePeriod">
                <el-input-number v-model="dataForm.maintenancePeriod" auto-complete="off" clearable
                                 style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" style="margin-right: 0px;" type="info" @click="cancel">取消</el-button>
            <el-button :loading="editLoading" :size="size" style="margin-right: 20px;" type="primary"
                       @click="submitMaintenanceMain">提交
            </el-button>
          </slot>
        </div>
      </el-dialog>


      <el-dialog v-model="maintenanceItemDialogVisible" :close-on-click-modal="false"
                 :title="isMaintenanceItemAddOperation?'新增保养项':'编辑保养项'"
                 destroy-on-close width="25%">
        <el-form ref="maintenanceItemDataForm" :model="maintenanceItemDataForm" :rules="maintenanceItemDataFormRules"
                 :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="maintenanceItemDataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item v-if="false" label="maintenanceMainId" prop="maintenanceMainId">
            <el-input v-model="maintenanceItemDataForm.maintenanceMainId" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="保养项" prop="maintenanceItem">
                <el-input v-model="maintenanceItemDataForm.maintenanceItem" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="保养项判断标准" prop="maintenanceItemStandard">
                <el-input v-model="maintenanceItemDataForm.maintenanceItemStandard" auto-complete="off"
                          clearable></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="20">
              <el-form-item label="起始范围值" prop="minValue">
                <el-input-number v-model="maintenanceItemDataForm.minValue" auto-complete="off" clearable
                                 style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="截至范围值" prop="maxValue">
                <el-input-number v-model="maintenanceItemDataForm.maxValue" auto-complete="off" clearable
                                 style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancelMaintenanceItem">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitMaintenanceItem">提交
            </el-button>
          </slot>
        </div>
      </el-dialog>


      <el-dialog v-model="excelUploadDialogVisible" :close-on-click-modal="false" :title="'设备保养Excel导入'"

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
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {
  addMaintenanceItem,
  deleteMaintenance,
  deleteMaintenanceItem,
  downloadTemplate,
  exportMaintenanceExcel,
  findMaintenanceById,
  findMaintenancePage,
  handleAdd,
  handleUpdate,
  updateMaintenanceItem,
  uploadExcel
} from "@/api/wlg/equipment/maintenanceManagement";
import {
  findMchNameList,
  findSpecListByMchName,
  findTypeVersionListByMchNameAndSpec
} from "@/api/wlg/equipment/equipmentManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";

export default {
  name: "maintenance",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        mchName: "",
        spec: "",
        typeVersion: "",
        maintenanceItem: "",
      },
      columns: [
        {prop: "mchName", label: "设备名称", minWidth: 150},
        {prop: "spec", label: "规格", minWidth: 150},
        {prop: "typeVersion", label: "型号", minWidth: 150},
        {prop: "maintenancePeriod", label: "保养周期（周）", minWidth: 130},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      progressPercentage: 0,
      progressContent: "",
      pregressDuration: 6,
      progressStatus: "",

      maintenanceItemTableData: [],
      maintenanceShiftTableData: [],

      isMaintenanceAddOperation: false, // true:新增, false:编辑
      isMaintenanceItemAddOperation: false,
      isMaintenanceShiftAddOperation: false,

      dialogVisible: false, // 新增编辑界面是否显示
      maintenanceItemDialogVisible: false,
      maintenanceShiftDialogVisible: false,

      excelUploadDialogVisible: false,

      editLoading: false,
      findLoading: false,
      findDetailLoading: false,
      exportLoading: false,
      downloadTemplateLoading: false,

      dataFormRules: {
        mchName: [{required: true, message: "请输入设备名称", trigger: "blur"}],
        spec: [{required: true, message: "请输入规格", trigger: "blur"}],
        typeVersion: [{required: true, message: "请输入型号", trigger: "blur"}],
        maintenancePeriod: [{required: true, message: "请输入保养周期", trigger: "blur"}],
      },
      maintenanceItemDataFormRules: {
        maintenanceItem: [{required: true, message: "请输入保养项", trigger: "blur"}],
        maintenanceItemStandard: [{required: true, message: "请输入保养项判断标准", trigger: "blur"}],
        minValue: [{required: true, message: "请输入起始范围值", trigger: "blur"}],
        maxValue: [{required: true, message: "请输入截止范围值", trigger: "blur"}],
      },

      mchNameOptions: [],
      specOptions: [],
      typeVersionOptions: [],
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        mchName: '',
        spec: "",
        typeVersion: "",
      },
      maintenanceItemDataForm: {
        maintenanceMainId: null,
        id: 0,
        maintenanceItem: '',
        maintenanceItemStandard: '',
        maintenancePeriod: null,
        minValue: null,
        maxValue: null,
      },
      currentSelectMaintenanceMainRowId: null,
    };
  },
  mounted() {
    findMchNameList().then(response => {
      if (response.data.data.length > 0) {
        this.mchNameOptions = response.data.data
      }
    })
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.mchName = this.filters.mchName;
      this.pageRequest.spec = this.filters.spec;
      this.pageRequest.typeVersion = this.filters.typeVersion;
      this.pageRequest.maintenanceItem = this.filters.maintenanceItem;
      this.findLoading = true;
      findMaintenancePage(this.pageRequest)
          .then((res) => {
            this.findLoading = false;
            this.currentSelectMaintenanceMainRowId = null;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },
    findMaintenanceDetail: function (id) {
      this.findDetailLoading = true;
      findMaintenanceById(id)
          .then((res) => {
            this.findDetailLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              if (responseData.data != null) {
                this.maintenanceItemTableData = responseData.data.maintenanceItemList;
              }
            }
          });
    },
    handleCurrentChange: function (val) {
      if (val == null || val.val == null) {
        this.currentSelectMaintenanceMainRowId = null;
        return;
      }
      this.currentSelectMaintenanceMainRowId = val.val.id;
      this.findMaintenanceDetail(this.currentSelectMaintenanceMainRowId);
    },
    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteMaintenance(data.params[0]).then(data.callback);
    },
    handleMaintenanceItemDelete: function (index, row) {
      if (row != null) {
        this.$confirm('确认删除选中记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteMaintenanceItem(row).then((res) => {
            const responseData = res.data
            if (responseData.code === '000000') {
              this.$message({message: '删除成功', type: 'success'})
              this.findMaintenanceDetail(this.currentSelectMaintenanceMainRowId);
            } else {
              this.$message({
                message: `操作失败${getResponseDataMessage(responseData)}`,
                type: 'error'
              })
            }
            this.loading = false
          });
        })
      }

    },

    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.isMaintenanceAddOperation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        mchName: "",
        spec: "",
        typeVersion: "",
        maintenancePeriod: null,
      };
    },
    // 显示新增界面
    handleAddMaintenanceItem: function () {
      if (this.currentSelectMaintenanceMainRowId == null || this.currentSelectMaintenanceMainRowId == 0) {
        this.$message({
          message:
              "请先选择一行保养设备",
          type: "error",
        });
        return;
      }

      this.maintenanceItemDialogVisible = true;
      this.isMaintenanceItemAddOperation = true;
      this.maintenanceItemDataForm = {
        maintenanceMainId: this.currentSelectMaintenanceMainRowId,
        id: 0,
        maintenanceItem: '',
        maintenanceItemStandard: '',
        minValue: null,
        maxValue: null,
      };
    },

    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.isMaintenanceAddOperation = false;
      this.dataForm = Object.assign({}, params.row);
    },
    handleMaintenanceItemEdit: function (index, row) {
      this.maintenanceItemDialogVisible = true;
      this.isMaintenanceItemAddOperation = false;
      this.maintenanceItemDataForm = Object.assign({}, row);
    },

    // 编辑
    submitMaintenanceMain: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.isMaintenanceAddOperation) {
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

    // 提交
    submitMaintenanceItem: function () {
      this.$refs.maintenanceItemDataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.maintenanceItemDataForm);
            if (this.isMaintenanceItemAddOperation) {
              addMaintenanceItem(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.maintenanceItemDialogVisible = false;
                  this.$refs["maintenanceItemDataForm"].resetFields();

                  this.findMaintenanceDetail(this.currentSelectMaintenanceMainRowId);
                } else {
                  this.$message({
                    message:
                        "操作失败 " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
              });
            } else {
              updateMaintenanceItem(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.maintenanceItemDialogVisible = false;
                  this.$refs["maintenanceItemDataForm"].resetFields();

                  this.findMaintenanceDetail(this.currentSelectMaintenanceMainRowId);
                } else {
                  this.$message({
                    message:
                        "操作失败, " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
              });
            }
          });
        }
      });
    },
    exportExcelData(excelFileName) {
      let pageRequest = {};
      pageRequest.mchName = this.filters.mchName;
      pageRequest.spec = this.filters.spec;
      pageRequest.typeVersion = this.filters.typeVersion;

      this.exportLoading = true;
      exportMaintenanceExcel(pageRequest).then(res => {
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
    selectMchName() {
      this.dataForm.spec = "";
      this.dataForm.typeVersion = "";

      let params = {};
      params.mchName = this.dataForm.mchName;
      if (params.mchName == null || params.mchName == "") {
        return;
      }
      findSpecListByMchName(params).then(response => {
        if (response.data.data.length > 0) {
          this.specOptions = response.data.data
        }
      })
    },
    selectSpec() {
      this.dataForm.typeVersion = "";

      let params = {};
      params.mchName = this.dataForm.mchName;
      if (params.mchName == null || params.mchName == "") {
        return;
      }
      params.spec = this.dataForm.spec;
      if (params.spec == null || params.spec == "") {
        return;
      }
      findTypeVersionListByMchNameAndSpec(params).then(response => {
        if (response.data.data.length > 0) {
          this.typeVersionOptions = response.data.data
        }
      })
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
        link.setAttribute('download', '设备保养导入模板' + "-" + new Date().getTime() + ".xlsx");
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
    cancelMaintenanceItem() {
      this.maintenanceItemDialogVisible = false;
    },
    cancelMaintenanceShift() {
      this.maintenanceShiftDialogVisible = false;
    },

    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
    dateFormat: function (dateValue) {
      return this.$moment(dateValue).format('YYYY-MM-DD')
    },
    timeFormat: function (dateValue) {
      return this.$moment(dateValue).format("HH:mm:ss");
    },
  },
};
</script>
