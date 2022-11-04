<template>
  <div>
    <div class="aac-container">
      <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :model="filters" :size="size">
          <el-row>
            <el-col :span="4">
              <el-form-item label="类别" prop="category">
                <el-select v-model.trim="filters.category" placeholder="" clearable style="width: 180px;">
                  <el-option v-for="item in categoryList" :key="item.category" :value="item.category" :label="item.category"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="项目" prop="project">
                <el-select v-model.trim="filters.project" placeholder="" clearable style="width: 180px;">
                  <el-option v-for="item in projectList" :key="item.project" :value="item.project" :label="item.project"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="零件名称" prop="partName">
                <el-select v-model.trim="filters.partName" placeholder="" clearable style="width: 180px;">
                  <el-option v-for="item in partNameList" :key="item.partName" :value="item.partName" :label="item.partName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="材料" prop="material">
                <el-select v-model.trim="filters.material" placeholder="" clearable style="width: 180px;">
                  <el-option v-for="item in materialList" :key="item.material" :value="item.material" :label="item.material"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="模具序号" prop="moldNo">
                <el-select v-model.trim="filters.moldNo" placeholder="" clearable style="width: 180px;">
                  <el-option v-for="item in moldNoList" :key="item.moldNo" :value="item.moldNo" :label="item.moldNo"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-button :loading="exportLoading" type="primary" @click="exportExcelTemplate('工艺条件数据模板')">导出模板
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
              <el-button :loading="exportDataLoading" type="success" @click="exportData('工艺条件数据表')">导出数据
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
                      <el-form-item label="模流-模温(℃)" prop="mfMoldTemp">
                          <el-input v-model="dataForm.mfMoldTemp" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="模流-料温(℃)" prop="mfMaterialTemp">
                          <el-input v-model="dataForm.mfMaterialTemp" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-射速(mm/s)" prop="mfJetVelocity">
                          <el-input v-model="dataForm.mfJetVelocity" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-VP压力(Kgf/cm2)" prop="mfVpSwitch">
                          <el-input v-model="dataForm.mfVpSwitch" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="模流-保压1(Kgf/cm2)" prop="mfHoldPressure1">
                          <el-input v-model="dataForm.mfHoldPressure1" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-保压1时间(s)" prop="mfHoldTime1">
                          <el-input v-model="dataForm.mfHoldTime1" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-保压2(Kgf/cm2)" prop="mfHoldPressure2">
                          <el-input v-model="dataForm.mfHoldPressure2" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="模流-保压2时间(s)" prop="mfHoldTime2">
                          <el-input v-model="dataForm.mfHoldTime2" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-保压3(Kgf/cm2)" prop="mfHoldPressure3">
                          <el-input v-model="dataForm.mfHoldPressure3" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-保压3时间(s)" prop="mfHoldTime3">
                          <el-input v-model="dataForm.mfHoldTime3" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="模流-保压4(Kgf/cm2)" prop="mfHoldPressure4">
                          <el-input v-model="dataForm.mfHoldPressure4" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-保压4时间(s)" prop="mfHoldTime4">
                          <el-input v-model="dataForm.mfHoldTime4" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-保压5(Kgf/cm2)" prop="mfHoldPressure5">
                          <el-input v-model="dataForm.mfHoldPressure5" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="模流-保压5时间(s)" prop="mfHoldTime5">
                          <el-input v-model="dataForm.mfHoldTime5" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-保压6(Kgf/cm2)" prop="mfHoldPressure6">
                          <el-input v-model="dataForm.mfHoldPressure6" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="模流-保压6时间(s)" prop="mfHoldTime6">
                          <el-input v-model="dataForm.mfHoldTime6" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>

              <el-row>
                  <el-col :span="8">
                      <el-form-item label="实际-模温(℃)" prop="moldTemp">
                          <el-input v-model="dataForm.moldTemp" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-料温(℃)" prop="materialTemp">
                          <el-input v-model="dataForm.materialTemp" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-射速(mm/s)" prop="jetVelocity">
                          <el-input v-model="dataForm.jetVelocity" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="实际-VP压力(Kgf/cm2)" prop="vpSwitch">
                          <el-input v-model="dataForm.vpSwitch" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-保压1(Kgf/cm2)" prop="holdPressure1">
                          <el-input v-model="dataForm.holdPressure1" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-保压1时间(s)" prop="holdTime1">
                          <el-input v-model="dataForm.holdTime1" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>

              <el-row>
                  <el-col :span="8">
                      <el-form-item label="实际-保压2(Kgf/cm2)" prop="holdPressure2">
                          <el-input v-model="dataForm.holdPressure2" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-保压2时间(s)" prop="holdTime2">
                          <el-input v-model="dataForm.holdTime2" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-保压3(Kgf/cm2)" prop="holdPressure3">
                          <el-input v-model="dataForm.holdPressure3" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>

              <el-row>
                  <el-col :span="8">
                      <el-form-item label="实际-保压3时间(s)" prop="holdTime3">
                          <el-input v-model="dataForm.holdTime3" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-保压4(Kgf/cm2)" prop="holdPressure4">
                          <el-input v-model="dataForm.holdPressure4" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-保压4时间(s)" prop="holdTime4">
                          <el-input v-model="dataForm.holdTime4" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>

              <el-row>
                  <el-col :span="8">
                      <el-form-item label="实际-保压5(Kgf/cm2)" prop="holdPressure5">
                          <el-input v-model="dataForm.holdPressure5" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-保压5时间(s)" prop="holdTime5">
                          <el-input v-model="dataForm.holdTime5" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="实际-保压6(Kgf/cm2)" prop="holdPressure6">
                          <el-input v-model="dataForm.holdPressure6" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="实际-保压6时间(s)" prop="holdTime6">
                          <el-input v-model="dataForm.holdTime6" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="保压速度(mm/s)" prop="holdPressureVelocity">
                          <el-input v-model="dataForm.holdPressureVelocity" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="压板位置(mm)" prop="platenPosition">
                          <el-input v-model="dataForm.platenPosition" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="开模速度(mm/s)" prop="openingSpeed">
                          <el-input v-model="dataForm.openingSpeed" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="顶出速度(mm/s)" prop="ejectionSpeed">
                          <el-input v-model="dataForm.ejectionSpeed" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="冷却时间(s)" prop="coolingTime">
                          <el-input v-model="dataForm.coolingTime" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="8">
                      <el-form-item label="锁模力(Ton)" prop="clampingForce">
                          <el-input v-model="dataForm.clampingForce" auto-complete="off" clearable type="textarea"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="钝化工艺" prop="passivation">
                          <el-input v-model="dataForm.passivation" auto-complete="off" clearable type="textarea"></el-input>
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
    uploadExcel,
    getCategory,
    getProject,
    getPartName,
    getMaterial,
    getMoldNo
  } from "@/api/lens/analysis/processConditionData";
  import {ElMessageBox} from "element-plus";
  export default {
    name: "processConditionData",
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
            {prop: "category", label: "类别", minWidth: 100, sortable:false},
            {prop: "project", label: "项目", minWidth: 100, sortable:false},
            {prop: "partName", label: "零件名称", minWidth: 100, sortable:false},
            {prop: "material", label: "材料", minWidth: 100, sortable:false},
            {prop: "moldNo", label: "模具序号", minWidth: 100, sortable:false},
            {prop: "mfMoldTemp", label: "模流-模温(℃)", minWidth: 100},
            {prop: "mfMaterialTemp", label: "模流-料温(℃)", minWidth: 100},
            {prop: "mfJetVelocity", label: "模流-射速(mm/s)", minWidth: 100},
            {prop: "mfVpSwitch", label: "模流-VP压力(Kgf/cm2)", minWidth: 100},
            {prop: "mfHoldPressure1", label: "模流-保压1(Kgf/cm2)", minWidth: 100},
            {prop: "mfHoldTime1", label: "模流-保压1时间(s)", minWidth: 100},
            {prop: "mfHoldPressure2", label: "模流-保压2(Kgf/cm2)", minWidth: 100},
            {prop: "mfHoldTime2", label: "模流-保压2时间(s)", minWidth: 100},
            {prop: "mfHoldPressure3", label: "模流-保压3(Kgf/cm2)", minWidth: 100},
            {prop: "mfHoldTime3", label: "模流-保压3时间(s)", minWidth: 100},
            {prop: "mfHoldPressure4", label: "模流-保压4(Kgf/cm2)", minWidth: 100},
            {prop: "mfHoldTime4", label: "模流-保压4时间(s)", minWidth: 100},
            {prop: "mfHoldPressure5", label: "模流-保压5(Kgf/cm2)", minWidth: 100},
            {prop: "mfHoldTime5", label: "模流-保压5时间(s)", minWidth: 100},
            {prop: "mfHoldPressure6", label: "模流-保压6(Kgf/cm2)", minWidth: 100},
            {prop: "mfHoldTime6", label: "模流-保压6时间(s)", minWidth: 100},
            {prop: "moldTemp", label: "实际-模温(℃)", minWidth: 100},
            {prop: "materialTemp", label: "实际-料温(℃)", minWidth: 100},
            {prop: "jetVelocity", label: "实际-射速(mm/s)", minWidth: 100},
            {prop: "vpSwitch", label: "实际-VP压力(Kgf/cm2)", minWidth: 100},
            {prop: "holdPressure1", label: "实际-保压1(Kgf/cm2)", minWidth: 100},
            {prop: "holdTime1", label: "实际-保压1时间(s)", minWidth: 100},
            {prop: "holdPressure2", label: "实际-保压2(Kgf/cm2)", minWidth: 100},
            {prop: "holdTime2", label: "实际-保压2时间(s)", minWidth: 100},
            {prop: "holdPressure3", label: "实际-保压3(Kgf/cm2)", minWidth: 100},
            {prop: "holdTime3", label: "实际-保压3时间(s)", minWidth: 100},
            {prop: "holdPressure4", label: "实际-保压4(Kgf/cm2)", minWidth: 100},
            {prop: "holdTime4", label: "实际-保压4时间(s)", minWidth: 100},
            {prop: "holdPressure5", label: "实际-保压5(Kgf/cm2)", minWidth: 100},
            {prop: "holdTime5", label: "实际-保压5时间(s)", minWidth: 100},
            {prop: "holdPressure6", label: "实际-保压6(Kgf/cm2)", minWidth: 100},
            {prop: "holdTime6", label: "实际-保压6时间(s)", minWidth: 100},
            {prop: "holdPressureVelocity", label: "保压速度(mm/s", minWidth: 100},
            {prop: "platenPosition", label: "压板位置(mm)", minWidth: 100},
            {prop: "openingSpeed", label: "开模速度(mm/s)", minWidth: 100},
            {prop: "ejectionSpeed", label: "顶出速度(mm/s)", minWidth: 100},
            {prop: "coolingTime", label: "冷却时间(s)", minWidth: 100},
            {prop: "clampingForce", label: "锁模力(Ton)", minWidth: 100},
            {prop: "passivation", label: "钝化工艺", minWidth: 100, sortable:false},
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
          mfMoldTemp: "",
          mfMaterialTemp: "",
          mfJetVelocity: "",
          mfVpSwitch: "",
          mfHoldPressure1: "",
          mfHoldTime1: "",
          mfHoldPressure2: "",
          mfHoldTime2: "",
          mfHoldPressure3: "",
          mfHoldTime3: "",
          mfHoldPressure4: "",
          mfHoldTime4: "",
          mfHoldPressure5: "",
          mfHoldTime5: "",
          mfHoldPressure6: "",
          mfHoldTime6: "",
          moldTemp: "",
          materialTemp: "",
          jetVelocity: "",
          vpSwitch: "",
          holdPressure1: "",
          holdTime1: "",
          holdPressure2: "",
          holdTime2: "",
          holdPressure3: "",
          holdTime3: "",
          holdPressure4: "",
          holdTime4: "",
          holdPressure5: "",
          holdTime5: "",
          holdPressure6: "",
          holdTime6: "",
          holdPressureVelocity: "",
          platenPosition: "",
          openingSpeed: "",
          ejectionSpeed: "",
          coolingTime: "",
          clampingForce: "",
          passivation: "",
          createdBy: "",
          updatedBy: "",
          createdTime: "",
          updatedTime: ""
        },
        categoryList: [],
        projectList: [],
        partNameList: [],
        materialList: [],
        moldNoList: []
      };
    },
    created () {
      this.init()
    },
    mounted() {
    },
    methods: {
      async init(){
        this.categoryList = await this.getCategory();
        this.projectList = await this.getProject();
        this.partNameList = await this.getPartName();
        this.materialList = await this.getMaterial();
        this.moldNoList = await this.getMoldNo();
      },
      getCategory () {
        return new Promise((resolve, reject) => {
          getCategory().then(res => {
            if (res.data.code !== "000000") {
              resolve([])
            }
            resolve(res.data.data)
          })
        })
      },
      getProject () {
        return new Promise((resolve, reject) => {
          getProject().then(res => {
            if (res.data.code !== "000000") {
              resolve([])
            }
            resolve(res.data.data)
          })
        })
      },
      getPartName () {
        return new Promise((resolve, reject) => {
          getPartName().then(res => {
            if (res.data.code !== "000000") {
              resolve([])
            }
            resolve(res.data.data)
          })
        })
      },
      getMaterial () {
        return new Promise((resolve, reject) => {
          getMaterial().then(res => {
            if (res.data.code !== "000000") {
              resolve([])
            }
            resolve(res.data.data)
          })
        })
      },
      getMoldNo () {
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
        getDataByConditions(this.pageRequest)
            .then((res) => {
              const responseData = res.data;
              if (responseData.code === "000000") {
                responseData.data.records.map((value) => {
                  for (let key in value) {
                    if (key !== 'category' && key !== 'project' && key !== 'partName' &&
                    key !== 'material' && key !== 'moldNo' && key !== 'id' &&
                    key !== 'createdBy' && key !== 'createdTime' && key !== 'updatedBy' &&
                    key !== 'updatedTime' && key !== 'passivation') {
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
    }
  }
};
</script>
