<template>
  <div>
    <!--表格栏-->
    <el-table ref="queryTable" v-loading="loading" :align="align"
              :border="border" :data="data.records"
              :element-loading-text="action.loading" :height="height" :highlight-current-row="highlightCurrentRow"
              :max-height="maxHeight" :row-class-name="rowClassName"
              :show-overflow-tooltip="showOverflowTooltip" :size="size" :stripe="stripe" style="width:100%;"
              @selection-change="selectionChange" @current-change="handleCurrentChange">
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
            <el-tooltip :content="action.detail" placement="top">
              <el-button :size="size" type="primary" @click="handleDetail(scope.$index, scope.row)">
                <template #icon>
                  <font-awesome-icon :icon="['fas', 'file-lines']"/>
                </template>
              </el-button>
            </el-tooltip>

          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <!--分页栏-->
    <div class="toolbar" style="padding:10px;">
      <el-pagination :current-page="pageRequest.current" :page-size="pageRequest.size"
                     :total="data.total" layout="total, prev, pager, next, jumper" style="float:right;"
                     @current-change="refreshPageRequest">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: 'QueryTable',

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

    rowClassName: [String, Function]
  },
  data() {
    return {
      action: {
        operation: "操作",
        loading: "拼命加载中",
        detail: '明细'

      },
      // 分页信息
      pageRequest: {
        current: 1,
        size: 10
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
    // 明细
    handleDetail: function (index, row) {
      this.loading = true
      let callback = () => {
        this.loading = false
      }
      this.$emit('handleDetail', {pageRequest: this.pageRequest, index: index, row: row, callback: callback});
    },
    handleClearSelection: function () {
      this.$refs.queryTable.setCurrentRow(null);
    },

  },
  mounted() {
    // this.refreshPageRequest(1)
  }
}
</script>
