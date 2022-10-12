<template>
  <div v-if="showTags" class="tags">
    <ul>
      <li
          v-for="(item,index) in tagsList"
          :key="index"
          :class="{'active': isActive(item.path)}"
          class="tags-li">
        <router-link :to="item.path" class="tags-li-title">{{ item.title }}</router-link>
        <span class="tags-li-icon" @click="closeTags(index)">
          <font-awesome-icon :icon="['fas', ['xmark']]"/>
        </span>
      </li>
    </ul>
    <div class="tags-close-box">
      <el-row align="middle" justify="center">
        <el-col :span="6">
          <el-tooltip :content="fullscreen ? '退出全屏' : '全屏'" placement="bottom">
            <font-awesome-icon v-if="fullscreenIcon" :icon="fullscreenIcon" class="w-full" size="lg"
                               @click="screen"/>
          </el-tooltip>
        </el-col>
        <el-col :span="18">
          <el-dropdown class="w-full" @command="handleTags">
            <el-button size="small" style="height: 30px;" type="primary">
              标签选项
              <font-awesome-icon :icon="['fas','angle-down']" class="ml-5"></font-awesome-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu size="small">
                <el-dropdown-item command="other">关闭其他</el-dropdown-item>
                <el-dropdown-item command="all">关闭所有</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-col>
      </el-row>
    </div>
  </div>
</template>


<script>

export default {
  data() {
    return {
      fullscreen: false,
      fullscreenIcon: ['fas', 'expand']
    };
  },
  computed: {
    tagsList() {
      return this.$store.state.tagsList;
    },
    showTags() {
      return this.tagsList.length > 0;
    }
  },
  methods: {
    screen() {
      this.fullscreenIcon = ''
      let element = document.documentElement;
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen();
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen();
        }
      }
      this.fullscreen = !this.fullscreen;
      this.$nextTick(() => {
        this.fullscreenIcon = this.fullscreen ? ['fas', 'compress'] : ['fas', 'expand']
      })
    },
    isActive(path) {
      return path === this.$route.fullPath;
    },
    // 关闭单个标签
    closeTags(index) {
      const delItem = this.tagsList[index];
      this.$store.commit("delTagsItem", {index});
      const item = this.tagsList[index]
          ? this.tagsList[index]
          : this.tagsList[index - 1];
      if (item) {
        delItem.path === this.$route.fullPath &&
        this.$router.push(item.path);
      } else {
        this.$router.push("/");
      }
      if (this.$route.meta.webUrl) {
        this.$store.commit('delIframeView', this.$route)
      }
    },
    // 关闭全部标签
    closeAll() {
      this.$store.commit("clearTags");
      this.$router.push("/");
    },
    // 关闭其他标签
    closeOther() {
      const curItem = this.tagsList.filter(item => {
        return item.path === this.$route.fullPath;
      });
      this.$store.commit("closeTagsOther", curItem);
    },
    // 设置标签
    setTags(route) {
      const isExist = this.tagsList.some(item => {
        return item.path === route.fullPath;
      });
      if (!isExist) {
        if (this.tagsList.length >= 8) {
          this.$store.commit("delTagsItem", {index: 0});
          this.$store.commit('delIframeView', this.tagsList[0])
        }
        let title = ''
        if (route.meta['title'] instanceof Array) {
          title = route.meta['title'].find(i => i.path === route.path).title;
        } else {
          title = route.meta['title'];
        }
        this.$store.commit("setTagsItem", {
          name: route.name,
          title: title,
          path: route.fullPath
        });
        if (this.$route.meta.webUrl) {
          this.$store.commit('addIframeView', this.$route)
        }
      }
    },
    handleTags(command) {
      command === "other" ? this.closeOther() : this.closeAll();
    }
  },
  watch: {
    $route(newValue) {
      this.setTags(newValue);
    }
  },
  created() {
    this.setTags(this.$route);
    // 关闭当前页面的标签页
    // this.$store.commit("closeCurrentTag", {
    //     $router: this.$router,
    //     $route: this.$route
    // });
  }
};
</script>


<style>
.tags {
  position: relative;
  height: 30px;
  overflow: hidden;
  background: #fff;
  padding-right: 120px;
  box-shadow: 0 5px 10px #ddd;
}

.tags ul {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
}

.tags-li {
  float: left;
  margin: 3px 5px 2px 3px;
  border-radius: 3px;
  font-size: 12px;
  overflow: hidden;
  cursor: pointer;
  height: 23px;
  line-height: 23px;
  border: 1px solid #e9eaec;
  background: #fff;
  padding: 0 5px 0 12px;
  vertical-align: middle;
  color: #666;
  -webkit-transition: all 0.3s ease-in;
  -moz-transition: all 0.3s ease-in;
  transition: all 0.3s ease-in;
}

.tags-li:not(.active):hover {
  background: #f8f8f8;
}

.tags-li.active {
  color: #fff;
}

.tags-li-title {
  float: left;
  max-width: 80px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-right: 5px;
  color: #666;
}

.tags-li.active .tags-li-title {
  color: #fff;
}

.tags-close-box {
  position: absolute;
  right: 0;
  top: 0;
  box-sizing: border-box;
  padding-top: 1px;
  text-align: center;
  width: 140px;
  height: 30px;
  background: #fff;
  box-shadow: -3px 0 15px 3px rgba(0, 0, 0, 0.1);
  z-index: 10;
}
</style>
