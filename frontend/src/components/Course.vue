<template>
  <el-container>
    <el-header>
      <el-page-header @back="goBack" style="float:left;width: 400px"></el-page-header>
      <CourseSidebar  v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></CourseSidebar>
    </el-header>
    <el-main style="">
      <div class="inner">
        <div class="backCSS">
        </div>
      </div>
      <!--
      <div>
        <div style="">
          <el-table
            style="width: 100%"
            title="公告">
            <el-table-column
              label="公告标题"
              width=""></el-table-column>
            <el-table-column
              label="公告简介"
              width=""></el-table-column>
            <el-table-column
              label="创建者"
              width=""></el-table-column>
            <el-table-column
              label="详情"
              width=""></el-table-column>
          </el-table>
        </div>
        <div>

          <el-table
            style="width: 100%"
            title="作业"
            :data="assignment">
            <el-table-column
              label="作业标题"
              prop="title"
              width=""></el-table-column>
            <el-table-column
              label="作业简介"
              prop="description"
              width=""></el-table-column>
            <el-table-column
              label="截止日期"
              prop="deadline"
              width=""></el-table-column>
            <el-table-column
              label="详情"
              width=""></el-table-column>
          </el-table>
        </div>
      </div>
      -->
    </el-main>
  </el-container>
</template>

<script>
  import CourseSidebar from "./side/CoursesSidebar"
    export default {
      name: "Course",
      data(){
          return{
            assignment:[{
              id:'',
              title:'',
              description:'',
              file:'',
              deadline:'',
              isExpired:'',
              creatorId:'',
            }],
            menuIndex:2,
          }
      },
      components:{
        CourseSidebar
      },
      methods:{
        goBack() {
          this.$router.go(-1);
        },
      },
      mounted() {
        localStorage.setItem('menuIndex',2);
      }

      /*
      created() {

        const loading = this.$loading({
          lock: true,
        });
        console.log("localStorage.getItem(\"sectionID\")   "+localStorage.getItem("sectionID"))
        this.$axios
          .get("/assignment",{
            params:{
              sectionID: localStorage.getItem("sectionID")
            }
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.assignment = res.data;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取课程请求失败");
          });
        this.$axios
          .get("/notice/list",{
            params:{
              sectionID: localStorage.getItem("sectionID")
            }
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.assignment = res.data;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取公告请求失败");
          });
      },
      */
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
