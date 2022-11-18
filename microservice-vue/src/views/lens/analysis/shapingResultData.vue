<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :model="filters" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="类别" prop="category">
                <el-select v-model.trim="filters.category" clearable placeholder="" style="width: 150px;">
                  <el-option v-for="item in categoryList" :key="item.category" :label="item.category"
                             :value="item.category"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="项目" prop="project">
                <el-select v-model.trim="filters.project" clearable placeholder="" style="width: 150px;">
                  <el-option v-for="item in projectList" :key="item.project" :label="item.project"
                             :value="item.project"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="零件名称" prop="partName">
                <el-select v-model.trim="filters.partName" clearable placeholder="" style="width: 150px;">
                  <el-option v-for="item in partNameList" :key="item.partName" :label="item.partName"
                             :value="item.partName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="材料" prop="material">
                <el-select v-model.trim="filters.material" clearable placeholder="" style="width: 150px;">
                  <el-option v-for="item in materialList" :key="item.material" :label="item.material"
                             :value="item.material"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="模具序号" prop="moldNo">
                <el-select v-model.trim="filters.moldNo" clearable placeholder="" style="width: 150px;">
                  <el-option v-for="item in moldNoList" :key="item.moldNo" :label="item.moldNo"
                             :value="item.moldNo"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="5">
              <el-form-item label="搜索类型" prop="searchType">
                <el-select v-model.trim="filters.searchType" clearable placeholder="请选择" style="width: 150px;">
                  <el-option v-for="item in options" :key="item.value" :label="item.label"
                             :value="item.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="起始值" prop="startValue">
                <el-input v-model="filters.startValue" clearable placeholder="起始值"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="结束值" prop="endValue">
                <el-input v-model="filters.endValue" clearable placeholder="结束值"></el-input>
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
          <el-table-column align="center" header-align="center" label="外观问题图片" prop="appearanceImg" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.appearanceImg)">预览
                <template #icon>
                  <font-awesome-icon icon="fa-solid fa-image"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" header-align="center" label="镀膜趋势" prop="coatingTrend" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.coatingTrend)">预览
                <template #icon>
                  <font-awesome-icon icon="fa-solid fa-image"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" header-align="center" label="R1镀膜片模拟结果" prop="cfsrR1" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.cfsrR1)">预览
                <template #icon>
                  <font-awesome-icon icon="fa-solid fa-image"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" header-align="center" label="R2镀膜片模拟结果" prop="cfsrR2" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.cfsrR2)">预览
                <template #icon>
                  <font-awesome-icon icon="fa-solid fa-image"/>
                </template>
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" header-align="center" label="R1&R2镀膜片模拟结果" prop="cfsrR1R2" width="100">
            <template v-slot="imgScope">
              <el-button type="primary" @click="preview(imgScope.row.cfsrR1R2)">预览
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
              <el-form-item label="芯厚(um)" prop="coreThickness">
                <el-input v-model="dataForm.coreThickness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="芯厚极差(um)" prop="coreThicknessRange">
                <el-input v-model="dataForm.coreThicknessRange" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1矢高(um)" prop="r1VectorHeight">
                <el-input v-model="dataForm.r1VectorHeight" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1矢高极差(um)" prop="r1VectorHeightRange">
                <el-input v-model="dataForm.r1VectorHeightRange" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="R2矢高(um)" prop="r2VectorHeight">
                <el-input v-model="dataForm.r2VectorHeight" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2矢高极差(um)" prop="r2VectorHeightRange">
                <el-input v-model="dataForm.r2VectorHeightRange" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="白片外径偏心(%/规格)" prop="outerDiameterEcc">
                <el-input v-model="dataForm.outerDiameterEcc" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="白片坎合偏心(%/规格)" prop="kanheEcc">
                <el-input v-model="dataForm.kanheEcc" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="白片面间偏心(%/规格)" prop="faceEcc">
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
              <el-form-item label="BP坎合圆度(um)" prop="bpKanheRoundness">
                <el-input v-model="dataForm.bpKanheRoundness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="DMP坎合圆度(um)" prop="dmpKanheRoundness">
                <el-input v-model="dataForm.dmpKanheRoundness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="白片外径均值(um)" prop="outerDiameterAverage">
                <el-input v-model="dataForm.outerDiameterAverage" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="白片外径极差(um)" prop="outerDiameterRange">
                <el-input v-model="dataForm.outerDiameterRange" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外径圆度(um)" prop="outerDiameterRoundness">
                <el-input v-model="dataForm.outerDiameterRoundness" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="白片外径收缩率(‰)" prop="outerDiameterShrinkage">
                <el-input v-model="dataForm.outerDiameterShrinkage" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="白片外径粗糙度(um)" prop="outerDiameterRoughness">
                <el-input v-model="dataForm.outerDiameterRoughness" auto-complete="off" clearable
                          type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1平面度(um)" prop="r1Flatness">
                <el-input v-model="dataForm.r1Flatness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2平面度(um)" prop="r2Flatness">
                <el-input v-model="dataForm.r2Flatness" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="R1分割位均值(um)" prop="r1SplitAverage">
                <el-input v-model="dataForm.r1SplitAverage" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2分割位均值(um)" prop="r2SplitAverage">
                <el-input v-model="dataForm.r2SplitAverage" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1(白片面型nm)" prop="wftR1">
                <el-input v-model="dataForm.wftR1" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="R2(白片面型nm)" prop="wftR2">
                <el-input v-model="dataForm.wftR2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="5+3+3稳定性(nm)" prop="wftStability">
                <el-input v-model="dataForm.wftStability" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="全穴一致性(白片面型nm)" prop="wftConsistency">
                <el-input v-model="dataForm.wftConsistency" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="最大AS(白片面型nm)" prop="wftMaxAs">
                <el-input v-model="dataForm.wftMaxAs" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R1(镀膜片面型nm)" prop="cftR1">
                <el-input v-model="dataForm.cftR1" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="R2(镀膜片面型nm)" prop="cftR2">
                <el-input v-model="dataForm.cftR2" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="全穴一致性(镀膜片面型nm)" prop="cftConsistency">
                <el-input v-model="dataForm.cftConsistency" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="最大AS(镀膜片面型nm)" prop="cftMaxAs">
                <el-input v-model="dataForm.cftMaxAs" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="毛边" prop="burr">
                <el-input v-model="dataForm.burr" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="熔接线" prop="weldline">
                <el-input v-model="dataForm.weldline" auto-complete="off" clearable type="textarea"></el-input>
              </el-form-item>
            </el-col>
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
      <el-dialog v-model="picDialogVisible" :close-on-click-modal="false" :title="图片展示" :width="dialogWidth"
                 @close="closeImg">
        <el-image
            :fit="contain"
            :src="codeImg"
            :width="imgWidth" @load="onLoadImg">
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
  getMoldNo,
  getPartName,
  getProject,
  handleDelete,
  handleUpdate,
  uploadExcel
} from "@/api/lens/analysis/shapingResultData";
import {getStream} from "@/api/lens/analysis/allData";
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
        moldNo: "",
        searchType: "",
        startValue: "",
        endValue: ""
      },
      columns: [
        {type: "index", label: "序号", minWidth: 50},
        {prop: "category", label: "类别", minWidth: 100, sortable: false},
        {prop: "project", label: "项目", minWidth: 100, sortable: false},
        {prop: "partName", label: "零件名称", minWidth: 100, sortable: false},
        {prop: "material", label: "材料", minWidth: 100, sortable: false},
        {prop: "moldNo", label: "模具序号", minWidth: 100, sortable: false},
        {prop: "coreThickness", label: "芯厚(um)", minWidth: 100},
        {prop: "coreThicknessRange", label: "芯厚极差(um)", minWidth: 100},
        {prop: "r1VectorHeight", label: "R1矢高(um)", minWidth: 100},
        {prop: "r1VectorHeightRange", label: "R1矢高极差(um)", minWidth: 100},
        {prop: "r2VectorHeight", label: "R2矢高(um)", minWidth: 100},
        {prop: "r2VectorHeightRange", label: "R2矢高极差(um)", minWidth: 100},
        {prop: "outerDiameterEcc", label: "白片外径偏心(%/规格）", minWidth: 100},
        {prop: "kanheEcc", label: "白片坎合偏心(%/规格）", minWidth: 100},
        {prop: "faceEcc", label: "白片面间偏心(%/规格）", minWidth: 100},
        {prop: "annealingProcess", label: "退火制程", minWidth: 100},
        {prop: "bpKanheRoundness", label: "BP坎合圆度(um)", minWidth: 100},
        {prop: "dmpKanheRoundness", label: "DMP坎合圆度(um)", minWidth: 100},
        {prop: "outerDiameterAverage", label: "白片外径均值(um)", minWidth: 100},
        {prop: "outerDiameterRange", label: "白片外径极差(um)", minWidth: 100},
        {prop: "outerDiameterRoundness", label: "外径圆度(um)", minWidth: 100},
        {prop: "outerDiameterShrinkage", label: "白片外径收缩率(‰)", minWidth: 100},
        {prop: "outerDiameterRoughness", label: "白片外径粗糙度(um)", minWidth: 100},
        {prop: "r1Flatness", label: "R1平面度(um)", minWidth: 100},
        {prop: "r2Flatness", label: "R2平面度(um)", minWidth: 100},
        {prop: "r1SplitAverage", label: "R1分割位均值(um)", minWidth: 100},
        {prop: "r2SplitAverage", label: "R2分割位均值(um)", minWidth: 100},
        {prop: "wftR1", label: "R1(白片面型nm)", minWidth: 100},
        {prop: "wftR2", label: "R2(白片面型nm)", minWidth: 100},
        {prop: "wftConsistency", label: "全穴一致性(白片面型nm)", minWidth: 100},
        {prop: "wftMaxAs", label: "最大AS(白片面型nm)", minWidth: 100},
        {prop: "wftStability", label: "5+3+3稳定性(nm)", minWidth: 100},
        {prop: "cftR1", label: "R1(镀膜片面型nm)", minWidth: 100},
        {prop: "cftR2", label: "R2(镀膜片面型nm)", minWidth: 100},
        {prop: "cftConsistency", label: "全穴一致性(镀膜片面型nm)", minWidth: 100},
        {prop: "cftMaxAs", label: "最大AS(镀膜片面型nm)", minWidth: 100},
        {prop: "burr", label: "毛边(um)", minWidth: 100},
        {prop: "weldline", label: "熔接线", minWidth: 100, sortable: false},
        {prop: "remarks", label: "备注", minWidth: 100, sortable: false},
        {prop: "appearanceProblem", label: "外观问题", minWidth: 100, sortable: false}

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
        bpKanheRoundness: "",
        dmpKanheRoundness: "",
        outerDiameterAverage: "",
        outerDiameterRange: "",
        outerDiameterRoundness: "",
        outerDiameterShrinkage: "",
        outerDiameterRoughness: "",
        r1Flatness: "",
        r2Flatness: "",
        r1SplitAverage: "",
        r2SplitAverage: "",
        wftR1: "",
        wftR2: "",
        wftConsistency: "",
        wftMaxAs: "",
        wftStability: "",
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
      categoryList: [],
      projectList: [],
      partNameList: [],
      materialList: [],
      moldNoList: [],
      options: [
        {value: "core_thickness", label: "芯厚(um)"},
        {value: "core_thickness_range", label: "芯厚极差(um)"},
        {value: "r1_vector_height", label: "R1矢高(um)"},
        {value: "r1_vector_height_range", label: "R1矢高极差(um)"},
        {value: "r2_vector_height", label: "R2矢高(um)"},
        {value: "r2_vector_height_range", label: "R2矢高极差(um)"},
        {value: "outer_diameter_ecc", label: "外径偏心(%/规格）"},
        {value: "kanhe_ecc", label: "坎合偏心(%/规格）"},
        {value: "face_ecc", label: "面间偏心(%/规格）"},
        {value: "annealing_process", label: "退火制程"},
        {value: "bp_kanhe_roundness", label: "BP坎合圆度(um)"},
        {value: "dmp_kanhe_roundness", label: "DMP坎合圆度(um)"},
        {value: "outer_diameter_average", label: "外径均值(um)"},
        {value: "outer_diameter_range", label: "外径极差(um)"},
        {value: "outer_diameter_roundness", label: "外径圆度(um)"},
        {value: "outer_diameter_shrinkage", label: "外径收缩率(‰)"},
        {value: "outer_diameter_roughness", label: "外径粗糙度(um)"},
        {value: "r1_flatness", label: "R1平面度(um)"},
        {value: "r2_flatness", label: "R2平面度(um)"},
        {value: "r1_split_average", label: "R1分割位均值(um)"},
        {value: "r2_split_average", label: "R2分割位均值(um)"},
        {value: "wft_r1", label: "R1(白片面型nm)"},
        {value: "wft_r2", label: "R2(白片面型nm)"},
        {value: "wft_consistency", label: "全穴一致性(白片面型nm)"},
        {value: "wft_max_as", label: "最大AS(白片面型nm)"},
        {value: "wft_stability", label: "5+3+3稳定性(nm)"},
        {value: "cft_r1", label: "R1(镀膜片面型nm)"},
        {value: "cft_r2", label: "R2(镀膜片面型nm)"},
        {value: "cft_consistency", label: "全穴一致性(镀膜片面型nm)"},
        {value: "cft_max_as", label: "最大AS(镀膜片面型nm)"},
        {value: "burr", label: "毛边(um)"}
      ],
      picDialogVisible: false,
      codeImg: "",
      imgWidth: "500px",
      dialogWidth: "600px"
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
      this.moldNoList = await this.getMoldNo();
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
    getMoldNo() {
      return new Promise((resolve, reject) => {
        getMoldNo().then(res => {
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
      this.pageRequest.moldNo = this.filters.moldNo;

      this.pageRequest.searchType = this.filters.searchType;
      this.pageRequest.startValue = this.filters.startValue;
      this.pageRequest.endValue = this.filters.endValue;

      getDataByConditions(this.pageRequest)
          .then((res) => {
            const responseData = res.data;
            if (responseData.code === "000000") {
              responseData.data.records.map((value) => {
                for (let key in value) {
                  if (key === 'coreThickness' || key === 'coreThicknessRange' || key === 'r1VectorHeight' ||
                      key === 'r1VectorHeightRange' || key === 'r2VectorHeight' || key === 'r2VectorHeightRange' ||
                      key === 'outerDiameterEcc' || key === 'kanheEcc' || key === 'faceEcc' ||
                      key === 'bpKanheRoundness' || key === 'dmpKanheRoundness' || key === 'outerDiameterAverage' ||
                      key === 'outerDiameterRange' || key === 'outerDiameterRoundness' || key === 'outerDiameterShrinkage' ||
                      key === 'outerDiameterRoughness' || key === 'r1Flatness' || key === 'r2Flatness' ||
                      key === 'r1SplitAverage' || key === 'r2SplitAverage' || key === 'burr' || key === 'wftR1' ||
                      key === 'wftR2' || key === 'wftConsistency' || key === 'wftMaxAs' || key === 'wftStability' ||
                      key === 'cftR1' || key === 'cftR2' || key === 'cftConsistency' || key === 'cftMaxAs' || key === 'annealingProcess') {
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
          }).then(data != null ? data.callback : "");
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
        filePathPrefix: "shapingResultData",
        fileName: fileName
      }
      getStream(params).then(res => {
        console.log(res.data.size)
        if (res.data.size) {
          this.picDialogVisible = true
          const url = window.URL.createObjectURL(res.data)
          this.codeImg = url
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
    }
  }
};
</script>
