<template>
  <div>
    <div class="container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item label="接口类型" prop="type">
            <el-input v-model="filters.type" clearable placeholder="接口类型"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="filters.name" clearable placeholder="名称"></el-input>
          </el-form-item>
          <el-form-item label="请求方法" prop="method">
            <el-select v-model="filters.method" clearable placeholder="请求方法">
              <el-option
                  v-for="item in methodOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item clearable label="权限类型" prop="permissionType">
            <el-select v-model="filters.permissionType" auto-complete="off" clearable
                       placeholder="权限类型">
              <el-option
                  v-for="item in permissionTypeOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
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
      <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="460" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage" @handleDelete="handleDelete" @handleEdit="handleEdit">
      </SysTable>


      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="40%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="接口编码" prop="code">
                <el-input v-model="dataForm.code" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="接口类型" prop="type">
                <el-input v-model="dataForm.type" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="名称" prop="name">
                <el-input v-model="dataForm.name" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="URL" prop="url">
                <el-input v-model="dataForm.url" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="请求方法" prop="method">
                <el-select v-model="dataForm.method" clearable placeholder="请求方法" style="width:100%">
                  <el-option
                      v-for="item in methodOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="描述" prop="description">
                <el-input v-model="dataForm.description" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item clearable label="权限类型" prop="permissionType">
                <el-select v-model="dataForm.permissionType" auto-complete="off"
                           placeholder="权限类型">
                  <el-option
                      v-for="item in permissionTypeOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  />
                </el-select>
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
import SysTable from "@/components/SysTable";
import {deleteResource, findResourcePage, handleAdd, handleUpdate,} from "@/api/system/resource";
import {getDict, selectDictLabel} from "@/api/system/dictData";
import {getResponseDataMessage} from "@/utils/commonUtils";

export default {
  name: "resource",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        type: "",
        name: "",
        method: "",
        permissionType: null,
      },
      columns: [
        {prop: "code", label: "接口编码", minWidth: 110},
        {prop: "type", label: "接口类型", minWidth: 100},
        {prop: "name", label: "名称", minWidth: 120},
        {prop: "url", label: "URL", minWidth: 120},
        {prop: "method", label: "请求方法", minWidth: 100},
        {prop: "description", label: "描述", minWidth: 150},
        {prop: "permissionType", label: "权限类型", minWidth: 100, formatter: this.permissionTypeFormat},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        code: [{required: true, message: "请输入接口编码", trigger: "blur"}],
        type: [{required: true, message: "请输入接口类型", trigger: "blur"}],
        name: [{required: true, message: "请输入接口名称", trigger: "blur"},],
        url: [{required: true, message: "请输入URL", trigger: "blur"},],
        method: [{required: true, message: "请输入接口方法", trigger: "blur"},],
        description: [{required: true, message: "请输入描述", trigger: "blur"},],
        permissionType: [{required: true, message: "请选择权限类型", trigger: "blur"},],
      },
      methodOptions: [],
      permissionTypeOptions: [],
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        code: "",
        type: "",
        name: "",
        url: "",
        method: "",
        description: "",
        permissionType: null,
      },
    };
  },
  mounted() {
    getDict("permission_type").then(response => {
      this.permissionTypeOptions = response.data.data
    })
    getDict("request_method").then(response => {
      this.methodOptions = response.data.data
    })
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.type = this.filters.type;
      this.pageRequest.name = this.filters.name;
      this.pageRequest.method = this.filters.method;
      this.pageRequest.permissionType = this.filters.permissionType;
      findResourcePage(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },

    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteResource(data.params[0]).then(data.callback);
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.operation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        code: "",
        type: "",
        name: "",
        url: "",
        method: "",
        description: "",
        permissionType: "1",
      };
    },

    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.operation = false;
      this.dataForm = Object.assign({}, params.row);
    },
    // 编辑
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.operation) {
              handleAdd(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.dialogVisible = false;
                  this.$refs["dataForm"].resetFields();
                } else {
                  this.$message({
                    message:
                        "操作失败 " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
                this.findPage(null);
              });
            } else {
              handleUpdate(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.dialogVisible = false;
                  this.$refs["dataForm"].resetFields();
                } else {
                  this.$message({
                    message:
                        "操作失败, " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
                this.findPage(null);
              });
            }
          });
        }
      });
    },

    // 取消
    cancel() {
      this.dialogVisible = false;
    },

    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
    permissionTypeFormat: function (row) {
      return selectDictLabel(this.permissionTypeOptions, row.permissionType);
    },
  },
};
</script>
