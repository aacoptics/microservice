<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-input v-model="filters.username" placeholder="用户名"></el-input>
          </el-form-item>
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
      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false" :showOperation="false"
                :stripe="false"
                @findPage="findPage">
      </SysTable>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {getLastWeekAccessLog} from "@/api/system/menu";

export default {
  name: "menuAccessLog",
  components: {SysTable},
  data() {
    return {
      size: 'default',
      filters: {
        username: ''
      },
      columns: [
        {prop: "title", label: "菜单名称", minWidth: 110},
        {prop: "username", label: "访问人工号", minWidth: 100},
        {prop: "realName", label: "访问人姓名", minWidth: 120},
        {prop: "os", label: "操作系统", minWidth: 120},
        {prop: "platform", label: "平台", minWidth: 100},
        {prop: "browser", label: "浏览器", minWidth: 120},
        {prop: "version", label: "版本", minWidth: 120},
        {prop: "accessTime", label: "访问时间", minWidth: 120, formatter: this.dateFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      getLastWeekAccessLog(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    }
  }
}
</script>
