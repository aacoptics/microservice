<template>
  <div>
    <div class="container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item label="字典类型" prop="dictType">
            <el-input
                v-model="filters.dictType"
                clearable
                placeholder="请输入字典类型"
                size="small"
                style="width: 240px"
            />
          </el-form-item>
          <el-form-item label="字典名称" prop="dictName">
            <el-input
                v-model="filters.dictName"
                clearable
                placeholder="请输入字典名称"
                size="small"
                style="width: 240px"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="filters.status" clearable placeholder="字典状态" size="small"
                       style="width: 240px">
              <el-option
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
              />
            </el-select>
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

      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="460" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage" @handleDelete="handleDelete" @handleEdit="handleEdit">
        <template v-slot:custom-column>
          <el-table-column align="center" fixed="right" header-align="center" label="字典类型"
                           width="200">
            <template v-slot="scope">
              <el-link :underline="false" type="primary" @click="openDictDetail(scope.row.id)">
                <span>{{ scope.row.dictType }}</span>
              </el-link>
            </template>
          </el-table-column>
        </template>
      </SysTable>

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="25%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="字典名称" prop="dictName">
            <el-input v-model="dataForm.dictName" placeholder="请输入字典名称"/>
          </el-form-item>
          <el-form-item label="字典类型" prop="dictType">
            <el-input v-model="dataForm.dictType" placeholder="请输入字典类型"/>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="dataForm.status">
              <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
              >{{ dict.dictLabel }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="dataForm.remark" placeholder="请输入内容" type="textarea"></el-input>
          </el-form-item>
        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" @click="resetSelection">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitForm">提交</el-button>
          </slot>
        </div>
      </el-dialog>
      <el-dialog v-model="dictDataDialogVisible" destroy-on-close title="字典数据" width="90%">
        <dict-data ref="dictData" :dictId="dictId"></dict-data>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";

import {deleteDictType, findDictTypePage, handleAdd, handleUpdate} from "@/api/system/dictType";
import {getDict, selectDictLabel} from "@/api/system/dictData";
import dictData from "./dictData"
import {getResponseDataMessage} from "@/utils/commonUtils";

export default {
  name: "environmentParameter",
  components: {SysTable, dictData},
  data() {
    return {
      size: 'default',
      filters: {
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      statusOptions: [],
      columns: [
        {prop: "dictName", label: "字典名称", minWidth: 120, sortable: false},
        {prop: "status", label: "状态", minWidth: 80, sortable: false, formatter: this.statusFormat},
        {prop: "remark", label: "备注", minWidth: 150, sortable: false},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 80, sortable: false},
        {prop: "updatedTime", label: "修改时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "updatedBy", label: "修改人", minWidth: 80, sortable: false},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      dictDataDialogVisible: false,
      editLoading: false,
      dataFormRules: {
        dictName: [{required: true, message: '请输入字典名称', trigger: 'blur'}],
        dictType: [{required: true, message: '请输入字典类型', trigger: 'blur'}],
        status: [{required: true, message: '请输入状态', trigger: 'blur'}]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        dictName: '',
        dictType: '',
        status: '',
        remark: ''
      },
      dictId: 0
    }
  },
  created() {
    getDict("sys_normal_disable").then(response => {
      this.statusOptions = response.data.data
    })
  },
  methods: {
    openDictDetail(dictId) {
      this.dictId = dictId
      this.dictDataDialogVisible = true
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.dictName = this.filters.dictName;
      this.pageRequest.dictType = this.filters.dictType;
      this.pageRequest.status = this.filters.status;
      findDictTypePage(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },
    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteDictType(data.params[0]).then(data.callback)
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true
      this.operation = true
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        dictName: '',
        dictType: '',
        status: '',
        remark: ''
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
            if (this.operation) {
              handleAdd(params).then((res) => {
                const responseData = res.data
                this.editLoading = false
                if (responseData.code === '000000') {
                  this.$message({message: '操作成功', type: 'success'})
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({
                    message: `操作失败${getResponseDataMessage(responseData)}`,
                    type: 'error'
                  })
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
                  this.$message({
                    message: `操作失败${getResponseDataMessage(responseData)}`,
                    type: 'error'
                  })
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
    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    statusFormat(row) {
      return selectDictLabel(this.statusOptions, row.status);
    },
  }
}
</script>
