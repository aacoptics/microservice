<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item label="事业部" prop="businessDivision">
            <el-select v-model="filters.businessDivision" clearable filterable placeholder="请选择事业部" style="width:100%" @change="findProductLineByBusinessDivision">
                  <el-option
                      v-for="item in businessDivisionOptions"
                      :key="item"
                    :label="item"
                    :value="item"
                  >
                  </el-option>
                </el-select>
          </el-form-item>
          <el-form-item label="产品线" prop="productLine">
            <el-select v-model="filters.productLine" clearable filterable placeholder="请选择产品线" style="width:100%" multiple collapse-tags
              collapse-tags-tooltip>
                  <el-option
                      v-for="item in productLineOptions"
                      :key="item"
                    :label="item"
                    :value="item"
                  >
                  </el-option>
                </el-select>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button type="primary" @click="findPage()" :loading="queryLoading">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>


          </el-form-item>
        </el-form>
      </div>
      <QueryAllTable id="condDataTable" ref="queryAllTable" :columns="researchBudgetColumns" :data="researchBudgetDataResult"
                     :height="550" :highlightCurrentRow="true"  v-loading="queryLoading" border
                     :stripe="true">
            </QueryAllTable>


    </div>
  </div>
</template>

<script>
import QueryAllTable from "@/components/QueryAllTable";

import {findResearchBudgetPage} from "@/api/finance/budget/researchBudget";
import {getResponseDataMessage} from "@/utils/commonUtils";
import {findAllBusinessDivision, findProductLineByBusinessDivision} from "@/api/finance/budget/businessDivisionProductLine";


export default {
  name: "researchBudget",
  components: {QueryAllTable},
  data() {
    return {
      size: "default",
      filters: {
        businessDivision: '',
        productLine: [],
      },
      businessDivisionOptions: [],
      productLineOptions: [],
      researchBudgetColumns: [
        // {prop: "seq", label: "序号", minWidth: 80},
        // {prop: "businessDivision", label: "事业部", minWidth: 150},
        // {prop: "productLine", label: "产品线", minWidth: 120},
        // {prop: "dataVersion", label: "数据版本", minWidth: 150},
        {prop: "itemSeq", label: "科目序号", minWidth: 120},
        {prop: "rowNo", label: "行项目号", minWidth: 120},
        {prop: "costItem", label: "费用项目", minWidth: 260},
        {prop: "costType", label: "固定/变动费用", minWidth: 150},
        {prop: "unit", label: "单位", minWidth: 120},
        {prop: "202201", label: "202201", minWidth: 120},
        {prop: "202202", label: "202202", minWidth: 120},
        {prop: "202203", label: "202203", minWidth: 120},
        {prop: "202204", label: "202204", minWidth: 120},
        {prop: "202205", label: "202205", minWidth: 120},
        {prop: "202206", label: "202206", minWidth: 120},
        {prop: "202207", label: "202207", minWidth: 120},
        {prop: "202208", label: "202208", minWidth: 120},
        {prop: "202209", label: "202209", minWidth: 120},
        {prop: "202210", label: "202210", minWidth: 120},
        {prop: "202211", label: "202211", minWidth: 120},
        {prop: "202212", label: "202212", minWidth: 120},
        {prop: "2022Q1", label: "2022Q1", minWidth: 120},
        {prop: "2022Q2", label: "2022Q2", minWidth: 120},
        {prop: "2022Q3", label: "2022Q3", minWidth: 120},
        {prop: "2022Q4", label: "2022Q4", minWidth: 120},
        {prop: "2022全年", label: "2022全年", minWidth: 120},
      ],
      researchBudgetDataResult: {},
      queryLoading:false,

    };
  },
  mounted() {
    this.findAllBusinessDivision();
  },
  methods: {
    // 获取分页数据
    findPage: function () {
      let params = {};
      params.businessDivision = this.filters.businessDivision;
      params.productLineList = this.filters.productLine;
      
      this.researchBudgetDataResult = {};
      this.queryLoading = true;
      findResearchBudgetPage(params)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.researchBudgetColumns = responseData.data.columns;
              this.researchBudgetDataResult.records = responseData.data.data;
            }
            else
            {
              this.$message({
                    message:
                        "查询失败 " + getResponseDataMessage(responseData),
                    type: "error",
                  });
            }
            this.queryLoading = false;
          });
    },

    findAllBusinessDivision: function () {
      findAllBusinessDivision()
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.businessDivisionOptions = responseData.data;
            }
          })
    },

    findProductLineByBusinessDivision: function (val) {
      if(val == '')
      {
        this.productLineOptions = [];
        return;
      }
      let params = {};
      params.businessDivision = val;
      findProductLineByBusinessDivision(params)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.productLineOptions = responseData.data;
            }
          })
    },
    
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm:ss");
    },
    
  },
};
</script>
