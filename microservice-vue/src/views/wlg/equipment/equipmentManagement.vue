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
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult" 
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false" border
                :stripe="true" :show-operation="false"
                @findPage="findPage">
      </SysTable>


    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {findEquipmentManagementPage} from "@/api/wlg/equipment/equipmentManagement";

export default {
  name: "equipmentManagement",
  components: {SysTable},
  data() {
    return {
      size: "default",
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
      },
      columns: [
        {prop: "mchCode", label: "资产编码", minWidth: 110},
        {prop: "mchName", label: "资产名称", minWidth: 100},
        {prop: "spec", label: "规格", minWidth: 100},
        {prop: "typeVersion", label: "型号", minWidth: 100},
        {prop: "status", label: "设备状态", minWidth: 100},
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
        {prop: "assetManagerId", label: "资产管理员", minWidth: 100},
        {prop: "mchManagerId", label: "设备管理员", minWidth: 100},
        {prop: "dutyPersonId", label: "责任人", minWidth: 100},
        {prop: "deptManagerId", label: "部门经理", minWidth: 100},
        {prop: "deptDirectorId", label: "部门总监", minWidth: 100},
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


    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
  },
};
</script>
