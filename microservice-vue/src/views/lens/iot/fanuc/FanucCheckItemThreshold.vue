<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size" label-width="100px">
          <el-form-item label="机台号" prop="machineName">
            <el-input v-model="filters.machineName" clearable placeholder="机台号"></el-input>
          </el-form-item>
          <el-form-item label="项目号" prop="moldFileName">
            <el-input v-model="filters.moldFileName" clearable placeholder="项目号"></el-input>
          </el-form-item>
          <el-form-item label="点检项" prop="checkItemName">
            <el-input v-model="filters.checkItemName" clearable placeholder="点检项"></el-input>
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
                @findPage="findPage" @handleDelete="handleDelete"
                @handleEdit="handleEdit">
      </SysTable>
      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="isFanucCheckItemThresholdAddOperation?'新增':'编辑'"
                 destroy-on-close width="25%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="20">
              <el-form-item label="机台号" prop="machineName">
                <el-input v-model="dataForm.machineName" clearable placeholder="机台号"></el-input>
              </el-form-item>
              <el-form-item label="项目号" prop="moldFileName">
                <el-input v-model="dataForm.moldFileName" clearable placeholder="项目号"></el-input>
              </el-form-item>
              <el-form-item label="点检项" prop="checkItem">
                <el-select v-model="dataForm.checkItem" filterable placeholder="点检项"
                           style="width:100%">
                  <el-option
                      v-for="item in checkItemOptions"
                      :key="item.key"
                      :label="item.value"
                      :value="item.key"
                  >
                  </el-option>
                </el-select>

              </el-form-item>
              <el-form-item label="偏移量" prop="offsetValue" width="100%">
                <el-input-number v-model="dataForm.offsetValue" clearable placeholder="偏移量" ></el-input-number>
              </el-form-item>
            </el-col>
                      
          </el-row>

        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" style="margin-right: 0px;" type="info" @click="cancel">取消</el-button>
            <el-button :loading="editLoading" :size="size" style="margin-right: 20px;" type="primary"
                       @click="submitFanucCheckItemThreshold">提交
            </el-button>
          </slot>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {
  findFanucCheckItemThresholdPage,
  deleteFanucCheckItemThreshold,
  handleAdd,
  handleUpdate,
} from "@/api/lens/iot/fanucCheckItemThreshold";
import {getResponseDataMessage} from "@/utils/commonUtils";

export default {
  name: "fanucCheckItemThreshold",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        machineName: "",
        moldFileName: "",
        checkItemName: "",
      },
      columns: [
        {prop: "machineName", label: "机台号", minWidth: 150},
        {prop: "moldFileName", label: "项目号", minWidth: 150},
        // {prop: "checkItem", label: "点检项", minWidth: 150},
        {prop: "checkItemName", label: "点检项", minWidth: 150},
        {prop: "offsetValue", label: "偏移量", minWidth: 150},
        {prop: "updatedBy", label: "更新人", minWidth: 100},
        {prop: "updatedTime", label: "更新时间", minWidth: 120, formatter: this.dateTimeFormat},
        {prop: "createdBy", label: "创建人", minWidth: 120},
        {prop: "createdTime", label: "创建时间", minWidth: 120, formatter: this.dateTimeFormat},
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      findLoading:false,

      isFanucCheckItemThresholdAddOperation: false, // true:新增, false:编辑

      dialogVisible: false, // 新增编辑界面是否显示

      dataFormRules: {
        machineName: [{required: true, message: "请输入机台号", trigger: "blur"}],
        checkItem: [{required: true, message: "请选择点检项", trigger: "change"}],
        offsetValue: [{required: true, message: "请输入偏移量", trigger: "blur"}],
      },
      checkItemOptions: [
        {"key": "monit_nozzle", "value": "喷嘴1温度(℃)"},
        {"key": "monit_barrel1", "value": "料筒1温度(℃)"},
        {"key": "monit_barrel2", "value": "料筒2温度(℃)"},
        {"key": "monit_barrel3", "value": "料筒3温度(℃)"},
        {"key": "monit_feed_th", "value": "料斗下温度(℃)"},
      ],

      // 新增编辑界面数据
      dataForm: {
        id: 0,
        machineName: '',
        moldFileName: '',
        checkItem: '',
        checkItemName: '',
        offsetValue: null,
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
      this.pageRequest.machineName = this.filters.machineName;
      this.pageRequest.moldFileName = this.filters.moldFileName;
      this.pageRequest.checkItemName = this.filters.checkItemName;

      this.findLoading = true;
      findFanucCheckItemThresholdPage(this.pageRequest)
          .then((res) => {
            this.findLoading = false;
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
        deleteFanucCheckItemThreshold(data.params[0]).then(data.callback);
    },
   
      // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.isFanucCheckItemThresholdAddOperation = true;
      this.dataForm = {
        id: 0,
        machineName: '',
        moldFileName: '',
        checkItem: '',
        checkItemName: '',
        offsetValue: null,
      };
    },

 
    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.isFanucCheckItemThresholdAddOperation = false;
      this.dataForm = Object.assign({}, params.row);
    },

    
    // 编辑
    submitFanucCheckItemThreshold: function () {
      let checkItemObj = this.checkItemOptions.find((item)=> item.key === this.dataForm.checkItem);
      this.dataForm.checkItemName = checkItemObj.value;

      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm("确认提交吗？", "提示", {}).then(() => {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if (this.isFanucCheckItemThresholdAddOperation) {
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
    dateFormat: function (dateValue) {
      return this.$moment(dateValue).format('YYYY-MM-DD')
    },
    timeFormat: function (dateValue) {
      return this.$moment(dateValue).format("HH:mm:ss");
    },
  },
};
</script>
