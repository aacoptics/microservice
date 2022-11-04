<template>
    <div>
      <div class="aac-container">
        <div class="toolbar w-full" style="float:left;padding-top:10px;padding-left:15px;">
          <el-form :inline="true" :size="size" :model="filters">
            <el-row>
              <el-col :span="5">
                <el-form-item label="类别" prop="category">
                  <el-select v-model.trim="filters.category" placeholder="" clearable style="width: 180px;">
                    <el-option v-for="item in categoryList" :key="item.category" :value="item.category" :label="item.category"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="项目" prop="project">
                  <el-select v-model.trim="filters.project" placeholder="" clearable style="width: 180px;">
                    <el-option v-for="item in projectList" :key="item.project" :value="item.project" :label="item.project"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="零件名称" prop="partName">
                  <el-select v-model.trim="filters.partName" placeholder="" clearable style="width: 180px;">
                    <el-option v-for="item in partNameList" :key="item.partName" :value="item.partName" :label="item.partName"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="材料" prop="material">
                  <el-select v-model.trim="filters.material" placeholder="" clearable style="width: 180px;">
                    <el-option v-for="item in materialList" :key="item.material" :value="item.material" :label="item.material"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form :inline="true" :size="size">
            <el-form-item>
              <el-button :loading="exportLoading" type="primary" @click="exportExcelTemplate('模具数据模板')">导出模板
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
                <el-button :loading="exportDataLoading" type="success" @click="exportData('模具数据表')">导出数据
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
                        <el-form-item label="模具类型" prop="moldType">
                            <el-input v-model="dataForm.moldType" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="模仁钝化工艺" prop="moldCorePassivation">
                            <el-input v-model="dataForm.moldCorePassivation" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="8">
                        <el-form-item label="流道类型" prop="runnerType">
                            <el-input v-model="dataForm.runnerType" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="一级分流道(mm)" prop="firstRunner">
                            <el-input v-model="dataForm.firstRunner" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="二级分流道(mm)" prop="secondRunner">
                            <el-input v-model="dataForm.secondRunner" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="8">
                        <el-form-item label="三级分流道(mm) " prop="thirdRunner">
                            <el-input v-model="dataForm.thirdRunner" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="分型面(um)" prop="partingSurface">
                            <el-input v-model="dataForm.partingSurface" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="分割位(um)" prop="splitPosition">
                            <el-input v-model="dataForm.splitPosition" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="8">
                        <el-form-item label="浇口类型" prop="gateType">
                            <el-input v-model="dataForm.gateType" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="浇口宽度(mm)" prop="gateWidth">
                            <el-input v-model="dataForm.gateWidth" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="浇口厚度(mm)" prop="gateThickness">
                            <el-input v-model="dataForm.gateThickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="8">
                        <el-form-item label="浇口R1面厚度(mm)" prop="gateR1Thickness">
                            <el-input v-model="dataForm.gateR1Thickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="浇口R2面厚度(mm)" prop="gateR2Thickness">
                            <el-input v-model="dataForm.gateR2Thickness" auto-complete="off" clearable type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="开模方式" prop="moldOpeningType">
                            <el-input v-model="dataForm.moldOpeningType" auto-complete="off" clearable type="textarea"></el-input>
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
    import{
        exportExcelTemplate,
        uploadExcel,
        getDataByConditions,
        exportExcel,
        handleDelete,
        handleUpdate,
        getCategory,
        getProject,
        getPartName,
        getMaterial,
    } from "@/api/lens/analysis/moldData";
    import {ElMessageBox} from "element-plus";

    export default {
        name: "moldData",
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
                    {prop: "category", label: "类别", minWidth: 100, sortable:false},
                    {prop: "project", label: "项目", minWidth: 100, sortable:false},
                    {prop: "partName", label: "零件名称", minWidth: 100, sortable:false},
                    {prop: "material", label: "材料", minWidth: 100, sortable:false},
                    {prop: "moldType", label: "模具类型", minWidth: 100, sortable:false},
                    {prop: "moldCorePassivation", label: "模仁钝化工艺", minWidth: 100, sortable:false},
                    {prop: "runnerType", label: "流道类型", minWidth: 100, sortable:false},
                    {prop: "firstRunner", label: "一级分流道(mm)", minWidth: 100},
                    {prop: "secondRunner", label: "二级分流道(mm)", minWidth: 100},
                    {prop: "thirdRunner", label: "三级分流道(mm)", minWidth: 100},
                    {prop: "partingSurface", label: "分型面(um)", minWidth: 100},
                    {prop: "splitPosition", label: "分割位(um)", minWidth: 100},
                    {prop: "gateType", label: "浇口类型", minWidth: 100, sortable:false},
                    {prop: "gateWidth", label: "浇口宽度(mm)", minWidth: 100},
                    {prop: "gateThickness", label: "浇口厚度(mm)", minWidth: 100},
                    {prop: "gateR1Thickness", label: "浇口R1面厚度(mm)", minWidth: 100},
                    {prop: "gateR2Thickness", label: "浇口R2面厚度(mm)", minWidth: 100},
                    {prop: "moldOpeningType", label: "开模方式", minWidth: 100, sortable:false}
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
                  moldType: "",
                  moldCorePassivation: "",
                  runnerType: "",
                  firstRunner: "",
                  secondRunner: "",
                  thirdRunner: "",
                  partingSurface: "",
                  splitPosition: "",
                  gateType: "",
                  gateWidth: "",
                  gateThickness: "",
                  gateR1Thickness: "",
                  gateR2Thickness: "",
                  moldOpeningType: "",
                  createdBy: "",
                  updatedBy: "",
                  createdTime: "",
                  updatedTime: ""
                },
                categoryList: [],
                projectList: [],
                partNameList: [],
                materialList: [],
            };
        },
        mounted() {
        },
        created () {
          this.init()
        },
        methods: {
          async init(){
            this.categoryList = await this.getCategory();
            this.projectList = await this.getProject();
            this.partNameList = await this.getPartName();
            this.materialList = await this.getMaterial();
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
                    responseData.data.records.map((value) => {
                      for (let key in value) {
                        if (key === 'firstRunner' || key === 'secondRunner' || key === 'thirdRunner' ||
                        key === 'partingSurface' || key === 'splitPosition' || key === 'gateWidth' ||
                        key === 'gateThickness' || key === 'gateR1Thickness' || key === 'gateR2Thickness') {
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