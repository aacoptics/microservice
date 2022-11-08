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
          <el-row>
            <el-form-item label="点检项" prop="checkItem">
              <el-input v-model="filters.checkItem" clearable placeholder="点检项"></el-input>
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
            <el-button :loading="exportLoading" :size="size" type="success"
                       @click="exportExcelData('点检工单')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas','download']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <orderTable id="condDataTable" ref="sysTable" :cell-style="{'text-align':'left'}" :columns="columns"
                  :data="pageResult" :header-cell-style="{'text-align':'center'}" :height="400"
                  :highlightCurrentRow="true"
                  :show-operation="false" :showBatchDelete="false" :showPreview="false" :showOperationDel="false"
                  :stripe="true"
                  border @findPage="findPage" 
                  @handleCurrentChange="handleCurrentChange" >
      </orderTable>


      <el-dialog v-model="inspectionOrderItemDialogVisible" :close-on-click-modal="false"
                 :title="isInspectionOrderItemAddOperation?'新增点检项':'编辑点检项'"
                 destroy-on-close width="25%">
        <el-form ref="inspectionOrderItemDataForm" :model="inspectionOrderItemDataForm"
                 :rules="inspectionOrderItemDataFormRules" :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="inspectionOrderItemDataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item v-if="false" label="inspectionOrderMainId" prop="inspectionOrderMainId">
            <el-input v-model="inspectionOrderItemDataForm.inspectionOrderMainId" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="点检项" prop="checkItem">
                <el-input v-model="inspectionOrderItemDataForm.checkItem" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="点检项判断标准" prop="checkItemStandard">
                <el-input v-model="inspectionOrderItemDataForm.checkItemStandard" auto-complete="off"
                          clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="起始范围值" prop="minValue">
                <el-input-number v-model="inspectionOrderItemDataForm.minValue" auto-complete="off" clearable
                                 style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="截至范围值" prop="maxValue">
                <el-input-number v-model="inspectionOrderItemDataForm.maxValue" auto-complete="off" clearable
                                 style="width:100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancelInspectionOrderItem">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitInspectionOrderItem">提交
            </el-button>
          </slot>
        </div>
      </el-dialog>
      <el-dialog v-model="previewDialogVisible" destroy-on-close
                 title="图片预览" width="850px">
        <div class="block">
          <el-image :src="imagePreviewSrc" style="width: 800px; height: 600px"/>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import orderTable from "./orderTable";
import {
  exportInspectionOrderExcel,
  findInspectionOrderById,
  findInspectionOrderPage,
  handleAdd,
  handleBatchConfirm,
  handleUpdate,
  findInspectionOrderDetailPage
} from "@/api/wlg/equipment/inspectionOrder";
import {
  findMchNameList,
  findSpecListByMchName,
  findTypeVersionListByMchNameAndSpec,
  convertUser
} from "@/api/wlg/equipment/equipmentManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";
import {getDict, selectDictLabel} from "@/api/system/dictData";
import {findImageById} from "@/api/wlg/equipment/image";
import {getAllUser} from "@/api/system/user"

export default {
  name: "inspectionOrder",
  components: {orderTable},
  data() {
    return {
      size: "default",
      filters: {
        mchCode: '',
        mchName: "",
        spec: "",
        typeVersion: "",
        status: "",
        equipNumber: "",
        checkItem: "",
      },
      imagePreviewSrc: '',

      columns: [
        {prop: "orderNumber", label: "工单号", minWidth: 110},
        {prop: "mchCode", label: "设备编码", minWidth: 110},
        {prop: "mchName", label: "设备名称", minWidth: 150},
        {prop: "equipNumber", label: "设备编号", minWidth: 150},
        {prop: "spec", label: "规格", minWidth: 120},
        {prop: "typeVersion", label: "型号", minWidth: 150},
        {prop: "factoryNo", label: "出厂编码", minWidth: 130},
        {prop: "dutyPersonId", label: "接单人", minWidth: 150, formatter: this.userFormat},
        {prop: "status", label: "状态", minWidth: 100, formatter: this.statusFormat},
        {prop: "inspectionDate", label: "点检日期", minWidth: 100},
        {prop: "inspectionShift", label: "点检班次", minWidth: 100},
        {prop: "shiftStartTime", label: "班次开始时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "shiftEndTime", label: "班次结束时间", minWidth: 120, formatter: this.dateTimeFormat},

        {prop: "checkItem", label: "点检项", minWidth: 100},
        {prop: "checkItemStandard", label: "点检项判断标准", minWidth: 150},
        {prop: "minValue", label: "起始范围值", minWidth: 120},
        {prop: "maxValue", label: "截至范围值", minWidth: 120},
        {prop: "actualValue", label: "实际值", minWidth: 100},
        {prop: "isFinish", label: "是否完成", minWidth: 100, formatter: this.yesNoFormat},
        {prop: "checkResult", label: "点检结果", minWidth: 100},
        {prop: "isException", label: "是否存在异常", minWidth: 120, formatter: this.yesNoFormat},
        {prop: "isFault", label: "是否存在故障", minWidth: 120, formatter: this.yesNoFormat},
        {prop: "isRepair", label: "是否需要维修", minWidth: 120, formatter: this.yesNoFormat},
        {prop: "faultDesc", label: "故障描述", minWidth: 100},

        {prop: "faultPhoto", label: "故障照片", minWidth: 100},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      previewDialogVisible: false,

      editLoading: false,
      findLoading: false,
      findDetailLoading: false,
      comfirmLoading: false,
      exportLoading: false,


      mchNameOptions: [],
      specOptions: [],
      typeVersionOptions: [],
      yesNoOptions: [],
      userOptions: [],

      // 新增编辑界面数据
      dataForm: {
        id: 0,
        mchName: '',
        spec: "",
        typeVersion: "",
      },
      inspectionOrderItemDataForm: {
        inspectionOrderMainId: null,
        id: 0,
        checkItem: '',
        checkItemStandard: '',
        minValue: null,
        maxValue: null,
      },
      orderStatusOptions: [],

    };
  },
  mounted() {
    findMchNameList().then(response => {
      if (response.data.data.length > 0) {
        this.mchNameOptions = response.data.data
      }
    }),
    getDict("wlg_em_inspection_order_status").then(response => {
      this.orderStatusOptions = response.data.data
    })
    getDict("wlg_em_yes_no").then(response => {
      this.yesNoOptions = response.data.data
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
      this.pageRequest.status = this.filters.status;
      this.pageRequest.equipNumber = this.filters.equipNumber;
      this.pageRequest.checkItem = this.filters.checkItem;
      this.findLoading = true;
      findInspectionOrderDetailPage(this.pageRequest)
          .then((res) => {
            this.findLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },
    findInspectionOrderDetail: function (id) {
      this.findDetailLoading = true;
      findInspectionOrderById(id)
          .then((res) => {
            this.findDetailLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              if (responseData.data != null) {
                this.inspectionOrderItemTableData = responseData.data.inspectionOrderItemList;
              }
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
      pageRequest.checkItem = this.filters.checkItem;

      this.exportLoading = true;
      exportInspectionOrderExcel(pageRequest).then(res => {
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
    handlePreview: function (index, row) {
      let id = row.faultImageId;
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
   


    statusFormat: function (row) {
      return selectDictLabel(this.orderStatusOptions, row.status);
    },
    yesNoFormat: function (row, column,cellValue) {
      return selectDictLabel(this.yesNoOptions, cellValue);
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
    userFormat: function (row, column, cellValue) {
      return convertUser(this.userOptions, cellValue)
    },
  },
};
</script>
