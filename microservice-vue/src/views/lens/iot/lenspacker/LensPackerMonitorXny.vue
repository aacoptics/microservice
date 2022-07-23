<template>
  <div>
    <div class="aac-container">
      <div style="margin-bottom: 20px">
        <div v-for="(item, index) of statusRadio" :key="index" :style="'background-color:' + getStatusRadioColor(item) + ';text-align:center'"
             class="status_radio_type"
             @click="onStatusRadioClick(item)">
          {{ item + '(' + this.statusCount[item] + ')' }}
        </div>
      </div>
      <el-card v-for="(singleMachineInfo, index) of LensPackerMachineInfo" :key="index"
               :body-style="{ padding: '0px', height:'120px'}"
               class="lenspacker_card_type"
               shadow="hover"
               style="cursor: pointer">

        <el-row>
          <el-col>
            <div style="font-weight: bold">
              <el-row
                  style="text-align: center;height:30px; font-weight: bold;font-size: 16px;border-bottom: 1px solid cornflowerblue">
                <el-col :span="24">
                  <p style="text-align: center;font-weight: bold;color: #008000;font-size: 24px">
                    {{ singleMachineInfo.machineNo.substr(4) }}</p>
                </el-col>
                <!--                <el-col :span="16">-->
                <!--                    {{ getMachineStatus(singleMachineInfo.status) }}-->
                <!--                  <div :style="'background-color:' + statusType[getMachineStatus(singleMachineInfo.status)] + ';height:30px;line-height:30px'">-->
                <!--                  </div>-->
                <!--                </el-col>-->
              </el-row>
              <el-row style="text-align: center;height:30px; font-weight: bold;font-size: 16px;">
                <el-col :span="24">
                  <!--                  <div v-if="singleMachineInfo.status === 2" :style="'background-color:' + statusType[getMachineStatus(singleMachineInfo.status)] + ';height:30px;line-height:30px'">-->
                  <!--                    {{ getMachineStatus(singleMachineInfo.status) + ':' + singleMachineInfo.alarmInfo }}-->
                  <!--                  </div>-->
                  <div v-if="singleMachineInfo.status === 2"
                       :style="'background-color:' + statusType[getMachineStatus(singleMachineInfo.status)] + ';height:30px;line-height:30px'">

                    {{ singleMachineInfo.alarmInfo }}
                  </div>
                  <div v-else
                       :style="'background-color:' + statusType[getMachineStatus(singleMachineInfo.status)] + ';height:30px;line-height:30px'">
                    {{ getMachineStatus(singleMachineInfo.status) }}
                  </div>
                </el-col>
              </el-row>
              <el-row style="height: 30px;line-height: 30px">
                <el-col :span="12">产能：{{ singleMachineInfo.outputQty }}</el-col>
                <el-col :span="12">节拍：{{ singleMachineInfo.machineCT.toFixed(2) }}</el-col>
              </el-row>
              <el-row style="height: 30px;line-height: 30px">
                <el-col v-if="singleMachineInfo.status !== 0" :span="12">
                  类型：{{ singleMachineInfo.cavityNums === 24 ? 24 : 16 }}穴
                </el-col>
                <el-col v-else :span="12">类型：</el-col>
                <el-col :span="12">握手信号：{{ singleMachineInfo.isComplete }}</el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import {getMachineAlarms, getMachineStatus} from "@/api/lens/iot/lenspackerXny";

export default {
  name: "CoatingMachineMonitor",
  created() {
    this.getLensPackerStatus()
    this.timer = setInterval(() => {
      this.getLensPackerStatus()
    }, 10000)
  },
  computed: {
    LensPackerMachineInfo() {
      const pages = []
      this.setDefaultCount()
      const position = this.$route.query.position;
      if (this.$route.params.status) {
        this.setStatusRadioValue(this.$route.params.status);
        this.refreshPage()
      }
      this.LensPackerInfoList.forEach((item) => {
        const statusName = this.getMachineStatus(item.status)
        if (item.machineNo.indexOf(position) === 0) {
          if (this.statusRadioValue.indexOf(statusName) > -1) {
            pages.push(item)
          }
          this.statusCount[statusName]++
        }
      })
      return pages
    }
  },
  methods: {
    refreshPage() {
      this.$router.push(this.$route.fullPath)
    },
    setStatusRadioValue(status) {
      this.statusRadioValue = []
      this.statusRadioValue.push(status)
    },
    setAllStatusRadioValue() {
      this.statusRadioValue = ['正常运行', '报警', '设备离线']
    },
    getMachineStatus(statusCode) {
      if (statusCode === 0) {
        return '设备离线'
      } else if (statusCode === 1) {
        return '正常运行'
      } else if (statusCode === 2) {
        return '报警'
      }
    },
    getStatusRadioColor(statusCode) {
      if (this.statusRadioValue.indexOf(statusCode) > -1)
        return this.statusType[statusCode]
      else
        return "rgba(235,235,235,1)"
    },
    setDefaultCount() {
      this.statusCount = {
        '设备离线': 0,
        '正常运行': 0,
        '报警': 0
      }
    },
    onStatusRadioClick(statusCode) {
      const idx = this.statusRadioValue.indexOf(statusCode)
      if (idx > -1)
        this.statusRadioValue.splice(idx, 1)
      else
        this.statusRadioValue.push(statusCode)
    },
    getLensPackerStatus() {
      getMachineStatus().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.LensPackerInfoList = responseData.data;
        }
      })
      getMachineAlarms().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.LensPackerAlarmInfo = responseData.data;
        }
      })
    }
  },
  beforeUnmount() {
    clearInterval(this.timer);
  },
  data() {
    return {
      LensPackerInfoList: [],
      // statusType: {
      //   '设备离线': 'item_coating_offline',
      //   '正常运行': 'item_coating_running',
      //   '上料预警': 'item_coating_alarm'
      // }
      LensPackerAlarmInfo: [],
      statusType: {
        '设备离线': "gray",
        '正常运行': "rgba(59,162,114,1)",
        '报警': "rgba(238,102,102,1)"
      },
      statusRadio: ['正常运行', '报警', '设备离线'],
      statusRadioValue: ['正常运行', '报警', '设备离线'],
      statusCount: {
        '设备离线': 0,
        '正常运行': 0,
        '报警': 0
      }
    }
  }
};
</script>
