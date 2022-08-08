<template>
  <el-container>
    <el-header >
      <el-page-header @back="goBack" style="float:left;width: 400px"></el-page-header>
      <admin-sidebar v-bind:menuIndex="menuIndex" style="width: 400px;float: right"></admin-sidebar>
    </el-header>
    <el-main >
      <div class="inner">
      <div class="backCSS">
      </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
  import AdminSidebar from "./side/AdminSidebar"
    export default {
        name: "Admin",
      components:{
        AdminSidebar
      },
      data(){
          return{
            menuIndex:1,
          }
      },
      methods:{
        goBack() {
          this.$router.go(-1);
        },
      },
      mounted() {
          localStorage.setItem('menuIndex',1);
      }
    }
</script>

<style scoped>
  .backCSS{
    background: url("../assets/background.jpeg") repeat;
    background-position: center;
    background-position-x: center;
    background-position-y: center;
    border-radius: 50%;
    height: 400px;
    width: 400px;
    background-size: cover;

    position: relative;
    float: left;
    left: -50%;
  }
  .inner{
    position: absolute;
    left: 50%;
  }
</style>
