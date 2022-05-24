<template>
  <div>

    <div class="aac-container" style="margin:0; padding:0;border: 0;height:auto;width: 100%;overflow: auto">
      <iframe id='webFrame'
              frameborder='0'
              hight='auto'
              scrolling='auto'
              src=""
              width='100%'
      ></iframe>
    </div>
  </div>
</template>
<script>

export default {
  name: 'nacos',
  mounted() {
    this.resizeFrame('webFrame')
  },
  methods: {
    resizeFrame(id) {
      const oIframe = document.getElementById(id);
      const deviceHeight = document.documentElement.clientHeight;
      oIframe.style.height = (Number(deviceHeight) - 125) + 'px'; //数字是页面布局高度差
    }
  }
}
</script>
