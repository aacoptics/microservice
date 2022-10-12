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
            <el-button :loading="exportLoading" :size="size" type="success"
                       @click="exportExcelData('维修工单')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <orderTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="true"  :header-cell-style="{'text-align':'center'}" border :show-batch-operation="true"
            :cell-style="{'text-align':'left'}" :show-operation="false" @selection-change="handleSelectionChange" @handlePreview="handlePreview"
                @findPage="findPage">
      </orderTable>

      <el-dialog v-model="previewDialogVisible" title="图片预览" 
                 width="850px" destroy-on-close> 
                 <div class="block">
                 <el-image :src="imagePreviewSrc" style="width: 800px; height: 600px"/>
                 </div>
      </el-dialog>

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
            <el-col :span="24">
              <el-form-item label="故障照片" prop="faultPhoto">
                <el-upload
                  class="avatar-uploader"
                  :show-file-list="false"
                  :before-upload="beforeAvatarUpload"
                  :http-request="submitUpload"
                >
                  <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
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
import {findRepairOrderPage, handleAdd, handleUpdate,  findRepairOrderById, handleBatchConfirm, exportRepairOrderExcel } from "@/api/wlg/equipment/repairOrder";
import {findEquipmentByMchCode} from "@/api/wlg/equipment/equipmentManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";
import {getDict, selectDictLabel} from "@/api/system/dictData";
import {addImage, findImageById} from "@/api/wlg/equipment/image";

export default {
  name: "repairOrder",
  components: {orderTable},
  data() {
    return {
      size: "default",

      imageUrl: '',

      imagePreviewSrc: '',
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
        {prop: "faultDesc", label: "故障描述", minWidth: 200},
        // {prop: "faultPhoto", label: "故障照片", minWidth: 100},
        {prop: "repairDesc", label: "维修说明", minWidth: 200},
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
      previewDialogVisible:false,

      editLoading: false,
      findLoading: false,
      comfirmLoading: false,
      previewLoading: false,
      exportLoading: false,

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
        faultImageId:null
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
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      return isJPG
    },
    submitUpload(params) {
      addImage(params).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.imageUrl = URL.createObjectURL(params.file);
          this.dataForm.faultImageId = responseData.data.id;
          this.$message.success('上传成功')
        } else {
          this.$message.error('上传失败！' + responseData.msg + "," + responseData.data);
        }
      }).catch((err) => {
        this.$message.error(err)
      })
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
      this.imageUrl = '';
    },
    findEquipmentByMchCode: function ()
    {
      if(this.dataForm.mchCode == null || this.dataForm.mchCode == "")
      {
        return;
      }
      let params = {};
      params.mchCode = this.dataForm.mchCode;
      findEquipmentByMchCode(params).then(response => {
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
    handlePreview: function (params)
    {
      let id = params.row.faultImageId;
      if(id == null)
      {
        this.$message.error('无故障图片！');
        return;
      }
      this.previewLoading = true;
      findImageById(id).then((response) => {
        this.previewLoading = false;
        const responseData = response.data
        if (responseData.code === '000000') {
          // const blob = new Blob([responseData.data.image], {type: 'image/jpeg'});
          // const imageUrl = window.URL.createObjectURL(blob);
          this.imagePreviewSrc = "data:image/jpeg;base64," + responseData.data.image;
          this.previewDialogVisible = true;
          this.$message.success('预览成功')
        } else {
          this.$message.error('预览失败！' + responseData.msg + "," + responseData.data);
        }
      }).catch((err) => {
        this.$message.error(err)
      })
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
    exportExcelData(excelFileName) {
      let pageRequest  = {};
      pageRequest.mchCode = this.filters.mchCode;
      pageRequest.mchName = this.filters.mchName;
      pageRequest.spec = this.filters.spec;
      pageRequest.typeVersion = this.filters.typeVersion;
      pageRequest.status = this.filters.status;

      this.exportLoading = true;
      exportRepairOrderExcel(pageRequest).then(res => {
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
<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>