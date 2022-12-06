<template>
  <div class="aac-container">
    <div style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size">
        <el-form-item label="日期" prop="date">
          <el-date-picker v-model="dateChosen" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
        </el-form-item>
      </el-form>
      <el-form :inline="true" :size="size">
        <el-form-item>
          <el-button type="primary" @click="getData">查询
            <template #icon>
              <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
            </template>
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div style="margin-top:10px">
      <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" style="width: 100%">
        <el-table-column prop="sensorNumber" label="传感器编号" width="100px" align="center"></el-table-column>
        <el-table-column prop="time" label="时间" width="100px" align="center"></el-table-column>
        <el-table-column prop="receiveDate" label="接收日期" width="100px" align="center"></el-table-column>
        <el-table-column prop="devTemplateUniqueId" label="模板唯一编号" width="150px" align="center"></el-table-column>
        <el-table-column prop="deviceId" label="设备编号" width="100px" align="center"></el-table-column>
        <el-table-column prop="thatMomentProgramVersionId" label="程序版本编号" width="150px" align="center"></el-table-column>
        <el-table-column prop="temperature" label="温度" width="100px" align="center"></el-table-column>
        <el-table-column prop="humidity" label="湿度" width="100px" align="center"></el-table-column>
        <el-table-column prop="dataColTime" label="数据收集时间" width="120px" align="center"></el-table-column>
        <el-table-column prop="clientId" label="客户端编号" width="100px" align="center"></el-table-column>
        <el-table-column prop="cncStatus" label="CNC状态" width="100px" align="center"></el-table-column>
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
  </div>
</template>

<script>
import{getTemphumidityInfoByDate} from "@/api/lens/iot/temphumidity";
export default {
  name: "TempHumidityInfo",
  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      dateChosen: '',
      size: 'default',
    }
  },
  methods: {
    getData() {
      console.log(this.dateChosen)
      if(this.dateChosen == '') {
        this.$message.warning('日期不能为空');
        return
      }
      getTemphumidityInfoByDate(this.dateChosen).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.tableData = responseData.data;
        }
      })
    },
    handleSizeChange(val) {
      this.currentPage = 1;
      this.pageSize = val;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
    }
  }
}
</script>

<style scoped>

</style>