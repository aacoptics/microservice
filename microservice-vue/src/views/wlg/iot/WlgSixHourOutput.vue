<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="机台号" prop="machineName">
            <el-select v-model="machineNames"
                       :size="size"
                       collapse-tags
                       multiple
                       placeholder="请选择机台号">
              <el-checkbox v-model="okAllChecked" @change='okSelectAll'>全选</el-checkbox>
              <el-option
                  v-for="item in machineNameArray"
                  :key="item.machineName"
                  :label="item.machineName"
                  :value="item.machineName"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="时间" prop="dateTimePicker">
            <el-date-picker
                v-model="dateTimePickerValue"
                :shortcuts="shortcuts"
                :size="size"
                end-placeholder="结束日期"
                range-separator="至"
                start-placeholder="开始日期"
                type="daterange">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button :loading="selectLoading"
                       type="primary"
                       @click="getByDateAndMachineNames">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="success"
                       @click="exportExcel('#inputReportTable', '投入产出报表.xlsx')">导出
              <template #icon>
                <font-awesome-icon :icon="['fas', 'cloud-arrow-down']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>

      </div>
      <el-table id="inputReportTable" :data="tableData" :size="size" stripe style="width: 100%">
        <el-table-column fixed="left" label="日期" prop="dateStr" width="70">
          <template v-slot="scope">
            <span style="font-weight: bold;color: black">{{
                this.$moment(scope.row.startTime).format("YY/MM/DD")
              }}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="left" label="时间" prop="timeStr" width="85">
          <template v-slot="scope">
            <span
                style="font-weight: bold;color: black">{{
                this.$moment(scope.row.startTime).format("HH:mm") + '-' + this.$moment(scope.row.endTime).format("HH:mm")
              }}</span>
          </template>
        </el-table-column>
        <el-table-column label="机台号" prop="machineName" width="70"/>
        <el-table-column label="材料号" prop="materialName" width="60"/>
        <el-table-column label="项目" prop="projectName" width="80"/>
        <el-table-column label="模具" prop="modelName" width="45"/>
        <el-table-column label="周期" prop="cycleName" width="45"/>
        <el-table-column label="阶段" prop="periodName" width="45"/>
        <el-table-column label="平均周期" prop="avgCycle" width="65"/>

        <el-table-column label="起始模次" prop="startWaferId" width="65"/>
        <el-table-column label="截止模次" prop="endWaferId" width="65"/>

        <el-table-column label="投入数" prop="inputQty" width="55">
          <template v-slot="scope">
            <el-tag style="width: 30px">{{ scope.row.inputQty }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="碎裂可流转" prop="brokenOk" width="90">
          <template v-slot="scope">
            <el-input-number v-show="scope.row.iseditor" v-model="scope.row.brokenOk"
                             :max="scope.row.inputQty - scope.row.brokenNg" :min="0"
                             :size="size" controls-position="right" style="width: 70px"/>
            <el-tag v-show="!scope.row.iseditor" style="width: 30px" type="warning">{{ scope.row.brokenOk }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="碎裂不可流转" prop="brokenNg" width="90">
          <template v-slot="scope">
            <el-input-number v-show="scope.row.iseditor" v-model="scope.row.brokenNg"
                             :max="scope.row.inputQty - scope.row.brokenOk" :min="0"
                             :size="size" controls-position="right"
                             style="width: 70px"
                             @change="scope.row.outputQty = scope.row.inputQty - scope.row.brokenNg"/>
            <el-tag v-show="!scope.row.iseditor" style="width: 30px" type="danger">{{ scope.row.brokenNg }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="产出数" prop="outputQty" width="55">
          <template v-slot="scope">
            <el-tag style="width: 30px" type="success">{{ scope.row.inputQty - scope.row.brokenNg }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="80">
          <template v-slot="scope">
            <span v-if="scope.row.inputQty > 0">
            <el-button v-if="!scope.row.iseditor" :size="size" type="warning"
                       @click="edit(scope.row, scope)">编辑</el-button>
            <el-button v-else :size="size" type="success" @click="updateOutPutInfo(scope.row)">确认</el-button>
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>

import {getByDateAndMachineName, getMachineName, updateOutPutInfo} from "@/api/wlg/iot/moldingMachineParamData";
import XLSX from "xlsx";
import FileSaver from 'file-saver'

export default {
  name: "WlgSixHourOutput",
  data() {
    return {
      tableData: [],
      machineNames: [],
      okAllChecked: false,
      size: 'small',
      machineNameArray: [],
      selectLoading: false,
      dateTimePickerValue: [],
      shortcuts: [{
        text: '最近一天',
        value: (() => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24);
          return [start, end]
        })()
      },
        {
          text: '最近一周',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            return [start, end]
          },
        },
        {
          text: '最近一个月',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            return [start, end]
          },
        }],
    }
  },
  methods: {
    exportExcel(tableId, excelFileName) {
      const wb = XLSX.utils.table_to_book(document.querySelector(tableId));
      const wbOut = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'});
      try {
        FileSaver.saveAs(new Blob([wbOut], {type: 'application/octet-stream'}), excelFileName)
      } catch (e) {
        if (typeof console !== 'undefined') console.log(e, wbOut)
      }
      return wbOut
    },
    dateTimeFormat: function (row, column) {
      if (row[column.property] == null) return '-';
      return this.$moment(row[column.property]).format("HH:mm");
    },
    dateFormat: function (row, column) {
      if (row[column.property] == null) return '-';
      return this.$moment(row[column.property]).format("YY/MM/DD");
    },
    edit(row) {
      row.iseditor = true;
    },
    okSelectAll() {
      this.machineNames = []
      if (this.okAllChecked) {
        this.machineNameArray.map(item => {
          this.machineNames.push(item.machineName)
        })
      } else {
        this.machineNames = []
      }
    },
    getMachineName() {
      getMachineName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameArray = responseData.data
        }
      })
    },
    getByDateAndMachineNames() {
      this.selectLoading = true
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD 00:00:00');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD 00:00:00');
      getByDateAndMachineName({
        startTime: startTime,
        endTime: endTime,
        machineNames: this.machineNames
      }).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.tableData = responseData.data
        }
        this.selectLoading = false
      }).catch((err) => {
        this.$message.error(err.message)
        this.selectLoading = false
      })
    },

    updateOutPutInfo(row) {
      this.selectLoading = true
      updateOutPutInfo(row).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.$message.success('更新成功')
          row.iseditor = false;
        } else {
          this.$message.error('更新失败，请联系IT')
        }
        this.selectLoading = false
      }).catch((err) => {
        this.$message.error('更新失败，请联系IT')
        this.selectLoading = false
      })
    },
  },
  mounted() {
    this.getMachineName();
  }
}
</script>
