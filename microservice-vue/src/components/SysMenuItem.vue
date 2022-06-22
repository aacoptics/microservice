<template>
  <template v-for="item in items">
    <template v-if="item.subs">
      <el-sub-menu v-show="item.visible" :key="item.index" :index="item.index">
        <template #title>
          <font-awesome-icon v-if="fontAwesomeIconFormat(item.icon) instanceof Array"
                             :icon="fontAwesomeIconFormat(item.icon)" class="mr-2" fixed-width/>
          <span>{{ item.title }}</span>
        </template>
        <sys-menu-item :items="item.subs"/>
      </el-sub-menu>
    </template>
    <template v-else>
      <el-menu-item v-show="item.visible" :key="item.index" :index="item.index" @click="handleClick(item.index)">
        <font-awesome-icon v-if="fontAwesomeIconFormat(item.icon) instanceof Array"
                           :icon="fontAwesomeIconFormat(item.icon)" class="mr-2" fixed-width/>
        <template #title>{{ item.title }}</template>
      </el-menu-item>
    </template>
  </template>
</template>

<script>
import {fontAwesomeIconFormat} from "@/utils/commonUtils";

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
      this.$router.push({path: `/${index}`});
    },
    fontAwesomeIconFormat: (icon) => fontAwesomeIconFormat(icon)
  },
}
</script>
