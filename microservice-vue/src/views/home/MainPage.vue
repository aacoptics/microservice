<template>
  <div class="mainPage-container" />
</template>
<script>

export default {
  name: 'mainPage',
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

<style scoped>
.mainPage-container {
  position: relative;
  width: 100%;
  height: 98%;
  background-image: url(../../assets/img/back.png);
  background-size: 100%;
  border: 1px solid #ddd;
  border-radius: 5px;
}
</style>
