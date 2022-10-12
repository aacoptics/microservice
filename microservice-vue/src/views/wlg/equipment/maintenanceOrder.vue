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
            <el-button type="info" :loading="comfirmLoading" @click="handleBatchConfirm">批量确认
              <template #icon>
                <font-awesome-icon :icon="['fas', 'check']"/>
              </template>
            </el-button>
            <el-button :loading="exportLoading" :size="size" type="success"
                       @click="exportExcelData('保养工单')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <orderTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false" :showPreview="false"
                :stripe="true"  :header-cell-style="{'text-align':'center'}" border @selection-change="handleSelectionChange"
            :cell-style="{'text-align':'left'}" :show-operation="false"
                @findPage="findPage" @handleCurrentChange="handleCurrentChange">
      </orderTable>
      <el-tabs type="border-card" style="margin-top: 50px;">
        <el-tab-pane label="保养项">
          <el-table size="small" :data="maintenanceOrderItemTableData" border :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'left'}" v-loading="findDetailLoading">
                <el-table-column prop="maintenanceItem" label="保养项" width="180" />
                <el-table-column prop="maintenanceItemStandard" label="保养项判断标准" width="180" />
                <el-table-column prop="minValue" label="起始范围值" />
                <el-table-column prop="maxValue" label="截至范围值" />
                <el-table-column prop="actualValue" label="实际值" />
                <el-table-column prop="isFinish" label="是否完成" />
                <el-table-column prop="maintenanceResult" label="保养结果" />
                <el-table-column prop="isException" label="是否存在异常" width="130"/>
                <el-table-column prop="isFault" label="是否存在故障" width="130"/>
                <el-table-column prop="isRepair" label="是否需要维修" width="130"/>
                <el-table-column prop="faultDesc" label="故障描述" />
                <!-- <el-table-column prop="faultPhoto" label="故障照片" /> -->
                <el-table-column prop="updatedBy" label="更新人" width="120"/>
                <el-table-column prop="updatedTime" label="更新时间" :formatter="dateTimeFormat" width="120"/>
                <el-table-column prop="createdBy" label="创建人" width="120"/>
                <el-table-column prop="createdTime" label="创建时间" :formatter="dateTimeFormat" width="120"/>
                <el-table-column label="故障照片" prop="faultImageId" fixed="right">
                  <template #default="scope">
                    <el-button size="small" type="primary" @click="handlePreview(scope.$index, scope.row)"
                      >预览</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
        </el-tab-pane>
      
      </el-tabs>

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="isMaintenanceOrderAddOperation?'新增':'编辑'"
                 width="25%" destroy-on-close>
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="设备名称" prop="mchName">
                <el-select v-model="dataForm.mchName" allow-create clearable filterable placeholder="请选择设备名称" @change="selectMchName" style="width:100%">
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
                <el-select v-model="dataForm.spec" allow-create clearable filterable placeholder="请选择规格" @change="selectSpec" style="width:100%">
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
                <el-select v-model="dataForm.typeVersion" allow-create clearable filterable placeholder="请选择型号" style="width:100%">
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
            <el-button :size="size" type="info" @click="cancel" style="margin-right: 0px;">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitMaintenanceOrderMain" style="margin-right: 20px;">提交</el-button>
          </slot>
        </div>
      </el-dialog>

      
      <el-dialog v-model="maintenanceOrderItemDialogVisible" :close-on-click-modal="false" :title="isMaintenanceOrderItemAddOperation?'新增保养项':'编辑保养项'"
                 width="25%" destroy-on-close>
        <el-form ref="maintenanceOrderItemDataForm" :model="maintenanceOrderItemDataForm" :rules="maintenanceOrderItemDataFormRules" :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="maintenanceOrderItemDataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item v-if="false" label="maintenanceOrderMainId" prop="maintenanceOrderMainId">
            <el-input v-model="maintenanceOrderItemDataForm.maintenanceOrderMainId" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="保养项" prop="checkItem">
                <el-input v-model="maintenanceOrderItemDataForm.checkItem" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
              <el-col :span="20">
              <el-form-item label="保养项判断标准" prop="checkItemStandard">
                <el-input v-model="maintenanceOrderItemDataForm.checkItemStandard" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="起始范围值" prop="minValue">
                <el-input-number v-model="maintenanceOrderItemDataForm.minValue" auto-complete="off" clearable style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="截至范围值" prop="maxValue">
                <el-input-number v-model="maintenanceOrderItemDataForm.maxValue" auto-complete="off" clearable style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          
        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancelMaintenanceOrderItem">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitMaintenanceOrderItem">提交</el-button>
          </slot>
        </div>
      </el-dialog>
      <el-dialog v-model="previewDialogVisible" title="图片预览" 
                 width="850px" destroy-on-close> 
                 <div class="block">
                 <el-image :src="imagePreviewSrc" style="width: 800px; height: 600px"/>
                 </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import orderTable from "./orderTable";
import {findMaintenanceOrderPage, handleAdd, handleUpdate,  findMaintenanceOrderById, handleBatchConfirm, exportMaintenanceOrderExcel} from "@/api/wlg/equipment/maintenanceOrder";
import {findMchNameList, findSpecListByMchName, findTypeVersionListByMchNameAndSpec} from "@/api/wlg/equipment/equipmentManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";
import {getDict, selectDictLabel} from "@/api/system/dictData";
import {findImageById} from "@/api/wlg/equipment/image";

export default {
  name: "maintenanceOrder",
  components: {orderTable},
  data() {
    return {
      size: "default",
      filters: {
        mchCode:'',
        mchName: "",
        spec: "",
        typeVersion: "",
        status: "",
      },
      imagePreviewSrc: '',

      columns: [
        {prop: "orderNumber", label: "工单号", minWidth: 110},
        {prop: "mchCode", label: "设备编码", minWidth: 110},
        {prop: "mchName", label: "设备名称", minWidth: 150},
        {prop: "spec", label: "规格", minWidth: 120},
        {prop: "typeVersion", label: "型号", minWidth: 150},
        {prop: "factoryNo", label: "出厂编码", minWidth: 130},
        {prop: "dutyPersonId", label: "责任人", minWidth: 100},
        {prop: "status", label: "状态", minWidth: 100, formatter: this.statusFormat},
        {prop: "maintenanceDate", label: "保养日期", minWidth: 100},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      maintenanceOrderItemTableData: [],

      isMaintenanceOrderAddOperation: false, // true:新增, false:编辑
      isMaintenanceOrderItemAddOperation: false,
      previewDialogVisible:false,

      dialogVisible: false, // 新增编辑界面是否显示
      maintenanceOrderItemDialogVisible: false,

      editLoading: false,
      findLoading: false,
      findDetailLoading: false,
      comfirmLoading: false,
      exportLoading: false,

      dataFormRules: {
        mchName: [{required: true, message: "请输入设备名称", trigger: "blur"}],
        spec: [{required: true, message: "请输入规格", trigger: "blur"}],
        typeVersion: [{required: true, message: "请输入型号", trigger: "blur"}],
      },
      maintenanceOrderItemDataFormRules:{
        checkItem: [{required: true, message: "请输入保养项", trigger: "blur"}],
        checkItemStandard: [{required: true, message: "请输入保养项判断标准", trigger: "blur"}],
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
      maintenanceOrderItemDataForm: {
        maintenanceOrderMainId: null,
        id: 0,
        checkItem: '',
        checkItemStandard: '',
        minValue:null,
        maxValue: null,
      },
      currentSelectMaintenanceOrderMainRowId: null,
      multipleSelection: [],
      orderStatusOptions:[]
    };
  },
  mounted() {
    findMchNameList().then(response => {
      if (response.data.data.length > 0) {
        this.mchNameOptions = response.data.data
      }
    }),
    getDict("wlg_em_maintenance_order_status").then(response => {
      this.orderStatusOptions = response.data.data
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
      this.findLoading = true;
      findMaintenanceOrderPage(this.pageRequest)
          .then((res) => {
            this.findLoading = false;
            this.currentSelectMaintenanceOrderMainRowId = null;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },
    findMaintenanceOrderDetail: function (id) {
      this.findDetailLoading = true;
      findMaintenanceOrderById(id)
          .then((res) => {
            this.findDetailLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              if(responseData.data != null)
              {
                this.maintenanceOrderItemTableData = responseData.data.maintenanceOrderItemList;
              }
            }
          });
    },
    handlePreview: function (index, row)
    {
      let id = row.faultImageId;
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
    handleCurrentChange: function(val)
    {
      if(val == null || val.val == null)
      {
        this.currentSelectMaintenanceOrderMainRowId = null;
        return;
      }
      this.currentSelectMaintenanceOrderMainRowId = val.val.id;
      this.findMaintenanceOrderDetail(this.currentSelectMaintenanceOrderMainRowId);
    },
    //获取多选的数据
    handleSelectionChange(val) {
      this.multipleSelection = val;//存储选中的数据
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
   
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.isMaintenanceOrderAddOperation = true;
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
      this.isMaintenanceOrderAddOperation = false;
      this.dataForm = Object.assign({}, params.row);
    },


     // 编辑
     submitMaintenanceOrderMain: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.isMaintenanceOrderAddOperation) {
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
    exportExcelData(excelFileName) {
      let pageRequest  = {};
      pageRequest.mchCode = this.filters.mchCode;
      pageRequest.mchName = this.filters.mchName;
      pageRequest.spec = this.filters.spec;
      pageRequest.typeVersion = this.filters.typeVersion;
      pageRequest.status = this.filters.status;

      this.exportLoading = true;
      exportMaintenanceOrderExcel(pageRequest).then(res => {
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
    cancelMaintenanceOrderItem()
    {
      this.maintenanceOrderItemDialogVisible = false;
    },
    statusFormat: function (row) {
      return selectDictLabel(this.orderStatusOptions, row.status);
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
