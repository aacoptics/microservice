<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">模组IoT平台</div>
      <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
        <el-form-item prop="username">
          <el-input v-model="param.username" placeholder="用户名" tabindex="1">
            <template #prepend>
              <el-button icon="el-icon-user" tabindex="-1"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              type="password"
              placeholder="密码"
              v-model="param.password"
              @keyup.enter="submitForm()"
              tabindex="2"
          >
            <template #prepend>
              <el-button icon="el-icon-lock" tabindex="-1"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm()" tabindex="3">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      param: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          {required: true, message: "请输入用户名", trigger: "blur"}
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.$store.commit("clearTags");
    this.ssoLogin()
  },
  methods: {
    ssoLogin() {
      if (this.$route.query.token) {
        const token = this.$route.query.token;

        this.param.username = 'ssoToken'
        this.param.password = token
        this.$store.dispatch('user/login', this.param)
            .then(() => {
              this.$router.push("/");
            })
            .catch(() => {
            })
      }

    },
    submitForm() {
      this.$refs.login.validate(valid => {
        if (valid) {
          this.$store.dispatch('user/login', this.param)
              .then(() => {
                this.$message.success("登录成功");
                this.$router.push("/");
              })
        } else {
          this.$message.error("请输入账号和密码");
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url(../../assets/img/login-bg.png);
  background-size: 100%;
}

.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #fff;
  border-bottom: 1px solid #ddd;
}

.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 350px;
  margin: -190px 0 0 -175px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.3);
  overflow: hidden;
}

.ms-content {
  padding: 30px 30px;
}

.login-btn {
  text-align: center;
}

.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}

.login-tips {
  font-size: 12px;
  line-height: 30px;
  color: #fff;
}
</style>
