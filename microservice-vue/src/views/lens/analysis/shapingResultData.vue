<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :model="filters" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="类别" prop="category">
                <el-input v-model="filters.category" clearable placeholder="类别"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="项目" prop="project">
                <el-input v-model="filters.project" clearable placeholder="项目"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="零件名称" prop="partName">
                <el-input v-model="filters.partName" clearable placeholder="零件名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="材料" prop="material">
                <el-input v-model="filters.material" clearable placeholder="材料"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="模具名称" prop="moldNo">
                <el-input v-model="filters.moldNo" clearable placeholder="模具名称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="exportLoading" type="primary" @click="exportExcelTemplate('成型结果数据模板')">导出模板
              <template #icon>
                <font-awesome-icon :icon="['fas', 'download']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-row align="middle" class="float-right" justify="center">
            <el-form-item>
              <el-button size="small" type="info" @click="handleExcelUpload">导入
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'upload']"/>
                </template>
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button :loading="exportDataLoading" type="success" @click="exportData('成型结果数据表')">导出数据
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'download']"/>
                </template>
              </el-button>
            </el-form-item>
          </el-row>
        </el-form>
      </div>

      <SysTable id="condDataTable" ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :show-operation="false" :showBatchDelete="false"
                :stripe="false" @findPage="findPage">
        <template v-slot:custom-column>
          <el-table-column align="center" fixed="right" header-align="center" label="操作"
                           width="120">
            <template v-slot="scope">
              <el-button-group>
                <el-tooltip content="编辑" placement="top">
                  <el-button size="small" type="primary" @click="handleEdit({index: scope.index, row: scope.row})">
                    <template #icon>
                      <font-awesome-icon :icon="['far', 'pen-to-square']"/>
                    </template>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <el-button size="small" type="danger" @click="handleDelete(scope.row)">
                    <template #icon>
                      <font-awesome-icon :icon="['far', 'trash-can']"/>
                    </template>
                  </el-button>
                </el-tooltip>
              </el-button-group>
            </template>
          </el-table-column>
        </template>
      </SysTable>

      <el-dialog v-model="excelUploadDialogVisible" :close-on-click-modal="false" :title="'Excel导入'" width="30%">
        <el-upload
            :before-upload="beforeUpload"
            :http-request="submitExcelUpload"
            :multiple="false"
            :show-file-list="false"
            accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            action=""
            class="upload-demo"
            drag>
          <font-awesome-icon :icon="['fas', 'cloud-arrow-up']" size="6x"/>
          <div class="el-upload__text">将Excel文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
        </div>
      </el-dialog>

      <el-dialog v-model="editDialogVisible" :close-on-click-modal="false" :title="编辑" width="90%">
        <el-form ref="dataForm" :model="dataForm" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="8">
              <el-form-item label="类别" prop="category">
                <el-input v-model="dataForm.category" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目" prop="project">
                <el-input v-model="dataForm.project" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="零件名称" prop="partName">
                <el-input v-model="dataForm.partName" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="模具序号" prop="moldNo">
                <el-input v-model="dataForm.moldNo" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="材料" prop="material">
                <el-input v-model="dataForm.material" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="芯厚" prop="coreThickness">
                <el-input v-model="dataForm.coreThickness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="芯厚极差" prop="coreThicknessRange">
                <el-input v-model="dataForm.coreThicknessRange" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1矢高" prop="r1VectorHeight">
                <el-input v-model="dataForm.r1VectorHeight" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1矢高极差" prop="r1VectorHeightRange">
                <el-input v-model="dataForm.r1VectorHeightRange" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="R2矢高" prop="r2VectorHeight">
                <el-input v-model="dataForm.r2VectorHeight" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2矢高极差" prop="r2VectorHeightRange">
                <el-input v-model="dataForm.r2VectorHeightRange" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外径偏心" prop="outerDiameterEcc">
                <el-input v-model="dataForm.outerDiameterEcc" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="坎合偏心" prop="kanheEcc">
                <el-input v-model="dataForm.kanheEcc" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="面间偏心" prop="faceEcc">
                <el-input v-model="dataForm.faceEcc" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="退火制程" prop="annealingProcess">
                <el-input v-model="dataForm.annealingProcess" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="坎合圆度" prop="kanheRoundness">
                <el-input v-model="dataForm.kanheRoundness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外径均值" prop="outerDiameterAverage">
                <el-input v-model="dataForm.outerDiameterAverage" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外径极差" prop="outerDiameterRange">
                <el-input v-model="dataForm.outerDiameterRange" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="外径圆度" prop="outerDiameterRoundness">
                <el-input v-model="dataForm.outerDiameterRoundness" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外径收缩率" prop="outerDiameterShrinkage">
                <el-input v-model="dataForm.outerDiameterShrinkage" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外径粗糙度" prop="outerDiameterRoughness">
                <el-input v-model="dataForm.outerDiameterRoughness" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="R1平面度" prop="r1Flatness">
                <el-input v-model="dataForm.r1Flatness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2平面度" prop="r2Flatness">
                <el-input v-model="dataForm.r2Flatness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1分割位均值" prop="r1SplitAverage">
                <el-input v-model="dataForm.r1SplitAverage" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="R2分割位均值" prop="r2SplitAverage">
                <el-input v-model="dataForm.r2SplitAverage" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="5+3+3稳定性" prop="wftStability">
                <el-input v-model="dataForm.wftStability" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="全穴一致性(白片面型)" prop="wftConsistency">
                <el-input v-model="dataForm.wftConsistency" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="最大AS(白片面型)" prop="wftMaxAs">
                <el-input v-model="dataForm.wftMaxAs" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外径收缩率(白片面型)" prop="wftOuterDiameterShrinkage">
                <el-input v-model="dataForm.wftOuterDiameterShrinkage" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1(镀膜片面型)" prop="cftR1">
                <el-input v-model="dataForm.cftR1" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="R2(镀膜片面型)" prop="cftR2">
                <el-input v-model="dataForm.cftR2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="全穴一致性(镀膜片面型)" prop="cftConsistency">
                <el-input v-model="dataForm.cftConsistency" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="最大AS(镀膜片面型)" prop="cftMaxAs">
                <el-input v-model="dataForm.cftMaxAs" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="镀膜趋势" prop="coatingTrend">
                <el-input v-model="dataForm.coatingTrend" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1镀膜片模拟结果" prop="cfsrR1">
                <el-input v-model="dataForm.cfsrR1" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2镀膜片模拟结果" prop="cfsrR2">
                <el-input v-model="dataForm.cfsrR2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="R1&R2镀膜片模拟结果" prop="cfsrR1R2">
                <el-input v-model="dataForm.cfsrR1R2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="毛边" prop="burr">
                <el-input v-model="dataForm.burr" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="熔接线" prop="weldline">
                <el-input v-model="dataForm.weldline" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="外观问题" prop="appearanceProblem">
                <el-input v-model="dataForm.appearanceProblem" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="备注" prop="remarks">
                <el-input v-model="dataForm.remarks" auto-complete="off" clearable type="textarea"></el-input>
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
import {getResponseDataMessage} from "@/utils/commonUtils";
import SysTable from "@/components/SysTable";
import {
  exportExcel,
  exportExcelTemplate,
  getDataByConditions,
  handleDelete,
  handleUpdate,
  uploadExcel
} from "@/api/lens/analysis/shapingResultData";
import {ElMessageBox} from "element-plus";

export default {
  name: "shapingResultData",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        category: "",
        project: "",
        partName: "",
        material: "",
        moldNo: ""
      },
      columns: [
        {type: "index", label: "序号", minWidth: 50},
        {prop: "category", label: "类别", minWidth: 100},
        {prop: "project", label: "项目", minWidth: 100},
        {prop: "partName", label: "零件名称", minWidth: 100},
        {prop: "material", label: "材料", minWidth: 100},
        {prop: "moldNo", label: "模具序号", minWidth: 100},
        {prop: "coreThickness", label: "芯厚", minWidth: 100},
        {prop: "coreThicknessRange", label: "芯厚极差", minWidth: 100},
        {prop: "r1VectorHeight", label: "R1矢高", minWidth: 100},
        {prop: "r1VectorHeightRange", label: "R1矢高极差", minWidth: 100},
        {prop: "r2VectorHeight", label: "R2矢高", minWidth: 100},
        {prop: "r2VectorHeightRange", label: "R2矢高极差", minWidth: 100},
        {prop: "outerDiameterEcc", label: "外径偏心", minWidth: 100},
        {prop: "kanheEcc", label: "坎合偏心", minWidth: 100},
        {prop: "faceEcc", label: "面间偏心", minWidth: 100},
        {prop: "annealingProcess", label: "退火制程", minWidth: 100},
        {prop: "kanheRoundness", label: "坎合圆度", minWidth: 100},
        {prop: "outerDiameterAverage", label: "外径均值", minWidth: 100},
        {prop: "outerDiameterRange", label: "外径极差", minWidth: 100},
        {prop: "outerDiameterRoundness", label: "外径圆度", minWidth: 100},
        {prop: "outerDiameterShrinkage", label: "外径收缩率", minWidth: 100},
        {prop: "outerDiameterRoughness", label: "外径粗糙度", minWidth: 100},
        {prop: "r1Flatness", label: "R1平面度", minWidth: 100},
        {prop: "r2Flatness", label: "R2平面度", minWidth: 100},
        {prop: "r1SplitAverage", label: "R1分割位均值", minWidth: 100},
        {prop: "r2SplitAverage", label: "R2分割位均值", minWidth: 100},
        {prop: "wftStability", label: "5+3+3稳定性", minWidth: 100},
        {prop: "wftConsistency", label: "全穴一致性(白片面型)", minWidth: 100},
        {prop: "wftMaxAs", label: "最大AS(白片面型)", minWidth: 100},
        {prop: "wftOuterDiameterShrinkage", label: "外径收缩率(白片面型)", minWidth: 100},
        {prop: "cftR1", label: "R1(镀膜片面型)", minWidth: 100},
        {prop: "cftR2", label: "R2(镀膜片面型)", minWidth: 100},
        {prop: "cftConsistency", label: "全穴一致性(镀膜片面型)", minWidth: 100},
        {prop: "cftMaxAs", label: "最大AS(镀膜片面型)", minWidth: 100},
        {prop: "coatingTrend", label: "镀膜趋势", minWidth: 100},
        {prop: "cfsrR1", label: "R1镀膜片模拟结果", minWidth: 100},
        {prop: "cfsrR2", label: "R2镀膜片模拟结果", minWidth: 100},
        {prop: "cfsrR1R2", label: "R1&R2镀膜片模拟结果", minWidth: 100},
        {prop: "burr", label: "毛边", minWidth: 100},
        {prop: "weldline", label: "熔接线", minWidth: 100},
        {prop: "appearanceProblem", label: "外观问题", minWidth: 100},
        {prop: "remarks", label: "备注", minWidth: 100}
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      editDialogVisible: false, // 编辑界面是否显示
      editLoading: false,
      excelUploadDialogVisible: false,
      exportLoading: false,
      exportDataLoading: false,
      // 编辑界面数据
      dataForm: {
        id: "0",
        category: "",
        project: "",
        partName: "",
        material: "",
        moldNo: "",
        coreThickness: "",
        coreThicknessRange: "",
        r1VectorHeight: "",
        r1VectorHeightRange: "",
        r2VectorHeight: "",
        r2VectorHeightRange: "",
        outerDiameterEcc: "",
        kanheEcc: "",
        faceEcc: "",
        annealingProcess: "",
        kanheRoundness: "",
        outerDiameterAverage: "",
        outerDiameterRange: "",
        outerDiameterRoundness: "",
        outerDiameterShrinkage: "",
        outerDiameterRoughness: "",
        r1Flatness: "",
        r2Flatness: "",
        r1SplitAverage: "",
        r2SplitAverage: "",
        wftStability: "",
        wftConsistency: "",
        wftMaxAs: "",
        wftOuterDiameterShrinkage: "",
        cftR1: "",
        cftR2: "",
        cftConsistency: "",
        cftMaxAs: "",
        coatingTrend: "",
        cfsrR1: "",
        cfsrR2: "",
        cfsrR1R2: "",
        burr: "",
        weldline: "",
        appearanceProblem: "",
        appearanceImg: "",
        remarks: "",
        createdBy: "",
        updatedBy: "",
        createdTime: "",
        updatedTime: ""
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
      this.pageRequest.category = this.filters.category;
      this.pageRequest.project = this.filters.project;
      this.pageRequest.partName = this.filters.partName;
      this.pageRequest.material = this.filters.material;
      this.pageRequest.moldNo = this.filters.moldNo;
      getDataByConditions(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              this.pageResult = responseData.data;
            } else {
              this.$message({message: getResponseDataMessage(responseData), type: "error"});
            }
          })
          .then(data != null ? data.callback : "");
    },

    // 显示编辑界面
    handleEdit: function (params) {
      this.editDialogVisible = true;
      this.dataForm = Object.assign({}, params.row);
    },

    // 编辑提交
    submitForm: function () {
      this.$confirm("确认提交吗？", "提示", {}).then(() => {
        this.editLoading = true;
        let params = Object.assign({}, this.dataForm);
        handleUpdate(params).then((res) => {
          const responseData = res.data;
          this.editLoading = false;
          if (responseData.code === "000000") {
            this.$message({message: "操作成功", type: "success"});
            this.editDialogVisible = false;
            this.$refs["dataForm"].resetFields();
          } else {
            this.$message({message: "操作失败" + getResponseDataMessage(responseData), type: "error",});
          }
          this.findPage(null);
        })
      })
    },

    // 打开excel导入对话框
    handleExcelUpload: function () {
      this.excelUploadDialogVisible = true
    },
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel'
      if (!isExcel) {
        this.$message.error('只能上传xlsx, xls格式的文件！')
        return false
      }
    },
    submitExcelUpload(params) {
      uploadExcel(params).then((response) => {
        const responseData = response.data
        if (responseData.code === '000000') {
          ElMessageBox.alert('上传成功！' + getResponseDataMessage(responseData, '</br>'), '上传信息', {
            dangerouslyUseHTMLString: true
          })
        } else {
          ElMessageBox.alert('上传失败！' + getResponseDataMessage(responseData, '</br>'), '上传信息', {
            dangerouslyUseHTMLString: true
          })
        }
        this.findPage(null);
        this.excelUploadDialogVisible = false;
      }).catch((err) => {
        ElMessageBox.alert(err, '上传信息', {
          dangerouslyUseHTMLString: true,
          type: 'error'
        })
        this.excelUploadDialogVisible = false;
      })
    },

    // 导出模板
    exportExcelTemplate(excelFileName) {
      this.exportLoading = true;
      exportExcelTemplate().then(res => {
        this.exportLoading = false;
        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', excelFileName + ".xlsx");
        document.body.appendChild(link);
        link.click();
      })
    },

    // 导出查询数据
    exportData(excelFileName) {
      this.pageRequest.category = this.filters.category;
      this.pageRequest.project = this.filters.project;
      this.pageRequest.partName = this.filters.partName;
      this.pageRequest.material = this.filters.material;
      this.pageRequest.moldNo = this.filters.moldNo;

      this.exportDataLoading = true;
      exportExcel(this.pageRequest).then(res => {
        this.exportDataLoading = false;
        let url = window.URL.createObjectURL(new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', excelFileName + "-" + new Date().getTime() + ".xlsx");
        document.body.appendChild(link);
        link.click();
      });
    },

    // 取消
    cancel() {
      this.dialogVisible = false;
    },

    // 批量删除
    handleDelete: function (data) {
      this.$confirm('确认删除选中记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        handleDelete(data).then(res => {
          const responseData = res.data
          if (responseData.code === '000000') {
            this.findPage(null);
            this.$message({message: '删除成功', type: 'success'})
          } else {
            this.$message({
              message: `操作失败${getResponseDataMessage(responseData)}`,
              type: 'error'
            })
          }
        });
      }).catch((err) => {
        console.log(err)
      })
    }
  }
};
</script>
