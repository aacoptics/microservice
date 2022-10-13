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

import {getMachineEvents, getMachineName} from "@/api/wlg/iot/moldingMachineParamData";
import SysTable from "@/components/SysTable";

export default {
  name: "WlgMoldingEvent",
  components: {SysTable},
  data() {
    return {
      columns: [
        {prop: "machineName", label: "机台号", minWidth: 110},
        {prop: "eventInfo", label: "事件信息", minWidth: 100},
        {prop: "alarmInfo", label: "报警信息", minWidth: 120},
        {prop: "startTime", label: "开始时间", minWidth: 120},
        {prop: "endTime", label: "结束时间", minWidth: 100}
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
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      getMachineEvents(this.formParam.machineName, startTime, endTime, this.pageRequest.current, this.pageRequest.size).then((res) => {
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
  },
  mounted() {
    this.getMachineName();
  }
}
</script>
