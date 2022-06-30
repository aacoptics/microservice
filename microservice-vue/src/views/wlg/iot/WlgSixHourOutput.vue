<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
            <el-form-item label="机台号" prop="machineName">
              <el-select v-model="machineNames"
                         placeholder="请选择机台号"
                         multiple
                         collapse-tags
                         :size="size">
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
                  type="daterange"
                  :shortcuts="shortcuts"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  :size="size">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary"
                         :loading="selectLoading"
                         @click="getByDateAndMachineNames">查询
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                </template>
              </el-button>
            </el-form-item>
          <el-form-item v-show="this.tableData.length > 0">
            <el-button type="success"
                       :loading="selectLoading"
                       @click="updateOutPutInfo">更新
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>

      </div>
      <el-table :data="tableData" border style="width: 100%" :size="size" stripe>
        <el-table-column fixed prop="startTime" label="日期" width="80" :formatter="this.dateFormat"/>
        <el-table-column prop="machineName" label="机台号" width="80" />
        <el-table-column prop="materialName" label="材料号" width="100" />
        <el-table-column prop="projectName" label="项目" width="100" />
        <el-table-column prop="modelName" label="模具" width="60" />
        <el-table-column prop="cycleName" label="周期" width="60" />
        <el-table-column prop="periodName" label="阶段" width="60" />
        <el-table-column prop="startTime" label="开始时间" width="80" :formatter="this.dateTimeFormat"/>
        <el-table-column prop="endTime" label="结束时间" width="80" :formatter="this.dateTimeFormat"/>
        <el-table-column prop="avgCycle" label="平均周期" width="80" />
        <el-table-column prop="inputQty" label="投入数" width="60" />
        <el-table-column prop="startWaferId" label="起始模次号" width="80" />
        <el-table-column prop="endWaferId" label="截止模次号" width="80" />

        <el-table-column prop="brokenOk" label="碎裂可流转" width="130">
          <template v-slot="scope">
            <el-input-number :size="size" controls-position="right" v-model="scope.row.brokenOk" v-show="scope.row.iseditor" />
            <span v-show="!scope.row.iseditor">{{scope.row.brokenOk}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="brokenNg" label="碎裂不可流转" width="130">
          <template v-slot="scope">
            <el-input-number :size="size" controls-position="right" v-model="scope.row.brokenNg" @change="scope.row.outputQty = scope.row.inputQty - scope.row.brokenNg" v-show="scope.row.iseditor" />
            <span v-show="!scope.row.iseditor">{{scope.row.brokenNg}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="outputQty" label="产出" width="80">
          <template v-slot="scope">
            <span>{{scope.row.inputQty - scope.row.brokenNg}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
          <template v-slot="scope">
            <el-button v-if="!scope.row.iseditor" :size="size" type="warning" @click="edit(scope.row, scope)">编辑</el-button>
            <el-button v-else :size="size" type="success" @click="save(scope.row)">确认</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>

import {
  getByDateAndMachineName,
  getMachineName,
  updateOutPutInfo
} from "@/api/wlg/iot/moldingMachineParamData";

export default {
  name: "WlgSixHourOutput",
  data() {
    return {
      tableData: [],
      machineNames:[],
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
    save(row) {
      row.iseditor = false;
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
    getByDateAndMachineNames(){
      this.selectLoading = true
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      getByDateAndMachineName({startTime: startTime, endTime: endTime, machineNames: this.machineNames}).then((response) => {
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

    updateOutPutInfo(){
      this.selectLoading = true
      updateOutPutInfo(this.tableData).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.$message.success('更新成功')
        }else{
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
