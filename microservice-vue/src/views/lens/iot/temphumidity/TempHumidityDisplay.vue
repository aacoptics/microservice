<template>
  <div class="aac-container">
    <div style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size">
        <el-form-item label="楼栋" prop="building_no" label-width="100px">
              <el-select v-model="filters.building_no" clearable filterable placeholder=""
                          style="width:90%">
                <el-option key="3" value="3">3号楼</el-option>
              </el-select>
        </el-form-item>
        <el-form-item label="楼层" prop="floor_no" label-width="100px">
              <el-select v-model="filters.floor_no" clearable filterable placeholder=""
                          style="width:90%">
                <el-option key="1" value="1">1楼</el-option>
                <el-option key="2" value="2">2楼</el-option>
                <el-option key="3" value="3">3楼</el-option>
                <el-option key="4" value="4">4楼</el-option>
                <el-option key="5" value="5">5楼</el-option>
                <el-option key="6" value="6">6楼</el-option>
                <el-option key="7" value="7">7楼</el-option>
              </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="filters.startDate" type="date" placeholder="" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="filters.endDate" type="date" placeholder="" value-format="YYYY-MM-DD" auto-complete="off"></el-date-picker>
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
        <el-table-column prop="seq" label="序号" width="80px" align="center"></el-table-column>
        <el-table-column prop="buildingNo" label="楼栋" width="100px" align="center"></el-table-column>
        <el-table-column prop="floorNo" label="楼层" width="100px" align="center"></el-table-column>
        <el-table-column prop="workDate" label="日期" width="100px" align="center"></el-table-column>
        <el-table-column prop="temp1" label="温度值1(1:30)" width="150px" align="center"></el-table-column>
        <el-table-column prop="temp2" label="温度值2(7:30)" width="150px" align="center"></el-table-column>
        <el-table-column prop="temp3" label="温度值3(13:30)" width="150px" align="center"></el-table-column>
        <el-table-column prop="temp4" label="温度值4(19:30)" width="150px" align="center"></el-table-column>
        <el-table-column prop="humidity1" label="湿度值1(1:30)" width="150px" align="center"></el-table-column>
        <el-table-column prop="humidity2" label="湿度值2(7:30)" width="150px" align="center"></el-table-column>
        <el-table-column prop="humidity3" label="湿度值3(13:30)" width="150px" align="center"></el-table-column>
        <el-table-column prop="humidity4" label="湿度值4(19:30)" width="150px" align="center"></el-table-column>
        <el-table-column prop="remark" label="备注" width="100px" align="center"></el-table-column>
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
import{getTemphumidityInfoDisplay} from "@/api/lens/iot/temphumidity";
export default {
  name: "TempHumidityDisplay",
  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      filters: {
        building_no: '',
        floor_no: '',
        startDate: '',
        endDate: ''
      },
      size: 'default',
    }
  },
  methods: {
    getData() {

      console.log(this.filters.building_no);
      console.log(this.filters.floor_no);
      console.log(this.filters.startDate);
      console.log(this.filters.endDate);

      if(this.filters.building_no == '') {
        this.$message.warning('楼栋不能为空');
        return
      }
      if(this.filters.floor_no == '') {
        this.$message.warning('楼层不能为空');
        return
      }
      if(this.filters.startDate == '') {
        this.$message.warning('开始日期不能为空');
        return
      }
      if(this.filters.endDate == '') {
        this.$message.warning('结束日期不能为空');
        return
      }

      getTemphumidityInfoDisplay(this.filters.building_no, this.filters.floor_no
                               , this.filters.startDate, this.filters.endDate).then((response) => {
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