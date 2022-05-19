<template>
  <div class="ico-body">
    <el-input v-model="filterName" clearable placeholder="请输入图标名称" style="position: relative;"
              @clear="filterIcons"
              @input="filterIcons">
      <template #suffix>
        <i class="fa fa-search"/>
      </template>
    </el-input>
    <div class="ico-list">
      <font-awesome-icon v-for="(item, index) in iconList" :key="index" :icon="item" aria-hidden="true"
                         @click="selectedIcon(item)"/>
    </div>
  </div>
</template>

<script>
import icons from './requireIcons'

export default {
  name: 'IconSelect',
  data() {
    return {
      size: 'mini',
      filterName: '',
      iconList: icons
    }
  },
  methods: {
    filterIcons() {
      this.iconList = icons
      if (this.filterName) {
        this.iconList = this.iconList.filter(item => item.includes(this.filterName))
      }
    },
    selectedIcon(name) {
      this.$emit('selected', name.toString())
      document.body.click()
    },
    reset() {
      this.filterName = ''
      this.iconList = icons
    }
  }
}
</script>

<style scoped>
.ico-body {
  width: 100%;
}

.ico-list {
  height: 200px;
  overflow-y: scroll;
}

.ico-list svg {
  margin: 3px;
  padding: 3px;
  cursor: pointer;
  font-size: 18px;
  width: 22px;
  border-radius: 3px;
}

.ico-list svg:hover {
  background-color: #1d9d74;
  color: #ffffff;
}
</style>
