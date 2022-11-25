<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :model="filters" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="事业部" prop="department">
                <el-select v-model.trim="filters.department" clearable placeholder="" style="width: 130px;">
                  <el-option v-for="item in departmentList" :key="item.department" :label="item.department"
                             :value="item.department"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="镜片数" prop="lensNumber">
                <el-select v-model.trim="filters.lensNumber" clearable placeholder="" style="width: 130px;">
                  <el-option v-for="item in lensNumberList" :key="item.lensNumber" :label="item.lensNumber"
                             :value="item.lensNumber"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="类别" prop="category">
                <el-select v-model.trim="filters.category" clearable placeholder="" style="width: 130px;">
                  <el-option v-for="item in categoryList" :key="item.category" :label="item.category"
                             :value="item.category"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="项目" prop="project">
                <el-select v-model.trim="filters.project" clearable placeholder="" style="width: 130px;">
                  <el-option v-for="item in projectList" :key="item.project" :label="item.project"
                             :value="item.project"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="零件名称" prop="partName">
                <el-select v-model.trim="filters.partName" clearable placeholder="" style="width: 130px;">
                  <el-option v-for="item in partNameList" :key="item.partName" :label="item.partName"
                             :value="item.partName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="材料" prop="material">
                <el-select v-model.trim="filters.material" clearable placeholder="" style="width: 130px;">
                  <el-option v-for="item in materialList" :key="item.material" :label="item.material"
                             :value="item.material"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="exportLoading" type="primary" @click="exportExcelTemplate('模流数据模板')">导出模板
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
              <el-button :loading="exportDataLoading" type="success" @click="exportData('模流数据表')">导出数据
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
          <el-table-column align="center" header-align="center" label="竞品组立图" prop="assemblyDrawing" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.assemblyDrawing)">预览
                <template #icon>
                  <font-awesome-icon icon="fa-solid fa-image"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" header-align="center" label="模拟面型图R1" prop="refractivePicR1" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.refractivePicR1)">预览
                <template #icon>
                  <font-awesome-icon icon="fa-solid fa-image"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" header-align="center" label="模拟面型图R2" prop="refractivePicR2" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.refractivePicR2)">预览
                <template #icon>
                  <font-awesome-icon icon="fa-solid fa-image"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
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
              <el-form-item label="事业部" prop="department">
                <el-input v-model="dataForm.department" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="类别" prop="category">
                <el-input v-model="dataForm.category" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="镜片数" prop="lensNumber">
                <el-input v-model="dataForm.lensNumber" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
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
            <el-col :span="8">
              <el-form-item label="材料" prop="material">
                <el-input v-model="dataForm.material" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="模具类型" prop="moldType">
                <el-input v-model="dataForm.moldType" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="流道类型" prop="runnerType">
                <el-input v-model="dataForm.runnerType" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="模流外径收缩率(‰)" prop="moldDiameterRate">
                <el-input v-model="dataForm.moldDiameterRate" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="流动前沿温度(℃)" prop="flowFrontTemperature">
                <el-input v-model="dataForm.flowFrontTemperature" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="VP切换压力(MPa)" prop="vpChangePressure">
                <el-input v-model="dataForm.vpChangePressure" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="模拟熔接线长度(mm)" prop="simulateWireLength">
                <el-input v-model="dataForm.simulateWireLength" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="整体平均体积收缩率差值(%)" prop="wholePercent">
                <el-input v-model="dataForm.wholePercent" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1有效径四点极差(%)" prop="effectiveR1">
                <el-input v-model="dataForm.effectiveR1" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2有效径四点极差(%)" prop="effectiveR2">
                <el-input v-model="dataForm.effectiveR2" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="R1坎合四点极差(%)" prop="ridgeR1">
                <el-input v-model="dataForm.ridgeR1" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2坎合四点极差(%)" prop="ridgeR2">
                <el-input v-model="dataForm.ridgeR2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="模拟面型R1(nm)" prop="refractiveR1">
                <el-input v-model="dataForm.refractiveR1" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="模拟面型R2(nm)" prop="refractiveR2">
                <el-input v-model="dataForm.refractiveR2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="竞品名称" prop="competitorName">
                <el-input v-model="dataForm.competitorName" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="竞品链接" prop="competitorLink">
                <el-input v-model="dataForm.competitorLink" auto-complete="off" clearable type="textarea"></el-input>
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
      <el-dialog v-model="picDialogVisible" :close-on-click-modal="false" :title="图片展示" width="340px"
                 @close="closeImg">
        <el-image
            style="width: 300px; height: 300px"
            :fit="fill"
            :src="codeImg"
            :preview-src-list="srcList">
        </el-image>
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
  getCategory,
  getDataByConditions,
  getMaterial,
  getPartName,
  getProject,
  handleDelete,
  handleUpdate,
  uploadExcel,
  getDepartment,
  getLensNumber
} from "@/api/lens/analysis/moldFlow";
import {getStream} from "@/api/lens/analysis/allData";
import {ElMessageBox} from "element-plus";

export default {
  name: "moldFlow",
  components: {SysTable},
  data() {
    return {
      size: "default",
      filters: {
        category: "",
        project: "",
        partName: "",
        material: "",
        department: "",
        lensNumber: ""
      },
      columns: [
        {type: "index", label: "序号", minWidth: 50},
        {prop: "department", label: "事业部", minWidth: 100, sortable: false},
        {prop: "category", label: "类别", minWidth: 100, sortable: false},
        {prop: "lensNumber", label: "镜片数", minWidth: 100, sortable: false},
        {prop: "project", label: "项目", minWidth: 100, sortable: false},
        {prop: "partName", label: "零件名称", minWidth: 100, sortable: false},
        {prop: "material", label: "材料", minWidth: 100, sortable: false},
        {prop: "moldType", label: "模具类型", minWidth: 100, sortable: false},
        {prop: "runnerType", label: "流道类型", minWidth: 100, sortable: false},
        {prop: "moldDiameterRate", label: "模流外径收缩率(‰)", minWidth: 100},
        {prop: "flowFrontTemperature", label: "流动前沿温度(℃)", minWidth: 100},
        {prop: "vpChangePressure", label: "VP切换压力(MPa)", minWidth: 100},
        {prop: "simulateWireLength", label: "模拟熔接线长度(mm)", minWidth: 100},
        {prop: "wholePercent", label: "整体平均体积收缩率差值(%)", minWidth: 100},
        {prop: "effectiveR1", label: "R1有效径四点极差(%)", minWidth: 100},
        {prop: "effectiveR2", label: "R2有效径四点极差(%)", minWidth: 100},
        {prop: "ridgeR1", label: "R1坎合四点极差(%)", minWidth: 100},
        {prop: "ridgeR2", label: "R2坎合四点极差(%)", minWidth: 100},
        {prop: "refractiveR1", label: "模拟面型R1(nm)", minWidth: 100},
        {prop: "refractiveR2", label: "模拟面型R2(nm)", minWidth: 100},
        {prop: "competitorName", label: "竞品名称", minWidth: 100, sortable: false},
        {prop: "competitorLink", label: "竞品链接", minWidth: 100, sortable: false}
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
        department: "",
        category: "",
        lensNumber: "",
        project: "",
        partName: "",
        material: "",
        moldType: "",
        runnerType: "",
        moldDiameterRate: "",
        flowFrontTemperature: "",
        vpChangePressure: "",
        simulateWireLength: "",
        wholePercent: "",
        effectiveR1: "",
        effectiveR2: "",
        ridgeR1: "",
        ridgeR2: "",
        refractiveR1: "",
        refractiveR2: "",
        competitorName: "",
        competitorLink: "",
        assemblyDrawing: "",
        createdBy: "",
        updatedBy: "",
        createdTime: "",
        updatedTime: ""
      },
      categoryList: [],
      projectList: [],
      partNameList: [],
      materialList: [],
      departmentList: [],
      lensNumberList: [],
      picDialogVisible: false,
      codeImg: "",
      imgWidth: "500px",
      dialogWidth: "600px",
      srcList: []
    };
  },
  mounted() {
  },
  created() {
    this.init()
  },
  methods: {
    async init() {
      this.categoryList = await this.getCategory();
      this.projectList = await this.getProject();
      this.partNameList = await this.getPartName();
      this.materialList = await this.getMaterial();
      this.departmentList = await this.getDepartment();
      this.lensNumberList = await this.getLensNumber();
    },
    getCategory() {
      return new Promise((resolve, reject) => {
        getCategory().then(res => {
          if (res.data.code !== "000000") {
            resolve([])
          }
          resolve(res.data.data)
        })
      })
    },
    getProject() {
      return new Promise((resolve, reject) => {
        getProject().then(res => {
          if (res.data.code !== "000000") {
            resolve([])
          }
          resolve(res.data.data)
        })
      })
    },
    getPartName() {
      return new Promise((resolve, reject) => {
        getPartName().then(res => {
          if (res.data.code !== "000000") {
            resolve([])
          }
          resolve(res.data.data)
        })
      })
    },
    getMaterial() {
      return new Promise((resolve, reject) => {
        getMaterial().then(res => {
          if (res.data.code !== "000000") {
            resolve([])
          }
          resolve(res.data.data)
        })
      })
    },
    getDepartment() {
      return new Promise((resolve, reject) => {
        getDepartment().then(res => {
          if (res.data.code !== "000000") {
            resolve([])
          }
          resolve(res.data.data)
        })
      })
    },
    getLensNumber() {
      return new Promise((resolve, reject) => {
        getLensNumber().then(res => {
          if (res.data.code !== "000000") {
            resolve([])
          }
          resolve(res.data.data)
        })
      })
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.category = this.filters.category;
      this.pageRequest.project = this.filters.project;
      this.pageRequest.partName = this.filters.partName;
      this.pageRequest.material = this.filters.material;
      this.pageRequest.department = this.filters.department;
      this.pageRequest.lensNumber = this.filters.lensNumber;

      getDataByConditions(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              responseData.data.records.map((value) => {
                for (let key in value) {
                  if (key === 'moldDiameterRate' || key === 'flowFrontTemperature' || key === 'vpChangePressure' ||
                      key === 'simulateWireLength' || key === 'wholePercent' || key === 'effectiveR1' ||
                      key === 'effectiveR2' || key === 'ridgeR1' || key === 'ridgeR2' ||
                      key === 'refractiveR1' || key === 'refractiveR2') {
                    //过滤不需要转换类型的值
                    //纯数字列排序需要转换为Number类型，否者经常出现升降排序混乱
                    value[key] = Number(value[key])
                  }
                }
              });
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
      this.pageRequest.department = this.filters.department;
      this.pageRequest.lensNumber = this.filters.lensNumber;

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
      this.editDialogVisible = false;
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
    },
    // 图片展示处理
    preview(fileName) {
      let params = {
        filePathPrefix: "moldFlowData",
        fileName: fileName
      }
      getStream(params).then(res => {
        console.log(res.data.size)
        if (res.data.size) {
          this.picDialogVisible = true
          const url = window.URL.createObjectURL(res.data)
          this.codeImg = url
          this.srcList.push(url)
        } else {
          ElMessageBox.alert('无相应图片', {
            dangerouslyUseHTMLString: true,
            type: 'error'
          })
        }
      }).catch((err) => {
        ElMessageBox.alert(err, '获取图片失败', {
          dangerouslyUseHTMLString: true,
          type: 'error'
        })
      });
    },

    // 调整图片高度
    onLoadImg(e) {
      var img = e.target;
      var width = 0;
      if (img.fileSize > 0 || (img.naturalWidth > 1 && img.naturalHeight > 1)) {
        width = img.naturalWidth;
      }
      this.imgWidth = width + 'px';
      this.dialogWidth = width + 30 + 'px';
    },
    // 关闭图片对话框
    closeImg() {
      this.codeImg = ""
      this.srcList = []
    }
  }
};
</script>
