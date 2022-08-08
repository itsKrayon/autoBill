<template>
  <el-container>
    <el-header class="nav_menu">
      <CourseSidebar  v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></CourseSidebar>
    </el-header>
    <el-header class="page_header">
      <div>
        <el-page-header @back="goBack" content="资源列表"></el-page-header>
      </div>
      <div class="button_in_row">
        <el-button
          v-if="userDetails.authorities[0].authority === 'Role_Teacher'"
          type="primary"
          round
          icon="el-icon-plus"
          @click="resourceAddDialogVisible = true"
        >上传资源
        </el-button>
      </div>
    </el-header>
    <el-main>
      <el-table
        :data="resources"
      class="el-table">
        <el-table-column
          prop="title"
          label="标题">
        </el-table-column>
        <el-table-column
          prop="creatorName"
          label="上传人">
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="上传时间">
        </el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <el-button @click="showResourceDetail(scope.row)">查看详情</el-button>
            <el-button @click="downloadResource(scope.row)">下载</el-button>
            <el-button @click="modifyResource(scope.row)" v-if="userDetails.authorities[0].authority === 'Role_Teacher'">修改</el-button>
            <el-button @click="withdrawResource(scope.row)" v-if="userDetails.authorities[0].authority === 'Role_Teacher'">删除</el-button>
          </template>

        </el-table-column>
      </el-table>

      <el-dialog
        title="资源详情"
        :visible.sync="resourceDetailDialogVisible"
        width="50%"
      >
        <el-descriptions class="margin-top" :column="3" border>
          <template slot="extra">
            <el-button type="primary" @click="downloadResource(resourceDetail)">下载</el-button>
            <el-button @click="modifyResource(resourceDetail)" v-if="userDetails.authorities[0].authority === 'Role_Teacher'">修改</el-button>
            <el-button @click="withdrawResource(resourceDetail)" v-if="userDetails.authorities[0].authority === 'Role_Teacher'">删除</el-button>
          </template>
          <el-descriptions-item :span="1">
            <template slot="label">
              资源编号
            </template>
            {{resourceDetail.id}}
          </el-descriptions-item>
          <el-descriptions-item :span="2">
            <template slot="label">
              标题
            </template>
            {{resourceDetail.title}}
          </el-descriptions-item>
          <el-descriptions-item :span="3">
            <template slot="label">
              详细描述
            </template>
            {{resourceDetail.description}}
          </el-descriptions-item>
          <!--<el-descriptions-item :span="3">-->
          <!--<template slot="label">-->
          <!--文件路径-->
          <!--</template>-->
          <!--{{resourceDetail.file}}-->
          <!--</el-descriptions-item>-->
          <el-descriptions-item :span="1">
            <template slot="label">
              上传人
            </template>
            {{resourceDetail.creatorName}}
          </el-descriptions-item>
          <el-descriptions-item :span="1">
            <template slot="label">
              上传时间
            </template>
            {{resourceDetail.createTime}}
          </el-descriptions-item>
          <el-descriptions-item :span="1">
            <template slot="label">
              下载次数
            </template>
            {{Math.floor(resourceDetail.downloads/2)}}
          </el-descriptions-item>


        </el-descriptions>
      </el-dialog>

      <el-dialog
        title="上传资源"
        :visible.sync="resourceAddDialogVisible"
        width="50%"
        @close="resetForm('addForm','addUpload')">
        <el-form :model="addForm" :rules="rules" ref="addForm">
          <el-form-item label="标题" prop="title">
            <el-input v-model="addForm.title"></el-input>
          </el-form-item>
          <el-form-item label="详细描述" prop="description">
            <el-input type="textarea" v-model="addForm.description"></el-input>
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

      <el-dialog
        title="修改资源"
        :visible.sync="resourceModifyDialogVisible"
        width="50%"
        @close="resetForm('modifyForm','modifyUpload')">
        <el-form :model="modifyForm" :rules="rules" ref="modifyForm">
          <el-form-item label="标题" prop="title">
            <el-input v-model="modifyForm.title"></el-input>
          </el-form-item>
          <el-form-item label="详细描述" prop="description">
            <el-input type="textarea" v-model="modifyForm.description"></el-input>
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
            <el-button @click="resetModifyForm('modifyForm','modifyUpload')">清空</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

    </el-main>
  </el-container>
</template>

<script>
  import CourseSidebar from "./side/CoursesSidebar"
  export default {
    name: "ResourceList",
    inject: ["reload"],
    components: {
      CourseSidebar,
    },
    data() {
      return {
        resources: [],
        resourceDetailDialogVisible: false,
        resourceDetail: {},
        resourceAddDialogVisible: false,
        addForm: {
          title: '',
          description: '',
        },
        fileList: [],
        rules: {
          title: {required: true, message: '请输入标题', trigger: 'blur'}
        },
        resourceModifyDialogVisible: false,
        modifyForm: {
          title: '',
          description: '',
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
      // 页面加载时先获取资源列表
      this.$axios
        .get("/resource", {
          params: {
            sectionId: this.$route.params.sectionID,
          },
        })
        .then((res) => {
          loading.close();
          console.log(res.data);
          this.resources = res.data;
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          this.$message.error("获取资源列表请求失败");
        });
    },
    methods: {
      goBack() {
        this.$router.go(-1);
      },
      showResourceDetail(row) {
        this.resourceDetail = row;
        this.resourceDetailDialogVisible = true;
      },
      modifyResource(row) {
        this.resourceDetail = row;
        this.modifyForm.title = row.title;
        this.modifyForm.description = row.description;
        this.resourceDetailDialogVisible = false;
        this.resourceModifyDialogVisible = true;
      },
      downloadResource(row) {
        const loading = this.$loading({
          lock: true,
        });
        //let currentTime = new Date().getTime();
        this.$axios
          .get("/resource/download", {
            params: {
              resourceId: row.id,
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
              this.resourceDetailDialogVisible = false;
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
      withdrawResource(row) {
        const loading = this.$loading({
          lock: true,
        });
        this.$axios
          .get("/resource/withdraw", {
            params: {
              resourceId: row.id,
            },
          })
          .then((res) => {
            loading.close();
            if (res.status === 200) {
              this.$message.success("删除成功");
              this.resourceDetailDialogVisible = false;
              this.reload();
            }
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("删除失败");
          });
      },
      resetForm(formName, uploadName) {
        this.$refs[formName].resetFields();
        this.$refs[uploadName].clearFiles();
      },
      resetModifyForm(formName, uploadName) {
        this.modifyForm.title = '';
        this.modifyForm.description = '';
        //this.$refs[formName].resetFields();
        this.$refs[uploadName].clearFiles();
      },
      httpRequest(option) {
        this.fileList.push(option)
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      onSubmitAdd(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log(this.fileList.length);
            if (this.fileList.length === 1) {
              let params = new FormData();
              this.fileList.forEach((x) => {
                params.append('file', x.file)
              });
              params.append("title", this.addForm.title);
              params.append("description", this.addForm.description);
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
                .post('/resource/upload', params, config)
                .then(res => {
                  loading.close();
                  if (res.status === 200) {
                    this.resourceAddDialogVisible = false;
                    this.resetForm('addForm', 'addUpload');
                    this.$message.success("上传成功");
                    this.reload();
                  }
                })
                .catch((error) => {
                  loading.close();
                  console.log(error);
                  this.$message.error("上传失败");
                });
            } else {
              this.$message.info("请选择一个文件");
            }
          }
        })
      },
      onSubmitModify() {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log(this.fileList.length);
            let params = new FormData();
            this.fileList.forEach((x) => {
              params.append('file', x.file)
            });
            params.append("title", this.modifyForm.title);
            params.append("description", this.modifyForm.description);
            params.append("sectionId", this.$route.params.sectionID);
            params.append("resourceId", this.resourceDetail.id);
            params.append("downloads", this.resourceDetail.downloads);
            let config = {
              headers: {
                "Content-Type": "multipart/form-data",
              }
            };
            const loading = this.$loading({
              lock: true,
            });
            this.$axios
              .post('/resource/modify', params, config)
              .then(res => {
                loading.close();
                if (res.status === 200) {
                  this.resourceModifyDialogVisible = false;
                  this.resetModifyForm('modifyForm', 'modifyUpload');
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
