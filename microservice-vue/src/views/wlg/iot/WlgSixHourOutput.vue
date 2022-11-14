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
                format="YYYY-MM-DD HH:00"
                range-separator="至"
                start-placeholder="开始日期"
                type="datetimerange">
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
      <el-table id="inputReportTable" :data="tableData" :size="size" :summary-method="getSummaries" show-summary stripe
                style="width: 100%">
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
        <el-table-column :filter-method="filterProjectName" :filters="projectName"
                         label="项目"
                         prop="projectName" width="80"/>
        <el-table-column :filter-method="filterModelName" :filters="moldName"
                         filter-placement="bottom-end"
                         label="模具" prop="modelName" width="55"/>
        <el-table-column label="周期" prop="cycleName" width="45"/>
        <el-table-column label="阶段" prop="periodName" width="45"/>
        <el-table-column label="标准周期" prop="standardCt" width="65"/>
        <el-table-column label="平均周期" prop="avgCycle" width="65"/>

        <el-table-column label="起始batch ID" prop="startWaferId" width="90">
          <template v-slot="scope">
            <el-input v-show="scope.row.iseditor" v-model="scope.row.startWaferId"
                              style="width: 70px" size="small"/>
            <span v-show="!scope.row.iseditor">{{ scope.row.startWaferId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="终止batch ID" prop="endWaferId" width="90">
          <template v-slot="scope">
            <el-input v-show="scope.row.iseditor" v-model="scope.row.endWaferId"
                      style="width: 70px"  size="small"/>
            <span v-show="!scope.row.iseditor">{{ scope.row.endWaferId }}</span>
          </template>
        </el-table-column>

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
        <el-table-column :filter-method="filterOperatorName" :filters="operatorName"
                         filter-placement="bottom-end"
                         label="更新人" prop="updateUser" width="80"/>
        <el-table-column label="更新时间" prop="updateTime" width="100">
          <template v-slot="scope">
            <span>{{ this.$moment(scope.row.updateTime).format('YY/MM/DD HH:mm') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="异常说明" prop="abnormalReason" width="180"/>
        <el-table-column fixed="right" label="操作" width="140">
          <template v-slot="scope">
            <span v-if="scope.row.inputQty > 0">
            <el-button v-if="!scope.row.iseditor" :size="size" type="warning"
                       @click="edit(scope.row, scope)">编辑</el-button>
            <el-button v-else :size="size" type="success" @click="updateOutPutInfo(scope.row)">确认</el-button>
            </span>
            <el-button :size="size" style="margin-left: 2px" type="danger"
                       @click="onAddReasonClick(scope.row)">异常说明
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog v-model="reasonDialog" :close-on-click-modal="false" title="异常原因填写">
        <el-input v-model="currentRow.abnormalReason" auto-complete="off" placeholder="请填写异常原因"
                  style="margin-top: 20px"></el-input>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button type="primary" @click="updateOutPutInfo(this.currentRow)">提交</el-button>
          </slot>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

import {getByDateAndMachineName, getMachineName, updateOutPutInfo} from "@/api/wlg/iot/moldingMachineParamData";
import XLSX from "xlsx";
import FileSaver from 'file-saver'
import {getUserDetail, getUsername} from "@/utils/auth";

export default {
  name: "WlgSixHourOutput",
  data() {
    return {
      reasonDialog: false,
      currentRow: {},
      modelName: '',
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
  computed: {
    userRealName() {
      let userName = getUsername()
      let userDetail = getUserDetail()
      let realName = userDetail && userDetail.name ? userDetail.name : userName;
      return realName ? realName : this.name;
    },
    moldName() {
      const resArray = []
      const res = []
      this.tableData.forEach(item => {
        if (resArray.indexOf(item.modelName) === -1) {
          resArray.push(item.modelName)
          res.push({text: item.modelName, value: item.modelName})
        }
      })
      return res
    },
    projectName() {
      const resArray = []
      const res = []
      this.tableData.forEach(item => {
        if (resArray.indexOf(item.projectName) === -1) {
          resArray.push(item.projectName)
          res.push({text: item.projectName, value: item.projectName})
        }
      })
      return res
    },
    operatorName() {
      const resArray = []
      const res = []
      this.tableData.forEach(item => {
        if (resArray.indexOf(item.updateUser) === -1) {
          resArray.push(item.updateUser)
          res.push({text: item.updateUser, value: item.updateUser})
        }
      })
      return res
    }
  },
  methods: {
    getSummaries(param) {
      const {columns, data} = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        const values = data.map((item) => Number(item[column.property]))
        if (column.property === 'inputQty' || column.property === 'outputQty' || column.property === 'brokenNg' || column.property === 'brokenOk') {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!Number.isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    onAddReasonClick(item) {
      this.currentRow = Object.assign({}, item)
      this.reasonDialog = true
    },
    filterModelName(value, row) {
      return row.modelName === value
    },

    filterProjectName(value, row) {
      return row.projectName === value
    },
    filterOperatorName(value, row) {
      return row.updateUser === value
    },
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
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:00:00');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:00:00');
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
      row.updateUser = this.userRealName;
      updateOutPutInfo(row).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.$message.success('更新成功')
          this.getByDateAndMachineNames()
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
