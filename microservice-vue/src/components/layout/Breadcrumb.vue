<template>
  <div class="crumbs">
    <el-breadcrumb v-if="current !== undefined" separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">系统首页</el-breadcrumb-item>
      <el-breadcrumb-item v-for="(item, index) of current.split('|')" :key="index">{{ item }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>
<script>
import {getMenuItems} from "@/utils/auth";

export default {
  name: 'Breadcrumb',
  computed: {
    current() {
      return this.getMenu(this.$route.path.replace("/", ""), getMenuItems())
    }
  },
  methods: {
    getMenu(value, arr, res) {
      arr.forEach(element => {
        const indexTitle = element.index.indexOf('?') === -1 ? element.index : element.index.split('?')[0]
        if (indexTitle === value) { // 判断条件
          res = element.levelInfo;
        } else {
          if (element.subs && element.subs.length > 0) {
            res = this.getMenu(value, element.subs, res);
          }
        }
      });
      return res;
    }
  },
}
</script>
