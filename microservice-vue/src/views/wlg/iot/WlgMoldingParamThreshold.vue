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
          <el-form-item v-if="false" label="机台Id" prop="machineId">
            <el-input v-model="dataForm.machineId" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="参数" prop="paramName">
            <el-select v-model="paramDesc" value-key="param_name" placeholder="请选择">
              <el-option
                  v-for="item in paramInfo"
                  :key="item.param_name"
                  :label="item.param_name"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="switchValue">
            <el-switch
                v-model="switchValue"
                active-text="阈值"
                inactive-text="上下限"
                :active-value="1"
                :inactive-value="0"
                @change="onSwitchChange"
            />
          </el-form-item>
          <el-form-item label="阈值" prop="threshold" v-if="switchValue === 1">
            <el-input-number :precision="2" :step="0.1" v-model="dataForm.threshold" auto-complete="off"></el-input-number>
          </el-form-item>
          <el-form-item label="最大值" prop="maxValue" v-if="switchValue === 0">
            <el-input-number :precision="2" :step="0.1" v-model="dataForm.maxValue" auto-complete="off"></el-input-number>
          </el-form-item>
          <el-form-item label="最小值" prop="minValue" v-if="switchValue === 0">
            <el-input-number :precision="2" :step="0.1" v-model="dataForm.minValue" auto-complete="off"></el-input-number>
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
import {deleteParamThreshold, getParamThreshold, handleAdd, handleUpdate} from "@/api/wlg/iot/moldingMachineParamData";

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
        {prop: "paramId", label: "参数名", minWidth: 200, formatter: this.paramFormat},
        {prop: "threshold", label: "阈值", minWidth: 120},
        {prop: "maxValue", label: "最大值", minWidth: 120},
        {prop: "minValue", label: "最小值", minWidth: 120},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateFormat},
      ],
      paramDesc:{},
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {

      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        machineId: 0,
        paramId: 0,
        arrayId: 0,
        threshold: 0,
        maxValue: 0,
        minValue: 0
      },
      selectUser: {},
      roleData: [],
      currentUserRoles: [],
      paramInfo: [
        {
          param_info:{
            param_id:27,
            array_id:1
          },
          param_name:"lower_moldcore_section_temp_actual_1"
        },
        {
          param_info:{
            param_id:27,
            array_id:2
          },
          param_name:"lower_moldcore_section_temp_actual_2"
        },
        {
          param_info:{
            param_id:27,
            array_id:3
          },
          param_name:"lower_moldcore_section_temp_actual_3"
        },
        {
          param_info:{
            param_id:27,
            array_id:4
          },
          param_name:"lower_moldcore_section_temp_actual_4"
        },
        {
          param_info:{
            param_id:27,
            array_id:5
          },
          param_name:"lower_moldcore_section_temp_actual_5"
        },
        {
          param_info:{
            param_id:27,
            array_id:6
          },
          param_name:"lower_moldcore_section_temp_actual_6"
        },
        {
          param_info:{
            param_id:27,
            array_id:7
          },
          param_name:"lower_moldcore_section_temp_actual_7"
        },
        {
          param_info:{
            param_id:27,
            array_id:8
          },
          param_name:"lower_moldcore_section_temp_actual_8"
        },
        {
          param_info:{
            param_id:27,
            array_id:9
          },
          param_name:"lower_moldcore_section_temp_actual_9"
        },
        {
          param_info:{
            param_id:27,
            array_id:10
          },
          param_name:"lower_moldcore_section_temp_actual_10"
        },
        {
          param_info:{
            param_id:25,
            array_id:0
          },
          param_name:"lower_mold_temp_actual_0"
        },
        {
          param_info:{
            param_id:55,
            array_id:1
          },
          param_name:"upper_moldcore_section_temp_actual_1"
        },
        {
          param_info:{
            param_id:55,
            array_id:2
          },
          param_name:"upper_moldcore_section_temp_actual_2"
        },
        {
          param_info:{
            param_id:55,
            array_id:3
          },
          param_name:"upper_moldcore_section_temp_actual_3"
        },
        {
          param_info:{
            param_id:55,
            array_id:4
          },
          param_name:"upper_moldcore_section_temp_actual_4"
        },
        {
          param_info:{
            param_id:55,
            array_id:5
          },
          param_name:"upper_moldcore_section_temp_actual_5"
        },
        {
          param_info:{
            param_id:55,
            array_id:6
          },
          param_name:"upper_moldcore_section_temp_actual_6"
        },
        {
          param_info:{
            param_id:55,
            array_id:7
          },
          param_name:"upper_moldcore_section_temp_actual_7"
        },
        {
          param_info:{
            param_id:55,
            array_id:8
          },
          param_name:"upper_moldcore_section_temp_actual_8"
        },
        {
          param_info:{
            param_id:55,
            array_id:9
          },
          param_name:"upper_moldcore_section_temp_actual_9"
        },
        {
          param_info:{
            param_id:55,
            array_id:10
          },
          param_name:"upper_moldcore_section_temp_actual_10"
        },
        {
          param_info:{
            param_id:53,
            array_id:0
          },
          param_name:"upper_mold_temp_actual_0"
        },
        {
          param_info:{
            param_id:38,
            array_id:0
          },
          param_name:"press_force_actual_0"
        }
      ],
      switchValue: 1
    }
  },
  methods: {
    onSwitchChange(){
      if(this.switchValue === 1)
      {
        this.dataForm.maxValue = 0
        this.dataForm.minValue = 0
      }else if(this.switchValue === 0){
        this.dataForm.threshold = 0
      }
    },
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
        deleteParamThreshold(data.params[0]).then(data.callback)
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true
      this.operation = true
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        machineId: 0,
        paramId: 0,
        arrayId: 0,
        threshold: 0,
        maxValue: 0,
        minValue: 0
      }
    },
    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
    },
    // 编辑
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            params.paramId = this.paramDesc.param_info.param_id
            params.arrayId = this.paramDesc.param_info.array_id
            params.machineId = this.machineId
            if (this.operation) {
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
    // 重置选择
    resetSelection() {
      this.dialogVisible = false
    },
    paramFormat: function (row, column) {
      const item = this.paramInfo.find(item => {
        return item.param_info.param_id === row.paramId && item.param_info.array_id === row.arrayId
      })
      return item.param_name
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    }
  },
  mounted() {
    setTimeout(() => {
      this.findPage(null)
    }, 100)
  }
}
</script>
