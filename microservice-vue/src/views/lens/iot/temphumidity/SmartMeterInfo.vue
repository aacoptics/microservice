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
        <el-table-column prop="deviceId" label="设备编号" width="100px" align="center"></el-table-column>
        <el-table-column prop="time" label="时间" width="100px" align="center"></el-table-column>
        <el-table-column prop="receiveDate" label="接收日期" width="100px" align="center"></el-table-column>
        <el-table-column prop="devTemplateUniqueId" label="模板唯一编号" width="150px" align="center"></el-table-column>
        <el-table-column prop="thatMomentProgramVersionId" label="程序版本编号" width="150px" align="center"></el-table-column>
        <el-table-column prop="programVersion" label="程序版本" width="100px" align="center"></el-table-column>
        <el-table-column prop="clientId" label="客户端编号" width="100px" align="center"></el-table-column>
        <el-table-column prop="dataColTime" label="数据收集时间" width="120px" align="center"></el-table-column>
        <el-table-column prop="cncStatus" label="CNC状态" width="100px" align="center"></el-table-column>
        <el-table-column prop="uan" label="Uan" align="center"></el-table-column>
        <el-table-column prop="ubn" label="Ubn" align="center"></el-table-column>
        <el-table-column prop="ucn" label="Ucn" align="center"></el-table-column>
        <el-table-column prop="uab" label="Uab" align="center"></el-table-column>
        <el-table-column prop="ubc" label="Ubc" align="center"></el-table-column>
        <el-table-column prop="uca" label="Uca" align="center"></el-table-column>
        <el-table-column prop="ia" label="Ia" align="center"></el-table-column>
        <el-table-column prop="ib" label="Ib" align="center"></el-table-column>
        <el-table-column prop="ic" label="Ic" align="center"></el-table-column>
        <el-table-column prop="pa" label="Pa" align="center"></el-table-column>
        <el-table-column prop="pb" label="Pb" align="center"></el-table-column>
        <el-table-column prop="pc" label="Pc" align="center"></el-table-column>
        <el-table-column prop="pTotal" label="Ptotal" align="center"></el-table-column>
        <el-table-column prop="qa" label="Qa" align="center"></el-table-column>
        <el-table-column prop="qb" label="Qb" align="center"></el-table-column>
        <el-table-column prop="qc" label="Qc" align="center"></el-table-column>
        <el-table-column prop="qTotal" label="Qtotal" align="center"></el-table-column>
        <el-table-column prop="sa" label="Sa" align="center"></el-table-column>
        <el-table-column prop="sb" label="Sb" align="center"></el-table-column>
        <el-table-column prop="sc" label="Sc" align="center"></el-table-column>
        <el-table-column prop="sTotal" label="Stotal" align="center"></el-table-column>
        <el-table-column prop="fa" label="Fa" align="center"></el-table-column>
        <el-table-column prop="fb" label="Fb" align="center"></el-table-column>
        <el-table-column prop="fc" label="Fc" align="center"></el-table-column>
        <el-table-column prop="fTotal" label="Ftotal" align="center"></el-table-column>
        <el-table-column prop="epi" label="EPI" align="center"></el-table-column>
        <el-table-column prop="epe" label="EPE" align="center"></el-table-column>
        <el-table-column prop="eql" label="EQL" align="center"></el-table-column>
        <el-table-column prop="eqc" label="EQC" align="center"></el-table-column>
        <el-table-column prop="currentTr" label="Current_tr" width="100px" align="center"></el-table-column>
        <el-table-column prop="voltageTr" label="Voltage_tr" width="100px" align="center"></el-table-column>
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
import {getSmartMeterInfoByDate} from "@/api/lens/iot/temphumidity";
export default {
  name: "SmartMeterInfo",
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
      getSmartMeterInfoByDate(this.dateChosen).then((response) => {
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