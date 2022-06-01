<template>
  <div class="sidebar">
    <el-menu
        :collapse="collapse"
        :default-active="onRoutes"
        active-text-color="#20a0ff"
        background-color="#324157"
        class="sidebar-el-menu"
        router
        text-color="#bfcbd9"
        unique-opened>
      <MenuItem :items="items"></MenuItem>
    </el-menu>
  </div>
</template>

<script>
import {getMenuItems} from "@/utils/auth";
import MenuItem from "@/components/MenuItem";

export default {
  components: {MenuItem},
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
      return this.$route.path.replace("/", "");
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
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 200px;
}

.sidebar > ul {
  height: 100%;
}
</style>
