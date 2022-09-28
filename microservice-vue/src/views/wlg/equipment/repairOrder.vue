<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-row>
          <el-form-item label="设备编码" prop="mchCode">
            <el-input v-model="filters.mchCode" clearable placeholder="设备编码"></el-input>
          </el-form-item>
          <el-form-item label="设备名称" prop="mchName">
            <el-input v-model="filters.mchName" clearable placeholder="设备名称"></el-input>
          </el-form-item>
          <el-form-item label="规格" prop="spec">
            <el-input v-model="filters.spec" clearable placeholder="规格"></el-input>
          </el-form-item>
        </el-row>
          <el-row>
          <el-form-item label="型号" prop="typeVersion">
            <el-input v-model="filters.typeVersion" clearable placeholder="型号"></el-input>
          </el-form-item>
          <el-form-item label="工单状态" prop="status">
          <el-select v-model="filters.status" clearable placeholder="工单状态" style="width:90%">
            <el-option
                v-for="item in orderStatusOptions"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"
            >
            </el-option>
          </el-select>
        </el-form-item>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button type="primary" @click="findPage(null)" :loading="findLoading">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
            <el-button type="success" @click="handleAdd" :loading="editLoading">新增
              <template #icon>
                <font-awesome-icon :icon="['fas', 'plus']"/>
              </template>
            </el-button>
            <el-button type="info" :loading="comfirmLoading" @click="handleBatchConfirm">批量确认
              <template #icon>
                <font-awesome-icon :icon="['fas', 'check']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <orderTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="true"  :header-cell-style="{'text-align':'center'}" border :show-batch-operation="true"
            :cell-style="{'text-align':'left'}" :show-operation="false" @selection-change="handleSelectionChange"
                @findPage="findPage">
      </orderTable>


      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="isRepairOrderAddOperation?'新增':'编辑'"
                 width="40%" destroy-on-close>
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="设备编码" prop="mchCode">
                <el-input v-model="dataForm.mchCode"  @blur="findEquipmentByMchCode" :disabled="!isRepairOrderAddOperation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备名称" prop="mchName">
                <el-input v-model="dataForm.mchName" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="规格" prop="spec">
                <el-input v-model="dataForm.spec" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
              <el-col :span="12">
              <el-form-item label="型号" prop="typeVersion">
                <el-input v-model="dataForm.typeVersion" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="出厂编码" prop="factoryNo">
                <el-input v-model="dataForm.factoryNo" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="责任人" prop="dutyPersonId">
                <el-input v-model="dataForm.dutyPersonId" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="故障描述" prop="faultDesc">
                <el-input v-model="dataForm.faultDesc"  auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="故障照片" prop="faultPhoto">
                <el-input v-model="dataForm.faultPhoto"  auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            </el-row>
          
           </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancel" style="margin-right: 0px;">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitRepairOrderMain" style="margin-right: 20px;">提交</el-button>
          </slot>
        </div>
      </el-dialog>

      

    </div>
  </div>
</template>

<script>
import orderTable from "./orderTable";
import {findRepairOrderPage, handleAdd, handleUpdate,  findRepairOrderById, handleBatchConfirm } from "@/api/wlg/equipment/repairOrder";
import {findEquipmentByMchCode} from "@/api/wlg/equipment/equipmentManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";
import {getDict, selectDictLabel} from "@/api/system/dictData";

export default {
  name: "repairOrder",
  components: {orderTable},
  data() {
    return {
      size: "default",
      filters: {
        mchCode: "",
        mchName: "",
        spec: "",
        typeVersion: "",
        status: "",
      },
      columns: [
        {prop: "orderNumber", label: "工单号", minWidth: 110},
        {prop: "mchCode", label: "设备编码", minWidth: 110},
        {prop: "mchName", label: "设备名称", minWidth: 150},
        {prop: "spec", label: "规格", minWidth: 100},
        {prop: "typeVersion", label: "型号", minWidth: 120},
        {prop: "factoryNo", label: "出厂编码", minWidth: 130},
        {prop: "dutyPersonId", label: "责任人", minWidth: 100},
        {prop: "status", label: "状态", minWidth: 100, formatter: this.statusFormat},
        {prop: "faultDesc", label: "故障描述", minWidth: 150},
        {prop: "faultPhoto", label: "故障照片", minWidth: 100},
        {prop: "repairDesc", label: "维修说明", minWidth: 150},
        {prop: "repairDatetime", label: "维修时间", minWidth: 100},
        {prop: "sourceType", label: "工单来源", minWidth: 100, formatter: this.orderSourceFormat},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      repairOrderItemTableData: [],

      isRepairOrderAddOperation: false, // true:新增, false:编辑
      isRepairOrderItemAddOperation: false,

      dialogVisible: false, // 新增编辑界面是否显示
      repairOrderItemDialogVisible: false,

      editLoading: false,
      findLoading: false,
      comfirmLoading: false,

      dataFormRules: {
        mchCode: [{required: true, message: "请输入设备编码", trigger: "blur"}],
        faultDesc: [{required: true, message: "请输入故障描述", trigger: "blur"}],
      },

      // 新增编辑界面数据
      dataForm: {
        id: 0,
        mchCode: '',
        mchName: '',
        spec: "",
        typeVersion: "",
        factoryNo:'',
        dutyPersonId:'',
        faultDesc:'',
        faultPhoto:null
      },
      multipleSelection: [],
      orderStatusOptions:[],
      orderSourceOptions:[],
    };
  },
  mounted() {
    getDict("wlg_em_repair_order_status").then(response => {
      this.orderStatusOptions = response.data.data
    });
    getDict("wlg_em_repair_order_source").then(response => {
      this.orderSourceOptions = response.data.data
    });
    
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
      this.pageRequest.status = this.filters.status;
      this.findLoading = true;
      findRepairOrderPage(this.pageRequest)
          .then((res) => {
            this.findLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },
    //获取多选的数据
    handleSelectionChange(val) {
      this.multipleSelection = val;//存储选中的数据
    },
   
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.isRepairOrderAddOperation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        mchCode: '',
        mchName: '',
        spec: "",
        typeVersion: "",
        factoryNo:'',
        dutyPersonId:'',
        faultDesc:'',
        faultPhoto:null
      };
    },
    findEquipmentByMchCode: function ()
    {
      var mchCode = this.dataForm.mchCode;
      findEquipmentByMchCode(mchCode).then(response => {
        const responseData = response.data;
        if (responseData.code === "000000") {
          this.dataForm.mchName = responseData.data.mchName;
          this.dataForm.spec = responseData.data.spec;
          this.dataForm.typeVersion = responseData.data.typeVersion;
          this.dataForm.factoryNo = responseData.data.factoryNo;
          this.dataForm.dutyPersonId = responseData.data.dutyPersonId;
        }else {
          this.$message({
            message:
                "操作失败 " + getResponseDataMessage(responseData),
            type: "error",
          });
        }
    })
    },    
    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.isRepairOrderAddOperation = false;
      this.dataForm = Object.assign({}, params.row);
    },
    //处理批量确认
    handleBatchConfirm: function()
    {
      if(this.multipleSelection == null || this.multipleSelection.length==0)
      {
        this.$message({
            message:
                "请至少选择一个工单",
            type: "error",
          });
          return;
      }

      this.$confirm("确定批量确认吗？", "提示", {}).then(() => {
        let ids = this.multipleSelection.selections.map(item => item.id).toString()
        let params = []
        let idArray = (ids + '').split(',')
        for (let i = 0; i < idArray.length; i++) {
          params.push(idArray[i])
        }
        this.comfirmLoading = true;
        handleBatchConfirm(params).then((res) => {
          const responseData = res.data;
          this.comfirmLoading = false;
          if (responseData.code === "000000") {
            this.$message({message: "操作成功", type: "success"});
          } else {
            this.$message({
              message:
                  "操作失败 " + getResponseDataMessage(responseData),
              type: "error",
            });
          }
          this.findPage(null);
        }); 
      });
    },

     // 编辑
     submitRepairOrderMain: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.isRepairOrderAddOperation) {
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
      return selectDictLabel(this.orderStatusOptions, row.status);
    },
    orderSourceFormat: function (row) {
      return selectDictLabel(this.orderSourceOptions, row.sourceType);
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
