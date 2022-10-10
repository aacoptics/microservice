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
            <el-button type="primary" @click="findPage(null)" :loading="findLoading">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
            <el-button type="success" @click="handleAdd">新增
              <template #icon>
                <font-awesome-icon :icon="['fas', 'plus']"/>
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
      <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="true"  :header-cell-style="{'text-align':'center'}" border
            :cell-style="{'text-align':'left'}"
                @findPage="findPage" @handleDelete="handleDelete" @handleEdit="handleEdit" @handleCurrentChange="handleCurrentChange">
      </SysTable>
      <el-tabs type="border-card" style="margin-top: 50px;">
        <el-tab-pane label="保养项">
          <el-button type="success" @click="handleAddMaintenanceItem" style="margin-bottom: 10px;">新增保养项
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'plus']"/>
                </template>
              </el-button>
          <el-table size="small" :data="maintenanceItemTableData" border :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'left'}" v-loading="findDetailLoading">
                <el-table-column prop="maintenanceItem" label="保养项" width="180" />
                <el-table-column prop="maintenanceItemStandard" label="保养项判断标准" width="180" />
                <el-table-column prop="minValue" label="起始范围值" />
                <el-table-column prop="maxValue" label="截至范围值" />
                <el-table-column prop="updatedBy" label="更新人" />
                <el-table-column prop="updatedTime" label="更新时间" :formatter="dateTimeFormat"/>
                <el-table-column prop="createdBy" label="创建人" />
                <el-table-column prop="createdTime" label="创建时间" :formatter="dateTimeFormat"/>
                <el-table-column label="操作" width="120" >
                  <template #default="scope">
                    <el-button size="small" @click="handleMaintenanceItemEdit(scope.$index, scope.row)" type="primary"
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
                 width="25%" destroy-on-close>
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="设备名称" prop="mchName">
                <el-select v-model="dataForm.mchName" clearable filterable placeholder="请选择设备名称" @change="selectMchName" style="width:100%">
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
                <el-select v-model="dataForm.spec" clearable filterable placeholder="请选择规格" @change="selectSpec" style="width:100%">
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
                <el-select v-model="dataForm.typeVersion" clearable filterable placeholder="请选择型号" style="width:100%">
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
              <el-form-item label="保养周期（月）" prop="maintenancePeriod">
                <el-input-number v-model="dataForm.maintenancePeriod" auto-complete="off" clearable style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
            </el-row>
          
           </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancel" style="margin-right: 0px;">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitMaintenanceMain" style="margin-right: 20px;">提交</el-button>
          </slot>
        </div>
      </el-dialog>

      
      <el-dialog v-model="maintenanceItemDialogVisible" :close-on-click-modal="false" :title="isMaintenanceItemAddOperation?'新增保养项':'编辑保养项'"
                 width="25%" destroy-on-close>
        <el-form ref="maintenanceItemDataForm" :model="maintenanceItemDataForm" :rules="maintenanceItemDataFormRules" :size="size" label-width="120px">
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
                <el-input v-model="maintenanceItemDataForm.maintenanceItemStandard" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="20">
              <el-form-item label="起始范围值" prop="minValue">
                <el-input-number v-model="maintenanceItemDataForm.minValue" auto-complete="off" clearable style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="截至范围值" prop="maxValue">
                <el-input-number v-model="maintenanceItemDataForm.maxValue" auto-complete="off" clearable style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          
        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancelMaintenanceItem">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitMaintenanceItem">提交</el-button>
          </slot>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {deleteMaintenance, findMaintenancePage, handleAdd, handleUpdate, addMaintenanceItem, exportMaintenanceExcel,
  updateMaintenanceItem, findMaintenanceById, deleteMaintenanceItem} from "@/api/wlg/equipment/maintenanceManagement";
import {findMchNameList, findSpecListByMchName, findTypeVersionListByMchNameAndSpec} from "@/api/wlg/equipment/equipmentManagement";
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
        {prop: "mchName", label: "设备名称", minWidth: 110},
        {prop: "spec", label: "规格", minWidth: 100},
        {prop: "typeVersion", label: "型号", minWidth: 100},
        {prop: "maintenancePeriod", label: "保养周期（月）", minWidth: 100},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      maintenanceItemTableData: [],
      maintenanceShiftTableData: [],

      isMaintenanceAddOperation: false, // true:新增, false:编辑
      isMaintenanceItemAddOperation: false,
      isMaintenanceShiftAddOperation: false,

      dialogVisible: false, // 新增编辑界面是否显示
      maintenanceItemDialogVisible: false,
      maintenanceShiftDialogVisible: false,

      editLoading: false,
      findLoading: false,
      findDetailLoading: false,
      exportLoading: false,

      dataFormRules: {
        mchName: [{required: true, message: "请输入设备名称", trigger: "blur"}],
        spec: [{required: true, message: "请输入规格", trigger: "blur"}],
        typeVersion: [{required: true, message: "请输入型号", trigger: "blur"}],
        maintenancePeriod: [{required: true, message: "请输入保养周期", trigger: "blur"}],
      },
      maintenanceItemDataFormRules:{
        maintenanceItem: [{required: true, message: "请输入保养项", trigger: "blur"}],
        maintenanceItemStandard: [{required: true, message: "请输入保养项判断标准", trigger: "blur"}],
        minValue: [{required: true, message: "请输入起始范围值", trigger: "blur"}],
        maxValue: [{required: true, message: "请输入截止范围值", trigger: "blur"}],
      },

      mchNameOptions:[],
      specOptions:[],
      typeVersionOptions:[],
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
        minValue:null,
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
              if(responseData.data != null)
              {
                this.maintenanceItemTableData = responseData.data.maintenanceItemList;
              }
            }
          });
    },
    handleCurrentChange: function(val)
    {
      if(val == null || val.val == null)
      {
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
      if (row != null)
      {
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
        typeVersion:"",
        maintenancePeriod: null,
      };
    },
    // 显示新增界面
    handleAddMaintenanceItem: function () {
      if(this.currentSelectMaintenanceMainRowId == null || this.currentSelectMaintenanceMainRowId == 0)
      {
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
        minValue:null,
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
      let pageRequest  = {};
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
    selectMchName()
    {
      this.dataForm.spec = "";
      this.dataForm.typeVersion = "";

      let params = {};
      params.mchName = this.dataForm.mchName;
      if(params.mchName == null || params.mchName == "")
      {
        return;
      }
      findSpecListByMchName(params).then(response => {
        if (response.data.data.length > 0) {
          this.specOptions = response.data.data
        }
      })
    },
    selectSpec()
    {
      this.dataForm.typeVersion = "";

      let params = {};
      params.mchName = this.dataForm.mchName;
      if(params.mchName == null || params.mchName == "")
      {
        return;
      }
      params.spec = this.dataForm.spec;
      if(params.spec == null || params.spec == "")
      {
        return;
      }
      findTypeVersionListByMchNameAndSpec(params).then(response => {
        if (response.data.data.length > 0) {
          this.typeVersionOptions = response.data.data
        }
      })
    },
    // 取消
    cancel() {
      this.dialogVisible = false;
    },
    cancelMaintenanceItem()
    {
      this.maintenanceItemDialogVisible = false;
    },
    cancelMaintenanceShift()
    {
      this.maintenanceShiftDialogVisible = false;
    },

    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
    dateFormat: function (dateValue) {
      return this.$moment(dateValue).format('YYYY-MM-DD')
    },
    timeFormat: function(dateValue)
    {
      return this.$moment(dateValue).format("HH:mm:ss");
    },
  },
};
</script>
