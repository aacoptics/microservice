<template>
  <div>
    <div class="aac-container" style="overflow: hidden;">
      <div style="margin-bottom: 20px">
        <div v-for="(item, index) of statusRadio" :key="index"
             :style="'background-color:' + statusInfo[item].color + ';text-align:center'"
             class="status_radio_type">
          {{ statusInfo[item].desc + '(' + this.statusCount[item] + ')' }}
        </div>
      </div>
      <el-card v-for="(area, index) of areaCode" :key="index" class="box-card" style="margin-top: 10px">
        <template #header>
          <div class="card-header">
            <span>{{ area }}</span>
          </div>
        </template>
        <el-card v-for="(singleMachineInfo, index) of moldDataByArea(area)" :key="index"
                 :body-style="{ padding: '0px', height:'50px'}"
                 :style="'background-color:' +
                 statusInfo[singleMachineInfo.CncNode.State].color + ';cursor: pointer'"
                 class="mold_card_type"
                 shadow="hover">
          <el-tooltip placement="top">
            <template #content>
              <p>监控号：{{ singleMachineInfo.CncBaseInfo.monitorNo }}</p>
              <p>程序名：{{ singleMachineInfo.CncBaseInfo.progName }}</p>
            </template>


            <div
                v-if="getMaintainStatus(singleMachineInfo.CncBaseInfo.monitorNo, singleMachineInfo.CncNode.State) && !singleMachineInfo.CncBaseInfo.isMicroProg"
                style="height:30px;line-height:40px;text-align: center;font-weight: bold">
              <el-badge class="item" is-dot style="height: 30px;line-height:40px">
                {{ singleMachineInfo.MachineNo }}
              </el-badge>
            </div>
            <div v-else style="height:50px;line-height:50px;text-align: center;font-weight: bold">
              {{ singleMachineInfo.MachineNo }}
            </div>
          </el-tooltip>
        </el-card>
      </el-card>
      <el-card style="margin-top: 20px">
        <template #header>
          <div class="card-header">
            <span>时间段：{{ timeArea.startTime }} 至 {{ timeArea.endTime }}</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="4">
            <div
                style="font-size: 50px;font-weight: bold;height: 240px;line-height: 240px;text-align: center;background-color: #409EFF;color: white">
              OEE
            </div>
          </el-col>
          <el-col :span="6">
            <el-row v-for="(area, index) of areaCode" :key="index">
              <div style="height: 80px;line-height: 80px">
                <div
                    style="float:left;font-size: xx-large;color: #222222;width: 90px; margin-right: 10px;font-weight: bold">
                  {{ area }}：
                </div>
                <div style="float:left;font-family: 'led regular';font-size: xxx-large;color: green">
                  {{ lastDayTotalTime[area] ? lastDayTotalTime[area] : '0.00' }}%
                </div>
              </div>
            </el-row>
          </el-col>
          <el-col :span="4">
            <div
                style="font-size: 30px;font-weight: bold;height: 240px;line-height: 240px;text-align: center;background-color: #409EFF;color: white">
              符合寿命占比
            </div>
          </el-col>
          <el-col :span="10" style="line-height: 60px;">
            <el-row>
              <div style="float:left;width: 160px;font-weight: bold;font-size: xx-large">异常数量：</div>
              <div style="float:left;font-family: 'led regular';font-size: xxx-large;color: green">
                {{ this.lastDayAbnormalCount }}
              </div>
            </el-row>
            <el-row>
              <div style="float:left;width: 160px;font-weight: bold;font-size: xx-large">正常数量：</div>
              <div style="float:left;font-family: 'led regular';font-size: xxx-large;color: green">
                {{ this.lastDayScrapRate.scrapCount - this.lastDayAbnormalCount }}
              </div>
            </el-row>
            <el-row>
              <div style="float:left;width: 160px;font-weight: bold;font-size: xx-large">退库数量：</div>
              <div style="float:left;font-family: 'led regular';font-size: xxx-large;color: green">
                {{ this.lastDayScrapRate.outCount }}
              </div>
            </el-row>
            <el-row>
              <div style="float:left;width: 160px;font-weight: bold;font-size: xx-large">异常占比：</div>
              <div style="float:left;font-family: 'led regular';font-size: xxx-large;color: green">
                {{
                  this.lastDayScrapRate.outCount > 0 ? (this.lastDayAbnormalCount * 1.0 / this.lastDayScrapRate.outCount * 100).toFixed(2) : '0.00'
                }}%
              </div>
            </el-row>
          </el-col>
        </el-row>
      </el-card>
      <div class="" style="width: 100%;height: 450px;margin-top: 20px">
        <!-- 表头 -->
        <div class="warp-title" style="height: 25px;background-color: #f5f7fa;cursor: pointer"
             @dblclick="abnormalListDialog = true">
          <ul class="item">
            <li>
              <span class="id" style="width: 30px;font-weight: bold;text-align: center">序号</span>
              <span class="toolNo" style="width: 80px;font-weight: bold;text-align: center">刀具编号</span>
              <span class="matCode" style="width: 100px;font-weight: bold;text-align: center">刀具物料号</span>
              <span class="matName" style="width: 280px;font-weight: bold;text-align: center">刀具名称</span>
              <span class="lifeSalvage" style="width: 80px;font-weight: bold;text-align: center">标准寿命</span>
              <span class="realLifeSalvage" style="width: 80px;font-weight: bold;text-align: center">实际寿命</span>
              <span class="lifeRate" style="width: 80px;font-weight: bold;text-align: center">寿命占比</span>
              <span class="scrapedTime" style="width: 120px;font-weight: bold;text-align: center">报废时间</span>
              <span class="area" style="width: 60px;font-weight: bold;text-align: center">工序</span>
              <span class="lastMachineNo" style="width: 60px;font-weight: bold;text-align: center">机床号</span>
              <span class="reasonTxt" style="width: 300px;font-weight: bold;text-align: center">原因</span>
              <span class="reason" style="width: 60px;font-weight: bold;text-align: center">原因填写</span>
              <span class="confirm" style="width: 60px;font-weight: bold;text-align: center">确认</span>
            </li>
          </ul>
        </div>
        <!-- 表格滚动区 -->
        <div>
          <scroll :class-option="defaultOption" :data="abnormalList"
                  class="warp-content" style="height: 420px }">
            <ul class="item">
              <li v-for="(item, index) of abnormalList" :key="index"
                  :style="{backgroundColor:((index+1)%2 == 0) ? '#f0f9eb' : '#ffffff'}">
                <span class="id" style="width: 30px;text-align: center" v-text="index + 1"></span>
                <span class="toolNo" style="width: 80px;text-align: center;cursor: pointer"
                      @click="onGetToolHisClick(item.toolNo, item.matCode, item.matName)"
                      v-text="item.toolNo"></span>
                <span class="matCode" style="width: 100px;text-align: center" v-text="item.matCode"></span>
                <span class="matName" style="width: 280px;text-align: center" v-text="item.matName"></span>
                <span class="lifeSalvage" style="width: 80px;text-align: center" v-text="item.lifeSalvage"></span>
                <span class="realLifeSalvage" style="width: 80px;text-align: center"
                      v-text="item.realLifeSalvage"></span>
                <!--                <span class="lifeRate" style="width: 80px;text-align: center"-->
                <!--                      v-text="(item.realLifeSalvage * 1.0 / item.lifeSalvage * 100).toFixed(2) + '%'"></span>-->
                <span class="lifeRate" style="width: 80px;text-align: center" v-text="item.lifeRate"></span>
                <span class="scrapedTime" style="width: 120px;text-align: center"
                      v-text="this.$moment(item.scrapedTime).format('YYYY/MM/DD HH:mm')"></span>
                <span class="area" style="width: 60px;text-align: center" v-text="item.area"></span>
                <span class="lastMachineNo" style="width: 60px;text-align: center" v-text="item.lastMachineNo"></span>
                <el-tooltip v-if="item.reason != null" placement="top">
                  <template #content>
                    <p>原因：{{ item.reason }}</p>
                  </template>
                  <span class="reasonTxt" style="width: 300px;text-overflow:ellipsis;overflow:hidden;text-align: center"
                        v-text="item.reason"></span>
                </el-tooltip>
                <span v-else class="reasonTxt" style="width: 300px;text-align: center">
                  <el-tag type="danger">原因未填写</el-tag>
                </span>
                <span class="reason" style="width: 60px;text-align: center">
                  <el-button circle type="primary" @click="onAddReasonClick(item)">
                    <template #icon>
                      <font-awesome-icon :icon="['fas','pencil']"/>
                    </template>
                  </el-button>
                </span>
                <span class="confirm" style="width: 60px;text-align: center">
                  <el-button circle type="success" @click="onConfirmReasonClick(item)">
                    <template #icon>
                      <font-awesome-icon :icon="['fas','check']"/>
                    </template>
                  </el-button>
                </span>
              </li>
            </ul>
          </scroll>
        </div>
      </div>
      <el-dialog v-model="reasonDialog" :close-on-click-modal="false" title="刀具异常原因填写">
        <el-select v-model="addReasonDialog.abnormalTool.abnormalType" filterable placeholder="请选择原因类型">
          <el-option
              v-for="item in abnormalType"
              :key="item.typeCode"
              :label="item.abnormalType"
              :value="item.typeCode"
          >
          </el-option>
        </el-select>
        <el-input v-model="addReasonDialog.abnormalTool.reason" auto-complete="off" placeholder="请填写异常原因"
                  style="margin-top: 20px"></el-input>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button @click="resetSelection">取消</el-button>
            <el-button :loading="editReasonLoading" type="primary" @click="submitForm">提交</el-button>
          </slot>
        </div>
      </el-dialog>

      <el-dialog v-model="abnormalListDialog" :close-on-click-modal="false" title="刀具异常清单" width="90%">
        <el-table
            id="AbnormalListTable"
            :key="1"
            :data="abnormalList"
            border
            height="500px"
            style="width: 100%;margin-top: 10px">
          <el-table-column :width=60 label="序号" prop="id"></el-table-column>
          <el-table-column :width=90 label="刀具编号">
            <template v-slot="scope">
            <span class="lifeRate" style="cursor: pointer"
                  @click="onGetToolHisClick(scope.row.toolNo, scope.row.matCode, scope.row.matName)"
                  v-text="scope.row.toolNo"></span>
            </template>
          </el-table-column>
          <el-table-column :width=110 label="刀具物料号" prop="matCode"></el-table-column>
          <el-table-column label="刀具名称" prop="matName"></el-table-column>
          <el-table-column :width=100 label="标准寿命" prop="lifeSalvage"></el-table-column>
          <el-table-column :width=100 label="实际寿命" prop="realLifeSalvage"></el-table-column>
          <el-table-column :width=80 label="寿命占比" prop="lifeRate">
            <template v-slot="scope">
            <span class="lifeRate" style="width: 80px;text-align: center"
                  v-text="(scope.row.realLifeSalvage * 1.0 / scope.row.lifeSalvage * 100).toFixed(2) + '%'"></span>
            </template>
          </el-table-column>
          <el-table-column :width=140 label="报废时间" prop="scrapedTime">
            <template v-slot="scope">
              <span v-text="this.$moment(scope.row.scrapedTime).format('YYYY/MM/DD HH:mm')"></span>
            </template>
          </el-table-column>
          <el-table-column :filter-method="filterHandler" :filters="headFilters.area" :width=80 label="工序"
                           prop="area"></el-table-column>
          <el-table-column :width=80 label="机床号" prop="lastMachineNo"></el-table-column>
          <el-table-column label="原因">
            <template v-slot="scope">
              <el-tooltip v-if="scope.row.reason != null" placement="top">
                <template #content>
                  <p>原因：{{ scope.row.reason }}</p>
                </template>
                <span class="reasonTxt" style="text-overflow:ellipsis;overflow:hidden;text-align: center"
                      v-text="scope.row.reason"></span>
              </el-tooltip>
              <span v-else class="reasonTxt" style="text-align: center">
                  <el-tag type="danger">原因未填写</el-tag>
                </span>
            </template>
          </el-table-column>
          <el-table-column :width=100 label="原因填写">
            <template v-slot="scope">
              <el-button circle type="primary" @click="onAddReasonClick(scope.row)">
                <template #icon>
                  <font-awesome-icon :icon="['fas','pencil']"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
          <el-table-column :width=100 label="确认">
            <template v-slot="scope">
              <el-button circle type="success"
                         @click="onConfirmReasonClick(scope.row)">
                <template #icon>
                  <font-awesome-icon :icon="['fas','check']"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
      <el-dialog v-model="toolHisListDialog" :close-on-click-modal="false" title="刀具运转明细" width="60%">
        <el-table
            id="ToolHisListTable"
            :key="1"
            :data="toolHisList"
            border
            height="500px"
            style="width: 100%;margin-top: 10px">
          <el-table-column :width=90 label="刀具编号" prop="toolNo"></el-table-column>
          <el-table-column :width=110 label="刀具物料号" prop="matCode"></el-table-column>
          <el-table-column label="刀具名称" prop="matName"></el-table-column>
          <el-table-column :width=120 label="监控号" prop="monitorNo"></el-table-column>
          <el-table-column :width=80 label="机床号" prop="machineNo"></el-table-column>
          <el-table-column :width=90 label="程序名" prop="programName"></el-table-column>
          <el-table-column :width=140 label="开始时间">
            <template v-slot="scope">
              <span v-text="this.$moment(scope.row.startTime).format('YYYY/MM/DD HH:mm')"></span>
            </template>
          </el-table-column>
          <el-table-column :width=140 label="结束时间">
            <template v-slot="scope">
              <span v-text="this.$moment(scope.row.endTime).format('YYYY/MM/DD HH:mm')"></span>
            </template>
          </el-table-column>
          <el-table-column :width=120 label="使用时长(min)">
            <template v-slot="scope">
            <span class="lifeRate" style="width: 80px;text-align: center"
                  v-text="(scope.row.totalTime * 1.0 / 60).toFixed(2)"></span>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {Client} from '@stomp/stompjs';
import scroll from 'vue-seamless-scroll/src'
import {MQTT_PASSWORD, MQTT_SERVICE, MQTT_TOPIC_TOOL_LIFE, MQTT_USERNAME} from '@/utils/rabbitConfig'
import {
  addAbnormalReason,
  confirmAbnormalReason,
  getAbnormalList,
  getAbnormalType,
  getAreaInfo,
  getLastDayAbnormalCount,
  getLastDayScrapCount,
  getLastDayTotalTime,
  getToolHisList,
  getToolMaintainStatus
} from "@/api/lens/iot/mold";
import {getUsername} from "@/utils/auth";
import {tableFilter} from "@/utils/baseUtils";

export default {
  name: "index",
  components: {
    scroll
  },
  data() {
    return {
      toolHisList: [],
      toolHisListDialog: false,
      abnormalListDialog: false,
      abnormalType: [],
      addReasonDialog: {abnormalTool: {}},
      reasonDialog: false,
      editReasonLoading: false,
      client: new Client(),
      abnormalList: [],
      moldData: [],
      statusInfo: {
        0: {
          desc: "离线",
          color: "gray"
        },
        2: {
          desc: "空闲",
          color: "rgba(250,200,88,1)"
        },
        3: {
          desc: "调机",
          color: "rgba(252,132,82,1)"
        },
        4: {
          desc: "报警",
          color: "rgba(238,102,102,1)"
        },
        5: {
          desc: "加工中",
          color: "rgba(59,162,114,1)"
        },
        6: {
          desc: "手轮模式",
          color: "rgba(84,112,198,1)"
        }
      },
      statusCount: {0: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0},
      areaInfo: {},
      areaCode: [],
      maintainStatus: {},
      lastDayTotalTime: [],
      lastDayAbnormalCount: 0,
      lastDayScrapRate: {
        scrapCount: 0,
        outCount: 0,
        rate: '0.00'
      },
      statusRadio: [5, 2, 3, 6, 4, 0],
      timeArea: {
        startTime: '',
        endTime: ''
      }
    }
  },
  computed: {
    defaultOption() {
      return {
        step: 0.3,          // 数值越大速度滚动越快
        limitMoveNum: 10,  // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true,  // 是否开启鼠标悬停stop
        direction: 1,     // 0向下 1向上 2向左 3向右
        openWatch: true,  // 开启数据实时监控刷新dom
        singleHeight: 0,  // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0,   // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000    // 单步运动停止的时间(默认值1000ms)
      }
    },
    headFilters() {
      return tableFilter(this.abnormalList)
    },
  },
  mounted() {
    this.getMachineAreaInfo();
    this.connect();
    this.getToolMaintainStatus();
  },
  methods: {
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    onGetToolHisClick(toolNo, matCode, matName) {
      getToolHisList(toolNo).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.toolHisList = responseData.data
          this.toolHisList.forEach(item => {
            item.toolNo = toolNo
            item.matCode = matCode
            item.matName = matName
          })
          this.toolHisListDialog = true
        } else {
          this.$message({message: '查找明细失败，请联系管理员', type: 'error'})
        }
      })

    },
    submitForm() {
      if (this.addReasonDialog.abnormalTool.reason != null && this.addReasonDialog.abnormalTool.reason.length > 0 && this.addReasonDialog.abnormalTool.abnormalType != null) {
        this.$confirm('确认提交吗？', '提示', {}).then(() => {
          this.addReasonDialog.abnormalTool.checkedPerson = getUsername()
          this.editReasonLoading = true
          addAbnormalReason(this.addReasonDialog.abnormalTool).then((response) => {
            const responseData = response.data
            if (responseData.code === '000000') {
              this.$message({message: '添加原因成功！', type: 'success'})
              this.addReasonDialog = {abnormalTool: {}}
              this.reasonDialog = false
            } else {
              this.$message({message: '添加原因失败，请联系管理员', type: 'error'})
            }
            this.getAbnormalInfo()
            this.editReasonLoading = false
          }).catch(() => {
            this.$message({message: '添加原因失败，请联系管理员', type: 'error'})
            this.editReasonLoading = false
          })
        })
      } else {
        this.$message({message: '操作失败,原因不能为空', type: 'error'})
      }
    },
    resetSelection() {
      this.addReasonDialog = {abnormalTool: {}}
      this.reasonDialog = false
    },
    onAddReasonClick(item) {
      this.getAbnormalType()
      this.addReasonDialog.abnormalTool = Object.assign({}, item)
      this.reasonDialog = true
    },
    onConfirmReasonClick(item) {
      if (item.reason == null) {
        this.$message({message: '异常原因未填写，请安排人员填写原因！', type: 'error'})
        return
      }
      this.$confirm('确定认同该原因吗？', '提示', {}).then(() => {
        item.isConfirmed = true
        item.confirmedPerson = getUsername()
        confirmAbnormalReason(item).then((response) => {
          const responseData = response.data
          if (responseData.code === '000000') {
            this.getAbnormalInfo()
            this.$message({message: '确认成功！', type: 'success'})
          } else {
            this.$message({message: '确认失败，请联系管理员', type: 'error'})
          }
          this.editReasonLoading = false
        }).catch(() => {
          this.$message({message: '确认失败，请联系管理员', type: 'error'})
          this.editReasonLoading = false
        })
      })
    },
    getAbnormalInfo() {
      getAbnormalList().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.abnormalList = responseData.data;
        }
      })
    },
    getMaintainStatus(monitorNo, state) {
      return (!this.maintainStatus[monitorNo]) && state === 5;
    },
    moldDataByArea(area) {
      const res = []
      this.moldData.forEach(item => {
        if (this.areaInfo[item.MachineNo] === area) {
          res.push(item)
        }
      })
      return res
    },
    onConnected: function () {
      const headers = {
        'auto-delete': 'true'
      };
      this.client.subscribe(MQTT_TOPIC_TOOL_LIFE, this.responseCallback, headers);
      //订阅的频道
      //this.client.subscribe(MQTT_TOPIC_TOOL_LIFE, this.responseCallback, headers);
    },
    onFailed: function (msg) {
      console.log(new Date() + "报错:" + msg);
      // this.reconnect()
    },
    //成功时的回调函数
    responseCallback: function (msg) {
      //接收消息的处理
      this.moldData = JSON.parse(msg.body)
      this.statusCount = {0: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0}
      this.moldData.forEach(item => {
        if (this.areaInfo[item.MachineNo]) {
          this.statusCount[item.CncNode.State]++;
        }
      })
    },
    //连接
    connect: function () {
      // this.client.debug = null
      // this.client.connect({
      //   login: MQTT_USERNAME,
      //   password: MQTT_PASSWORD
      // }, this.onConnected, this.onFailed);
      this.client = new Client({
        brokerURL: MQTT_SERVICE,
        connectHeaders: {
          login: MQTT_USERNAME,
          passcode: MQTT_PASSWORD,
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 5000,
        heartbeatOutgoing: 5000,
      });

      this.client.onConnect = this.onConnected;
      this.client.onStompError = this.onFailed;
      this.client.activate();
    },
    // reconnect() {
    //   const reconInv = setInterval(() => {
    //     console.info(new Date() + '重连中...')
    //     this.client = Stomp.client(MQTT_SERVICE)
    //     this.client.debug = null
    //     this.client.connect({
    //       login: MQTT_USERNAME,
    //       password: MQTT_PASSWORD
    //     }, () => {
    //       console.info(new Date() + '重连成功')
    //       // 连接成功，清除定时器
    //       clearInterval(reconInv)
    //       this.onConnected()
    //     }, this.onFailed)
    //   }, 2000)
    // },
    disconnect() {
      if (this.client !== null) {
        this.client.deactivate();
        console.log("断开MQ连接");
      }
    },
    getToolMaintainStatus() {
      const monitorNos = []
      this.moldData.forEach(item => {
        if (item.CncNode.State === 5 && item.CncBaseInfo.monitorNo != null && monitorNos.indexOf(item.CncBaseInfo.monitorNo) < 0) {
          monitorNos.push(item.CncBaseInfo.monitorNo)
        }
      })
      getToolMaintainStatus(monitorNos).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.maintainStatus = responseData.data;
        }
      })
      this.getTimeArea();
      this.getLastDayOee();
      this.getLastDayAbnormalCount();
      this.getLastDayScrapRate();
      this.getAbnormalInfo();
    },
    getLastDayOee() {
      getLastDayTotalTime(this.timeArea.startTime).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          responseData.data.forEach(item => {
            const oee = (item.totalTime * 1.0 / (item.runCount * 24 * 60 * 60) * 100).toFixed(2)
            this.lastDayTotalTime[item.area] = oee
          })
        }
      })
    },
    getTimeArea() {
      const currentTime = new Date();
      let startTime = '';
      let endTime = '';
      let tempTime = new Date(new Date(new Date().toLocaleDateString()).getTime() + 7 * 60 * 60 * 1000 + 30 * 60 * 1000)
      if (currentTime <= tempTime) {
        tempTime.setDate(tempTime.getDate() - 2)
      } else {
        tempTime.setDate(tempTime.getDate() - 1)
      }
      startTime = this.$moment(tempTime).format('YYYY-MM-DD HH:mm:ss');
      tempTime.setDate(tempTime.getDate() + 1)
      endTime = this.$moment(tempTime).format('YYYY-MM-DD HH:mm:ss');
      this.timeArea.startTime = startTime
      this.timeArea.endTime = endTime
    },
    getLastDayScrapRate() {
      getLastDayScrapCount(this.timeArea.startTime).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          const {scrap, out} = responseData.data
          this.lastDayScrapRate.scrapCount = scrap
          this.lastDayScrapRate.outCount = out
          if (out > 0) {
            this.lastDayScrapRate.rate = (scrap * 1.0 / out * 100).toFixed(2)
          } else {
            this.lastDayScrapRate.rate = '0.00'

          }

        }
      })
    },

    getLastDayAbnormalCount() {
      getLastDayAbnormalCount(this.timeArea.startTime, this.timeArea.endTime).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.lastDayAbnormalCount = responseData.data
        }
      })
    },
    getMachineAreaInfo() {
      getAreaInfo().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          const {areaInfo, areaCode} = responseData.data
          this.areaInfo = areaInfo
          this.areaCode = areaCode
        }
      })
    },
    getAbnormalType() {
      getAbnormalType().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.abnormalType = responseData.data
        }
      })
    }
  },
  watch: {},
  created() {
    this.timer = setInterval(() => {
      this.getToolMaintainStatus()
    }, 10000)
  },
  beforeUnmount() {
    clearInterval(this.timer);
    this.disconnect();
  }
}
</script>

<style lang="scss" scoped>
.el-card ::v-deep(.el-card__header) {
  padding: 8px 20px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  font-weight: bold;
  font-size: 20px;
}

.warp-title {
  overflow: hidden;

  ul {
    list-style: none;
    padding: 0;
    margin: 0 auto;
  }

  li {
    height: 20px;
    line-height: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
  }
}

.warp-content {
  overflow: hidden;

  ul {
    list-style: none;
    padding: 0;
    margin: 0 auto;
  }

  li {
    height: 43px;
    line-height: 43px;
    display: flex;
    justify-content: space-between;
    font-size: 15px;
  }
}
</style>
