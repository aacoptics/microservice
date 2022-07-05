<template>
  <div>
    <div class="aac-container">
      <mqtt-client ref="mqttClient" :value="feedingTopic" @messageArrived="messageArrived"/>
      <el-card v-for="(item, index) of Object.keys(feedingStatusData).sort().reduce(
        (obj, key) => {
          obj[key] = feedingStatusData[key];
          return obj;
        },
        {})" :key="index"
               :body-style="{ padding: '0px', height:'240px'}"
               class="feeding_card_type"
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
              <el-row align="middle" style="height: 20px;margin-top: 10px">
                <el-col :span="8">
                  <span>初始化状态</span>
                </el-col>
                <el-col :span="4">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>{{ statusTypeName[1][item.Data[1]] }}</p>
                    </template>
                    <div :style="'background-color:' + statusType[1][item.Data[1]]"
                         class="status-legend"></div>
                  </el-tooltip>
                </el-col>

                <el-col :span="8">
                  <span>运行状态</span>
                </el-col>
                <el-col :span="4">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>{{ statusTypeName[2][item.Data[2]] }}</p>
                    </template>
                    <div :style="'background-color:' + statusType[2][item.Data[2]]"
                         class="status-legend">
                    </div>
                  </el-tooltip>
                </el-col>
              </el-row>
              <el-row align="middle" style="height: 20px;margin-top: 10px">
                <el-col :span="8">
                  <span>手动/自动</span>
                </el-col>
                <el-col :span="4">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>{{ statusTypeName[3][item.Data[3]] }}</p>
                    </template>
                    <div :style="'background-color:' + statusType[3][item.Data[3]]"
                         class="status-legend"></div>
                  </el-tooltip>
                </el-col>
                <el-col :span="8">
                  <span>单机/联机</span>
                </el-col>
                <el-col :span="4">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>{{ statusTypeName[4][item.Data[4]] }}</p>
                    </template>
                    <div :style="'background-color:' + statusType[4][item.Data[4]]"
                         class="status-legend"></div>
                  </el-tooltip>
                </el-col>
              </el-row>
              <el-row align="middle" style="height: 20px;margin-top: 10px">
                <el-col :span="20">
                  <span>机器人连接状态</span>
                </el-col>
                <el-col :span="4">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>{{ statusTypeName[5][item.Data[5]] }}</p>
                    </template>
                    <div :style="'background-color:' + statusType[5][item.Data[5]]"
                         class="status-legend"></div>
                  </el-tooltip>
                </el-col>
              </el-row>

              <el-divider content-position="left">报警：</el-divider>
              <el-row style="height: 15px;line-height: 15px;font-size: xx-small;">
                <p v-if="item.Data[2] === 3">
                  {{ feedingStatusData[item.ClientId].Data.AlarmCode }}
                </p>
              </el-row>
              <!--              <el-row style="height: 30px;line-height: 30px">-->
              <!--                <el-col :span="12">-->
              <!--                  <p>-->
              <!--                    模式：{{ item.Data.unitcontrol_mode }}-->
              <!--                  </p>-->
              <!--                </el-col>-->
              <!--              </el-row>-->
              <!--              <el-divider content-position="left">事件：</el-divider>-->
              <!--              <el-row style="height: 15px;line-height: 15px;font-size: xx-small;">-->
              <!--                <p v-for="(item, index) of moldingEventData[item.ClientId].Data" :key="index">-->
              <!--                  {{ item.EventInfo }}-->
              <!--                </p>-->
              <!--              </el-row>-->
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
    feedingTopic() {
      return [
        {
          topic: 'FeedingMachine/#',
          qos: 0
        }
      ]
    },
  },
  methods: {
    messageArrived(msg) {
      switch (msg.Message) {
        case 'status':
          this.feedingStatusData[msg.ClientId] = msg
          break;
        case 'event':
          this.feedingEventData[msg.ClientId] = msg
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
      feedingStatusData: {},
      feedingEventData: {},

      statusType: {
        1: {
          1: "rgba(154,96,180,1)",
          2: "rgba(250,200,88,1)",
          3: "rgba(59,162,114,1)"
        },
        2: {
          0: "grey",
          1: "rgba(59,162,114,1)",
          2: "rgba(238,102,102,1)",
          3: "rgba(238,102,102,1)",
          4: "rgba(252,132,82,1)"
        },
        3: {
          1: "rgba(250,200,88,1)",
          2: "rgba(59,162,114,1)"
        },
        4: {
          1: "rgba(250,200,88,1)",
          2: "rgba(59,162,114,1)"
        },
        5: {
          1: "rgba(250,200,88,1)",
          2: "rgba(59,162,114,1)"
        },
      },
      statusTypeName: {
        1: {
          1: "当前未初始化",
          2: "当前初始化中",
          3: "当前ready"
        },
        2: {
          0: "离线",
          1: "运行中",
          2: "急停中",
          3: "报警中",
          4: "其他"
        },
        3: {
          1: "手动",
          2: "自动"
        },
        4: {
          1: "单机",
          2: "联机"
        },
        5: {
          1: "上料机器人未连接",
          2: "上料机器人已连接"
        }
      },
    }
  }
};
</script>

<style>
.feeding_card_type {
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
