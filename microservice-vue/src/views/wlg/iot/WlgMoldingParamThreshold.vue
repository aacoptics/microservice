<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
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
      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage"
                @handleDelete="handleDelete"
                @handleEdit="handleEdit">
      </SysTable>
      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="40%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="80px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="域账号" prop="username">
            <el-input v-model="dataForm.username" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="dataForm.name" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input v-model="dataForm.description" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="mobile">
            <el-input v-model="dataForm.mobile" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="角色" prop="roleIds">
            <el-select v-model="dataForm.roleIds" multiple placeholder="请选择">
              <el-option
                  v-for="item in roleData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" @click="resetSelection">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitForm">提交</el-button>
          </slot>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {handleRoleData} from "@/api/system/role";
import {deleteUser, findUserInfoPage, findUserRolesById, handleAdd, handleUpdate} from "@/api/system/user";
import {getParamThreshold} from "@/api/wlg/iot/moldingMachineParamData";
import {findDictTypeById} from "@/api/system/dictType";

export default {
  name: "paramThreshold",
  components: {SysTable},
  props: {
    machineId: Number, // 表格分页数据},
    machineName: String
  },
  data() {
    return {
      size: 'default',
      filters: {
      },
      columns: [
        {prop: "paramId", label: "参数名", minWidth: 100},
        {prop: "arrayId", label: "电话", minWidth: 120},
        {prop: "threshold", label: "阈值", minWidth: 120},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
        mobile: [{
          required: true, trigger: 'blur', validator: (r, v, b) => {
            (!v ? b('请输入手机号') : (!(/^(?:(?:\+|00)86)?1\d{10}$/.test(v))) ? b('手机号格式不正确') : b())
          }
        }]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        username: '',
        name: '',
        description: '',
        mobile: '',
        roleIds: []
      },
      selectUser: {},
      roleData: [],
      currentUserRoles: [],
      paramInfo: [
        {
          "param_info":{
            "param_id":27,
            "array_id":1
          },
          "param_name":"lower_moldcore_section_temp_actual_1"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":2
          },
          "param_name":"lower_moldcore_section_temp_actual_2"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":3
          },
          "param_name":"lower_moldcore_section_temp_actual_3"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":4
          },
          "param_name":"lower_moldcore_section_temp_actual_4"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":5
          },
          "param_name":"lower_moldcore_section_temp_actual_5"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":6
          },
          "param_name":"lower_moldcore_section_temp_actual_6"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":7
          },
          "param_name":"lower_moldcore_section_temp_actual_7"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":8
          },
          "param_name":"lower_moldcore_section_temp_actual_8"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":9
          },
          "param_name":"lower_moldcore_section_temp_actual_9"
        },
        {
          "param_info":{
            "param_id":27,
            "array_id":10
          },
          "param_name":"lower_moldcore_section_temp_actual_10"
        },
        {
          "param_info":{
            "param_id":25,
            "array_id":0
          },
          "param_name":"lower_mold_temp_actual_0"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":1
          },
          "param_name":"upper_moldcore_section_temp_actual_1"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":2
          },
          "param_name":"upper_moldcore_section_temp_actual_2"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":3
          },
          "param_name":"upper_moldcore_section_temp_actual_3"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":4
          },
          "param_name":"upper_moldcore_section_temp_actual_4"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":5
          },
          "param_name":"upper_moldcore_section_temp_actual_5"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":6
          },
          "param_name":"upper_moldcore_section_temp_actual_6"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":7
          },
          "param_name":"upper_moldcore_section_temp_actual_7"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":8
          },
          "param_name":"upper_moldcore_section_temp_actual_8"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":9
          },
          "param_name":"upper_moldcore_section_temp_actual_9"
        },
        {
          "param_info":{
            "param_id":55,
            "array_id":10
          },
          "param_name":"upper_moldcore_section_temp_actual_10"
        },
        {
          "param_info":{
            "param_id":53,
            "array_id":0
          },
          "param_name":"upper_mold_temp_actual_0"
        },
        {
          "param_info":{
            "param_id":38,
            "array_id":0
          },
          "param_name":"press_force_actual_0"
        }
      ],
    }
  },
  methods: {
    refreshData(){
      this.machineNameStr = this.machineName
      this.findPage(null)
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      getParamThreshold(this.machineId, this.pageRequest.current, this.pageRequest.size).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },

    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteUser(data.params[0]).then(data.callback)
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true
      this.operation = true
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        username: '',
        name: '',
        description: '',
        mobile: '',
        roleIds: [],
        accountNonExpired: true,
        accountNonLocked: true,
        credentialsNonExpired: true,
        enabled: true
      }
    },
    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.getCurrentUserRoleIds()
    },
    // 编辑
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            if (this.operation) {
              params.password = this.uuid(20)
              handleAdd(params).then((res) => {
                const responseData = res.data
                this.editLoading = false
                if (responseData.code === '000000') {
                  this.$message({message: '操作成功', type: 'success'})
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + responseData.msg, type: 'error'})
                }
                this.findPage(null)
              })
            } else {
              handleUpdate(params).then((res) => {
                const responseData = res.data
                this.editLoading = false
                if (responseData.code === '000000') {
                  this.$message({message: '操作成功', type: 'success'})
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + responseData.msg, type: 'error'})
                }
                this.findPage(null)
              })
            }
          })
        }
      })
    },
    uuid(size) {
      const seed = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'Q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '2', '3', '4', '5', '6', '7', '8', '9'];//数组
      const seedLength = seed.length;//数组长度
      let createPassword = '';
      for (let i = 0; i < size; i++) {
        const j = Math.floor(Math.random() * seedLength);
        createPassword += seed[j];
      }
      return createPassword;
    },
    // 获取数据

    findRoleData: function () {
      handleRoleData().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.roleData = responseData.data
        }
      })
    },
    // 角色选择改变监听
    handleUserSelectChange(val) {
      if (val == null || val.val == null) {
        this.currentUserRoles = []
        this.dataForm.roleIds = []
        return
      }
      this.selectUser = val.val
      findUserRolesById(val.val.id).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.currentUserRoles = responseData.data
          this.getCurrentUserRoleIds()
        }
      })
    },
    getCurrentUserRoleIds() {
      let roles = []
      for (let i = 0, len = this.currentUserRoles.length; i < len; i++) {
        roles.push(this.currentUserRoles[i].id)
      }
      this.dataForm.roleIds = roles
    },
    // 重置选择
    resetSelection() {
      this.getCurrentUserRoleIds()
      this.dialogVisible = false
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    }
  }
}
</script>
