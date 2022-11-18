<template>
  <div class="aac-container">
    <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size">
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
              end-placeholder="结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              range-separator="至"
              start-placeholder="开始时间"
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
          <el-button type="success" @click="handleDownload">导出
            <template #icon>
              <font-awesome-icon :icon="['fas', 'cloud-arrow-down']"/>
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
               width="60%">
      <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
        <el-form-item v-if="false" label="Id" prop="id">
          <el-input v-model="dataForm.id" auto-complete="off"></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="6">
            <el-form-item label="模次号" prop="cycleNo">
              <el-input v-model="dataForm.cycleNo" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="送测记录" prop="measRecord">
              <el-input v-model="dataForm.measRecord" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="调整动作" prop="operateRecord">
          <el-input v-model="dataForm.operateRecord" auto-complete="off" clearable></el-input>
        </el-form-item>

        <el-row>
          <el-col :span="3">
            <el-form-item label="是否粘下模" prop="stickingLower">
              <el-switch
                  v-model="dataForm.stickingLower"
                  class="ml-2"
                  :active-value="1"
                  :inactive-value="0"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开模碎裂" prop="dieBreaking">
              <el-select v-model="dataForm.dieBreaking" clearable placeholder="开模碎裂" style="width:100%">
                <el-option
                    v-for="item in dieBreakingOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="抓取碎裂" prop="grabBreaking">
              <el-select v-model="dataForm.grabBreaking" clearable placeholder="抓取碎裂" style="width:100%">
                <el-option
                    v-for="item in grabBreakingOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="冷却过程碎裂" prop="coolingBreaking">
              <el-select v-model="dataForm.coolingBreaking" clearable placeholder="冷却过程碎裂" style="width:100%">
                <el-option
                    v-for="item in coolingBreakingOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dataForm.remark" auto-complete="off" clearable></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-form-item label="性能确认-偏心" prop="decenterResult">
              <el-input v-model="dataForm.decenterResult" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性能确认-芯厚" prop="thicknessResult">
              <el-input v-model="dataForm.thicknessResult" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性能确认-面型" prop="mxpvResult">
              <el-input v-model="dataForm.mxpvResult" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="性能确认结果" prop="performanceResult">
              <el-input v-model="dataForm.performanceResult" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="综合结果" prop="aggregateResult">
              <el-input v-model="dataForm.aggregateResult" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="批次号" prop="batchNo">
              <el-input v-model="dataForm.batchNo" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="熔炼批次" prop="smeltingBatch">
              <el-input v-model="dataForm.smeltingBatch" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="U编号" prop="ucode">
              <el-input v-model="dataForm.ucode" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="U数值" prop="uvalue">
              <el-input v-model="dataForm.uvalue" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="U角度" prop="uangle">
              <el-input v-model="dataForm.uangle" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="V编号" prop="vcode">
              <el-input v-model="dataForm.vcode" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="V数值" prop="vvalue">
              <el-input v-model="dataForm.vvalue" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="V角度" prop="vangle">
              <el-input v-model="dataForm.vangle" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="W编号" prop="wcode">
              <el-input v-model="dataForm.wcode" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="W数值" prop="wvalue">
              <el-input v-model="dataForm.wvalue" auto-complete="off" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="W角度" prop="wangle">
              <el-input v-model="dataForm.wangle" auto-complete="off" clearable></el-input>
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
import {
  downloadExcel,
  getCycleDetail,
  getMachineName,
  handleCycleDetailUpdate
} from "@/api/wlg/iot/moldingMachineParamData";
import {getUserDetail, getUsername} from "@/utils/auth";

export default {
  name: "notificationRobot",
  components: {SysTable},
  data() {
    const checkNumber = (rule, value, callback) => {
      if (value) {
        const reg = /^[+-]?(0|([1-9]\d*))(\.\d+)?$/;
        if (reg.test(value) === false) {
          callback(new Error('只可输入数字'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    const checkCycleNo = (rule, value, callback) => {
      if (value) {
        if(value === "跳模")
          callback()
        const reg = /W[1-9]\d*/;
        if (reg.test(value) === false) {
          callback(new Error('模次号格式不正确，请检查！'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
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
        {sortable: false, prop: "cycleDate", label: "日期", minWidth: 80, fixed: "left"},
        {sortable: false, prop: "shiftClass", label: "班别", minWidth: 40, fixed: "left"},
        {sortable: false, prop: "machineName", label: "机台号", minWidth: 70, fixed: "left"},
        {sortable: false, prop: "cycleTime", label: "开始时间", minWidth: 80, fixed: "left"},
        {sortable: false, prop: "cycleNo", label: "模次号", minWidth: 60, fixed: "left"},
        {sortable: false, prop: "waferId", label: "log file编号", minWidth: 60, fixed: "left"},
        {sortable: false, prop: "recipeName", label: "Recipe name", minWidth: 160, fixed: "left"},
        {sortable: false, prop: "measRecord", label: "送测记录", minWidth: 160},
        {sortable: false, prop: "operateRecord", label: "调整动作", minWidth: 160},
        {sortable: false, prop: "stickingLower", label: "是否粘下模", minWidth: 40},
        {sortable: false, prop: "dieBreaking", label: "开模碎裂", minWidth: 40},
        {sortable: false, prop: "grabBreaking", label: "抓取碎裂", minWidth: 40},
        {sortable: false, prop: "coolingBreaking", label: "冷却过程碎裂", minWidth: 40},
        {sortable: false, prop: "remark", label: "备注", minWidth: 160},
        {sortable: false, prop: "decenterResult", label: "性能确认-偏心", minWidth: 120},
        {sortable: false, prop: "thicknessResult", label: "性能确认-芯厚", minWidth: 120},
        {sortable: false, prop: "mxpvResult", label: "性能确认-面型", minWidth: 120},
        {sortable: false, prop: "performanceResult", label: "性能确认结果", minWidth: 120},
        {sortable: false, prop: "aggregateResult", label: "综合结果", minWidth: 120},
        {sortable: false, prop: "batchNo", label: "批次号", minWidth: 160},
        {sortable: false, prop: "smeltingBatch", label: "熔炼批次", minWidth: 160},
        {sortable: false, prop: "ucode", label: "U编号", minWidth: 80},
        {sortable: false, prop: "uvalue", label: "U数值", minWidth: 80},
        {sortable: false, prop: "uangle", label: "U角度", minWidth: 80},
        {sortable: false, prop: "vcode", label: "U编号", minWidth: 80},
        {sortable: false, prop: "vvalue", label: "U数值", minWidth: 80},
        {sortable: false, prop: "vangle", label: "U角度", minWidth: 80},
        {sortable: false, prop: "wcode", label: "U编号", minWidth: 80},
        {sortable: false, prop: "wvalue", label: "U数值", minWidth: 80},
        {sortable: false, prop: "wangle", label: "U角度", minWidth: 80},
        {sortable: false, prop: "updateUser", label: "更新人", minWidth: 100},
        {sortable: false, prop: "updateTime", label: "更新时间", minWidth: 130},
        {sortable: false, prop: "createTime", label: "创建时间", minWidth: 130},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        // robotName: [{required: true, message: "请输入群名称", trigger: "blur"}],
        // robotType: [{required: true, message: "请选择机器人类型", trigger: "change"},],
        cycleNo: {required: true, trigger: 'blur', validator: checkCycleNo},
        uvalue: {required: false, trigger: 'blur', validator: checkNumber},
        uangle: {required: false, trigger: 'blur', validator: checkNumber},
        vvalue: {required: false, trigger: 'blur', validator: checkNumber},
        vangle: {required: false, trigger: 'blur', validator: checkNumber},
        wvalue: {required: false, trigger: 'blur', validator: checkNumber},
        wangle: {required: false, trigger: 'blur', validator: checkNumber},
      },
      dieBreakingOptions: [],
      grabBreakingOptions: [],
      coolingBreakingOptions: [],
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
        measRecord: "",
        operateRecord: "",
        stickingLower: "",
        dieBreaking: "",
        grabBreaking: "",
        coolingBreaking: "",
        remark: "",
        decenterResult: "",
        thicknessResult: "",
        mxpvResult: "",
        performanceResult: "",
        aggregateResult: "",
        batchNo: "",
        smeltingBatch: "",
        ucode: "",
        uvalue: "",
        uangle: "",
        vcode: "",
        vvalue: "",
        vangle: "",
        wcode: "",
        wvalue: "",
        wangle: "",
        updateTime: "",
        updateUser: "",
        createTime: "",
      },
    };
  },
  mounted() {
    this.getMachineName();
    getDict("wlg_die_breaking_type").then(response => {
      this.dieBreakingOptions = response.data.data
    })
    getDict("wlg_grab_breaking_type").then(response => {
      this.grabBreakingOptions = response.data.data
    })
    getDict("wlg_cooling_breaking_type").then(response => {
      this.coolingBreakingOptions = response.data.data
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
    handleDownload: function () {
      console.log(this.dateTimePickerValue)
      if (this.dateTimePickerValue == null || this.dateTimePickerValue.length !== 2) {
        this.$message({message: "请先选择时间段！", type: "error"});
        return
      }
      const startTime = this.$moment(this.dateTimePickerValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = this.$moment(this.dateTimePickerValue[1]).format('YYYY-MM-DD HH:mm:ss');
      this.pageRequest.startTime = startTime;
      this.pageRequest.endTime = endTime;
      this.pageRequest.machineNames = this.filters.machineNames;
      downloadExcel(this.pageRequest)
          .then((response) => {
            if (response.headers['content-type'] === 'APPLICATION/OCTET-STREAM') {
              let filename = '模造机模次明细.xlsx'
              let url = window.URL.createObjectURL(new Blob([response.data]))
              let link = document.createElement('a')
              link.style.display = 'none'
              link.href = url
              link.setAttribute('download', filename)
              document.body.appendChild(link)
              link.click()
            }
          })
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      if (this.dateTimePickerValue.length > 0) {
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
              params.updateUser = this.userRealName;
              if (!this.checkCycleNo(params.cycleNo)) {
                this.$message({message: "模次号格式不正确，请检查！", type: "error"});
                this.editLoading = false;
                return
              }
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
    checkCycleNo(str) {
      const reg = /W[1-9]\d*/;  /*定义验证表达式*/
      return reg.test(str);   /*进行验证*/
    },
    checkNumber(str) {
      const reg = /^[+-]?(0|([1-9]\d*))(\.\d+)?$/;  /*定义验证表达式*/
      return reg.test(str);   /*进行验证*/
    }
  },
  computed: {
    userRealName() {
      let userName = getUsername()
      let userDetail = getUserDetail()
      let realName = userDetail && userDetail.name ? userDetail.name : userName;
      return realName ? realName : this.name;
    },
  }
};
</script>
