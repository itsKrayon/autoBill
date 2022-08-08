<template>
  <el-container>
    <el-header class="nav_menu">
      <CourseSidebar  v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></CourseSidebar>
    </el-header>
    <el-header class="page_header">
      <div>
        <el-page-header @back="goBack" content="作业列表"></el-page-header>
      </div>
      <div class="button_in_row">
        <el-button
          type="primary"
          round
          icon="el-icon-plus"
          @click="assignmentAddDialogVisible = true"
        >发布作业
        </el-button>
      </div>
    </el-header>
    <el-main>
      <el-table
        :data="assignments"
      class="el-table">
        <el-table-column
          prop="title"
          label="标题">
        </el-table-column>
        <el-table-column
          prop="creatorName"
          label="发布人">
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="发布时间">
        </el-table-column>
        <el-table-column
          prop="deadline"
          label="截止时间">
        </el-table-column>
        <el-table-column
          label="状态">
          <template slot-scope="scope">
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

      <el-dialog
        title="发布作业"
        :visible.sync="assignmentAddDialogVisible"
        width="50%"
        @close="resetForm('addForm','addUpload')">
        <el-form :model="addForm" :rules="rules" ref="addForm">
          <el-form-item label="标题" prop="title">
            <el-input v-model="addForm.title"></el-input>
          </el-form-item>
          <el-form-item label="详细描述" prop="description">
            <el-input type="textarea" v-model="addForm.description"></el-input>
          </el-form-item>
          <el-form-item label="截止日期" prop="deadline">
            <el-date-picker type="date" placeholder="选择日期" v-model="addForm.deadline" style="width: 100%;"
                            :picker-options="pickerOptions"></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-form-item label="文件上传：" prop="file">
              <el-upload
                ref="addUpload"
                action="#"
                :http-request="httpRequest"
                :on-exceed="handleExceed"
                :limit="1">
                <el-button size="small" type="primary">选择文件</el-button>
                &nbsp;<span slot="tip" class="el-upload__tip">
            只能上传1个文件
          </span>
              </el-upload>
            </el-form-item>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmitAdd('addForm')">上传</el-button>
            <el-button @click="resetForm('addForm','addUpload')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
  import CourseSidebar from "../side/CoursesSidebar.vue"
  export default {
    name: "TeacherAssignmentList",
    inject: ["reload", "dateConversion"],
    components: {
      CourseSidebar,
    },
    data() {
      return {
        assignments: [],
        assignmentAddDialogVisible: false,
        addForm: {
          title: '',
          description: '',
          deadline: '',
        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() <= Date.now();
          },
        },
        fileList: [],
        rules: {
          title: {required: true, message: '请输入标题', trigger: 'blur'}
        },
        userDetails: {},
      }
    },
    created() {
      this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
      localStorage.setItem("menuIndex", "2");
      const loading = this.$loading({
        lock: true,
      });
      // 页面加载时先获取作业列表
      this.$axios
        .get("/assignment", {
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
      httpRequest(option) {
        this.fileList.push(option)
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      resetForm(formName, uploadName) {
        this.$refs[formName].resetFields();
        this.$refs[uploadName].clearFiles();
      },
      onSubmitAdd(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log(this.fileList.length);
            let params = new FormData();
            this.fileList.forEach((x) => {
              params.append('assignmentFile', x.file)
            });
            params.append("title", this.addForm.title);
            params.append("description", this.addForm.description);
            params.append("deadline", this.dateConversion(this.addForm.deadline));
            params.append("sectionId", this.$route.params.sectionID);
            let config = {
              headers: {
                "Content-Type": "multipart/form-data",
              }
            };
            const loading = this.$loading({
              lock: true,
            });
            this.$axios
              .post('/assignment/add', params, config)
              .then(res => {
                loading.close();
                if (res.status === 200) {
                  this.assignmentAddDialogVisible = false;
                  this.resetForm('addForm', 'addUpload');
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
      showAssignmentDetail(row) {
        window.location.href = "/teacher_assignment/" + this.$route.params.sectionID + "/" + row.id;
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
