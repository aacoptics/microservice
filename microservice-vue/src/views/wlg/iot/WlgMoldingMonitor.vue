<template>
  <div>
    <div class="aac-container">
      <mqtt-client ref="mqttClient" :value="moldingTopic" @messageArrived="messageArrived"/>
      <el-card v-for="(item, index) of Object.keys(this.moldingStatusData).sort().reduce(
        (obj, key) => {
          obj[key] = this.moldingStatusData[key];
          return obj;
        },
        {})" :key="index"
               :body-style="{ padding: '0px', height:'240px'}"
               class="molding_card_type"
               shadow="hover"
               style="cursor: pointer">

        <el-row>
          <el-col>
            <div style="font-weight: bold">
              <el-row
                  style="text-align: center;height:30px; font-weight: bold;font-size: 16px;border-bottom: 1px solid cornflowerblue">
                <el-col :span="24">
                  <p style="text-align: center;font-weight: bold;color: #008000;font-size: 24px">
                    {{ item.ClientId }}</p>
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
                  <div
                      :style="'background-color:' + statusType[item.Data.unitcontrol_stage] + ';height:30px;line-height:30px'">

                    {{ item.Data.unitcontrol_stage }}
                  </div>
                </el-col>
              </el-row>
              <el-row align="middle" style="height: 20px;">
                <span>严重报警</span>
                <span v-if="item.Data.unitcontrol_fatal_error == 'True'" class="status-legend"
                      style="background-color: rgba(238,102,102,1);margin-left: 4px"></span>
                <span v-else class="status-legend" style="background-color: rgba(59,162,114,1);margin-left: 4px"></span>

                <span style="margin-left: 8px">报警</span>
                <span v-if="item.Data.unitcontrol_error == 'True'" class="status-legend"
                      style="background-color: rgba(238,102,102,1);margin-left: 4px"></span>
                <span v-else class="status-legend" style="background-color: rgba(59,162,114,1);margin-left: 4px"></span>

                <span style="margin-left: 8px">警告</span>
                <span v-if="item.Data.unitcontrol_warning == 'True'" class="status-legend"
                      style="background-color: rgba(250,200,88,1);margin-left: 4px"></span>
                <span v-else class="status-legend" style="background-color: rgba(59,162,114,1);margin-left: 4px"></span>
              </el-row>
              <el-row style="height: 30px;line-height: 30px">
                <el-col :span="12">
                  <p>
                    模式：{{ item.Data.unitcontrol_mode }}
                  </p>
                </el-col>
              </el-row>
              <el-divider content-position="left">事件：</el-divider>
              <el-row v-if="moldingEventData[item.ClientId] && moldingEventData[item.ClientId].Data"
                      style="height: 15px;line-height: 15px;font-size: xx-small;">
                <p v-for="(item, index) of moldingEventData[item.ClientId].Data" :key="index">
                  {{ item.EventInfo }}
                </p>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import MqttClient from "@/components/MqttClient";

export default {
  name: "WlgMoldingMonitor",
  components: {
    MqttClient
  },

  computed: {
    moldingTopic() {
      return [
        {
          topic: 'MoldingMachine/#',
          qos: 0
        }
      ]
    },
  },
  methods: {

    messageArrived(msg) {
      switch (msg.Message) {
        case 'status':
          this.moldingStatusData[msg.ClientId] = msg
          break;
        case 'event':
          this.moldingEventData[msg.ClientId] = msg
          break;
      }
    },
    initConnect() {
      this.$nextTick(() => {
        this.$refs.mqttClient.createMqttConnection();
      });
    },
    //断开连接
    disconnect() {
      this.$refs.mqttClient.destroyMqttConnection();
    },
  },
  mounted() {
    let _this = this;
    setTimeout(function () {
      _this.initConnect()
    }, 100);
  },
  beforeUnmount() {
    this.disconnect();
  },
  data() {
    return {
      moldingStatusData: {},
      moldingEventData: {},
      statusType: {
        'INITIALIZING': "rgba(250,200,88,1)",
        'UNINITIALIZED': "rgba(154,96,180,1)",
        'READY': "rgba(252,132,82,1)",
        'Disconnected': "gray",
        'ACTIVATED': "rgba(59,162,114,1)",
        'ABORTING': "rgba(238,102,102,1)",
        'STOPPING': "rgba(238,102,102,1)"
      },
    }
  }
};
</script>

<style>
.molding_card_type {
  width: 260px;
  height: 240px;
  border: 1px solid cornflowerblue;
  background-color: #f0f0f0;
  margin-right: 50px;
  margin-bottom: 20px;
  float: left;
}

.status-legend {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  line-height: 30px;
}
</style>
