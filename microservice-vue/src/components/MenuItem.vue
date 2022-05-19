<template>
  <template v-for="item in items">
    <template v-if="item.subs">
      <el-sub-menu v-show="item.visible" :key="item.index" :index="item.index">
        <template #title>
          <font-awesome-icon v-if="item.icon" :icon="getFontAwesomeIcon(item.icon)" class="mr4"/>
          <span>{{ item.title }}</span>
        </template>
        <MenuItem :items="item.subs"/>
      </el-sub-menu>
    </template>
    <template v-else>
      <el-menu-item v-show="item.visible" :key="item.index" :index="item.index" @click="handleClick(item.index)">
        <font-awesome-icon v-if="item.icon" :icon="getFontAwesomeIcon(item.icon)" class="mr4"/>
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
    getFontAwesomeIcon(icon) {
      let icons = icon.split(',');
      if (icons.length !== 2) return icon
      return [icons[0], icons[1]]
    }
  },
}
</script>
