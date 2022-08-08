<template>
  <el-container>
    <el-header class="nav_menu">
      <user-sidebar v-if="readonly"></user-sidebar>
      <admin-sidebar v-else></admin-sidebar>
    </el-header>
    <el-header class="page_header">
      <div>
        <el-page-header @back="goBack" content="班级列表"></el-page-header>
      </div>
      <div class="button_in_row">
        <el-button
          type="primary"
          round
          icon="el-icon-plus"
          @click="dialogAddFormVisible = true"
          v-if="!readonly"
          >新增班级</el-button
        >
      </div>
    </el-header>
    <el-main>
      <div class="card_wrapper">
        <el-card
          class="card_item"
          v-for="i in classes"
          :key="i.id"
          :body-style="{ padding: '0px' }"
          @click.native="toClassDetail(i.id)"
        >
          <div class="card_header">
            <el-dropdown @command="handleCommand">
              <i class="el-icon-more card_more"></i>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  icon="el-icon-view"
                  :command="beforeHandleCommand(i, 'toClassDetail')"
                  >查看详情</el-dropdown-item
                >
                <el-dropdown-item
                  icon="el-icon-edit"
                  :command="beforeHandleCommand(i, 'edit')"
                  divided
                  v-if="!readonly"
                  >编辑信息</el-dropdown-item
                >
                <el-dropdown-item
                  icon="el-icon-delete"
                  :command="beforeHandleCommand(i, 'delete')"
                  v-if="!readonly"
                  >删除班级</el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div class="card_body">
            <el-link>{{ i.grade }}年级（{{ i.classNum }}）班</el-link>
          </div>
        </el-card>
      </div>
      <el-empty description="暂无班级" v-if="classes.length === 0"></el-empty>
      <el-dialog
        title="新增班级"
        :visible.sync="dialogAddFormVisible"
        width="25%"
        center
        @close="resetForm('addForm')"
      >
        <el-form
          ref="addForm"
          :model="addForm"
          :rules="rules"
          label-width="80px"
          label-position="left"
          style="margin: 0 30px 0 10px;"
        >
          <el-form-item label="年级" prop="grade">
            <el-input
              v-model="addForm.grade"
              placeholder="请输入年级"
            ></el-input>
          </el-form-item>
          <el-form-item label="班级" prop="classNum">
            <el-input
              v-model="addForm.classNum"
              placeholder="请输入班级"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetForm('addForm')"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            :loading="buttonLoading"
            @click="addClass"
            >提交</el-button
          >
        </div>
      </el-dialog>
      <el-dialog
        title="编辑班级信息"
        :visible.sync="dialogEditFormVisible"
        width="25%"
        center
      >
        <el-form
          ref="editForm"
          :model="editForm"
          :rules="rules"
          label-width="80px"
          label-position="left"
          style="margin: 0 30px 0 10px;"
        >
          <el-form-item label="id" prop="id" required>
            <el-input v-model="editForm.id" disabled></el-input>
          </el-form-item>
          <el-form-item label="年级" prop="grade">
            <el-input
              v-model="editForm.grade"
              placeholder="请输入年级"
            ></el-input>
          </el-form-item>
          <el-form-item label="班级" prop="classNum">
            <el-input
              v-model="editForm.classNum"
              placeholder="请输入班级"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetEditForm"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            @click="editClassInfo"
            :disabled="!editFormChanged"
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
  name: "ClassList",
  inject: ["reload"],
  components: {
    UserSidebar,
    AdminSidebar,
  },
  data() {
    return {
      // 存储在localStorage中的用户详情，包括权限等信息
      userDetails: {},
      classes: [],
      dialogAddFormVisible: false,
      addForm: {
        grade: "",
        classNum: "",
      },
      dialogEditFormVisible: false,
      editForm: {
        id: "",
        grade: "",
        classNum: "",
      },
      beforeEditForm: {},
      buttonLoading: false,
      rules: {
        grade: [
          { required: true, message: "请输入年级", trigger: "blur" },
          {
            type: "number",
            min: 1,
            max: 12,
            message: "年级须为1~12间的数字",
            trigger: "blur",
            transform(value) {
              return Number(value);
            },
          },
        ],
        classNum: [
          { required: true, message: "请输入班级", trigger: "blur" },
          {
            type: "number",
            min: 1,
            max: 99,
            message: "班级须为1~2位的数字",
            trigger: "blur",
            transform(value) {
              return Number(value);
            },
          },
        ],
      },
    };
  },
  computed: {
    readonly: function () {
      return this.userDetails.authorities[0].authority !== "Role_Admin";
    },
    editFormChanged: function () {
      return !(
        this.editForm.grade === this.beforeEditForm.grade &&
        this.editForm.classNum === this.beforeEditForm.classNum
      );
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
    switch (this.userDetails.authorities[0].authority) {
      case "Role_Admin": {
        // 获取全部班级列表
        this.$axios
          .get("/classes")
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.classes = res.data.classes;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取全部班级列表失败");
          });
        break;
      }
      case "Role_Teacher": {
        // 获取老师的任教班级列表
        this.$axios
          .get("/classesTeacher", {
            params: {
              id: this.userDetails.id,
            },
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.classes = res.data.classes;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取教师班级列表失败");
          });
        break;
      }
      case "Role_Student": {
        // 获取学生的所在班级
        this.$axios
          .get("/classesStudent", {
            params: {
              id: this.userDetails.id,
            },
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.classes = res.data.classes;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取学生班级失败");
          });
        break;
      }
      case "Role_Parent": {
        // 获取家长对应学生的所在班级
        this.$axios
          .get("/classesParent", {
            params: {
              id: this.userDetails.id,
            },
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.classes = res.data.classes;
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取家长对应班级失败");
          });
        break;
      }
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    toClassDetail(classId) {
      this.$router.push(`/class/${classId}`);
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // 将表单值重置为修改之前的数值
    resetEditForm() {
      // 深拷贝，防止两个form的数据双向绑定
      Object.assign(this.editForm, this.beforeEditForm);
    },
    addClass() {
      // 先校验表单，再发新增班级的请求，成功后刷新页面
      this.$refs["addForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("grade", this.addForm.grade);
          param.append("classNum", this.addForm.classNum);
          this.$axios
            .post("/classes/add", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogAddFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功新增班级",
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
                this.$message.error("新增班级失败");
              }
            });
        }
      });
    },
    // 点击编辑按钮之后，弹出表单弹窗之前
    beforeEditClassInfo(object) {
      // 深拷贝，防止两个对象的数据双向绑定
      Object.assign(this.beforeEditForm, object);
      this.resetEditForm();
      this.dialogEditFormVisible = true;
    },
    editClassInfo() {
      // 先校验表单，再发编辑班级信息的请求，成功后刷新页面
      this.$refs["editForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("id", this.editForm.id);
          param.append("grade", this.editForm.grade);
          param.append("classNum", this.editForm.classNum);
          this.$axios
            .post("/classes/edit", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogEditFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功编辑班级信息",
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
                this.$message.error("编辑班级信息失败");
              }
            });
        }
      });
    },
    deleteClass(classId) {
      this.$confirm(
        "此操作将永久删除该班级，并且注销班级中所有学生及其家长的账号, 是否继续?",
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
          // 发删除班级请求，成功后刷新页面
          this.$axios
            .get("/classes/delete", {
              params: {
                id: classId,
              },
            })
            .then((res) => {
              loading.close();
              if (res.status === 200) {
                this.$message({
                  type: "success",
                  message: "成功删除班级",
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
                this.$message.error("删除班级失败");
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    beforeHandleCommand(object, command) {
      return {
        object: object,
        command: command,
      };
    },
    handleCommand(command) {
      switch (command.command) {
        case "toClassDetail":
          this.toClassDetail(command.object.id);
          break;
        case "edit":
          this.beforeEditClassInfo(command.object);
          break;
        case "delete":
          this.deleteClass(command.object.id);
          break;
      }
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
  display: flex;
  justify-content: space-between;
  margin: 0 20px 0;
}
.button_in_row {
  margin-top: -10px;
}
.card_wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
  margin-top: -40px;
}
.card_item {
  flex: 1;
  min-width: calc((100% - 170px) / 4);
  max-width: calc((100% - 170px) / 4);
  margin: 20px;
}
.card_item:hover {
  box-shadow: 0 4px 8px 0 rgba(245, 180, 180, 0.2),
    0 6px 20px 0 rgba(0, 0, 0, 0.19);
  cursor: pointer;
}
.card_header {
  border-radius: 4px 4px 0 0;
  min-height: 220px;
  background-color: #40a0ffee;
  text-align: right;
  padding-right: 5px;
}
.card_more {
  margin: 20px 4px 0 0;
  font-size: 25px;
  color: white;
  transform: rotate(90deg);
}
.card_body {
  margin: 40px;
}
</style>
