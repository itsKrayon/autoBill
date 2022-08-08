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
          @click="assignmentSubmitDialogVisible = true"
          v-if="userDetails.authorities[0].authority === 'Role_Student' &&assignment.status === 1 && assignment.isExpired === 0"
        >提交
        </el-button>
      </div>
    </el-header>

    <el-main>
      <el-descriptions title="作业信息" class="el-descriptions" :column="2" border>
        <template slot="extra">
          <el-button @click="downloadAssignment()" round v-if="assignment.file !== ''">下载附件</el-button>
        </template>
        <el-descriptions-item label="标题" :span="2">{{assignment.title}}</el-descriptions-item>
        <el-descriptions-item label="截止时间" :span="1">{{assignment.deadline}}</el-descriptions-item>
        <el-descriptions-item label="状态" :span="1">
          <el-tag type="danger" v-if="assignment.status === 0">未批改</el-tag>
          <el-tag type="info" v-if="assignment.status === 1">未提交</el-tag>
          <el-tag type="success" v-if="assignment.status === 2">已批改</el-tag>
          <el-tag type="success" v-if="assignment.isExpired === 0">未截止</el-tag>
          <el-tag type="info" v-if="assignment.isExpired !== 0">已截止</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间" :span="1" v-if="assignment.status !== 1">{{assignment.submitDatetime}}</el-descriptions-item>
        <el-descriptions-item label="已提交作业" :span="1" v-if="assignment.status !== 1">
          <el-button @click="downloadWork()" round size="small">点击下载</el-button>
        </el-descriptions-item>
        <el-descriptions-item label="详细描述" :span="2">{{assignment.description}}</el-descriptions-item>
        <el-descriptions-item label="评语" :span="2" v-if="assignment.status === 2">{{assignment.comment}}</el-descriptions-item>
      </el-descriptions>

      <el-dialog
        title="提交作业"
        :visible.sync="assignmentSubmitDialogVisible"
        width="50%"
        @close="resetForm('submitForm','submitUpload')">
        <el-form ref="submitForm">
          <el-form-item>
            <el-form-item label="文件上传：" prop="file">
              <el-upload
                ref="submitUpload"
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
            <el-button type="primary" @click="onSubmit('submitForm')">上传</el-button>
            <el-button @click="resetForm('submitForm','submitUpload')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </el-main>
  </el-container>

</template>

<script>
  import CourseSidebar from "../side/CoursesSidebar.vue"
    export default {
        name: "StudentAssignmentDetail",
      inject: ["reload"],
      components: {
        CourseSidebar,
      },
      data(){
          return{
            assignment: {},
            assignmentSubmitDialogVisible: false,
            fileList: [],
            userDetails: {},
          }
      },
      created(){
        this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
        const loading = this.$loading({
          lock: true,
        });
        // 页面加载时先获取作业详情
        this.$axios
          .get("/assignmentList/detail", {
            params: {
              assignmentListId: this.$route.params.assignmentListID,
            },
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.assignment = res.data;
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
        onSubmit() {
          console.log(this.fileList.length);
          if(this.fileList.length === 1){
            let params = new FormData();
            this.fileList.forEach((x) => {
              params.append('file', x.file)
            });
            params.append("assignmentListId", this.$route.params.assignmentListID);
            let config = {
              headers: {
                "Content-Type": "multipart/form-data",
              }
            };
            const loading = this.$loading({
              lock: true,
            });
            this.$axios
              .post('/assignment/submit', params, config)
              .then(res => {
                loading.close();
                if (res.status === 200) {
                  this.assignmentSubmitDialogVisible = false;
                  this.resetForm('submitForm','submitUpload');
                  this.$message.success("提交成功");
                  this.reload();
                }
              })
              .catch((error) => {
                loading.close();
                console.log(error);
                this.$message.error("发布失败");
              });
          }else{
            this.$message.info("请选择一个文件");
          }
        },
        downloadAssignment(){
          const loading = this.$loading({
            lock: true,
          });
          //let currentTime = new Date().getTime();
          this.$axios
            .get("/assignment/enclosure", {
              params: {
                assignmentId: this.assignment.assignmentId,
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
        downloadWork(){
          const loading = this.$loading({
            lock: true,
          });
          //let currentTime = new Date().getTime();
          this.$axios
            .get("/assignmentList", {
              params: {
                assignmentListId: this.$route.params.assignmentListID,
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

  .el-descriptions {
    margin-left: 20px;
    margin-right: 20px;
    padding-right: 40px;
    margin-top: 10px;
  }


  .nav_menu {
    margin-bottom: 40px;
    border-bottom: 1px solid #f0f0f0;
  }
</style>
