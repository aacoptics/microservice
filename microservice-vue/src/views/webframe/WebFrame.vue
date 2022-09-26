<template>
  <div class="aac-container" style="margin:0; padding:0;border: 0;height:auto;width: 100%;overflow: auto">
    <iframe id='webFrame'
            :src='urlAddress'
            frameborder='0'
            height='auto'
            scrolling='auto'
            width='100%'>
    </iframe>
  </div>
</template>
<script>

import {getUsername} from "@/utils/auth";

export default {
  name: "WebFrame",
  mounted() {
    this.resizeFrame('webFrame')
  },
  computed: {
    urlAddress() {
      let webUrl = this.$route.meta.webUrl
      if (webUrl.indexOf("{#username}") > -1)
        webUrl = webUrl.replace("{#username}", getUsername())
      return webUrl
    }
  },
  methods: {
    resizeFrame(id) {
      const oIframe = document.getElementById(id);
      const deviceHeight = document.documentElement.clientHeight;
      oIframe.style.height = (Number(deviceHeight) - 158) + 'px'; //数字是页面布局高度差
    }
  }
}
</script>
