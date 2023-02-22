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
            <el-button type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
              </template>
            </el-button>
          </el-form-item>
          <el-row align="middle" class="float-right" justify="center">
            <el-form-item>
              <el-button :loading="exportDataLoading" type="success" @click="exportData('关联数据表')">导出数据
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
      </SysTable>
    </div>
  </div>
</template>

<script>
import {getResponseDataMessage} from "@/utils/commonUtils";
import SysTable from "@/components/SysTable";
import {
  exportExcel,
  getCategory,
  getDataByConditions,
  getMaterial,
  getPartName,
  getProject,
  getDepartment,
  getLensNumber
} from "@/api/lens/analysis/allData";

export default {
  name: "allData",
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
        {type: "index", label: "序号", minWidth: 40, fixed: "left"},
        {prop: "department", label: "事业部", minWidth: 55, sortable: false, fixed: "left"},
        {prop: "category", label: "类别", minWidth: 55, sortable: false, fixed: "left"},
        {prop: "lensNumber", label: "镜片数", minWidth: 55, sortable: false, fixed: "left"},
        {prop: "project", label: "项目", minWidth: 55, sortable: false, fixed: "left"},
        {prop: "partName", label: "零件名称", minWidth: 50, sortable: false, fixed: "left"},
        {prop: "material", label: "材料", minWidth: 100, sortable: false},

        {prop: "pcMoldNo", label: "模具序号(工艺条件)", minWidth: 100, sortable: false},
        {prop: "pcMoldType", label: "模具类型(工艺条件)", minWidth: 100, sortable: false},
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
        {prop: "mfCoolingTime", label: "模流冷却时间(s)", minWidth: 100, sortable: false},
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
        {prop: "platenPosition", label: "压板位置(mm)", minWidth: 100},
        {prop: "openingSpeed", label: "开模速度(mm/s)", minWidth: 100},
        {prop: "ejectionSpeed", label: "顶出速度(mm/s)", minWidth: 100},
        {prop: "coolingTime", label: "工艺冷却时间(s)", minWidth: 100, sortable: false},
        {prop: "clampingForce", label: "锁模力(Ton)", minWidth: 100, sortable: false},
        {prop: "passivation", label: "钝化工艺", minWidth: 100, sortable: false},

        {prop: "srMoldNo", label: "模具序号(成型结果)", minWidth: 100, sortable: false},
        {prop: "srMoldType", label: "模具类型(成型结果)", minWidth: 100, sortable: false},
        {prop: "coreThickness", label: "芯厚(um)", minWidth: 100},
        {prop: "coreThicknessRange", label: "芯厚极差(um)", minWidth: 100},
        {prop: "r1VectorHeight", label: "R1矢高(um)", minWidth: 100},
        {prop: "r1VectorHeightRange", label: "R1矢高极差(um)", minWidth: 100},
        {prop: "r2VectorHeight", label: "R2矢高(um)", minWidth: 100},
        {prop: "r2VectorHeightRange", label: "R2矢高极差(um)", minWidth: 100},
        {prop: "outerDiameterEcc", label: "白片外径偏心(%/规格）", minWidth: 100, sortable: false},
        {prop: "kanheEcc", label: "白片坎合偏心(%/规格）", minWidth: 100, sortable: false},
        {prop: "faceEcc", label: "白片面间偏心(%/规格）", minWidth: 100, sortable: false},
        {prop: "annealingProcess", label: "退火制程", minWidth: 100, sortable: false},
        {prop: "bpKanheRoundness", label: "BP坎合圆度(um)", minWidth: 100},
        {prop: "dmpKanheRoundness", label: "DMP坎合圆度(um)", minWidth: 100},
        {prop: "outerDiameterAverage", label: "白片外径均值(um)", minWidth: 100},
        {prop: "outerDiameterRange", label: "白片外径极差(um)", minWidth: 100},
        {prop: "outerDiameterRoundness", label: "白片外径圆度(um)", minWidth: 100},
        {prop: "outerDiameterShrinkage", label: "白片外径收缩率(‰)", minWidth: 100},
        {prop: "outerDiameterRoughness", label: "白片外径粗糙度(um)", minWidth: 100, sortable: false},
        {prop: "r1Flatness", label: "白片平面度R1(um)", minWidth: 100},
        {prop: "r2Flatness", label: "白片平面度R2(um)", minWidth: 100},
        {prop: "r1SplitAverage", label: "R1分割位台阶均值(um)", minWidth: 100, sortable: false},
        {prop: "r2SplitAverage", label: "R2分割位台阶均值(um)", minWidth: 100, sortable: false},
        {prop: "wftR1", label: "R1(白片面型nm)", minWidth: 100},
        {prop: "wftR2", label: "R2(白片面型nm)", minWidth: 100},
        {prop: "wftConsistency", label: "全穴一致性(白片面型nm)", minWidth: 100},
        {prop: "wftMaxAs", label: "最大AS(白片面型nm)", minWidth: 100},
        {prop: "wftStability", label: "5+3+3稳定性(nm)", minWidth: 100},
        {prop: "cftR1", label: "R1(镀膜片面型nm)", minWidth: 100},
        {prop: "cftR2", label: "R2(镀膜片面型nm)", minWidth: 100},
        {prop: "cftConsistency", label: "全穴一致性(镀膜片面型nm)", minWidth: 100},
        {prop: "cftMaxAs", label: "最大AS(镀膜片面型nm)", minWidth: 100},
        {prop: "burr", label: "分型面毛边(um)", minWidth: 100, sortable: false},
        {prop: "weldline", label: "熔接线", minWidth: 100, sortable: false},
        {prop: "appearanceProblem", label: "外观问题", minWidth: 100, sortable: false},
        {prop: "remarks", label: "备注", minWidth: 100, sortable: false},
        {prop: "abcFilesNo", label: "ABC档合格数分布", minWidth: 100, sortable: false},
        {prop: "structureNo", label: "结构方案总数", minWidth: 100, sortable: false},
        {prop: "moldTypeNo", label: "模具类型总数", minWidth: 100},
        {prop: "moldCost", label: "模具费用", minWidth: 100, sortable: false},
        {prop: "evtTime", label: "项目EVT耗时", minWidth: 100, sortable: false},
        {prop: "dvtTime", label: "项目DVT耗时", minWidth: 100, sortable: false},
        {prop: "evtDvtTime", label: "项目(EVT+DVT)耗时", minWidth: 100, sortable: false},
        {prop: "evtCost", label: "项目EVT费用", minWidth: 100, sortable: false},
        {prop: "dvtCost", label: "项目DVT费用", minWidth: 100, sortable: false},
        {prop: "evtDvtCost", label: "项目(EVT+DVT)费用", minWidth: 100, sortable: false},
        {prop: "projectMassProduction", label: "项目量产", minWidth: 100, sortable: false},
        {prop: "mtfAvgYield", label: "MTF平均良率", minWidth: 100, sortable: false},
        {prop: "massProductionTime", label: "量产时长（首次）", minWidth: 100, sortable: false},
        {prop: "massProductionShipment", label: "量产出货量（首次）", minWidth: 100, sortable: false},
        {prop: "projectInitiationTime", label: "项目立项时间", minWidth: 100, sortable: false},

        {prop: "structureSchemesNo", label: "结构方案总数", minWidth: 100, sortable: false},
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
        {prop: "r1R2Distance", label: "R1终端到R2坎合根部距离(um)", minWidth: 100, sortable: false},
        {prop: "middlePartThickness", label: "机构中间部分厚度(um)", minWidth: 100, sortable: false},
        {prop: "bottomDiameterDistance", label: "坎合底部到外径距离(um)", minWidth: 100, sortable: false},
        {prop: "mechanismDiameterThicknessRatio", label: "机构径厚比", minWidth: 100, sortable: false},
        {prop: "r1KanheAngle", label: "R1坎合角度(°)", minWidth: 100, sortable: false},
        {prop: "r1KanheHeight", label: "R1坎合高度(um)", minWidth: 100, sortable: false},
        {prop: "r2KanheAngle", label: "R2坎合角度(°)", minWidth: 100, sortable: false},
        {prop: "r2KanheHeight", label: "R2坎合高度(um)", minWidth: 100, sortable: false},
        {prop: "r1Srtm", label: "R1消光位置(粗糙度SRTM)", minWidth: 100, sortable: false},
        {prop: "r2Srtm", label: "R2消光位置(粗糙度SRTM)", minWidth: 100, sortable: false},
        {prop: "r1SplitPosition", label: "R1分割位位置", minWidth: 100, sortable: false},
        {prop: "r2SplitPosition", label: "R2分割位位置", minWidth: 100, sortable: false},
        {prop: "outerDiameterSrtm", label: "外径消光位置(粗糙度SRTM)", minWidth: 100, sortable: false},
        {prop: "partSurfaceLiftRatio", label: "分型面上抬比例", minWidth: 100, sortable: false},
        {prop: "mechanismTrou", label: "机构逃肉", minWidth: 100, sortable: false},


        {prop: "mfMoldType", label: "模具类型(模流)", minWidth: 100, sortable: false},
        {prop: "mfRunnerType", label: "流道类型(模流)", minWidth: 100, sortable: false},
        {prop: "moldDiameterRate", label: "模流外径收缩率(‰)", minWidth: 100, sortable: false},
        {prop: "flowFrontTemperature", label: "流动前沿温度(℃)", minWidth: 100, sortable: false},
        {prop: "vpChangePressure", label: "VP切换压力(MPa)", minWidth: 100, sortable: false},
        {prop: "simulateWireLength", label: "模拟熔接线长度(mm)", minWidth: 100, sortable: false},
        {prop: "wholePercent", label: "整体平均体积收缩率差值(%)", minWidth: 100, sortable: false},
        {prop: "effectiveR1", label: "R1有效径四点极差(%)", minWidth: 100, sortable: false},
        {prop: "effectiveR2", label: "R2有效径四点极差(%)", minWidth: 100, sortable: false},
        {prop: "ridgeR1", label: "R1坎合四点极差(%)", minWidth: 100, sortable: false},
        {prop: "ridgeR2", label: "R2坎合四点极差(%)", minWidth: 100, sortable: false},
        {prop: "refractiveR1", label: "模拟面型R1(nm)", minWidth: 100, sortable: false},
        {prop: "refractiveR2", label: "模拟面型R2(nm)", minWidth: 100, sortable: false},
        {prop: "competitorName", label: "竞品名称", minWidth: 100, sortable: false},
        {prop: "competitorLink", label: "竞品链接", minWidth: 100, sortable: false},

        {prop: "mdMoldNo", label: "模具序号(模具)", minWidth: 100, sortable: false},
        {prop: "mdMoldType", label: "模具类型(模具)", minWidth: 100, sortable: false},
        {prop: "moldTypeTotal", label: "模具类型总数", minWidth: 100, sortable: false},
        {prop: "moldCorePassivation", label: "模仁钝化工艺", minWidth: 100, sortable: false},
        {prop: "mdRunnerType", label: "流道类型(模具)", minWidth: 100, sortable: false},
        {prop: "cavityInnerDiameter", label: "型腔内径", minWidth: 100, sortable: false},
        {prop: "cavityInnerDiameterRange", label: "型腔内径极差", minWidth: 100, sortable: false},
        {prop: "firstRunner", label: "一级分流道(mm)", minWidth: 100, sortable: false},
        {prop: "secondRunner", label: "二级分流道(mm)", minWidth: 100, sortable: false},
        {prop: "thirdRunner", label: "主流道(mm)", minWidth: 100, sortable: false},
        {prop: "partingSurface", label: "分型面排气(um)", minWidth: 100, sortable: false},
        {prop: "splitPositionR1", label: "R1分割位排气(um)", minWidth: 100, sortable: false},
        {prop: "splitPositionR2", label: "R2分割位排气(um)", minWidth: 100, sortable: false},
        {prop: "gateType", label: "浇口类型", minWidth: 100, sortable: false},
        {prop: "gateWidth", label: "浇口宽度(mm)", minWidth: 100},
        {prop: "gateThickness", label: "浇口厚度(mm)", minWidth: 100},
        {prop: "gateR1Thickness", label: "浇口R1面厚度(mm)", minWidth: 100, sortable: false},
        {prop: "gateR2Thickness", label: "浇口R2面厚度(mm)", minWidth: 100, sortable: false},
        {prop: "moldOpeningType", label: "开模方式", minWidth: 100, sortable: false}
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      categoryList: [],
      projectList: [],
      partNameList: [],
      materialList: [],
      departmentList: [],
      lensNumberList: []
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
                  if (key === 'coreThickness' || key === 'coreThicknessRange' || key === 'r1VectorHeight' ||
                      key === 'r1VectorHeightRange' || key === 'r2VectorHeight' || key === 'r2VectorHeightRange' ||
                      key === 'bpKanheRoundness' || key === 'dmpKanheRoundness' || key === 'outerDiameterAverage' ||
                      key === 'outerDiameterRange' || key === 'outerDiameterRoundness' || key === 'outerDiameterShrinkage' ||
                      key === 'r1Flatness' || key === 'r2Flatness' || key === 'wftR1' || key === 'wftR2' || 
                      key === 'wftConsistency' || key === 'wftMaxAs' || key === 'wftStability' ||
                      key === 'cftR1' || key === 'cftR2' || key === 'cftConsistency' || key === 'cftMaxAs' || 
                      key === 'moldTypeNo' || key === 'coreThicknessLens' || key === 'maxWallThickness' || key === 'minWallThickness' ||
                      key === 'maxCoreRatio' || key === 'maxMinRatio' || key === 'outerDiameter' ||
                      key === 'edgeThickness' || key === 'wholeMinWallThickness' || key === 'wholeMaxWallThickness' ||
                      key === 'wholeMaxMinRatio' || key === 'wholeDiameterThicknessRatio' || key === 'maxAngleR1' ||
                      key === 'maxAngleR2' || key === 'r1MaxHeightDifference' || key === 'r2MaxHeightDifference' ||
                      key === 'gateWidth' || key === 'gateThickness' || key === 'mfMoldTemp' || key === 'mfMaterialTemp' ||
                      key === 'mfJetVelocity' || key === 'mfVpSwitch' || key === 'mfHoldPressure1' || key === 'mfHoldTime1' ||
                      key === 'mfHoldPressure2' || key === 'mfHoldTime2' || key === 'mfHoldPressure3' || key === 'mfHoldTime3' ||
                      key === 'mfHoldPressure4' || key === 'mfHoldTime4' || key === 'mfHoldPressure5' || key === 'mfHoldTime5' ||
                      key === 'mfHoldPressure6' || key === 'mfHoldTime6' || key === 'moldTemp' || key === 'materialTemp' ||
                      key === 'jetVelocity' || key === 'vpSwitch' || key === 'holdPressure1' || key === 'holdTime1' || key === 'holdPressure2' ||
                      key === 'holdTime2' || key === 'holdPressure3' || key === 'holdTime3' || key === 'holdPressure4' || key === 'holdTime4' ||
                      key === 'holdPressure5' || key === 'holdTime5' || key === 'holdPressure6' || key === 'holdTime6' || key === 'platenPosition' ||
                      key === 'openingSpeed' || key === 'ejectionSpeed') {
                    //过滤不需要转换类型的值
                    //纯数字列排序需要转换为Number类型，否者经常出现升降排序混乱
                    value[key] = Number(value[key])
                  }
                }
              });
              this.pageResult = responseData.data;
            } else {
              // this.$message({message: getResponseDataMessage(responseData), type: "error"});
              this.pageResult = {}
            }
          }).then(data != null ? data.callback : "");
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

  }
};
</script>
