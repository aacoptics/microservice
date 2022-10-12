<template>
  <transition-group mode="out-in" name="move">
    <inner-link
        v-for="(item, index) in iframeViews"
        v-show="$route.path === item.path"
        :key="item.path"
        :iframeId="'iframe' + index"
        :src="item.meta.webUrl"
    ></inner-link>
  </transition-group>
</template>

<script>

import InnerLink from "@/components/Iframe/InnerLink";

export default {
  components: {InnerLink},
  computed: {
    iframeViews() {
      return this.$store.state.iframeViews
    }
  }
}
</script>
