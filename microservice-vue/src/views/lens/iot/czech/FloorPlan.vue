<template>
<!--  <div v-if="this.showContent">-->
    <div class="aac-container">
      <div style="margin-bottom: 20px">
        <div v-for="(item, index) of statusRadio" :key="index" class="status_radio_type"
             :style="'background-color:' + statusInfo[item].color + ';text-align:center'">
          {{ statusInfo[item].desc }}
        </div>
      </div>
      <el-row :gutter="10">
        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(9)" style="cursor: pointer;font-size: 20px">Room #9</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room9Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px' }"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>

          </div>
        </el-col>

        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(4)" style="cursor: pointer;font-size: 20px">Room #4</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room4Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(8)" style="cursor: pointer;font-size: 20px">Room #8</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room8Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-col>

        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(3)" style="cursor: pointer;font-size: 20px">Room #3</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room3Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(7)" style="cursor: pointer;font-size: 20px">Room #7</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room7Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-row>
              <el-col :span="4"><div class="grid-content bg-blue"><span style="color: white; font-size:13px">Cleaning room</span></div></el-col>
              <el-col :span="4"><div class="grid-content bg-blue"><span style="color: white; ">Mikura</span></div></el-col>
              <el-col :span="4"><div class="grid-content bg-blue"><span style="color: white; ">Microscope</span></div></el-col>
              <el-col :span="4"><div class="grid-content bg-blue"><span style="color: white; ">Microscope</span></div></el-col>
              <el-col :span="4"><div class="grid-content bg-blue"><span style="color: white; ">Microscope</span></div></el-col>
              <el-col :span="2"><div class="grid-content bg-blue"><span style="color: white; ">UA3P</span></div></el-col>
              <el-col :span="2"><div class="grid-content bg-blue"><span style="color: white; ">UA3P</span></div></el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(6)" style="cursor: pointer;font-size: 20px">Room #6</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room6Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(2)" style="cursor: pointer;font-size: 20px">Room #2</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room2Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(5)" style="cursor: pointer;font-size: 20px">Room #5</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room5Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="cz_floor_plan">
            <el-tag @click="roomDetail(1)" style="cursor: pointer;font-size: 20px">Room #1</el-tag>
            <el-row>
              <el-col v-for="(machineInfo, index) of this.room1Info" :span="3" :key="index">
                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"
                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">
                  <el-tooltip placement="top">
                    <template #content>
                      <p>Mold: {{machineInfo.mold}}</p>
                      <p>Side: {{machineInfo.side}}</p>
                      <p>Process: {{machineInfo.process}}</p>
                      <p>ML: {{machineInfo.ml}}</p>
                      <p>OP id: {{}}</p>
                    </template>
                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>
                  </el-tooltip>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-dialog v-model="roomDetailDialogVisible" width="100%"  destroy-on-close title="Room Detail">
        <room-detail ref="roomDetail" :floor-number="floorNo"></room-detail>
      </el-dialog>
      <el-dialog v-model="machineDetailDialogVisible" width="80%"  destroy-on-close title="Machine Detail">
        <machine-detail ref="machineDetail" :machine-no="machineNo"></machine-detail>
      </el-dialog>
    </div>
<!--  </div>-->
</template>

<style type="text/css">
.cz_floor_plan {
  border:1px solid cornflowerblue;
  height: 140px;
  margin-top: 10px;
}
</style>
<script>
import {
  getAllFloorPlanMachineInfo
} from "@/api/lens/iot/czech";
import roomDetail from "./RoomDetail"
import machineDetail from "./MachineDetail"
export default {
  name: "FloorPlan",
  components: { roomDetail, machineDetail },
  data() {
    return {
      room1Info: [],
      room2Info: [],
      room3Info: [],
      room4Info: [],
      room5Info: [],
      room6Info: [],
      room7Info: [],
      room8Info: [],
      room9Info: [],
      allRoomInfo: [],
      floorNo: 0,
      machineNo: '',
      roomDetailDialogVisible: false,
      machineDetailDialogVisible: false,
      showContent: false,
      statusRadio: [0, 1, 2, 3],
      statusInfo: {
        0: {
          desc: "Running",
          color: "rgba(59,162,114,1)"
        },
        1: {
          desc: "Idle",
          color: "rgba(252,132,82,1)"
        },
        2: {
          desc: "Failure",
          color: "rgba(255,0,0,1)"
        },
        3: {
          desc: "Maintenance",
          color: "grey"
        }
      },
    }
  },
  created() {
    this.getMachineInfo();
  },
  methods: {
    roomDetail(floorNumber) {
      this.floorNo = floorNumber;
      this.roomDetailDialogVisible = true;
      //this.$router.push({name: 'roomDetail', params:{floorNumber: floorNumber}});
      // console.log(floorNumber);
      // this.$router.push({name: 'roomDetail'});
    },
    getMachineInfo() {
      getAllFloorPlanMachineInfo().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.allRoomInfo = responseData.data;
          responseData.data.forEach(item => {
            if(item.machineNo.indexOf('1') === 0){
              this.room1Info.push(item)
            }
            else if(item.machineNo.indexOf('2') === 0){
              this.room2Info.push(item)
            }
            else if(item.machineNo.indexOf('3') === 0){
              this.room3Info.push(item)
            }
            else if(item.machineNo.indexOf('4') === 0){
              this.room4Info.push(item)
            }
            else if(item.machineNo.indexOf('5') === 0){
              this.room5Info.push(item)
            }
            else if(item.machineNo.indexOf('6') === 0){
              this.room6Info.push(item)
            }
            else if(item.machineNo.indexOf('7') === 0){
              this.room7Info.push(item)
            }
            else if(item.machineNo.indexOf('8') === 0){
              this.room8Info.push(item)
            }
            else if(item.machineNo.indexOf('9') === 0){
              this.room9Info.push(item)
            }
          })
        }
      });
    },
    onClick(machineNo) {
      this.machineNo = machineNo;
      this.machineDetailDialogVisible = true;
      //this.$router.push({name: 'machineDetail', params: {machineNo: machineNo}});
    }
  }
}
</script>

<style scoped>

</style>
