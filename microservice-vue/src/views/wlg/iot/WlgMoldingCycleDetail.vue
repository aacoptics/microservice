<template>
  <div class="aac-container">
    <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size" label-width="100px">
        <el-form-item label="机台号" prop="machineName">
          <el-select v-model="filters.machineNames"
                     :size="size"
                     collapse-tags
                     multiple
                     placeholder="请选择机台号">
            <el-checkbox v-model="okAllChecked" @change='okSelectAll'>全选</el-checkbox>
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
              format="YYYY-MM-DD HH:00"
              range-separator="至"
              start-placeholder="开始日期"
              type="datetimerange">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <el-form :inline="true" :size="size">
        <el-form-item>
          <el-button type="primary" @click="findPage(null)">查询
            <template #icon>
              <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
            </template>
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="handleAdd">新增
            <template #icon>
              <font-awesome-icon :icon="['fas', 'plus']"/>
            </template>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
              :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
              :stripe="false" :show-operation-del="false"
              @findPage="findPage" @handleEdit="handleEdit">
    </SysTable>

    <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
               width="40%">
      <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
        <el-form-item v-if="false" label="Id" prop="id">
          <el-input v-model="dataForm.id" auto-complete="off"></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="群名称" prop="robotName">
              <el-input v-model="dataForm.robotName" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机器人类型" prop="robotType">
              <el-select v-model="dataForm.robotType" clearable placeholder="机器人类型" style="width:100%">
                <el-option
                    v-for="item in robotOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="群类型" prop="chatType">
              <el-select v-model="dataForm.chatType" clearable placeholder="群类型" style="width:100%">
                <el-option
                    v-for="item in chatTypeOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item v-if="dataForm.robotType === 'group_robot'" label="机器人链接" prop="robotUrl">
              <el-input v-model="dataForm.robotUrl" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="dialog-footer" style="padding-top: 20px;text-align: end">
        <slot name="footer">
          <el-button :size="size" @click="cancel">取消</el-button>
          <el-button :loading="editLoading" :size="size" type="primary" @click="submitForm">提交</el-button>
        </slot>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {getDict} from "@/api/system/dictData";
import {getResponseDataMessage} from "@/utils/commonUtils";
import {deleteRobot, findRobotInfoPage, handleAdd, handleUpdate} from "@/api/notification/robot";
import {getCycleDetail, getMachineName, handleCycleDetailUpdate} from "@/api/wlg/iot/moldingMachineParamData";

export default {
  name: "notificationRobot",
  components: {SysTable},
  data() {
    return {
      size: "small",
      filters: {
        machineNames: []
      },
      okAllChecked: false,
      machineNameArray: [],
      dateTimePickerValue: [],
      shortcuts: [{
        text: '最近一天',
        value: (() => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24);
          return [start, end]
        })()
      },
        {
          text: '最近一周',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            return [start, end]
          },
        },
        {
          text: '最近一个月',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            return [start, end]
          },
        }],
      columns: [
        {prop: "cycleDate", label: "日期", minWidth: 110},
        {prop: "shiftClass", label: "班别", minWidth: 110},
        {prop: "machineName", label: "机台号", minWidth: 110},
        {prop: "cycleTime", label: "开始时间", minWidth: 110},
        {prop: "cycleNo", label: "模次号", minWidth: 80},
        {prop: "waferId", label: "log file编号", minWidth: 80},
        {prop: "recipeName", label: "Recipe name", minWidth: 350},
        {prop: "measRecord", label: "送测记录", minWidth: 350},
        {prop: "operateRecord", label: "调整动作", minWidth: 350},
        {prop: "stickingLower", label: "是否粘下模", minWidth: 350},
        {prop: "dieBreaking", label: "开模碎裂", minWidth: 350},
        {prop: "grabBreaking", label: "抓取碎裂", minWidth: 350},
        {prop: "coolingBreaking", label: "冷却过程碎裂", minWidth: 350},
        {prop: "remark", label: "备注", minWidth: 350},
        {prop: "decenterResult", label: "性能确认-偏心", minWidth: 350},
        {prop: "thicknessResult", label: "性能确认-芯厚", minWidth: 350},
        {prop: "mxpvResult", label: "性能确认-面型", minWidth: 350},
        {prop: "performanceResult", label: "性能确认结果", minWidth: 350},
        {prop: "aggregateResult", label: "综合结果", minWidth: 350},
        {prop: "batchNo", label: "批次号", minWidth: 350},
        {prop: "smeltingBatch", label: "熔炼批次", minWidth: 350},
        {prop: "uCode", label: "U编号", minWidth: 350},
        {prop: "uValue", label: "U数值", minWidth: 350},
        {prop: "uAngle", label: "U角度", minWidth: 350},
        {prop: "vCode", label: "U编号", minWidth: 350},
        {prop: "vValue", label: "U数值", minWidth: 350},
        {prop: "vAngle", label: "U角度", minWidth: 350},
        {prop: "wCode", label: "U编号", minWidth: 350},
        {prop: "wValue", label: "U数值", minWidth: 350},
        {prop: "wAngle", label: "U角度", minWidth: 350},
        {prop: "updateUser", label: "更新人", minWidth: 100},
        {prop: "updateTime", label: "更新时间", minWidth: 120},
        {prop: "createTime", label: "创建时间", minWidth: 120},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        robotName: [{required: true, message: "请输入群名称", trigger: "blur"}],
        robotType: [{required: true, message: "请选择机器人类型", trigger: "change"},],
        chatType: [{required: true, message: "请选择群类型", trigger: "change"},],
        robotUrl: [{required: true, message: "请输入机器人URL", trigger: "blur"},]
      },
      robotOptions: [],
      chatTypeOptions: [],
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        machineName: "",
        cycleDate: "",
        cycleTime: "",
        startTime: "",
        shiftClass: "",
        cycleNo: "",
        waferId: "",
        recipeName: "",
        measRecord: null,
        operateRecord: null,
        stickingLower: null,
        dieBreaking: null,
        grabBreaking: null,
        coolingBreaking: null,
        remark: null,
        decenterResult: null,
        thicknessResult: null,
        mxpvResult: null,
        performanceResult: null,
        aggregateResult: null,
        batchNo: null,
        smeltingBatch: null,
        uCode: null,
        uValue: null,
        uAngle: null,
        vCode: null,
        vValue: null,
        vAngle: null,
        wCode: null,
        wValue: null,
        wAngle: null,
        updateTime: "",
        updateUser: "",
        createTime: "",
      },
    };
  },
  mounted() {
    this.getMachineName();
    getDict("notification_robot_type").then(response => {
      this.robotOptions = response.data.data
    })
    getDict("notification_chat_type").then(response => {
      this.chatTypeOptions = response.data.data
    })

  },
  methods: {
    getMachineName() {
      getMachineName().then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          this.machineNameArray = responseData.data
        }
      })
    },
    okSelectAll() {
      this.filters.machineNames = []
      if (this.okAllChecked) {
        this.machineNameArray.map(item => {
          this.filters.machineNames.push(item.machineName)
        })
      } else {
        this.filters.machineNames = []
      }
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      if(this.dateTimePickerValue.length > 0){
        const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
        const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
        this.pageRequest.startTime = startTime;
        this.pageRequest.endTime = endTime;
      }
      this.pageRequest.machineNames = this.filters.machineNames;

      getCycleDetail(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },

    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.operation = false;
      this.dataForm = Object.assign({}, params.row);
    },
    // 编辑
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.operation) {
              // handleAdd(params).then((res) => {
              //   const responseData = res.data;
              //   this.editLoading = false;
              //   if (responseData.code === "000000") {
              //     this.$message({message: "操作成功", type: "success"});
              //     this.dialogVisible = false;
              //     this.$refs["dataForm"].resetFields();
              //   } else {
              //     this.$message({
              //       message:
              //           "操作失败 " + getResponseDataMessage(responseData),
              //       type: "error",
              //     });
              //   }
              //   this.findPage(null);
              // });
            } else {
              handleCycleDetailUpdate(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.dialogVisible = false;
                  this.$refs["dataForm"].resetFields();
                } else {
                  this.$message({
                    message:
                        "操作失败, " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
                this.findPage(null);
              });
            }
          });
        }
      });
    },

    // 取消
    cancel() {
      this.dialogVisible = false;
    },

    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    }
  },
};
</script>
