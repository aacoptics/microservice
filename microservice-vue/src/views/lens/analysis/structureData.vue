<template>
    <div>
      <div class="aac-container">
        <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form :inline="true" :size="size" :model="filters">
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
            </el-row>
          </el-form>
          <el-form :inline="true" :size="size">
            <el-form-item>
              <el-button :loading="exportLoading" type="primary" @click="exportExcelTemplate('结构数据模板')">导出模板
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
                <el-button :loading="exportDataLoading" type="success" @click="exportData('结构数据表')">导出数据
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
                        <el-form-item label="材料" prop="material">
                            <el-input v-model="dataForm.material" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="光学芯厚(um)" prop="coreThicknessLens">
                            <el-input v-model="dataForm.coreThicknessLens" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="最厚壁厚(um)" prop="maxWallThickness">
                            <el-input v-model="dataForm.maxWallThickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="最薄壁厚(um)" prop="minWallThickness">
                            <el-input v-model="dataForm.minWallThickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="最厚/芯厚" prop="maxCoreRatio">
                            <el-input v-model="dataForm.maxCoreRatio" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="光学厚薄比" prop="maxMinRatio">
                            <el-input v-model="dataForm.maxMinRatio" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="整体外径(um)" prop="outerDiameter">
                            <el-input v-model="dataForm.outerDiameter" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="整体边厚(um)" prop="edgeThickness">
                            <el-input v-model="dataForm.edgeThickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="整体最薄壁厚(um)" prop="wholeMinWallThickness">
                            <el-input v-model="dataForm.wholeMinWallThickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="整体最厚壁厚(um)" prop="wholeMaxWallThickness">
                            <el-input v-model="dataForm.wholeMaxWallThickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="整体厚薄比" prop="wholeMaxMinRatio">
                            <el-input v-model="dataForm.wholeMaxMinRatio" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="整体径厚比" prop="wholeDiameterThicknessRatio">
                            <el-input v-model="dataForm.wholeDiameterThicknessRatio" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="最大角度R1" prop="maxAngleR1">
                            <el-input v-model="dataForm.maxAngleR1" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="最大角度R2" prop="maxAngleR2">
                            <el-input v-model="dataForm.maxAngleR2" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="R1终端到R2坎合根部距离(um)" prop="r1R2Distance">
                            <el-input v-model="dataForm.r1R2Distance" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="机构中间部分厚度(um)" prop="middlePartThickness">
                            <el-input v-model="dataForm.middlePartThickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="坎合底部到外径距离(um)" prop="bottomDiameterDistance">
                            <el-input v-model="dataForm.bottomDiameterDistance" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="机构径厚比" prop="mechanismDiameterThicknessRatio">
                            <el-input v-model="dataForm.mechanismDiameterThicknessRatio" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="R1坎合角度" prop="r1KanheAngle">
                            <el-input v-model="dataForm.r1KanheAngle" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="R1坎合高度" prop="r1KanheHeight">
                            <el-input v-model="dataForm.r1KanheHeight" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="R2坎合角度" prop="r2KanheAngle">
                            <el-input v-model="dataForm.r2KanheAngle" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="R2坎合高度" prop="r2KanheHeight">
                            <el-input v-model="dataForm.r2KanheHeight" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="R1消光位置(粗糙度SRTM)" prop="r1Srtm">
                            <el-input v-model="dataForm.r1Srtm" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="R2消光位置(粗糙度SRTM)" prop="r2Srtm">
                            <el-input v-model="dataForm.r2Srtm" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="外径消光位置(粗糙度SRTM)" prop="outerDiameterSrtm">
                            <el-input v-model="dataForm.outerDiameterSrtm" auto-complete="off" clearable type="textarea"></el-input>
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
  } from "@/api/lens/analysis/structureData";
  import {ElMessageBox} from "element-plus";

  export default {
    name: "structureData",
    components: {SysTable},
    data() {
      return {
        size: "default",
        filters: {
          category: "",
          project: "",
          partName: "",
          material: ""
        },
        columns: [
            {type: "index", label: "序号", minWidth: 50},
            {prop: "category", label: "类别", minWidth: 100},
            {prop: "project", label: "项目", minWidth: 100},
            {prop: "partName", label: "零件名称", minWidth: 100},
            {prop: "material", label: "材料", minWidth: 100},
            {prop: "coreThicknessLens", label: "光学芯厚(um)", minWidth: 100},
            {prop: "maxWallThickness", label: "最厚壁厚(um)", minWidth: 100},
            {prop: "minWallThickness", label: "最薄壁厚(um)", minWidth: 100},
            {prop: "maxCoreRatio", label: "最厚/芯厚", minWidth: 100},
            {prop: "maxMinRatio", label: "光学厚薄比", minWidth: 100},
            {prop: "outerDiameter", label: "整体外径(um)", minWidth: 100},
            {prop: "edgeThickness", label: "整体边厚(um)", minWidth: 100},
            {prop: "wholeMinWallThickness", label: "整体最薄壁厚(um)", minWidth: 100},
            {prop: "wholeMaxWallThickness", label: "整体最厚壁厚(um)", minWidth: 100},
            {prop: "wholeMaxMinRatio", label: "整体厚薄比", minWidth: 100},
            {prop: "wholeDiameterThicknessRatio", label: "整体径厚比", minWidth: 100},
            {prop: "maxAngleR1", label: "最大角度R1(°)", minWidth: 100},
            {prop: "maxAngleR2", label: "最大角度R2(°)", minWidth: 100},
            {prop: "r1MaxHeightDifference", label: "R1最大高度差(um)", minWidth: 100},
            {prop: "r2MaxHeightDifference", label: "R2最大高度差(um)", minWidth: 100},
            {prop: "r1R2Distance", label: "R1终端到R2坎合根部距离(um)", minWidth: 100},
            {prop: "middlePartThickness", label: "机构中间部分厚度(um)", minWidth: 100},
            {prop: "bottomDiameterDistance", label: "坎合底部到外径距离(um)", minWidth: 100},
            {prop: "mechanismDiameterThicknessRatio", label: "机构径厚比", minWidth: 100},
            {prop: "r1KanheAngle", label: "R1坎合角度(°)", minWidth: 100},
            {prop: "r1KanheHeight", label: "R1坎合高度(um)", minWidth: 100},
            {prop: "r2KanheAngle", label: "R2坎合角度(°)", minWidth: 100},
            {prop: "r2KanheHeight", label: "R2坎合高度(um)", minWidth: 100},
            {prop: "r1Srtm", label: "R1消光位置(粗糙度SRTM)", minWidth: 100},
            {prop: "r2Srtm", label: "R2消光位置(粗糙度SRTM)", minWidth: 100},
            {prop: "outerDiameterSrtm", label: "外径消光位置(粗糙度SRTM)", minWidth: 100},
            {prop: "assemblyDrawing", label: "组立图", minWidth: 100}
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
          coreThicknessLens: "",
          maxWallThickness: "",
          minWallThickness: "",
          maxCoreRatio: "",
          maxMinRatio: "",
          outerDiameter: "",
          edgeThickness: "",
          wholeMinWallThickness: "",
          wholeMaxWallThickness: "",
          wholeMaxMinRatio: "",
          wholeDiameterThicknessRatio: "",
          maxAngleR1: "",
          maxAngleR2: "",
          r1MaxHeightDifference: "",
          r2MaxHeightDifference: "",
          r1R2Distance: "",
          middlePartThickness: "",
          bottomDiameterDistance: "",
          mechanismDiameterThicknessRatio: "",
          r1KanheAngle: "",
          r1KanheHeight: "",
          r2KanheAngle: "",
          r2KanheHeight: "",
          r1Srtm: "",
          r2Srtm: "",
          outerDiameterSrtm: "",
          assemblyDrawing: "",
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
