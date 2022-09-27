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
          <!-- <el-form-item label="维修项" prop="checkItem">
            <el-input v-model="filters.checkItem" clearable placeholder="维修项"></el-input>
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
          </el-form-item>
        </el-form>
      </div>
      <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="true"  :header-cell-style="{'text-align':'center'}" border
            :cell-style="{'text-align':'left'}" :show-operation="false"
                @findPage="findPage" @handleCurrentChange="handleCurrentChange">
      </SysTable>


      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="isRepairOrderAddOperation?'新增':'编辑'"
                 width="25%" destroy-on-close>
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="设备编码" prop="mchCode">
                <el-input v-model="dataForm.mchCode" :disabled="!isRepairOrderAddOperation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="设备名称" prop="mchName">
                <el-input v-model="dataForm.mchName" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="规格" prop="spec">
                <el-input v-model="dataForm.spec" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
              <el-col :span="20">
              <el-form-item label="型号" prop="typeVersion">
                <el-input v-model="dataForm.typeVersion" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="出厂编码" prop="factoryNo">
                <el-input v-model="dataForm.factoryNo" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="责任人" prop="dutyPersionId">
                <el-input v-model="dataForm.dutyPersionId" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
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
import SysTable from "@/components/SysTable";
import {findRepairOrderPage, handleAdd, handleUpdate,  
   findRepairOrderById, } from "@/api/wlg/equipment/repairOrder";
import {findMchNameList, findSpecListByMchName, findTypeVersionListByMchNameAndSpec} from "@/api/wlg/equipment/equipmentManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";

export default {
  name: "repairOrder",
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
        {prop: "orderNumber", label: "工单号", minWidth: 110},
        {prop: "mchCode", label: "设备编码", minWidth: 110},
        {prop: "mchName", label: "设备名称", minWidth: 150},
        {prop: "spec", label: "规格", minWidth: 100},
        {prop: "typeVersion", label: "型号", minWidth: 120},
        {prop: "factoryNo", label: "出厂编码", minWidth: 120},
        {prop: "dutyPersonId", label: "责任人", minWidth: 100},
        {prop: "status", label: "状态", minWidth: 100},
        {prop: "falutDesc", label: "故障描述", minWidth: 100},
        {prop: "falutPhoto", label: "故障照片", minWidth: 100},
        {prop: "repairDesc", label: "维修说明", minWidth: 100},
        {prop: "repairDate", label: "维修时间", minWidth: 100},
        {prop: "sourceType", label: "工单来源", minWidth: 100},
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
      findDetailLoading: false,
      dataFormRules: {
        mchCode: [{required: true, message: "请输入设备名称", trigger: "blur"}],
        faultDesc: [{required: true, message: "请输入型号", trigger: "blur"}],
        faultPhoto: [{required: true, message: "请输入型号", trigger: "blur"}],
      },

      mchNameOptions:[],
      specOptions:[],
      typeVersionOptions:[],
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        mchCode: '',
        mchName: '',
        spec: "",
        typeVersion: "",
        factoryNo:'',
        dutyPersionId:'',
        faultDesc:'',
        faultPhoto:null
      },
      repairOrderItemDataForm: {
        repairOrderMainId: null,
        id: 0,
        checkItem: '',
        checkItemStandard: '',
        minValue:null,
        maxValue: null,
      },
      currentSelectRepairOrderMainRowId: null,
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
      findRepairOrderPage(this.pageRequest)
          .then((res) => {
            this.findLoading = false;
            this.currentSelectRepairOrderMainRowId = null;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },


   
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.isRepairOrderAddOperation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        mchName: "",
        spec:"",
        typeVersion: "",
      };
    },

    
    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.isRepairOrderAddOperation = false;
      this.dataForm = Object.assign({}, params.row);
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
    cancelRepairOrderItem()
    {
      this.repairOrderItemDialogVisible = false;
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
