<template>
  <el-container>
    <el-header class="nav_menu">
      <admin-sidebar v-if="isAdmin"></admin-sidebar>
      <user-sidebar v-else></user-sidebar>
    </el-header>
    <el-header class="page_header">
      <el-page-header @back="goBack" content="帖子详情页"></el-page-header>
    </el-header>
    <el-main>
      <el-card shadow="never" class="card_wrapper">
        <div class="post_row_wrapper">
          <div class="post_title_wrapper">
            {{ mainPost.title }}
          </div>
          <div style="margin-top: -5px;">
            <el-button
              type="primary"
              plain
              icon="el-icon-edit-outline"
              size="small"
              @click="dialogCommentAddFormVisible = true"
              v-if="!isAdmin"
              >写回答</el-button
            >
          </div>
        </div>
        <div class="post_content_wrapper">
          <span v-if="!isContentTooLong">{{ mainPost.content }}</span>
          <span
            @click="
              showAllContent
                ? (showAllContent = false)
                : (showAllContent = true)
            "
            class="post_content"
            v-if="isContentTooLong"
          >
            {{
              showAllContent ? mainPost.content : getShortText(mainPost.content)
            }}
          </span>
          <el-link
            :underline="false"
            v-if="!showAllContent && isContentTooLong"
            @click="showAllContent = true"
            >显示全部<i class="el-icon-arrow-down el-icon--right"></i>
          </el-link>
          <el-link
            :underline="false"
            v-if="showAllContent && isContentTooLong"
            @click="showAllContent = false"
            >收起<i class="el-icon-arrow-up el-icon--right"></i>
          </el-link>
        </div>
        <div class="info_wrapper">
          <el-link
            :disabled="!mainPost.hasCreateUser"
            class="creator_wrapper"
            @click="toUserDetail(mainPost.createUserID)"
          >
            {{
              "发帖人：" +
              getCreatorName(mainPost.createName, mainPost.hasCreateUser)
            }}
          </el-link>
          <div>{{ "最后修改时间：" + mainPost.createDateTime }}</div>
        </div>
        <el-empty
          description="帖子不存在"
          v-if="mainPost.length === 0"
        ></el-empty>
      </el-card>
      <el-card
        shadow="never"
        class="card_wrapper"
        body-style="padding: 0"
        style="margin-top: 5px;"
      >
        <div class="num_row">{{ "共" + comments.length + "条回答" }}</div>
        <div
          class="comment_wrapper"
          v-for="(comment, index) in comments"
          :key="index"
        >
          <div class="first_row_wrapper">
            <div
              class="creator_row"
              @click="
                beforeToUserDetail(comment.createUserID, comment.hasCreateUser)
              "
            >
              <img
                src="https://pic3.zhimg.com/v2-d41c2ceaed8f51999522f903672a521f_xs.jpg?source=1940ef5c"
                class="image_wrapper"
              />
              <span class="creator_name">{{
                getCreatorName(comment.createName, comment.hasCreateUser)
              }}</span>
            </div>
            <div class="time_wrapper">
              {{ "最后修改时间：" + mainPost.createDateTime }}
            </div>
          </div>
          <div class="comment_content">{{ comment.content }}</div>
          <div class="button_row">
            <el-link
              icon="el-icon-s-comment"
              type="info"
              :underline="false"
              v-if="comment.hasSubComments || isAdmin"
              class="button_wrapper"
              @click="
                beforeCheckSubComments(
                  comment.id,
                  getCreatorName(comment.createName, comment.hasCreateUser)
                )
              "
              >查看评论</el-link
            >
            <el-link
              icon="el-icon-s-promotion"
              type="info"
              :underline="false"
              v-if="!isAdmin && !comment.hasSubComments"
              class="button_wrapper"
              @click="
                beforeCheckSubComments(
                  comment.id,
                  getCreatorName(comment.createName, comment.hasCreateUser)
                )
              "
              >添加评论</el-link
            >
            <el-link
              icon="el-icon-s-order"
              type="info"
              :underline="false"
              v-if="!isAdmin && isMyComment(comment.createUserID)"
              class="button_wrapper"
              @click="beforeEditComment(index)"
              >修改回答</el-link
            >
            <el-link
              icon="el-icon-s-release"
              type="info"
              :underline="false"
              v-if="isAdmin || isMyComment(comment.createUserID)"
              class="button_wrapper"
              @click="deleteComment(comment.id)"
              >删除回答</el-link
            >
          </div>
        </div>
        <el-empty
          description="暂无回答"
          v-if="comments.length === 0"
        ></el-empty>
      </el-card>
      <el-dialog
        title="写回答"
        :visible.sync="dialogCommentAddFormVisible"
        width="35%"
        center
        @close="resetForm('commentAddForm')"
      >
        <el-form ref="commentAddForm" :model="commentAddForm" :rules="rules">
          <el-form-item prop="content">
            <el-input
              v-model="commentAddForm.content"
              type="textarea"
              maxlength="1000"
              show-word-limit
              :autosize="{ minRows: 6, maxRows: 8 }"
              placeholder="请输入回答内容"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetForm('commentAddForm')"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            :loading="buttonLoading"
            @click="addComment"
            >提交</el-button
          >
        </div>
      </el-dialog>
      <el-dialog
        title="修改回答"
        :visible.sync="dialogCommentEditFormVisible"
        width="40%"
        center
      >
        <el-form
          ref="commentEditForm"
          :model="commentEditForm"
          :rules="rules"
          label-width="80px"
          label-position="right"
          style="margin-right: 40px;"
        >
          <el-form-item label="id" prop="id" required>
            <el-input v-model="commentEditForm.id" disabled></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <el-input
              v-model="commentEditForm.content"
              type="textarea"
              maxlength="1000"
              show-word-limit
              :autosize="{ minRows: 6, maxRows: 8 }"
              placeholder="请输入回答内容"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetCommentEditForm"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            @click="editComment"
            :disabled="!commentEditFormChanged"
            :loading="buttonLoading"
            >提交</el-button
          >
        </div>
      </el-dialog>
      <el-dialog
        title="查看评论"
        :visible.sync="dialogSubCommentsVisible"
        width="50%"
        center
        @close="reload"
      >
        <el-scrollbar
          style="height: 400px;"
          wrap-class="default-scrollbar__wrap"
          ref="myScrollbar"
          v-loading="subCommentsLoading"
        >
          <el-card shadow="never" body-style="padding: 0">
            <div class="subcomment_row_wrapper">
              <div class="num_row">共{{ subComments.length }}条评论</div>
              <div style="margin: 10px 20px 10px;">
                <el-link
                  type="primary"
                  icon="el-icon-position"
                  :underline="false"
                  @click="responseMainComment"
                  v-if="!isAdmin"
                  >回复主评论</el-link
                >
              </div>
            </div>
            <div
              class="comment_wrapper"
              v-for="(comment, index) in subComments"
              :key="index"
            >
              <div class="first_row_wrapper">
                <div class="creator_row">
                  <img
                    src="https://pic3.zhimg.com/v2-d41c2ceaed8f51999522f903672a521f_xs.jpg?source=1940ef5c"
                    class="image_wrapper"
                    @click="
                      beforeToUserDetail(
                        comment.createUserID,
                        comment.hasCreateUser
                      )
                    "
                  />
                  <span
                    class="creator_name subcomment_text"
                    @click="
                      beforeToUserDetail(
                        comment.createUserID,
                        comment.hasCreateUser
                      )
                    "
                    >{{
                      getCreatorName(comment.createName, comment.hasCreateUser)
                    }}</span
                  >
                  <span
                    v-if="comment.postingCommentID !== main_comment_id"
                    class="response_text"
                    >回复</span
                  >
                  <span
                    v-if="comment.postingCommentID !== main_comment_id"
                    class="response_name_text"
                    @click="
                      toResponseUserDetail(
                        comment.postingCommentID,
                        main_comment_id
                      )
                    "
                    >{{
                      getResponseUserName(
                        comment.postingCommentID,
                        main_comment_id
                      ).name
                    }}</span
                  >
                </div>
                <div class="time_wrapper">
                  {{ "评论时间：" + mainPost.createDateTime }}
                </div>
              </div>
              <div class="comment_content">{{ comment.content }}</div>
              <div class="button_row">
                <el-link
                  icon="el-icon-chat-line-round"
                  type="info"
                  :underline="false"
                  v-if="!isAdmin"
                  class="button_wrapper"
                  @click="
                    changeComment(
                      comment.id,
                      getCreatorName(comment.createName, comment.hasCreateUser)
                    )
                  "
                  >回复</el-link
                >
                <el-link
                  icon="el-icon-edit-outline"
                  type="info"
                  :underline="false"
                  class="button_wrapper"
                  @click="beforeEditSubComment(index)"
                  v-if="!isAdmin && isMyComment(comment.createUserID)"
                  >修改评论</el-link
                >
                <el-link
                  icon="el-icon-document-delete"
                  type="info"
                  :underline="false"
                  class="button_wrapper"
                  @click="deleteSubComment(comment.id)"
                  v-if="isAdmin || isMyComment(comment.createUserID)"
                  >删除评论</el-link
                >
              </div>
            </div>
            <el-empty
              description="暂无评论"
              v-if="subComments.length === 0"
            ></el-empty>
            <div v-if="!isAdmin">
              <el-input
                placeholder="请输入内容"
                v-model="subCommentForm.content"
                clearable
                maxlength="100"
                show-word-limit
              >
                <template slot="prepend">{{
                  subCommentForm.to_user_name
                }}</template>
                <el-button
                  slot="append"
                  icon="el-icon-chat-dot-square"
                  @click="addSubComment"
                  >评论</el-button
                >
              </el-input>
            </div>
          </el-card>
        </el-scrollbar>
      </el-dialog>
      <el-dialog
        title="修改评论"
        :visible.sync="dialogSubCommentEditFormVisible"
        width="40%"
        center
      >
        <el-form
          ref="subCommentEditForm"
          :model="subCommentEditForm"
          :rules="rules"
          label-width="80px"
          label-position="right"
          style="margin-right: 40px;"
        >
          <el-form-item label="id" prop="id" required>
            <el-input v-model="subCommentEditForm.id" disabled></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <el-input
              v-model="subCommentEditForm.content"
              maxlength="100"
              show-word-limit
              placeholder="请输入评论内容"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button icon="el-icon-refresh" @click="resetSubCommentEditForm"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-check"
            @click="editSubComment"
            :disabled="!subCommentEditFormChanged"
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
  name: "PostDetail",
  inject: ["reload"],
  components: {
    UserSidebar,
    AdminSidebar,
  },
  data() {
    return {
      // 存储在localStorage中的用户详情，包括权限等信息
      userDetails: {},
      // 主贴内容是否完全展示/省略
      showAllContent: false,
      // 主贴信息
      mainPost: {},
      // 主贴评论列表
      comments: [],
      subComments: [],
      buttonLoading: false,
      dialogCommentAddFormVisible: false,
      commentAddForm: {
        content: "",
      },
      dialogCommentEditFormVisible: false,
      commentEditForm: {
        id: "",
        content: "",
      },
      beforeEditForm: {},
      rules: {
        content: [{ required: true, message: "内容不得为空", trigger: "blur" }],
      },
      subCommentsLoading: false,
      dialogSubCommentsVisible: false,
      subCommentForm: {
        content: "",
        to_comment_id: "",
        to_user_name: "",
      },
      // 子评论弹窗对应的主评论id
      main_comment_id: "",
      dialogSubCommentEditFormVisible: false,
      subCommentEditForm: {
        id: "",
        content: "",
      },
    };
  },
  computed: {
    isAdmin: function () {
      return this.userDetails.authorities[0].authority === "Role_Admin";
    },
    isContentTooLong() {
      return this.mainPost.content.length >= 100;
    },
    commentEditFormChanged() {
      return !(this.commentEditForm.content === this.beforeEditForm.content);
    },
    subCommentEditFormChanged() {
      return !(this.subCommentEditForm.content === this.beforeEditForm.content);
    },
  },
  created() {
    this.userDetails = JSON.parse(localStorage.getItem("userDetails"));
    localStorage.setItem("menuIndex", "3");
    const loading = this.$loading({
      lock: true,
    });
    // 获取主贴信息
    this.$axios
      .get("/posting/get", {
        params: {
          id: this.$route.params.id,
        },
      })
      .then((res) => {
        if (res.status === 200 && res.data) {
          console.log(res.data);
          this.mainPost = res.data;
        } else {
          this.$message.error("主贴不存在");
        }
      })
      .catch((error) => {
        console.log(error);
        this.$message.error("获取主贴信息失败");
      });
    // 获取主贴回答列表
    this.$axios
      .get("/postingComment/get", {
        params: {
          id: this.$route.params.id,
          postingCommentID: "",
        },
      })
      .then((res) => {
        loading.close();
        console.log(res.data);
        this.comments = res.data;
      })
      .catch((error) => {
        loading.close();
        console.log(error);
        this.$message.error("获取主贴回答列表失败");
      });
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    beforeToUserDetail(userID, exist) {
      if (exist) {
        this.toUserDetail(userID);
      } else {
        this.$message("用户已注销，无法查看用户信息");
      }
    },
    toUserDetail(userID) {
      this.$router.push(`/profile/${userID}`);
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
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
    isMyComment(creatorID) {
      return this.userDetails.id === creatorID;
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
    resetCommentEditForm() {
      Object.assign(this.commentEditForm, this.beforeEditForm);
    },
    scrollDown() {
      this.$refs["myScrollbar"].wrap.scrollTop = this.$refs[
        "myScrollbar"
      ].wrap.scrollHeight;
    },
    addComment() {
      this.$refs["commentAddForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("postingID", this.$route.params.id);
          param.append("content", this.commentAddForm.content);
          param.append("createUserID", this.userDetails.id);
          param.append("createDateTime", this.getDateString());
          this.$axios
            .post("/postingComment/add", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogCommentAddFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功新增主贴回答",
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
                this.$message.error("新增主贴回答失败");
              }
            });
        }
      });
    },
    beforeEditComment(index) {
      Object.assign(this.beforeEditForm, this.comments[index]);
      this.resetCommentEditForm();
      this.dialogCommentEditFormVisible = true;
    },
    editComment() {
      this.$refs["commentEditForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("id", this.commentEditForm.id);
          param.append("content", this.commentEditForm.content);
          param.append("createDateTime", this.getDateString());
          this.$axios
            .post("/postingComment/edit", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogCommentEditFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功编辑主贴回答",
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
                this.$message.error("编辑主贴回答失败");
              }
            });
        }
      });
    },
    deleteComment(commentID) {
      this.$confirm(
        "此操作将删除主贴回答及其所有的回复信息, 是否继续?",
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
          let param = new URLSearchParams();
          param.append("id", commentID);
          this.$axios
            .post("/postingComment/delete", param)
            .then((res) => {
              loading.close();
              if (res.status === 200) {
                this.$message({
                  type: "success",
                  message: "成功删除主贴回答",
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
                this.$message.error("删除主贴回答失败");
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
    loadSubComments() {
      this.$axios
        .get("/postingComment/get", {
          params: {
            id: this.$route.params.id,
            postingCommentID: this.main_comment_id,
          },
        })
        .then((res) => {
          this.subCommentsLoading = false;
          console.log(res.data);
          this.subComments = res.data;
        })
        .catch((error) => {
          this.subCommentsLoading = false;
          console.log(error);
          this.$message.error("获取子评论列表失败");
        });
    },
    beforeCheckSubComments(commentID, username) {
      this.main_comment_id = commentID;
      this.subCommentForm.to_comment_id = this.main_comment_id;
      this.subCommentForm.to_user_name = "@" + username;
      Object.assign(this.beforeEditForm, this.subCommentForm);
      this.dialogSubCommentsVisible = true;
      this.subCommentsLoading = true;
      this.loadSubComments();
    },
    resetSubCommentForm() {
      Object.assign(this.subCommentForm, this.beforeEditForm);
    },
    changeComment(commentID, username) {
      this.subCommentForm.to_comment_id = commentID;
      this.subCommentForm.to_user_name = "@" + username;
      this.subCommentForm.content = "";
      this.scrollDown();
    },
    getResponseUserName(responseID, commentID) {
      // responseID是回复子评论的id, commentID是主评论的id
      if (responseID === commentID) {
        return {};
      } else {
        let num = this.subComments.findIndex(function (item) {
          return item.id === responseID;
        });
        if (num < 0) {
          return {};
        } else {
          let item = this.subComments[num];
          return {
            user_id: item.createUserID,
            exist: item.hasCreateUser,
            name: item.hasCreateUser ? item.createName : "账号已注销",
          };
        }
      }
    },
    toResponseUserDetail(responseID, commentID) {
      let object = this.getResponseUserName(responseID, commentID);
      if (object.exist) {
        this.toUserDetail(object.user_id);
      } else {
        this.$message("用户已注销，无法查看用户信息");
      }
    },
    responseMainComment() {
      if (this.subCommentForm.to_comment_id !== this.main_comment_id) {
        this.resetSubCommentForm();
      }
      this.scrollDown();
    },
    addSubComment() {
      if (this.subCommentForm.content.length > 0) {
        this.subCommentsLoading = true;
        let param = new URLSearchParams();
        param.append("postingID", this.$route.params.id);
        param.append("content", this.subCommentForm.content);
        param.append("postingCommentID", this.subCommentForm.to_comment_id);
        param.append("createUserID", this.userDetails.id);
        param.append("createDateTime", this.getDateString());
        this.$axios
          .post("/postingComment/addComment", param)
          .then((res) => {
            if (res.status === 200) {
              this.loadSubComments();
              this.resetSubCommentForm();
              this.$message({
                type: "success",
                message: "成功新增子评论",
              });
            } else {
              this.subCommentsLoading = false;
              this.$message.error("新增子评论失败(请求成功)");
            }
          })
          .catch((error) => {
            this.subCommentsLoading = false;
            console.log(error);
            if (error.response.status === 501) {
              this.$message.error(error.response.data.message);
            } else {
              this.$message.error("新增子评论失败");
            }
          });
      } else {
        this.$message.error("评论内容不能为空");
      }
    },
    deleteSubComment(commentID) {
      this.$confirm(
        "此操作将删除该子评论及其所有的回复信息, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.subCommentsLoading = true;
          let param = new URLSearchParams();
          param.append("id", commentID);
          this.$axios
            .post("/postingComment/deleteComment", param)
            .then((res) => {
              if (res.status === 200) {
                this.loadSubComments();
                this.$message({
                  type: "success",
                  message: "成功删除子评论",
                });
              } else {
                this.subCommentsLoading = false;
                this.$message.error("删除子评论失败(请求成功)");
              }
            })
            .catch((error) => {
              this.subCommentsLoading = false;
              console.log(error);
              if (error.response.status === 501) {
                this.$message.error(error.response.data.message);
              } else {
                this.$message.error("删除子评论失败");
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
    resetSubCommentEditForm() {
      Object.assign(this.subCommentEditForm, this.beforeEditForm);
    },
    beforeEditSubComment(index) {
      Object.assign(this.beforeEditForm, this.subComments[index]);
      this.resetSubCommentEditForm();
      this.dialogSubCommentEditFormVisible = true;
    },
    editSubComment() {
      this.$refs["subCommentEditForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let param = new URLSearchParams();
          param.append("id", this.subCommentEditForm.id);
          param.append("content", this.subCommentEditForm.content);
          param.append("createDateTime", this.getDateString());
          this.$axios
            .post("/postingComment/edit", param)
            .then((res) => {
              this.buttonLoading = false;
              if (res.status === 200) {
                this.dialogSubCommentEditFormVisible = false;
                this.$message({
                  type: "success",
                  message: "成功编辑子评论",
                });
                this.loadSubComments();
              }
            })
            .catch((error) => {
              this.buttonLoading = false;
              console.log(error);
              if (error.response.status === 501) {
                this.$message.error(error.response.data.message);
              } else {
                this.$message.error("编辑子评论失败");
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
.card_wrapper {
  margin: -20px 20px 0;
}
.post_row_wrapper {
  display: flex;
  justify-content: space-between;
}
.post_title_wrapper {
  text-align: start;
  font: 20px large;
  color: #111;
  line-height: 150%;
  padding-bottom: 5px;
}
.post_content_wrapper {
  text-align: start;
  font: 14px;
  color: #333;
  line-height: 150%;
}
.post_content:hover {
  cursor: pointer;
  color: #636b7c;
}
.num_row {
  padding: 10px 20px 10px;
  text-align: start;
  font: 16px large;
  color: #111;
  border-bottom: 1px solid #f0f0f0;
}
.comment_wrapper {
  padding: 10px 20px 10px;
  border-bottom: 1px solid #f0f0f0;
}
.creator_row {
  display: flex;
  justify-content: start;
}
.creator_row:hover {
  cursor: pointer;
}
.image_wrapper {
  height: 40px;
  width: 40px;
  border-radius: 3px;
}
.creator_name {
  margin: 10px 8px 0 12px;
  font: 15px large;
  color: #111;
}
.subcomment_text {
  font: 14px large;
}
.response_text {
  margin: 10px 8px 0 0;
  font: 14px large;
  color: #666;
}
.response_text:hover {
  cursor: auto;
}
.response_name_text {
  margin: 10px 0 0 0;
  font: 14px large;
  color: #111;
}
.comment_content {
  margin-top: 10px;
  text-align: start;
  font: 14px;
  color: #333;
  line-height: 150%;
}
.button_row {
  display: flex;
  justify-content: start;
  margin: 5px 0 5px;
}
.button_wrapper {
  margin-right: 10px;
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
.first_row_wrapper {
  display: flex;
  justify-content: space-between;
}
.time_wrapper {
  font: 14px small;
  color: #737474;
}
.subcomment_row_wrapper {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #f0f0f0;
}
</style>
