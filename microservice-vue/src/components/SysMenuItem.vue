<template>
  <template v-for="item in items">
    <template v-if="item.subs">
      <el-sub-menu v-show="item.visible" :key="item.index" :index="item.index">
        <template #title>
          <font-awesome-icon v-if="fontAwesomeIconFormat(item.icon) instanceof Array"
                             :icon="fontAwesomeIconFormat(item.icon)" class="mr-2" fixed-width/>
          <span class="font-sans ... text-xs whitespace-normal leading-tight">{{ item.title }}</span>
        </template>
        <sys-menu-item :items="item.subs"/>
      </el-sub-menu>
    </template>
    <template v-else>
      <el-menu-item v-show="item.visible" :key="item.index" :index="item.index" @click="handleClick(item.index)">
        <font-awesome-icon v-if="fontAwesomeIconFormat(item.icon) instanceof Array"
                           :icon="fontAwesomeIconFormat(item.icon)" class="mr-2" fixed-width/>
        <template #title>
          <span class="font-sans ... text-xs whitespace-normal leading-tight">{{ item.title }}</span>
        </template>
      </el-menu-item>
    </template>
  </template>
</template>

<script>
import {fontAwesomeIconFormat} from "@/utils/commonUtils";
import querystring from 'querystring'

export default {
  name: 'SysMenuItem',
  props: {
    items: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },
  methods: {
    handleClick(index) {
      if (index.indexOf('?') > -1) {
        const queryArray = index.split('?')
        let queryInfo = querystring.parse(queryArray[1]);
        this.$router.push({path: `/${queryArray[0]}`, query: queryInfo});
      } else {
        this.$router.push({path: `/${index}`});
      }
    },
    fontAwesomeIconFormat: (icon) => fontAwesomeIconFormat(icon)
  },
}
</script>

<style scoped>
.el-sub-menu .el-menu-item {
  height: 40px;
  line-height: 40px;
  min-width: 200px;
  padding: 0 0
}
</style>
