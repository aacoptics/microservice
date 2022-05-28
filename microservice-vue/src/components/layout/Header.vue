<template>
  <div class="header">
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="collapseChange">
      <el-icon v-if="!collapse">
        <Fold/>
      </el-icon>
      <el-icon v-else>
        <Expand/>
      </el-icon>
    </div>
    <div class="logo">光学统一数据平台</div>
    <div class="header-right">
      <div class="header-user-con">
        <!-- 消息中心 -->
        <!--<div class="btn-bell">
          <el-tooltip
              effect="dark"
              :content="message?`有${message}条未读消息`:`消息中心`"
              placement="bottom">
            <router-link to="/tabs">
              <font-awesome-icon :icon="['far', 'bell']" style="color: white"/>
            </router-link>
          </el-tooltip>
          <span class="btn-bell-badge" v-if="message"></span>
        </div>-->
        <!-- 用户头像 -->
        <div class="user-avator">
          <img src="../../assets/img/avator.png"/>
        </div>
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{ userRealName }}
                      <font-awesome-icon :icon="['fas', 'caret-down']"/>
                    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <h3 style="text-align: center">
                {{ username }}
              </h3>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script>
import {getUserDetail, getUsername, removeLoginInfo} from "@/utils/auth";
import {logout} from "@/api/system/user";
import {Expand, Fold} from "@element-plus/icons-vue";

export default {
  components: {Expand, Fold},
  data() {
    return {
      fullscreen: false,
      name: "default",
      message: 2
    };
  },
  computed: {
    username() {
      let userName = getUsername()
      return userName ? userName : this.name;
    },
    userRealName() {
      let userName = getUsername()
      let userDetail = getUserDetail()
      let realName = userDetail && userDetail.name ? userDetail.name : userName;
      return realName ? realName : this.name;
    },
    collapse() {
      return this.$store.state.collapse;
    }
  },
  methods: {
    // 用户名下拉菜单选择事件
    handleCommand(command) {
      if (command === "logout") {
        removeLoginInfo()
        logout().then((response) => {
          if (response.status === 200) {
            this.$message.success("退出成功");
          }
        })
        this.$router.push("/login");
      }
    },
    // 侧边栏折叠
    collapseChange() {
      this.$store.commit("handleCollapse", !this.collapse);
    }
  },
  mounted() {
    if (document.body.clientWidth < 1500) {
      this.collapseChange();
    }
  }
};
</script>
<style scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #fff;
}

.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}

.header .logo {
  float: left;
  width: 250px;
  line-height: 70px;
}

.header-right {
  float: right;
  padding-right: 50px;
}

.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}

.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}

.btn-bell,
.btn-fullscreen {
  position: relative;
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 15px;
  cursor: pointer;
}

.btn-bell-badge {
  position: absolute;
  right: 0;
  top: -2px;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: #f56c6c;
  color: #fff;
}

.btn-bell .el-icon-bell {
  color: #fff;
}

.user-name {
  margin-left: 1px;
}

.user-avator {
  margin-left: 20px;
  margin-right: 10px;
}

.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.el-dropdown-link {
  color: #fff;
  cursor: pointer;
}

.el-dropdown-menu__item {
  text-align: center;
}
</style>
