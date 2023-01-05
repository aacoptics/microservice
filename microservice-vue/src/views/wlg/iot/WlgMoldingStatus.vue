<template>
  <div>
    <div class="aac-container">
      <el-row>
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form :size="size" label-width="100px">
            <el-row>
              <el-form-item label="机台号" prop="machineName">

                <el-select v-model="formParam.machineName"
                           :size="size"
                           placeholder="请选择机台号">
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
                    type="datetimerange">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="findPage(null)">查询
                  <template #icon>
                    <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                  </template>
                </el-button>
              </el-form-item>
            </el-row>
          </el-form>
        </div>

      </el-row>
      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :show-operation="false"
                :showBatchDelete="false" :stripe="false"
                @findPage="findPage">
      </SysTable>

    </div>
  </div>
</template>

<script>

import {getMachineStatus, getMachineName} from "@/api/wlg/iot/moldingMachineParamData";
import SysTable from "@/components/SysTable";

export default {
  name: "WlgMoldingEvent",
  components: {SysTable},
  data() {
    return {
      columns: [
        {prop: "machineName", label: "机台号", minWidth: 110},
        {prop: "alarmInfo", label: "状态", minWidth: 100},
        {prop: "startTime", label: "开始时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "endTime", label: "结束时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "duration", label: "持续时间", minWidth: 120, formatter: this.formatSeconds}
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      size: 'small',
      machineNameArray: [],
      formParam: {
        machineName: ''
      },
      dateTimePickerValue: [],
      shortcuts: [{
        text: '最近一天',
        value: (() => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24);
          return [start, end]
        })()
      }],
    }
  },
  methods: {
    formatSeconds: function (row, column) {
      //  秒
      let second = parseInt(row[column.property])
      //  分
      let minute = 0
      //  小时
      let hour = 0
      //  天
      let day = 0
      //  如果秒数大于60，将秒数转换成整数
      if (second > 60) {
        //  获取分钟，除以60取整数，得到整数分钟
        minute = parseInt(second / 60)
        //  获取秒数，秒数取佘，得到整数秒数
        second = parseInt(second % 60)
        //  如果分钟大于60，将分钟转换成小时
        if (minute > 60) {
          //  获取小时，获取分钟除以60，得到整数小时
          hour = parseInt(minute / 60)
          //  获取小时后取佘的分，获取分钟除以60取佘的分
          minute = parseInt(minute % 60)
          if (hour > 23) {
            //  获取天数，获取小时除以24，得到整天数
            day = parseInt(hour / 24)
            //  获取天数后取余的小时，获取小时除以24取余的小时
            hour = parseInt(hour % 24)
          }
        }
      }

      let result = '' + parseInt(second) + '秒'
      if (minute > 0) {
        result = '' + parseInt(minute) + '分' + result
      }
      if (hour > 0) {
        result = '' + parseInt(hour) + '小时' + result
      }
      //  if (day > 0) {
      //    result = '' + parseInt(day) + '天' + result
      //  }
      return result
    },
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      getMachineStatus(this.formParam.machineName, startTime, endTime, this.pageRequest.current, this.pageRequest.size).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },
    getMachineName() {
      getMachineName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameArray = responseData.data
        }
      })
    },
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm:ss')
    },
  },
  mounted() {
    this.getMachineName();
  }
}
</script>
