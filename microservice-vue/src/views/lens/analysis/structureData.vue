<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :model="filters" :size="size">
          <el-row>
            <el-col :span="5">
              <el-form-item label="类别" prop="category">
                <el-select v-model.trim="filters.category" clearable placeholder="" style="width: 180px;">
                  <el-option v-for="item in categoryList" :key="item.category" :label="item.category"
                             :value="item.category"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="项目" prop="project">
                <el-select v-model.trim="filters.project" clearable placeholder="" style="width: 180px;">
                  <el-option v-for="item in projectList" :key="item.project" :label="item.project"
                             :value="item.project"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="零件名称" prop="partName">
                <el-select v-model.trim="filters.partName" clearable placeholder="" style="width: 180px;">
                  <el-option v-for="item in partNameList" :key="item.partName" :label="item.partName"
                             :value="item.partName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="材料" prop="material">
                <el-select v-model.trim="filters.material" clearable placeholder="" style="width: 180px;">
                  <el-option v-for="item in materialList" :key="item.material" :label="item.material"
                             :value="item.material"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="5">
              <el-form-item label="搜索类型" prop="searchType">
                <el-select v-model.trim="filters.searchType" clearable placeholder="请选择" style="width: 180px;">
                  <el-option v-for="item in options" :key="item.value" :label="item.label"
                             :value="item.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="起始值" prop="startValue">
                <el-input v-model="filters.startValue" clearable placeholder="起始值" style="width: 180px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="结束值" prop="endValue">
                <el-input v-model="filters.endValue" clearable placeholder="结束值" style="width: 180px;"></el-input>
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
          <el-table-column align="center" header-align="center" label="组立图" prop="assemblyDrawing" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.assemblyDrawing)">预览
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
              <el-form-item label="光学芯厚(um)" prop="coreThicknessLens">
                <el-input v-model="dataForm.coreThicknessLens" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="光学最厚壁厚(um)" prop="maxWallThickness">
                <el-input v-model="dataForm.maxWallThickness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="光学最薄壁厚(um)" prop="minWallThickness">
                <el-input v-model="dataForm.minWallThickness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="光学最厚/芯厚" prop="maxCoreRatio">
                <el-input v-model="dataForm.maxCoreRatio" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="光学厚薄比" prop="maxMinRatio">
                <el-input v-model="dataForm.maxMinRatio" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="光学最大角度R1(°)" prop="opticsMaxAngleR1">
                <el-input v-model="dataForm.opticsMaxAngleR1" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="光学最大角度R2(°)" prop="opticsMaxAngleR2">
                <el-input v-model="dataForm.opticsMaxAngleR2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
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
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="整体最薄壁厚(um)" prop="wholeMinWallThickness">
                <el-input v-model="dataForm.wholeMinWallThickness" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="整体最厚壁厚(um)" prop="wholeMaxWallThickness">
                <el-input v-model="dataForm.wholeMaxWallThickness" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="整体厚薄比" prop="wholeMaxMinRatio">
                <el-input v-model="dataForm.wholeMaxMinRatio" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="整体径厚比" prop="wholeDiameterThicknessRatio">
                <el-input v-model="dataForm.wholeDiameterThicknessRatio" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="整体最大角度R1(°)" prop="maxAngleR1">
                <el-input v-model="dataForm.maxAngleR1" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="整体最大角度R2(°)" prop="maxAngleR2">
                <el-input v-model="dataForm.maxAngleR2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="整体R1最大高度差(um)" prop="r1MaxHeightDifference">
                <el-input v-model="dataForm.r1MaxHeightDifference" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="整体R2最大高度差(um)" prop="r2MaxHeightDifference">
                <el-input v-model="dataForm.r2MaxHeightDifference" auto-complete="off" clearable
                          type="textarea"></el-input>
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
                <el-input v-model="dataForm.middlePartThickness" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="坎合底部到外径距离(um)" prop="bottomDiameterDistance">
                <el-input v-model="dataForm.bottomDiameterDistance" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="机构径厚比" prop="mechanismDiameterThicknessRatio">
                <el-input v-model="dataForm.mechanismDiameterThicknessRatio" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="R1坎合角度(°)" prop="r1KanheAngle">
                <el-input v-model="dataForm.r1KanheAngle" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1坎合高度(um)" prop="r1KanheHeight">
                <el-input v-model="dataForm.r1KanheHeight" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2坎合角度(°)" prop="r2KanheAngle">
                <el-input v-model="dataForm.r2KanheAngle" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="R2坎合高度(um)" prop="r2KanheHeight">
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
            <el-col :span="8">
              <el-form-item label="R1分割位位置" prop="r1SplitPosition">
                <el-input v-model="dataForm.r1SplitPosition" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2分割位位置" prop="r2SplitPosition">
                <el-input v-model="dataForm.r2SplitPosition" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="分型面上抬比例" prop="partSurfaceLiftRatio">
                <el-input v-model="dataForm.partSurfaceLiftRatio" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="机构逃肉" prop="mechanismTrou">
                <el-input v-model="dataForm.mechanismTrou" auto-complete="off" clearable type="textarea"></el-input>
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
} from "@/api/lens/analysis/structureData";
import {getStream} from "@/api/lens/analysis/allData";
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
        material: "",
        searchType: "",
        startValue: "",
        endValue: ""
      },
      columns: [
        {type: "index", label: "序号", minWidth: 50},
        {prop: "department", label: "事业部", minWidth: 100, sortable: false},
        {prop: "category", label: "类别", minWidth: 100, sortable: false},
        {prop: "lensNumber", label: "镜片数", minWidth: 100, sortable: false},
        {prop: "project", label: "项目", minWidth: 100, sortable: false},
        {prop: "partName", label: "零件名称", minWidth: 100, sortable: false},
        {prop: "material", label: "材料", minWidth: 100, sortable: false},
        {prop: "coreThicknessLens", label: "光学芯厚(um)", minWidth: 100},
        {prop: "maxWallThickness", label: "光学最厚壁厚(um)", minWidth: 100},
        {prop: "minWallThickness", label: "光学最薄壁厚(um)", minWidth: 100},
        {prop: "maxCoreRatio", label: "光学最厚/芯厚", minWidth: 100},
        {prop: "maxMinRatio", label: "光学厚薄比", minWidth: 100},
        {prop: "opticsMaxAngleR1", label: "最大光学角度R1", minWidth: 100},
        {prop: "opticsMaxAngleR2", label: "最大光学角度R2", minWidth: 100},
        {prop: "outerDiameter", label: "整体外径(um)", minWidth: 100},
        {prop: "edgeThickness", label: "整体边厚(um)", minWidth: 100},
        {prop: "wholeMinWallThickness", label: "整体最薄壁厚(um)", minWidth: 100},
        {prop: "wholeMaxWallThickness", label: "整体最厚壁厚(um)", minWidth: 100},
        {prop: "wholeMaxMinRatio", label: "整体厚薄比", minWidth: 100},
        {prop: "wholeDiameterThicknessRatio", label: "整体径厚比", minWidth: 100},
        {prop: "maxAngleR1", label: "整体最大角度R1(°)", minWidth: 100},
        {prop: "maxAngleR2", label: "整体最大角度R2(°)", minWidth: 100},
        {prop: "r1MaxHeightDifference", label: "整体R1最大高度差(um)", minWidth: 100},
        {prop: "r2MaxHeightDifference", label: "整体R2最大高度差(um)", minWidth: 100},
        {prop: "r1R2Distance", label: "R1终端到R2坎合根部距离(um)", minWidth: 100},
        {prop: "middlePartThickness", label: "机构中间部分厚度(um)", minWidth: 100},
        {prop: "bottomDiameterDistance", label: "坎合底部到外径距离(um)", minWidth: 100},
        {prop: "mechanismDiameterThicknessRatio", label: "机构径厚比", minWidth: 100},
        {prop: "r1KanheAngle", label: "R1坎合角度(°)", minWidth: 100},
        {prop: "r1KanheHeight", label: "R1坎合高度(um)", minWidth: 100},
        {prop: "r2KanheAngle", label: "R2坎合角度(°)", minWidth: 100},
        {prop: "r2KanheHeight", label: "R2坎合高度(um)", minWidth: 100},
        {prop: "r1Srtm", label: "R1消光位置(粗糙度SRTM)", minWidth: 100, sortable: false},
        {prop: "r2Srtm", label: "R2消光位置(粗糙度SRTM)", minWidth: 100, sortable: false},
        {prop: "r1SplitPosition", label: "R1分割位位置", minWidth: 100, sortable: false},
        {prop: "r2SplitPosition", label: "R2分割位位置", minWidth: 100, sortable: false},
        {prop: "outerDiameterSrtm", label: "外径消光位置(粗糙度SRTM)", minWidth: 100, sortable: false},
        {prop: "partSurfaceLiftRatio", label: "分型面上抬比例", minWidth: 100, sortable: false},
        {prop: "mechanismTrou", label: "机构逃肉", minWidth: 100, sortable: false}
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
        coreThicknessLens: "",
        maxWallThickness: "",
        minWallThickness: "",
        maxCoreRatio: "",
        maxMinRatio: "",
        opticsMaxAngleR1: "",
        opticsMaxAngleR2: "",
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
        r1SplitPosition: "",
        r2SplitPosition: "",
        outerDiameterSrtm: "",
        partSurfaceLiftRatio: "",
        mechanismTrou: "",
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
      options: [
        {value: "core_thickness_lens", label: "光学芯厚(um)"},
        {value: "max_wall_thickness", label: "最厚壁厚(um)"},
        {value: "min_wall_thickness", label: "最薄壁厚(um)"},
        {value: "max_core_ratio", label: "最厚/芯厚"},
        {value: "max_min_ratio", label: "光学厚薄比"},
        {value: "outer_diameter", label: "整体外径(um)"},
        {value: "edge_thickness", label: "整体边厚(um)"},
        {value: "whole_min_wall_thickness", label: "整体最薄壁厚(um)"},
        {value: "whole_max_wall_thickness", label: "整体最厚壁厚(um)"},
        {value: "whole_max_min_ratio", label: "整体厚薄比"},
        {value: "whole_diameter_thickness_ratio", label: "整体径厚比"},
        {value: "max_angle_r1", label: "最大角度R1(°)"},
        {value: "max_angle_r2", label: "最大角度R2(°)"},
        {value: "r1_max_height_difference", label: "R1最大高度差(um)"},
        {value: "r2_max_height_difference", label: "R2最大高度差(um)"},
        {value: "r1_r2_distance", label: "R1终端到R2坎合根部距离(um)"},
        {value: "middle_part_thickness", label: "机构中间部分厚度(um)"},
        {value: "bottom_diameter_distance", label: "坎合底部到外径距离(um)"},
        {value: "mechanism_diameter_thickness_ratio", label: "机构径厚比"},
        {value: "r1_kanhe_angle", label: "R1坎合角度(°)"},
        {value: "r1_kanhe_height", label: "R1坎合高度(um)"},
        {value: "r2_kanhe_angle", label: "R2坎合角度(°)"},
        {value: "r2_kanhe_height", label: "R2坎合高度(um)"},
        {value: "optics_max_angle_r1", label: "光学最大角度R1(°)"},
        {value: "optics_max_angle_r2", label: "光学最大角度R2(°)"}
      ],
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
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest;
      }
      this.pageRequest.category = this.filters.category;
      this.pageRequest.project = this.filters.project;
      this.pageRequest.partName = this.filters.partName;
      this.pageRequest.material = this.filters.material;

      this.pageRequest.searchType = this.filters.searchType;
      this.pageRequest.startValue = this.filters.startValue;
      this.pageRequest.endValue = this.filters.endValue;

      getDataByConditions(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              responseData.data.records.map((value) => {
                for (let key in value) {
                  if (key === 'coreThicknessLens' || key === 'maxWallThickness' || key === 'minWallThickness' ||
                      key === 'maxCoreRatio' || key === 'maxMinRatio' || key === 'outerDiameter' ||
                      key === 'edgeThickness' || key === 'wholeMinWallThickness' || key === 'wholeMaxWallThickness' ||
                      key === 'wholeMaxMinRatio' || key === 'wholeDiameterThicknessRatio' || key === 'maxAngleR1' ||
                      key === 'maxAngleR2' || key === 'r1MaxHeightDifference' || key === 'r2MaxHeightDifference' ||
                      key === 'r1R2Distance' || key === 'middlePartThickness' || key === 'bottomDiameterDistance' ||
                      key === 'mechanismDiameterThicknessRatio' || key === 'r1KanheAngle' || key === 'r1KanheHeight' ||
                      key === 'r2KanheAngle' || key === 'r2KanheHeight') {
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
      this.pageRequest.searchType = this.filters.searchType;
      this.pageRequest.startValue = this.filters.startValue;
      this.pageRequest.endValue = this.filters.endValue;

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
        filePathPrefix: "structureData",
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
