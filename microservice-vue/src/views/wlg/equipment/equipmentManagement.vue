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
          <el-form-item label="型号" prop="typeVersion">
            <el-input v-model="filters.typeVersion" clearable placeholder="型号"></el-input>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="出厂编码" prop="factoryNo">
            <el-input v-model="filters.factoryNo" clearable placeholder="出厂编码"></el-input>
          </el-form-item>
          <el-form-item label="位置编码" prop="locationNo">
            <el-input v-model="filters.locationNo" clearable placeholder="位置编码"></el-input>
          </el-form-item>
          <el-form-item label="资产管理员" prop="assetManagerId">
            <el-input v-model="filters.assetManagerId" clearable placeholder="资产管理员"></el-input>
          </el-form-item>
          <el-form-item label="设备管理员" prop="mchManagerId">
            <el-input v-model="filters.mchManagerId" clearable placeholder="设备管理员"></el-input>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="责任人" prop="dutyPersonId">
            <el-input v-model="filters.dutyPersonId" clearable placeholder="责任人"></el-input>
          </el-form-item>
          <el-form-item label="设备编号" prop="equipNumber">
            <el-input v-model="filters.equipNumber" clearable placeholder="设备编号"></el-input>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
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
      <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult" 
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false" 
                :header-cell-style="{'text-align':'center'}" border :cell-style="{'text-align':'left'}"
                :stripe="true" :show-operation="true" :showOperationDel="false" @handleEdit="handleEdit"
                @findPage="findPage">
      </SysTable>


      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="30%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="资产编码" prop="mchCode">
                <el-input v-model="dataForm.mchCode" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="资产名称" prop="mchName">
                <el-input v-model="dataForm.mchName" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="规格" prop="spec">
                <el-input v-model="dataForm.spec" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="型号" prop="typeVersion">
                <el-input v-model="dataForm.typeVersion" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="设备编号" prop="equipNumber">
                <el-input v-model="dataForm.equipNumber" auto-complete="off" clearable></el-input>
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
import {findEquipmentManagementPage, exportEquipmentExcel, handleAdd, handleUpdate} from "@/api/wlg/equipment/equipmentManagement";
import {getDict, selectDictLabel} from "@/api/system/dictData";

export default {
  name: "equipmentManagement",
  components: {SysTable},
  data() {
    return {
      size: "default",

      exportLoading: false,
      editLoading: false,

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
      },
      dataForm: {
        id: 0,
        mchCode: "",
        mchName: "",
        spec: "",
        equipNumber: "",
      },

      equipmentStatusOptions:[],
      columns: [
        {prop: "mchCode", label: "资产编码", minWidth: 110},
        {prop: "mchName", label: "资产名称", minWidth: 100},
        {prop: "spec", label: "规格", minWidth: 100},
        {prop: "typeVersion", label: "型号", minWidth: 100},
        {prop: "equipNumber", label: "设备编号", minWidth: 150},
        {prop: "status", label: "状态", minWidth: 100,  formatter: this.statusFormat},
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
        {prop: "assetManagerId", label: "资产管理员", minWidth: 110},
        {prop: "mchManagerId", label: "设备管理员", minWidth: 110},
        {prop: "dutyPersonId", label: "责任人", minWidth: 100},
        {prop: "deptManagerId", label: "部门经理", minWidth: 100},
        {prop: "deptDirectorId", label: "部门总监", minWidth: 100},
        {prop: "vicePresidentId", label: "部门VP", minWidth: 100},
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

      findEquipmentManagementPage(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },

    exportExcelData(excelFileName) {
      let pageRequest  = {};
      pageRequest.mchCode = this.filters.mchCode;
      pageRequest.mchName = this.filters.mchName;
      pageRequest.spec = this.filters.spec;
      pageRequest.typeVersion = this.filters.typeVersion;
      pageRequest.factoryNo = this.filters.factoryNo;
      pageRequest.locationNo = this.filters.locationNo;
      pageRequest.assetManagerId = this.filters.assetManagerId;
      pageRequest.mchManagerId = this.filters.mchManagerId;
      pageRequest.dutyPersonId = this.filters.dutyPersonId;

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

    // 取消
    cancel() {
      this.dialogVisible = false;
    },

    statusFormat: function (row) {
      return selectDictLabel(this.equipmentStatusOptions, row.status);
    },
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
  },
};
</script>
