<template>
  <div>
    <div class="aac-container">
      <mqtt-client ref="mqttClient" :value="fanucTopic" @messageArrived="messageArrived"/>
      <div style="margin-bottom: 20px">
        <div v-for="(item, index) of statusRadio" :key="index"
             :style="'background-color:' + getStatusRadioColor(item) + ';text-align:center'"
             class="status_radio_type"
             @click="onStatusRadioClick(item)">
          {{ this.status[item] + '(' + this.statusCount[item] + ')' }}
        </div>
      </div>
      <el-card v-for="(val, key, index) in fanucMachineInfo" :key="index"
               :body-style="{ padding: '0px', height:'255px'}"
               class="fanuc_card_type"
               shadow="hover"
               style="cursor: pointer"
               @click="onCardClick(key)">
        <p style="text-align: center;font-weight: bold;color: #008000;font-size: 24px">{{ key }}</p>
        <el-row style="text-align: center;height:30px; font-weight: bold;font-size: 16px;">
          <el-col :span="24">
            <div :style="'background-color:' + getStatusColor(val.Status) + ';height:30px;line-height:30px'">
              {{ this.status[val.Status] ? this.status[val.Status] : this.status.default }}
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <div style="font-weight: bold">
              <p style="margin-top: 5px">模具文件名：{{ val.MoldFileName }}</p>
              <el-row>
                <el-col :span="12">射出数：{{ val.ShotCount }}</el-col>
                <el-col v-if="checkMachineStatus(val.Status)" :span="12">周期：{{ val.Cycle }}秒</el-col>
                <el-col v-else :span="12">周期：</el-col>
              </el-row>
              <!--              <p >-->
              <!--                <span style="margin-right: 10px">{{ item.monitShotCount }}</span>-->
              <!--                <span>{{ item.monitCycle }}秒</span>-->
              <!--              </p>-->
              <el-row>
                <el-col :span="12">良品数：{{ val.GoodCount }}</el-col>
                <el-col v-if="checkMachineStatus(val.Status)" :span="12">时间：{{
                    this.$moment(val.Date + val.Time, 'YYMMDDHHmmss')
                        .format('MM/DD HH:mm')
                  }}
                </el-col>
                <el-col v-else :span="12">时间：</el-col>
              </el-row>
              <!--              <p v-if="checkMachineStatus(item.monitStatus)" style="text-align: center">-->
              <!--                <span style="margin-right: 10px">{{ item.monitGoodCount }}</span>-->
              <!--                <span>{{-->
              <!--                    this.$moment(item.monitDateTime, 'YYMMDDHHmmss')-->
              <!--                        .format('MM/DD HH:mm')-->
              <!--                  }}</span>-->
              <!--              </p>-->
            </div>
          </el-col>
          <!--          <el-col :span="7">-->
          <!--            <div :class="getStatusType(item.monitStatus)">{{ getStatusName(item.monitStatus) }}-->
          <!--            </div>-->
          <!--          </el-col>-->
        </el-row>
        <el-row style="border-top: 1px dashed cornflowerblue">
          <div :id="'circleChart' + key"
               style="height: 120px;width: 280px"></div>
        </el-row>
      </el-card>

      <el-dialog
          v-model="dialogVisible"
          title="注塑机详细"
          width="98%"
          @closed="onDialogClosed"
          @opened="showPieChart">
        <el-row style="border: 1px solid #2d8cf0">
          <el-col :span="12" style="border-right: 1px solid #2d8cf0">
            <el-row>
              <div style="font-weight: bold;color: white;background-color: #2d8cf0;width: 100%">设备详细信息</div>
            </el-row>
            <el-row style="margin-bottom: 10px">
              <el-col :span="8">
                <img src="@/assets/img/FanucMachine.jpg" style="width: 100%;height: 100%"/></el-col>
              <el-col :span="10" style="font-weight: bold">
                <el-row style="margin-bottom: 10px">
                  <p>机器名：</p>
                  <p style="color: green">{{ this.dialogMachineName }}</p>
                </el-row>
                <el-row style="margin-bottom: 10px">
                  <p>当前状态：</p>
                  <p style="color: green">{{
                      this.status[this.fanucMonitorData[this.dialogMachineName].Data.Status] ?
                          this.status[this.fanucMonitorData[this.dialogMachineName].Data.Status]
                          : this.status.default
                    }}</p>
                </el-row>
                <el-row style="margin-bottom: 10px">
                  <p>模具文件名：</p>
                  <p style="color: green">{{ this.fanucMonitorData[this.dialogMachineName].Data.MoldFileName }}</p>
                </el-row>
                <el-row style="margin-bottom: 10px">
                  <p>HR模式：</p>
                  <p style="color: green">{{ this.fanucCondData[this.dialogMachineName].Data.cond_hr_mode }}</p>
                </el-row>
                <el-row style="margin-bottom: 10px">
                  <p>实时成型间隔时间(s)：</p>
                  <p style="color: green">{{ this.fanucMonitorData[this.dialogMachineName].Data.Cycle }}</p>
                </el-row>
                <el-row style="margin-bottom: 10px">
                  <p>最后一次成型时间：</p>
                  <p style="color: green">{{
                      this.$moment(this.fanucMonitorData[this.dialogMachineName].Data.Date
                          + this.fanucMonitorData[this.dialogMachineName].Data.Time, 'YYMMDDHHmmss')
                          .format('MM/DD HH:mm:ss')
                    }}</p>
                </el-row>
                <el-row style="margin-bottom: 10px">
                  <p>最大射出速度(mm/s)：</p>
                  <p style="color: green">{{ this.fanucCondData[this.dialogMachineName].Data.cond_inj_speed1 }}</p>
                </el-row>
                <el-row style="margin-bottom: 10px">
                  <p>最大射出压力(kgf/cm2)：</p>
                  <p style="color: green">{{ this.fanucCondData[this.dialogMachineName].Data.cond_max_inj_pres }}</p>
                </el-row>
                <el-row style="margin-bottom: 10px">
                  <p>保压切换模式：</p>
                  <p style="color: green">{{ this.fanucCondData[this.dialogMachineName].Data.cond_trans_mode }}</p>
                </el-row>
              </el-col>
              <el-col :span="6" style="justify-content: flex-end;">
                <el-row style="font-weight: bold;font-size: large">
                  <p>成型次数</p>
                </el-row>
                <el-row style="font-family: 'led regular';font-size: xxx-large;color: green">
                  <p>{{ this.fanucMonitorData[this.dialogMachineName].Data.ShotCount }}</p>
                </el-row>
                <el-row style="font-weight: bold;font-size: large">
                  <p>良品数</p>
                </el-row>
                <el-row style="font-family: 'led regular';font-size: xxx-large;color: green">
                  <p>{{ this.fanucMonitorData[this.dialogMachineName].Data.GoodCount }}</p>
                </el-row>
                <el-row style="font-weight: bold;font-size: large">
                  <p>良率（%）</p>
                </el-row>
                <el-row style="font-family: 'led regular';font-size: xxx-large;color: green">
                  <p>{{
                      Math.round(this.fanucMonitorData[this.dialogMachineName].Data.GoodCount / this.fanucMonitorData[this.dialogMachineName].Data.ShotCount * 10000) / 100.00
                    }}%</p>
                </el-row>
              </el-col>
            </el-row>
            <el-row>
              <div style="font-weight: bold;color: white;background-color: #2d8cf0;width: 100%">设备状态</div>
            </el-row>
            <el-row>
              <div id='pieChart'
                   style="width: 98%;
                     height: 300px;
                     background-color: white;
                     margin-top: 10px">
              </div>
            </el-row>
          </el-col>
          <el-col :span="12">
            <el-radio-group v-model="tabRadio" style="margin-bottom: 10px" @change="onRadioClick">
              <el-radio-button label="成型条件"></el-radio-button>
              <el-radio-button label="监控数据"></el-radio-button>
              <el-radio-button label="报警履历"></el-radio-button>
              <!--              <el-radio-button label="数据分析"></el-radio-button>-->
            </el-radio-group>
            <div v-if="tabRadio === '成型条件'" class="block">
              <el-date-picker
                  v-model="dateTimePickerValue"
                  :shortcuts="shortcuts"
                  end-placeholder="结束日期"
                  range-separator="至"
                  size="small"
                  start-placeholder="开始日期"
                  type="datetimerange">
              </el-date-picker>
              <el-button size="small" style="margin-left: 10px" type="primary"
                         @click="getDialogMachineCondData">查询
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                </template>
              </el-button>
              <el-button size="small" type="success"
                         @click="exportExcel('#condDataTable', 'CondData.xlsx')">导出
                <template #icon>
                  <font-awesome-icon :icon="['fas','download']"/>
                </template>
              </el-button>
              <el-table
                  id="condDataTable"
                  :key="1"
                  :data="fanucDialogCondData"
                  border
                  header-row-class-name="tableHead"
                  height=495px
                  v-loading="queryLoading"
                  style="width: 100%;margin-top: 10px">
                <el-table-column :width="80" label="机台号" prop="monitMcName"></el-table-column>
                <el-table-column :width="20 * '机台ID'.length" label="机台ID" prop="monitMcId"></el-table-column>
                <el-table-column :width="150" label="项目号" prop="condMoldFileName"></el-table-column>
                <el-table-column :width="20 * '模具ID'.length" label="模具ID" prop="condMoldId"></el-table-column>
                <el-table-column :width="20 * '保压段数'.length" label="保压段数" prop="condPackStep"></el-table-column>
                <el-table-column :width="20 * '保压1'.length" label="保压1" prop="condPackPres1"></el-table-column>
                <el-table-column :width="20 * '保压2'.length" label="保压2" prop="condPackPres2"></el-table-column>
                <el-table-column :width="20 * '保压3'.length" label="保压3" prop="condPackPres3"></el-table-column>
                <el-table-column :width="20 * '保压4'.length" label="保压4" prop="condPackPres4"></el-table-column>
                <el-table-column :width="20 * '保压5'.length" label="保压5" prop="condPackPres5"></el-table-column>
                <el-table-column :width="20 * '保压6'.length" label="保压6" prop="condPackPres6"></el-table-column>
                <el-table-column :width="20 * '计量前压力'.length" label="计量前压力"
                                 prop="condBefExtPres"></el-table-column>
                <el-table-column :width="20 * '保压时间1'.length" label="保压时间1"
                                 prop="condPackTime1"></el-table-column>
                <el-table-column :width="20 * '保压时间2'.length" label="保压时间2"
                                 prop="condPackTime2"></el-table-column>
                <el-table-column :width="20 * '保压时间3'.length" label="保压时间3"
                                 prop="condPackTime3"></el-table-column>
                <el-table-column :width="20 * '保压时间4'.length" label="保压时间4"
                                 prop="condPackTime4"></el-table-column>
                <el-table-column :width="20 * '保压时间5'.length" label="保压时间5"
                                 prop="condPackTime5"></el-table-column>
                <el-table-column :width="20 * '保压时间6'.length" label="保压时间6"
                                 prop="condPackTime6"></el-table-column>
                <el-table-column :width="20 * '计量前保压时间'.length" label="计量前保压时间"
                                 prop="condBefExtTime"></el-table-column>
                <el-table-column :width="20 * '最大射出压力'.length" label="最大射出压力"
                                 prop="condMaxInjPres"></el-table-column>
                <el-table-column :width="20 * '最大射出时间'.length" label="最大射出时间"
                                 prop="condMaxInjTime"></el-table-column>
                <el-table-column :width="20 * '最大保压速度'.length" label="最大保压速度"
                                 prop="condMaxPackVel"></el-table-column>
                <el-table-column :width="20 * '加速度时间'.length" label="加速度时间"
                                 prop="condAccelTime"></el-table-column>
                <el-table-column :width="20 * '加速度'.length" label="加速度" prop="condAcceleration"></el-table-column>
                <el-table-column :width="20 * '计量位置'.length" label="计量位置" prop="condShotSize"></el-table-column>
                <el-table-column :width="20 * '射出段数'.length" label="射出段数" prop="condInjStep"></el-table-column>
                <el-table-column :width="20 * '射出速度1'.length" label="射出速度1"
                                 prop="condInjSpeed1"></el-table-column>
                <el-table-column :width="20 * '射出速度2'.length" label="射出速度2"
                                 prop="condInjSpeed2"></el-table-column>
                <el-table-column :width="20 * '射出速度3'.length" label="射出速度3"
                                 prop="condInjSpeed3"></el-table-column>
                <el-table-column :width="20 * '射出速度4'.length" label="射出速度4"
                                 prop="condInjSpeed4"></el-table-column>
                <el-table-column :width="20 * '射出速度5'.length" label="射出速度5"
                                 prop="condInjSpeed5"></el-table-column>
                <el-table-column :width="20 * '射出速度6'.length" label="射出速度6"
                                 prop="condInjSpeed6"></el-table-column>
                <el-table-column :width="20 * '通常清料背压'.length" label="通常清料背压"
                                 prop="condPurgePressure"></el-table-column>
                <el-table-column :width="20 * '通常清料转速'.length" label="通常清料转速"
                                 prop="condPurgeRotation"></el-table-column>
                <el-table-column :width="20 * '自动模厚锁模力'.length" label="自动模厚锁模力"
                                 prop="condAutoDieHForce"></el-table-column>
                <el-table-column :width="20 * '射出速度7'.length" label="射出速度7"
                                 prop="condInjSpeed7"></el-table-column>
                <el-table-column :width="20 * '射出速度8'.length" label="射出速度8"
                                 prop="condInjSpeed8"></el-table-column>
                <el-table-column :width="20 * '射出速度9'.length" label="射出速度9"
                                 prop="condInjSpeed9"></el-table-column>
                <el-table-column :width="20 * '射出速度10'.length" label="射出速度10"
                                 prop="condInjSpeed10"></el-table-column>
                <el-table-column :width="20 * '最大射出压1'.length" label="最大射出压1"
                                 prop="condMaxInjectPress1"></el-table-column>
                <el-table-column :width="20 * '最大射出压2'.length" label="最大射出压2"
                                 prop="condMaxInjectPress2"></el-table-column>
                <el-table-column :width="20 * '最大射出压3'.length" label="最大射出压3"
                                 prop="condMaxInjectPress3"></el-table-column>
                <el-table-column :width="20 * '最大射出压4'.length" label="最大射出压4"
                                 prop="condMaxInjectPress4"></el-table-column>
                <el-table-column :width="20 * '最大射出压5'.length" label="最大射出压5"
                                 prop="condMaxInjectPress5"></el-table-column>
                <el-table-column :width="20 * '最大射出压6'.length" label="最大射出压6"
                                 prop="condMaxInjectPress6"></el-table-column>
                <el-table-column :width="20 * '最大射出压7'.length" label="最大射出压7"
                                 prop="condMaxInjectPress7"></el-table-column>
                <el-table-column :width="20 * '最大射出压8'.length" label="最大射出压8"
                                 prop="condMaxInjectPress8"></el-table-column>
                <el-table-column :width="20 * '最大射出压9'.length" label="最大射出压9"
                                 prop="condMaxInjectPress9"></el-table-column>
                <el-table-column :width="20 * '最大射出压10'.length" label="最大射出压10"
                                 prop="condMaxInjectPress10"></el-table-column>
                <el-table-column :width="20 * '射出切换位置1'.length" label="射出切换位置1"
                                 prop="condInjSwitchPos1"></el-table-column>
                <el-table-column :width="20 * '射出切换位置2'.length" label="射出切换位置2"
                                 prop="condInjSwitchPos2"></el-table-column>
                <el-table-column :width="20 * '射出切换位置3'.length" label="射出切换位置3"
                                 prop="condInjSwitchPos3"></el-table-column>
                <el-table-column :width="20 * '射出切换位置4'.length" label="射出切换位置4"
                                 prop="condInjSwitchPos4"></el-table-column>
                <el-table-column :width="20 * '射出切换位置5'.length" label="射出切换位置5"
                                 prop="condInjSwitchPos5"></el-table-column>
                <el-table-column :width="20 * '射出切换位置6'.length" label="射出切换位置6"
                                 prop="condInjSwitchPos6"></el-table-column>
                <el-table-column :width="20 * '射出切换位置7'.length" label="射出切换位置7"
                                 prop="condInjSwitchPos7"></el-table-column>
                <el-table-column :width="20 * '射出切换位置8'.length" label="射出切换位置8"
                                 prop="condInjSwitchPos8"></el-table-column>
                <el-table-column :width="20 * '射出切换位置9'.length" label="射出切换位置9"
                                 prop="condInjSwitchPos9"></el-table-column>
                <el-table-column :width="20 * '射出控制模式'.length" label="射出控制模式"
                                 prop="condInjectMode"></el-table-column>
                <el-table-column :width="20 * '切换位置'.length" label="切换位置"
                                 prop="condTransPosition"></el-table-column>
                <el-table-column :width="20 * '切换压力'.length" label="切换压力"
                                 prop="condTransPressure"></el-table-column>
                <el-table-column :width="20 * '切换压力段数'.length" label="切换压力段数"
                                 prop="condTransPressStep"></el-table-column>
                <el-table-column :width="20 * '切换模内/喷嘴压力'.length" label="切换模内/喷嘴压力"
                                 prop="condTransCavNzlPrs"></el-table-column>
                <el-table-column :width="20 * '切换模内压/NZ压段数'.length" label="切换模内压/NZ压段数"
                                 prop="condTransCavityStep"></el-table-column>
                <el-table-column :width="20 * '切换喷嘴压力'.length" label="切换喷嘴压力"
                                 prop="condTransNozzlePrs"></el-table-column>
                <el-table-column :width="20 * '切换喷嘴压力段数'.length" label="切换喷嘴压力段数"
                                 prop="condTransNozzleStep"></el-table-column>
                <el-table-column :width="20 * '信号切换段数'.length" label="信号切换段数"
                                 prop="condSgnlTransfStep"></el-table-column>
                <el-table-column :width="20 * '计量段数'.length" label="计量段数"
                                 prop="condExtrdStep"></el-table-column>
                <el-table-column :width="20 * '背压1'.length" label="背压1" prop="condBackPres1"></el-table-column>
                <el-table-column :width="20 * '背压2'.length" label="背压2" prop="condBackPres2"></el-table-column>
                <el-table-column :width="20 * '背压3'.length" label="背压3" prop="condBackPres3"></el-table-column>
                <el-table-column :width="20 * '背压4'.length" label="背压4" prop="condBackPres4"></el-table-column>
                <el-table-column :width="20 * '背压5'.length" label="背压5" prop="condBackPres5"></el-table-column>
                <el-table-column :width="20 * '背压6'.length" label="背压6" prop="condBackPres6"></el-table-column>
                <el-table-column :width="20 * '螺杆转速1'.length" label="螺杆转速1"
                                 prop="condScrewRotate1"></el-table-column>
                <el-table-column :width="20 * '螺杆转速2'.length" label="螺杆转速2"
                                 prop="condScrewRotate2"></el-table-column>
                <el-table-column :width="20 * '螺杆转速3'.length" label="螺杆转速3"
                                 prop="condScrewRotate3"></el-table-column>
                <el-table-column :width="20 * '螺杆转速4'.length" label="螺杆转速4"
                                 prop="condScrewRotate4"></el-table-column>
                <el-table-column :width="20 * '螺杆转速5'.length" label="螺杆转速5"
                                 prop="condScrewRotate5"></el-table-column>
                <el-table-column :width="20 * '螺杆转速6'.length" label="螺杆转速6"
                                 prop="condScrewRotate6"></el-table-column>
                <el-table-column :width="20 * '料筒保持温度'.length" label="料筒保持温度"
                                 prop="condNzl1HoldTemp"></el-table-column>
                <el-table-column :width="20 * '喷嘴2保持温度'.length" label="喷嘴2保持温度"
                                 prop="condNzl2HoldTemp"></el-table-column>
                <el-table-column :width="20 * '喷嘴连接器保持温度'.length" label="喷嘴连接器保持温度"
                                 prop="condNzlAdaptHoldTemp"></el-table-column>
                <el-table-column :width="20 * '料筒1保持温度'.length" label="料筒1保持温度"
                                 prop="condBrl1HoldTemp"></el-table-column>
                <el-table-column :width="20 * '料筒2保持温度'.length" label="料筒2保持温度"
                                 prop="condBrl2HoldTemp"></el-table-column>
                <el-table-column :width="20 * '料筒3保持温度'.length" label="料筒3保持温度"
                                 prop="condBrl3HoldTemp"></el-table-column>
                <el-table-column :width="20 * '料筒4保持温度'.length" label="料筒4保持温度"
                                 prop="condBrl4HoldTemp"></el-table-column>
                <el-table-column :width="20 * '料筒5保持温度'.length" label="料筒5保持温度"
                                 prop="condBrl5HoldTemp"></el-table-column>
                <el-table-column :width="20 * '料筒6保持温度'.length" label="料筒6保持温度"
                                 prop="condBrl6HoldTemp"></el-table-column>
                <el-table-column :width="20 * '喷嘴1设定温度'.length" label="喷嘴1设定温度"
                                 prop="condNozzle1Set"></el-table-column>
                <el-table-column :width="20 * '料筒1设定温度'.length" label="料筒1设定温度"
                                 prop="condBarrel1Set"></el-table-column>
                <el-table-column :width="20 * '料筒2设定温度'.length" label="料筒2设定温度"
                                 prop="condBarrel2Set"></el-table-column>
                <el-table-column :width="20 * '料筒3设定温度'.length" label="料筒3设定温度"
                                 prop="condBarrel3Set"></el-table-column>
                <el-table-column :width="20 * '料筒4设定温度'.length" label="料筒4设定温度"
                                 prop="condBarrel4Set"></el-table-column>
                <el-table-column :width="20 * '料筒5设定温度'.length" label="料筒5设定温度"
                                 prop="condBarrel5Set"></el-table-column>
                <el-table-column :width="20 * '料筒6设定温度'.length" label="料筒6设定温度"
                                 prop="condBarrel6Set"></el-table-column>
                <el-table-column :width="20 * '模具1设定温度'.length" label="模具1设定温度"
                                 prop="condMold1Set"></el-table-column>
                <el-table-column :width="20 * '模具2设定温度'.length" label="模具2设定温度"
                                 prop="condMold2Set"></el-table-column>
                <el-table-column :width="20 * '顶出开始模式:模具'.length" label="顶出开始模式:模具"
                                 prop="condEjectStartModeMold"></el-table-column>
                <el-table-column :width="20 * '顶出开始位置:模具'.length" label="顶出开始位置:模具"
                                 prop="condEjectStartPosMold"></el-table-column>
                <el-table-column :width="20 * '加速度模式'.length" label="加速度模式"
                                 prop="condAccelMode"></el-table-column>
                <el-table-column :width="20 * '计量模式'.length" label="计量模式" prop="condExtrdSw"></el-table-column>
                <el-table-column :width="20 * '计量切换位置1'.length" label="计量切换位置1"
                                 prop="condExtrdSwPos1"></el-table-column>
                <el-table-column :width="20 * '计量切换位置2'.length" label="计量切换位置2"
                                 prop="condExtrdSwPos2"></el-table-column>
                <el-table-column :width="20 * '计量切换位置3'.length" label="计量切换位置3"
                                 prop="condExtrdSwPos3"></el-table-column>
                <el-table-column :width="20 * '计量切换位置4'.length" label="计量切换位置4"
                                 prop="condExtrdSwPos4"></el-table-column>
                <el-table-column :width="20 * '计量切换位置5'.length" label="计量切换位置5"
                                 prop="condExtrdSwPos5"></el-table-column>
                <el-table-column :width="20 * '减压距离'.length" label="减压距离" prop="condDcmpDist"></el-table-column>
                <el-table-column :width="20 * '减压速度'.length" label="减压速度" prop="condDcmpVel"></el-table-column>
                <el-table-column :width="20 * '冷却时间'.length" label="冷却时间"
                                 prop="condCoolTime1"></el-table-column>
                <el-table-column :width="20 * '冷却时间'.length" label="冷却时间"
                                 prop="condCoolTime2"></el-table-column>
                <el-table-column :width="20 * '喷嘴1上限温度'.length" label="喷嘴1上限温度"
                                 prop="condNozzle1High"></el-table-column>
                <el-table-column :width="20 * '料筒1上限温度'.length" label="料筒1上限温度"
                                 prop="condBarrel1High"></el-table-column>
                <el-table-column :width="20 * '料筒2上限温度'.length" label="料筒2上限温度"
                                 prop="condBarrel2High"></el-table-column>
                <el-table-column :width="20 * '料筒3上限温度'.length" label="料筒3上限温度"
                                 prop="condBarrel3High"></el-table-column>
                <el-table-column :width="20 * '料斗下上限温度'.length" label="料斗下上限温度"
                                 prop="condFeedThroatHigh"></el-table-column>
                <el-table-column :width="20 * '模具1上限温度'.length" label="模具1上限温度"
                                 prop="condMold1High"></el-table-column>
                <el-table-column :width="20 * '模具2上限温度'.length" label="模具2上限温度"
                                 prop="condMold2High"></el-table-column>
                <el-table-column :width="20 * '模具3上限温度'.length" label="模具3上限温度"
                                 prop="condMold3High"></el-table-column>
                <el-table-column :width="20 * '模具4上限温度'.length" label="模具4上限温度"
                                 prop="condMold4High"></el-table-column>
                <el-table-column :width="20 * '料斗下设定温度'.length" label="料斗下设定温度"
                                 prop="condFeedThroatSet"></el-table-column>
                <el-table-column :width="20 * '模具3设定温度'.length" label="模具3设定温度"
                                 prop="condMold3Set"></el-table-column>
                <el-table-column :width="20 * '模具4设定温度'.length" label="模具4设定温度"
                                 prop="condMold4Set"></el-table-column>
                <el-table-column :width="20 * '喷嘴1下限温度'.length" label="喷嘴1下限温度"
                                 prop="condNozzle1Low"></el-table-column>
                <el-table-column :width="20 * '料筒1下限温度'.length" label="料筒1下限温度"
                                 prop="condBarrel1Low"></el-table-column>
                <el-table-column :width="20 * '料筒2下限温度'.length" label="料筒2下限温度"
                                 prop="condBarrel2Low"></el-table-column>
                <el-table-column :width="20 * '料筒3下限温度'.length" label="料筒3下限温度"
                                 prop="condBarrel3Low"></el-table-column>
                <el-table-column :width="20 * '料斗下下限温度'.length" label="料斗下下限温度"
                                 prop="condFeedThroatLow"></el-table-column>
                <el-table-column :width="20 * '模具1下限温度'.length" label="模具1下限温度"
                                 prop="condMold1Low"></el-table-column>
                <el-table-column :width="20 * '模具2下限温度'.length" label="模具2下限温度"
                                 prop="condMold2Low"></el-table-column>
                <el-table-column :width="20 * '模具3下限温度'.length" label="模具3下限温度"
                                 prop="condMold3Low"></el-table-column>
                <el-table-column :width="20 * '模具4下限温度'.length" label="模具4下限温度"
                                 prop="condMold4Low"></el-table-column>
                <el-table-column :width="20 * '闭模限'.length" label="闭模限"
                                 prop="condCloseLimitPos"></el-table-column>
                <el-table-column :width="20 * '闭模变速位置1'.length" label="闭模变速位置1"
                                 prop="condCloseSwPos1"></el-table-column>
                <el-table-column :width="20 * '闭模变速位置2'.length" label="闭模变速位置2"
                                 prop="condCloseSwPos2"></el-table-column>
                <el-table-column :width="20 * '闭模变速位置3'.length" label="闭模变速位置3"
                                 prop="condCloseSwPos3"></el-table-column>
                <el-table-column :width="20 * '模具保护位置'.length" label="模具保护位置"
                                 prop="condMoldProtect"></el-table-column>
                <el-table-column :width="20 * '模具接触位置'.length" label="模具接触位置"
                                 prop="condMoldTouchPos"></el-table-column>
                <el-table-column :width="20 * '开模变速位置1'.length" label="开模变速位置1"
                                 prop="condOpenSwPos1"></el-table-column>
                <el-table-column :width="20 * '开模变速位置2'.length" label="开模变速位置2"
                                 prop="condOpenSwPos2"></el-table-column>
                <el-table-column :width="20 * '开模变速位置3'.length" label="开模变速位置3"
                                 prop="condOpenSwPos3"></el-table-column>
                <el-table-column :width="20 * '开模变速位置4'.length" label="开模变速位置4"
                                 prop="condOpenSwPos4"></el-table-column>
                <el-table-column :width="20 * '开模结束位置'.length" label="开模结束位置"
                                 prop="condFullyOpenPos"></el-table-column>
                <el-table-column :width="20 * '顶出开始位置'.length" label="顶出开始位置"
                                 prop="condEjectStartPos"></el-table-column>
                <el-table-column :width="20 * '闭模速度1'.length" label="闭模速度1"
                                 prop="condCloseVel1"></el-table-column>
                <el-table-column :width="20 * '闭模速度2'.length" label="闭模速度2"
                                 prop="condCloseVel2"></el-table-column>
                <el-table-column :width="20 * '闭模速度3'.length" label="闭模速度3"
                                 prop="condCloseVel3"></el-table-column>
                <el-table-column :width="20 * '闭模速度4'.length" label="闭模速度4"
                                 prop="condCloseVel4"></el-table-column>
                <el-table-column :width="20 * '模具接触速度'.length" label="模具接触速度"
                                 prop="condMoldTouchVel"></el-table-column>
                <el-table-column :width="20 * '开模速度1'.length" label="开模速度1"
                                 prop="condOpenVel1"></el-table-column>
                <el-table-column :width="20 * '开模速度2'.length" label="开模速度2"
                                 prop="condOpenVel2"></el-table-column>
                <el-table-column :width="20 * '开模速度3'.length" label="开模速度3"
                                 prop="condOpenVel3"></el-table-column>
                <el-table-column :width="20 * '开模速度4'.length" label="开模速度4"
                                 prop="condOpenVel4"></el-table-column>
                <el-table-column :width="20 * '开模结束速度'.length" label="开模结束速度"
                                 prop="condFullyOpenVel"></el-table-column>
                <el-table-column :width="20 * '闭模段数'.length" label="闭模段数"
                                 prop="condCloseStep"></el-table-column>
                <el-table-column :width="20 * '开模段数'.length" label="开模段数" prop="condOpenStep"></el-table-column>
                <el-table-column :width="20 * '模具保护力1'.length" label="模具保护力1"
                                 prop="condMoldProtect1"></el-table-column>
                <el-table-column :width="20 * '模具保护力1(减)'.length" label="模具保护力1(减)"
                                 prop="condMoldProtect1Minus"></el-table-column>
                <el-table-column :width="20 * '模具保护力2'.length" label="模具保护力2"
                                 prop="condMoldProtect2"></el-table-column>
                <el-table-column :width="20 * '模具保护力2(减)'.length" label="模具保护力2(减)"
                                 prop="condMoldProtect2Minus"></el-table-column>
                <el-table-column :width="20 * '模具保护时间1'.length" label="模具保护时间1"
                                 prop="condProtectTime1"></el-table-column>
                <el-table-column :width="20 * '模具保护时间2'.length" label="模具保护时间2"
                                 prop="condProtectTime2"></el-table-column>
                <el-table-column :width="20 * '顶杆动作类型'.length" label="顶杆动作类型"
                                 prop="condEjectPattern1"></el-table-column>
                <el-table-column :width="20 * '顶出次数'.length" label="顶出次数"
                                 prop="condEjectPulse"></el-table-column>
                <el-table-column :width="20 * '顶出开始模式'.length" label="顶出开始模式"
                                 prop="condEjectStartMode"></el-table-column>
                <el-table-column :width="20 * '顶杆后退位置'.length" label="顶杆后退位置"
                                 prop="condEjectRetractPos"></el-table-column>
                <el-table-column :width="20 * '顶杆前进位置'.length" label="顶杆前进位置"
                                 prop="condEjectFullyAdvance"></el-table-column>
                <el-table-column :width="20 * '顶杆后退速度'.length" label="顶杆后退速度"
                                 prop="condEjectRetractVel"></el-table-column>
                <el-table-column :width="20 * '顶杆前进速度'.length" label="顶杆前进速度"
                                 prop="condEjectAdvanceVel"></el-table-column>
                <el-table-column :width="20 * '顶杆动作类型'.length" label="顶杆动作类型"
                                 prop="condEjectPattern2"></el-table-column>
                <el-table-column :width="20 * '顶杆后退停止'.length" label="顶杆后退停止"
                                 prop="condEjectDwellInRet"></el-table-column>
                <el-table-column :width="20 * '顶杆前进停止'.length" label="顶杆前进停止"
                                 prop="condEjectDwellInAdv"></el-table-column>
                <el-table-column :width="20 * 'HR模式'.length" label="HR模式" prop="condHrMode"></el-table-column>
                <el-table-column :width="20 * '切换模式'.length" label="切换模式"
                                 prop="condTransMode"></el-table-column>
                <el-table-column :width="20 * '工序监视项目05'.length" label="工序监视项目05"
                                 prop="condProcMoniItem5"></el-table-column>
                <el-table-column :width="160" label="插入时间" prop="dbCreateTime">
                  <template #default="scope">
                    <span>{{ this.$moment(scope.row.dbCreateTime).format("YYYY-MM-DD HH:mm:ss") }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div v-if="tabRadio === '监控数据'" class="block">
              <el-date-picker
                  v-model="dateTimePickerValue"
                  :shortcuts="shortcuts"
                  end-placeholder="结束日期"
                  range-separator="至"
                  size="small"
                  start-placeholder="开始日期"
                  type="datetimerange">
              </el-date-picker>
              <el-button size="small" style="margin-left: 10px" type="primary"
                         @click="getDialogMachineMonitData">查询
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                </template>
              </el-button>
              <el-button size="small" type="success"
                         @click="json2Excel(fanucDialogMonitData, 'MonitData.xlsx')">导出
                <template #icon>
                  <font-awesome-icon :icon="['fas','download']"/>
                </template>
              </el-button>
              <div style="height: 495px">
                <el-auto-resizer>
                  <template #default="{ height, width }">
                    <el-table-v2
                        :columns="fanucDialogMonitDataColumns"
                        :data="fanucDialogMonitData"
                        :width="width"
                        :height="height"
                        v-loading="queryLoading"
                        fixed
                    />
                  </template>
                </el-auto-resizer>
              </div>
              <!--              <el-table-->
              <!--                  id="monitDataTable"-->
              <!--                  :data="fanucDialogMonitData"-->
              <!--                  border-->
              <!--                  header-row-class-name="tableHead"-->
              <!--                  height=495px-->
              <!--                  v-loading="queryLoading"-->
              <!--                  style="width: 100%;margin-top: 10px">-->
              <!--                <el-table-column :width="80" label="机台号" prop="monitMcName"></el-table-column>-->
              <!--                <el-table-column :width="20 * '机台ID'.length" label="机台ID" prop="monitMcId"></el-table-column>-->
              <!--                <el-table-column :width="150" label="项目号" prop="condMoldFileName"></el-table-column>-->
              <!--                <el-table-column :width="185" label="注塑时间" prop="monitDateTime"></el-table-column>-->
              <!--                <el-table-column :width="45" label="状态" prop="monitStatus"></el-table-column>-->
              <!--                <el-table-column :width="20 * '循环时间'.length" label="循环时间" prop="monitCycle"></el-table-column>-->
              <!--                <el-table-column :width="65" label="循环数" prop="monitCyclecount"></el-table-column>-->
              <!--                <el-table-column :width="65" label="射出数" prop="monitShotcount"></el-table-column>-->
              <!--                <el-table-column :width="65" label="正品数" prop="monitGoodcount"></el-table-column>-->
              <!--                <el-table-column :width="20 * '射出时间'.length" label="射出时间" prop="monitInjTime"></el-table-column>-->
              <!--                <el-table-column :width="20 * '计量时间'.length" label="计量时间"-->
              <!--                                 prop="monitRecovTime"></el-table-column>-->
              <!--                <el-table-column :width="20 * '最小缓冲'.length" label="最小缓冲"-->
              <!--                                 prop="monitMCushion"></el-table-column>-->
              <!--                <el-table-column :width="20 * '计量位置'.length" label="计量位置"-->
              <!--                                 prop="monitExtrdPos"></el-table-column>-->
              <!--                <el-table-column :width="20 * '峰值压'.length" label="峰值压" prop="monitPeakPrs"></el-table-column>-->
              <!--                <el-table-column :width="20 * 'V_P位置'.length" label="V_P位置" prop="monitVPPos"></el-table-column>-->
              <!--                <el-table-column :width="20 * '模具1'.length" label="模具1" prop="monitMold1"></el-table-column>-->
              <!--                <el-table-column :width="20 * '模具2'.length" label="模具2" prop="monitMold2"></el-table-column>-->
              <!--                <el-table-column :width="20 * '喷嘴1'.length" label="喷嘴1" prop="monitNozzle"></el-table-column>-->
              <!--                <el-table-column :width="20 * '喷嘴2'.length" label="喷嘴2" prop="monitNozzle2"></el-table-column>-->
              <!--                <el-table-column :width="20 * '料筒1'.length" label="料筒1" prop="monitBarrel1"></el-table-column>-->
              <!--                <el-table-column :width="20 * '料筒2'.length" label="料筒2" prop="monitBarrel2"></el-table-column>-->
              <!--                <el-table-column :width="20 * '料筒3'.length" label="料筒3" prop="monitBarrel3"></el-table-column>-->
              <!--                <el-table-column :width="20 * '料筒4'.length" label="料筒4" prop="monitBarrel4"></el-table-column>-->
              <!--                <el-table-column :width="20 * '料斗下温度'.length" label="料斗下温度"-->
              <!--                                 prop="monitFeedTh"></el-table-column>-->
              <!--                <el-table-column :width="20 * '计量开始位置'.length" label="计量开始位置"-->
              <!--                                 prop="monitExtrdStart"></el-table-column>-->
              <!--                <el-table-column :width="20 * '计量扭矩'.length" label="计量扭矩"-->
              <!--                                 prop="monitExtrdTorq"></el-table-column>-->
              <!--                <el-table-column :width="20 * '模具3'.length" label="模具3" prop="monitMold3"></el-table-column>-->
              <!--                <el-table-column :width="20 * '模具4'.length" label="模具4" prop="monitMold4"></el-table-column>-->
              <!--                <el-table-column :width="20 * '峰值时间'.length" label="峰值时间" prop="monitPeakT"></el-table-column>-->
              <!--                <el-table-column :width="20 * '峰值位置'.length" label="峰值位置" prop="monitPeakPos"></el-table-column>-->
              <!--                <el-table-column :width="20 * '推顶固定偏差扭矩'.length" label="推顶固定偏差扭矩"-->
              <!--                                 prop="monitEjeDevStTrq"></el-table-column>-->
              <!--                <el-table-column :width="20 * '关闭时间'.length" label="关闭时间"-->
              <!--                                 prop="monitCloseTime"></el-table-column>-->
              <!--                <el-table-column :width="20 * 'V-P压力'.length" label="V-P压力" prop="monitVPPrs"></el-table-column>-->
              <!--                <el-table-column :width="20 * '模具5'.length" label="模具5" prop="monitMold5"></el-table-column>-->
              <!--                <el-table-column :width="20 * '模具6'.length" label="模具6" prop="monitMold6"></el-table-column>-->
              <!--                <el-table-column :width="20 * '射出开始压'.length" label="射出开始压"-->
              <!--                                 prop="monitInjPres"></el-table-column>-->
              <!--                <el-table-column :width="20 * 'V-P补偿'.length" label="V-P补偿" prop="monitVPAdj"></el-table-column>-->
              <!--                <el-table-column :width="20 * '逆流峰值'.length" label="逆流峰值" prop="monitFlwPeak"></el-table-column>-->
              <!--                <el-table-column :width="20 * '逆流量'.length" label="逆流量" prop="monitBackflw"></el-table-column>-->
              <!--                <el-table-column :width="20 * '锁模时间'.length" label="锁模时间"-->
              <!--                                 prop="monitLockupTim"></el-table-column>-->
              <!--                <el-table-column :width="20 * '取出时间'.length" label="取出时间"-->
              <!--                                 prop="monitPickupTim"></el-table-column>-->
              <!--                <el-table-column :width="20 * '树脂滞留时间'.length" label="树脂滞留时间"-->
              <!--                                 prop="monitResidenceT"></el-table-column>-->
              <!--                <el-table-column :width="20 * '顶杆平均偏差扭矩'.length" label="顶杆平均偏差扭矩"-->
              <!--                                 prop="monitEjeDevAvTrq"></el-table-column>-->
              <!--                <el-table-column :width="20 * '模具7'.length" label="模具7" prop="monitMold7"></el-table-column>-->
              <!--                <el-table-column :width="20 * '模具8'.length" label="模具8" prop="monitMold8"></el-table-column>-->
              <!--                <el-table-column :width="20 * '射出开始位置'.length" label="射出开始位置"-->
              <!--                                 prop="monitInjStartPos"></el-table-column>-->
              <!--                <el-table-column :width="20 * '螺杆旋转量'.length" label="螺杆旋转量"-->
              <!--                                 prop="monitScrewRevolution"></el-table-column>-->
              <!--                <el-table-column :width="185" label="插入时间" prop="dbCreateTime"></el-table-column>-->
              <!--              </el-table>-->
            </div>
            <div v-if="tabRadio === '报警履历'" class="block">
              <el-date-picker
                  v-model="dateTimePickerValue"
                  :shortcuts="shortcuts"
                  end-placeholder="结束日期"
                  range-separator="至"
                  size="small"
                  start-placeholder="开始日期"
                  type="datetimerange">
              </el-date-picker>
              <el-button size="small" style="margin-left: 10px" type="primary"
                         @click="getDialogMachineAlarmData">查询
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
                </template>
              </el-button>
              <el-button size="small" type="success"
                         @click="exportExcel('#alarmDataTable', 'AlarmData.xlsx')">导出
                <template #icon>
                  <font-awesome-icon :icon="['fas','download']"/>
                </template>
              </el-button>
              <el-table
                  id="alarmDataTable"
                  :data="fanucDialogAlarmData"
                  border
                  header-row-class-name="tableHead"
                  height=495px
                  v-loading="queryLoading"
                  style="width: 100%;margin-top: 10px">
                <el-table-column :width="80" label="机台号" prop="monitMcName"></el-table-column>
                <el-table-column :width="20 * '机台ID'.length" label="机台ID" prop="monitMcId"></el-table-column>
                <el-table-column :width="20 * '报警代码'.length" label="报警代码" prop="alarmCode"></el-table-column>
                <el-table-column :width="200" label="报警描述" prop="alarmContent"></el-table-column>
                <el-table-column :width="180" label="报警开始时间" prop="alarmDate"></el-table-column>
                <el-table-column :width="180" label="报警结束时间" prop="alarmResetTime"></el-table-column>
                <el-table-column :width="180" label="持续时间" prop="alarmDownTime"></el-table-column>
                <el-table-column :width="160" label="插入时间" prop="dbCreateTime">
                  <template #default="scope">
                    <span>{{ this.$moment(scope.row.dbCreateTime).format("YYYY-MM-DD HH:mm:ss") }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <!--            <div v-if="tabRadio === '数据分析'" class="block">-->
            <!--              数据分析-->
            <!--            </div>-->
          </el-col>
        </el-row>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import {getAlarmData, getCondData, getMonitData, selectEquips} from "@/api/lens/iot/fanucNe";
import FileSaver from 'file-saver'
import XLSX from 'xlsx'
import MqttClient from "@/components/MqttClient";

export default {
  name: "FanucMonitor",
  components: {
    MqttClient
  },
  mounted() {
    let _this = this;
    setTimeout(function () {
      _this.getEquipList()
      const position = _this.$route.query.position
      _this.floorInfo = position.substring(2, position.length)
      _this.siteInfo = _this.$route.query.site
      _this.initConnect()
    }, 100);

    //this.getFanucDataByFloor()
  },
  computed: {
    fanucTopic() {
      return [
        {
          topic: 'Fanuc/' + this.siteInfo + '/monitData/' + this.floorInfo + '/+',
          qos: 0
        },
        {
          topic: 'Fanuc/' + this.siteInfo + '/condData/' + this.floorInfo + '/+',
          qos: 0
        },
        {
          topic: 'Fanuc/' + this.siteInfo + '/moldData/' + this.floorInfo + '/+',
          qos: 0
        }
      ]
    },
    fanucMachineInfo() {
      return this.getFanucMachineInfo()
    }
  },
  methods: {
    getFanucMachineInfo() {
      const pages = {}
      const position = this.$route.query.position;
      if (this.$route.params.status) {
        this.setStatusRadioValue(this.$route.params.status);
        if (this.$route.params.status === 'default') {
          this.pushStatusRadioValue('01')
          this.pushStatusRadioValue('16')
          this.pushStatusRadioValue('11')
          this.pushStatusRadioValue('50')
          this.pushStatusRadioValue('00')
        }
        this.refreshPage()
      }
      this.setDefaultCount()
      const floor = position.substring(2, position.length)
      for (const item in this.fanucMonitorData) {
        if (item.indexOf(floor) === -1)
          continue;
        if (this.status[this.fanucMonitorData[item].Data.Status]) {
          if (this.statusRadioValue.indexOf(this.fanucMonitorData[item].Data.Status) > -1) {
            pages[item] = this.fanucMonitorData[item].Data
          }
          this.statusCount[this.fanucMonitorData[item].Data.Status]++
        } else {
          if (this.statusRadioValue.indexOf("default") > -1) {
            pages[item] = this.fanucMonitorData[item].Data
          }
          this.statusCount.default++
        }
      }
      return this.sortObjByKey(pages)
    },
    sortObjByKey(obj) {
      const keys = Object.keys(obj).sort();
      const newObj = {};
      for (let i = 0; i < keys.length; i++) {
        const index = keys[i];
        if (this.equipList.indexOf(index) === -1)
          continue
        newObj[index] = obj[index];
      }
      return newObj;
    },
    initConnect() {
      this.$nextTick(() => {
        this.$refs.mqttClient.createMqttConnection();
      });
    },
    //接收消息
    messageArrived(msg) {
      switch (msg.Message) {
        case 'monitData':
          this.fanucMonitorData[msg.ClientId] = msg
          break;
        case 'moldData':
          this.fanucMoldData[msg.ClientId] = msg
          setTimeout(() => {
            this.refreshData('circleChart' + msg.ClientId)
          }, 100)
          break;
        case 'condData':
          this.fanucCondData[msg.ClientId] = msg
          break;
      }
    },
    //断开连接
    disconnect() {
      this.$refs.mqttClient.destroyMqttConnection();
    },
    refreshPage() {
      this.$router.push(this.$route.fullPath)
    },
    setStatusRadioValue(status) {
      this.statusRadioValue = []
      this.statusRadioValue.push(status)
    },
    pushStatusRadioValue(status) {
      this.statusRadioValue.push(status)
    },
    setAllStatusRadioValue() {
      this.statusRadioValue = ['02', '00', '01', '17', '03', '16', '11', '50', '-1', 'default']
    },
    getStatusColor(statusCode) {
      return this.statusColor[statusCode] ? this.statusColor[statusCode] : this.statusColor.default
    },
    getStatusRadioColor(statusCode) {
      if (this.statusRadioValue.indexOf(statusCode) > -1)
        return this.statusColor[statusCode]
      else
        return "rgba(235,235,235,1)"
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
    json2Excel(data, excelFileName) {
      const wopts = {
        bookType: 'xlsx',
        bookSST: false,
        type: 'binary'
      };
      const workBook = {
        SheetNames: ['Sheet1'],
        Sheets: {},
        Props: {}
      };
      //1、XLSX.utils.json_to_sheet(data) 接收一个对象数组并返回一个基于对象关键字自动生成的“标题”的工作表，默认的列顺序由使用Object.keys的字段的第一次出现确定
      //2、将数据放入对象workBook的Sheets中等待输出
      workBook.Sheets['Sheet1'] = XLSX.utils.json_to_sheet(data)

      //3、XLSX.write() 开始编写Excel表格
      //4、changeData() 将数据处理成需要输出的格式
      saveAs(new Blob([this.changeData(XLSX.write(workBook, wopts))], {type: 'application/octet-stream'}), excelFileName)
    },
    changeData(s) {

      //如果存在ArrayBuffer对象(es6) 最好采用该对象
      if (typeof ArrayBuffer !== 'undefined') {

        //1、创建一个字节长度为s.length的内存区域
        var buf = new ArrayBuffer(s.length);

        //2、创建一个指向buf的Unit8视图，开始于字节0，直到缓冲区的末尾
        var view = new Uint8Array(buf);

        //3、返回指定位置的字符的Unicode编码
        for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
        return buf;

      } else {
        var buf = new Array(s.length);
        for (var i = 0; i != s.length; ++i) buf[i] = s.charCodeAt(i) & 0xFF;
        return buf;
      }
    },
    onStatusRadioClick(statusCode) {
      const idx = this.statusRadioValue.indexOf(statusCode)
      if (idx > -1)
        this.statusRadioValue.splice(idx, 1)
      else
        this.statusRadioValue.push(statusCode)
    }
    ,
    onRadioClick() {
      this.fanucDialogCondData = []
      this.fanucDialogMonitData = []
    }
    ,
    checkMachineStatus(status) {
      return status !== '-1';
    }
    ,
    onCardClick(mcName) {
      this.dialogMachineName = mcName
      this.dialogVisible = true
    }
    ,
    onDialogClosed() {
      this.fanucDialogCondData = []
      this.fanucDialogMonitData = []
      this.fanucDialogAlarmData = []
      this.dateTimePickerValue = []
    }
    ,
    getEquipList() {
      selectEquips().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.equipList = responseData.data;
        }
      })
    }
    ,
    getDialogMachineCondData() {
      if (this.dateTimePickerValue.length !== 2) {
        this.$message.error("请选择查询时间段");
      } else {
        const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
        const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
        this.queryLoading = true
        getCondData(startTime, endTime, this.dialogMachineName).then((response) => {
          const responseData = response.data
          if (responseData.code === '000000') {
            this.fanucDialogCondData = responseData.data;
          }
          this.queryLoading = false
        }).catch(() => {
          this.queryLoading = false
        })
      }
    }
    ,
    getDialogMachineMonitData() {
      if (this.dateTimePickerValue.length !== 2) {
        this.$message.error("请选择查询时间段");
      } else {
        const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
        const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
        this.queryLoading = true
        getMonitData(startTime, endTime, this.dialogMachineName).then((response) => {
          const responseData = response.data
          if (responseData.code === '000000') {
            this.fanucDialogMonitData = responseData.data;
          }
          this.queryLoading = false
        }).catch(() => {
          this.queryLoading = false
        })
      }
    }
    ,
    getDialogMachineAlarmData() {
      if (this.dateTimePickerValue.length !== 2) {
        this.$message.error("请选择查询时间段");
      } else {
        const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
        const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
        this.queryLoading = true
        getAlarmData(startTime, endTime, this.dialogMachineName).then((response) => {
          const responseData = response.data
          if (responseData.code === '000000') {
            this.fanucDialogAlarmData = responseData.data;
          }
          this.queryLoading = false
        }).catch(() => {
          this.queryLoading = false
        })
      }
    }
    ,
    showPieChart() {
      const chartDom = document.getElementById('pieChart');
      const myChart = echarts.init(chartDom);
      let option;
      option = {
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} %"
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '状态',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '28',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: this.getMoldData(this.fanucMoldData[this.dialogMachineName])
          }
        ],
        color: ["rgba(59,162,114,1)", "rgba(250,200,88,1)", "rgba(84,112,198,1)", "rgba(238,102,102,1)", "rgba(154,96,180,1)", "gray", "rgba(252,132,82,1)"]
      };
      myChart.setOption(option);
    }
    ,
    drawCircleChart(chartId) {
      const chartDom = document.getElementById(chartId);
      const myChart = echarts.init(chartDom);
      let option;
      const listData = this.getMoldData(this.fanucMoldData[chartId.replace('circleChart', '')])
      option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {d}%'
        },
        legend: {
          itemWidth: 10,
          itemHeight: 10,
          icon: 'circle',
          x: 'right',
          y: 'center',
          orient: 'vertical', //设置图例排列纵向显示
          align: 'left',       //设置图例中文字位置在icon标识符的右侧
          itemGap: 3,         //设置图例之间的间距
          padding: [0, 0, 0, 0], //设置图例与圆环图之间的间距
          bottom: '55%',       //距离下边距
          formatter: function (name) {  //该函数用于设置图例显示后的百分比
            let value = 0;
            listData.forEach((item) => {
              if (item.name === name) {
                value = item.value;
              }
            })
            return `${name}  ${value}%`;  //返回出图例所显示的内容是名称+百分比
          },
          textStyle: { //图例文字的样式
            color: 'black',
            fontSize: 12,
          },
          data: ['自动运转', '运转待机', '手动运转', '报警', '生产完成', '停机', '其他'],
        },
        // graphic: [  //为环形图中间添加文字
        //   {
        //     tooltip: {
        //       trigger: 'item',
        //       formatter: "当前状态: " + this.status[fanucData.monitStatus]
        //     },
        //     elements: [{
        //       type: "circle",
        //       shape: {
        //         cx: 40,
        //         cy: 40,
        //         r: 20
        //
        //       },
        //       left: "12.3%",
        //       top: "39%",
        //       style: {
        //         fill: this.statusColor[fanucData.monitStatus],
        //       }
        //     }]
        //
        //   },
        // ],
        series: [{
          name: '状态占比',
          type: 'pie',
          radius: ['50%', '62%'],
          center: ['20%', '50%'],
          data: listData,
          label: false,
          labelLine: false,
        }
        ],
        color: ["rgba(59,162,114,1)", "rgba(250,200,88,1)", "rgba(84,112,198,1)", "rgb(238,102,102)", "rgba(154,96,180,1)", "gray", "rgba(252,132,82,1)"]
      }

      // setInterval(() => {
      //   const list = this.getMoldData(fanucData)
      //   myChart.setOption({
      //     legend: {
      //       formatter: function (name) {  //该函数用于设置图例显示后的百分比
      //         let value = 0;
      //         list.forEach((item) => {
      //           if (item.name === name) {
      //             value = item.value;
      //           }
      //         })
      //         return `${name}  ${value}%`;  //返回出图例所显示的内容是名称+百分比
      //       }
      //     },
      //     series: [{
      //       data: list
      //     }]
      //   });
      // }, 10000);

      myChart.setOption(option);
    }
    ,
    refreshData(chartId) {
      const chartDom = document.getElementById(chartId);
      if (!chartDom)
        return
      const myChart = echarts.getInstanceByDom(chartDom)
      if (!myChart) {
        this.drawCircleChart(chartId)
        return;
      }
      //更新数据
      const list = this.getMoldData(this.fanucMoldData[chartId.replace('circleChart', '')])
      myChart.setOption({
        legend: {
          formatter: function (name) {  //该函数用于设置图例显示后的百分比
            let value = 0;
            list.forEach((item) => {
              if (item.name === name) {
                value = item.value;
              }
            })
            return `${name}  ${value}%`;  //返回出图例所显示的内容是名称+百分比
          }
        },
        series: [{
          data: list
        }]
      });
      // const option = myChart.getOption();
      // option.series[0].data = data;
      // myChart.setOption(option);
    }
    ,
    getMoldData(fanucData) {
      const moldData = [];
      const mold_automate = fanucData.Data.mold_automate.replace('%', '').trim()
      const mold_wait = fanucData.Data.mold_wait.replace('%', '').trim()
      const mold_manual = fanucData.Data.mold_manual.replace('%', '').trim()
      const mold_alarm = fanucData.Data.mold_alarm.replace('%', '').trim()
      const mold_complete = fanucData.Data.mold_complete.replace('%', '').trim()
      const mold_shutdown = fanucData.Data.mold_shutdown.replace('%', '').trim()
      const mold_other = (100.00 - mold_automate - mold_wait - mold_manual - mold_alarm - mold_complete - mold_shutdown).toFixed(2)
      moldData.push({value: mold_automate, name: '自动运转'})
      moldData.push({value: mold_wait, name: '运转待机'})
      moldData.push({value: mold_manual, name: '手动运转'})
      moldData.push({value: mold_alarm, name: '报警'})
      moldData.push({value: mold_complete, name: '生产完成'})
      moldData.push({value: mold_shutdown, name: '停机'})
      moldData.push({value: mold_other, name: '其他'})
      return moldData;
    }
    ,
    setDefaultCount() {
      this.statusCount = {
        '02': 0,
        '00': 0,
        '01': 0,
        '17': 0,
        '03': 0,
        '16': 0,
        '11': 0,
        '50': 0,
        '-1': 0,
        default: 0
      }
    }
  },
  beforeUnmount() {
    this.disconnect();
  },
  data() {
    return {
      fanucDialogMonitDataColumns: [
        {
          key: 'monitMcName',
          dataKey: 'monitMcName',
          title: '机台号',
          width: 80,
        },
        {
          key: 'monitMcId',
          dataKey: 'monitMcId',
          title: '机台ID',
          width: 20 * '机台ID'.length,
        },
        {
          key: 'condMoldFileName',
          dataKey: 'condMoldFileName',
          title: '项目号',
          width: 150,
        },
        {
          key: 'monitDateTime',
          dataKey: 'monitDateTime',
          title: '注塑时间',
          width: 185,
        },
        {
          key: 'monitStatus',
          dataKey: 'monitStatus',
          title: '状态',
          width: 45,
        },
        {
          key: 'monitCycle',
          dataKey: 'monitCycle',
          title: '循环时间',
          width: 20 * '循环时间'.length,
        },
        {
          key: 'monitCyclecount',
          dataKey: 'monitCyclecount',
          title: '循环数',
          width: 65,
        },
        {
          key: 'monitShotcount',
          dataKey: 'monitShotcount',
          title: '射出数',
          width: 65,
        },
        {
          key: 'monitGoodcount',
          dataKey: 'monitGoodcount',
          title: '正品数',
          width: 65,
        },
        {
          key: 'monitInjTime',
          dataKey: 'monitInjTime',
          title: '射出时间',
          width: 20 * '射出时间'.length,
        },
        {
          key: 'monitRecovTime',
          dataKey: 'monitRecovTime',
          title: '计量时间',
          width: 20 * '计量时间'.length,
        },
        {
          key: 'monitMCushion',
          dataKey: 'monitMCushion',
          title: '最小缓冲',
          width: 20 * '最小缓冲'.length,
        },
        {
          key: 'monitExtrdPos',
          dataKey: 'monitExtrdPos',
          title: '计量位置',
          width: 20 * '计量位置'.length,
        },
        {
          key: 'monitPeakPrs',
          dataKey: 'monitPeakPrs',
          title: '峰值压',
          width: 20 * '峰值压'.length,
        },
        {
          key: 'monitVPPos',
          dataKey: 'monitVPPos',
          title: 'V_P位置',
          width: 20 * 'V_P位置'.length,
        },
        {
          key: 'monitMold1',
          dataKey: 'monitMold1',
          title: '模具1',
          width: 20 * '模具1'.length,
        },
        {
          key: 'monitMold2',
          dataKey: 'monitMold2',
          title: '模具2',
          width: 20 * '模具2'.length,
        },
        {
          key: 'monitMold3',
          dataKey: 'monitMold3',
          title: '模具3',
          width: 20 * '模具3'.length,
        },
        {
          key: 'monitMold4',
          dataKey: 'monitMold4',
          title: '模具4',
          width: 20 * '模具4'.length,
        },
        {
          key: 'monitMold5',
          dataKey: 'monitMold5',
          title: '模具5',
          width: 20 * '模具5'.length,
        },
        {
          key: 'monitMold6',
          dataKey: 'monitMold6',
          title: '模具6',
          width: 20 * '模具6'.length,
        },
        {
          key: 'monitMold7',
          dataKey: 'monitMold7',
          title: '模具7',
          width: 20 * '模具7'.length,
        },
        {
          key: 'monitMold8',
          dataKey: 'monitMold8',
          title: '模具8',
          width: 20 * '模具8'.length,
        },
        {
          key: 'monitNozzle',
          dataKey: 'monitNozzle',
          title: '喷嘴1',
          width: 20 * '喷嘴1'.length,
        },
        {
          key: 'monitNozzle2',
          dataKey: 'monitNozzle2',
          title: '喷嘴2',
          width: 20 * '喷嘴2'.length,
        },
        {
          key: 'monitBarrel1',
          dataKey: 'monitBarrel1',
          title: '料筒1',
          width: 20 * '料筒1'.length,
        },
        {
          key: 'monitBarrel2',
          dataKey: 'monitBarrel2',
          title: '料筒2',
          width: 20 * '料筒2'.length,
        },
        {
          key: 'monitBarrel3',
          dataKey: 'monitBarrel3',
          title: '料筒3',
          width: 20 * '料筒3'.length,
        },
        {
          key: 'monitBarrel4',
          dataKey: 'monitBarrel4',
          title: '料筒4',
          width: 20 * '料筒4'.length,
        },
        {
          key: 'monitFeedTh',
          dataKey: 'monitFeedTh',
          title: '料斗下温度',
          width: 20 * '料斗下温度'.length,
        },
        {
          key: 'monitExtrdStart',
          dataKey: 'monitExtrdStart',
          title: '计量开始位置',
          width: 20 * '计量开始位置'.length,
        },
        {
          key: 'monitExtrdTorq',
          dataKey: 'monitExtrdTorq',
          title: '计量扭矩',
          width: 20 * '计量扭矩'.length,
        },
        {
          key: 'monitPeakT',
          dataKey: 'monitPeakT',
          title: '峰值时间',
          width: 20 * '峰值时间'.length,
        },
        {
          key: 'monitPeakPos',
          dataKey: 'monitPeakPos',
          title: '峰值位置',
          width: 20 * '峰值位置'.length,
        },
        {
          key: 'monitEjeDevStTrq',
          dataKey: 'monitEjeDevStTrq',
          title: '推顶固定偏差扭矩',
          width: 20 * '推顶固定偏差扭矩'.length,
        },
        {
          key: 'monitCloseTime',
          dataKey: 'monitCloseTime',
          title: '关闭时间',
          width: 20 * '关闭时间'.length,
        },
        {
          key: 'monitVPPrs',
          dataKey: 'monitVPPrs',
          title: 'V-P压力',
          width: 20 * 'V-P压力'.length,
        },
        {
          key: 'monitInjPres',
          dataKey: 'monitInjPres',
          title: '射出开始压',
          width: 20 * '射出开始压'.length,
        },
        {
          key: 'monitVPAdj',
          dataKey: 'monitVPAdj',
          title: 'V-P补偿',
          width: 20 * 'V-P补偿'.length,
        },
        {
          key: 'monitFlwPeak',
          dataKey: 'monitFlwPeak',
          title: '逆流峰值',
          width: 20 * '逆流峰值'.length,
        },
        {
          key: 'monitBackflw',
          dataKey: 'monitBackflw',
          title: '逆流量',
          width: 20 * '逆流量'.length,
        },
        {
          key: 'monitLockupTim',
          dataKey: 'monitLockupTim',
          title: '锁模时间',
          width: 20 * '锁模时间'.length,
        },
        {
          key: 'monitPickupTim',
          dataKey: 'monitPickupTim',
          title: '取出时间',
          width: 20 * '取出时间'.length,
        },
        {
          key: 'monitResidenceT',
          dataKey: 'monitResidenceT',
          title: '树脂滞留时间',
          width: 20 * '树脂滞留时间'.length,
        },
        {
          key: 'monitEjeDevAvTrq',
          dataKey: 'monitEjeDevAvTrq',
          title: '顶杆平均偏差扭矩',
          width: 20 * '顶杆平均偏差扭矩'.length,
        },
        {
          key: 'monitInjStartPos',
          dataKey: 'monitInjStartPos',
          title: '射出开始位置',
          width: 20 * '射出开始位置'.length,
        },
        {
          key: 'monitScrewRevolution',
          dataKey: 'monitScrewRevolution',
          title: '螺杆旋转量',
          width: 20 * '螺杆旋转量'.length,
        },
        {
          key: 'dbCreateTime',
          dataKey: 'dbCreateTime',
          title: '插入时间',
          width: 160,
          cellRenderer: ({cellData: dbCreateTime}) => (
              <span>{this.$moment(dbCreateTime).format("YYYY-MM-DD HH:mm:ss")}</span>
          ),
        },
      ],
      queryLoading: false,
      floorInfo: '',
      siteInfo: '',
      client: {
        connected: false,
      },
      circlePieData: {},
      tabRadio: '成型条件',
      shortcuts: [{
        text: '最近一周',
        value: (() => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
          return [start, end]
        })()
      }, {
        text: '最近一个月',
        value: (() => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
          return [start, end]
        })()
      }, {
        text: '最近三个月',
        value: (() => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
          return [start, end]
        })()
      }],
      dateTimePickerValue: [],
      dialogMachineName: '',
      dialogVisible: false,
      fanucMonitorInfo: [],
      fanucMonitorData: {},
      fanucCondData: {},
      fanucMoldData: {},
      fanucDialogData: {
        monitData: {},
        condData: {},
        moldData: {},
        moldFileName: ""
      },
      fanucDialogCondData: [],
      fanucDialogMonitData: [],
      fanucDialogAlarmData: [],
      statusType: {
        '-1': 'item_disconnect',
        '00': 'item_manual',
        '01': 'item_wait',
        '02': 'item_automatic',
        '03': 'item_alarm',
        '17': 'item_hold',
        '50': 'item_semiAuto'
      },
      status: {
        '-1': '离线',
        '00': '手动运转',
        '01': '运转待机',
        '02': '自动运转',
        '03': '报警',
        '16': '冷间启动',
        '17': '低温保持',
        '11': '清料',
        '50': '半自动',
        default: '其他'
      },
      statusRadio: ['02', '00', '01', '17', '03', '16', '11', '50', '-1', 'default'],
      statusRadioValue: ['02', '00', '01', '17', '03', '16', '11', '50', '-1', 'default'],
      statusColor: {
        '-1': "gray",
        '00': "rgba(84,112,198,1)",
        '01': "rgba(250,200,88,1)",
        '02': "rgba(59,162,114,1)",
        '03': "rgba(238,102,102,1)",
        '17': "rgba(252,132,82,1)",
        '16': "rgba(252,132,82,1)",
        '11': "rgba(252,132,82,1)",
        '50': "rgba(154,96,180,1)",
        default: "rgba(252,132,82,1)"
      },
      statusCount: {
        '02': 0,
        '00': 0,
        '01': 0,
        '17': 0,
        '03': 0,
        '16': 0,
        '11': 0,
        '50': 0,
        '-1': 0,
        default: 0
      },
      equipList: []
    }
  },
  watch: {
    $route: {
      handler() {
        let _this = this;
        setTimeout(function () {
          _this.floorInfo = _this.$route.query.position.substring(2, _this.$route.query.position.length)
          _this.initConnect()
        }, 100);
      },
      deep: true,
    }
  }
};
</script>

<style scoped>
.fanuc_card_type {
  width: 300px;
  height: 260px;
  border: 1px solid cornflowerblue;
  background-color: #f0f0f0;
  margin-right: 70px;
  margin-bottom: 20px;
  float: left;
}

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
