<template>
<el-container>
  <el-header class="nav_menu">
    <CourseSidebar  v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></CourseSidebar>
  </el-header>
  <el-header class="page_header">
    <div>
      <el-page-header @back="goBack" content="作业列表"></el-page-header>
    </div>
  </el-header>

  <el-main>

    <el-table
      :data="assignments" class="el-table">
      <el-table-column
        prop="title"
        label="标题">
      </el-table-column>
      <el-table-column
        prop="deadline"
        label="截止时间">
      </el-table-column>
      <el-table-column
        prop="submitDatetime"
        label="提交时间">
      </el-table-column>
      <el-table-column
        label="状态">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.status === 0">未批改</el-tag>
          <el-tag type="info" v-if="scope.row.status === 1">未提交</el-tag>
          <el-tag type="success" v-if="scope.row.status === 2">已批改</el-tag>
          <el-tag type="success" v-if="scope.row.isExpired === 0">未截止</el-tag>
          <el-tag type="info" v-if="scope.row.isExpired !== 0">已截止</el-tag>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button @click="showAssignmentDetail(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>

  </el-main>

</el-container>
</template>

<script>
  import CourseSidebar from "../side/CoursesSidebar.vue"
    export default {
        name: "StudentAssignmentList",
      inject: ["reload"],
      components: {
        CourseSidebar,
      },
      data(){
          return {
            assignments: [],
            userDetails: {},
          }
      },
      created(){
        this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
        const loading = this.$loading({
          lock: true,
        });
        // 页面加载时先获取作业列表
        this.$axios
          .get("/assignment/myAssignment", {
            params: {
              sectionId: this.$route.params.sectionID,
            },
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.assignments = res.data;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取作业列表请求失败");
          });
      },
      methods: {
        goBack() {
          this.$router.go(-1);
        },
        showAssignmentDetail(row){
          window.location.href = "/student_assignment/"+this.$route.params.sectionID+"/"+row.id;
        },
      }
    }
</script>

<style scoped>


  .page_header {
    display: flex;
    justify-content: space-between;
    margin: 0 20px 0;
  }

  .button_in_row {
    margin-top: -10px;
  }

  .el-table {
    margin-left: 20px;
    margin-right: 20px;
    padding-right: 40px;
  }

  .nav_menu {
    margin-bottom: 40px;
    border-bottom: 1px solid #f0f0f0;
  }

</style>
