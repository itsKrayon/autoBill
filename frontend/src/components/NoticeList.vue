<template>
    <el-container>
      <el-header class="nav_menu">
        <CourseSidebar  v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></CourseSidebar>
      </el-header>
      <el-header class="page_header">
        <div>
          <el-page-header @back="goBack" content="公告列表"></el-page-header>
        </div>
        <div class="button_in_row">
          <el-button
            type="primary"
            round
            icon="el-icon-plus"
            @click="noticeAddDialogVisible = true"
            v-if="userDetails.authorities[0].authority === 'Role_Teacher'"
          >发布公告
          </el-button>
        </div>
      </el-header>
      <el-main>

        <el-table
          :data="notices.filter(data => !search || data.title.toLowerCase().includes(search.toLowerCase()))"
        class="el-table">
          <el-table-column
            prop="title"
            label="标题">
          </el-table-column>
          <el-table-column
            prop="description"
            label="简介">
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="发布时间">
          </el-table-column>
          <el-table-column>
            <template slot="header" slot-scope="scope">
              <el-input
                v-model="search"
                size="large"
                prefix-icon="el-icon-search"
                clearable
                placeholder="请输入关键字搜索标题"/>
            </template>
            <template slot-scope="scope">
              <el-button @click="showNoticeDetail(scope.row)">查看详情</el-button>
            </template>

          </el-table-column>
        </el-table>

        <el-dialog
          title="发布公告"
          :visible.sync="noticeAddDialogVisible"
          width="50%"
          @close="resetForm('addForm')">
          <el-form :model="addForm" :rules="rules" ref="addForm">
            <el-form-item label="标题" prop="title">
              <el-input v-model="addForm.title"></el-input>
            </el-form-item>
            <el-form-item label="简介" prop="description">
              <el-input type="textarea" v-model="addForm.description"></el-input>
            </el-form-item>
            <el-form-item label="详细描述" prop="content">
              <el-input type="textarea" v-model="addForm.content"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmitAdd('addForm')">上传</el-button>
              <el-button @click="resetForm('addForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-dialog>

      </el-main>
    </el-container>
</template>

<script>
  import CourseSidebar from "./side/CoursesSidebar"
    export default {
        name: "NoticeList",
      inject: ["reload"],
      components: {
        CourseSidebar,
      },
      data(){
          return {
            notices: [],
            noticeAddDialogVisible: false,
            addForm: {
              title: '',
              description: '',
              content: '',
            },
            rules: {
              title: {required: true, message: '请输入标题', trigger: 'blur'}
            },
            search: '',
            userDetails: {},
          }
      },
      created() {
        this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
        localStorage.setItem("menuIndex", "2");
        const loading = this.$loading({
          lock: true,
        });
        // 页面加载时先获取课程公告列表
        this.$axios
          .get("/notice", {
            params: {
              sectionId: this.$route.params.sectionID,
            }
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.notices = res.data;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取公告列表请求失败");
          });
      },
      methods:{
        goBack() {
          this.$router.go(-1);
        },
        resetForm(formName) {
          this.$refs[formName].resetFields();
        },
        onSubmitAdd(formName) {
          this.$refs[formName].validate((valid) => {
            if(valid){
              const loading = this.$loading({
                lock: true,
              });
              this.$axios
                .post('/notice/add', {
                  title: this.addForm.title,
                  description: this.addForm.description,
                  content: this.addForm.content,
                  sectionId: this.$route.params.sectionID,
                })
                .then(res => {
                  loading.close();
                  if (res.status === 200) {
                    this.resetForm('addForm');
                    this.$message.success("发布成功");
                    this.reload();
                  }
                })
                .catch((error) => {
                  loading.close();
                  console.log(error);
                  this.$message.error("发布失败");
                });
            }
          })
        },
        showNoticeDetail(row){
          window.location.href = "/notice/"+row.id;
        }
      },
    }
</script>

<style scoped>

  .page_header {
    display: flex;
    justify-content: space-between;
    margin: 0 20px 0;
  }

  .el-table {
    margin-left: 20px;
    margin-right: 20px;
    padding-right: 40px;
  }

  .button_in_row {
    margin-top: -10px;
  }

  .nav_menu {
    margin-bottom: 40px;
    border-bottom: 1px solid #f0f0f0;
  }

</style>
