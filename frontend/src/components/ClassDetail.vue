<template>
  <el-container>
    <el-header class="nav_menu">
      <user-sidebar v-if="readonly"></user-sidebar>
      <admin-sidebar v-else></admin-sidebar>
    </el-header>
    <el-header class="page_header">
      <el-page-header @back="goBack" content="班级成员列表"></el-page-header>
    </el-header>
    <el-main>
      <div class="row_wrapper">
        <div>
          <el-form :inline="true" ref="searchForm" :model="searchForm">
            <el-form-item prop="name">
              <el-input
                v-model="searchForm.name"
                placeholder="班级成员姓名"
              ></el-input>
            </el-form-item>
            <el-form-item prop="role">
              <el-select v-model="searchForm.role" placeholder="身份">
                <el-option label="教师" value="1"></el-option>
                <el-option label="学生" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button icon="el-icon-refresh" @click="resetForm('searchForm')"
                >重置</el-button
              >
              <el-button type="primary" icon="el-icon-search" @click="search"
                >查询</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="!readonly">
          <el-button
            type="primary"
            round
            icon="el-icon-plus"
            @click="beforeAddTeacher"
            >增加教师</el-button
          >
          <el-button-group class="button_group">
            <el-button
              type="primary"
              round
              icon="el-icon-s-management"
              @click="dialogTeacherFormVisible = true"
              >教师注册</el-button
            >
            <el-button
              type="primary"
              round
              @click="dialogStudentFormVisible = true"
              >学生注册<i class="el-icon-s-custom el-icon--right"></i
            ></el-button>
          </el-button-group>
        </div>
      </div>
      <div class="card_wrapper">
        <el-card v-for="user in teacher" :key="user.id" class="card_item">
          <div slot="header" class="card_header" @click="toUserDetail(user.id)">
            <div>{{ "用户名：" + user.username }}</div>
            <div>{{ "姓名：" + user.name }}</div>
            <div>身份：教师</div>
          </div>
          <div class="card_body">
            <div>
              <el-link
                icon="el-icon-tickets"
                type="primary"
                :underline="false"
                class="card_button"
                @click="toUserDetail(user.id)"
                >{{ readonly ? "查看信息" : "管理信息" }}</el-link
              >
            </div>
            <div>
              <el-link
                icon="el-icon-scissors"
                type="warning"
                :underline="false"
                v-if="!readonly"
                class="card_button"
                @click="removeTeacher(user.id)"
                >移除教师</el-link
              >
            </div>
            <div>
              <el-link
                icon="el-icon-delete"
                type="danger"
                :underline="false"
                v-if="!readonly"
                class="card_button"
                @click="deleteTeacher(user.id)"
                >注销教师</el-link
              >
            </div>
          </div>
        </el-card>
        <el-card v-for="user in student" :key="user.id" class="card_item">
          <div slot="header" class="card_header" @click="toUserDetail(user.id)">
            <div>{{ "用户名：" + user.username }}</div>
            <div>{{ "姓名：" + user.name }}</div>
            <div>身份：学生</div>
          </div>
          <div class="card_body">
            <div>
              <el-link
                icon="el-icon-tickets"
                type="primary"
                :underline="false"
                class="card_button"
                @click="toUserDetail(user.id)"
                >{{ readonly ? "查看信息" : "管理信息" }}</el-link
              >
            </div>
            <div>
              <el-link
                icon="el-icon-scissors"
                type="warning"
                :underline="false"
                v-if="!readonly"
                class="card_button"
                @click="beforeRemoveStudent(user.id)"
                >移动班级</el-link
              >
            </div>
            <div>
              <el-link
                icon="el-icon-delete"
                type="danger"
                :underline="false"
                v-if="!readonly"
                class="card_button"
                @click="deleteStudent(user.id)"
                >注销学生</el-link
              >
            </div>
          </div>
        </el-card>
      </div>
      <el-empty
        description="没有符合条件的班级成员"
        v-if="teacher.length === 0 && student.length === 0"
      ></el-empty>
      <el-dialog
        title="增加教师"
        :visible.sync="dialogTeacherTableVisible"
        width="40%"
      >
        <el-scrollbar
          style="height: 200px;"
          wrap-class="default-scrollbar__wrap"
          v-loading="infoLoading"
        >
          <div style="margin: 10px;">
            <el-table :data="teachers">
              <el-table-column property="id" label="ID"></el-table-column>
              <el-table-column
                property="username"
                label="用户名"
              ></el-table-column>
              <el-table-column property="name" label="姓名"></el-table-column>
              <el-table-column fixed="right" label="操作">
                <template slot-scope="scope">
                  <el-button
                    @click="addTeacher(scope.row)"
                    type="text"
                    size="small"
                    >添加到班级</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-scrollbar>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogTeacherTableVisible = false"
            >关 闭</el-button
          >
        </div>
      </el-dialog>
      <el-dialog
        title="注册老师"
        :visible.sync="dialogTeacherFormVisible"
        width="25%"
        center
        @close="resetForm('teacherForm')"
      >
        <el-form
          ref="teacherForm"
          :model="teacherForm"
          :rules="rules"
          label-width="80px"
          style="margin: 0 30px 0 20px;"
        >
          <el-form-item label="教师姓名" prop="name">
            <el-input
              v-model="teacherForm.name"
              placeholder="请输入教师姓名"
            ></el-input>
          </el-form-item>
          <el-form-item label="教师电话" prop="phone">
            <el-input
              v-model="teacherForm.phone"
              placeholder="请输入教师电话"
            ></el-input>
          </el-form-item>
          <el-form-item label="教师住址" prop="address">
            <el-input
              v-model="teacherForm.address"
              placeholder="请输入教师住址"
            ></el-input>
          </el-form-item>
          <el-form-item label="教师邮箱" prop="email">
            <el-input
              v-model="teacherForm.email"
              placeholder="请输入教师邮箱"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetForm('teacherForm')"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            :loading="buttonLoading"
            @click="registerTeacher"
            >提交</el-button
          >
        </div>
      </el-dialog>
      <el-dialog
        title="注册学生"
        :visible.sync="dialogStudentFormVisible"
        width="25%"
        center
        @close="resetForm('studentForm')"
      >
        <el-scrollbar
          style="height: 310px;"
          wrap-class="default-scrollbar__wrap"
        >
          <el-form
            ref="studentForm"
            :model="studentForm"
            :rules="rules"
            label-width="80px"
            style="margin: 0 30px 0 20px;"
          >
            <el-form-item label="学生姓名" prop="name">
              <el-input
                v-model="studentForm.name"
                placeholder="请输入学生姓名"
              ></el-input>
            </el-form-item>
            <el-form-item label="学生电话" prop="phone">
              <el-input
                v-model="studentForm.phone"
                placeholder="请输入学生电话"
              ></el-input>
            </el-form-item>
            <el-form-item label="学生住址" prop="address">
              <el-input
                v-model="studentForm.address"
                placeholder="请输入学生住址"
              ></el-input>
            </el-form-item>
            <el-form-item label="学生邮箱" prop="email">
              <el-input
                v-model="studentForm.email"
                placeholder="请输入学生邮箱"
              ></el-input>
            </el-form-item>
            <el-form-item label="家长姓名" prop="p_name">
              <el-input
                v-model="studentForm.p_name"
                placeholder="请输入家长姓名"
              ></el-input>
            </el-form-item>
            <el-form-item label="家长电话" prop="p_phone">
              <el-input
                v-model="studentForm.p_phone"
                placeholder="请输入家长电话"
              ></el-input>
            </el-form-item>
            <el-form-item label="家长住址" prop="p_address">
              <el-input
                v-model="studentForm.p_address"
                placeholder="请输入家长住址"
              ></el-input>
            </el-form-item>
            <el-form-item label="家长邮箱" prop="p_email">
              <el-input
                v-model="studentForm.p_email"
                placeholder="请输入家长邮箱"
              ></el-input>
            </el-form-item>
          </el-form>
        </el-scrollbar>

        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetForm('studentForm')"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            :loading="buttonLoading"
            @click="registerStudent"
            >提交</el-button
          >
        </div>
      </el-dialog>
      <el-dialog
        title="移动班级"
        :visible.sync="dialogClassTableVisible"
        width="40%"
      >
        <el-scrollbar
          style="height: 200px;"
          wrap-class="default-scrollbar__wrap"
          v-loading="infoLoading"
        >
          <div style="margin: 10px;">
            <el-table
              :data="classes.filter((v) => v.id != this.$route.params.id)"
            >
              <el-table-column property="id" label="ID"></el-table-column>
              <el-table-column property="grade" label="年级"></el-table-column>
              <el-table-column
                property="classNum"
                label="班级"
              ></el-table-column>
              <el-table-column fixed="right" label="操作">
                <template slot-scope="scope">
                  <el-button
                    @click="removeStudent(scope.row)"
                    type="text"
                    size="small"
                    >移动到班级</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-scrollbar>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogClassTableVisible = false">关 闭</el-button>
        </div>
      </el-dialog>
    </el-main>
  </el-container>
</template>
<script>
import AdminSidebar from "./side/AdminSidebar.vue";
import UserSidebar from "./side/UserSidebar.vue";
export default {
  name: "ClassDetail",
  inject: ["reload"],
  components: {
    UserSidebar,
    AdminSidebar,
  },
  data() {
    return {
      // 用户自身的权限
      authority: "admin",
      // 存储在localStorage中的用户详情，包括权限等信息
      userDetails: {},
      // 所有符合搜索条件的老师
      teacher: [],
      // 所有符合搜索条件的学生
      student: [],
      searchForm: {
        name: "",
        role: "",
      },
      dialogTeacherTableVisible: false,
      // 为班级新增教师时，需要的所有不在班级中的老师的数据
      teachers: [],
      // 注册新教师
      dialogTeacherFormVisible: false,
      teacherForm: {
        name: "",
        phone: "",
        address: "",
        email: "",
      },
      // 注册新学生
      dialogStudentFormVisible: false,
      studentForm: {
        name: "",
        phone: "",
        address: "",
        email: "",
        p_name: "",
        p_phone: "",
        p_address: "",
        p_email: "",
      },
      // 存在的所有班级列表
      dialogClassTableVisible: false,
      classes: [],
      // 需要移动班级的学生id
      studentToRemove: "",
      buttonLoading: false,
      infoLoading: false,
      rules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        phone: [
          {
            pattern: /(^(\d{3,4}-)?\d{7,8})$|(^0?(13[0-9]|15[012356789]|18[0-9]|14[57])[0-9]{8})$/,
            message: "电话格式不正确",
            trigger: "blur",
          },
        ],
        email: [
          {
            pattern: /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/,
            message: "邮箱格式不正确",
            trigger: "blur",
          },
        ],
        p_name: [
          { required: true, message: "请输入家长姓名", trigger: "blur" },
        ],
        p_phone: [
          {
            pattern: /(^(\d{3,4}-)?\d{7,8})$|(^0?(13[0-9]|15[012356789]|18[0-9]|14[57])[0-9]{8})$/,
            message: "家长电话格式不正确",
            trigger: "blur",
          },
        ],
        p_email: [
          {
            pattern: /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/,
            message: "家长邮箱格式不正确",
            trigger: "blur",
          },
        ],
      },
    };
  },
  computed: {
    readonly: function () {
      return this.userDetails.authorities[0].authority !== "Role_Admin";
    },
  },
  created() {
    this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
    this.readonly
      ? localStorage.setItem("menuIndex", "5")
      : localStorage.setItem("menuIndex", "4");
    const loading = this.$loading({
      lock: true,
    });
    // 页面加载时先获取班级成员列表（以班级id为条件搜索角色为老师和学生的用户）
    let param = new URLSearchParams();
    param.append("classID", this.$route.params.id);
    param.append("name", "");
    param.append("role", "");
    this.$axios
      .post("/user/search", param)
      .then((res) => {
        loading.close();
        console.log(res.data);
        this.student = res.data.student;
        this.teacher = res.data.teacher;
      })
      .catch((error) => {
        loading.close();
        console.log(error);
        this.$message.error("获取班级成员列表失败");
      });
  },
  methods: {
    getRoleText(type) {
      return type === "1" ? "老师" : "学生";
    },
    goBack() {
      this.$router.go(-1);
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    toUserDetail(userID) {
      this.$router.push(`/profile/${userID}`);
    },
    search() {
      const loading = this.$loading({
        lock: true,
      });
      let param = new URLSearchParams();
      param.append("classID", this.$route.params.id);
      param.append("name", this.searchForm.name);
      param.append("role", this.searchForm.role);
      this.$axios
        .post("/user/search", param)
        .then((res) => {
          console.log(res.data);
          this.teacher = [];
          this.student = [];
          if (res.data.teacher) {
            this.teacher = res.data.teacher;
          }
          if (res.data.student) {
            this.student = res.data.student;
          }
          loading.close();
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          this.$message.error("搜索班级成员失败");
        });
    },
    // 在点击增加教师按钮之后，显示弹窗之前
    beforeAddTeacher() {
      this.dialogTeacherTableVisible = true;
      this.infoLoading = true;
      let param = new URLSearchParams();
      param.append("classID", this.$route.params.id);
      this.$axios
        .post("/teacher/search", param)
        .then((res) => {
          this.infoLoading = false;
          console.log(res.data);
          this.teachers = res.data.teachers;
        })
        .catch((error) => {
          this.infoLoading = false;
          console.log(error);
          this.$message.error("获取教师列表失败");
        });
    },
    addTeacher(row) {
      const loading = this.$loading({
        lock: true,
      });
      let param = new URLSearchParams();
      param.append("classID", this.$route.params.id);
      param.append("teacherID", row.id);
      this.$axios
        .post("/teacher/add", param)
        .then((res) => {
          loading.close();
          if (res.status === 200) {
            this.$message({
              type: "success",
              message: "成功添加教师",
            });
            this.reload();
          }
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          if (error.response.status === 501) {
            this.$message.error(error.response.data.message);
          } else {
            this.$message.error("添加教师失败");
          }
        });
    },
    registerTeacher() {
      this.$refs["teacherForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("name", this.teacherForm.name);
          param.append("phone", this.teacherForm.phone);
          param.append("address", this.teacherForm.address);
          param.append("email", this.teacherForm.email);
          param.append("classID", this.$route.params.id);
          this.$axios
            .post("/teacher/register", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.$message({
                  type: "success",
                  message: "成功注册并添加教师",
                });
                this.reload();
              }
            })
            .catch((error) => {
              this.buttonLoading = false;
              console.log(error);
              if (error.response.status === 501) {
                this.$message.error(error.response.data.message);
              } else {
                this.$message.error("注册教师失败");
              }
            });
        }
      });
    },
    registerStudent() {
      this.$refs["studentForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("name", this.studentForm.name);
          param.append("phone", this.studentForm.phone);
          param.append("address", this.studentForm.address);
          param.append("email", this.studentForm.email);
          param.append("p_name", this.studentForm.p_name);
          param.append("p_phone", this.studentForm.p_phone);
          param.append("p_address", this.studentForm.p_address);
          param.append("p_email", this.studentForm.p_email);
          param.append("classID", this.$route.params.id);
          this.$axios
            .post("/student/register", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.$message({
                  type: "success",
                  message: "成功注册并添加学生",
                });
                this.reload();
              }
            })
            .catch((error) => {
              this.buttonLoading = false;
              console.log(error);
              if (error.response.status === 501) {
                this.$message.error(error.response.data.message);
              } else {
                this.$message.error("注册学生失败");
              }
            });
        }
      });
    },
    removeTeacher(teacherID) {
      this.$confirm("此操作将解除该教师与班级的教学关系, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const loading = this.$loading({
            lock: true,
          });
          this.$axios
            .get("/teacher/remove", {
              params: {
                classID: this.$route.params.id,
                teacherID: teacherID,
              },
            })
            .then((res) => {
              loading.close();
              if (res.status === 200) {
                this.$message({
                  type: "success",
                  message: "成功移除教师",
                });
                this.reload();
              }
            })
            .catch((error) => {
              loading.close();
              console.log(error);
              if (error.response.status === 501) {
                this.$message.error(error.response.data.message);
              } else {
                this.$message.error("移除教师失败");
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    deleteTeacher(teacherID) {
      this.$confirm(
        "此操作将注销该教师的账号，并且解除其与所有任教班级的教学关系, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          const loading = this.$loading({
            lock: true,
          });
          this.$axios
            .get("/teacher/delete", {
              params: {
                teacherID: teacherID,
              },
            })
            .then((res) => {
              loading.close();
              if (res.status === 200) {
                this.$message({
                  type: "success",
                  message: "成功注销教师",
                });
                this.reload();
              }
            })
            .catch((error) => {
              loading.close();
              console.log(error);
              if (error.response.status === 501) {
                this.$message.error(error.response.data.message);
              } else {
                this.$message.error("注销教师失败");
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    beforeRemoveStudent(studentID) {
      this.studentToRemove = studentID;
      this.infoLoading = true;
      this.$axios
        .get("/classes")
        .then((res) => {
          this.infoLoading = false;
          console.log(res.data);
          this.classes = res.data.classes;
        })
        .catch((error) => {
          this.infoLoading = false;
          console.log(error);
          this.$message.error("获取班级列表失败");
        });
      this.dialogClassTableVisible = true;
    },
    removeStudent(row) {
      const loading = this.$loading({
        lock: true,
      });
      this.$axios
        .get("/student/remove", {
          params: {
            studentID: this.studentToRemove,
            classID: row.id,
          },
        })
        .then((res) => {
          loading.close();
          if (res.status === 200) {
            this.$message({
              type: "success",
              message: "成功移动学生班级",
            });
            this.reload();
          }
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          if (error.response.status === 501) {
            this.$message.error(error.response.data.message);
          } else {
            this.$message.error("移动学生班级失败");
          }
        });
    },
    deleteStudent(studentID) {
      this.$confirm("此操作将注销该学生的账号，是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const loading = this.$loading({
            lock: true,
          });
          this.$axios
            .get("/student/delete", {
              params: {
                studentID: studentID,
              },
            })
            .then((res) => {
              loading.close();
              if (res.status === 200) {
                this.$message({
                  type: "success",
                  message: "成功注销学生",
                });
                this.reload();
              }
            })
            .catch((error) => {
              loading.close();
              console.log(error);
              if (error.response.status === 501) {
                this.$message.error(error.response.data.message);
              } else {
                this.$message.error("注销学生请求失败");
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
  },
};
</script>
<style scoped>
.nav_menu {
  margin-bottom: 50px;
  border-bottom: 1px solid #f0f0f0;
}
.page_header {
  margin: 0 20px 0;
}
.row_wrapper {
  display: flex;
  justify-content: space-between;
  margin: -20px 20px 0;
}
.button_group {
  margin-left: 10px;
}
.card_wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
}
.card_item {
  flex: 1;
  min-width: calc((100% - 170px) / 4);
  max-width: calc((100% - 170px) / 4);
  margin: 20px;
}
.card_item:hover {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  /* cursor: pointer; */
}
.card_header {
  text-align: left;
}
.card_header:hover {
  cursor: pointer;
}
.card_header div {
  margin: 10px;
}
.card_body {
  display: flex;
  justify-content: end;
}
.card_button {
  margin-left: 10px;
}
</style>
