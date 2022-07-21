<template>
  <div>
    <!--表格栏-->
    <el-table :id="randomId" ref="sysTable" v-loading="loading" :align="align"
              :border="border" :cell-style="cellStyle" :data="data.records" :element-loading-text="action.loading"
              :header-cell-style="headerCellStyle" :height="computedHeight"
              :highlight-current-row="highlightCurrentRow" :max-height="computedMaxHeight"
              :row-class-name="rowClassName" :show-overflow-tooltip="showOverflowTooltip"
              :size="size" :span-method="spanMethod" :stripe="stripe" style="width:100%;"
              @selection-change="selectionChange" @current-change="handleCurrentChange">
      <el-table-column v-if="showBatchDelete & showOperation" type="selection" width="40"></el-table-column>
      <el-table-column v-for="column in columns" :key="column.prop" :fixed="column.fixed"
                       :formatter="column.formatter" :label="column.label" :min-width="column.minWidth"
                       :prop="column.prop"
                       :sortable="column.sortable==null?true:column.sortable" :type="column.type" :width="column.width"
                       align="center"
                       header-align="center">
      </el-table-column>
      <slot name="custom-column"></slot>
      <el-table-column v-if="showOperation" :label="action.operation" align="center" fixed="right" header-align="center"
                       width="120">
        <template v-slot="scope">
          <el-button-group>
            <el-tooltip v-if="showOperation & showOperationEdit" :content="action.edit" placement="top">
              <el-button :size="size" type="primary" @click="handleEdit(scope.$index, scope.row)">
                <template #icon>
                  <font-awesome-icon :icon="['far', 'pen-to-square']"/>
                </template>
              </el-button>
            </el-tooltip>
            <el-tooltip v-if="showOperation & showOperationDel" :content="action.delete" placement="top">
              <el-button :size="size" type="danger" @click="handleDelete(scope.$index, scope.row)">
                <template #icon>
                  <font-awesome-icon :icon="['far', 'trash-can']"/>
                </template>
              </el-button>
            </el-tooltip>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <!--分页栏-->
    <div class="toolbar" style="padding:10px;">
      <el-button v-if="showBatchDelete & showOperation" :disabled="this.selections.length===0" :size="size"
                 style="float:left;" type="danger" @click="handleBatchDelete()">
        {{ action.batchDelete }}
      </el-button>
      <el-pagination v-if="showPagination" :current-page="pageRequest.current" :page-size="pageRequest.size"
                     :page-sizes="pageSizes"
                     :total="data.total" layout="total, sizes, prev, pager, next, jumper" style="float:right;"
                     @current-change="refreshPageRequest" @size-change="handleSizeChange">
      </el-pagination>
      <el-pagination v-else :current-page="pageRequest.current" :page-size="pageRequest.size"
                     :total="data.total" layout="total" style="float:right;"
                     @current-change="refreshPageRequest">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {debounce, getResponseDataMessage} from "@/utils/commonUtils";
import {v4 as uuidV4} from 'uuid';

export default {
  name: 'SysTable',
  props: {
    columns: Array, // 表格列配置
    data: Object, // 表格分页数据
    permsEdit: String,  // 编辑权限标识
    permsDelete: String,  // 删除权限标识
    size: { // 尺寸样式
      type: String,
      default: 'small'
    },
    align: {  // 文本对齐方式
      type: String,
      default: 'left'
    },
    maxHeight: {  // 表格最大高度
      type: Number,
      default: 500
    },
    height: {  // 表格最大高度
      type: Number,
      default: 400
    },
    showOperation: {  // 是否显示操作组件
      type: Boolean,
      default: true
    },
    showOperationEdit: {  // 是否显示编辑操作组件
      type: Boolean,
      default: true
    },
    showOperationDel: {  // 是否显示删除操作组件
      type: Boolean,
      default: true
    },
    border: {  // 是否显示边框
      type: Boolean,
      default: false
    },
    stripe: {  // 是否显示斑马线
      type: Boolean,
      default: true
    },
    highlightCurrentRow: {  // // 是否高亮当前行
      type: Boolean,
      default: true
    },
    showOverflowTooltip: {  // 是否单行显示
      type: Boolean,
      default: true
    },
    showBatchDelete: {  // 是否显示操作组件
      type: Boolean,
      default: true
    },
    rowClassName: [String, Function],
    headerCellStyle: Object,
    cellStyle: [Object, Function],
    pageSize: {
      type: Number,
      default: 10
    },
    pageSizes: {
      type: Array,
      default: () => [10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500]
    },
    spanMethod: Function,
    showPagination: {
      type: Boolean,
      default: true
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
  data() {
    return {
      changeHeight: -1,
      changeMaxHeight: -1,
      randomId: uuidV4(),
      action: {
        operation: "操作",
        add: "新增",
        edit: "编辑",
        delete: "删除",
        batchDelete: "批量删除",
        search: "查询",
        loading: "拼命加载中",
        submit: "提交",
        confirm: "确定",
        cancel: "取消",
        reset: "重置"
      },
      // 分页信息
      pageRequest: {
        current: 1,
        size: this.pageSize
      },
      loading: false,  // 加载标识
      selections: []  // 列表选中列
    }
  },
  methods: {
    // 分页查询
    findPage: function () {
      this.loading = true
      let callback = () => {
        this.$nextTick(() => {
          let table = document.getElementById(this.randomId);
          if (table === null || table === undefined) return
          this.changeHeight = this.changeMaxHeight = Math.max(...[...table.getElementsByTagName('tbody')].map(t => t.clientHeight)) +
              Math.max(...[...table.getElementsByTagName('thead')].map(t => t.clientHeight)) + 5
        })
        this.loading = false
      }
      this.$emit('findPage', {pageRequest: this.pageRequest, callback: callback})
    },
    // 选择切换
    selectionChange: function (selections) {
      this.selections = selections
      this.$emit('selectionChange', {selections: selections})
    },
    // 选择切换
    handleCurrentChange: function (val) {
      this.$emit('handleCurrentChange', {val: val})
    },
    // 换页刷新
    refreshPageRequest: function (pageNum) {
      this.pageRequest.current = pageNum
      this.findPage()
    },
    // 换页刷新
    handleSizeChange: function (size) {
      this.pageRequest.size = size
      this.findPage()
    },
    // 编辑
    handleEdit: function (index, row) {
      this.$emit('handleEdit', {index: index, row: row})
    },
    // 删除
    handleDelete: function (index, row) {
      this.delete(row.id)
    },
    // 批量删除
    handleBatchDelete: function () {
      let ids = this.selections.map(item => item.id).toString()
      this.delete(ids)
    },
    handleClearSelection: function () {
      this.$refs.sysTable.setCurrentRow(null);
    },
    // 删除操作
    delete: function (ids) {
      this.$confirm('确认删除选中记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = []
        let idArray = (ids + '').split(',')
        for (let i = 0; i < idArray.length; i++) {
          params.push({'id': idArray[i]})
        }
        this.loading = true
        let callback = res => {
          const responseData = res.data
          if (responseData.code === '000000') {
            this.$message({message: '删除成功', type: 'success'})
            this.findPage()
          } else {
            this.$message({
              message: `操作失败${getResponseDataMessage(responseData)}`,
              type: 'error'
            })
          }
          this.loading = false
        }
        this.$emit('handleDelete', {params: params, callback: callback})
      }).catch((err) => {
        console.log(err)
      })
    }
  },
  mounted() {
    this.refreshPageRequest(1)
  }
}
</script>
