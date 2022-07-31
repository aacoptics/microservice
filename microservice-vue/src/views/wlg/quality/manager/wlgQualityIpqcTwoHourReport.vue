<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="项目" prop="projectName">
                <el-input v-model="filters.projectName" clearable placeholder="项目"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="模具" prop="moldName">
                <el-input v-model="filters.moldName" clearable placeholder="模具"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="日期" prop="currentDate">
                <el-date-picker v-model="filters.currentDate" auto-complete="off"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
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
                :height="400" :highlightCurrentRow="true" :show-operation="false" :showBatchDelete="false"
                :pageSize="100000000" :pageSizes="[100000000]"
                :showPagination="false"
                :stripe="false" @findPage="findPage">
      </SysTable>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {date2str} from "@/utils/commonUtils";
import {qualityIpqcTwoHourReport} from "@/api/wlg/manager/wlgQualityIpqcTwoHourReport";

export default {
  name: "wlgQualityIpqcTwoHourReport",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        projectName: "",
        moldName: "",
        currentDate: date2str(new Date().setDate(new Date().getDate() - 1)),
      },
      columns: [],
      pageRequest: {current: 1, size: 100000000},
      pageResult: {},
      excelUploadDialogVisible: false,
      exportLoading: false,
      exportReportLoading: false
    };
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.projectName = this.filters.projectName;
      this.pageRequest.moldName = this.filters.moldName;
      this.pageRequest.currentDate = this.filters.endOqcTime != null ? date2str(this.filters.endOqcTime) : null;

      qualityIpqcTwoHourReport(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.columns = responseData.data.sysTableColumnDTOs;
              this.pageResult = responseData.data.dataRecords;
            }
          })
          .then(data != null ? data.callback : "");
    },
  },
};
</script>
