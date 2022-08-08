<template>
  <div>
    <el-menu
      :default-active="menuIndex"
      class="el-menu-vertical-demo"
      mode="horizontal"
      style="float: right"
    >
      <el-menu-item index="1">
        <span slot="title" >
          <router-link to="/admin" style="color: black;text-decoration: none;">首页</router-link>
        </span>
      </el-menu-item>
      <el-menu-item index="2" route="/coursesAdmin">
        <span slot="title">
          <router-link to="/coursesAdmin" style="color: black;text-decoration: none;">课程</router-link>
        </span>
      </el-menu-item>
      <el-menu-item index="3" route="/posts">
        <span slot="title">
          <router-link to="/posts" style="color: black;text-decoration: none;">论坛</router-link>
        </span>
      </el-menu-item>
      <el-menu-item index="4" route="/classes">
        <span slot="title">
          <router-link to="/classes" style="color: black;text-decoration: none;">班级</router-link>
        </span>
      </el-menu-item>
      <el-menu-item index="5" route="/login">
        <span slot="title"><el-button type="text" style="color: black;text-decoration: none;" @click="logout">退出</el-button></span>
      </el-menu-item>
    </el-menu>

  </div>
</template>

<script>
    export default {
        name: "AdminSidebar",
      data(){
        return{
          menuIndex:''
        }
      },
      methods:{
        logout(){
          localStorage.setItem('token','');
          localStorage.setItem('userDetails', '');
          this.$router.push({path: '/login'})
        }
      },
      mounted() {
          this.menuIndex=localStorage.getItem('menuIndex')
      }
    }
</script>

<style scoped>

</style>
