<template>
  <el-container>
    <el-header class="nav_menu">
      <admin-sidebar v-if="isAdmin"></admin-sidebar>
      <user-sidebar v-else></user-sidebar>
    </el-header>
    <el-header class="page_header">
      <el-page-header @back="goBack" content="用户详情页"></el-page-header>
    </el-header>
    <el-main>
      <el-card class="info_wrapper">
        <el-empty
          description="暂无用户信息"
          v-if="userAuthority === ''"
        ></el-empty>
        <el-descriptions
          title="教师信息"
          :column="4"
          direction="vertical"
          border
          v-if="isUserTeacher"
        >
          <template slot="extra">
            <el-button
              type="primary"
              size="small"
              icon="el-icon-edit"
              @click="beforeEditUserInfo('teacher')"
              round
              v-if="isAdmin || isOwner"
              >维护教师信息</el-button
            >
            <el-button
              type="primary"
              size="small"
              icon="el-icon-edit-outline"
              @click="beforeChangePassword"
              round
              v-if="isOwner"
              >修改密码</el-button
            >
          </template>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-postcard"></i>
              id
            </template>
            {{ this.teacher.id }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-user"></i>
              用户名
            </template>
            {{ this.teacher.username }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-document"></i>
              姓名 </template
            >{{ this.teacher.name }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-mobile-phone"></i>
              联系方式 </template
            >{{
              this.teacher.phone ? this.teacher.phone : "暂无"
            }}</el-descriptions-item
          >
          <el-descriptions-item :span="2">
            <template slot="label">
              <i class="el-icon-location-outline"></i>
              地址
            </template>
            {{
              this.teacher.address ? this.teacher.address : "暂无"
            }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-message"></i>
              邮箱 </template
            >{{
              this.teacher.email ? this.teacher.email : "暂无"
            }}</el-descriptions-item
          >
          <el-descriptions-item :span="2">
            <template slot="label">
              <i class="el-icon-notebook-1"></i>
              任教班级 </template
            >{{
              this.teacher.class ? this.teacher.class : "暂无"
            }}</el-descriptions-item
          >
        </el-descriptions>
        <el-descriptions
          title="学生信息"
          :column="4"
          direction="vertical"
          border
          v-if="isUserStudent || isUserParent"
        >
          <template slot="extra">
            <el-button
              type="primary"
              size="small"
              icon="el-icon-edit"
              @click="beforeEditUserInfo('student')"
              round
              v-if="isAdmin || isOwner"
              >维护学生信息</el-button
            >
            <el-button
              type="primary"
              size="small"
              icon="el-icon-edit-outline"
              @click="beforeChangePassword"
              round
              v-if="isOwner && isUserStudent"
              >修改密码</el-button
            >
          </template>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-postcard"></i>
              id
            </template>
            {{ this.student.id }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-user"></i>
              用户名
            </template>
            {{ this.student.username }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-document"></i>
              姓名 </template
            >{{ this.student.name }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-mobile-phone"></i>
              联系方式 </template
            >{{
              this.student.phone ? this.student.phone : "暂无"
            }}</el-descriptions-item
          >
          <el-descriptions-item :span="2">
            <template slot="label">
              <i class="el-icon-location-outline"></i>
              地址
            </template>
            {{
              this.student.address ? this.student.address : "暂无"
            }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-message"></i>
              邮箱 </template
            >{{
              this.student.email ? this.student.email : "暂无"
            }}</el-descriptions-item
          >
          <el-descriptions-item :span="2">
            <template slot="label">
              <i class="el-icon-notebook-1"></i>
              所在班级 </template
            >{{
              this.student.class ? this.student.class : "暂无"
            }}</el-descriptions-item
          >
        </el-descriptions>
        <el-divider v-if="isUserParent || isUserStudent"></el-divider>
        <el-descriptions
          title="家长信息"
          :column="4"
          direction="vertical"
          border
          v-if="isUserParent || isUserStudent"
        >
          <template slot="extra">
            <el-button
              type="primary"
              size="small"
              icon="el-icon-edit"
              @click="beforeEditUserInfo('parent')"
              round
              v-if="isAdmin || isOwner"
              >维护家长信息</el-button
            >
            <el-button
              type="primary"
              size="small"
              icon="el-icon-edit-outline"
              @click="beforeChangePassword"
              round
              v-if="isOwner && isUserParent"
              >修改密码</el-button
            >
          </template>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-postcard"></i>
              id
            </template>
            {{ this.parent.id }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-user"></i>
              用户名
            </template>
            {{ this.parent.username }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-document"></i>
              姓名 </template
            >{{ this.parent.name }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-mobile-phone"></i>
              联系方式 </template
            >{{
              this.parent.phone ? this.parent.phone : "暂无"
            }}</el-descriptions-item
          >
          <el-descriptions-item :span="2">
            <template slot="label">
              <i class="el-icon-location-outline"></i>
              地址
            </template>
            {{
              this.parent.address ? this.parent.address : "暂无"
            }}</el-descriptions-item
          >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-message"></i>
              邮箱 </template
            >{{
              this.parent.email ? this.parent.email : "暂无"
            }}</el-descriptions-item
          >
        </el-descriptions>
      </el-card>
      <el-dialog
        title="修改密码"
        :visible.sync="dialogPasswordFormVisible"
        width="25%"
        center
      >
        <el-form
          ref="passwordForm"
          :model="passwordForm"
          :rules="rules"
          label-width="80px"
          label-position="left"
          style="margin: 0 30px 0 10px;"
        >
          <el-form-item label="原密码" prop="oldPass">
            <el-input :type="password" v-model="passwordForm.oldPass" disabled>
              <i slot="suffix" :class="icon" @click="showPass"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPass">
            <el-input
              type="password"
              v-model="passwordForm.newPass"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input
              type="password"
              v-model="passwordForm.checkPass"
              placeholder="请重复密码"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetPasswordForm"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            @click="changePassword"
            :loading="buttonLoading"
            >提交</el-button
          >
        </div>
      </el-dialog>
      <el-dialog
        title="维护用户信息"
        :visible.sync="dialogInfoFormVisible"
        width="35%"
        center
      >
        <el-form
          ref="infoForm"
          :model="infoForm"
          :rules="rules"
          label-width="100px"
          label-position="right"
          style="margin: 0 30px 0 10px;"
        >
          <el-form-item label="id" prop="id" required>
            <el-input v-model="infoForm.id" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="username" required>
            <el-input v-model="infoForm.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="infoForm.name" :disabled="!isAdmin"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input v-model="infoForm.phone"></el-input>
          </el-form-item>
          <el-form-item label="住址" prop="address">
            <el-input v-model="infoForm.address"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="infoForm.email"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetInfoForm"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            @click="editUserInfo"
            :disabled="!infoFormChanged"
            :loading="buttonLoading"
            >提交</el-button
          >
        </div>
      </el-dialog>
    </el-main>
  </el-container>
</template>
<script>
import AdminSidebar from "./side/AdminSidebar.vue";
import UserSidebar from "./side/UserSidebar.vue";
export default {
  name: "UserDetail",
  inject: ["reload"],
  components: {
    UserSidebar,
    AdminSidebar,
  },
  data() {
    return {
      // 被查看用户的身份
      userAuthority: "",
      // 存储在localStorage中的用户详情，包括权限等信息
      userDetails: {},
      teacher: {},
      student: {},
      parent: {},
      buttonLoading: false,
      dialogPasswordFormVisible: false,
      passwordForm: {
        oldPass: "",
        newPass: "",
        checkPass: "",
      },
      // 用于改变input类型
      password: "password",
      // 用于改变input右侧图标
      icon: "el-input__icon el-icon-unlock",
      dialogInfoFormVisible: false,
      // 必须要声明form中的字段，否则会有无法更改表单内容的问题
      infoForm: {
        id: "",
        username: "",
        name: "",
        phone: "",
        address: "",
        email: "",
      },
      beforeInfoForm: {},
      rules: {
        newPass: [
          {
            validator: (rule, value, callback) => {
              if (value === "") {
                callback(new Error("请输入新的密码"));
              } else if (value === this.passwordForm.oldPass) {
                callback(new Error("新密码不能与原密码相同"));
              } else {
                if (this.passwordForm.checkPass !== "") {
                  this.$nextTick(() => {
                    this.$refs.passwordForm.validateField("checkPass");
                  });
                }
                callback();
              }
            },
            trigger: "blur",
          },
        ],
        checkPass: [
          {
            validator: (rule, value, callback) => {
              if (value === "") {
                callback(new Error("请再次输入密码"));
              } else if (value !== this.passwordForm.newPass) {
                callback(new Error("两次输入密码不一致!"));
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],
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
      },
    };
  },
  computed: {
    isAdmin: function () {
      return this.userDetails.authorities[0].authority === "Role_Admin";
    },
    isOwner: function () {
      return this.$route.params.id === String(this.userDetails.id);
    },
    isUserTeacher() {
      return this.userAuthority === "teacher";
    },
    isUserStudent() {
      return this.userAuthority === "student";
    },
    isUserParent() {
      return this.userAuthority === "parent";
    },
    infoFormChanged: function () {
      return !(
        this.infoForm.id === this.beforeInfoForm.id &&
        this.infoForm.username === this.beforeInfoForm.username &&
        this.infoForm.name === this.beforeInfoForm.name &&
        this.infoForm.phone === this.beforeInfoForm.phone &&
        this.infoForm.address === this.beforeInfoForm.address &&
        this.infoForm.email === this.beforeInfoForm.email
      );
    },
  },
  created() {
    this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
    if (this.isAdmin || this.isOwner) {
      localStorage.setItem("menuIndex", "4");
    } else {
      localStorage.setItem("menuIndex", "5");
    }
    const loading = this.$loading({
      lock: true,
    });
    if (this.isOwner) {
      this.$axios
        .get("/profile/user", {
          params: {
            id: this.$route.params.id,
          },
        })
        .then((res) => {
          loading.close();
          console.log(res.data);
          this.userAuthority = res.data.authority.authority;
          switch (res.data.authority.authority) {
            // 个人身份为老师
            case "teacher":
              this.teacher = res.data.teacher;
              break;
            // 个人身份为学生
            case "student":
              this.student = res.data.student;
              this.parent = res.data.parent;
              break;
            // 个人身份为家长
            case "parent":
              this.student = res.data.student;
              this.parent = res.data.parent;
              break;
          }
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          this.$message.error("获取个人信息失败");
        });
    } else {
      this.$axios
        .get("/profile/other", {
          params: {
            id: this.$route.params.id,
          },
        })
        .then((res) => {
          loading.close();
          console.log(res.data);
          this.userAuthority = res.data.authority.authority;
          switch (res.data.authority.authority) {
            // 查看用户身份为老师
            case "teacher":
              this.teacher = res.data.teacher;
              break;
            // 查看用户身份为学生
            case "student":
              this.student = res.data.student;
              this.parent = res.data.parent;
              break;
            case "parent":
              this.student = res.data.student;
              this.parent = res.data.parent;
              break;
          }
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          this.$message.error("获取用户信息失败");
        });
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    resetPasswordForm() {
      this.passwordForm.newPass = "";
      this.passwordForm.checkPass = "";
      this.$nextTick(() => {
        this.$refs.passwordForm.clearValidate();
      });
    },
    beforeChangePassword() {
      switch (this.userAuthority) {
        case "teacher":
          this.passwordForm.oldPass = this.teacher.password;
          break;
        case "student":
          this.passwordForm.oldPass = this.student.password;
          break;
        case "parent":
          this.passwordForm.oldPass = this.parent.password;
          break;
      }
      this.resetPasswordForm();
      this.dialogPasswordFormVisible = true;
    },
    showPass() {
      switch (this.password) {
        case "text":
          this.password = "password";
          this.icon = "el-input__icon el-icon-unlock";
          break;
        case "password":
          this.password = "text";
          this.icon = "el-input__icon el-icon-lock";
          break;
      }
    },
    changePassword() {
      this.$refs["passwordForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("id", this.$route.params.id);
          param.append("password", this.passwordForm.newPass);
          this.$axios
            .post("/profile/password", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogPasswordFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功修改密码",
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
                this.$message.error("修改密码失败");
              }
            });
        }
      });
    },
    resetInfoForm() {
      Object.assign(this.infoForm, this.beforeInfoForm);
    },
    beforeEditUserInfo(role) {
      switch (role) {
        case "teacher":
          Object.assign(this.beforeInfoForm, this.teacher);
          break;
        case "parent":
          Object.assign(this.beforeInfoForm, this.parent);
          break;
        case "student":
          Object.assign(this.beforeInfoForm, this.student);
          break;
      }
      this.resetInfoForm();
      this.dialogInfoFormVisible = true;
    },
    editUserInfo() {
      this.$refs["infoForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("id", this.infoForm.id);
          param.append("username", this.infoForm.username);
          param.append("name", this.infoForm.name);
          param.append("phone", this.infoForm.phone);
          param.append("address", this.infoForm.address);
          param.append("email", this.infoForm.email);
          this.$axios
            .post("/profile/update", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogInfoFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功修改用户信息",
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
                this.$message.error("修改用户信息请求失败");
              }
            });
        }
      });
    },
  },
};
</script>
<style scoped>
.nav_menu {
  margin-bottom: 40px;
  border-bottom: 1px solid #f0f0f0;
}
.page_header {
  margin: 0 20px 0;
}
.info_wrapper {
  margin: -20px 20px 10px;
}
</style>
