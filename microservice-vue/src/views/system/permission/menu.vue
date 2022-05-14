<template>
  <div>
    <div class="container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button icon="el-icon-search" type="primary"
                       @click="findMenuTreeData()">查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-plus" type="success"
                       @click="handleAddTop">新增
            </el-button>
          </el-form-item>
        </el-form>
      </div>
          <el-table :data="menuData"
                    row-key="id"
                    size="mini"
                    :height="600"
                    :tree-props="defaultProps"
                    style="width: 100%; margin-bottom: 20px"
                    ref="treeTable"
                    border
                    v-loading="menuLoading"
                    default-expand-all
                    element-loading-text="加载中...">
            <el-table-column prop="title" label="菜单名称"  width="180" />
            <el-table-column label="图标" width="60">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <span style="text-align:left"><i :class="scope.row.icon" style="margin-right: 10px"></i></span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="菜单编码"  width="200" />
            <el-table-column prop="orderNum" label="排序"  width="120" />
            <el-table-column prop="path" label="菜单路径"  width="200" />
            <el-table-column prop="component" label="Vue组件"  width="280" />
            <el-table-column prop="description" label="描述"  width="180" />
            <el-table-column label="菜单栏是否显示" prop="visible" width="120"/>
            <el-table-column prop="webUrl" label="URL"  width="500" />
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="scope">
                <el-button size="mini" type="primary" @click="handleAdd(scope.row)"
                  >新增</el-button
                >
                <el-button size="mini" type="info" @click="handleEdit(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>

      <el-dialog :title="operation?'新增':'编辑'" width="40%" v-model="dialogVisible"
                 :close-on-click-modal="false">
        <el-form :model="dataForm" label-width="100px" :rules="dataFormRules" ref="dataForm" :size="size">
          <el-form-item label="Id" prop="id" v-if="false">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="菜单编码" prop="name">
                <el-input v-model="dataForm.name" auto-complete="off" :disabled="!operation" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
          <el-form-item label="菜单名称" prop="title">
            <el-input v-model="dataForm.title" auto-complete="off" clearable></el-input>
          </el-form-item>
           </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="Vue组件" prop="component">
                <el-input v-model="dataForm.component" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="图标" prop="icon">
                <el-input v-model="dataForm.icon" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="菜单路径" prop="path">
                <el-input v-model="dataForm.path" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序" prop="orderNum">
                <el-input-number v-model="dataForm.orderNum" auto-complete="off" clearable
                                 style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="URL" prop="webUrl">
                <el-input v-model="dataForm.webUrl" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="菜单栏是否显示" prop="visible">
                <el-switch v-model="dataForm.visible"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="描述" prop="description">
                <el-input v-model="dataForm.description" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" @click="cancel">取消</el-button>
            <el-button :size="size" type="primary" @click="submitForm" :loading="editLoading">提交</el-button>
          </slot>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {deleteMenu, findMenuTree, handleAdd, handleUpdate} from "@/api/system/menu";

export default {
  name: "menu",
  data() {
    return {
      size: 'small',
      filters: {
        code: ''
      },
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        name: [
          {required: true, message: '请输入菜单编码', trigger: 'blur'}
        ],
        title: [
          {required: true, message: '请输入菜单名称', trigger: 'blur'}
        ],
        orderNum: [
          {required: true, message: '请输入排序', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        name: '',
        title: '',
        description: '',
        icon: '',
        orderNum: '',
        path: '',
        component: '',
        webUrl: '',
        visible: true
      },
      menuData: [],
      menuLoading: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      },

    }
  },
  methods: {

        // 显示新增顶级菜单
    handleAddTop: function () {
      this.editLoading = false
      this.dialogVisible = true
      this.operation = true
      this.dataForm = {
        parentId: -1,
        id: 0,
        name: '',
        title: '',
        description: '',
        icon: '',
        orderNum: 0,
        path: '',
        component: '',
        webUrl: '',
        visible: true
      }
    },
    // 显示新增界面
    handleAdd: function (params) {
      this.editLoading = false
      this.dialogVisible = true
      this.operation = true
      this.dataForm = {
        parentId: params.id,
        id: 0,
        name: '',
        title: '',
        description: '',
        icon: '',
        orderNum: 0,
        path: '',
        component: '',
        webUrl: '',
        visible: true
      }
    },
    // 显示编辑界面
    handleEdit: function (params) {
      this.editLoading = false
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params)
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

                  this.findMenuTreeData()
                } else {
                  this.$message({message: '操作失败, ' + responseData.msg, type: 'error'})
                }

              })
            } else {
              handleUpdate(params).then((res) => {
                const responseData = res.data
                this.editLoading = false
                if (responseData.code === '000000') {
                  this.$message({message: '操作成功', type: 'success'})
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()

                  this.findMenuTreeData()
                } else {
                  this.$message({message: '操作失败, ' + responseData.msg, type: 'error'})
                }
              })
            }
          })
        }
      })
    },
    // 获取数据
    findMenuTreeData: function () {
      this.menuLoading = true
      findMenuTree().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.menuData = responseData.data
        }
        this.menuLoading = false
      })
    },

      // 重置选择
    resetSelection() {
      this.dialogVisible = false
    },
    // 删除操作
    handleDelete: function (data) {
      this.$confirm('确认删除选中记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {};
        params.id = data.id;

        deleteMenu(params).then((res) => {
          const responseData = res.data
          if (responseData.code === '000000') {
            this.$message({message: '删除成功', type: 'success'})
            this.findMenuTreeData()
          } else {
            this.$message({message: '操作失败, ' + responseData.msg, type: 'error'})
          }
        })

      }).catch((err) => {
        console.log(err)
      })
    },

    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    // 取消
    cancel() {
      this.dialogVisible = false;
    },
  },
  mounted() {
    this.findMenuTreeData();
  }
}
</script>

<style scoped>
.menu-container {
  margin-top: 10px;
}

.menu-header {
  padding-left: 8px;
  padding-bottom: 5px;
  text-align: left;
  font-size: 16px;
  color: rgb(20, 89, 121);

}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
