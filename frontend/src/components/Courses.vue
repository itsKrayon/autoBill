<template>
  <el-container>
    <el-header>
      <el-page-header @back="goBack" style="float:left;width: 400px"></el-page-header>
      <UserSidebar v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></UserSidebar>
    </el-header>
    <el-main style="">
      <div>
        <!--
        <div style="">
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
              label="操作"
              width="">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button @click="getCourse(scope.row.id)" plain>详情</el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
        </div>
        -->
        <div class="card_wrapper">
          <el-card
            class="card_item"
            v-for="i in courses"
            :key="i.id"
            :body-style="{ padding: '0px' }"
            @click.native="getCourse(i.id)"
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
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div class="card_body">
              <div>课程：{{ i.courseName }}</div>
              <div>老师：{{i.teacherName}}</div>
            </div>
          </el-card>
        </div>


        <el-empty v-show="empty" description="暂无课程"></el-empty>
      </div>

    </el-main>
  </el-container>
</template>

<script>
  import UserSidebar from './side/UserSidebar'
    export default {
        name: "Courses",
      components:{
        UserSidebar
      },
      data(){
          return{
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
            menuIndex:2
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
      },
      methods:{
        getCourse(id){
          console.log(id);
          localStorage.setItem('sectionID', id);
          this.$router.push({path: '/course'});
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
            case "toClassDetail":
              this.getCourse(command.object.id);
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
