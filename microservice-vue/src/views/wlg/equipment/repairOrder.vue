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
            <el-form-item label="设备编号" prop="equipNumber">
              <el-input v-model="filters.equipNumber" clearable placeholder="设备编号"></el-input>
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
            <el-button :loading="findLoading" type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
            <el-button :loading="editLoading" type="success" @click="handleAdd">新增
              <template #icon>
                <font-awesome-icon :icon="['fas', 'plus']"/>
              </template>
            </el-button>
            <el-button :loading="comfirmLoading" type="info" @click="handleBatchConfirm">批量确认
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
      <orderTable id="condDataTable" ref="sysTable" :cell-style="{'text-align':'left'}" :columns="columns"
                  :data="pageResult" :header-cell-style="{'text-align':'center'}" :height="400"
                  :highlightCurrentRow="true" :show-batch-operation="true" :show-operation="false"
                  :showBatchDelete="false" :showOperationDel="false"
                  :stripe="true" border @findPage="findPage"
                  @handlePreview="handlePreview"
                  @selection-change="handleSelectionChange">
      </orderTable>

      <el-dialog v-model="previewDialogVisible" destroy-on-close
                 title="图片预览" width="850px">
        <div class="block">
          <el-image :src="imagePreviewSrc" style="width: 800px; height: 600px"/>
        </div>
      </el-dialog>

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="isRepairOrderAddOperation?'新增':'编辑'"
                 destroy-on-close width="40%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="150px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="24">
              <el-form-item label="设备编码/设备编号" prop="code">
                <el-input v-model="dataForm.code" :disabled="!isRepairOrderAddOperation"
                          auto-complete="off" clearable @blur="findEquipmentByMchCode"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备编码" prop="mchCode">
                <el-input v-model="dataForm.mchCode" :disabled="true"
                          auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备编号" prop="equipNumber">
                <el-input v-model="dataForm.equipNumber" :disabled="true"
                          auto-complete="off" clearable></el-input>
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
            <!-- <el-col :span="12">
              <el-form-item label="工段类型" prop="sectionType">
                <el-input v-model="dataForm.sectionType" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col> -->
            <!-- <el-col :span="12">
              <el-form-item label="责任人" prop="dutyPersonId">
                <el-input v-model="dataForm.dutyPersonId" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col> -->
            <el-col :span="12">
              <el-form-item label="设备负责人" prop="equipDuty">
                <el-input v-model="dataForm.equipDuty" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备负责人经理" prop="equipDutyManager">
                <el-input v-model="dataForm.equipDutyManager" :disabled="true" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="异常分类" prop="exceptionTypeId">
                <el-select v-model="dataForm.exceptionTypeId" clearable filterable placeholder="请选择异常分类"
                           style="width:100%" @change="selectExceptionType">
                  <el-option
                      v-for="item in exceptionTypeOptions"
                      :key="item.id"
                      :label="item.exceptionType"
                      :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="异常子类" prop="exceptionSubclass">
                <el-select v-model="dataForm.exceptionSubclass" clearable filterable placeholder="请选择异常子类"
                           style="width:100%">
                  <el-option
                      v-for="item in exceptionSubclassOptions"
                      :key="item.subClass"
                      :label="item.subClass"
                      :value="item.subClass"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="模具" prop="mould">
                <el-input v-model="dataForm.mould" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="故障描述" prop="faultDesc">
                <el-input v-model="dataForm.faultDesc" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="故障照片" prop="faultPhoto">
                <el-upload
                    :before-upload="beforeAvatarUpload"
                    :http-request="submitUpload"
                    :show-file-list="false"
                    class="avatar-uploader"
                >
                  <img v-if="imageUrl" :src="imageUrl" class="avatar"/>
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" style="margin-right: 0px;" type="info" @click="cancel">取消</el-button>
            <el-button :loading="editLoading" :size="size" style="margin-right: 20px;" type="primary"
                       @click="submitRepairOrderMain">提交
            </el-button>
          </slot>
        </div>
      </el-dialog>


      <el-dialog v-model="dialogEditDutyPersonVisible" :close-on-click-modal="false"
                 destroy-on-close
                 title="编辑接单人" width="25%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="工单号" prop="orderNumber">
                <el-input v-model="dataForm.orderNumber" clearable disabled placeholder="工单号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="设备名称" prop="mchName">
                <el-input v-model="dataForm.mchName" clearable disabled placeholder="设备名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="规格" prop="spec">
                <el-input v-model="dataForm.spec" clearable disabled placeholder="规格"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="型号" prop="typeVersion">
                <el-input v-model="dataForm.typeVersion" clearable disabled placeholder="型号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="接单人" prop="status">
                <el-select v-model="dataForm.dutyPersonId" clearable filterable placeholder="接单人" style="width:100%">
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
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" style="margin-right: 0px;" type="info" @click="cancelEditPerson">取消</el-button>
            <el-button :loading="editLoading" :size="size" style="margin-right: 20px;" type="primary"
                       @click="submitRepairOrderMain">提交
            </el-button>
          </slot>
        </div>
      </el-dialog>


    </div>
  </div>
</template>

<script>
import orderTable from "./orderTable";
import {
  exportRepairOrderExcel,
  findRepairOrderPage,
  handleAdd,
  handleBatchConfirm,
  handleUpdate
} from "@/api/wlg/equipment/repairOrder";
import {
  findExceptionTypeList,
  findExceptionTypeById,
} from "@/api/wlg/equipment/exceptionTypeManagement";
import {convertUser, findEquipmentByMchCode} from "@/api/wlg/equipment/equipmentManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";
import {getDict, selectDictLabel} from "@/api/system/dictData";
import {addImage, findImageById} from "@/api/wlg/equipment/image";
import {getAllUser} from "@/api/system/user"

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
        equipNumber: "",
      },
      columns: [
        {prop: "orderNumber", label: "工单号", minWidth: 110},
        {prop: "mchCode", label: "设备编码", minWidth: 110},
        {prop: "mchName", label: "设备名称", minWidth: 150},
        {prop: "equipNumber", label: "设备编号", minWidth: 150},
        {prop: "spec", label: "规格", minWidth: 120},
        {prop: "typeVersion", label: "型号", minWidth: 150},
        // {prop: "factoryNo", label: "出厂编码", minWidth: 130},
        {prop: "dutyPersonId", label: "接单人", minWidth: 150, formatter: this.userFormat},
        {prop: "status", label: "状态", minWidth: 100, formatter: this.statusFormat},
        {prop: "exceptionType", label: "异常分类", minWidth: 130},
        {prop: "exceptionSubclass", label: "异常子类", minWidth: 130},
        {prop: "faultDesc", label: "故障描述", minWidth: 200},
        {prop: "mould", label: "模具", minWidth: 130},
        {prop: "reason", label: "原因分析", minWidth: 200},
        {prop: "handleMethod", label: "处理方法", minWidth: 200},
        {prop: "isClosed", label: "是否结案", minWidth: 200, formatter: this.yesNoFormat},
        {prop: "longTermMeasure", label: "长期措施", minWidth: 200},        
        // {prop: "faultPhoto", label: "故障照片", minWidth: 100},
        {prop: "repairDesc", label: "备注", minWidth: 200},
        {prop: "stageDatetime", label: "接单时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "repairDatetime", label: "提交时间", minWidth: 120},
        {prop: "receiveOrderTime", label: "接单时长（Min）", minWidth: 140},
        {prop: "repairOrderTime", label: "维修时长（Min）", minWidth: 140},
        {prop: "consumptionTime", label: "累计时长（Min）", minWidth: 140},        
        {prop: "sourceType", label: "工单来源", minWidth: 100, formatter: this.orderSourceFormat},
        {prop: "updatedBy", label: "操作人", minWidth: 150, formatter: this.userFormat},
        {prop: "updatedTime", label: "操作时间", minWidth: 120, formatter: this.dateTimeFormat},
        // {prop: "createdBy", label: "创建人", minWidth: 120},
        // {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      repairOrderItemTableData: [],

      isRepairOrderAddOperation: false, // true:新增, false:编辑
      isRepairOrderItemAddOperation: false,

      dialogVisible: false, // 新增编辑界面是否显示
      repairOrderItemDialogVisible: false,
      previewDialogVisible: false,
      dialogEditDutyPersonVisible: false,

      editLoading: false,
      findLoading: false,
      comfirmLoading: false,
      previewLoading: false,
      exportLoading: false,

      dataFormRules: {
        mchCode: [{required: true, message: "请输入设备编码", trigger: "blur"}],
        equipNumber: [{required: true, message: "请输入设备编号", trigger: "blur"}],
        faultDesc: [{required: true, message: "请输入故障描述", trigger: "blur"}],
      },

      // 新增编辑界面数据
      dataForm: {
        id: 0,
        code: '',
        mchCode: '',
        equipNumber: '',
        mchName: '',
        spec: "",
        typeVersion: "",
        factoryNo: '',
        dutyPersonId: '',
        exceptionType:'',
        exceptionTypeId: null,
        mould: '',
        exceptionSubclass: '',
        equipDuty: '',
        equipDutyManager: '',
        faultDesc: '',
        faultImageId: null
      },
      multipleSelection: [],
      orderStatusOptions: [],
      orderSourceOptions: [],
      userOptions: [],
      exceptionTypeOptions: [],
      exceptionSubclassOptions: [],
      yesNoOptions: [],
    };
  },
  mounted() {
    findExceptionTypeList().then(response => {
      if (response.data.data.length > 0) {
        this.exceptionTypeOptions = response.data.data
      }
    }),
    getDict("wlg_em_repair_order_status").then(response => {
      this.orderStatusOptions = response.data.data
    });
    getDict("wlg_em_repair_order_source").then(response => {
      this.orderSourceOptions = response.data.data
    });
    getDict("wlg_em_yes_no").then(response => {
      this.yesNoOptions = response.data.data
    }),
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
      this.pageRequest.status = this.filters.status;
      this.pageRequest.equipNumber = this.filters.equipNumber;
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
        factoryNo: '',
        dutyPersonId: '',
        exceptionType: '',
        exceptionSubclass: '',
        module: '',
        faultDesc: '',
        faultPhoto: null
      };
      this.imageUrl = '';
    },
    findEquipmentByMchCode: function () {
      if (this.dataForm.code == null || this.dataForm.code == "") {
        return;
      }
      let params = {};
      params.mchCode = this.dataForm.code;
      findEquipmentByMchCode(params).then(response => {
        const responseData = response.data;
        if (responseData.code === "000000") {
          this.dataForm.mchCode = responseData.data.mchCode;
          this.dataForm.equipNumber = responseData.data.equipNumber;
          this.dataForm.mchName = responseData.data.mchName;
          this.dataForm.spec = responseData.data.spec;
          this.dataForm.typeVersion = responseData.data.typeVersion;
          this.dataForm.factoryNo = responseData.data.factoryNo;
          this.dataForm.dutyPersonId = responseData.data.dutyPersonId;
          this.dataForm.equipDuty = responseData.data.equipDuty;
          this.dataForm.equipDutyManager = responseData.data.equipDutyManager;
        } else {
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
      this.dialogEditDutyPersonVisible = true;
      this.isRepairOrderAddOperation = false;
      this.dataForm = Object.assign({}, params.row);
    },
    handlePreview: function (params) {
      let id = params.row.faultImageId;
      if (id == null) {
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
    cancelEditPerson() {
      this.dialogEditDutyPersonVisible = false;
    },
    selectExceptionType(val)
    {
      this.exceptionSubclassOptions = [];
      this.dataForm.exceptionSubclass = '';
      
      if (this.dataForm.exceptionTypeId == null || this.dataForm.exceptionTypeId == '') {
        return;
      }

      findExceptionTypeById(this.dataForm.exceptionTypeId)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              if (responseData.data != null) {
                this.exceptionSubclassOptions = responseData.data.exceptionSubClassList;
              }
            }
          });
    },
    //处理批量确认
    handleBatchConfirm: function () {
      if (this.multipleSelection == null || this.multipleSelection.length == 0) {
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

    
    submitRepairOrderMain: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            for (let item of this.exceptionTypeOptions) {
                if (item.id == this.dataForm.exceptionTypeId) {
                  this.dataForm.exceptionType = item.exceptionType;
                  break;
                }
            }

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
                  this.dialogEditDutyPersonVisible = false;
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
      let pageRequest = {};
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
    yesNoFormat: function (row, column, cellValue) {
      return selectDictLabel(this.yesNoOptions, cellValue);
    },
    // 时间格式化
    dateTimeFormat: function (row, column) {
      if(row[column.property] == null)
      {
        return null;
      }
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
    dateFormat: function (dateValue) {
      return this.$moment(dateValue).format('YYYY-MM-DD')
    },
    timeFormat: function (dateValue) {
      return this.$moment(dateValue).format("HH:mm:ss");
    },
    userFormat: function (row, column, cellValue) {
      return convertUser(this.userOptions, cellValue)
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
