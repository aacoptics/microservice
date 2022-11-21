<template>
  <!--  <div v-if="this.showContent">-->
  <div class="aac-container">
    <div style="margin-bottom: 20px">
      <div v-for="(item, index) of statusRadio" :key="index"
           :style="'background-color:' + statusInfo[item].color + ';text-align:center'"
           class="status_radio_type">
        {{ statusInfo[item].desc }}
      </div>
    </div>
    <el-row :gutter="10">
      <el-col :span="12">
        <div class="cz_floor_plan">
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(9)">Room #9</el-tag>
          <el-row>
            <!--              <el-col v-for="(machineInfo, index) of this.room9Info" :span="3" :key="index">-->
            <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
            <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px' }"-->
            <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
            <!--                  <el-tooltip placement="top">-->
            <!--                    <template #content>-->
            <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
            <!--                      <p>Side: {{machineInfo.side}}</p>-->
            <!--                      <p>Process: {{machineInfo.process}}</p>-->
            <!--                      <p>ML: {{machineInfo.ml}}</p>-->
            <!--                      <p>OP id: {{}}</p>-->
            <!--                    </template>-->
            <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
            <!--                  </el-tooltip>-->
            <!--                </el-card>-->
            <!--              </el-col>-->
            <el-col v-for="(machineInfo, index) of this.room9InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px' }"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room9InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px' }"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
          </el-row>

        </div>
      </el-col>

      <el-col :span="12">
        <div class="cz_floor_plan">
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(4)">Room #4</el-tag>
          <el-row>
            <!--              <el-col v-for="(machineInfo, index) of this.room4Info" :span="3" :key="index">-->
            <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
            <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"-->
            <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
            <!--                  <el-tooltip placement="top">-->
            <!--                    <template #content>-->
            <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
            <!--                      <p>Side: {{machineInfo.side}}</p>-->
            <!--                      <p>Process: {{machineInfo.process}}</p>-->
            <!--                      <p>ML: {{machineInfo.ml}}</p>-->
            <!--                      <p>OP id: {{}}</p>-->
            <!--                    </template>-->
            <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
            <!--                  </el-tooltip>-->
            <!--                </el-card>-->
            <!--              </el-col>-->
            <el-col v-for="(machineInfo, index) of this.room4InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room4InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
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
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(8)">Room #8</el-tag>
          <el-row>
            <!--              <el-col v-for="(machineInfo, index) of this.room8Info" :span="3" :key="index">-->
            <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
            <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"-->
            <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
            <!--                  <el-tooltip placement="top">-->
            <!--                    <template #content>-->
            <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
            <!--                      <p>Side: {{machineInfo.side}}</p>-->
            <!--                      <p>Process: {{machineInfo.process}}</p>-->
            <!--                      <p>ML: {{machineInfo.ml}}</p>-->
            <!--                      <p>OP id: {{}}</p>-->
            <!--                    </template>-->
            <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
            <!--                  </el-tooltip>-->
            <!--                </el-card>-->
            <!--              </el-col>-->
            <el-col v-for="(machineInfo, index) of this.room8InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room8InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="cz_floor_plan">
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(3)">Room #3</el-tag>
          <el-row>
            <!--              <el-col v-for="(machineInfo, index) of this.room3Info" :span="3" :key="index">-->
            <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
            <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"-->
            <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
            <!--                  <el-tooltip placement="top">-->
            <!--                    <template #content>-->
            <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
            <!--                      <p>Side: {{machineInfo.side}}</p>-->
            <!--                      <p>Process: {{machineInfo.process}}</p>-->
            <!--                      <p>ML: {{machineInfo.ml}}</p>-->
            <!--                      <p>OP id: {{}}</p>-->
            <!--                    </template>-->
            <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
            <!--                  </el-tooltip>-->
            <!--                </el-card>-->
            <!--              </el-col>-->
            <el-col v-for="(machineInfo, index) of this.room3InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room3InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
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
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(7)">Room #7</el-tag>
          <el-row>
            <!--              <el-col v-for="(machineInfo, index) of this.room7Info" :span="3" :key="index">-->
            <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
            <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"-->
            <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
            <!--                  <el-tooltip placement="top">-->
            <!--                    <template #content>-->
            <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
            <!--                      <p>Side: {{machineInfo.side}}</p>-->
            <!--                      <p>Process: {{machineInfo.process}}</p>-->
            <!--                      <p>ML: {{machineInfo.ml}}</p>-->
            <!--                      <p>OP id: {{}}</p>-->
            <!--                    </template>-->
            <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
            <!--                  </el-tooltip>-->
            <!--                </el-card>-->
            <!--              </el-col>-->
            <el-col v-for="(machineInfo, index) of this.room7InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room7InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="cz_floor_plan">
          <el-row>
            <el-col :span="4">
              <div class="grid-content bg-blue"><span style="color: white; font-size:13px">Cleaning room</span></div>
            </el-col>
            <el-col :span="4">
              <div class="grid-content bg-blue"><span style="color: white; ">Mikura</span></div>
            </el-col>
            <el-col :span="4">
              <div class="grid-content bg-blue"><span style="color: white; ">Microscope</span></div>
            </el-col>
            <el-col :span="4">
              <div class="grid-content bg-blue"><span style="color: white; ">Microscope</span></div>
            </el-col>
            <el-col :span="4">
              <div class="grid-content bg-blue"><span style="color: white; ">Microscope</span></div>
            </el-col>
            <el-col :span="2">
              <div class="grid-content bg-blue"><span style="color: white; ">UA3P</span></div>
            </el-col>
            <el-col :span="2">
              <div class="grid-content bg-blue"><span style="color: white; ">UA3P</span></div>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="12">
        <div class="cz_floor_plan">
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(6)">Room #6</el-tag>
          <el-row>
            <!--              <el-col v-for="(machineInfo, index) of this.room6Info" :span="3" :key="index">-->
            <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
            <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"-->
            <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
            <!--                  <el-tooltip placement="top">-->
            <!--                    <template #content>-->
            <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
            <!--                      <p>Side: {{machineInfo.side}}</p>-->
            <!--                      <p>Process: {{machineInfo.process}}</p>-->
            <!--                      <p>ML: {{machineInfo.ml}}</p>-->
            <!--                      <p>OP id: {{}}</p>-->
            <!--                    </template>-->
            <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
            <!--                  </el-tooltip>-->
            <!--                </el-card>-->
            <!--              </el-col>-->
            <el-col v-for="(machineInfo, index) of this.room6InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room6InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="cz_floor_plan">
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(2)">Room #2</el-tag>
          <el-row>
            <!--              <el-col v-for="(machineInfo, index) of this.room2Info" :span="3" :key="index">-->
            <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
            <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"-->
            <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
            <!--                  <el-tooltip placement="top">-->
            <!--                    <template #content>-->
            <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
            <!--                      <p>Side: {{machineInfo.side}}</p>-->
            <!--                      <p>Process: {{machineInfo.process}}</p>-->
            <!--                      <p>ML: {{machineInfo.ml}}</p>-->
            <!--                      <p>OP id: {{}}</p>-->
            <!--                    </template>-->
            <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
            <!--                  </el-tooltip>-->
            <!--                </el-card>-->
            <!--              </el-col>-->
            <el-col v-for="(machineInfo, index) of this.room2InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room2InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
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
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(5)">Room #5</el-tag>
          <el-row>
            <!--              <el-col v-for="(machineInfo, index) of this.room5Info" :span="3" :key="index">-->
            <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
            <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"-->
            <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
            <!--                  <el-tooltip placement="top">-->
            <!--                    <template #content>-->
            <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
            <!--                      <p>Side: {{machineInfo.side}}</p>-->
            <!--                      <p>Process: {{machineInfo.process}}</p>-->
            <!--                      <p>ML: {{machineInfo.ml}}</p>-->
            <!--                      <p>OP id: {{}}</p>-->
            <!--                    </template>-->
            <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
            <!--                  </el-tooltip>-->
            <!--                </el-card>-->
            <!--              </el-col>-->
            <el-col v-for="(machineInfo, index) of this.room5InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room5InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="cz_floor_plan">
          <el-tag style="cursor: pointer;font-size: 20px" @click="roomDetail(1)">Room #1</el-tag>
          <!--            <el-row>-->
          <!--              <el-col v-for="(machineInfo, index) of this.room1Info" :span="3" :key="index">-->
          <!--                <el-card style="border:1px rgba(59,162,114,1);margin:5px;" class="cz_floor_type" @click="onClick(machineInfo.machineNo)"-->
          <!--                         :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"-->
          <!--                         :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'">-->
          <!--                  <el-tooltip placement="top">-->
          <!--                    <template #content>-->
          <!--                      <p>Mold: {{machineInfo.mold}}</p>-->
          <!--                      <p>Side: {{machineInfo.side}}</p>-->
          <!--                      <p>Process: {{machineInfo.process}}</p>-->
          <!--                      <p>ML: {{machineInfo.ml}}</p>-->
          <!--                      <p>OP id: {{}}</p>-->
          <!--                    </template>-->
          <!--                    <p style="text-align: center;font-weight: bold;">FG{{machineInfo.machineNo}}</p>-->
          <!--                  </el-tooltip>-->
          <!--                </el-card>-->
          <!--              </el-col>-->
          <!--            </el-row>-->
          <el-row>
            <el-col v-for="(machineInfo, index) of this.room1InfoEven" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
            <el-col v-for="(machineInfo, index) of this.room1InfoOdd" :key="index" :span="3">
              <el-card :body-style="{ paddingLeft: '0px',paddingRight: '0px',paddingTop:'11px',paddingBottom:'11px'}"
                       :style="'background-color:' + statusInfo[machineInfo.showStatusCode].color + ';cursor: pointer'"
                       class="cz_floor_type"
                       style="border:1px rgba(59,162,114,1);margin:5px;"
                       @click="onClick(machineInfo.machineNo)">
                <el-tooltip placement="top">
                  <template #content>
                    <p>Mold: {{ machineInfo.mold }}</p>
                    <p>Side: {{ machineInfo.side }}</p>
                    <p>Process: {{ machineInfo.process }}</p>
                    <p>ML: {{ machineInfo.ml }}</p>
                    <p>HMI version: {{ machineInfo.hmiVersion }}</p>
                    <p>OP id: {{}}</p>
                  </template>
                  <p style="text-align: center;font-weight: bold;">FG{{ machineInfo.machineNo }}</p>
                </el-tooltip>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <el-dialog v-model="roomDetailDialogVisible" destroy-on-close title="Room Detail" width="100%">
      <room-detail ref="roomDetail" :floor-number="floorNo"></room-detail>
    </el-dialog>
    <el-dialog v-model="machineDetailDialogVisible" destroy-on-close title="Machine Detail" width="80%">
      <machine-detail ref="machineDetail" :machine-no="machineNo"></machine-detail>
    </el-dialog>
  </div>
  <!--  </div>-->
</template>

<style type="text/css">
.cz_floor_plan {
  border: 1px solid cornflowerblue;
  height: 140px;
  margin-top: 10px;
}
</style>
<script>
import {getAllFloorPlanMachineInfo} from "@/api/lens/iot/czech";
import roomDetail from "./RoomDetail"
import machineDetail from "./MachineDetail"

export default {
  name: "FloorPlan",
  components: {roomDetail, machineDetail},
  data() {
    return {
      room1Info: [],
      room1InfoEven: [],
      room1InfoOdd: [],
      room2Info: [],
      room2InfoEven: [],
      room2InfoOdd: [],
      room3Info: [],
      room3InfoEven: [],
      room3InfoOdd: [],
      room4Info: [],
      room4InfoEven: [],
      room4InfoOdd: [],
      room5Info: [],
      room5InfoEven: [],
      room5InfoOdd: [],
      room6Info: [],
      room6InfoEven: [],
      room6InfoOdd: [],
      room7Info: [],
      room7InfoEven: [],
      room7InfoOdd: [],
      room8Info: [],
      room8InfoEven: [],
      room8InfoOdd: [],
      room9Info: [],
      room9InfoEven: [],
      room9InfoOdd: [],
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
            if (item.machineNo.indexOf('1') === 0) {
              // this.room1Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room1InfoEven.push(item)
              } else {
                this.room1InfoOdd.push(item)
              }
            } else if (item.machineNo.indexOf('2') === 0) {
              // this.room2Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room2InfoEven.push(item)
              } else {
                this.room2InfoOdd.push(item)
              }
            } else if (item.machineNo.indexOf('3') === 0) {
              // this.room3Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room3InfoEven.push(item)
              } else {
                this.room3InfoOdd.push(item)
              }
            } else if (item.machineNo.indexOf('4') === 0) {
              // this.room4Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room4InfoEven.push(item)
              } else {
                this.room4InfoOdd.push(item)
              }
            } else if (item.machineNo.indexOf('5') === 0) {
              // this.room5Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room5InfoEven.push(item)
              } else {
                this.room5InfoOdd.push(item)
              }
            } else if (item.machineNo.indexOf('6') === 0) {
              // this.room6Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room6InfoEven.push(item)
              } else {
                this.room6InfoOdd.push(item)
              }
            } else if (item.machineNo.indexOf('7') === 0) {
              // this.room7Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room7InfoEven.push(item)
              } else {
                this.room7InfoOdd.push(item)
              }
            } else if (item.machineNo.indexOf('8') === 0) {
              // this.room8Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room8InfoEven.push(item)
              } else {
                this.room8InfoOdd.push(item)
              }
            } else if (item.machineNo.indexOf('9') === 0) {
              // this.room9Info.push(item)
              if (Number(item.machineNo) % 2 === 0) {
                this.room9InfoEven.push(item)
              } else {
                this.room9InfoOdd.push(item)
              }
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
