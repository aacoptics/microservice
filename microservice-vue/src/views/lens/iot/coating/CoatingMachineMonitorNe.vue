<template>
  <div>
    <div class="aac-container">
      <div style="margin-bottom: 20px">
        <div v-for="(item, index) of statusRadio" :key="index"
             :style="'background-color:' + getStatusRadioColor(item) + ';text-align:center'"
             class="status_radio_type"
             @click="onStatusRadioClick(item)">
          {{ item + '(' + this.statusCount[item] + ')' }}
        </div>
      </div>
      <el-card v-for="(singleMachineInfo, index) of coatingStatusInfo" :key="index"
               :body-style="{ padding: '0px', height:'60px'}"
               class="coating_card_type"
               shadow="hover"
               style="cursor: pointer">

        <el-row>
          <el-col>
            <div style="font-weight: bold">
              <el-row
                  style="text-align: center;height:30px; font-weight: bold;font-size: 16px;border-bottom: 1px solid cornflowerblue">
                <el-col :span="8">
                  <p style="text-align: center;font-weight: bold;color: #008000;font-size: 24px">
                    {{ singleMachineInfo.name }}</p>
                </el-col>
                <el-col :span="16">
                  <div
                      :style="'background-color:' + statusType[getMachineStatus(singleMachineInfo.status, singleMachineInfo.isOnline)] + ';height:30px;line-height:30px'">
                    {{ getMachineStatus(singleMachineInfo.status, singleMachineInfo.isOnline) }}
                  </div>
                </el-col>
              </el-row>
              <el-row style="text-align: center;height: 30px;line-height: 30px">
                <el-col :span="8">产能：{{ singleMachineInfo.totalNums }}</el-col>
                <el-col :span="16">累计锅次：{{ singleMachineInfo.runNo }}</el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </el-card>
      <!--      <div v-for="(singleMachineInfo, index) of coatingStatusInfo" :key="index"-->
      <!--           class="coating_div_type">-->
      <!--        <div :class="statusType[getMachineStatus(singleMachineInfo.status, singleMachineInfo.isOnline)]">-->
      <!--          <el-row style="margin-bottom: 10px">-->
      <!--            <el-col :span="12">-->
      <!--              <span style="width: 200px">机台号：{{ singleMachineInfo.name }}</span>-->
      <!--            </el-col>-->
      <!--            <el-col :span="12">-->
      <!--              <span>状态：{{ getMachineStatus(singleMachineInfo.status, singleMachineInfo.isOnline) }}</span>-->
      <!--            </el-col>-->
      <!--          </el-row>-->
      <!--          <el-row>-->
      <!--            <el-col :span="12">-->
      <!--              <span>产能：</span>-->
      <!--              <span>{{ singleMachineInfo.totalNums }}</span>-->
      <!--            </el-col>-->
      <!--            <el-col :span="12">-->
      <!--              <span>累计锅次：</span>-->
      <!--              <span>{{ singleMachineInfo.runNo }}</span>-->
      <!--            </el-col>-->
      <!--          </el-row>-->
      <!--        </div>-->
      <!--      </div>-->
    </div>
  </div>
</template>

<script>
import {getStatus} from "@/api/lens/iot/coatingNe";

export default {
  name: "CoatingMachineMonitor",
  created() {
    this.getCoatingMachineInfo()
    this.timer = setInterval(() => {
      this.getCoatingMachineInfo()
    }, 10000)
  },
  computed: {
    coatingStatusInfo() {
      const pages = []
      this.setDefaultCount()
      if (this.$route.params.status) {
        this.setStatusRadioValue(this.$route.params.status);
        this.refreshPage()
      }
      this.coatingMachineInfoList.forEach((item) => {
        const statusName = this.getMachineStatus(item.status, item.isOnline)
        if (this.checkCoatingPhase(item.name)) {
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
      this.statusRadioValue = ['正常运行', '上料预警', '设备离线']
    },
    checkCoatingPhase(machineName) {
      if (this.$route.query.position === '1') {
        if (machineName.indexOf("A") === 0 ||
            machineName.indexOf("B") === 0 ||
            machineName.indexOf("C") === 0 ||
            machineName.indexOf("D") === 0 ||
            machineName.indexOf("E") === 0 ||
            machineName.indexOf("F") === 0 ||
            machineName.indexOf("L") === 0) {
          return true;
        } else {
          return false;
        }
      } else if (this.$route.query.position === '2') {
        if (machineName.indexOf("A") === -1 &&
            machineName.indexOf("B") === -1 &&
            machineName.indexOf("C") === -1 &&
            machineName.indexOf("D") === -1 &&
            machineName.indexOf("E") === -1 &&
            machineName.indexOf("F") === -1 &&
            machineName.indexOf("L") === -1) {
          return true;
        } else {
          return false;
        }
      }
    },
    getMachineStatus(statusCode, isOnline) {
      if (!isOnline) {
        return '设备离线'
      } else {
        if (statusCode === 1) {
          return '上料预警'
        } else if (statusCode === 0) {
          return '正常运行'
        } else {
          return '设备离线'
        }
      }
    },
    setDefaultCount() {
      this.statusCount = {
        '设备离线': 0,
        '正常运行': 0,
        '上料预警': 0
      }
    },
    getStatusRadioColor(statusCode) {
      if (this.statusRadioValue.indexOf(statusCode) > -1)
        return this.statusType[statusCode]
      else
        return "rgba(235,235,235,1)"
    },
    onStatusRadioClick(statusCode) {
      const idx = this.statusRadioValue.indexOf(statusCode)
      if (idx > -1)
        this.statusRadioValue.splice(idx, 1)
      else
        this.statusRadioValue.push(statusCode)
    },
    getCoatingMachineInfo() {
      getStatus().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.coatingMachineInfoList = responseData.data.site;
        }
      })
    }
  },
  beforeUnmount() {
    clearInterval(this.timer);
  },
  data() {
    return {
      pathStatusInfo: "",
      coatingMachineInfoList: [],
      // statusType: {
      //   '设备离线': 'item_coating_offline',
      //   '正常运行': 'item_coating_running',
      //   '上料预警': 'item_coating_alarm'
      // }
      statusType: {
        "设备离线": "gray",
        "正常运行": "rgba(59,162,114,1)",
        "上料预警": "rgba(154,96,180,1)"
      },
      statusRadio: ['正常运行', '上料预警', '设备离线'],
      statusRadioValue: ['正常运行', '上料预警', '设备离线'],
      statusCount: {
        '设备离线': 0,
        '正常运行': 0,
        '上料预警': 0
      }
    }
  }
};
</script>

<style scoped>
.status_radio_type {
  width: 100px;
  height: 30px;
  line-height: 30px;
  border-radius: 5px;
  font-family: 'Microsoft YaHei', serif;
  font-weight: bold;
  font-size: 14px;
  cursor: pointer;
  display: inline-block;
  margin-left: 5px;
  margin-right: 5px;
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
</style>
