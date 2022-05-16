<template>
  <div class="ico-body">
    <el-input v-model="filterName" style="position: relative;" clearable placeholder="请输入图标名称"
              @clear="filterIcons"
              @input="filterIcons">
      <template #suffix>
        <i class="fa fa-search"/>
      </template>
    </el-input>
    <div class="ico-list">
      <i v-for="(item, index) in iconList" :key="index" :class="item" aria-hidden="true" @click="selectedIcon(item)"/>
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
      this.$emit('selected', name)
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

.ico-list .fa {
  margin: 5px;
  padding: 5px;
  cursor: pointer;
  font-size: 18px;
  width: 28px;
  border-radius: 3px;
}

.ico-list .fa:hover {
  background-color: #1d9d74;
  color: #ffffff;
}
</style>
