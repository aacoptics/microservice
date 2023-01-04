<template>
  <div class="aac-container">
    <div style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size" label-width="100px">
        <el-form-item label="楼栋" prop="buildingNo">
          <el-select v-model="param.buildingNo" clearable filterable placeholder="楼栋">
            <el-option v-for="buildingNoItem in buildingNoOptions"
                       :label="buildingNoItem"
                       :value="buildingNoItem"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="楼层" prop="floorNo">
          <el-select v-model="param.floorNo" clearable filterable placeholder="楼层">
            <el-option v-for="floorNoItem in floorNoOptions"
                        :label="floorNoItem"
                        :value="floorNoItem"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="配电间" prop="roomNo">
          <el-select v-model="param.roomNo" clearable filterable placeholder="配电间">
            <el-option v-for="roomNoItem in roomNoOptions"
                        :label="roomNoItem"
                        :value="roomNoItem"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <el-form :inline="true" :size="size" label-width="100px">
        <el-form-item label="电表" prop="meterNo">
          <el-select v-model="param.meterNo" clearable filterable placeholder="电表">
            <el-option v-for="meterNoItem in meterNoOptions"
                        :label="meterNoItem"
                        :value="meterNoItem"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="param.startDate" type="date" placeholder="开始日期" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="param.endDate" type="date" placeholder="开始日期" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
        </el-form-item>
      </el-form>
      <el-form :inline="true" :size="size">
        <el-form-item>
          <el-button :loading="queryLoading" type="primary" @click="getData">查询
            <template #icon>
              <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
            </template>
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div style="margin-top:10px">
      <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" style="width: 100%">
        <el-table-column prop="buildingNo" label="楼栋" width="100px" align="center"></el-table-column>
        <el-table-column prop="floorNo" label="楼层" width="100px" align="center"></el-table-column>
        <el-table-column prop="roomNo" label="配电间" width="100px" align="center"></el-table-column>
        <el-table-column prop="meterNo" label="电表" width="100px" align="center"></el-table-column>
        <el-table-column prop="workDate" label="日期" width="100px" align="center"></el-table-column>
        <el-table-column prop="powerTotalQty" label="用电量" width="100px" align="center"></el-table-column>
      </el-table>
    </div>

    <div style="margin-top:10px">
      <el-pagination @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="currentPage"
                     :page-sizes="[10,20,50,100]"
                     :page-size="pageSize"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="tableData.length"></el-pagination>
    </div>

    <div style="margin-top:10px" id="powerQtyPlot"></div>

  </div>
</template>

<script>
import {getElectricMeterInfo, getElectricMeterQueryDataList, getElectricMeterPowerQty} from "@/api/lens/iot/temphumidity";
import * as echarts from 'echarts';
export default {
  name: "ElectricMeterInfo",
  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      size: 'default',
      param: {
        buildingNo: '',
        floorNo: '',
        roomNo: '',
        meterNo: '',
        startDate: '',
        endDate: ''
      },
      buildingNoOptions: [],
      floorNoOptions: [],
      roomNoOptions: [],
      meterNoOptions: [],
      queryLoading: false,
      workDate: [],
      powerQty: [],
    }
  },
  methods: {
    getData() {
      if(this.param.buildingNo == '') {
        this.$message.warning('楼栋不能为空');
        return
      }
      if(this.param.startDate == '' || this.param.endDate == '') {
        this.$message.warning('日期不能为空');
        return
      }
      getElectricMeterInfo(this.param).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.tableData = responseData.data;
        }
      });

      getElectricMeterPowerQty(this.param.startDate, this.param.endDate).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          responseData.data.forEach(item => {
            this.workDate.push(item.workDate);
            this.powerQty.push(item.powerQty);
          })
          this.drawPowerQtyPlot();
        }
      });
    },
    drawPowerQtyPlot() {
      var chartDom = document.getElementById('powerQtyPlot');
      var powerQtyPlot = echarts.init(chartDom);
      var option;
      option = {
        title: {
          text: '用电量',
          textAlign: 'center',
          x: 'center',
          y: 'top'
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.workDate
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: this.powerQty,
            type: 'line'
          }
        ]
      };
      powerQtyPlot.setOption(option);
    },
    handleSizeChange(val) {
      this.currentPage = 1;
      this.pageSize = val;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
    }
  },
  mounted() {
    getElectricMeterQueryDataList('buildingNo').then(response => {
      this.buildingNoOptions = response.data
    })
    getElectricMeterQueryDataList('floorNo').then(response => {
      this.floorNoOptions = response.data
    })
    getElectricMeterQueryDataList('roomNo').then(response => {
      this.roomNoOptions = response.data
    })
    getElectricMeterQueryDataList('meterNo').then(response => {
      this.meterNoOptions = response.data
    })
  }
}
</script>

<style scoped>

</style>