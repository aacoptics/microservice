<template>
  <div class="aac-container">
    <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size">
        <el-form-item label="消息批次号" prop="batchId">
          <el-input v-model="filters.batchId" clearable placeholder="消息批次号"></el-input>
        </el-form-item>
        <el-form-item label="消息类型名称" prop="conType">
          <el-input v-model="filters.conType" clearable placeholder="消息类型名称"></el-input>
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
      </el-form>
    </div>
    <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
              :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
              :stripe="false" :show-operation="false"
              @findPage="findPage">
      <template v-slot:custom-column>
        <el-table-column align="center" fixed="right" header-align="center" label="操作">
          <template v-slot="scope">
            <el-button v-if="scope.row.msgStatus" type="warning" @click="handleDelete(scope)">撤回消息
              <template #icon>
                <font-awesome-icon :icon="['fas', 'arrow-rotate-left']"/>
              </template>
            </el-button>
            <el-button v-else disabled type="warning" @click="handleDelete(scope)">撤回消息
              <template #icon>
                <font-awesome-icon :icon="['fas', 'arrow-rotate-left']"/>
              </template>
            </el-button>
          </template>
        </el-table-column>
      </template>
    </SysTable>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {getDict} from "@/api/system/dictData";
import {getResponseDataMessage} from "@/utils/commonUtils";
import {deleteRobot, findRobotInfoPage, handleAdd, handleUpdate} from "@/api/notification/robot";
import {deleteMessage, queryMessageHistory} from "@/api/notification/notificationTask";
import {handleUpdateFeedingAlarm} from "@/api/wlg/iot/moldingMachineParamData";

export default {
  name: "notificationRobot",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        batchId: "",
        conType: ""
      },
      columns: [
        {prop: "batchId", label: "消息批次号", minWidth: 110},
        {prop: "conType", label: "消息类型", minWidth: 120},
        {prop: "conTypeDesc", label: "消息类型描述", minWidth: 120},
        {prop: "messageId", label: "消息Id", minWidth: 80},
        {prop: "msgType", label: "消息类型", minWidth: 80},
        {prop: "msgStatus", label: "消息状态", minWidth: 60, formatter: this.statusFormat},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
    };
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.batchId = this.filters.batchId;
      this.pageRequest.conType = this.filters.conType;
      queryMessageHistory(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },
    handleDelete(params) {
      this.$confirm('确认要撤回该消息吗?').then(() => {
        deleteMessage(params.row).then((res) => {
          const responseData = res.data
          if (responseData.code === '000000') {
            this.$message({message: '撤回成功', type: 'success'})
          } else {
            this.$message({message: responseData.data.msg, type: 'error'})
          }
          this.findPage(null)
        })
      })
    },

    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },

    statusFormat: function (row, column) {
      if(row[column.property])
        return '已发送'
      else
        return '已撤回'
    },
  },
};
</script>
