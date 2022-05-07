<template>
  <div class="about">
    <v-header/>
    <v-sidebar/>
    <div :class="{ 'content-collapse': collapse }" class="content-box">
      <v-tags></v-tags>

      <div class="content">
        <v-breadcrumb/>
        <router-view v-slot="{ Component }">
          <transition mode="out-in" name="move">
            <keep-alive :include="tagsList">
              <component :is="Component"/>
            </keep-alive>
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>
<script>
import vHeader from "@/components/layout/Header";
import vSidebar from "@/components/layout/Sidebar";
import vTags from "@/components/layout/Tags.vue";
import vBreadcrumb from "@/components/layout/Breadcrumb";

export default {
  components: {
    vHeader,
    vSidebar,
    vTags,
    vBreadcrumb
  },
  computed: {
    tagsList() {
      return this.$store.state.tagsList.map(item => item.name);
    },
    collapse() {
      return this.$store.state.collapse;
    }
  }
};
</script>
