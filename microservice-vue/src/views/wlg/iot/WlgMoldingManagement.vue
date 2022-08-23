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
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage"
                :show-operation-del="false"
                @handleEdit="handleEdit">
      </SysTable>
      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'阈值维护'"
                 width="40%">
      </el-dialog>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {getMoldingInfo} from "@/api/wlg/iot/moldingMachineParamData";

export default {
  name: "WlgMoldingManagement",
  components: {SysTable},
  data() {
    return {
      size: 'default',
      filters: {
        machineName: ''
      },
      columns: [
        {prop: "machineName", label: "机台号", minWidth: 110},
        {prop: "ipAddress", label: "ip地址", minWidth: 100},
        {prop: "port", label: "端口号", minWidth: 120},
        {prop: "namespaceIndex", label: "命名空间", minWidth: 120},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.username = this.filters.username
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
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
    }
  }
}
</script>
