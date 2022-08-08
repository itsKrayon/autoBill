<template>
  <el-container>
    <el-header class="nav_menu">
      <CourseSidebar  v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></CourseSidebar>
    </el-header>
    <el-header class="page_header">
      <div>
        <el-page-header @back="goBack" content="作业详情"></el-page-header>
      </div>
      <div class="button_in_row">
        <el-button
          type="primary"
          round
          icon="el-icon-edit"
          @click="modifyAssignment()"
        >修改
        </el-button>
        <el-button
          type="danger"
          round
          icon="el-icon-delete"
          @click="deleteAssignment()"
        >删除
        </el-button>
      </div>
    </el-header>

    <el-main>
      <el-descriptions title="作业信息" class="el-descriptions" :column="3" border>
        <template slot="extra">
          <el-button @click="downloadAssignment()" round v-if="assignment.file !== ''">下载附件</el-button>
        </template>
        <el-descriptions-item label="标题" :span="3">{{assignment.title}}</el-descriptions-item>
        <el-descriptions-item label="发布时间" :span="1">{{assignment.createTime}}</el-descriptions-item>
        <el-descriptions-item label="截止时间" :span="1">{{assignment.deadline}}</el-descriptions-item>
        <el-descriptions-item label="状态" :span="1">
          <el-tag type="success" v-if="assignment.isExpired === 0">未截止</el-tag>
          <el-tag type="info" v-if="assignment.isExpired !== 0">已截止</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="详细描述" :span="3">{{assignment.description}}</el-descriptions-item>
      </el-descriptions>

      &nbsp;
      <el-descriptions title="提交情况" class="margin-top" :column="3">
      </el-descriptions>
      <el-table
        :data="assignment_works"
      class="el-table">
        <el-table-column
          prop="studentName"
          label="学生姓名">
        </el-table-column>
        <el-table-column
          label="状态">
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.status === 0">未批改</el-tag>
            <el-tag type="info" v-if="scope.row.status === 1">未提交</el-tag>
            <el-tag type="success" v-if="scope.row.status === 2">已批改</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="submitTime"
          label="提交时间">
        </el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <el-button @click="downloadWork(scope.row)" v-if="scope.row.status !== 1">下载</el-button>
            <el-button @click="checkWork(scope.row)" v-if="scope.row.status === 0">批改</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog
        title="修改作业"
        :visible.sync="assignmentModifyDialogVisible"
        width="50%"
        @close="resetForm('modifyForm','modifyUpload')">
        <el-form :model="modifyForm" :rules="rules" ref="modifyForm">
          <el-form-item label="标题" prop="title">
            <el-input v-model="modifyForm.title"></el-input>
          </el-form-item>
          <el-form-item label="详细描述" prop="description">
            <el-input type="textarea" v-model="modifyForm.description"></el-input>
          </el-form-item>
          <el-form-item label="截止日期" prop="deadline">
            <el-date-picker type="date" placeholder="选择日期" v-model="modifyForm.deadline" style="width: 100%;"
                            :picker-options="pickerOptions"></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-form-item label="文件上传：" prop="file">
              <el-upload
                ref="modifyUpload"
                action="#"
                :http-request="httpRequest"
                :on-exceed="handleExceed"
                :limit="1">
                <el-button size="small" type="primary">选择文件</el-button>
                &nbsp;<span slot="tip" class="el-upload__tip">
            只能上传1个文件，不上传视为不修改文件
          </span>
              </el-upload>
            </el-form-item>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmitModify('modifyForm')">提交</el-button>
            <el-button @click="clearModifyForm('modifyForm','modifyUpload')">清空</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

      <el-dialog
        title="添加评语"
        :visible.sync="checkAddDialogVisible"
        width="50%">
        <el-form :model="checkForm" :rules="rules" ref="checkForm">
          <el-form-item label="评语" prop="comment">
            <el-input v-model="checkForm.comment"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmitCheck('checkForm')">上传</el-button>
            <el-button @click="resetCheckForm('checkForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

    </el-main>
  </el-container>
</template>

<script>
  import CourseSidebar from "../side/CoursesSidebar.vue"
  export default {
    name: "TeacherAssignmentDetail",
    inject: ["reload", "dateConversion"],
    components: {
      CourseSidebar,
    },
    data() {
      return {
        assignment: {},
        assignment_works: [],
        assignmentModifyDialogVisible: false,
        modifyForm: {
          title: '',
          description: '',
          deadline: '',
        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() <= Date.now();
          },
        },
        checkForm: {
          comment: '',
        },
        checkAddDialogVisible: false,
        checkWorkID: '',
        fileList: [],
        rules: {
          comment: {required: true, message: '请输入评语', trigger: 'blur'}
        },
        userDetails: {},
      }
    },
    created() {
      this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
      const loading = this.$loading({
        lock: true,
      });
      // 页面加载时先获取作业详情
      this.$axios
        .get("/assignment/detail", {
          params: {
            assignmentId: this.$route.params.assignmentID,
          },
        })
        .then((res) => {
          console.log(res.data);
          this.assignment = res.data;
          this.$axios
            .get("/assignment/situation", {
              params: {
                assignmentId: this.$route.params.assignmentID,
              },
            })
            .then((res) => {
              loading.close();
              console.log(res.data);
              this.assignment_works = res.data;
            })
            .catch((error) => {
              loading.close();
              console.log(error);
              this.$message.error("获取作业提交情况请求失败");
            });
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          this.$message.error("获取作业详情请求失败");
        });
    },
    methods: {
      goBack() {
        this.$router.go(-1);
      },
      modifyAssignment() {
        this.modifyForm.title = this.assignment.title;
        this.modifyForm.description = this.assignment.description;
        this.modifyForm.deadline = this.assignment.deadline;
        this.assignmentModifyDialogVisible = true;
      },
      httpRequest(option) {
        this.fileList.push(option)
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      clearModifyForm(formName, uploadName) {
        this.modifyForm.title = '';
        this.modifyForm.description = '';
        this.modifyForm.deadline = '';
        //this.$refs[formName].resetFields();
        this.$refs[uploadName].clearFiles();
      },
      resetForm(formName, uploadName) {
        this.$refs[formName].resetFields();
        this.$refs[uploadName].clearFiles();
      },
      resetCheckForm(formName) {
        this.$refs[formName].resetFields();
      },
      onSubmitModify(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log(this.fileList.length);
            let params = new FormData();
            this.fileList.forEach((x) => {
              params.append('assignmentFile', x.file)
            });
            params.append("title", this.modifyForm.title);
            params.append("description", this.modifyForm.description);
            params.append("sectionId", this.assignment.sectionId);
            params.append("assignmentId", this.assignment.id);
            params.append("deadline", this.dateConversion(this.modifyForm.deadline));
            let config = {
              headers: {
                "Content-Type": "multipart/form-data",
              }
            };
            const loading = this.$loading({
              lock: true,
            });
            this.$axios
              .post('/assignment/edit', params, config)
              .then(res => {
                loading.close();
                if (res.status === 200) {
                  this.assignmentModifyDialogVisible = false;
                  this.resetForm('modifyForm', 'modifyUpload');
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
      deleteAssignment() {
        this.$confirm('此操作将永久删除该作业, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const loading = this.$loading({
            lock: true,
          });
          this.$axios
            .get("/assignment/delete", {
              params: {
                assignmentId: this.assignment.id,
              },
            })
            .then((res) => {
              loading.close();
              if (res.status === 200) {
                this.$message.success("删除成功");
                window.location.href = "/teacher_assignments/" + this.$route.params.sectionID;
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
      checkWork(row) {
        this.checkAddDialogVisible = true;
        this.checkWorkID = row.assignmentListId;
      },
      onSubmitCheck(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let param = new URLSearchParams();
            param.append("assignmentListId", this.checkWorkID);
            param.append("comment", this.checkForm.comment);
            this.$axios
              .post("/assignment/check", param)
              .then((res) => {
                if (res.status === 200) {
                  this.checkAddDialogVisible = false;
                  this.$message.success("批改成功");
                  this.reload();
                }
              })
              .catch((error) => {
                console.log(error);
                this.$message.error("批改失败");
              });
          }
        })
      },
      downloadAssignment() {
        const loading = this.$loading({
          lock: true,
        });
        //let currentTime = new Date().getTime();
        this.$axios
          .get("/assignment/enclosure", {
            params: {
              assignmentId: this.$route.params.assignmentID,
            },
            responseType: 'arraybuffer',
          })
          .then((res) => {
            loading.close();
            if (res.status === 200) {
              const data = res.data;
              const url = window.URL.createObjectURL(new Blob([data], {
                type: ""
              }));
              this.reload();
              const a = document.createElement('a');
              a.style.display = 'none';
              a.href = url;
              const fileName = res.headers['content-disposition'].split(';')[1].split('fileName=')[1];
              a.setAttribute('download', fileName);
              document.body.appendChild(a);
              a.click();
              document.body.removeChild(a);
            }
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("下载失败");
          });
      },
      downloadWork(row) {
        const loading = this.$loading({
          lock: true,
        });
        //let currentTime = new Date().getTime();
        this.$axios
          .get("/assignmentList", {
            params: {
              assignmentListId: row.assignmentListId,
            },
            responseType: 'arraybuffer',
          })
          .then((res) => {
            loading.close();
            if (res.status === 200) {
              const data = res.data;
              const url = window.URL.createObjectURL(new Blob([data], {
                type: ""
              }));
              this.reload();
              const a = document.createElement('a');
              a.style.display = 'none';
              a.href = url;
              const fileName = res.headers['content-disposition'].split(';')[1].split('fileName=')[1];
              a.setAttribute('download', fileName);
              document.body.appendChild(a);
              a.click();
              document.body.removeChild(a);
            }
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("下载失败");
          });
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

  .margin-top {
    margin-top: 10px;
  }


  .el-descriptions {
    margin-left: 20px;
    margin-right: 20px;
    padding-right: 40px;
    margin-top: 10px;
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
