<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="异常分类" prop="exceptionType">
            <el-input v-model="filters.exceptionType" clearable placeholder="异常分类"></el-input>
          </el-form-item>

        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="findLoading" type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
            <el-button type="success" @click="handleAdd">新增
              <template #icon>
                <font-awesome-icon :icon="['fas', 'plus']"/>
              </template>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <SysTable id="condDataTable" ref="sysTable" :cell-style="{'text-align':'left'}" :columns="columns"
                :data="pageResult" :header-cell-style="{'text-align':'center'}" :height="400"
                :highlightCurrentRow="true" :showBatchDelete="false" :stripe="true" border
                @findPage="findPage" @handleCurrentChange="handleCurrentChange" @handleDelete="handleDelete"
                @handleEdit="handleEdit">
      </SysTable>

      <el-tabs style="margin-top: 50px;" type="border-card">
        <el-tab-pane label="异常子类">
          <el-button style="margin-bottom: 10px;" type="success" @click="handleAddSubClass">新增异常子类
            <template #icon>
              <font-awesome-icon :icon="['fas', 'plus']"/>
            </template>
          </el-button>
          <el-table v-loading="findDetailLoading" :cell-style="{'text-align':'left'}" :data="subClassTableData"
                    :header-cell-style="{'text-align':'center'}"
                    border size="small">
            <el-table-column label="异常子类" prop="subClass" width="180"/>
            <el-table-column label="更新人" prop="updatedBy"/>
            <el-table-column :formatter="dateTimeFormat" label="更新时间" prop="updatedTime"/>
            <el-table-column label="创建人" prop="createdBy"/>
            <el-table-column :formatter="dateTimeFormat" label="创建时间" prop="createdTime"/>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleSubClassEdit(scope.$index, scope.row)"
                >
                  <template #icon>
                    <font-awesome-icon :icon="['far', 'pen-to-square']"/>
                  </template>
                </el-button
                >
                <el-button
                    size="small"
                    type="danger"
                    @click="handleSubClassDelete(scope.$index, scope.row)"
                >
                  <template #icon>
                    <font-awesome-icon :icon="['far', 'trash-can']"/>
                  </template>
                </el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="isExceptionTypeAddOperation?'新增':'编辑'"
                 destroy-on-close width="25%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="异常分类" prop="exceptionType">
                <el-input v-model="dataForm.exceptionType" clearable placeholder="异常分类"></el-input>
              </el-form-item>
            </el-col>
                      
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" style="margin-right: 0px;" type="info" @click="cancel">取消</el-button>
            <el-button :loading="editLoading" :size="size" style="margin-right: 20px;" type="primary"
                       @click="submitExceptionType">提交
            </el-button>
          </slot>
        </div>
      </el-dialog>


      <el-dialog v-model="subClassDialogVisible" :close-on-click-modal="false"
                 :title="isSubClassAddOperation?'新增异常子类':'新增异常子类'"
                 destroy-on-close width="25%">
        <el-form ref="subClassDataForm" :model="subClassDataForm" :rules="subClassDataFormRules"
                 :size="size" label-width="120px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="subClassDataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item v-if="false" label="exceptionTypeId" prop="exceptionTypeId">
            <el-input v-model="subClassDataForm.exceptionTypeId" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="异常子类" prop="subClass">
                <el-input v-model="subClassDataForm.subClass" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
           
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" type="info" @click="cancelSubClass">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitSubClass">提交</el-button>
          </slot>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {
  findExceptionTypePage,
  findExceptionTypeById,
  deleteExceptionType,
  handleAdd,
  handleUpdate,
  addExceptionSubClass,
  updateExceptionSubClass,
  deleteExceptionSubClass,
} from "@/api/wlg/equipment/exceptionTypeManagement";
import {getResponseDataMessage} from "@/utils/commonUtils";

export default {
  name: "exceptionType",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        exceptionType: "",
      },
      columns: [
        {prop: "exceptionType", label: "异常分类", minWidth: 150},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},

      subClassTableData: [],

      isExceptionTypeAddOperation: false, // true:新增, false:编辑
      isSubClassAddOperation: false,

      dialogVisible: false, // 新增编辑界面是否显示
      subClassDialogVisible: false,

      dataFormRules: {
        exceptionType: [{required: true, message: "请输入异常分类", trigger: "blur"}],
      },
      subClassDataFormRules: {
        subClass: [{required: true, message: "请输入异常子类", trigger: "blur"}],
      },

      // 新增编辑界面数据
      dataForm: {
        id: 0,
        exceptionType: '',
      },
      subClassDataForm: {
        exceptionTypeId: null,
        id: 0,
        subClass: '',
      },
      currentSelectExceptionTypeMainRowId: null,
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
      this.pageRequest.exceptionType = this.filters.exceptionType;

      this.findLoading = true;
      findExceptionTypePage(this.pageRequest)
          .then((res) => {
            this.findLoading = false;
            this.currentSelectExceptionTypeMainRowId = null;
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            }
          })
          .then(data != null ? data.callback : "");
    },
    findExceptionTypeDetail: function (id) {
      this.findDetailLoading = true;
      findExceptionTypeById(id)
          .then((res) => {
            this.findDetailLoading = false;
            const responseData = res.data;
            if (responseData.code === "000000") {
              if (responseData.data != null) {
                this.subClassTableData = responseData.data.exceptionSubClassList;
              }
            }
          });
    },
    handleCurrentChange: function (val) {
      if (val == null || val.val == null) {
        this.currentSelectExceptionTypeMainRowId = null;
        return;
      }
      this.currentSelectExceptionTypeMainRowId = val.val.id;
      this.findExceptionTypeDetail(this.currentSelectExceptionTypeMainRowId);
    },
    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteExceptionType(data.params[0]).then(data.callback);
    },
    handleSubClassDelete: function (index, row) {
      if (row != null) {
        this.$confirm('确认删除选中记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteExceptionSubClass(row).then((res) => {
            const responseData = res.data
            if (responseData.code === '000000') {
              this.$message({message: '删除成功', type: 'success'})
              this.findExceptionTypeDetail(this.currentSelectExceptionTypeMainRowId);
            } else {
              this.$message({
                message: `操作失败${getResponseDataMessage(responseData)}`,
                type: 'error'
              })
            }
            this.loading = false
          });
        })
      }

    },
      // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.isExceptionTypeAddOperation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        exceptionType: "",
      };
    },
    // 显示新增界面
    handleAddSubClass: function () {
      if (this.currentSelectExceptionTypeMainRowId == null || this.currentSelectExceptionTypeMainRowId == 0) {
        this.$message({
          message:
              "请先选择一行异常分类",
          type: "error",
        });
        return;
      }

      this.subClassDialogVisible = true;
      this.isSubClassAddOperation = true;
      this.subClassDataForm = {
        exceptionTypeId: this.currentSelectExceptionTypeMainRowId,
        id: 0,
        subClass: '',
      }
    },
 
    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.isInsubClasstionAddOperation = false;
      this.dataForm = Object.assign({}, params.row);
    },
    handleSubClassEdit: function (index, row) {
      this.subClassDialogVisible = true;
      this.isSubClassAddOperation = false;
      this.subClassDataForm = Object.assign({}, row);
    },
    
    // 编辑
    submitExceptionType: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.isExceptionTypeAddOperation) {
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
   
    // 提交
    submitSubClass: function () {
      this.$refs.subClassDataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.subClassDataForm);
            if (this.isSubClassAddOperation) {
              addExceptionSubClass(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.subClassDialogVisible = false;
                  this.$refs["subClassDataForm"].resetFields();

                  this.findExceptionTypeDetail(this.currentSelectExceptionTypeMainRowId);
                } else {
                  this.$message({
                    message:
                        "操作失败 " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
              });
            } else {
              updateExceptionSubClass(params).then((res) => {
                const responseData = res.data;
                this.editLoading = false;
                if (responseData.code === "000000") {
                  this.$message({message: "操作成功", type: "success"});
                  this.subClassDialogVisible = false;
                  this.$refs["subClassDataForm"].resetFields();

                  this.findExceptionTypeDetail(this.currentSelectExceptionTypeMainRowId);
                } else {
                  this.$message({
                    message:
                        "操作失败, " + getResponseDataMessage(responseData),
                    type: "error",
                  });
                }
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
    cancelSubClass() {
      this.subClassDialogVisible = false;
    },


    // 时间格式化
    dateTimeFormat: function (row, column) {
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
    dateFormat: function (dateValue) {
      return this.$moment(dateValue).format('YYYY-MM-DD')
    },
    timeFormat: function (dateValue) {
      return this.$moment(dateValue).format("HH:mm:ss");
    },
  },
};
</script>
