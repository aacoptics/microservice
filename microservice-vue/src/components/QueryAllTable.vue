<template>
  <div>
    <!--表格栏-->
    <el-table ref="queryAllTable" :data="data.records" :highlight-current-row="highlightCurrentRow"
              @selection-change="selectionChange" 
              v-loading="loading" :element-loading-text="action.loading"
              :border="border" :stripe="stripe"
              :show-overflow-tooltip="showOverflowTooltip" :max-height="maxHeight" :height="height" :size="size"
              :span-method="objectSpanMethod"
              :align="align" style="width:100%;">
      <el-table-column v-for="column in columns" header-align="left" align="left"
                       :prop="column.prop" :label="column.label" :width="column.width" :min-width="column.minWidth"
                       :fixed="column.fixed" :key="column.prop" :type="column.type" :formatter="column.formatter"
                       :sortable="column.sortable==null?true:column.sortable">
      </el-table-column>
      <slot name="custom-column"></slot>
    </el-table>

  </div>
</template>

<script>
export default {
  name: 'QueryAllTable',

  props: {
    columns: Array, // 表格列配置
    data: Object, // 表格数据
    size: { // 尺寸样式
      type: String,
      default: 'mini'
    },
    align: {  // 文本对齐方式
      type: String,
      default: 'left'
    },
    maxHeight: {  // 表格最大高度
      type: Number,
      default: 600
    },
    height: {  // 表格最大高度
      type: Number,
      default: 600
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

  },
  data() {
    return {
      action: {
        loading: "拼命加载中...",
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
      this.$emit('findPage', {callback: callback})
    },
    // 选择切换
    selectionChange: function (selections) {
      this.selections = selections
      this.$emit('selectionChange', {selections: selections})
    },
    // 合并单元格
    objectSpanMethod: function ({row, column, rowIndex, columnIndex}) {
      this.$emit('objectSpanMethod', {row: row, column: column, rowIndex: rowIndex, columnIndex: columnIndex})
    },

    handleClearSelection: function () {
      this.$refs.queryTable.setCurrentRow(null);
    },
    
  },
  mounted() {
  }
}
</script>
