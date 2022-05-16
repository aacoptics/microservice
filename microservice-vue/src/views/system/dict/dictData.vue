<template>
  <div>
    <div class="container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item label="字典标签" prop="dictLabel">
            <el-input
                v-model="filters.dictLabel"
                clearable
                placeholder="请输入字典标签"
                size="small"
            />
          </el-form-item>
          <el-form-item label="字典键值" prop="dictLabel">
            <el-input
                v-model="filters.dictValue"
                clearable
                placeholder="请输入字典键值"
                size="small"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="filters.status" clearable placeholder="数据状态" size="small">
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
                <i class="fa-solid fa-magnifying-glass fa-sm"></i>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="handleAdd">新增
              <template #icon>
                <i class="fa-solid fa-plus fa-sm"></i>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="460" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage" @handleDelete="handleDelete" @handleEdit="handleEdit">
      </SysTable>

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="25%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="120px">
          <el-form-item label="字典类型">
            <el-input v-model="dataForm.dictType" :disabled="true"/>
          </el-form-item>
          <el-form-item label="数据标签" prop="dictLabel">
            <el-input v-model="dataForm.dictLabel" placeholder="请输入数据标签"/>
          </el-form-item>
          <el-form-item label="数据键值" prop="dictValue">
            <el-input v-model="dataForm.dictValue" placeholder="请输入数据键值"/>
          </el-form-item>
          <el-form-item label="显示排序" prop="dictSort">
            <el-input-number v-model="dataForm.dictSort" :min="0" controls-position="right"/>
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
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";

import {findDictTypeById} from "@/api/system/dictType";
import {
  deleteDictData,
  findDictDataPage,
  getDict,
  handleAdd,
  handleUpdate,
  selectDictLabel
} from "@/api/system/dictData";

export default {
  name: "dictData",
  components: {SysTable},
  props: {
    dictId: Number, // 表格分页数据},
  },
  data() {
    return {
      size: 'default',
      filters: {
        dictType: undefined,
        dictLabel: undefined,
        dictValue: undefined,
        status: undefined
      },
      // 状态数据字典
      statusOptions: [],
      // 类型数据字典
      typeOptions: [],
      columns: [
        {prop: "dictType", label: "字典类型", minWidth: 120, sortable: false},
        {prop: "dictLabel", label: "字典标签", minWidth: 120, sortable: false},
        {prop: "dictValue", label: "字典键值", minWidth: 100, sortable: false},
        {prop: "dictSort", label: "字典排序", minWidth: 90},
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
      editLoading: false,
      dataFormRules: {
        dictLabel: [{required: true, message: '数据标签不能为空', trigger: 'blur'}],
        dictValue: [{required: true, message: '数据键值不能为空', trigger: 'blur'}],
        dictSort: [{required: true, message: '数据顺序不能为空', trigger: 'blur'}],
        status: [{required: true, message: '请输入状态', trigger: 'blur'}]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        dictType: '',
        dictLabel: '',
        dictValue: '',
        dictSort: '',
        status: '',
        remark: ''
      },
      callback: undefined
    }
  },
  // created() {
  //   //const dictId = this.$route.params && this.$route.params.dictId;
  //   this.getType(this.dictId);
  //   getDict("sys_normal_disable").then(response => {
  //     this.statusOptions = response.data.data
  //   })
  // },
  methods: {
    /** 查询字典类型详细 */
    getType(dictTypeId) {
      findDictTypeById(dictTypeId).then(response => {
        if (response.data.code === '000000') {
          this.filters.dictType = response.data.data.dictType;
          this.findPage(null);
        }
      });
    },
    // 获取分页数据
    findPage: function (data) {
      if (this.filters.dictType === undefined) {
        if (data != null) this.callback = data.callback;
        return;
      }
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.dictType = this.filters.dictType;
      this.pageRequest.dictValue = this.filters.dictValue;
      this.pageRequest.dictValue = this.filters.dictValue;
      this.pageRequest.status = this.filters.status;
      findDictDataPage(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
        this.callback != null && this.callback();
      }).then(data != null ? data.callback : '')
    },
    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteDictData(data.params[0]).then(data.callback)
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true
      this.operation = true
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        dictType: this.filters.dictType,
        dictLabel: '',
        dictValue: '',
        dictSort: 1,
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
                    message: '操作失败, ' + responseData.msg + (responseData.data === undefined ? '' : responseData.data),
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
                    message: '操作失败, ' + responseData.msg + (responseData.data === undefined ? '' : responseData.data),
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
    refreshDictData() {
      this.getType(this.dictId);
      getDict("sys_normal_disable").then(response => {
        this.statusOptions = response.data.data
      })
    }
  },
  mounted() {
    this.refreshDictData()
  }
}
</script>
