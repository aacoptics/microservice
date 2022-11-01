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
          <!-- <el-form-item label="点检项" prop="checkItem">
            <el-input v-model="filters.checkItem" clearable placeholder="点检项"></el-input>
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
                       @click="exportExcelData('点检维护')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <SysTable id="condDataTable" ref="sysTable" :cell-style="{'text-align':'left'}" :columns="columns"
                :data="pageResult" :header-cell-style="{'text-align':'center'}" :height="400"
                :highlightCurrentRow="true" :showBatchDelete="false" :stripe="true" border
                @findPage="findPage" @handleCurrentChange="handleCurrentChange" @handleDelete="handleDelete"
                @handleEdit="handleEdit">
      </SysTable>
      <el-tabs style="margin-top: 50px;" type="border-card">
        <el-tab-pane label="点检项">
          <el-button style="margin-bottom: 10px;" type="success" @click="handleAddInspectionItem">新增点检项
            <template #icon>
              <font-awesome-icon :icon="['fas', 'plus']"/>
            </template>
          </el-button>
          <el-table v-loading="findDetailLoading" :cell-style="{'text-align':'left'}" :data="inspectionItemTableData"
                    :header-cell-style="{'text-align':'center'}"
                    border size="small">
            <el-table-column label="点检项" prop="checkItem" width="180"/>
            <el-table-column label="点检项判断标准" prop="checkItemStandard" width="180"/>
            <el-table-column label="起始范围值" prop="minValue"/>
            <el-table-column label="截至范围值" prop="maxValue"/>
            <el-table-column label="更新人" prop="updatedBy"/>
            <el-table-column :formatter="dateTimeFormat" label="更新时间" prop="updatedTime"/>
            <el-table-column label="创建人" prop="createdBy"/>
            <el-table-column :formatter="dateTimeFormat" label="创建时间" prop="createdTime"/>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleInspectionItemEdit(scope.$index, scope.row)"
                >
                  <template #icon>
                    <font-awesome-icon :icon="['far', 'pen-to-square']"/>
                  </template>
                </el-button
                >
                <el-button
                    size="small"
                    type="danger"
                    @click="handleInspectionItemDelete(scope.$index, scope.row)"
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
        <el-tab-pane label="点检班次">
          <el-button style=" margin-bottom: 10px;" type="success" @click="handleAddInspectionShift">新增点检班次
            <template #icon>
              <font-awesome-icon :icon="['fas', 'plus']"/>
            </template>
          </el-button>
          <el-table v-loading="findDetailLoading" :cell-style="{'text-align':'left'}" :data="inspectionShiftTableData"
                    :header-cell-style="{'text-align':'center'}"
                    border size="small">
            <el-table-column label="班次" prop="shift" width="180"/>
            <el-table-column label="开始时间" prop="startTime"/>
            <el-table-column label="结束时间" prop="endTime"/>
            <el-table-column label="更新人" prop="updatedBy"/>
            <el-table-column :formatter="dateTimeFormat" label="更新时间" prop="updatedTime"/>
            <el-table-column label="创建人" prop="createdBy"/>
            <el-table-column :formatter="dateTimeFormat" label="创建时间" prop="createdTime"/>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleInspectionShiftEdit(scope.$index, scope.row)"
                >
                  <template #icon>
                    <font-awesome-icon :icon="['far', 'pen-to-square']"/>
                  </template>
                </el-button
                >
                <el-button
                    size="small"
                    type="danger"
                    @click="handleInspectionShiftDelete(scope.$index, scope.row)"
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

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="isInspectionAddOperation?'新增':'编辑'"
                 destroy-on-close width="25%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
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
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" style="margin-right: 0px;" type="info" @click="cancel">取消</el-button>
            <el-button :loading="editLoading" :size="size" style="margin-right: 20px;" type="primary"
                       @click="submitInspectionMain">提交
            </el-button>
          </slot>
        </div>
      </el-dialog>


      <el-dialog v-model="inspectionItemDialogVisible" :close-on-click-modal="false"
                 :title="isInspectionItemAddOperation?'新增点检项':'编辑点检项'"
                 destroy-on-close width="25%">
        <el-form ref="inspectionItemDataForm" :model="inspectionItemDataForm" :rules="inspectionItemDataFormRules"
                 :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="inspectionItemDataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item v-if="false" label="inspectionMainId" prop="inspectionMainId">
            <el-input v-model="inspectionItemDataForm.inspectionMainId" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="点检项" prop="checkItem">
                <el-input v-model="inspectionItemDataForm.checkItem" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="点检项判断标准" prop="checkItemStandard">
                <el-input v-model="inspectionItemDataForm.checkItemStandard" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="起始范围值" prop="minValue">
                <el-input-number v-model="inspectionItemDataForm.minValue" auto-complete="off" clearable
                                 style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="截至范围值" prop="maxValue">
                <el-input-number v-model="inspectionItemDataForm.maxValue" auto-complete="off" clearable
                                 style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancelInspectionItem">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitInspectionItem">提交</el-button>
          </slot>
        </div>
      </el-dialog>

      <!-- 点检班次-->
      <el-dialog v-model="inspectionShiftDialogVisible" :close-on-click-modal="false"
                 :title="isInspectionShiftAddOperation?'新增点检班次':'编辑点检班次'"
                 destroy-on-close width="25%">
        <el-form ref="inspectionShiftDataForm" :model="inspectionShiftDataForm" :rules="inspectionShiftDataFormRules"
                 :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="inspectionShiftDataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item v-if="false" label="inspectionMainId" prop="inspectionMainId">
            <el-input v-model="inspectionShiftDataForm.inspectionMainId" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="班次" prop="shift">
                <el-input v-model="inspectionShiftDataForm.shift" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="开始时间" prop="startTime">
                <el-time-picker v-model="inspectionShiftDataForm.startTime" placeholder="班次开始时间"
                                style="width:100%"/>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="结束时间" prop="endTime">
                <el-time-picker v-model="inspectionShiftDataForm.endTime" placeholder="班次结束时间"
                                style="width:100%"/>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancelInspectionShift">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitInspectionShift">提交
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
  addInspectionItem,
  addInspectionShift,
  deleteInspection,
  deleteInspectionItem,
  deleteInspectionShift,
  downloadTemplate,
  exportInspectionExcel,
  findInspectionById,
  findInspectionPage,
  handleAdd,
  handleUpdate,
  updateInspectionItem,
  updateInspectionShift,
  uploadExcel
} from "@/api/wlg/equipment/inspectionManagement";
import {
  findMchNameList,
  findSpecListByMchName,
  findTypeVersionListByMchNameAndSpec
} from "@/api/wlg/equipment/equipmentManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";

export default {
  name: "inspection",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        mchName: "",
        spec: "",
        typeVersion: "",
        checkItem: "",
      },
      columns: [
        {prop: "mchName", label: "设备名称", minWidth: 150},
        {prop: "spec", label: "规格", minWidth: 150},
        {prop: "typeVersion", label: "型号", minWidth: 150},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      inspectionItemTableData: [],
      inspectionShiftTableData: [],

      progressPercentage: 0,
      progressContent: "",
      pregressDuration: 6,
      progressStatus: "",

      isInspectionAddOperation: false, // true:新增, false:编辑
      isInspectionItemAddOperation: false,
      isInspectionShiftAddOperation: false,

      dialogVisible: false, // 新增编辑界面是否显示
      inspectionItemDialogVisible: false,
      inspectionShiftDialogVisible: false,

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
      },
      inspectionItemDataFormRules: {
        checkItem: [{required: true, message: "请输入点检项", trigger: "blur"}],
        checkItemStandard: [{required: true, message: "请输入点检项判断标准", trigger: "blur"}],
        minValue: [{required: true, message: "请输入起始范围值", trigger: "blur"}],
        maxValue: [{required: true, message: "请输入截止范围值", trigger: "blur"}],
      },
      inspectionShiftDataFormRules: {
        shift: [{required: true, message: "请输入班次", trigger: "blur"}],
        startTime: [{required: true, message: "请选择班次开始时间", trigger: "blur"}],
        endTime: [{required: true, message: "请选择班次结束时间", trigger: "blur"}],
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
      inspectionItemDataForm: {
        inspectionMainId: null,
        id: 0,
        checkItem: '',
        checkItemStandard: '',
        minValue: null,
        maxValue: null,
      },
      inspectionShiftDataForm: {
        inspectionMainId: null,
        id: 0,
        shift: '',
        startTime: '',
        endTime: '',
      },
      currentSelectInspectionMainRowId: null,
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
      this.pageRequest.checkItem = this.filters.checkItem;
      this.findLoading = true;
      findInspectionPage(this.pageRequest)
          .then((res) => {
            this.findLoading = false;
            this.currentSelectInspectionMainRowId = null;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },
    findInspectionDetail: function (id) {
      this.findDetailLoading = true;
      findInspectionById(id)
          .then((res) => {
            this.findDetailLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              if (responseData.data != null) {
                this.inspectionItemTableData = responseData.data.inspectionItemList;
                this.inspectionShiftTableData = responseData.data.inspectionShiftList;
              }
            }
          });
    },
    handleCurrentChange: function (val) {
      if (val == null || val.val == null) {
        this.currentSelectInspectionMainRowId = null;
        return;
      }
      this.currentSelectInspectionMainRowId = val.val.id;
      this.findInspectionDetail(this.currentSelectInspectionMainRowId);
    },
    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteInspection(data.params[0]).then(data.callback);
    },
    handleInspectionItemDelete: function (index, row) {
      if (row != null) {
        this.$confirm('确认删除选中记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteInspectionItem(row).then((res) => {
            const responseData = res.data
            if (responseData.code === '000000') {
              this.$message({message: '删除成功', type: 'success'})
              this.findInspectionDetail(this.currentSelectInspectionMainRowId);
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
    handleInspectionShiftDelete: function (index, row) {
      if (row != null) {
        this.$confirm('确认删除选中记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteInspectionShift(row).then((res) => {
            const responseData = res.data
            if (responseData.code === '000000') {
              this.$message({message: '删除成功', type: 'success'})
              this.findInspectionDetail(this.currentSelectInspectionMainRowId);
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
      this.isInspectionAddOperation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        mchName: "",
        spec: "",
        typeVersion: "",
      };
    },
    // 显示新增界面
    handleAddInspectionItem: function () {
      if (this.currentSelectInspectionMainRowId == null || this.currentSelectInspectionMainRowId == 0) {
        this.$message({
          message:
              "请先选择一行点检设备",
          type: "error",
        });
        return;
      }

      this.inspectionItemDialogVisible = true;
      this.isInspectionItemAddOperation = true;
      this.inspectionItemDataForm = {
        inspectionMainId: this.currentSelectInspectionMainRowId,
        id: 0,
        checkItem: '',
        checkItemStandard: '',
        minValue: null,
        maxValue: null,
      };
    },
    // 显示新增界面
    handleAddInspectionShift: function () {
      if (this.currentSelectInspectionMainRowId == null || this.currentSelectInspectionMainRowId == 0) {
        this.$message({
          message:
              "请先选择一行点检设备",
          type: "error",
        });
        return;
      }
      this.inspectionShiftDialogVisible = true;
      this.isInspectionShiftAddOperation = true;

      this.inspectionShiftDataForm = {
        inspectionMainId: this.currentSelectInspectionMainRowId,
        id: 0,
        shift: "",
        startTime: null,
        endTime: null,
      };
    },
    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.isInspectionAddOperation = false;
      this.dataForm = Object.assign({}, params.row);
    },
    handleInspectionItemEdit: function (index, row) {
      this.inspectionItemDialogVisible = true;
      this.isInspectionItemAddOperation = false;
      this.inspectionItemDataForm = Object.assign({}, row);
    },
    handleInspectionShiftEdit: function (index, row) {
      this.inspectionShiftDialogVisible = true;
      this.isInspectionShiftAddOperation = false;
      this.inspectionShiftDataForm = Object.assign({}, row);

      var currentDate = this.dateFormat(new Date());
      this.inspectionShiftDataForm.startTime = currentDate + " " + row.startTime;
      this.inspectionShiftDataForm.endTime = currentDate + " " + row.endTime;
    },
    // 编辑
    submitInspectionMain: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.isInspectionAddOperation) {
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
    submitInspectionShift: function () {
      this.$refs.inspectionShiftDataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.inspectionShiftDataForm);
            params.startTime = this.timeFormat(this.inspectionShiftDataForm.startTime);
            params.endTime = this.timeFormat(this.inspectionShiftDataForm.endTime);


            if (this.isInspectionShiftAddOperation) {
              addInspectionShift(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.inspectionShiftDialogVisible = false;
                  this.$refs["inspectionShiftDataForm"].resetFields();

                  this.findInspectionDetail(this.currentSelectInspectionMainRowId);
                } else {
                  this.$message({
                    message:
                        "操作失败 " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
              });
            } else {
              updateInspectionShift(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.inspectionShiftDialogVisible = false;
                  this.$refs["inspectionShiftDataForm"].resetFields();

                  this.findInspectionDetail(this.currentSelectInspectionMainRowId);
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
    // 提交
    submitInspectionItem: function () {
      this.$refs.inspectionItemDataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.inspectionItemDataForm);
            if (this.isInspectionItemAddOperation) {
              addInspectionItem(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.inspectionItemDialogVisible = false;
                  this.$refs["inspectionItemDataForm"].resetFields();

                  this.findInspectionDetail(this.currentSelectInspectionMainRowId);
                } else {
                  this.$message({
                    message:
                        "操作失败 " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
              });
            } else {
              updateInspectionItem(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.inspectionItemDialogVisible = false;
                  this.$refs["inspectionItemDataForm"].resetFields();

                  this.findInspectionDetail(this.currentSelectInspectionMainRowId);
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
      exportInspectionExcel(pageRequest).then(res => {
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
    cancelInspectionItem() {
      this.inspectionItemDialogVisible = false;
    },
    cancelInspectionShift() {
      this.inspectionShiftDialogVisible = false;
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
