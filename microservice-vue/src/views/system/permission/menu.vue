<template>
  <div>
    <div class="container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" size="default">
          <el-form-item>
            <el-button type="primary" @click="findMenuTreeData()">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="handleAddTop">新增
              <template #icon>
                <font-awesome-icon :icon="['fas', 'plus']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="info" plain @click="toggleExpandAll">展开/折叠
              <template #icon>
                <font-awesome-icon :icon="['fas', 'arrow-right-arrow-left']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table ref="treeTable"
                v-if="refreshTable"
                :id="randomId"
                v-loading="menuLoading"
                :data="menuData"
                :height="height"
                :size="size"
                :tree-props="defaultProps"
                border
                :default-expand-all="isExpandAll"
                element-loading-text="加载中..."
                row-key="id"
                style="width: 100%; margin-bottom: 20px"
                :row-class-name="tableRowClassName">
        <el-table-column label="菜单名称" prop="title" width="180"/>
        <el-table-column label="图标" width="60">
          <template #default="scope">
            <font-awesome-icon v-if="fontAwesomeIconFormat(scope.row.icon) instanceof Array"
                               :icon="fontAwesomeIconFormat(scope.row.icon)" pull="left" size="lg"/>
          </template>
        </el-table-column>
        <el-table-column label="菜单编码" prop="name" width="200"/>
        <el-table-column label="排序" prop="orderNum" width="120"/>
        <el-table-column label="菜单路径" prop="path" width="200"/>
        <el-table-column label="Vue组件" prop="component" width="280"/>
        <el-table-column label="描述" prop="description" width="180"/>
        <el-table-column label="显示状态" prop="visible" width="80"/>
        <el-table-column label="URL" prop="webUrl" width="500"/>
        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-button :size="size" type="primary" @click="handleAdd(scope.row)">新增</el-button>
            <el-button :size="size" type="info" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button :size="size" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="40%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" label-width="100px" size="default">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="24">
              <el-form-item label="菜单类型" prop="menuType">
                <el-radio-group v-model="dataForm.menuType">
                  <el-radio
                      v-for="item in menuTypeOptions"
                      :key="item.dictValue"
                      :label="item.dictValue">
                    {{ item.dictLabel }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item prop="name">
                <template #label>
                    <span>
                      <el-tooltip content="例如: userManagement" placement="top">
                        <font-awesome-icon :icon="['fa-solid', 'circle-question']"/>
                      </el-tooltip>
                      菜单编码
                    </span>
                </template>
                <el-input v-model="dataForm.name" :disabled="!operation" auto-complete="off" clearable
                          placeholder="请输入菜单编码"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item prop="title">
                <template #label>
                    <span>
                      <el-tooltip content="例如: 用户管理" placement="top">
                        <font-awesome-icon :icon="['fa-solid', 'circle-question']"/>
                      </el-tooltip>
                      菜单名称
                    </span>
                </template>
                <el-input v-model="dataForm.title" auto-complete="off" clearable placeholder="请输入菜单名称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="排序" prop="orderNum">
                <el-input-number v-model="dataForm.orderNum" auto-complete="off" clearable
                                 style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="显示状态" prop="visible">
                <el-switch v-model="dataForm.visible"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="图标" prop="icon">
                <el-popover
                    :width="500"
                    placement="bottom-start"
                    trigger="click"
                    @show="$refs['iconSelect'].reset()">
                  <template #reference>
                    <el-input v-model="dataForm.icon" auto-complete="off" clearable placeholder="点击选择图标" readonly>
                      <template #prefix>
                        <font-awesome-icon v-if="dataForm.icon" :icon="JSON.parse(dataForm.icon)" style="color: gray"/>
                        <font-awesome-icon v-else :icon="['fa-solid', 'magnifying-glass']"/>
                      </template>
                      <template #append>
                        <font-awesome-icon :icon="['fa-solid', 'circle-xmark']" style="cursor: pointer"
                                           @click="dataForm.icon=''"/>
                      </template>
                    </el-input>
                  </template>
                  <IconSelect ref="iconSelect" @selected="selected"/>
                </el-popover>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="dataForm.menuType !== '0'">
            <el-col :span="24">
              <el-form-item prop="path">
                <template #label>
                    <span>
                      <el-tooltip content="例如: /user" placement="top">
                         <font-awesome-icon :icon="['fa-solid', 'circle-question']"/>
                      </el-tooltip>
                      路由地址
                    </span>
                </template>
                <el-input v-model="dataForm.path" auto-complete="off" clearable placeholder="请输入路由地址"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="dataForm.menuType === '1'">
            <el-col :span="24">
              <el-form-item prop="component">
                <template #label>
                    <span>
                      <el-tooltip content="例如: views/system/permission/user.vue" placement="top">
                        <font-awesome-icon :icon="['fa-solid', 'circle-question']"/>
                      </el-tooltip>
                      组件路径
                    </span>
                </template>
                <el-input v-model="dataForm.component" auto-complete="off" clearable
                          placeholder="请输入组件路径"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="dataForm.menuType === '2'">
            <el-col :span="24">
              <el-form-item prop="webUrl">
                <template #label>
                    <span>
                      <el-tooltip content="例如: https://www.aacoptics.com/" placement="top">
                        <font-awesome-icon :icon="['fa-solid', 'circle-question']"/>
                      </el-tooltip>
                      外链地址
                    </span>
                </template>
                <el-input v-model="dataForm.webUrl" auto-complete="off" clearable
                          placeholder="请输入外链地址"></el-input>
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
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitForm">提交</el-button>
          </slot>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {deleteMenu, findMenuTree, handleAdd, handleUpdate} from "@/api/system/menu";
import IconSelect from "@/components/IconSelect";
import {fontAwesomeIconFormat} from "@/utils/commonUtils";
import {getDict} from "@/api/system/dictData";
import {v4 as uuidV4} from "uuid";

export default {
  name: "menu",
  components: {IconSelect},
  data() {
    return {
      size: 'small',
      filters: {
        code: ''
      },
      height: 460,
      randomId: uuidV4(),
      refreshTable: true, // 重新渲染表格状态
      isExpandAll: false,
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        menuType: [
          {required: true, message: '请选择菜单类型', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入菜单编码', trigger: 'blur'}
        ],
        title: [
          {required: true, message: '请输入菜单名称', trigger: 'blur'}
        ],
        orderNum: [
          {required: true, message: '请输入排序', trigger: 'blur'}
        ],
        visible: [
          {required: true, message: '请选择是否显示', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        menuType: '0',
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
      // 菜单
      menuTypeOptions: []
    }
  },
  methods: {
    selected(name) {
      this.dataForm.icon = name
    },
    // 显示新增顶级菜单
    handleAddTop: function () {
      this.editLoading = false
      this.dialogVisible = true
      this.operation = true
      this.dataForm = {
        parentId: -1,
        id: 0,
        menuType: '0',
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
        menuType: '0',
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
      this.dataForm.menuType = this.dataForm.menuType.toString()
    },
    // 编辑
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            if (params.menuType === '2') params.component = 'views/webframe/WebFrame.vue'
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
        this.refresh()
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
    tableRowClassName(row) {
      if (this.isExpandAll === false) return ''
      return row.row.menuType === 0 ? 'success-row' : row.row.menuType === 2 ? 'warning-row' : ''
    },
    refresh() {
      this.$nextTick(() => {
        let table = document.getElementById(this.randomId);
        if (table === null || table === undefined) return
        this.height = Math.max(...[...table.getElementsByTagName('tbody')].map(t => t.clientHeight)) +
            Math.max(...[...table.getElementsByTagName('thead')].map(t => t.clientHeight)) + 5
      })
    },
    // 展开/折叠操作
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
      setTimeout(() => {
        this.refresh()
      }, 0)
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    fontAwesomeIconFormat: (icon) => fontAwesomeIconFormat(icon),
    // 取消
    cancel() {
      this.dialogVisible = false;
    },
  },
  mounted() {
    this.findMenuTreeData();
    getDict("sys_menu_type").then(response => {
      this.menuTypeOptions = response.data.data
    })
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
