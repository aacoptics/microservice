<template>
  <div v-loading="loading" class="aac-container"
       element-loading-text="正在加载页面，请稍候！"
       style="margin:0; padding:0; border: 0; height:auto; width: 100%; overflow: auto">
    <iframe
        :id="iframeId"
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
  props: {
    src: {
      type: String,
      default: "/"
    },
    iframeId: {
      type: String
    }
  },
  data() {
    return {
      loading: false
    };
  },
  computed: {
    urlAddress() {
      let webUrl = this.src
      if (webUrl.indexOf("{#username}") > -1)
        webUrl = webUrl.replace("{#username}", getUsername())
      return webUrl
    }
  },
  mounted() {
    let _this = this;
    const iframeId = ("#" + this.iframeId).replace(/\//g, "\\/");
    const iframe = document.querySelector(iframeId);
    const deviceHeight = document.documentElement.clientHeight;
    iframe.style.height = (Number(deviceHeight) - 158) + 'px'; //数字是页面布局高度差

    // iframe页面loading控制
    if (iframe.attachEvent) {
      this.loading = true;
      iframe.attachEvent("onload", function () {
        _this.loading = false;
      });
    } else {
      this.loading = true;
      iframe.onload = function () {
        _this.loading = false;
      };
    }
  }
};
</script>
