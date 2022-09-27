<template>
  <div class="sidebar">
    <el-menu
        :collapse="collapse"
        :default-active="onRoutes"
        background-color="#ecf5ff"
        class="sidebar-el-menu"
        router
        unique-opened>
      <sys-menu-item :items="items"></sys-menu-item>
    </el-menu>
  </div>
</template>

<script>
import {getMenuItems} from "@/utils/auth";
import SysMenuItem from "@/components/SysMenuItem";

export default {
  components: {SysMenuItem},
  data() {
    return {
      items: [
        {
          icon: '["fas","house"]',
          index: "mainPage",
          title: "系统首页",
          visible: true
        }
      ]
    };
  },
  created() {
    this.items = this.items.concat(getMenuItems())
  },
  computed: {
    onRoutes() {
      return this.$route.fullPath.replace("/", "");
    },
    collapse() {
      return this.$store.state.collapse
    }
  }
};
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  background-color: #ecf5ff;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 210px;
}

.sidebar > ul {
  height: 100%;
}


</style>
