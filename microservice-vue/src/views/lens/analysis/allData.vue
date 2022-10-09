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
    import {getResponseDataMessage} from "../utils/commonUtils";
    import SysTable from "@/components/SysTable";
    import{
        getDataByConditions,
        exportExcel
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
                  material: ""
                },
                columns: [
                    {type: "index", label: "序号", minWidth: 50},
                    {prop: "category", label: "类别", minWidth: 100},
                    {prop: "project", label: "项目", minWidth: 100},
                    {prop: "partName", label: "零件名称", minWidth: 100},
                    {prop: "material", label: "材料", minWidth: 100},
                    {prop: "moldNo", label: "模具序号", minWidth: 100},
                    {prop: "moldTemp", label: "模温", minWidth: 100},
                    {prop: "materialTemp", label: "料温", minWidth: 100},
                    {prop: "jetVelocity", label: "射速", minWidth: 100},
                    {prop: "vpSwitch", label: "VP切换", minWidth: 100},
                    {prop: "holdPressure1", label: "保压1", minWidth: 100},
                    {prop: "holdPressure2", label: "保压2", minWidth: 100},
                    {prop: "holdPressure3", label: "保压3", minWidth: 100},
                    {prop: "holdPressure4", label: "保压4", minWidth: 100},
                    {prop: "holdPressure5", label: "保压5", minWidth: 100},
                    {prop: "holdPressure6", label: "保压6", minWidth: 100},
                    {prop: "holdTime1", label: "保时1", minWidth: 100},
                    {prop: "holdTime2", label: "保时2", minWidth: 100},
                    {prop: "holdTime3", label: "保时3", minWidth: 100},
                    {prop: "holdTime4", label: "保时4", minWidth: 100},
                    {prop: "holdTime5", label: "保时5", minWidth: 100},
                    {prop: "holdTime6", label: "保时6", minWidth: 100},
                    {prop: "holdPressureVelocity", label: "保压速度", minWidth: 100},
                    {prop: "platenPosition", label: "压板位置", minWidth: 100},
                    {prop: "openingSpeed", label: "开模速度", minWidth: 100},
                    {prop: "ejectionSpeed", label: "顶出速度", minWidth: 100},
                    {prop: "coolingTime", label: "冷却时间", minWidth: 100},
                    {prop: "clampingForce", label: "锁模力", minWidth: 100},
                    {prop: "passivation", label: "钝化工艺", minWidth: 100},

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
                    {prop: "remarks", label: "备注", minWidth: 100},

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
                    {prop: "maxAngleR1", label: "最大角度R1", minWidth: 100},
                    {prop: "maxAngleR2", label: "最大角度R2", minWidth: 100},
                    {prop: "r1R2Distance", label: "R1终端到R2坎合根部距离(um)", minWidth: 100},
                    {prop: "middlePartThickness", label: "机构中间部分厚度(um)", minWidth: 100},
                    {prop: "bottomDiameterDistance", label: "坎合底部到外径距离(um)", minWidth: 100},
                    {prop: "mechanismDiameterThicknessRatio", label: "机构径厚比", minWidth: 100},
                    {prop: "r1KanheAngle", label: "R1坎合角度", minWidth: 100},
                    {prop: "r1KanheHeight", label: "R1坎合高度", minWidth: 100},
                    {prop: "r2KanheAngle", label: "R2坎合角度", minWidth: 100},
                    {prop: "r2KanheHeight", label: "R2坎合高度", minWidth: 100},
                    {prop: "r1Srtm", label: "R1消光位置(粗糙度SRTM)", minWidth: 100},
                    {prop: "r2Srtm", label: "R2消光位置(粗糙度SRTM)", minWidth: 100},
                    {prop: "outerDiameterSrtm", label: "外径消光位置(粗糙度SRTM)", minWidth: 100}
                ],
                pageRequest: {current: 1, size: 10},
                pageResult: {}
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

      }
    };
</script>