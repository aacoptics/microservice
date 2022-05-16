<template>
  <template v-for="item in items">
    <template v-if="item.subs">
      <el-sub-menu v-show="item.visible" :key="item.index" :index="item.index">
        <template #title>
          <i v-if="item.icon" :class="item.icon" class="mr4"></i>
          <span>{{ item.title }}</span>
        </template>
        <MenuItem :items="item.subs"/>
      </el-sub-menu>
    </template>
    <template v-else>
      <el-menu-item v-show="item.visible" :key="item.index" :index="item.index" @click="handleClick(item.index)">
        <i v-if="item.icon" :class="item.icon" class="mr4"></i>
        <template #title>{{ item.title }}</template>
      </el-menu-item>
    </template>
  </template>
</template>

<script>
export default {
  name: 'MenuItem',
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
  },

}
</script>
