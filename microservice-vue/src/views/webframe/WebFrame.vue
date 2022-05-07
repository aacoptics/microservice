<template>
  <div class="container" style="margin:0; padding:0;border: 0;height:auto;width: 100%;overflow: auto">
    <iframe id='webFrame'
            height='auto'
            width='100%'
            :src='urlAddress'
            frameborder='0'
            scrolling='auto'>
    </iframe>
  </div>
</template>
<script>

export default {
  name: "WebFrame",
  mounted() {
    this.resizeFrame('webFrame')
  },
  computed: {
    urlAddress() {
      return this.$route.meta.webUrl
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
