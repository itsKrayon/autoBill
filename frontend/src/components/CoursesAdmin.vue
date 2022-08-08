<template>
  <el-container>
    <el-header >
      <el-page-header @back="goBack" style="float:left;width: 400px"></el-page-header>
      <AdminSidebar v-bind:menuIndex="menuIndex" style="width: 400px;float: right"></AdminSidebar>
    </el-header>
    <el-main :gutter="20">
      <div>
        <div style="">
          <el-row :gutter="20">
            <el-col :span="13">
              <template>
                <el-select v-model="form.courseID" filterable placeholder="course">
                  <el-option
                    v-for="item in course"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </template>
              <template>
                <el-select v-model="form.classID" filterable placeholder="class">
                  <el-option
                    v-for="item in classA"
                    :key="item.id"
                    :label="item.id"
                    :value="item.id">
                  </el-option>
                </el-select>
              </template>
              <template>
                <el-select v-model="form.teacherID" filterable placeholder="teacher">
                  <el-option
                    v-for="item in teacher"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </template>
              <el-button style="float: right;" icon="el-icon-search" @click="searchCourse">搜索</el-button>
            </el-col>

            <el-col :span="11">
              <el-button style="float: right" plain @click="addC">新增</el-button>
            </el-col>

          </el-row>
          <!--
          <el-table
            :data="courses"
            style="width: 100%"
            title="课程">
            <el-table-column
              label="ID"
              prop="courseID"
            ></el-table-column>
            <el-table-column
              label="课程"
              prop="courseName"
              width=""></el-table-column>
            <el-table-column
              label="班级"
              prop="classID"
              width=""></el-table-column>
            <el-table-column
              label="老师"
              prop="teacherName"
              width=""></el-table-column>
            <el-table-column
              label="详情"
              width="">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button @click="deleteCourse(scope.row.id)" plain>删除</el-button>
                  <el-button @click="getCourse(scope.row.id)" plain>详情</el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
          -->
          <div class="card_wrapper">
            <el-card
              class="card_item"
              v-for="i in courses"
              :key="i.id"
              :body-style="{ padding: '0px' }"
              @click.native="deleteCourse(i.id)"
            >
              <div class="card_header">
                <el-dropdown @command="handleCommand">
                  <i class="el-icon-more card_more"></i>
                  <el-dropdown-menu slot="dropdown">

                    <el-dropdown-item
                      icon="el-icon-delete"
                      :command="beforeHandleCommand(i, 'delete')"

                    >删除课程</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
              <div class="card_body">
                <div>课程：{{ i.courseName }}</div>
                <div>老师：{{i.teacherName}}</div>
              </div>
            </el-card>
          </div>


            <el-dialog title="新增课程" :visible.sync="show" style="margin: 0px;">
              <el-form :model="form" :rules="rules">
                <el-form-item label="科目  "  style="margin: 10px;" >
                  <template>
                    <el-select v-model="form.courseID" filterable placeholder="course">
                      <el-option
                        v-for="item in course"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-form-item>
                <el-form-item label="班级"  style="margin: 10px;" >
                  <template>
                    <el-select v-model="form.classID" filterable placeholder="class">
                      <el-option
                        v-for="item in classA"
                        :key="item.id"
                        :label="item.className"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-form-item>
                <el-form-item label="教师"  style="margin: 10px;">
                  <template>
                    <el-select v-model="form.teacherID" filterable placeholder="teacher">
                      <el-option
                        v-for="item in teacher"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="show = false">取 消</el-button>
                <el-button type="primary" @click="addCourse">确 定</el-button>
              </div>
            </el-dialog>

          <el-empty v-show="empty" description="暂无课程"></el-empty>
        </div>
      </div>

    </el-main>
  </el-container>
</template>

<script>
  import AdminSidebar from './side/AdminSidebar'
  export default {
    name: "Courses",
    components:{
      AdminSidebar
    },
    data(){
      return{
        rules: {
          courseID: [{required: true, message: '', trigger: 'change'}],
          classID: [{required: true, message: '', trigger: 'change'}],
          teacherID:[{required: true, message: '', trigger: 'change'}],
        },
        show:false,
        empty:false,
        courses:[{
          id:'',
          number:'',
          courseID:'',
          teacherID:'',
          teacherName:'',
          classID:'',
          courseName:''
        }],
        classA:[{
          id:'',
          grade:'',
          classNum:'',
          className:''
        }],
        teacher:[{
          id:'',
          username:'',
          name:''
        }],
        course:[{
          id:'',
          name:''
        }],
        form:{
          courseID:'',
          classID:'',
          teacherID:'',
        },
        courseIDSearch:'',
        classIDSearch:'',
        teacherIDSearch:'',
        loading: false,
        menuIndex:2,
      }
    },
    created() {
      const loading = this.$loading({
        lock: true,
      });
      localStorage.setItem('menuIndex',2);
      this.$axios
        .get("/section")
        .then((res) => {
          loading.close();
          console.log(res.data);

          this.courses = res.data;
          if (this.courses.length ===0){
            this.empty=true;
          } else {
            this.empty=false;
          }
        })
        .catch((error) => {
          loading.close();
          console.log(error);
          this.$message.error("获取课程请求失败");
        });
      this.get3();
    },
    methods:{

      deleteCourse(courseID){
        const loading = this.$loading({
          lock: true,
        });
        console.log(courseID);
        this.$axios
          .get("/section/delete",{
            params:{
              sectionID: courseID
            }
          })
          .then((res) => {
            if(res.status === 200 && res.data.message === "success") {
              loading.close();
              console.log(res.data);
              location.reload();
              this.$message.success("删除成功");
            }else{
              this.$message.error(res.data.message);
            }
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("删除失败");
          });
      },
      addCourse(){
        const loading = this.$loading({
          lock: true,
        });
        this.$axios
          .post("/section/add",{
            courseID:this.form.courseID,
            classID:this.form.classID,
            teacherID:this.form.teacherID
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.show = false;
            this.form.courseID='';
            this.form.classID='';
            this.form.teacherID='';
            location.reload();
            this.$message.success("新增成功");
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("新增失败");
          });
      },
      searchCourse(){
        const loading = this.$loading({
          lock: true,
        });
        this.$axios
          .post("/section/search",{
            courseID:this.form.courseID,
            classID:this.form.classID,
            teacherID:this.form.teacherID
          })
          .then((res) => {
            loading.close();
            console.log("search"+res.data);
            this.courses = res.data;
            this.form.courseID='';
            this.form.classID='';
            this.form.teacherID='';
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("搜索失败");
          });
      },
      addC(){
        this.show=true;
        console.log(this.show);
        const loading = this.$loading({
          lock: true,
        });
        this.get3();
      },
      get3(){
        const loading = this.$loading({
          lock: true,
        });
        this.$axios
          .get("/classes")
          .then((res) => {
            loading.close();
            console.log("class:"+res.data.classes);
            this.classA = res.data.classes;
            for(var i=0;i<this.classA.length;i++){
              this.classA[i].className=this.classA[i].grade+"年级"+this.classA[i].classNum+"班";
            }
            //console.log(this.classA)
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取班级请求失败");
          });

        this.$axios
          .get("/course")
          .then((res) => {
            loading.close();
            this.course = res.data;
            console.log(res.data)
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取课程请求失败");
          });
        this.$axios
          .get("/teacher/all")
          .then((res) => {
            loading.close();
            this.teacher = res.data.teachers;
            console.log("12]]]]]"+ res.data.teachers)
            console.log("]]]]]"+this.teacher)
          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取教师请求失败");
          });
      },
      goBack() {
        this.$router.go(-1);
      },
      beforeHandleCommand(object, command) {
        return {
          object: object,
          command: command,
        };
      },
      handleCommand(command) {
        switch (command.command) {

          case "delete":
            this.deleteCourse(command.object.id);
            break;
        }
      }
    }
  }
</script>

<style scoped>
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
