<template>
  <div>
        <el-menu
          :default-active="menuIndex"
          class="el-menu-vertical-demo"
          mode="horizontal"
          style="float: right"
          >
          <el-menu-item index="1" route="/home">
              <span slot="title"><router-link to="/home" style="color: black;text-decoration: none;">首页</router-link></span>
          </el-menu-item>
          <el-menu-item index="2" route="/courses">
            <span slot="title"><router-link to="/courses" style="color: black;text-decoration: none;">课程</router-link></span>
          </el-menu-item>
          <el-menu-item index="3" route="/posts" v-show="show">
              <span slot="title"><router-link to="/posts" style="color: black;text-decoration: none;">论坛</router-link></span>
          </el-menu-item>
          <el-menu-item index="4" route="/profile/id">
              <span slot="title"><el-button type="text"  @click="profile" style="color: black;text-decoration: none;">个人</el-button></span>
          </el-menu-item>
          <el-menu-item index="5" route="/classes">
              <span slot="title"><router-link to="/classes" style="color: black;text-decoration: none;">班级</router-link></span>
          </el-menu-item>
          <el-menu-item index="6" route="/login">
            <span slot="title"><el-button type="text" style="color: black;text-decoration: none;" @click="logout">退出</el-button></span>
          </el-menu-item>
        </el-menu>

  </div>
</template>

<script>
    export default {
        name: "UserSidebar",
      data(){
          return{
            show:true,
            menuIndex:''
          }
      },
      created() {
        var userDetails=JSON.parse(localStorage.getItem('userDetails'));
        this.menuIndex=localStorage.getItem('menuIndex');
        console.log("menuIndex"+localStorage.getItem('menuIndex'));

        console.log(userDetails);
        if (userDetails.role===2){
          this.show=false;
        } else{
          this.show=true;
        }
      },
      methods:{
        logout(){
          localStorage.setItem('token','');
          localStorage.setItem('userDetails', '');
          this.$router.push({path: '/login'})
        },
        profile(){
          let path="/profile/";
          var userDetails=JSON.parse(localStorage.getItem("userDetails"));
          path+=userDetails.id;
          console.log("path: "+path);
          this.$router.push({path: path})
        },
      }
    }
</script>

<style scoped>

</style>
