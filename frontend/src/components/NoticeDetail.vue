<template>
<el-container>
  <el-header class="nav_menu">
    <CourseSidebar  v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></CourseSidebar>
  </el-header>
  <el-header class="page_header">
    <div>
      <el-page-header @back="goBack" content="公告详情"></el-page-header>
    </div>
    <div class="button_in_row">
      <el-button
        type="primary"
        round
        icon="el-icon-edit"
        @click="modifyNotice()"
        v-if="userDetails.authorities[0].authority === 'Role_Teacher'"
      >修改
      </el-button>
      <el-button
        type="danger"
        round
        icon="el-icon-delete"
        @click="deleteNotice()"
        v-if="userDetails.authorities[0].authority === 'Role_Teacher'"
      >删除
      </el-button>
    </div>
  </el-header>

  <el-main>

      <el-descriptions class="el-descriptions" title="公告信息" :column="2" border>
        <el-descriptions-item label="标题" :span="1" class="el-descriptions-item">{{notice.title}}</el-descriptions-item>
        <el-descriptions-item label="发布时间" :span="1" class="el-descriptions-item">{{notice.createTime}}</el-descriptions-item>
        <el-descriptions-item label="简介" :span="2" class="el-descriptions-item">{{notice.description}}</el-descriptions-item>
        <el-descriptions-item label="详细描述" :span="2" class="el-descriptions-item">{{notice.content}}</el-descriptions-item>
      </el-descriptions>

    <el-dialog
      title="修改公告"
      :visible.sync="noticeModifyDialogVisible"
      width="50%"
      @close="resetForm('modifyForm')">
      <el-form :model="modifyForm" :rules="rules" ref="modifyForm">
        <el-form-item label="标题" prop="title">
          <el-input v-model="modifyForm.title"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input type="textarea" v-model="modifyForm.description"></el-input>
        </el-form-item>
        <el-form-item label="详细描述" prop="content">
          <el-input type="textarea" v-model="modifyForm.content"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmitModify('modifyForm')">上传</el-button>
          <el-button @click="clearModifyForm()">清空</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </el-main>

</el-container>
</template>

<script>
  import CourseSidebar from "./side/CoursesSidebar"
    export default {
        name: "NoticeDetail",
      inject: ["reload"],
      components: {
        CourseSidebar,
      },
      data(){
          return{
            notice: {},
            noticeModifyDialogVisible: false,
            modifyForm: {
              title: '',
              description: '',
              content: '',
            },
            rules: {
              title: {required: true, message: '请输入标题', trigger: 'blur'}
            },
            userDetails: {},
          }
      },
      created(){
        this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
        localStorage.setItem("menuIndex", "2");
        const loading = this.$loading({
          lock: true,
        });
        // 页面加载时先获取作业详情
        this.$axios
          .get("/notice/detail", {
            params: {
              noticeId: this.$route.params.id,
            },
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.notice = res.data;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取公告详情请求失败");
          });
      },
      methods: {
        goBack() {
          this.$router.go(-1);
        },
        deleteNotice() {
          this.$confirm('此操作将永久删除该公告, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            const loading = this.$loading({
              lock: true,
            });
            this.$axios
              .get("/notice/delete", {
                params: {
                  noticeId: this.$route.params.id,
                },
              })
              .then((res) => {
                loading.close();
                if (res.status === 200) {
                  this.$message.success("删除成功");
                  window.location.href = "/notices/" + this.notice.sectionId;
                }
              })
              .catch((error) => {
                loading.close();
                console.log(error);
                this.$message.error("删除失败");
              });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        },
        modifyNotice(){
          this.modifyForm.title = this.notice.title;
          this.modifyForm.description = this.notice.description;
          this.modifyForm.content = this.notice.content;
          this.noticeModifyDialogVisible = true;
        },
        resetForm(formName) {
          this.$refs[formName].resetFields();
        },
        clearModifyForm(){
          this.modifyForm.title = '';
          this.modifyForm.description = '';
          this.modifyForm.content = '';
        },
        onSubmitModify(formName) {
          this.$refs[formName].validate((valid) => {
            if(valid){
              const loading = this.$loading({
                lock: true,
              });
              this.$axios
                .post('/notice/edit', {
                  id: this.$route.params.id,
                  title: this.modifyForm.title,
                  description: this.modifyForm.description,
                  content: this.modifyForm.content,
                  sectionId: this.notice.sectionId,
                })
                .then(res => {
                  loading.close();
                  if (res.status === 200) {
                    this.clearModifyForm();
                    this.$message.success("修改成功");
                    this.reload();
                  }
                })
                .catch((error) => {
                  loading.close();
                  console.log(error);
                  this.$message.error("修改失败");
                });
            }
          })
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

  .el-descriptions {
    margin-left: 60px;
    margin-right: 20px;
    padding-right: 60px;
  }

  .nav_menu {
    margin-bottom: 40px;
    border-bottom: 1px solid #f0f0f0;
  }
</style>
