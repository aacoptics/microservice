<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-input v-model="filters.code" placeholder="角色名"></el-input>
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
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage"
                @handleCurrentChange="handleRoleSelectChange" @handleDelete="handleDelete" @handleEdit="handleEdit">
      </SysTable>
      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="80%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="80px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="角色代码" prop="code">
            <el-input v-model="dataForm.code" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="角色名称" prop="name">
            <el-input v-model="dataForm.name" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="角色描述" prop="description">
            <el-input v-model="dataForm.description" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <el-row>
          <el-col :span="12">
            <div :v-if="true" class="menu-container" style="padding-top: 10px">
              <div class="menu-header">
                <span>角色菜单授权</span>
              </div>
              <el-tree ref="menuTree"
                       v-loading="menuLoading"
                       :data="menuData"
                       :props="defaultProps"
                       element-loading-text="拼命加载中"
                       highlight-current node-key="id"
                       show-checkbox
                       size="mini"
                       style="width: 100%;padding-top:20px;">
                <template #default="{ data }">
                  <div style="width: 100%;font-weight: bold;font-family: 'Microsoft YaHei',serif">
                    <el-row>
                      <el-col :span="8">
                        <span style="text-align:center">
                          <font-awesome-icon v-if="fontAwesomeIconFormat(data.icon) instanceof Array"
                                             :icon="fontAwesomeIconFormat(data.icon)" fixed-width
                                             style="margin-right: 10px"/>
                          {{ data.title }}
                        </span>
                      </el-col>
                      <el-col :span="5">
               <span style="text-align:center">
                 <el-tag :type="data.parentId === '-1' ? 'primary' : 'success'" size="small">
                   {{ data.parentId === '-1' ? '顶级菜单' : '菜单' }}
                 </el-tag>
               </span>
                      </el-col>
                      <el-col :span="8">
                        <span style="text-align:center;color: #20a0ff">{{ data.path ? data.path : '\t' }}</span>
                      </el-col>
                    </el-row>
                  </div>
                </template>
              </el-tree>
              <div style="float:left;padding-left:24px;padding-top:12px;padding-bottom:4px;">
                <el-checkbox v-model="menuCheckAll" @change="handleMenuCheckAll"><b>全选</b>
                </el-checkbox>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div :v-if="true" class="menu-container" style="padding-top: 10px">
              <div class="menu-header">
                <span>角色接口授权</span>
              </div>
              <el-tree ref="resourceTree"
                       v-loading="resourceLoading"
                       :data="resourceData"
                       :props="resourceProps"
                       element-loading-text="拼命加载中"
                       highlight-current node-key="id"
                       show-checkbox
                       size="mini"
                       style="width: 100%;padding-top:20px;">
                <template #default="{ data }">
                  <div style="width: 100%;font-weight: bold;font-family: 'Microsoft YaHei',serif">
                    <el-row>
                      <el-col :span="20">
                        <span style="text-align:center">{{ data.name }}</span>
                      </el-col>
                      <el-col :span="4">
                        <span style="text-align:center">
                          <el-tag :type="data.children ? 'primary' : 'success'" size="small">
                            {{ data.children ? '模块' : data.method }}
                          </el-tag>
                        </span>
                      </el-col>
                    </el-row>
                  </div>
                </template>
              </el-tree>
              <div style="float:left;padding-left:24px;padding-top:12px;padding-bottom:4px;">
                <el-checkbox v-model="resourceCheckAll" @change="handleResourceCheckAll">
                  <b>全选</b>
                </el-checkbox>
              </div>
            </div>
          </el-col>
        </el-row>

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
import {deleteRole, findRoleInfoPage, handleAdd, handleUpdate} from "@/api/system/role";
import {findMenuTree, findRoleMenus} from "@/api/system/menu";
import {findResourceTree, findRoleResource} from "@/api/system/resource";
import {fontAwesomeIconFormat} from "@/utils/commonUtils";

export default {
  name: "roleManagement",
  components: {SysTable},
  data() {
    return {
      size: 'default',
      filters: {
        code: ''
      },
      columns: [
        {prop: "code", label: "角色代码", minWidth: 100},
        {prop: "name", label: "角色名称", minWidth: 100},
        {prop: "description", label: "角色描述", minWidth: 120},
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
        code: [
          {required: true, message: '请输入角色代码', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入角色名称', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        code: '',
        name: '',
        description: ''
      },
      selectRole: {},
      menuData: [],
      resourceData: [],
      menuSelections: [],
      menuLoading: false,
      resourceLoading: false,
      authLoading: false,
      menuCheckAll: false,
      resourceCheckAll: false,
      currentRoleMenus: [],
      currentRoleResource: [],
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      resourceProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.code = this.filters.code
      findRoleInfoPage(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
          this.findMenuTreeData()
          this.findResourceTreeData()
        }
      }).then(data != null ? data.callback : '')
    },
    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteRole(data.params[0]).then(data.callback)
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true
      this.operation = true
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        code: '',
        name: '',
        description: ''
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
            let checkedMenuNodes = this.$refs.menuTree.getCheckedNodes(false, true)
            let checkedResourceNodes = this.$refs.resourceTree.getCheckedNodes(true, false)
            let roleMenus = []
            let roleResources = []
            for (let i = 0, len = checkedMenuNodes.length; i < len; i++) {
              roleMenus.push(checkedMenuNodes[i].id)
            }
            for (let i = 0, len = checkedResourceNodes.length; i < len; i++) {
              roleResources.push(checkedResourceNodes[i].id)
            }
            params.resourceIds = roleResources
            params.menuIds = roleMenus
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
    // 获取数据
    findResourceTreeData: function () {
      this.resourceLoading = true
      findResourceTree().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          const tempData = {}
          responseData.data.forEach(resource => {
            if (!Object.prototype.hasOwnProperty.call(tempData, resource.type)) {
              tempData[resource.type] = {children: []}
            }
            tempData[resource.type].children.push(resource)
          })
          const resourcesData = []
          for (const key in tempData) {
            const resource = {name: key, children: tempData[key].children}
            resourcesData.push(resource)
          }
          this.resourceData = resourcesData
        }
        this.resourceLoading = false
      })
    },
    getChildrenList(arr, res) {
      arr.forEach(item => {
        if (item.children && item.children.length > 0) {
          this.getChildrenList(item.children, res)
        } else {
          res.push(item)
        }
      })
    },
    // 角色选择改变监听
    handleRoleSelectChange(val) {
      if (val == null || val.val == null) {
        this.selectRole = null
        this.$refs.menuTree.setCheckedNodes([])
        this.$refs.resourceTree.setCheckedNodes([])
        return
      }
      this.selectRole = val.val
      findRoleMenus(val.val.id).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.currentRoleMenus = responseData.data
          const children = []
          this.getChildrenList(responseData.data, children)
          this.$refs.menuTree.setCheckedNodes(children)
        }
      })
      findRoleResource(val.val.id).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.currentRoleResource = responseData.data
          this.$refs.resourceTree.setCheckedNodes(responseData.data)
        }
      })
    },
    // 重置选择
    resetSelection() {
      this.menuCheckAll = false
      this.resourceCheckAll = false
      const children = []
      this.getChildrenList(this.currentRoleMenus, children)
      this.$refs.menuTree.setCheckedNodes(children)
      this.$refs.resourceTree.setCheckedNodes(this.currentRoleResource)
      this.dialogVisible = false
    },
    // 全选操作
    handleMenuCheckAll() {
      if (this.menuCheckAll) {
        let allMenus = []
        this.checkAll(this.menuData, allMenus)
        this.$refs.menuTree.setCheckedNodes(allMenus)
      } else {
        this.$refs.menuTree.setCheckedNodes([])
      }
    },
    handleResourceCheckAll() {
      if (this.resourceCheckAll) {
        let allResource = []
        this.checkAll(this.resourceData, allResource)
        this.$refs.resourceTree.setCheckedNodes(allResource)
      } else {
        this.$refs.resourceTree.setCheckedNodes([])
      }
    },
    // 递归全选
    checkAll(menuData, allMenus) {
      menuData.forEach(menu => {
        allMenus.push(menu)
        if (menu.children) {
          this.checkAll(menu.children, allMenus)
        }
      });
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    fontAwesomeIconFormat: (icon) => fontAwesomeIconFormat(icon),
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
</style>
