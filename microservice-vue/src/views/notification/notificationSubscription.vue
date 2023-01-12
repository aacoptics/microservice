<template>
  <div>
    <div class="aac-container">
      <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
        <el-form :inline="true" :size="size">
          <el-form-item>
            <el-select v-model="filters.selectedType" placeholder="请选择" @change="findPage(null)">
              <el-option
                  v-for="item in starOptions"
                  :key="item.value"
                  :label="item.title"
                  :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.planKey" placeholder="任务名称"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="findPage(null)">查询
              <template #icon>
                <font-awesome-icon :icon="['fas', 'magnifying-glass']"/>
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
        </el-form>
      </div>
      <SysTable ref="sysTable" :columns="columns" :data="pageResult"
                :height="400" :highlightCurrentRow="true" :showBatchDelete="false"
                :stripe="false"
                @findPage="findPage"
                size="small"
                @handleCurrentChange="handleTaskSelectChange" @handleDelete="handleDelete" @handleEdit="handleEdit">
        <template v-slot:custom-column>
          <!--          <el-table-column align="center" fixed="right" header-align="center" label="定时状态"-->
          <!--                           width="80">-->
          <!--            <template v-slot="scope">-->
          <!--              <el-button v-if="scope.row.triggerStatus === 1"-->
          <!--                         round size="small" type="success">-->
          <!--                启动中-->
          <!--              </el-button>-->
          <!--              <el-button v-else round size="small" type="danger">-->
          <!--                停止-->
          <!--              </el-button>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
          <el-table-column align="center" fixed="left" header-align="center" label="订阅"
                           width="60">
            <template v-slot="scope">
              <el-tooltip v-if="scope.row.subscriptionEnabled" :content="IsStarContent(scope.row)" placement="bottom">
                <font-awesome-icon :class="isStarClass(scope.row)" :icon="isStarIcon(scope.row)"
                                   class="ml-1 cursor-pointer h-6" fixed-width size="2x" @click="star(scope.row)"/>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column align="center" fixed="left" header-align="center" label="消息编码"
                           width="80">
            <template v-slot="scope">
                <el-tag>{{
                    scope.row.notificationNo
                  }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" fixed="left" header-align="center" label="消息名称"
                           width="200">
            <template v-slot="scope">
              <el-tooltip v-if="scope.row.remark && scope.row.remark != null && scope.row.remark.trim() !== ''" :content="scope.row.remark" placement="top">
                <span>{{scope.row.jobDesc}}</span>
              </el-tooltip>
              <el-span>{{scope.row.jobDesc}}</el-span>
            </template>
          </el-table-column>
          <el-table-column align="center" fixed="right" header-align="center" label="定时状态"
                           width="80">
            <template v-slot="scope">
              <el-switch
                  v-model="scope.row.triggerStatus"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="启用"
                  inactive-text="停用"
                  inline-prompt
                  width="60px"
                  @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column align="center" fixed="right" header-align="center" label="执行"
                           width="80">
            <template v-slot="scope">
              <el-button size="small" type="warning" @click="handleTrigger(scope.row)">
                执行一次
              </el-button>
            </template>
          </el-table-column>
        </template>
      </SysTable>
      <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :title="operation?'新增':'编辑'"
                 width="60%" @open="handleDialogOpen">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="110px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="执行器" prop="jobGroup">
                <el-select v-model="dataForm.jobGroup" placeholder="请选择">
                  <el-option
                      v-for="item in executorInfo"
                      :key="item.id"
                      :label="item.title"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="消息Key" prop="planKey">
                <el-input v-model="dataForm.planKey" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="消息环境" prop="jobEnvironment">
                <el-select v-model="dataForm.jobEnvironment" placeholder="请选择">
                  <el-option
                      v-for="item in environmentOption"
                      :key="item.value"
                      :label="item.title"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上线时间" prop="onlineTime">
                <el-date-picker
                    v-model="dataForm.onlineTime"
                    :editable="false"
                    placeholder="请选择上线时间"
                    size="small"
                    type="date"
                    value-format="YYYY-MM-DD 00:00:00"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="产品线" prop="productLine">
                <el-select v-model="dataForm.productLine" placeholder="请选择">
                  <el-option
                      v-for="item in productionLineOption"
                      :key="item.value"
                      :label="item.value"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="消息责任人（审批人）" prop="responsiblePerson">
                <el-select ref="select"
                           v-model="dataForm.responsiblePerson"
                           :remote-method="remoteMethod"
                           filterable
                           placeholder="请选择审批人"
                           remote
                           remote-show-suffix
                           @change="setResponsiblePersonName(dataForm)">
                  <el-option
                      v-for="item in searchUserList"
                      :key="item['employeeNo']"
                      :label="item.name"
                      :value="item['employeeNo']"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="执行时间" prop="executeTime">
                <el-input v-model="dataForm.executeTime" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="可订阅" prop="subscriptionEnabled">
                <el-switch
                    v-model="dataForm.subscriptionEnabled"
                    inline-prompt
                    active-text="是"
                    :active-value="true"
                    inactive-text="否"
                    :inactive-value="false"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="消息名称" prop="jobDesc">
                <el-input v-model="dataForm.jobDesc" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="IT负责人" prop="author">
                <el-input v-model="dataForm.author" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="消息描述" prop="remark">
                <el-input v-model="dataForm.remark" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="消息Handle" prop="executorHandler">
                <el-select v-model="dataForm.executorHandler" placeholder="请选择">
                  <el-option
                      v-for="item in executorHandlerOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="消息飞书群" prop="currentRobotsInfo">
                <el-select v-model="currentRobotsInfo" multiple placeholder="请选择"
                           value-key="id">
                  <el-option
                      v-for="item in robotOptions"
                      :key="item.id"
                      :label="item.robotName"
                      :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="报警邮件" prop="alarmEmail">
                <el-input v-model="dataForm.alarmEmail" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="调度类型" prop="scheduleType">
                <el-select v-model="dataForm.scheduleType" placeholder="请选择">
                  <el-option
                      v-for="item in scheduleTypeOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col v-if="dataForm.scheduleType!=='NONE'" :span="12">
              <el-form-item v-if="dataForm.scheduleType==='CRON'" label="CRON" prop="scheduleConf">
                <el-input v-model="dataForm.scheduleConf" auto-complete="off" placeholder="请输入CRON">
                  <template #append>
                    <el-button type="primary" @click="handleShowCron">
                      <template #icon>
                        <font-awesome-icon :icon="['far','clock']"/>
                      </template>
                      生成表达式
                    </el-button>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item v-if="dataForm.scheduleType==='FIX_RATE'" label="固定速度" prop="scheduleConf">
                <el-input v-model="dataForm.scheduleConf" auto-complete="off" placeholder="请输入（秒）"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="路由策略" prop="executorRouteStrategy">
                <el-select v-model="dataForm.executorRouteStrategy" placeholder="请选择">
                  <el-option
                      v-for="item in executorRouteOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="子任务ID" prop="childJobid">
                <el-input v-model="dataForm.childJobid" auto-complete="off" placeholder="如有多个，逗号分隔"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="调度过期策略" prop="misfireStrategy">
                <el-select v-model="dataForm.misfireStrategy" placeholder="请选择">
                  <el-option
                      v-for="item in misfireStrategyOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="阻塞处理策略" prop="executorBlockStrategy">
                <el-select v-model="dataForm.executorBlockStrategy" placeholder="请选择">
                  <el-option
                      v-for="item in executorBlockStrategyOptions"
                      :key="item.dictValue"
                      :label="item.dictLabel"
                      :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="任务超时时间" prop="executorTimeout">
                <el-input-number v-model="dataForm.executorTimeout" :min="0" auto-complete="off"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="失败重试次数" prop="executorFailRetryCount">
                <el-input-number v-model="dataForm.executorFailRetryCount" :min="0"
                                 auto-complete="off"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <!--          <el-row>-->
          <!--            <el-col :span="12">-->
          <!--              <el-form-item label="是否启用" prop="executorTimeout">-->
          <!--                <el-switch-->
          <!--                    v-model="dataForm.triggerStatus"-->
          <!--                    :active-value="1"-->
          <!--                    :inactive-value="0"-->
          <!--                    active-text="是"-->
          <!--                    inactive-text="否"-->
          <!--                    inline-prompt-->
          <!--                />-->
          <!--              </el-form-item>-->
          <!--            </el-col>-->
          <!--          </el-row>-->
        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" @click="resetSelection">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="submitForm">提交</el-button>
          </slot>
        </div>
      </el-dialog>
      <el-dialog v-model="triggerDialogVisible" :close-on-click-modal="false" destroy-on-close title="触发一次"
                 width="40%" @open="handleTriggerDialogOpen">
        <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" :size="size" label-width="110px">
          <el-form-item v-if="false" label="Id" prop="id">
            <el-input v-model="dataForm.id" auto-complete="off"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="24">
              <el-form-item label="任务Key" prop="planKey">
                <el-input v-model="dataForm.planKey" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="任务批次" prop="batchId">
                <el-input v-model="batchId" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="消息机器人" prop="currentRobotsInfo">
                <el-select v-model="currentRobotsInfo" multiple placeholder="请选择"
                           value-key="id">
                  <el-option
                      v-for="item in robotOptions"
                      :key="item.id"
                      :label="item.robotName"
                      :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="dialog-footer" style="padding-top: 20px;text-align: end">
          <slot name="footer">
            <el-button :size="size" @click="closeTriggerDialog">取消</el-button>
            <el-button :loading="editLoading" :size="size" type="primary" @click="triggerJob">触发</el-button>
          </slot>
        </div>
      </el-dialog>
      <el-dialog v-model="openCron" append-to-body class="scrollbar" destroy-on-close title="Cron表达式生成器">
        <crontab :expression="expression" @fill="crontabFill" @hide="openCron=false"></crontab>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import SysTable from "@/components/SysTable";
import {
  deleteTask,
  findTaskInfoPage, getFeishuUser, getFeishuUsers,
  handleAdd,
  handleUpdate, sendFeishuApprove,
  startTask,
  stopTask,
  triggerNotificationJob
} from "@/api/notification/notificationTask";
import {getDict} from "@/api/system/dictData";
import {findByIds, getAllRobotInfo} from "@/api/notification/robot";
import Crontab from '@/components/Crontab'
import {debounce} from "@/utils/commonUtils";
import {getUsername} from "@/utils/auth";
import {ElMessageBox} from "element-plus";

export default {
  name: "user",
  components: {SysTable, Crontab},
  data() {
    return {
      size: 'default',
      expression: "",
      filters: {
        selectedType: 0,
        planKey: ''
      },
      starOptions: [
        {
          title: '所有消息',
          value: 0
        },
        {
          title: '可订阅的消息',
          value: 1
        },
        {
          title: '我订阅的',
          value: 2
        }
      ],
      columns: [
        {prop: "planKey", label: "消息Key", minWidth: 100, sortable: false},
        {prop: "productLine", label: "产品线", minWidth: 80, sortable: false},
        {prop: "jobEnvironment", label: "环境", minWidth: 60, formatter: this.environmentFormat, sortable: false},
        {prop: "executeTime", label: "执行时间", minWidth: 100, sortable: false},
        {prop: "onlineTime", label: "上线时间", minWidth: 80, formatter: this.dateTimeFormat, sortable: false},
        {prop: "responsiblePersonName", label: "消息责任人（审批人）", minWidth: 80, sortable: false},
        {prop: "author", label: "IT责任人", minWidth: 80, sortable: false}
      ],
      pageRequest: {current: 1, size: 10},
      pageResult: {},
      operation: false, // true:新增, false:编辑
      openCron: false,
      dialogVisible: false, // 新增编辑界面是否显示
      triggerDialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        jobGroup: [{required: true, message: '请选择执行器', trigger: 'change'}],
        jobEnvironment: [{required: true, message: '请选择消息环境', trigger: 'change'}],
        productLine: [{required: true, message: '请选择产品线', trigger: 'change'}],
        responsiblePerson: [{required: true, message: '请选择责任人', trigger: 'change'}],
        onlineTime: [{required: true, message: '请选择上线时间', trigger: 'change'}],
        jobDesc: [{required: true, message: '请输入消息名称', trigger: 'blur'}],
        planKey: [{required: true, message: '请输入消息Key', trigger: 'blur'}],
        scheduleType: [{required: true, message: '请选择调度类型', trigger: 'change'}],
        scheduleConf: [{required: true, message: '请输入任务调度时间', trigger: 'blur'}],
        executeTime: [{required: true, message: '请描述执行时间，如：每天10点', trigger: 'blur'}],
        executorRouteStrategy: [{required: true, message: '请选择路由策略', trigger: 'change'}],
        executorBlockStrategy: [{required: true, message: '请选择阻塞处理策略', trigger: 'change'}],
        author: [{required: true, message: '请输入负责人', trigger: 'blur'}],
        executorHandler: [{required: true, message: '请选择消息Handle', trigger: 'change'}]
      },
      batchId: '',
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        jobGroup: 4,
        executorParam: '',
        planKey: '',
        jobDesc: '',
        author: '',
        alarmEmail: '',
        scheduleType: '',
        scheduleConf: '',
        executorRouteStrategy: '',
        childJobId: '',
        misfireStrategy: '',
        executorBlockStrategy: '',
        executorTimeout: 0,
        executorFailRetryCount: 0,
        executorHandler: 'RobotHandle',
        glueType: 'BEAN',
        glueSource: '',
        glueRemark: 'GLUE代码初始化',
        triggerStatus: 0,
        xxlJobId: 0,
        productLine: '',
        remark: '',
        onlineTime: '',
        responsiblePerson: '',
        responsiblePersonName: '',
        itPerson: '',
        jobStatus: true,
        subscriptionEnabled: true,
        executeTime: '',
        jobEnvironment: ''
      },
      searchUserList: [],
      executorInfo: [{"id": 4, "appName": "notification-center", "title": "统一消息中心"}],
      environmentOption: [{"title": "正式", "value": "PROD"}, {"title": "测试", "value": "QAS"}],
      productionLineOption: [
        {
          value: "手机LENS"
        },
        {
          value: "非手机LENS"
        },
        {
          value: "镜头WLG"
        },
        {
          value: "模组MOD"
        },
        {
          value: "传动VCM"
        },
        {
          value: "财务"
        },
        {
          value: "采购与供应链"
        },
        {
          value: "人力"
        },
        {
          value: "其他"
        },
      ],
      robotOptions: [],
      executorHandlerOptions: [],
      currentRobotsInfo: [],
      scheduleTypeOptions: [],
      misfireStrategyOptions: [],
      executorRouteOptions: [],
      executorBlockStrategyOptions: []
    }
  },
  methods: {
    remoteMethod: async function (query) {
      if (query != null && query.trim() !== '') {
        let res = await getFeishuUsers(query)
        const ResponseData = res.data
        if (ResponseData.code === '000000') {
          this.searchUserList = ResponseData.data
        } else {
          this.searchUserList = []
        }
      } else {
        this.searchUserList = []
      }
    },
    setResponsiblePersonName(row) {
      const idx = this.searchUserList.findIndex(item => item.employeeNo === row.responsiblePerson)
      if (idx > -1) {
        row.responsiblePersonName = this.searchUserList[idx].name
      }
    },
    getFeishuUser: function (query) {
      if (query != null && query.trim() !== '') {
        getFeishuUser(query).then((res) => {
          const ResponseData = res.data
          if (ResponseData.code === '000000') {
            this.searchUserList = []
            this.searchUserList.push(ResponseData.data)
          } else {
            this.searchUserList = []
          }
        })
      } else {
        this.searchUserList = []
      }
    },
    async star(row) {

      let msg = ''
      if (row.subscriptionStatus === 3) {
        this.$message({message: '正在审批中！', type: 'warning'})
        return
      } else if (row.subscriptionStatus === 1) {
        msg = '确认取消 ' + row.jobDesc + ' 的订阅吗，取消再订阅需要重新进行审批！'
      } else {
        msg = '确认订阅 ' + row.jobDesc + ' 吗，如确认，将发送审批至消息责任人 ' + row.responsiblePersonName + '！'
      }

      ElMessageBox.confirm(
          msg,
          'Warning',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
          }
      )
          .then(async () => {
            const subscriptionInfo = {
              notificationJobId: row.notificationId,
              subscriptionPerson: getUsername(),
              notificationDesc: row.jobDesc + '：' + row.remark,
              approveUser: row.responsiblePerson,
              subscriptionStatus: row.subscriptionStatus
            }
            let res = await sendFeishuApprove(subscriptionInfo)
            // let res = await deleteOrCreate(this.owner['employeeNo'], this.username)
            const ResponseData = res.data
            if (ResponseData.code === '000000') {
              this.$message({message: ResponseData.data, type: 'success'})
              if (row.subscriptionStatus !== 1)
                row.subscriptionStatus = 3
              else
                this.findPage(null)

            } else {
              this.$message({message: '操作失败', type: 'error'})
            }
          })
          .catch(() => {

          })
    },
    IsStarContent(row) {
      if (row.subscriptionStatus === 1)
        return '取消订阅'
      else if (row.subscriptionStatus === 3)
        return '审批中'
      else
        return '订阅'
    },
    isStarIcon(row) {
      if (row.subscriptionStatus === 1)
        return ['fas', 'star']
      else if (row.subscriptionStatus === 3)
        return ['fab', 'bilibili']
      else
        return ['far', 'star']
    },
    isStarClass(row) {
      return row.subscriptionStatus === 1 ? 'text-yellow-500' : 'text-gray-500'
    },
    // 任务状态修改
    handleStatusChange(row) {
      let text = row.triggerStatus === 1 ? "启用" : "停用";
      this.$confirm('确认要' + text + '""' + row.planKey + '"任务吗?').then(() => {
        if (row.triggerStatus === 1) {
          startTask(row).then((res) => {
            const responseData = res.data
            if (responseData.code === '000000') {
              this.$message({message: text + '成功', type: 'success'})
            } else {
              this.$message({message: responseData.data.msg, type: 'error'})
              row.triggerStatus = row.triggerStatus === 0 ? 1 : 0;
            }
          })
        } else if (row.triggerStatus === 0) {
          stopTask(row).then((res) => {
            const responseData = res.data
            if (responseData.code === '000000') {
              this.$message({message: text + '成功', type: 'success'})
            } else {
              this.$message({message: responseData.data.msg, type: 'error'})
              row.triggerStatus = row.triggerStatus === 0 ? 1 : 0;
            }
          })
        }
      }).catch(function () {
        row.triggerStatus = row.triggerStatus === 0 ? 1 : 0;
      });
    },
    handleShowCron() {
      this.expression = this.dataForm.scheduleConf;
      this.openCron = true;
    },
    crontabFill(value) {
      this.dataForm.scheduleConf = value;
    },
    // 获取分页数据
    findPage: function (data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.planKey = this.filters.planKey
      this.pageRequest.username = getUsername()
      this.pageRequest.searchOption = this.filters.selectedType
      findTaskInfoPage(this.pageRequest).then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.pageResult = responseData.data
        }
      }).then(data != null ? data.callback : '')
    },

    handleDialogOpen() {
      // getGroupInfo().then((res) => {
      //   const responseData = res.data
      //   if (responseData.code === '000000') {
      //     this.executorInfo = responseData.data
      //   }
      // })

      getAllRobotInfo().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.robotOptions = responseData.data
        }
      })

      getDict("notification_schedule_type").then(response => {
        this.scheduleTypeOptions = response.data.data
      })

      getDict("notification_misfire_strategy").then(response => {
        this.misfireStrategyOptions = response.data.data
      })

      getDict("notification_handle").then(response => {
        this.executorHandlerOptions = response.data.data
      })

      getDict("notification_executor_route").then(response => {
        this.executorRouteOptions = response.data.data
      })

      getDict("notification_executor_block_strategy").then(response => {
        this.executorBlockStrategyOptions = response.data.data
      })
    },

    handleTriggerDialogOpen() {
      getAllRobotInfo().then((res) => {
        const responseData = res.data
        if (responseData.code === '000000') {
          this.robotOptions = responseData.data
        }
      })
    },

    // 批量删除
    handleDelete: function (data) {
      if (data.params.length > 0)
        deleteTask(data.params[0]).then(data.callback)
    },
    // 显示新增界面
    handleAdd: function () {
      this.dialogVisible = true
      this.operation = true
      this.$refs.sysTable.handleClearSelection();
      this.dataForm = {
        id: 0,
        jobGroup: 4,
        executorParam: '',
        planKey: '',
        jobDesc: '',
        alarmEmail: '',
        scheduleType: 'CRON',
        scheduleConf: '',
        executorRouteStrategy: 'FIRST',
        childJobId: '',
        misfireStrategy: 'DO_NOTHING',
        executorBlockStrategy: 'SERIAL_EXECUTION',
        executorTimeout: 0,
        executorFailRetryCount: 0,
        executorHandler: 'RobotHandle',
        glueType: 'BEAN',
        glueSource: '',
        glueRemark: 'GLUE代码初始化',
        triggerStatus: 0,
        xxlJobId: 0,
        productLine: '手机LENS',
        remark: '',
        onlineTime: this.$moment(new Date()).format("YYYY-MM-DD 00:00:00"),
        responsiblePerson: '',
        responsiblePersonName: '',
        itPerson: '',
        jobStatus: true,
        subscriptionEnabled: true,
        executeTime: '',
        jobEnvironment: ''
      }
    },
    // 显示编辑界面
    handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.getFeishuUser(this.dataForm.responsiblePerson)
    },
    handleTrigger: function (params) {
      this.triggerDialogVisible = true
      this.dataForm = Object.assign({}, params)
    },
    // 编辑
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            const executorParams = {}
            executorParams.planKey = params.planKey
            executorParams.batchId = ""
            executorParams.msgTypeInfo = this.currentRobotsInfo.map((item) => {
              return Object.assign({},
                  {
                    'id': item.id
                  })
            })
            params.executorParam = JSON.stringify(executorParams)

            if (this.operation) {
              handleAdd(params).then((res) => {
                const responseData = res.data
                this.editLoading = false
                if (responseData.code === '000000') {
                  this.$message({message: '操作成功', type: 'success'})
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + responseData.msg, type: 'error'})
                }
                this.findPage(null)
              })
            } else {
              handleUpdate(params).then((res) => {
                const responseData = res.data
                this.editLoading = false
                if (responseData.code === '000000') {
                  this.$message({message: '操作成功', type: 'success'})
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + responseData.msg, type: 'error'})
                }
                this.findPage(null)
              })
            }
          })
        }
      })
    },
    triggerJob: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认触发吗？', '提示', {}).then(() => {
            this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            const executorParams = {}
            executorParams.planKey = params.planKey
            executorParams.batchId = this.batchId
            executorParams.msgTypeInfo = this.currentRobotsInfo.map((item) => {
              return Object.assign({},
                  {
                    'id': item.id
                  })
            })
            params.executorParam = JSON.stringify(executorParams)

            const triggerParam = {
              jobId: params.id,
              executorParam: params.executorParam,
              addressList: ''
            }
            triggerNotificationJob(triggerParam).then((res) => {
              const responseData = res.data
              this.editLoading = false
              if (responseData.code === '000000') {
                this.$message({message: '操作成功', type: 'success'})
                this.triggerDialogVisible = false
                this.$refs['dataForm'].resetFields()
              } else {
                this.$message({message: '操作失败, ' + responseData.msg, type: 'error'})
              }
              this.findPage(null)
            })
          })
        }
      })
    },
    handleTaskSelectChange(val) {
      this.currentRobotsInfo = []
      if (val == null || val.val == null) {
        return
      }

      const robotIdList = []
      if (val.val.executorParam) {
        const executorParamJson = JSON.parse(val.val.executorParam)
        if (executorParamJson.msgTypeInfo) {
          const robotList = executorParamJson.msgTypeInfo
          robotList.forEach((item) => {
            if (item.id)
              robotIdList.push(item.id)
          })
        }
      }
      if (robotIdList.length > 0) {
        findByIds(robotIdList).then((res) => {
          const responseData = res.data
          if (responseData.code === '000000') {
            this.currentRobotsInfo = responseData.data
          }
        })
      }
    },
    // 重置选择
    resetSelection() {
      this.dialogVisible = false
    },

    closeTriggerDialog() {
      this.triggerDialogVisible = false
    },
    // 时间格式化
    dateFormat: function (row, column) {
      return this.$moment(row[column.property]).format('YYYY-MM-DD HH:mm')
    },
    dateTimeFormat: function (row, column) {
      if (row[column.property])
        return this.$moment(row[column.property]).format("YYYY/MM/DD");
      else
        return ''
    },
    environmentFormat: function (row, column) {
      if (row[column.property]) {
        const newArr = this.environmentOption.filter(function (p) {
          return p.value === row[column.property];
        });
        if (newArr.length > 0)
          return newArr[0].title
        else return ''
      } else
        return ''
    },
  },
  computed: {
    computedHeight() {
      debounce(() => {
        return Math.max(...[this.changeHeight, this.height])
      }, 100);
    },
    computedMaxHeight() {
      debounce(() => {
        return Math.max(...[this.changeMaxHeight, this.maxHeight])
      }, 100);
    },
  },
}
</script>
