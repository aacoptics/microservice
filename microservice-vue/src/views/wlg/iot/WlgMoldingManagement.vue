<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-input v-model="filters.machineName" placeholder="机台号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :show-operation-del="false"
                :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage"
                @handleCurrentChange="handleSelectChange"
                @handleEdit="handleEdit">
        <template v-slot:custom-column>
          <el-table-column align="center" fixed="right" header-align="center" label="换料提醒"
                           width="80">
            <template v-slot="scope">
              <el-switch
                  v-model="scope.row.feedingAlarm"
                  :active-value="true"
                  :inactive-value="false"
                  active-text="是"
                  inactive-text="否"
                  inline-prompt
                  @change="handleStatusChange(scope)"
              />
            </template>
          </el-table-column>
          <el-table-column align="center" fixed="right" header-align="center" label="CT维护">
            <template v-slot="scope">
              <el-input-number v-model="scope.row.standardCt" :min="0" :precision="2" :step="0.1"
                               controls-position="right" size="small"/>
              <el-button circle type="success" @click="handleCtButtonClick(scope)">
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'check']"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
        </template>
      </SysTable>
      <el-dialog v-model="dialogVisible" :title="operation?'新增':'阈值维护'" destroy-on-close width="90%">
        <wlg-molding-param-threshold ref="paramThreshold" :machine-id="currentMachineInfo.id"
                                     :machine-name="currentMachineInfo.machineName"></wlg-molding-param-threshold>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {getMoldingInfo, handleUpdateFeedingAlarm} from "@/api/wlg/iot/moldingMachineParamData";
import WlgMoldingParamThreshold from "./WlgMoldingParamThreshold"

export default {
  name: "WlgMoldingManagement",
  components: {SysTable, WlgMoldingParamThreshold},
  data() {
    return {
      size: 'default',
      filters: {
        machineName: ''
      },
      columns: [
        {prop: "machineName", label: "机台号", minWidth: 80},
        {prop: "ipAddress", label: "ip地址", minWidth: 80},
        {prop: "port", label: "端口号", minWidth: 80},
        {prop: "namespaceIndex", label: "命名空间", minWidth: 40},
        {prop: "createdBy", label: "创建人", minWidth: 80},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      currentMachineInfo: {},
      dataForm: {
        id: 0,
        feedingAlarm: false,
        standardCt: 0
      },
    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      getMoldingInfo(this.filters.machineName, this.pageRequest.current, this.pageRequest.size).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },
    // 重置选择
    resetSelection() {
      this.dialogVisible = false
    },
    handleSelectChange(val) {
      if (val == null || val.val == null) {
        this.currentMachineInfo = {}
        return
      }
      this.currentMachineInfo = val.val
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
    },
    handleStatusChange(params) {
      let text = params.row.feedingAlarm === true ? "启用" : "停用";
      this.$confirm('确认要' + text + '""' + params.row.machineName + '"换料提醒吗?').then(() => {
        handleUpdateFeedingAlarm(params.row).then((res) => {
          const responseData = res.data
          if (responseData.code === '000000') {
            this.$message({message: text + '成功', type: 'success'})
          } else {
            this.$message({message: responseData.data.msg, type: 'error'})
            params.row.feedingAlarm = params.row.feedingAlarm === false;
          }
        })
      }).catch(function () {
        params.row.feedingAlarm = params.row.feedingAlarm === false;
      });
    },
    handleCtButtonClick(params) {
      this.$confirm('确认要保存标准CT吗?').then(() => {
        handleUpdateFeedingAlarm(params.row).then((res) => {
          const responseData = res.data
          if (responseData.code === '000000') {
            this.$message({message: '修改成功', type: 'success'})
          } else {
            this.$message({message: responseData.data.msg, type: 'error'})
          }
        })
      }).catch(function () {
        this.$message({message: '保存失败！', type: 'error'})
      });
    },
  },
  mounted() {
    this.findPage(null)
  }
}
</script>
