<template>
  <el-container>
    <el-header class="nav_menu">
      <admin-sidebar v-if="isAdmin"></admin-sidebar>
      <user-sidebar v-else></user-sidebar>
    </el-header>
    <el-header class="page_header">
      <div>
        <el-page-header @back="goBack" content="帖子列表页"></el-page-header>
      </div>
      <div class="button_in_row" style="margin-top: -10px;">
        <div>
          <el-input
            placeholder="请输入帖子标题"
            v-model="search_title"
            clearable
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="searchByTitle"
              >搜索</el-button
            >
          </el-input>
        </div>
        <div>
          <el-button
            type="primary"
            icon="el-icon-chat-line-square"
            round
            v-if="!isAdmin"
            style="margin-left: 20px;"
            @click="dialogAddFormVisible = true"
            >发布帖子</el-button
          >
        </div>
      </div>
    </el-header>
    <el-main>
      <el-card shadow="never" body-style="padding: 0" class="card_wrapper">
        <el-menu
          :default-active="search_mode"
          mode="horizontal"
          @select="changeSearchMode"
        >
          <el-menu-item index="1">全部</el-menu-item>
          <el-menu-item index="2">我发布的</el-menu-item>
          <el-menu-item index="3">我参与讨论的</el-menu-item>
        </el-menu>
        <div v-for="(post, index) in posts" :key="index" class="post_wrapper">
          <div @mouseenter="mouse_hover = index" @mouseleave="mouse_hover = -1">
            <div class="title_row">
              <div class="title_wrapper" @click="toPostDetail(post.id)">
                {{ post.title }}
              </div>
              <div class="button_in_row">
                <el-link
                  icon="el-icon-edit"
                  type="info"
                  :underline="false"
                  v-if="
                    !isAdmin &&
                    isMyPost(post.createUserID) &&
                    mouse_hover == index
                  "
                  @click="beforeEditPost(index)"
                  >编辑帖子</el-link
                >
                <el-link
                  icon="el-icon-delete"
                  type="info"
                  :underline="false"
                  v-if="
                    (isAdmin || isMyPost(post.createUserID)) &&
                    mouse_hover == index
                  "
                  style="margin-left: 10px;"
                  @click="deletePost(post.id)"
                  >删除帖子</el-link
                >
              </div>
            </div>
            <div class="content_wrapper" @click="toPostDetail(post.id)">
              {{ getShortText(post.content)
              }}<el-link :underline="false"
                >查看详情<i class="el-icon-view el-icon--right"></i>
              </el-link>
            </div>
            <div class="info_wrapper">
              <el-link
                :disabled="!post.hasCreateUser"
                class="creator_wrapper"
                @click="toUserDetail(post.createUserID)"
              >
                {{
                  "发帖人：" +
                  getCreatorName(post.createName, post.hasCreateUser)
                }}
              </el-link>
              <div>{{ "最后修改时间：" + post.createDateTime }}</div>
            </div>
          </div>
        </div>
        <el-empty description="暂无帖子" v-if="posts.length === 0"></el-empty>
      </el-card>
      <el-dialog
        title="发表帖子"
        :visible.sync="dialogAddFormVisible"
        width="35%"
        center
        @close="resetForm('addForm')"
      >
        <el-form
          ref="addForm"
          :model="addForm"
          :rules="rules"
          label-width="80px"
          label-position="right"
          style="margin-right: 40px;"
        >
          <el-form-item label="标题" prop="title">
            <el-input
              v-model="addForm.title"
              maxlength="30"
              show-word-limit
              placeholder="请输入帖子标题"
            ></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <el-input
              v-model="addForm.content"
              type="textarea"
              maxlength="1000"
              show-word-limit
              :autosize="{ minRows: 6, maxRows: 8 }"
              placeholder="请输入帖子内容"
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
            @click="addPost"
            >提交</el-button
          >
        </div>
      </el-dialog>
      <el-dialog
        title="编辑帖子"
        :visible.sync="dialogEditFormVisible"
        width="35%"
        center
      >
        <el-form
          ref="editForm"
          :model="editForm"
          :rules="rules"
          label-width="80px"
          label-position="right"
          style="margin-right: 40px;"
        >
          <el-form-item label="id" prop="id" required>
            <el-input v-model="editForm.id" disabled></el-input>
          </el-form-item>
          <el-form-item label="标题" prop="title">
            <el-input
              v-model="editForm.title"
              maxlength="30"
              show-word-limit
              placeholder="请输入帖子标题"
            ></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <el-input
              v-model="editForm.content"
              type="textarea"
              maxlength="1000"
              show-word-limit
              :autosize="{ minRows: 6, maxRows: 8 }"
              placeholder="请输入帖子内容"
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
            @click="editPost"
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
  name: "PostList",
  inject: ["reload"],
  components: {
    UserSidebar,
    AdminSidebar,
  },
  data() {
    return {
      // 存储在localStorage中的用户详情，包括权限等信息
      userDetails: {},
      posts: [],
      // 现在鼠标所在的帖子位置，用于控制删除、编辑帖子按钮的显示
      mouse_hover: -1,
      search_title: "",
      // 搜索模式：1是全部帖子，2是我发布的帖子，3是我参与讨论的帖子
      search_mode: "1",
      buttonLoading: false,
      dialogAddFormVisible: false,
      addForm: {
        title: "",
        content: "",
      },
      dialogEditFormVisible: false,
      editForm: {
        id: "",
        title: "",
        content: "",
      },
      beforeEditForm: {},
      rules: {
        title: [{ required: true, message: "请输入帖子标题", trigger: "blur" }],
      },
    };
  },
  computed: {
    isAdmin: function () {
      return this.userDetails.authorities[0].authority === "Role_Admin";
    },
    editFormChanged: function () {
      return !(
        this.editForm.title === this.beforeEditForm.title &&
        this.editForm.content === this.beforeEditForm.content
      );
    },
  },
  created() {
    this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
    localStorage.setItem("menuIndex", "3");
    const loading = this.$loading({
      lock: true,
    });
    this.$axios
      .get("/posting/search", {
        params: {
          title: "",
          myPosting: "",
          attend: "",
          id: this.userDetails.id,
        },
      })
      .then((res) => {
        loading.close();
        console.log(res.data);
        this.posts = res.data;
      })
      .catch((error) => {
        loading.close();
        console.log(error);
        this.$message.error("获取帖子列表失败");
      });
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    toPostDetail(postID) {
      this.$router.push(`/post/${postID}`);
    },
    toUserDetail(userID) {
      this.$router.push(`/profile/${userID}`);
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // 点击导航栏选择不同搜索模式
    changeSearchMode(key) {
      this.search_mode = key;
      switch (this.search_mode) {
        case "1": {
          const loading = this.$loading({
            lock: true,
          });
          this.$axios
            .get("/posting/search", {
              params: {
                title: "",
                myPosting: "",
                attend: "",
                id: this.userDetails.id,
              },
            })
            .then((res) => {
              loading.close();
              console.log(res.data);
              this.posts = res.data;
            })
            .catch((error) => {
              loading.close();
              console.log(error);
              this.$message.error("获取帖子列表失败");
            });
          break;
        }
        case "2": {
          const loading = this.$loading({
            lock: true,
          });
          this.$axios
            .get("/posting/search", {
              params: {
                title: "",
                myPosting: "true",
                attend: "",
                id: this.userDetails.id,
              },
            })
            .then((res) => {
              loading.close();
              console.log(res.data);
              this.posts = res.data;
            })
            .catch((error) => {
              loading.close();
              console.log(error);
              this.$message.error("获取我发布的帖子列表失败");
            });
          break;
        }
        case "3": {
          const loading = this.$loading({
            lock: true,
          });
          this.$axios
            .get("/posting/search", {
              params: {
                title: "",
                myPosting: "",
                attend: "true",
                id: this.userDetails.id,
              },
            })
            .then((res) => {
              loading.close();
              console.log(res.data);
              this.posts = res.data;
            })
            .catch((error) => {
              loading.close();
              console.log(error);
              this.$message.error("获取我参与讨论的帖子列表失败");
            });
          break;
        }
      }
    },
    searchByTitle() {
      const loading = this.$loading({
        lock: true,
      });
      this.search_mode = "1";
      this.$axios
        .get("/posting/search", {
          params: {
            title: this.search_title,
            myPosting: "",
            attend: "",
            id: this.userDetails.id,
          },
        })
        .then((res) => {
          loading.close();
          console.log(res.data);
          this.posts = res.data;
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          this.$message.error("获取按标题搜索的帖子列表失败");
        });
    },
    // 帖子内容过长用省略号替代
    getShortText(text) {
      if (text.length <= 100) {
        return text;
      } else {
        return text.substring(0, 100) + "...";
      }
    },
    getCreatorName(name, exist) {
      return exist ? name : "账号已注销";
    },
    // 返回是否是自己发的帖子
    isMyPost(creatorID) {
      return creatorID === this.userDetails.id;
    },
    getDateString() {
      let d = new Date();
      let months = [
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
      ];
      return d.getFullYear() + "-" + months[d.getMonth()] + "-" + d.getDate();
    },
    addPost() {
      this.$refs["addForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("title", this.addForm.title);
          param.append("content", this.addForm.content);
          param.append("createUserID", this.userDetails.id);
          param.append("createDateTime", this.getDateString());
          this.$axios
            .post("/posting/add", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogAddFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功发表帖子",
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
                this.$message.error("发表帖子失败");
              }
            });
        }
      });
    },
    resetEditForm() {
      Object.assign(this.editForm, this.beforeEditForm);
    },
    // 点击编辑帖子之后，显示编辑弹窗之前
    beforeEditPost(index) {
      Object.assign(this.beforeEditForm, this.posts[index]);
      this.resetEditForm();
      this.dialogEditFormVisible = true;
    },
    editPost() {
      this.$refs["editForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("id", this.editForm.id);
          param.append("title", this.editForm.title);
          param.append("content", this.editForm.content);
          param.append("createDateTime", this.getDateString());
          this.$axios
            .post("/posting/edit", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogEditFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功编辑帖子",
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
                this.$message.error("编辑帖子失败");
              }
            });
        }
      });
    },
    deletePost(postID) {
      this.$confirm("此操作将删除帖子及其所有的评论信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const loading = this.$loading({
            lock: true,
          });
          let param = new URLSearchParams();
          param.append("id", postID);
          this.$axios
            .post("/posting/delete", param)
            .then((res) => {
              loading.close();
              if (res.status === 200) {
                this.$message({
                  type: "success",
                  message: "成功删除帖子",
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
                this.$message.error("删除帖子失败");
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
  display: flex;
  justify-content: end;
}
.card_wrapper {
  margin: -20px 20px 0;
}
.filter_row {
  display: flex;
  justify-content: start;
}
.post_wrapper {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}
.title_row {
  display: flex;
  justify-content: space-between;
}
.title_wrapper {
  text-align: start;
  font: 18px large;
  color: #111;
  line-height: 150%;
  padding-bottom: 5px;
}
.title_wrapper:hover {
  cursor: pointer;
  color: #175199;
}
.content_wrapper {
  text-align: start;
  font: 14px;
  color: #333;
  line-height: 150%;
}
.content_wrapper:hover {
  cursor: pointer;
  color: #636b7c;
}
.info_wrapper {
  display: flex;
  justify-content: start;
  font: 14px small;
  color: #737474;
  margin-top: 5px;
}
.creator_wrapper {
  margin-right: 10px;
}
</style>
