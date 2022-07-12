<template>
  <div>
    <div class="aac-container">
      <el-row>
        <el-col :span="10">
          <el-row style="padding: 10px">
            <el-input style="width: 250px;margin-right: 10px" v-model="monitorNo" placeholder="请输入监控号"></el-input>
            <el-button style="margin-left: 10px" type="primary" @click="getMachineNoByMonitorNo(null)">查询（全部）
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-row>
          <el-row style="padding: 10px" v-show="programSheetMachineList.length > 0">
            <el-select v-model="programSheetMachineNo" filterable placeholder="请选择机台号" style="width: 250px" @change="onProgramMachineChange">
              <el-option
                  v-for="item in programSheetMachineList"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
              >
              </el-option>
            </el-select>
          </el-row>
          <el-row style="padding: 10px">
            <el-button style="margin-right: 10px" type="primary" @click="getByMonitorNo(null)">查询（单类）
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
            <el-button style="margin-right: 10px" type="success" :loading="saveBtnLoading"
                       @click="saveEditInfo()">保存
              <template #icon>
                <font-awesome-icon :icon="['fas','check']"/>
              </template>
            </el-button>
            <el-button style="margin-right: 10px" type="warning" :loading="saveBtnLoading"
                       @click="addMachineProgramSheet()">新增程序单机台
              <template #icon>
                <font-awesome-icon :icon="['fas', 'plus']"/>
              </template>
            </el-button>
          </el-row>
          <el-row style="padding: 10px">
            <span style="padding-top: 5px">批量设置机台号：</span>
<!--            <el-input style="width: 250px;margin-right: 10px" v-model="allMachineNumber" placeholder="请输入机台号" @change="changeAllMachineNumber"></el-input>-->
            <el-select v-model="allMachineNumber" filterable placeholder="请选择机台号" style="width: 250px" @change="changeAllMachineNumber">
              <el-option
                  v-for="item in machineNameList"
                  :key="item.fequipName"
                  :label="item.fequipName"
                  :value="item.fequipName"
              >
              </el-option>
            </el-select>
          </el-row>
        </el-col>
        <el-col :span="14">
          <el-upload
              class="upload-demo"
              :before-upload="beforeUpload"
              accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
              :http-request="uploadExcel"
              action=""
              :multiple="false"
              :show-file-list="false"
              drag>
            <font-awesome-icon class="el-icon--upload" :icon="['fas','cloud-arrow-up']"/>
            <div class="el-upload__text">将Excel文件拖到此处，或<em>点击上传</em></div>
          </el-upload>
        </el-col>
      </el-row>
      <el-table
          id="ToolLifeTable"
          :data="moldToolLifeSheetByMachine"
          border
          :height="tableMaxHeight"
          :key="1"
          header-row-class-name="tableHead"
          style="width: 100%;margin-top: 10px"
          v-loading="toolLifeLoading"
          @cell-click="cellClick">
        <el-table-column :width=100 prop="monitorNo" label="监控号"></el-table-column>
        <el-table-column prop="programName" label="程序名"></el-table-column>
        <el-table-column :width=200 prop="workpiece" label="工件名称"></el-table-column>
        <el-table-column prop="material" label="材质"></el-table-column>
        <el-table-column prop="route" label="工序"></el-table-column>
        <el-table-column :width=130 prop="toolName" label="刀具名称"></el-table-column>
        <el-table-column prop="toolDiameter" label="刀具直径"></el-table-column>
        <el-table-column prop="toolNo" label="刀号"></el-table-column>
        <el-table-column prop="type" label="类型"></el-table-column>
        <el-table-column prop="margin" label="余量"></el-table-column>
        <el-table-column :width=100 prop="toolValidLength" label="刀具有效刃长"></el-table-column>
        <el-table-column prop="brand" label="品牌"></el-table-column>
        <el-table-column :width=120 prop="maxDepth" label="最大深度（Z）"></el-table-column>
        <el-table-column prop="workTime" label="加工时间"></el-table-column>
        <el-table-column prop="cutDepth" label="切深"></el-table-column>
        <el-table-column prop="feed" label="进给"></el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
<!--        <el-table-column prop="machineNo" :width=120 fixed="left"  label="机台号"></el-table-column>-->

        <el-table-column prop="machineNo" :width=120 fixed="left"  label="机台号">
          <template v-slot="scope">
            <el-select-v2
                size="small"
                v-if="scope.row.isSelected"
                v-model="machineName"
                filterable
                placeholder="请选择"
                :options="machineOptions"
                @change="cellEventForMachineNo(scope.row)"/>
<!--            <span v-else>{{ machineName }}</span>-->
                <span v-else>{{scope.row.machineNo}}</span>
          </template>
        </el-table-column>
        <el-table-column :width=120 fixed="left" prop="handleCode" label="刀柄编码">
          <template v-slot="scope">
            <el-select-v2
                size="small"
                v-if="scope.row.isSelected"
                v-model="scope.row.matInfo.handleCode"
                placeholder="请选择"
                filterable
                :options="toolOptions"
                @blur="cellEvent(scope.row)"
                @change="getMatInfo(scope.row)"
            />
<!--            @change="checkDiameter(scope.row, scope.row.toolDiameter)"-->
            <span v-else :style="getFontColor(scope.row.isCheck)">{{ scope.row.matInfo?.handleCode }}</span>
          </template>
        </el-table-column>
        <el-table-column :width=120 fixed="left" prop="toolCode" label="刀具编码">
          <template v-slot="scope">
            <span :style="getFontColor(scope.row.isCheck)">{{ scope.row.matInfo?.toolCode }}</span>
          </template>
        </el-table-column>
        <el-table-column :width=130 fixed="left" prop="matCode" label="物料号">
          <template v-slot="scope">
            <span :style="getFontColor(scope.row.isCheck)">{{ scope.row.matInfo?.matCode }}</span>
          </template>
        </el-table-column>
        <el-table-column :width=230 fixed="left" prop="matName" label="物料名称">
          <template v-slot="scope">
            <span :style="getFontColor(scope.row.isCheck)">{{ scope.row.matInfo?.matName }}</span>
          </template>
        </el-table-column>
        <el-table-column :width=100 fixed="left" prop="lifeSalvage" label="标准寿命">
          <template v-slot="scope">
            <span :style="getFontColor(scope.row.isCheck)">{{ scope.row.matInfo?.lifeSalvage }}</span>
          </template>
        </el-table-column>
        <el-table-column :width=100 fixed="left" prop="actualLife" label="实际寿命">
          <template v-slot="scope">
            <span :style="getFontColor(scope.row.isCheck)">{{ scope.row.matInfo?.actualLife }}</span>
          </template>
        </el-table-column>
        <el-table-column :width=100 fixed="left" prop="leftLife" label="剩余寿命">
          <template v-slot="scope">
            <span :style="getFontColor(scope.row.isCheck)">{{ scope.row.matInfo?.leftLife }}</span>
          </template>
        </el-table-column>
        <el-table-column :width=200 prop="createDateTime" label="创建时间"></el-table-column>
      </el-table>
      <el-dialog v-model="addMachineDialog" title="添加机台程序单" :close-on-click-modal="false" :show-close="false" width="30%">
        <el-select v-model="dialogMachineName" filterable placeholder="程序单机台号配置">
          <el-option
              v-for="item in machineNameList"
              :key="item.fequipName"
              :label="item.fequipName"
              :value="item.fequipName"
          >
          </el-option>
        </el-select>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button @click="resetSelection">取消</el-button>
            <el-button type="primary" @click="submitForm" :loading="addMachineLoading">提交</el-button>
          </slot>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  uploadExcel,
  getByMonitorNo,
  updateToolInfo,
  getMachineList,
  getMatInfoList, getByMonitorNoAndMachineNo,
} from "@/api/lens/iot/mold";
import {tableFilter} from "@/utils/baseUtils";

export default {
  name: "toolLife",
  created() {
    this.getAllMachine()
    this.getAllMatInfo()
  },
  data() {
    return {
      rules: {
        diameter: {}
      },
      addMachineDialog: false,
      addMachineLoading: false,
      dialogMachineName: "",
      selectLoading: false,
      machineName: "",
      machineNameList: [],
      matInfoList: [],
      matInfo: {},
      monitorNo: "",
      moldToolLifeSheet: [],
      toolLifeLoading: false,
      saveBtnLoading: false,
      programSheetMachineList: [],
      programSheetMachineNo: "",
      allMachineNumber: ""
    }
  },
  computed: {
    moldToolLifeSheetByMachine(){
      // if(this.programSheetMachineList.length && this.programSheetMachineList.length > 1){
      //   return this.moldToolLifeSheet.filter(item => item.machineNo === this.programSheetMachineNo)
      // }else{
      //   return this.moldToolLifeSheet
      // }
      return this.moldToolLifeSheet
    },
    tableMaxHeight() {
      return window.innerHeight - 370 + 'px';
    },
    toolOptions() {
      const list = [];
      for (let i = 0; i < this.matInfoList.length; i++) {
        list.push({label: this.matInfoList[i].handleCode, value: this.matInfoList[i].handleCode})
      }
      return list
    },
    machineOptions(){
      const list = [];
      for (let i = 0; i < this.machineNameList.length; i++) {
        list.push({label: this.machineNameList[i].fequipName, value: this.machineNameList[i].fequipName})
      }
      return list
    }
  },
  methods: {
    getMatInfo(row){
      const result = this.matInfoList.find(function (item) {
        return item.handleCode === row.matInfo.handleCode;
      }, this.matInfoList);
      row.matInfo = result
    },
    resetSelection() {
      this.dialogMachineName = ''
      this.addMachineDialog = false
    },
    submitForm() {
      if (this.dialogMachineName !== '' && this.dialogMachineName !== this.machineName) {
        if(this.moldToolLifeSheetByMachine.length === 0){
          this.$message.warning('当前列表为空，请先查询对应的程序单！')
          return
        }
        this.$confirm('确认提交吗？', '提示', {}).then(() => {
          this.addMachineLoading = true
          updateToolInfo({toolInfos: this.moldToolLifeSheetByMachine, machineNo: this.dialogMachineName}).then((response) => {
            const responseData = response.data
            if (responseData.code === '000000') {
              this.$message.success('添加成功！')
            } else {
              this.$message.error('添加失败！' + responseData.msg)
            }
            this.addMachineLoading = false
          }).catch((err) => {
            this.$message.error(err.message)
            this.addMachineLoading = false
          })
        })
      } else {
        this.$message({message: '操作失败,机台号与之前相等，且不为空', type: 'error'})
      }
    },
    cellEvent(row) {
      row.isSelected = !row.isSelected;
    },
    cellEventForMachineNo(row) {
      // var data = this.moldToolLifeSheetByMachine;
      // for(var i=0; i < data.length; i++) {
      //   if(row.id == data[i].id) {
      //     data[i].machineNo = this.machineName
      //   }
      // }
      for(let i=0; i < this.moldToolLifeSheetByMachine.length; i++) {
        if(row.id === this.moldToolLifeSheetByMachine[i].id) {
          this.moldToolLifeSheetByMachine[i].machineNo = this.machineName
        }
      }
      row.isSelected = !row.isSelected;
    },
    cellClick(row, column) {
      switch (column.label) {
        case '刀柄编码':
          this.cellEvent(row)
          break
        case '机台号':
          this.cellEvent(row)
          break
        default:
          return
      }
    },
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel'
      if (!isExcel) {
        this.$message.error('只能上传xlsx, xls格式的文件！')
        return false
      }
    },
    addMachineProgramSheet() {
      this.addMachineDialog = true
    },
    saveEditInfo() {
      const tempMoldList = Object.assign([], this.moldToolLifeSheetByMachine)
      if (tempMoldList.length === 0) {
        this.$message.warning('当前列表为空，请先查询对应的程序单！')
        return;
      }
      for (let i = 0; i < tempMoldList.length; i++) {
        console.log(tempMoldList[i])
        if (tempMoldList[i].isCheck != null && !tempMoldList[i].isCheck) {
          this.$message.error('保存失败，存在直径不匹配的刀柄（表格已标红）')
          return;
        }
        if (tempMoldList[i].machineNo === "" && tempMoldList[i].matInfo.toolCode !== null && tempMoldList[i].matInfo.toolCode !== "") {
          this.$message.error('保存失败，刀具编码有值时机台号不能为空')
          return;
        }
        //无需批量赋值机台号
        // if (this.machineName.length > 0) {
        //   tempMoldList[i].machineNo = this.machineName
        // }
      }
      this.saveBtnLoading = true
      updateToolInfo({toolInfos: tempMoldList, machineNo: null}).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.programSheetMachineList = tableFilter(this.moldToolLifeSheet).machineNo
          this.programSheetMachineNo = this.machineName
          this.$message.success('保存成功！')
        } else {
          this.$message.error('保存失败！' + responseData.msg)
        }
        this.saveBtnLoading = false
      }).catch((err) => {
        this.$message.error(err.message)
        this.saveBtnLoading = false
      })
    },
    uploadExcel(params) {
      uploadExcel(params).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          const monitorNo = responseData.data;
          this.getTableInfoByMonitorNo(monitorNo)
          this.$message.success('上传成功！')
        } else {
          this.$message.error('上传失败！' + responseData.msg)
        }
      }).catch((err) => {
        this.$message.error(err)
      })
    },
    onProgramMachineChange(){
      this.machineName = this.programSheetMachineNo
    },
    getTableInfoByMonitorNo(monitorNo) {
      this.toolLifeLoading = true
      if (monitorNo == null) {
        monitorNo = this.monitorNo;
      }
      getByMonitorNo(monitorNo).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.moldToolLifeSheet = responseData.data;
        } else {
          this.$message.error('获取失败！' + responseData.msg)
        }
        if(this.moldToolLifeSheet.length == 0) {
          this.$message.warning('查询不到该监控号数据！')
        }
        this.toolLifeLoading = false
      }).catch((err) => {
        this.$message.error(err)
        this.toolLifeLoading = false
      })
    },
    getByMonitorNo(monitorNo) {
      this.toolLifeLoading = true
      if (monitorNo == null) {
        monitorNo = this.monitorNo;
      }
      if(this.programSheetMachineList.length == 0) {
        this.$message.error('请先查询机台号')
        return
      }
      var machineNo = this.programSheetMachineNo
      getByMonitorNoAndMachineNo(monitorNo, machineNo).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.moldToolLifeSheet = responseData.data;
        } else {
          this.$message.error('获取失败！' + responseData.msg)
        }
        if(this.moldToolLifeSheet.length > 0) {
          // this.programSheetMachineList = tableFilter(this.moldToolLifeSheet).machineNo
          // console.log(this.programSheetMachineList)
          // if (this.programSheetMachineNo === '') {
          //   this.machineName = this.programSheetMachineList.length > 0 ? this.programSheetMachineList[0].value : ""
          //   this.programSheetMachineNo = this.machineName
          // } else {
          //   this.machineName = this.programSheetMachineNo
          // }
        }else{
          this.$message.warning('查询不到该监控号数据！')
        }
        this.toolLifeLoading = false
      }).catch((err) => {
        this.$message.error(err)
        this.toolLifeLoading = false
      })
    },
    getMachineNoByMonitorNo(monitorNo) {
      this.toolLifeLoading = true
      if (monitorNo == null) {
        monitorNo = this.monitorNo;
      }
      getByMonitorNo(monitorNo).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.moldToolLifeSheet = responseData.data; //查询到的所有数据
        } else {
          this.$message.error('获取失败！' + responseData.msg)
        }
        if(this.moldToolLifeSheet.length > 0) {
          this.programSheetMachineList = tableFilter(this.moldToolLifeSheet).machineNo //筛选出机台号
          for(var i=0; i < this.programSheetMachineList.length; i++) {
            if(this.programSheetMachineList[i].value === '') {
              this.programSheetMachineList[i].text = '未绑定机台程序'
            }
          }
          if (this.programSheetMachineNo === '') {
            this.machineName = this.programSheetMachineList.length > 0 ? this.programSheetMachineList[0].value : ""
            this.programSheetMachineNo = this.machineName
          } else {
            this.machineName = this.programSheetMachineNo
          }
        }else{
          this.$message.warning('查询不到该监控号数据！')
        }
        this.toolLifeLoading = false
      }).catch((err) => {
        this.$message.error(err)
        this.toolLifeLoading = false
      })
    },
    getAllMachine() {
      getMachineList().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameList = responseData.data;
        } else {
          this.$message.error('获取失败！' + responseData.msg)
        }
      }).catch((err) => {
        this.$message.error(err)
      })
    },
    getAllMatInfo() {
      getMatInfoList().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.matInfoList = responseData.data;
        } else {
          this.$message.error('获取失败！' + responseData.msg)
        }
      }).catch((err) => {
        this.$message.error(err)
      })
    },
    checkDiameter(row, diameter) {
      const reg = new RegExp('(D|E)(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))');
      const value = row.matInfo.matName.match(reg)[0]
      const matDiameter = value.substring(1, value.length)
      if (Math.abs(matDiameter - diameter) > 0.05) {
        row.isCheck = false
        this.$message.error('直径不匹配，请重新选择！')
      } else {
        row.isCheck = true
      }
      this.cellEvent(row)
    },
    getFontColor(isCheck) {
      return isCheck == null || isCheck ? 'color:black' : 'color:red'
    },
    changeAllMachineNumber() {
      var data = this.moldToolLifeSheetByMachine;
      for(var i=0; i < data.length; i++) {
        data[i].machineNo = this.allMachineNumber
      }
    }
  },
};
</script>
