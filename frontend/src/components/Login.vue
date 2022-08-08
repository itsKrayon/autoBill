<template>
  <div id="base_login">
    <el-form :model="loginForm"
             :rules="rules"
             class="login_container"
             label-position="left"
             label-width="0px"
             v-loading="loading">
      <h3 class="login_title">Login</h3>
      <el-form-item prop="username">
        <el-input type="text"
                  v-model="loginForm.username"
                  auto-complete="off"
                  placeholder="username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password"
                  v-model="loginForm.password"
                  auto-complete="off"
                  placeholder="password"></el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary"
                   style="width: 40%;background: #2aabd2;border: none"
                   v-on:click="login">login</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<script>

  export default {
    name: 'Login',
    data () {
      return {
        loginForm: {
          username: '',
          password: ''
        },
        rules: {
          username: [{required: true, message: '', trigger: 'blur'}],
          password: [{required: true, message: '', trigger: 'blur'}]
        },
        loading: false
      }
    },
    methods: {
      login(){
        console.log(this.loginForm.username+this.loginForm.password);
        localStorage.setItem('token','');
        this.$axios
          .post("/login",{
            username:this.loginForm.username,
            password:this.loginForm.password
          } )
          .then((res) => {
            console.log(res);
            // 在localStorage中存储用户信息
            if(res.data.userDetails) {
              localStorage.setItem('userDetails', JSON.stringify(res.data.userDetails));
            }
            // 存储用户token
            if(res.data.token) {
              localStorage.setItem('token', res.data.token);
            }
            localStorage.setItem('menuIndex',1);
            // 跳转对应页面
            var userDetails=JSON.parse(localStorage.getItem('userDetails'));
            if (userDetails.authorities[0].authority === "Role_Admin"){
              this.$router.push({path: '/admin'})
            } else {
              this.$router.push({path:'/home'})
            }
          })
          .catch((error) => {
            console.log(error);
            this.$message.error("登录失败");
          });
      }

    }
  }
</script>

<style scoped>
  #base_login{
    background: url("../assets/background.jpeg") repeat;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }
  body{
    margin: 0px;
    padding: 0px;
  }
  .login_container{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #494e8f;
  }
</style>
