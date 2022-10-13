<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item label="商务项目号" prop="businessProject">
            <el-input v-model="filters.businessProject" clearable placeholder="商务项目号"></el-input>
          </el-form-item>
          <el-form-item label="内部项目号" prop="internalProject">
            <el-input v-model="filters.internalProject" clearable placeholder="内部项目号"></el-input>
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
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage" @handleDelete="handleDelete" @handleEdit="handleEdit">
      </SysTable>


      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="25%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="商务项目号" prop="businessProject">
                <el-input v-model="dataForm.businessProject" :disabled="!operation" auto-complete="off"
                          clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="内部项目号" prop="internalProject">
                <el-input v-model="dataForm.internalProject" auto-complete="off" clearable></el-input>
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
import {deleteProjectMap, findProjectMapPage, handleAdd, handleUpdate,} from "@/api/wlg/report/projectMap";
import {getResponseDataMessage} from "@/utils/commonUtils";

export default {
  name: "projectMapManagement",
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
        {prop: "businessProject", label: "商务项目号", minWidth: 110},
        {prop: "internalProject", label: "内部项目号", minWidth: 100},
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
        businessProject: [{required: true, message: "请输入商务项目号", trigger: "blur"}],
        internalProject: [{required: true, message: "请输入内部项目号", trigger: "blur"}],
      },
      methodOptions: [],
      permissionTypeOptions: [],
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        businessProject: "",
        internalProject: "",
      },
    };
  },
  mounted() {
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.businessProject = this.filters.businessProject;
      this.pageRequest.internalProject = this.filters.internalProject;
      findProjectMapPage(this.pageRequest)
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
        deleteProjectMap(data.params[0]).then(data.callback);
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.operation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        businessProject: "",
        internalProject: "",
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
  },
};
</script>
