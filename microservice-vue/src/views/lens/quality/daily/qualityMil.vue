<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="MIL类型" prop="milType">
                <el-select v-model="filters.milType" clearable placeholder="MIL类型">
                  <el-option
                      v-for="item in qualityMilTypeOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="厂区" prop="site">
                <el-input v-model="filters.site" clearable placeholder="厂区"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="项目" prop="project">
                <el-input v-model="filters.project" clearable placeholder="项目"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="开始时间" prop="startEventHappenDate">
                <el-date-picker v-model="filters.startEventHappenDate" auto-complete="off" type="datetime"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="终止时间" prop="endEventHappenDate">
                <el-date-picker v-model="filters.endEventHappenDate" auto-complete="off" type="datetime"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="风险类别" prop="riskType">
                <el-input v-model="filters.riskType" clearable placeholder="风险类别"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="严重等级" prop="severityLevel">
                <el-select v-model="filters.severityLevel" allow-create clearable filterable placeholder="严重等级">
                  <el-option
                      v-for="item in qualityMilSeverityLevelOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="状态" prop="status">
                <el-select v-model="filters.status" allow-create clearable filterable placeholder="状态">
                  <el-option
                      v-for="item in qualityMilStatusOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="责任人" prop="responsibilities">
                <el-select v-model="filters.responsibilities" clearable filterable multiple placeholder="责任人">
                  <el-option
                      v-for="item in userOptions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button type="info" @click="handleOpenExcelUpload">Excel导入
              <template #icon>
                <font-awesome-icon :icon="['fas', 'upload']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button :loading="exportLoading" type="primary" @click="exportExcelData('质量MIL模板')">导出模板
              <template #icon>
                <font-awesome-icon :icon="['fas', 'download']"/>
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
          <el-form-item>
            <el-button type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-form-item class="float-right">
            <el-button :loading="exportReportLoading" type="success" @click="exportReportExcelData('Mil报表')">导出报表
              <template #icon>
                <font-awesome-icon :icon="['fas', 'download']"/>
              </template>
            </el-button>
          </el-form-item>
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
                <el-tooltip v-if="false" content="删除" placement="top">
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

      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="90%">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="100px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row v-if="operation">
            <el-col :span="24">
              <el-form-item label="milType" prop="milType">
                <el-select v-model="dataForm.milType" clearable placeholder="milType">
                  <el-option
                      v-for="item in qualityMilTypeAddOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目" prop="project">
                <el-input v-model="dataForm.project" :disabled="!operation" auto-complete="off" autosize clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item v-if="'' +dataForm.milType === '1' || '' +dataForm.milType === '2'" label="分类" prop="type">
                <el-input v-model="dataForm.type" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item v-if="'' +dataForm.milType === '4'" label="风险分项" prop="risk">
                <el-input v-model="dataForm.risk" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="风险类别" prop="riskType">
                <el-input v-model="dataForm.riskType" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="严重等级" prop="severityLevel">
                <el-select v-model="dataForm.severityLevel" allow-create clearable filterable placeholder="严重等级">
                  <el-option
                      v-for="item in qualityMilSeverityLevelOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="发生时间" prop="eventHappenDate">
                <el-date-picker v-model="dataForm.eventHappenDate" :disabled="!operation" auto-complete="off"
                                type="datetime">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item v-if="'' +dataForm.milType === '4'" label="客户" prop="customer">
                <el-input v-model="dataForm.customer" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="厂区" prop="site">
                <el-input v-model="dataForm.site" :disabled="!operation" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item v-if="'' +dataForm.milType === '1' || '' +dataForm.milType === '2'" label="工段"
                            prop="workshop">
                <el-input v-model="dataForm.workshop" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="站别" prop="station">
                <el-input v-model="dataForm.station" auto-complete="off" autosize clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="问题描述" prop="questionPresentation">
                <el-input v-model="dataForm.questionPresentation" :disabled="!operation" auto-complete="off" autosize
                          clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="原因分析" prop="reasonAnalysis">
                <el-input v-model="dataForm.reasonAnalysis" auto-complete="off" autosize clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item v-if="'' +dataForm.milType === '4'" label="对策&处理进展" prop="solutionProgress">
                <el-input v-model="dataForm.solutionProgress" :disabled="!operation" auto-complete="off" autosize
                          clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="标准化" prop="lessonLearn">
                <el-input v-model="dataForm.lessonLearn" auto-complete="off" autosize clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="负责人" prop="responsibilities">
                <el-select v-model="dataForm.responsibilities" clearable filterable multiple placeholder="责任人">
                  <el-option
                      v-for="item in userOptions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item v-if="dataForm.milType !== 4" label="生产经理" prop="productionManager">
                <el-select v-model="dataForm.productionManager" clearable filterable placeholder="生产经理">
                  <el-option
                      v-for="item in userOptions"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="状态" prop="status">
                <el-select v-model="dataForm.status" allow-create clearable filterable placeholder="状态">
                  <el-option
                      v-for="item in qualityMilStatusOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="计划完成时间" prop="planFinishDate">
                <el-date-picker v-model="dataForm.planFinishDate" auto-complete="off" type="datetime">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="实际完成时间" prop="actualFinishDate">
                <el-date-picker v-model="dataForm.actualFinishDate" auto-complete="off" type="datetime">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="处罚责任人" prop="punishmentPerson">
                <el-select v-model="dataForm.punishmentPerson" clearable filterable placeholder="处罚责任人">
                  <el-option
                      v-for="item in userOptions"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="处罚措施" prop="punishmentMeasures">
                <el-input v-model="dataForm.punishmentMeasures" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="是否再次发生" prop="happenTimes">
                <el-input-number v-model="dataForm.happenTimes" auto-complete="off" clearable></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="再次不良比例" prop="badAgainRate">
                <el-input v-model="dataForm.badAgainRate" auto-complete="off" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="备注" prop="remark">
                <el-input v-model="dataForm.remark" auto-complete="off" autosize clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item v-if="dataForm.milType === 4" label="每日状态更新" prop="statusSync">
                <el-input v-model="dataForm.statusSync" auto-complete="off" autosize clearable
                          type="textarea"></el-input>
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

      <el-dialog v-model="excelUploadDialogVisible" :close-on-click-modal="false" :title="'Excel导入'"
                 width="25%">
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
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {getDict, selectDictLabel} from "@/api/system/dictData";
import {date2str, getResponseDataMessage} from "@/utils/commonUtils";
import {
  exportExcel,
  findQualityMilPage,
  handleAdd,
  handleDelete,
  handleUpdate,
  listAllUser,
  listQualityMilExportExcel,
  uploadExcel
} from "@/api/lens/quality/qualityMil";

export default {
  name: "qualityMil",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        milType: "0",
        site: "",
        project: "",
        startEventHappenDate: "",
        endEventHappenDate: "",
        riskType: "",
        severityLevel: "",
        status: "",
        responsibilities: [],
      },
      columns: [
        {prop: "index", label: "序号", minWidth: 80},
        {prop: "milType", label: "MIL类型", minWidth: 100, formatter: this.milTypeFormat},
        {prop: "project", label: "项目", minWidth: 100},
        {prop: "type", label: "分类", minWidth: 100},
        {prop: "risk", label: "风险分项", minWidth: 100},
        {prop: "riskType", label: "风险类别", minWidth: 100},
        {prop: "severityLevel", label: "严重等级", minWidth: 100},
        {prop: "eventHappenDate", label: "发生时间", minWidth: 100, formatter: this.dateFormat},
        {prop: "customer", label: "客户", minWidth: 100},
        {prop: "site", label: "厂区", minWidth: 100},
        {prop: "workshop", label: "工段", minWidth: 100},
        {prop: "station", label: "站别", minWidth: 100},
        {prop: "questionPresentation", label: "问题描述", minWidth: 150},
        {prop: "reasonAnalysis", label: "原因分析", minWidth: 150},
        {prop: "solutionProgress", label: "对策&处理进展", minWidth: 150},
        {prop: "lessonLearn", label: "标准化", minWidth: 100},
        {prop: "responsibility", label: "负责人", minWidth: 100, formatter: this.userFormat},
        {prop: "productionManager", label: "生产经理", minWidth: 100},
        {prop: "status", label: "状态", minWidth: 100},
        {prop: "planFinishDate", label: "计划完成时间", minWidth: 150, formatter: this.dateTimeFormat},
        {prop: "actualFinishDate", label: "实际完成时间", minWidth: 150, formatter: this.dateTimeFormat},
        {prop: "punishmentPerson", label: "处罚责任人", minWidth: 150},
        {prop: "punishmentMeasures", label: "处罚措施", minWidth: 150},
        {prop: "happenTimes", label: "是否再次发生", minWidth: 150},
        {prop: "badAgainRate", label: "再次不良比例", minWidth: 150},
        {prop: "remark", label: "备注", minWidth: 150},
        {prop: "statusSync", label: "每日状态更新", minWidth: 150},
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
      excelUploadDialogVisible: false,
      exportLoading: false,
      exportReportLoading: false,
      dataFormRules: {
        project: [{required: true, message: '请输入项目', trigger: 'blur'}],
        eventHappenDate: [{required: true, message: '请输入发生时间', trigger: 'blur'}],
        customer: [{required: true, message: '请输入客户', trigger: 'blur'}],
        site: [{required: true, message: '请输入厂区', trigger: 'blur'}],
        questionPresentation: [{required: true, message: '请输入问题描述', trigger: 'blur'}],
        solutionProgress: [{required: true, message: '请输入对策&处理进展', trigger: 'blur'}],
        responsibilities: [{required: true, message: '请输入负责人', trigger: 'blur'}],
      },
      qualityMilTypeOptions: [],
      qualityMilTypeAddOptions: [],
      qualityMilSeverityLevelOptions: [],
      qualityMilStatusOptions: [],
      userOptions: [],
      // 新增编辑界面数据
      dataForm: {
        milType: "0",
        id: "0",
        project: "",
        type: "",
        risk: "",
        riskType: "",
        severityLevel: "",
        eventHappenDate: "",
        customer: "",
        site: "",
        workshop: "",
        station: "",
        questionPresentation: "",
        reasonAnalysis: "",
        solutionProgress: "",
        lessonLearn: "",
        responsibility: "",
        responsibilities: [],
        productionManager: "",
        status: "",
        planFinishDate: "",
        actualFinishDate: "",
        punishmentPerson: "",
        punishmentMeasures: "",
        happenTimes: "",
        badAgainRate: "",
        remark: "",
        statusSync: "",
      },
    };
  },
  mounted() {
    getDict("quality_mil_type").then(response => {
      this.qualityMilTypeOptions = response.data.data
    })
    getDict("quality_mil_type").then(response => {
      response.data.data.shift()
      this.qualityMilTypeAddOptions = response.data.data
    })
    getDict("quality_mil_severity_level").then(response => {
      this.qualityMilSeverityLevelOptions = response.data.data
    })
    getDict("quality_mil_status").then(response => {
      this.qualityMilStatusOptions = response.data.data
    })
    listAllUser().then(response => {
      this.userOptions = response.data.data
    })
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.milType = this.filters.milType;
      this.pageRequest.site = this.filters.site;
      this.pageRequest.project = this.filters.project;
      this.pageRequest.startEventHappenDate = this.filters.startEventHappenDate;
      this.pageRequest.endEventHappenDate = this.filters.endEventHappenDate;
      this.pageRequest.riskType = this.filters.riskType;
      this.pageRequest.severityLevel = this.filters.severityLevel;
      this.pageRequest.status = this.filters.status;
      this.pageRequest.responsibilities = this.filters.responsibilities;
      findQualityMilPage(this.pageRequest)
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
      this.$confirm('确认删除选中记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        handleDelete(data.milType, data.id).then(res => {
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
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true;
      this.operation = true;
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        milType: "1",
        id: "0",
        project: "",
        type: "",
        risk: "",
        riskType: "",
        severityLevel: "",
        eventHappenDate: date2str(new Date()) + "T00:00:00",
        customer: "-",
        site: "",
        workshop: "",
        station: "",
        questionPresentation: "",
        reasonAnalysis: "",
        solutionProgress: "-",
        lessonLearn: "",
        responsibility: "-",
        responsibilities: [],
        productionManager: "",
        status: "",
        planFinishDate: "",
        actualFinishDate: "",
        punishmentPerson: "",
        punishmentMeasures: "",
        happenTimes: "",
        badAgainRate: "",
        remark: "",
        statusSync: "",
      };
    },

    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true;
      this.operation = false;
      this.dataForm = Object.assign({}, params.row);
      this.dataForm.responsibilities = params.row.responsibility.split(',')
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
                  this.$message({message: "操作失败 " + getResponseDataMessage(responseData), type: "error",});
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
                  this.$message({message: "操作失败" + getResponseDataMessage(responseData), type: "error",});
                }
                this.findPage(null);
              })
            }
          })
        }
      })
    },
    handleOpenExcelUpload: function () {
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
          alert('上传成功！' + getResponseDataMessage(responseData, '\n'))
        } else {
          alert('上传失败！' + getResponseDataMessage(responseData, '\n'))
        }
        this.findPage(null);
        this.excelUploadDialogVisible = false;
      }).catch((err) => {
        alert(err)
        this.excelUploadDialogVisible = false;
      })
    },
    exportExcelData(excelFileName) {
      this.exportLoading = true;
      exportExcel().then(res => {
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
    exportReportExcelData(excelFileName) {
      this.pageRequest.milType = this.filters.milType;
      this.pageRequest.site = this.filters.site;
      this.pageRequest.project = this.filters.project;
      this.pageRequest.startEventHappenDate = this.filters.startEventHappenDate;
      this.pageRequest.endEventHappenDate = this.filters.endEventHappenDate;
      this.pageRequest.riskType = this.filters.riskType;
      this.pageRequest.severityLevel = this.filters.severityLevel;
      this.pageRequest.status = this.filters.status;
      this.pageRequest.responsibilities = this.filters.responsibilities;

      this.exportReportLoading = true;
      listQualityMilExportExcel(this.pageRequest).then(res => {
        this.exportReportLoading = false;
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
    // 时间格式化
    dateFormat: function (row, column) {
      if (row[column.property] == null) return '-';
      return this.$moment(row[column.property]).format("YYYY-MM-DD");
    },
    dateTimeFormat: function (row, column) {
      if (row[column.property] == null) return '-';
      return this.$moment(row[column.property]).format("YYYY-MM-DD HH:mm");
    },
    milTypeFormat: function (row) {
      return selectDictLabel(this.qualityMilTypeOptions, row.milType);
    },
    userFormat: function (row) {
      return row.responsibility.split(',').map(id => this.selectUserLabel(this.userOptions, id)).join(',');
    },
    selectUserLabel: function (datum, value) {
      let actions = [];
      Object.keys(datum).some((key) => {
        if (datum[key].id === ('' + value)) {
          actions.push(datum[key].name);
          return true;
        }
      })
      return actions.join('');
    }
  },
};
</script>
